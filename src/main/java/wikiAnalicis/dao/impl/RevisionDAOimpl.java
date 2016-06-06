package wikiAnalicis.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.RevisionDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;
@Repository
public class RevisionDAOimpl implements RevisionDAO {
	@Autowired
	private HibernateUtil util;
	public RevisionDAOimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public long createRevision(Revision revision) {
		return (Long) util.create(revision);
	}

	@Override
	public Revision updateRevision(Revision revision) {
		return util.update(revision);
	}

	@Override
	public void deleteRevision(long id) {
		Revision revision = new Revision();
		revision.setId(id);
		util.delete(revision);
	}

	@Override
	public List<Revision> getAllRevisions() {
		return util.fetchAll(Revision.class);
	}

	@Override
	public Revision getRevision(long id) {
		return util.fetchById(id, Revision.class);
	}
	@Override
	public List<Revision> getAllRevisions(Integer offset, Integer maxResults) {
		return util.listPagination(offset, maxResults, "Revision");
	}
	@Override
	public List<Revision> getAllRevisions(Page page,Integer offset, Integer maxResults) {
		Query query = util.getSessionFactory().getCurrentSession().createQuery("from Revision as r where r.page = :page");
		query.setParameter("page", page);
	    query.setFirstResult( (offset!=null?offset:0));
	    query.setMaxResults(maxResults!=null?maxResults:10);
		List<Revision> list = query.list();
		return list;
	}
	@Override
	 public Long count(){
		  return util.count(Revision.class);
		 }
}
