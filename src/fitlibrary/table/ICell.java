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
	void pass(TestResults testResults);
	void pass(TestResults testResults, String msg);
	void fail(TestResults testResults);
	void exceptionMayBeExpected(boolean exceptionExpected, Exception e,
			TestResults testResults);
	void error(TestResults testResults);
	boolean unresolved(Evaluator evaluator);
	void wrongHtml(TestResults testResults, String show);
	void passIfNotEmbedded(TestResults testResults);
	boolean hasEmbeddedTable();
	void failWithStringEquals(TestResults testResults, String show,
			Evaluator evaluator);
	void fail(TestResults testResults, String show, Evaluator evaluator);
	void error(TestResults testResults, Throwable e);
	Table getEmbeddedTable();
	boolean isBlank(Evaluator evaluator);
	void unexpected(TestResults testResults, String string);
	String fullText();
	void ignore(TestResults testResults);
	void passOrFail(TestResults testResults, boolean right);
}
