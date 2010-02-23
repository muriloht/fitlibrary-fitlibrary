/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.arrayParser;

import fitlibrary.object.DomainFixtured;

public class Match implements DomainFixtured {
	public int[] getArray123() {
		return new int[] {1,2,3};
	}
	public int[] getArrayEmpty() {
		return new int[] {};
	}
}
