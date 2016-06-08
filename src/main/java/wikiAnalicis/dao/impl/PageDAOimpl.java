package wikiAnalicis.dao.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.PageDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class PageDAOimpl implements PageDAO {
	@Autowired
	private HibernateUtil util;

	public PageDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createPage(Page page) {
		return (Long) util.create(page);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Page mergePage(Page page) {
		Page p = (Page)util.getSessionFactory().getCurrentSession().merge(page);
		p.getRevisions().size();
		return p;
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
	
	 @Transactional
	 public List<Page> list(Integer offset, Integer maxResults){
		 List<Page> list = util.listPagination(offset, maxResults, "Page");
		 for (Page page : list) {
			page.getRevisions().size();
		}
	  return list;
	 }
	 public Long count(){
		  return util.count(Page.class);
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
