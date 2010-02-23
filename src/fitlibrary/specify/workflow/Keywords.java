/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.workflow;

import fitlibrary.object.DomainFixtured;

public class Keywords implements DomainFixtured {
	private double sum = 0.0;

	public void buyAtDollarWithDiscountPercent(int count, double cost, int discountPercent) {
		sum  += count * cost * (100-discountPercent)/100;
	}
	public double totalOwingDollar() {
		return sum;
	}
}
