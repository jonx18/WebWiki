package wikiAnalicis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;


public interface InCategoryService {
	public long createInCategory(InCategory inCategory);

	public InCategory mergeInCategory(InCategory inCategory);

	public InCategory updateInCategory(InCategory inCategory);

	public void deleteInCategory(long id);

	public List<InCategory> getAllInCategorys();

	public InCategory getInCategory(long id);

	public List<InCategory> list(Integer offset, Integer maxResults);

	public Long count();

}
