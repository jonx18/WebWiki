package wikiAnalicis.dao;

import java.util.List;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;
import wikiAnalicis.entity.statics.PageStatistics;

public interface StatisticsDAO {
	public long createPageStatistics(PageStatistics pageStatistics);

	public PageStatistics mergePageStatistics(PageStatistics pageStatistics);

	public PageStatistics updatePageStatistics(PageStatistics pageStatistics);

	public void deletePageStatistics(long id);

	public List<PageStatistics> getAllPagesStatistics();

	public PageStatistics getPageStatistics(long id);
	public PageStatistics getPageStatistics(Page page);

	public List<PageStatistics> list(Integer offset, Integer maxResults);

}
