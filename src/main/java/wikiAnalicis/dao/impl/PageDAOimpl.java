package wikiAnalicis.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.PageDAO;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.JPAUtil;

@Repository
public class PageDAOimpl implements PageDAO {
	@Autowired
	private JPAUtil util;

	public PageDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createPage(Page page) {
		return (Long) util.create(page);
	}

	@Override
	public Page mergePage(Page page) {
		return util.merge(page);
	}

	@Override
	public Page updatePage(Page page) {
		return util.update(page);
	}

	@Override
	public void deletePage(long id) {
		Page page = new Page();
		page.setId(id);
		util.delete(page);
	}

	@Override
	public List<Page> getAllPages() {
		return util.fetchAll(Page.class);
	}

	@Override
	public Page getPage(long id) {
		return util.fetchById(id, Page.class);
	}

	@Override
	public void addRevisionsTo(Page page, List<Revision> revisions) {
//		EntityManager entityManager = util.getEntityManager();
//		EntityTransaction t = entityManager.getTransaction();
//		t.begin();
//		try {
//			page = entityManager.merge(page);
//			page.getRevisions().addAll(revisions);
//		} catch (Exception e) {
//			// TODO: handle exception
//			t.rollback();
//		}finally {
//			if (t.isActive()) {
//				t.commit();
//			}
//			
//		}
		page = util.merge(page);
		page.getRevisions().addAll(revisions);
		page = util.merge(page);
	}
}
