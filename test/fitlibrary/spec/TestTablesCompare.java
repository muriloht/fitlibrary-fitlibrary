/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.spec;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JMock.class)
public class TestTablesCompare {
	final Mockery context = new Mockery();
	final SpecifyErrorReport errorReport = context.mock(SpecifyErrorReport.class);
	final TablesCompare tablesCompare = new TablesCompare(errorReport);

	@Test public void emptyStringEquals() {
		assertThat(tablesCompare.equals("", ""),is(true));
	}
	@Test public void stringEqualsIgnoringWhiteSpace() {
		assertThat(tablesCompare.equals("ab", "ab "),is(true));
	}
	@Test public void someTagsAndCharactersAreIgnored() {
		assertThat(tablesCompare.equals("\t\r<hr><hr/><br><br/>ab", "ab "),is(true));
	}
	@Test public void ignore() {
		assertThat(tablesCompare.equals("ab", "IGNORE"),is(true));
	}
	@Test
	public void fitLabelContentsOfActualCanBeLonger() {
		assertThat(tablesCompare.equals(
				"ab <span class=\"fit_label\">XYZ</span>cd", 
				"ab <span class=\"fit_label\">X</span>cd"),is(true));
	}
	@Test
	public void fitLabelContentsOfActualCanBeLongerButMustMatch() {
		assertThat(tablesCompare.equals(
				"ab <span class=\"fit_label\">XYZ</span>", 
				"ab <span class=\"fit_label\">Y</span>"),is(false));
	}
	@Test
	public void prefixOfFitLabelMustMatch() {
		assertThat(tablesCompare.equals(
				"ab <span class=\"fit_label\">XYZ</span>", 
				"aB <span class=\"fit_label\">XYZ</span>"),is(false));
	}
	@Test
	public void suffixOfFitLabelMustMatch() {
		assertThat(tablesCompare.equals(
				"ab <span class=\"fit_label\">XYZ</span>YZ", 
				"ab <span class=\"fit_label\">Y</span>YYZ"),is(false));
	}
	@Test
	public void severalFitLabelsMatch() {
		assertThat(tablesCompare.equals(
				"ab <span class=\"fit_label\">XYZ</span>cd<span class=\"fit_label\">XYZ</span>ef", 
				"ab <span class=\"fit_label\">X</span>cd<span class=\"fit_label\">XY</span>ef"),is(true));
	}
}
