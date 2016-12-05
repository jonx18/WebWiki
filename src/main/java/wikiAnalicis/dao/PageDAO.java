package wikiAnalicis.dao;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;


public interface PageDAO {
	public long createPage(Page page);
	public Page mergePage(Page page);
	public Page updatePage(Page page);
	public void deletePage(long id);
	public List<Page> getAllPages();
	public Page getPage(long id); 
	public Page getPage(String title); 
	public void addRevisionsTo(Page Page,List<Revision> revisions);
	 public List<Page> list(Integer offset, Integer maxResults);
	 public Long count();
	 public Double  averageRevisionsInAllPages();
	 public Map<Long, Long> countPagesForNumberOfRevisions();
	 public Map<String, Long> countPagesInNamespace(Locale locale);
	 public Map<Date, Long> newPagesInDays();
	 public Map<String, TreeMap<Date, Long>> newPagesForNamespacesInDays(Locale locale);
	public Map<Date, Long> revisionInDaysOf(Page page);
	public Map<Date, Long> contentInDaysOf(Page page);
	public Map<String, Long> countColaboratorRevisionsInPage(Page page, Locale locale);
	public List<Page> getAllPagesInNamespace(Integer ns);
	public void createAllPages(List<Page> pages);
	
	
}
