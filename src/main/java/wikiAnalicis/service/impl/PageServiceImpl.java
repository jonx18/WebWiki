package wikiAnalicis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.PageDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
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
	public Page mergePage(Page page) {
		return pageDAO.mergePage(page);
	}

	@Override
	public Page updatePage(Page page) {
		return pageDAO.updatePage(page);
	}

	@Override
	public void deletePage(long id) {
		pageDAO.deletePage(id);
		;
	}

	@Override
	public List<Page> getAllPages() {
		return pageDAO.getAllPages();
	}

	@Override
	public Page getPage(long id) {
		return pageDAO.getPage(id);
	}

	@Override
	public Page getPage(String title) {
		// TODO Auto-generated method stub
		return pageDAO.getPage(title);
	}

	@Override
	public void addRevisionsTo(Page Page, List<Revision> revisions) {
		// TODO Auto-generated method stub
		pageDAO.addRevisionsTo(Page, revisions);
	}

	public List<Page> list(Integer offset, Integer maxResults) {
		return pageDAO.list(offset, maxResults);
	}

	public Long count() {
		return pageDAO.count();
	}

	@Override
	public Double averageRevisionsInAllPages() {
		return pageDAO.averageRevisionsInAllPages();
	}

	@Override
	public Map<Long, Long> countPagesForNumberOfRevisions() {
		// TODO Auto-generated method stub
		return pageDAO.countPagesForNumberOfRevisions();
	}

	@Override
	public Map<String, Long> countPagesInNamespace(Locale locale) {
		// TODO Auto-generated method stub
		return pageDAO.countPagesInNamespace(locale);
	}

	@Override
	public Map<Date, Long> newPagesInDays() {
		// TODO Auto-generated method stub
		return pageDAO.newPagesInDays();
	}

	@Override
	public Map<String, TreeMap<Date, Long>> newPagesForNamespacesInDays(Locale locale) {
		// TODO Auto-generated method stub
		return pageDAO.newPagesForNamespacesInDays(locale);
	}

	@Override
	public Map<Date, Long> revisionInDaysOf(Page page) {
		// TODO Auto-generated method stub
		return pageDAO.revisionInDaysOf(page);
	}

	@Override
	public Map<Date, Long> contentInDaysOf(Page page) {
		// TODO Auto-generated method stub
		return pageDAO.contentInDaysOf(page);
	}

	@Override
	public Map<String, Long> countColaboratorRevisionsInPage(Page page, Locale locale) {
		// TODO Auto-generated method stub
		return pageDAO.countColaboratorRevisionsInPage(page, locale);
	}

	@Override
	public List<Page> getAllPagesInNamespace(Integer ns) {
		// TODO Auto-generated method stub
		return pageDAO.getAllPagesInNamespace(ns);
	}
	@Override
	public void createAllPages(List<Page> pages) {
		// TODO Auto-generated method stub
		pageDAO.createAllPages(pages);
	}
}
