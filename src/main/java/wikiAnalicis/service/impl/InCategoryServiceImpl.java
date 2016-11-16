package wikiAnalicis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.InCategoryDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.service.InCategoryService;

@Service
@Transactional
public class InCategoryServiceImpl implements InCategoryService {
	@Autowired
	private InCategoryDAO inInCategoryDAO;

	public InCategoryServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createInCategory(InCategory inInCategory) {
		return inInCategoryDAO.createInCategory(inInCategory);
	}

	@Override
	public List<Page> getAllCategorysedPages() {
		// TODO Auto-generated method stub
		return inInCategoryDAO.getAllCategorysedPages();
	}
	
	@Override
	public InCategory mergeInCategory(InCategory inInCategory) {
		return inInCategoryDAO.mergeInCategory(inInCategory);
	}

	@Override
	public InCategory updateInCategory(InCategory inInCategory) {
		return inInCategoryDAO.updateInCategory(inInCategory);
	}

	@Override
	public void deleteInCategory(long id) {
		inInCategoryDAO.deleteInCategory(id);
	}

	@Override
	public List<InCategory> getAllInCategorys() {
		return inInCategoryDAO.getAllInCategorys();
	}

	@Override
	public InCategory getInCategory(long id) {
		return inInCategoryDAO.getInCategory(id);
	}

	public List<InCategory> list(Integer offset, Integer maxResults) {
		return inInCategoryDAO.list(offset, maxResults);
	}

	public Long count() {
		return inInCategoryDAO.count();
	}
@Override
public List<InCategory> getAllInCategorysOfCategory(Category category) {
	// TODO Auto-generated method stub
	return inInCategoryDAO.getAllInCategorysOfCategory(category);
}
@Override
public List<InCategory> getAllInCategorysOfPage(Page page) {
	// TODO Auto-generated method stub
	return inInCategoryDAO.getAllInCategorysOfPage(page);
}
@Override
public Map<Date, String[]> getCategoriesNamesOfPage(Page page) {
	return inInCategoryDAO.getCategoriesNamesOfPage(page);
}

}
