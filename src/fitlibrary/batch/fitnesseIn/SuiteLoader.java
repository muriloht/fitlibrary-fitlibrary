/*
 * Copyright (c) 2009 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.batch.fitnesseIn;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Queue;

import fitlibrary.batch.trinidad.InMemoryTestImpl;
import fitlibrary.batch.trinidad.TestDescriptor;
import fitnesse.FitNesseContext;
import fitnesse.testrunner.SuiteContentsFinder;
import fitnesse.testrunner.run.PageListSetUpTearDownSurrounder;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class SuiteLoader implements Runnable {
	private final String name;
	private final Queue<TestDescriptor> queue;
	private final FitNesseContext context;

	public SuiteLoader(String name, Queue<TestDescriptor> queue, FitNesseContext context) {
		this.name = name;
		this.queue = queue;
		this.context = context;
	}
	@Override
	public void run() {
		try {
			WikiPagePath path = PathParser.parse(name);
			PageCrawler crawler = context.getRootPage().getPageCrawler();
			WikiPage suiteRoot = crawler.getPage(path);
			if (!suiteRoot.getData().hasAttribute("Suite")){
				throw new IllegalArgumentException("page "+name+" is not a suite");
			}
			WikiPage root = crawler.getPage(PathParser.parse("."));
			List<WikiPage> pages = new SuiteContentsFinder(suiteRoot, null, root).getAllPagesToRunForThisSuite();
			
			PageListSetUpTearDownSurrounder surrounder = new PageListSetUpTearDownSurrounder();
            surrounder.addSuiteSetUpsAndTearDowns(pages);
            
			for (WikiPage page : pages){
				if (selects(page)){		    		
					String testName = page.getFullPath().toString();
					String content = ParallelFitNesseRepository.formatWikiPage(testName,page,null,null,context);
					queue.add(new InMemoryTestImpl(testName,content));
				}
			}
		} 
		catch(Exception e) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			printWriter.close();
			queue.add(new InMemoryTestImpl("Exception","error reading suite "+name+": "+e+"\n"+writer.toString()));
		}
		finally {
			queue.add(ParallelFitNesseRepository.TEST_SENTINEL);
		}
	}
	private boolean selects(WikiPage page) throws Exception {
		return page.getData().hasAttribute("Test")||isSuiteSetUpOrTearDown(page.getName());
	}
	private boolean isSuiteSetUpOrTearDown(String pageName) throws Exception{
		return pageName.equals("SuiteSetUp")
		|| pageName.equals("SuiteTearDown")
		|| pageName.endsWith(".SuiteSetUp")
		|| pageName.endsWith(".SuiteTearDown");
	}
}