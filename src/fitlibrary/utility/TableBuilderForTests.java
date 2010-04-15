/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.utility;


public class TableBuilderForTests {
//	static class Build <T, ElementBuilder> {
//		final T tables;
//		final List<ElementBuilder> elements = new ArrayList<ElementBuilder>();
//
//		public Build(T tables) {
//			this(tables, new ArrayList<ElementBuilder>());
//		}
//		public Build(T tables, List<ElementBuilder> els) {
//			this.tables = tables;
//			for (ElementBuilder element : els)
//				this.elements.add(element);
//		}
//		public Build<T,ElementBuilder> with(ElementBuilder... els) {
//			List<ElementBuilder> copy = new ArrayList<ElementBuilder>(elements);
//			for (ElementBuilder element : els)
//				copy.add(element);
//			return new Build<T,ElementBuilder>(tables, copy);
//		}
//		public void expect(Mockery context) {
//			context.checking(new Expectations() {{
//				allowing(tables).size(); will(returnValue(elements.size()));
//				for (int i = 0; i < elements.size(); i++) {
//					final int ii = i;
//					allowing(tables).table(i); will(returnValue(elements.get(ii).table));
//				}
//				allowing(tables).last(); will(returnValue(elements.get(elements.size()-1).table));
//			}});
//		}
//	}
//	private void trial() {
//		tables(tables).with(
//				table(table1).with(
//						row(row1).with(
//								cell(cell1),
//								cell(cell2).with(
//										tables(innerTables).with(
//												table(innerTable1).with(
//														row(innerRow1).with(
//																cell(innerCell)
//														)
//												)
//										)
//								)
//						),
//						row(row2)
//				)
//		);
//	}
}
