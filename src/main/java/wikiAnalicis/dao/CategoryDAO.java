package wikiAnalicis.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.Revision;

public interface CategoryDAO {
	public long createCategory(Category category);

	public Category mergeCategory(Category category);

	public Category updateCategory(Category category);

	public void deleteCategory(long id);

	public List<Category> getAllCategorys();

	public Category getCategory(long id);
	
	public Category getCategory(String title);

	public void addRevisionsTo(Category category, List<Revision> revisions);

	public List<Category> list(Integer offset, Integer maxResults);

	public Long count();

	public Double averageRevisionsInAllCategorys();

	public Map<Long, Long> countCategorysForNumberOfRevisions();


	public Map<Date, Long> newCategorysInDays();

	public Map<Date, Long> revisionInDaysOf(Category category);

	public Map<Date, Long> contentInDaysOf(Category category);

	public Map<String, Long> countColaboratorRevisionsInCategory(Category category);
}
