package wikiAnalicis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.CategoryDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryDAO categoryDAO;

	public CategoryServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createCategory(Category category) {
		return categoryDAO.createCategory(category);
	}

	@Override
	public Category mergeCategory(Category category) {
		return categoryDAO.mergeCategory(category);
	}

	@Override
	public Category updateCategory(Category category) {
		return categoryDAO.updateCategory(category);
	}

	@Override
	public void deleteCategory(long id) {
		categoryDAO.deleteCategory(id);
		;
	}

	@Override
	public List<Category> getAllCategorys() {
		return categoryDAO.getAllCategorys();
	}

	@Override
	public Category getCategory(long id) {
		return categoryDAO.getCategory(id);
	}

	@Override
	public void addRevisionsTo(Category Category, List<Revision> revisions) {
		// TODO Auto-generated method stub
		categoryDAO.addRevisionsTo(Category, revisions);
	}

	public List<Category> list(Integer offset, Integer maxResults) {
		return categoryDAO.list(offset, maxResults);
	}

	public Long count() {
		return categoryDAO.count();
	}

	@Override
	public Double averageRevisionsInAllCategorys() {
		return categoryDAO.averageRevisionsInAllCategorys();
	}

	@Override
	public Map<Long, Long> countCategorysForNumberOfRevisions() {
		// TODO Auto-generated method stub
		return categoryDAO.countCategorysForNumberOfRevisions();
	}

	@Override
	public Map<Date, Long> newCategorysInDays() {
		// TODO Auto-generated method stub
		return categoryDAO.newCategorysInDays();
	}

	@Override
	public Map<Date, Long> revisionInDaysOf(Category category) {
		// TODO Auto-generated method stub
		return categoryDAO.revisionInDaysOf(category);
	}

	@Override
	public Map<Date, Long> contentInDaysOf(Category category) {
		// TODO Auto-generated method stub
		return categoryDAO.contentInDaysOf(category);
	}

	@Override
	public Map<String, Long> countColaboratorRevisionsInCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryDAO.countColaboratorRevisionsInCategory(category);
	}

}
