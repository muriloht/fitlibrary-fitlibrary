/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.arrayParser;

import fitlibrary.object.DomainFixtured;

public class Parse implements DomainFixtured {
	public int[] givenInts(int[] array) {
		return array;
	}
	public int[][] givenInts2D(int[][] array2D) {
		return array2D;
	}
	public String[] givenStrings(String[] array) {
		return array;
	}
}
