/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary;

import fitlibrary.object.DomainTraverse;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Row;

public class DomainFixture extends DoFixture {
    private DomainTraverse domainTraverse = new DomainTraverse(this);
    
    public DomainFixture() {
    	setTraverse(domainTraverse);
    }
    public DomainFixture(Object sut) {
    	this();
    	setSystemUnderTest(sut);
    }
}
