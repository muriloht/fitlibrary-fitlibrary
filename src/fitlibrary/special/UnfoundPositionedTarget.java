/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.special;

import org.apache.log4j.Logger;

import fitlibrary.log.FitLibraryLogger;
import fitlibrary.runResults.TestResults;
import fitlibrary.runtime.RuntimeContextInternal;
import fitlibrary.table.Row;
import fitlibrary.typed.TypedObject;

public class UnfoundPositionedTarget implements PositionedTarget {
	private static Logger logger = FitLibraryLogger.getLogger(UnfoundPositionedTarget.class);
	@Override
	public String ambiguityErrorMessage() {
		return null;
	}
	@Override
	public boolean isFound() {
		return false;
	}
	@Override
	public TypedObject run(Row row, TestResults testResults, RuntimeContextInternal runtimeContextInternal) {
		return null;
	}
	@Override
	public String getPartialErrorMessage() {
		return "";
	}
	@Override
	public boolean partiallyValid() {
		return false;
	}
}
