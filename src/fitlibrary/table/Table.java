/*
 * Copyright (c) 2010 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/

package fitlibrary.table;

import fit.Parse;
import fitlibrary.utility.ITableListener;
import fitlibrary.utility.TestResults;

public interface Table extends TableElement<Table,Row> {
	void error(TestResults testResults, Throwable e);
	void error(ITableListener tableListener, Throwable e);
	Parse parse();
	int phaseBoundaryCount();
	Row newRow();
	boolean rowExists(int i);
	void ignore(TestResults testResults);
	void pass(TestResults testResults);
	TablesOnParse getTables();
	void insertTable(int offset, Table table);
	Table withDummyFirstRow();
	void evenUpRows();
	void addFoldingText(String foldingText);
	boolean isPlainTextTable();
	void replaceAt(int r, Row newRow);
	void setLeader(String leader);
	void setTrailer(String trailer);
	String getLeader();
	String getTrailer();
}
