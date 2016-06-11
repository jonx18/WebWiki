package wikiAnalicis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;


public interface PageService {
	public long createPage(Page page);
	public Page mergePage(Page page);
	public Page updatePage(Page page);
	public void deletePage(long id);
	public List<Page> getAllPages();
	public Page getPage(long id); 
	public void addRevisionsTo(Page Page,List<Revision> revisions);
	public List<Page> list(Integer offset, Integer maxResults);
	public Long count();
	public Double  averageRevisionsInAllPages();
	public Map<Long, Long> countPagesForNumberOfRevisions();
	 public Map<String, Long> countPagesInNamespace();
	 public Map<Date, Long> newPagesInDays();
	 public Map<String, TreeMap<Date, Long>> newPagesForNamespacesInDays();
	public Map<Date, Long> revisionInDaysOf(Page page);
	public Map<Date, Long> contentInDaysOf(Page page);
	public Map<String, Long> countColaboratorRevisionsInPage(Page page);
	public List<Page> getAllPagesInNamespace(Integer ns);
}
