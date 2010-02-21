/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.parser.lookup;

import java.lang.reflect.Method;

import fitlibrary.parser.HtmlParser;
import fitlibrary.parser.Parser;
import fitlibrary.table.ICell;
import fitlibrary.traverse.Evaluator;
import fitlibrary.typed.TypedObject;
import fitlibrary.utility.TestResults;

public class GetterParser implements ResultParser {
	private Parser parser;
	private Object objectToCall;
	private Method method;

	public GetterParser(Parser parser, Method method) {
		this.parser = parser;
		this.method = method;
	}
	public TypedObject parseTyped(ICell cell, TestResults testResults) throws Exception {
		return parser.parseTyped(cell,testResults);
	}
	public boolean matches(ICell cell, Object result, TestResults testResults) throws Exception {
		return parser.matches(cell,result,testResults);
	}
	public String show(Object result) throws Exception {
		if (result == null)
			return "";
		return parser.show(result);
	}
	public void setTarget(Object element) {
		this.objectToCall = element;
	}
	public Object getResult() throws Exception {
		return method.invoke(objectToCall, new Object[]{});
	}
	public boolean isShowAsHtml() {
		return parser instanceof HtmlParser;
	}
	public Evaluator traverse(TypedObject typedObject) {
		return parser.traverse(typedObject);
	}
}
