package wikiAnalicis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.PageDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.service.PageService;

@Service
@Transactional
public class PageServiceImpl implements PageService {
	@Autowired
	private PageDAO pageDAO;
	
	public PageServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public long createPage(Page page) {
		return pageDAO.createPage(page);
	}

	@Override
	public Page updatePage(Page page) {
		return pageDAO.updatePage(page);
	}

	@Override
	public void deletePage(long id) {
		pageDAO.deletePage(id);;
	}

	@Override
	public List<Page> getAllPages() {
		return pageDAO.getAllPages();
	}

	@Override
	public Page getPage(long id) {
		return pageDAO.getPage(id);
	}

}
