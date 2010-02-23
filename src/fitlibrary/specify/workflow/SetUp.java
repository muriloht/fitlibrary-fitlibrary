/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.workflow;

import fitlibrary.object.DomainFixtured;
import fitlibrary.traverse.DomainAdapter;

public class SetUp implements DomainAdapter, DomainFixtured  {
	private boolean setUp = false;

	public void setUp() {
		setUp  = true;
	}
	public boolean isSetUp() {
		return setUp;
	}
	public Object getSystemUnderTest() {
		return null;
	}
}
