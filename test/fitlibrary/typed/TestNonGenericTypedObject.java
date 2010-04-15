/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.typed;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import fitlibrary.closure.Closure;
import fitlibrary.closure.LookupClosure;
import fitlibrary.traverse.Evaluator;
import fitlibrary.typed.NonGenericTypedObject.MethodTargetFactory;

@RunWith(JMock.class)
public class TestNonGenericTypedObject {
	final Mockery context = new Mockery();
	final Subject subject = context.mock(Subject.class);
	final Evaluator evaluator = context.mock(Evaluator.class);
	final LookupClosure lookupClosure = context.mock(LookupClosure.class);
	final Closure closure = context.mock(Closure.class);
	final MethodTargetFactory methodTargetFactory = context.mock(MethodTargetFactory.class);
	final NonGenericTypedObject typedObject = new NonGenericTypedObject(subject,lookupClosure,methodTargetFactory);

	@Test
	public void methodDoesNotExist() throws Exception {
		context.checking(new Expectations() {{
			oneOf(lookupClosure).findMethodClosure(typedObject,"unknown",0); will(returnValue(null));
		}});
		assertThat(typedObject.new_findSpecificMethod("unknown",0,evaluator).isNone(),
				is(true));
	}
	@Test
	public void methodExistsOnEvaluator() throws Exception {
		verifyMethodOnEvaluator("m");
	}
	private void verifyMethodOnEvaluator(final String methodName) throws Exception {
		context.checking(new Expectations() {{
			oneOf(methodTargetFactory).createCalledMethodTarget(closure, evaluator);
			oneOf(lookupClosure).findMethodClosure(typedObject, methodName, 0); will(returnValue(closure));
		}});
		assertThat(typedObject.new_findSpecificMethod(methodName,0,evaluator).isSome(),
				is(true));
	}
	
	interface Subject extends Evaluator {
		void m();
	}
	interface Sut {
		void m();
	}
}
