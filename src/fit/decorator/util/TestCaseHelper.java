package fit.decorator.util;

import fit.Counts;
import junit.framework.Assert;

public class TestCaseHelper {
	public static void assertCounts(Counts expected, Counts actual) {
		Assert.assertEquals(expected.wrong, actual.wrong);
		Assert.assertEquals(expected.exceptions, actual.exceptions);
		Assert.assertEquals(expected.ignores, actual.ignores);
		Assert.assertEquals(expected.right, actual.right);
	}

	public static Counts counts(int right, int wrong, int ignores,
			int exceptions) {
		Counts expected = new Counts();
		expected.right = right;
		expected.wrong = wrong;
		expected.ignores = ignores;
		expected.exceptions = exceptions;
		return expected;
	}
}
