/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.runtime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fitlibrary.dynamicVariable.DynamicVariables;
import fitlibrary.dynamicVariable.GlobalDynamicVariables;
import fitlibrary.dynamicVariable.LocalDynamicVariables;
import fitlibrary.log.FileLogger;
import fitlibrary.suite.CollectObjectsForMethodLookup;

public class RuntimeContextImplementation implements RuntimeContextInternal {
	private static final String EXPAND_DEFINED_ACTIONS = "$$expandDefinedActions$$";
	private DynamicVariables dynamicVariables = new GlobalDynamicVariables();
	private Map<String,Integer> timeouts = new HashMap<String, Integer>();
	private FileLogger fileLogger = new FileLogger();
	private CollectObjectsForMethodLookup objectCollector;

	public RuntimeContextImplementation() {
		//
	}
	public RuntimeContextImplementation(String[] s) {
		for (int i = 0; i < s.length-1; i += 2)
			dynamicVariables.put(s[i],s[i+1]);
	}
	public RuntimeContextImplementation(DynamicVariables dynamicVariables) {
		this.dynamicVariables = dynamicVariables;
	}
	public RuntimeContextInternal freshCopy() {
		return new RuntimeContextImplementation(new GlobalDynamicVariables(dynamicVariables.top()));
	}
	public DynamicVariables dynamicVariables() {
		return dynamicVariables;
	}
	@Override
	public String toString() {
		return dynamicVariables().toString();
	}
	public void putTimeout(String name, int timeout) {
		timeouts.put(name,timeout);
	}
	public int getTimeout(String name, int defaultTimeout) {
		Integer timeout = timeouts.get(name);
		if (timeout == null)
			return defaultTimeout;
		return timeout;
	}
	public void startLogging(String fileName) {
		fileLogger.start(fileName);
	}
	public void printToLog(String s) throws IOException {
		fileLogger.println(s);
	}
	public void pushLocal() {
		dynamicVariables = new LocalDynamicVariables(dynamicVariables);
	}
	public void popLocal() {
		dynamicVariables = dynamicVariables.popLocal();
	}
	public void setDynamicVariable(String key, Object value) {
		dynamicVariables.put(key, value);
	}
	public Object getDynamicVariable(String key) {
		return dynamicVariables.get(key);
	}
	public boolean toExpandDefinedActions() {
		return "true".equals(getDynamicVariable(EXPAND_DEFINED_ACTIONS));
	}
	public void setExpandDefinedActions(boolean expandDefinedActions) {
		setDynamicVariable(EXPAND_DEFINED_ACTIONS, ""+expandDefinedActions);
	}
	public CollectObjectsForMethodLookup getObjectCollector() {
		return objectCollector;
	}
	public void setObjectCollector(CollectObjectsForMethodLookup collector) {
		this.objectCollector = collector;
	}
}
