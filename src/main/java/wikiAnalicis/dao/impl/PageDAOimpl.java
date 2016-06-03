package wikiAnalicis.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.PageDAO;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.util.HibernateUtil;

@Repository
public class PageDAOimpl implements PageDAO {
	@Autowired
	private HibernateUtil hibernateUtil;

	public PageDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createPage(Page page) {
		return (Long) hibernateUtil.create(page);
	}

	@Override
	public Page mergePage(Page page) {
		return hibernateUtil.merge(page);
	}

	@Override
	public Page updatePage(Page page) {
		return hibernateUtil.update(page);
	}

	@Override
	public void deletePage(long id) {
		Page page = new Page();
		page.setId(id);
		hibernateUtil.delete(page);
	}

	@Override
	public List<Page> getAllPages() {
		return hibernateUtil.fetchAll(Page.class);
	}

	@Override
	public Page getPage(long id) {
		return hibernateUtil.fetchById(id, Page.class);
	}

}
