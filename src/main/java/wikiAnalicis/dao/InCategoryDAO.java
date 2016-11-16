package wikiAnalicis.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;

public interface InCategoryDAO {
	public long createInCategory(InCategory inCategory);

	public InCategory mergeInCategory(InCategory inCategory);

	public InCategory updateInCategory(InCategory inCategory);

	public void deleteInCategory(long id);

	public List<InCategory> getAllInCategorys();

	public InCategory getInCategory(long id);

	public List<InCategory> list(Integer offset, Integer maxResults);

	public List<InCategory> getAllInCategorysOfPage(Page page);
	
	public List<InCategory> getAllInCategorysOfCategory(Category category);
	
	public Long count();

	public List<Page> getAllCategorysedPages();
	public Map<Date, String[]> getCategoriesNamesOfPage(Page page);

}
