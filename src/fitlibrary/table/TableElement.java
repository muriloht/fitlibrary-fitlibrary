/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.table;

public interface TableElement<From, To> extends Iterable<To> {
	int size();
	boolean isEmpty();
	To elementAt(int i);
	void add(To t);
	From deepCopy();
}
