/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.table;

import fitlibrary.traverse.Evaluator;
import fitlibrary.utility.TestResults;

public interface ICell {
	String text();
	String text(Evaluator evaluator);
	String fullText();
	boolean isBlank(Evaluator evaluator);
	boolean hasEmbeddedTable();
	Table getEmbeddedTable();
	
	void passOrFail(TestResults testResults, boolean right);
	void pass(TestResults testResults);
	void pass(TestResults testResults, String msg);
	void passIfNotEmbedded(TestResults testResults);
	void fail(TestResults testResults);
	void fail(TestResults testResults, String show, Evaluator evaluator);
	void failWithStringEquals(TestResults testResults, String show,
			Evaluator evaluator);
	void exceptionMayBeExpected(boolean exceptionExpected, Exception e,
			TestResults testResults);
	void error(TestResults testResults);
	void error(TestResults testResults, Throwable e);
	void ignore(TestResults testResults);
	void unexpected(TestResults testResults, String string);
	void wrongHtml(TestResults testResults, String show);
	
	boolean unresolved(Evaluator evaluator);
	void shown();
	void setUnvisitedEscapedText(String s);
}
