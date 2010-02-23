/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.workflow;

import fitlibrary.object.DomainFixtured;

public class SetUpTearDownOfOther implements DomainFixtured {
    public SetUp withOtherSetUp() {
        return new SetUp();
    }
    public TearDown withOtherTearDown() {
        return new TearDown();
    }
}
