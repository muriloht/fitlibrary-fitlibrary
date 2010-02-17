/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.traverse.workflow;

import java.util.ArrayList;
import java.util.List;

import fitlibrary.global.TemporaryPlugBoardForRuntime;
import fitlibrary.table.Row;
import fitlibrary.traverse.workflow.caller.ValidCall;
import fitlibrary.utility.ExtendedCamelCase;
import fitlibrary.utility.TestResults;

public class PlainText {
	private final Row row;
	private final TestResults testResults;
	private final DoTraverse doTraverse;
	private String prefixAction = "";
	private String infixAction = "";
	private String infixPart = "";

	public PlainText(Row row, TestResults testResults, DoTraverse doTraverse) {
		this.row = row;
		this.testResults = testResults;
		this.doTraverse = doTraverse;
	}
	public void analyse() {
		String textCall = row.cell(1).fullText();
		List<ValidCall> results = new ArrayList<ValidCall>();

		findDefinedActionCallsFromPlainText(textCall, results);

		textCall = infixes(prefixes(textCall));
		findProperty("get", textCall, results);
		findProperty("is", textCall, results);
		findMethodsFromPlainText(textCall, results);
		
		row.removeFirstCell();
		if (results.isEmpty()) {
			error("Unknown action");
			return;
		}
		if (results.size() > 1) {
			error("Ambiguous action (see details in logs after table)");
			doTraverse.showAfterTable("Possible action tables:<br/>");
			for (ValidCall call: results)
				call.possibility(doTraverse);
			return;
		}
		row.removeAllCells();
		if (!"".equals(prefixAction)) {
			row.addCell("<b>"+prefixAction+"</b>");
		}
		for (String word : results.get(0).getList())
			row.addCell(word);
		if (!"".equals(infixPart)) {
			row.addCell("<b>"+infixAction+"</b>");
			row.addCell(infixPart);
		}
		doTraverse.interpretRow(row,testResults,null);
	}
	private void findDefinedActionCallsFromPlainText(String textCall, List<ValidCall> results) {
		TemporaryPlugBoardForRuntime.definedActionsRepository().findPlainTextCall(textCall, results);
		
	}
	private void findMethodsFromPlainText(String textCall, List<ValidCall> results) {
		doTraverse.switchSetUp().findMethodsFromPlainText(textCall,results);
	}
	private void error(String message) {
		row.cell(0).error(testResults, message);
	}
	private void findProperty(String prefix, String textCall, List<ValidCall> results) {
		int count = results.size();
		findMethodsFromPlainText(ExtendedCamelCase.camel(prefix+" "+textCall),results);
		if (results.size() > count)
			results.get(results.size()-1).setCall(textCall);
	}
	private String prefixes(String textCallOriginal) {
		String textCall = textCallOriginal;
		String[] prefixes = { "not", "reject", "show", "show after", "ensure"};
		for (String prefix : prefixes) {
			String prefixString = prefix;
			if (textCall.startsWith(prefixString)) {
				textCall = textCall.substring(prefixString.length());
				prefixAction = prefix;
			}
		}
		return textCall;
	}
	private String infixes(String textCallOriginal) {
		String textCall = textCallOriginal;
		String[] infixes = { "is", "is not", "matches", "eventually matches", "does not match", 
				"becomes", "contains", "eventually contains"};
		for (String infix : infixes) {
			String infixString = "*"+infix+"*";
			int pos = textCall.indexOf(infixString);
			if (pos >= 0) {
				infixPart = textCall.substring(pos+infixString.length());
				textCall = textCall.substring(0,pos);
				infixAction = infix;
			}
		}
		return textCall;
	}
}
