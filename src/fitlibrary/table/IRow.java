/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.table;

import fitlibrary.traverse.Evaluator;
import fitlibrary.utility.TestResults;

public interface IRow { // Temporary name
	String text(int i, Evaluator evaluator);
	void passKeywords(TestResults testResults);
	int size();
	ICell cell(int i);
	IRow addCell(Cell cell);
	IRow rowFrom(int i);
	boolean cellExists(int i);
	ICell last();
	void error(TestResults testResults, Throwable e);
	void shown();
}
