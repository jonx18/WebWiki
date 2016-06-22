package wikiAnalicis.dao;

import java.util.List;

import wikiAnalicis.entity.InCategory;

public interface InCategoryDAO {
	public long createInCategory(InCategory inCategory);

	public InCategory mergeInCategory(InCategory inCategory);

	public InCategory updateInCategory(InCategory inCategory);

	public void deleteInCategory(long id);

	public List<InCategory> getAllInCategorys();

	public InCategory getInCategory(long id);

	public List<InCategory> list(Integer offset, Integer maxResults);

	public Long count();

}
