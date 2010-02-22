/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.table;

import fit.Parse;
import fitlibrary.traverse.Evaluator;
import fitlibrary.utility.TestResults;

public interface IRow { // Temporary name
	int size();
	ICell last();
	ICell cell(int i);
	boolean cellExists(int i);
	String text(int i, Evaluator evaluator);
	void passKeywords(TestResults testResults);
	IRow addCell(Cell cell);
	IRow rowFrom(int i);
	void error(TestResults testResults, Throwable e);
	void shown();
	ICell addCell(String s);
	IRow copy();
	Parse parse();
}
