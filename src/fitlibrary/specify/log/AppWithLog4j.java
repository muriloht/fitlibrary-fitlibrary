/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.specify.log;

import org.apache.log4j.Logger;

public class AppWithLog4j {
	private static Logger logger = Logger.getLogger(AppWithLog4j.class);
	
	public boolean call() {
		logger.trace("App called");
		return true;
	}
}
