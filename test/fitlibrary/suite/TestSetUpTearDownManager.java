/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.suite;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.suite.SetUpTearDownManager.MethodCaller;
import fitlibrary.traverse.DomainAdapter;

@RunWith(JMock.class)
public class TestSetUpTearDownManager {
	Mockery context = new Mockery();
	MethodCaller methodCaller = context.mock(MethodCaller.class);
	SetUpTearDownManager setUpTearDownManager = new SetUpTearDownManager();
	DomainAdapter domainAdapter = context.mock(DomainAdapter.class);

	@Test
	public void setUpNotCalledAsNotDomainObject() {
		final Object object = "someObj";
		setUpTearDownManager.addReferences(object,methodCaller);
	}
	@Test
	public void tearDownNotCalledAsNotDomainObject() {
		final Object object = "someObj";
		setUpTearDownManager.removeReferences(object,methodCaller);
	}
	@Test
	public void setUpCalledWithNewDomainAdapter() {
		context.checking(new Expectations() {{
			oneOf(methodCaller).setUp(domainAdapter);
			allowing(domainAdapter).getSystemUnderTest(); will(returnValue(null));
		}});
		setUpTearDownManager.addReferences(domainAdapter,methodCaller);
	}
	@Test
	public void setUpOnlyCalledOnceWithNewDomainAdapter() {
		context.checking(new Expectations() {{
			oneOf(methodCaller).setUp(domainAdapter);
			allowing(domainAdapter).getSystemUnderTest(); will(returnValue(null));
		}});
		setUpTearDownManager.addReferences(domainAdapter,methodCaller);
		setUpTearDownManager.addReferences(domainAdapter,methodCaller);
	}
	@Test
	public void tearDownCalledWithDomainAdapter() {
		context.checking(new Expectations() {{
			oneOf(methodCaller).setUp(domainAdapter);
			oneOf(methodCaller).tearDown(domainAdapter);
			allowing(domainAdapter).getSystemUnderTest(); will(returnValue(null));
		}});
		setUpTearDownManager.addReferences(domainAdapter,methodCaller);
		setUpTearDownManager.removeReferences(domainAdapter,methodCaller);
	}
	@Test
	public void setUpAndTearDownOnlyCalledOnceWithNewDomainAdapter() {
		context.checking(new Expectations() {{
			oneOf(methodCaller).setUp(domainAdapter);
			oneOf(methodCaller).tearDown(domainAdapter);
			allowing(domainAdapter).getSystemUnderTest(); will(returnValue(null));
		}});
		setUpTearDownManager.addReferences(domainAdapter,methodCaller);
		setUpTearDownManager.addReferences(domainAdapter,methodCaller);
		setUpTearDownManager.removeReferences(domainAdapter,methodCaller);
		setUpTearDownManager.removeReferences(domainAdapter,methodCaller);
	}
}
