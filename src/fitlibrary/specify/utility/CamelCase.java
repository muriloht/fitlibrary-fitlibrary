/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.utility;
import fitlibrary.object.DomainFixtured;
import fitlibrary.utility.ExtendedCamelCase;

public class CamelCase implements DomainFixtured {
	public String identifierName(String name) {
        return ExtendedCamelCase.camel(name);
	}
}
