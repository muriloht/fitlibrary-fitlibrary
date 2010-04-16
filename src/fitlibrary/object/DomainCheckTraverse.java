/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.object;

import fitlibrary.closure.CalledMethodTarget;
import fitlibrary.exception.NoSystemUnderTestException;
import fitlibrary.exception.method.MissingMethodException;
import fitlibrary.global.PlugBoard;
import fitlibrary.table.Cell;
import fitlibrary.table.Row;
import fitlibrary.table.RowOnParse;
import fitlibrary.table.Table;
import fitlibrary.table.TableFactory;
import fitlibrary.traverse.TableEvaluator;
import fitlibrary.traverse.Traverse;
import fitlibrary.typed.TypedObject;
import fitlibrary.utility.ITableListener;
import fitlibrary.utility.TestResults;

public class DomainCheckTraverse extends Traverse implements TableEvaluator {
	private DomainTraverse domainTraverse;
	
	public DomainCheckTraverse() {
		//
	}
    public DomainCheckTraverse(Object sut) {
		super(sut);
	}
    @Override
	public Object interpretAfterFirstRow(Table table, TestResults testResults) {
        table.error(testResults,new RuntimeException("Don't expect to have this called!"));
        return null;
    }
	public void setDomainTraverse(DomainTraverse domainTraverse) {
		this.domainTraverse = domainTraverse;
	}
	public void runTable(Table table, ITableListener tableListener) {
        if (switchOnExpected(table)) {
            domainTraverse.setCurrentAction();
            return;
        }
        try {
            for (int rowNo = 0; rowNo < table.size(); rowNo++) {
                Row row = table.elementAt(rowNo);
                if (row.text(0,this).equals("comment"))
                	return;
				processRow(row,tableListener.getTestResults());
            }
        } catch (Exception e) {
            table.error(tableListener,e);
        }
    }
	private void processRow(Row row, TestResults testResults) {
		for (int i = 0; i < row.size(); i += 2) {
			Cell cell = row.elementAt(i);
			Cell cell2 = row.elementAt(i+1);
			if (DomainObjectSetUpTraverse.givesClass(cell,this)) {
				if (getSystemUnderTest() == null)
					throw new NoSystemUnderTestException();
				checkClass(testResults, cell, cell2);
//				ClassMethodTarget target = new ClassMethodTarget(getSystemUnderTest());
//				target.invokeAndCheckCell(cell2,true,testResults);
			} else {
				try {
			    	CalledMethodTarget target =PlugBoard.lookupTarget.findGetterOnSut(cell.text(this),this);
			    	target.invokeAndCheck(TableFactory.row(),cell2,testResults,false);
				} catch (MissingMethodException ex) {
					cell.error(testResults,ex);
				}
			}
		}
	}
	private void checkClass(TestResults testResults, Cell cell, Cell classCell) {
		String typeName = classCell.text(this);
		try {
			Class<?> sutClass = PlugBoard.lookupTarget.findClassFromFactoryMethod(this, getTypedSystemUnderTest().getClassType(), typeName);
			if (getSystemUnderTest().getClass().equals(sutClass))
				classCell.pass(testResults);
			else
				classCell.fail(testResults);
		} catch (Exception e) {
			cell.error(testResults,e);
		}
	}
    private boolean switchOnExpected(Table table) {
        return domainTraverse != null && table.size() == 1 && 
                table.elementAt(0).size() == 1 && 
                table.elementAt(0).elementAt(0).matchesText("expected",this);
    }
	@Override
	public void addNamedObject(String text, TypedObject typedObject, Row row, TestResults testResults) {
		// TODO Auto-generated method stub
		// Remove this later
	}
	@Override
	public void select(String name) {
		// TODO Auto-generated method stub
		// Remove this later
	}
}
