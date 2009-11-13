/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.traverse.workflow;

import java.util.ArrayList;
import java.util.List;

import fit.Counts;
import fit.Fixture;
import fit.Parse;
import fitlibrary.DoFixture;
import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.collection.CollectionSetUpTraverse;
import fitlibrary.exception.AbandonException;
import fitlibrary.exception.FitLibraryException;
import fitlibrary.exception.IgnoredException;
import fitlibrary.exception.method.AmbiguousActionException;
import fitlibrary.exception.method.AmbiguousNameException;
import fitlibrary.exception.method.MissingMethodException;
import fitlibrary.global.PlugBoard;
import fitlibrary.suite.InFlowPageRunner;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.table.Tables;
import fitlibrary.traverse.Evaluator;
import fitlibrary.traverse.Traverse;
import fitlibrary.traverse.workflow.caller.ActionCaller;
import fitlibrary.traverse.workflow.caller.DefinedActionCaller;
import fitlibrary.traverse.workflow.caller.FixtureCaller;
import fitlibrary.traverse.workflow.caller.MultiDefinedActionCaller;
import fitlibrary.traverse.workflow.caller.PostFixSpecialCaller;
import fitlibrary.traverse.workflow.caller.SpecialCaller;
import fitlibrary.traverse.workflow.caller.ValidCall;
import fitlibrary.typed.TypedObject;
import fitlibrary.utility.TableListener;
import fitlibrary.utility.TestResults;

public abstract class DoTraverseInterpreter extends Traverse implements DoEvaluator {
	private static final String EXPAND_DEFINED_ACTIONS = "$$expandDefinedActions$$";
	protected boolean gatherExpectedForGeneration;
	protected Object expectedResult = new Boolean(true); // Used for UI code generation
	private CollectionSetUpTraverse setUpTraverse = null; // delegate for setup phase
	private boolean settingUp = true;
	
	public DoTraverseInterpreter() {
		//
	}
	public DoTraverseInterpreter(Object sut) {
		super(sut);
	}
	public DoTraverseInterpreter(TypedObject typedObject) {
		super(typedObject);
	}
	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		Object result = null;
		for (int rowNo = 1; rowNo < table.size(); rowNo++) {
			Row row = table.row(rowNo);
			if (testResults.isAbandoned())
				row.ignore(testResults);
			else
				try {
					result = interpretRow(row,testResults,null);
					if (result instanceof DoEvaluator) {
						DoEvaluator resultingEvaluator = (DoEvaluator)result;
						resultingEvaluator.setRuntimeContext(runtimeContext);
						resultingEvaluator.setUp(table, testResults);
						resultingEvaluator.interpretInFlow(new Table(row),testResults);
						resultingEvaluator.tearDown(table, testResults);
						break;
					} else if (result instanceof Evaluator) {
						interpretEvaluator((Evaluator)result,new Table(row),testResults);
						break;
					} else if (getAlienTraverseHandler().isAlienTraverse(result)) {
						getAlienTraverseHandler().doTable(result, new Table(row),testResults,this);
						break;
					}
				} catch (Exception ex) {
					row.error(testResults,ex);
				}
		}
		return result;
	}
	// overridden in DomainTraverse (with quite separate code)
    public Object interpretWholeTable(Table table, TableListener tableListener) {
    	return interpretWholeTable(table, tableListener.getTestResults());
	}
	public Object interpretWholeTable(Table table, TestResults testResults) {
		try {
			Fixture fixtureByName = fixtureOrDoTraverseByName(table,testResults);
			if (fixtureByName != null && fixtureByName.getClass() == Fixture.class)
				fixtureByName = null;
			Object result = interpretRow(table.row(0),testResults,fixtureByName);
			if (testResults.isAbandoned()) {
				interpretInFlow(table,testResults);
				return null;
			} if (result instanceof Evaluator) {
				interpretEvaluator((Evaluator)result, table, testResults);
				return result;
			} else if (getAlienTraverseHandler().isAlienTraverse(result)) {
				getAlienTraverseHandler().doTable(result,table,testResults,this);
			} else // do the rest of the table with this traverse
				return interpretInFlow(table,testResults);
		} catch (Throwable e) {
            table.error(testResults,e);
		}
		return null;
	}
	private void interpretEvaluator(Evaluator evaluator, Table table, TestResults testResults) {
		evaluator.setRuntimeContext(runtimeContext);
		evaluator.setUp(table,testResults);
		evaluator.interpretAfterFirstRow(table,testResults);
		evaluator.tearDown(table,testResults);
	}
	protected Fixture fixtureOrDoTraverseByName(Table table, TestResults testResults) {
        String className = table.row(0).text(0,this).trim();
		if (className.equals(""))
            return null;
		try {
			return new OpenFixture(testResults.getCounts()).getLinkedFixtureWithArgs(table.parse);
		} catch (Throwable e) {
			if (table.row(0).size() == 1) {
				try {
					Object traverse = Class.forName(className).newInstance();
					if (traverse instanceof DoTraverse) {
						DoFixture doFixture = new DoFixture();
						doFixture.setTraverse((DoTraverse) traverse);
						return doFixture;
					}
				} catch (Throwable e1) {
					//
				}
			}
			return null;
		}
	}
	static class OpenFixture extends Fixture {
		public OpenFixture(Counts counts) {
			this.counts = counts;
		}
		@Override
		public Fixture getLinkedFixtureWithArgs(Parse tables) throws Throwable {
			return super.getLinkedFixtureWithArgs(tables);
		}
	}
    // @Overridden in CollectionSetUpTraverse
    public Object interpretInFlow(Table table, TestResults testResults) {
    	return interpretAfterFirstRow(table,testResults);
    }
    public Object interpretRow(Row row, TestResults testResults, Fixture fixtureByName) {
    	if (testResults.isAbandoned()) {
			row.ignore(testResults);
			return null;
		}
    	final Cell cell = row.cell(0);
    	if (cell.hasEmbeddedTable()) {
    		setExpectedResult(null);
    		interpretInnerTables(cell.innerTables(),testResults);
    		return null;
    	}
    	setExpectedResult(new Boolean(true));
    	try {
    		DoCaller[] actions = { 
    				new DefinedActionCaller(row, this),
    				new MultiDefinedActionCaller(row, this),
    				new SpecialCaller(row,switchSetUp()),
    				new PostFixSpecialCaller(row,switchSetUp()),
    				new FixtureCaller(fixtureByName),
    				new ActionCaller(row,switchSetUp()) };
			checkForAmbiguity(actions);
			for (int i = 0; i < actions.length; i++)
				if (actions[i].isValid()) {
					Object result = actions[i].run(row, testResults);
					if (testResults.isAbandoned() && !testResults.problems())
						row.ignore(testResults);
					return result;
				}
			methodsAreMissing(actions,row.text(0, this));
    	} catch (IgnoredException ex) {
    		//
    	} catch (AbandonException e) {
    		row.ignore(testResults);
    	} catch (Exception ex) {
    		row.error(testResults, ex);
    	}
    	return null;
    }
	public CalledMethodTarget findMethodFromRow(final Row row, int from, int less) throws Exception {
		return findMethodByActionName(row.rowFrom(from), row.size() - less);
	}
	public void findMethodsFromPlainText(String textCall, List<ValidCall> results) {
		asTypedObject(this).findMethodsFromPlainText(textCall,results);
	}
	/** Is overridden in subclass SequenceTraverse to process arguments differently
	 */
	public CalledMethodTarget findMethodByActionName(Row row, int allArgs) throws Exception {
		return PlugBoard.lookupTarget.findMethodInEverySecondCell(this, row, allArgs);
	}
	private static void checkForAmbiguity(DoCaller[] actions) {
		final String AND = " AND ";
		String message = "";
		List<String> valid = new ArrayList<String>();
		for (int i = 0; i < actions.length; i++) {
			if (actions[i].isValid()) {
				String ambiguityErrorMessage = actions[i].ambiguityErrorMessage();
				valid.add(ambiguityErrorMessage);
				message += AND+ambiguityErrorMessage;
			}
		}
		if (valid.size() > 1)
			throw new AmbiguousActionException(message.substring(AND.length()));
	}
	private void methodsAreMissing(DoCaller[] actions, String possibleFixtureName) {
		// It would be better to pass all these exceptions on in a wrapper exception.
		// Then they can be sorted and organised into <hr> lines in the cell.
		final String OR = " OR: ";
		String missingMethods = "";
		String missingAt = "";
		String ambiguousMethods = "";
		for (int i = 0; i < actions.length; i++)
			if (actions[i].isProblem()) {
				Exception exception = actions[i].problem();
				if (exception instanceof MissingMethodException) {
					MissingMethodException missingMethodException = (MissingMethodException) exception;
					missingMethods += OR+missingMethodException.getMethodSignature();
					missingAt = missingMethodException.getClasses();
				} else if (exception instanceof AmbiguousNameException) {
					AmbiguousNameException ambiguousNameException = (AmbiguousNameException) exception;
					ambiguousMethods += OR+ambiguousNameException.getMessage();
				} else if (exception instanceof ClassNotFoundException) {
					ClassNotFoundException cnf = (ClassNotFoundException) exception;
					if (cnf.getCause() != null) {
						System.out.println("methodsAreMissing(): CNFE: "+exception.getMessage()+": "+cnf.getCause().getMessage());
					} else
						System.out.println("methodsAreMissing(): CNFE: "+exception.getMessage());
					missingMethods += OR+exception.getMessage();
				} else
					missingMethods += OR+exception.getMessage();
			}
		String message = "";
		if (possibleFixtureName.contains("."))
			message += "Missing class or ";
		if (!"".equals(missingMethods))
			message += "Missing methods: "+missingMethods.substring(OR.length());
		if (!"".equals(ambiguousMethods))
			message += " "+ambiguousMethods.substring(OR.length());
		if (!"".equals(missingAt))
			message += " in "+missingAt;
		throw new FitLibraryException(message.trim());
	}
	private void interpretInnerTables(Tables tables, TestResults testResults) {
		new InFlowPageRunner(this,testResults).run(tables,0,new TableListener(testResults),true);
	}
	public void setSetUpTraverse(CollectionSetUpTraverse setUpTraverse) {
		this.setUpTraverse = setUpTraverse;
		setUpTraverse.setOuterContext(this);
	}
	public void setSetUpTraverse(Object object) {
		setSetUpTraverse(new CollectionSetUpTraverse(object));
	}
	public void setSettingUp(boolean settingUp) {
		this.settingUp = settingUp;
	}
	public DoTraverseInterpreter switchSetUp() {
		if (settingUp && setUpTraverse != null)
			return setUpTraverse;
		return this;
	}
	public void finishSettingUp() {
		setSettingUp(false);
	}
	public void setExpectedResult(Object expectedResult) {
		this.expectedResult = expectedResult;
	}
	public Object getExpectedResult() {
		return expectedResult;
	}
	protected Object callMethodInRow(Row row, TestResults testResults, boolean catchError, Cell operatorCell) throws Exception {
		return findMethodFromRow(row,1, 2).invokeForSpecial(row.rowFrom(2),testResults,catchError,operatorCell);
	}
	protected CalledMethodTarget findSpecialMethod(String name) {
		return PlugBoard.lookupTarget.findSpecialMethod(this, name);
	}
	public void setGatherExpectedForGeneration(boolean gatherExpectedForGeneration) {
		this.gatherExpectedForGeneration = gatherExpectedForGeneration;
	}
	public boolean toExpandDefinedActions() {
		return "true".equals(getDynamicVariable(EXPAND_DEFINED_ACTIONS));
	}
	public void setExpandDefinedActions(boolean expandDefinedActions) {
		setDynamicVariable(EXPAND_DEFINED_ACTIONS, ""+expandDefinedActions);
	}
}
