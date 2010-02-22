/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.traverse.workflow.special;

import org.jmock.Mockery;

import fitlibrary.closure.ICalledMethodTarget;
import fitlibrary.table.ICell;
import fitlibrary.table.IRow;
import fitlibrary.utility.TestResults;

public abstract class TestSpecialAction {
	Mockery context = new Mockery();
	SpecialActionContext actionContext = context.mock(SpecialActionContext.class);
	IRow initialRow = context.mock(IRow.class,"initialRow");
	IRow subRow = context.mock(IRow.class,"subRow");
	ICell expectedCell = context.mock(ICell.class,"expected cell");
	ICell firstCell = context.mock(ICell.class,"first cell");
	TestResults testResults = new TestResults();
	PrefixSpecialAction special = new PrefixSpecialAction(actionContext);
	ICalledMethodTarget target = context.mock(ICalledMethodTarget.class);
	
}
