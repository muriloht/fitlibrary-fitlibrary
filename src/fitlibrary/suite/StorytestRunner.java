/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.suite;

import fitlibrary.table.Tables;
import fitlibrary.utility.TestResults;

public interface StorytestRunner {
	TestResults doStorytest(Tables actualTables);
}
