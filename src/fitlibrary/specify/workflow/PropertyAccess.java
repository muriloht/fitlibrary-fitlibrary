/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.workflow;

import fitlibrary.object.DomainFixtured;

public class PropertyAccess implements DomainFixtured {
	public boolean isTrueProperty() {
		return true;
	}
	public int getIntPropertyWith3() {
		return 3;
	}
}
