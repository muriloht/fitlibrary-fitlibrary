/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
 */

package fitlibrary.traverse.function;

import java.util.ArrayList;
import java.util.List;

import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.exception.IgnoredException;
import fitlibrary.exception.method.VoidMethodException;
import fitlibrary.global.PlugBoard;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.Table;
import fitlibrary.traverse.Traverse;
import fitlibrary.utility.ExtendedCamelCase;
import fitlibrary.utility.option.None;
import fitlibrary.utility.option.Option;
import fitlibrary.utility.option.Some;

public class RuleTable extends Traverse {
	private List<ColumnTarget> columnTargets = new ArrayList<ColumnTarget>();
	private boolean hasErrors = false;
	private Option<CalledMethodTarget> executeMethod = None.none();
	private Option<CalledMethodTarget> resetMethod =  None.none();

	public RuleTable(Object sut) {
		super(sut);
	}
	@Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
		try {
			basicCheck(table, testResults);
			header(table, testResults);
			optionalfunctions();
			if (!hasErrors)
				body(table, testResults);
		} catch (IgnoredException e) {
			//
		} catch (Exception e) {
			table.elementAt(1).error(testResults, e);
		}
		return null;
	}
	private void header(Table table, TestResults testResults) {
		Row headerRow = table.elementAt(1);
		for (Cell cell : headerRow) {
			try {
				String name = cell.text(this);
				boolean input = true;
				if (name.endsWith("?")) {
					input = false;
					name = name.substring(0, name.length() - 1);
				}
				String fn = ExtendedCamelCase.camel(name);
				if (input)
					columnTargets.add(new InputColumnTarget(PlugBoard.lookupTarget.findSetterOnSut(fn, this)));
				else {
					CalledMethodTarget target = PlugBoard.lookupTarget.findGetterOnSut(fn,this);
					if (target.returnsVoid())
						throw new VoidMethodException(fn,"RuleTable");
					columnTargets.add(new OutputColumnTarget(target));
				}
			} catch (Exception e) {
				cell.error(testResults, e);
				hasErrors = true;
			}
		}
	}
	private void body(Table table, TestResults testResults) {
		for (int r = 2; r < table.size(); r++) {
			Row row = table.elementAt(r);
			try {
				if (resetMethod.isSome())
					resetMethod.get().invoke();
				row(testResults, row);
			} catch (Exception e) {
				row.error(testResults, e);
			}
		}
	}
	private void row(TestResults testResults, Row row) throws Exception {
		boolean haveCalledExecuteForThisRow = executeMethod.isNone();
		for (int i = 0; i < row.size(); i++) {
			Cell cell = row.elementAt(i);
			try {
				ColumnTarget columnTarget = columnTargets.get(i);
				if (!haveCalledExecuteForThisRow && columnTarget.isOutput()) {
					executeMethod.get().invoke();
					haveCalledExecuteForThisRow = true;
				}
				columnTarget.act(cell, testResults);
			} catch (Exception e) {
				cell.error(testResults, e);
				return;
			}
		}
	}
	private void basicCheck(Table table, TestResults testResults) {
		int width = table.elementAt(1).size();
		for (int r = 2; r < table.size(); r++) {
			Row row = table.elementAt(r);
			if (width != row.size()) {
				row.elementAt(0).error(testResults,"Irregular shaped: This row differs in width from the header");
				throw new IgnoredException();
			}
		}
	}
	private void optionalfunctions() {
		try {
			resetMethod = new Some<CalledMethodTarget>(PlugBoard.lookupTarget.findTheMethodMapped("reset", 0, this));
		} catch (Exception e) {
			// Do nothing, it's optional
		}
		try {
			executeMethod = new Some<CalledMethodTarget>(PlugBoard.lookupTarget.findTheMethodMapped("execute", 0, this));
		} catch (Exception e) {
			// Do nothing, it's optional
		}
	}
	static abstract class ColumnTarget {
		protected CalledMethodTarget target;

		public ColumnTarget(CalledMethodTarget target) {
			this.target = target;
		}
		public abstract boolean isOutput();
		public abstract void act(Cell cell, TestResults testResults) throws Exception;
	}
	static class InputColumnTarget extends ColumnTarget {
		public InputColumnTarget(CalledMethodTarget target) {
			super(target);
		}
		@Override
		public void act(Cell cell, TestResults testResults) throws Exception {
			target.invoke(cell,testResults);
		}
		@Override
		public boolean isOutput() {
			return false;
		}
	}
	static class OutputColumnTarget extends ColumnTarget{
		public OutputColumnTarget(CalledMethodTarget target) {
			super(target);
		}
		@Override
		public void act(Cell cell, TestResults testResults) throws Exception {
			target.invokeAndCheckCell(cell,true,testResults);
		}
		@Override
		public boolean isOutput() {
			return true;
		}
	}
}
