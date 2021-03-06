/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
 * Written: 21/09/2006
*/

package fitlibrary.specify.domain;

import fitlibrary.object.DomainFixtured;
import fitlibrary.traverse.DomainAdapter;

public class BadClassFromClassFactoryMethodForInterface implements DomainAdapter, DomainFixtured  {
	public void setAbstractUser(@SuppressWarnings("unused") AbstractUser user) {
		//
	}
	public interface AbstractUser {
		//
	}
	public static class PrivateUser implements AbstractUser {
		private String name;
		
		private PrivateUser() {
			//
		}
		@SuppressWarnings("unused")
		private String getName() {
			return name;
		}
		@SuppressWarnings("unused")
		private void setName(String name) {
			this.name = name;
		}
	}
	public static class NoNullaryUser implements AbstractUser {
		public NoNullaryUser(@SuppressWarnings("unused") int i) {
			//
		}
	}
	public Class<?> concreteClassOfAbstractUser(String typeName) {
		if ("Private".equals(typeName))
			return PrivateUser.class;
		if ("No Nullary".equals(typeName))
			return NoNullaryUser.class;
		if ("String".equals(typeName))
			return String.class;
		return null;
	}
	@Override
	public Object getSystemUnderTest() {
		return null;
	}

}
