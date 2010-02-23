/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary;

import java.util.Arrays;
import java.util.List;

import fitlibrary.traverse.workflow.DoTraverse;

public class Configuration extends DoTraverse {
	public enum ConfigAutoWrapPojo { WITH_DO_FIXURE, WITH_DOMAIN_FIXURE }
	public static ConfigAutoWrapPojo configAutoWrapPojo = 
		ConfigAutoWrapPojo.WITH_DO_FIXURE;
	private final static String[] methodsThatAreVisibleAsActions = {
		"autoWrapPojoWithDomainFixture/0", "autoWrapPojoWithDoFixture/0"
	};
	
	public boolean autoWrapPojoWithDomainFixture() {
		configAutoWrapPojo = ConfigAutoWrapPojo.WITH_DOMAIN_FIXURE;
		return true;
	}
	public boolean autoWrapPojoWithDoFixture() {
		configAutoWrapPojo = ConfigAutoWrapPojo.WITH_DO_FIXURE;
		return true;
	}
	@Override
	public List<String> methodsThatAreVisible() {
		return Arrays.asList(methodsThatAreVisibleAsActions);
	}
}
