/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.traverse.workflow.caller;

import fit.Fixture;
import fitlibrary.runResults.TestResults;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.DoCaller;
import fitlibrary.typed.TypedObject;
import fitlibraryGeneric.typed.GenericTypedObject;

public class FixtureCaller extends DoCaller {
	private Fixture fixtureByName;

	public FixtureCaller(Fixture fixtureByName) {
		this.fixtureByName = fixtureByName;
	}
	@Override
	public boolean isValid() {
		return fixtureByName != null;
	}
	@Override
	public TypedObject run(Row row, TestResults testResults) throws Exception {
		return new GenericTypedObject(fixtureByName);
	}
	@Override
	public String ambiguityErrorMessage() {
		return "fixture "+fixtureByName.getClass().getName();
	}
}
