package wikiAnalicis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;
import wikiAnalicis.entity.statics.PageStatistics;


public interface StatisticsService {
	public long createPageStatistics(PageStatistics pageStatistics);

	public PageStatistics mergePageStatistics(PageStatistics pageStatistics);

	public PageStatistics updatePageStatistics(PageStatistics pageStatistics);

	public void deletePageStatistics(long id);

	public List<PageStatistics> getAllPagesStatistics();

	public PageStatistics getPageStatistics(long id);
	public PageStatistics getPageStatistics(Page page);

	public List<PageStatistics> list(Integer offset, Integer maxResults);



}
