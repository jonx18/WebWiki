package wikiAnalicis.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public long createRevision(Revision revision) {
		return (Long) util.create(revision);
	}
@Override
@Transactional
public void createAllRevisions(List<Revision> revisions) {
	util.getSessionFactory().getCurrentSession().flush();
	util.getSessionFactory().getCurrentSession().clear();
	for (Revision revision : revisions) {
		util.merge(revision);
	}

}
	@Override
	@Transactional
	public Revision updateRevision(Revision revision) {
		return util.update(revision);
	}

	@Override
	@Transactional
	public void deleteRevision(long id) {
		Revision revision = new Revision();
		revision.setId(id);
		util.delete(revision);
	}

	@Override
	@Transactional
	public List<Revision> getAllRevisions() {
		return util.fetchAll(Revision.class);
	}

	@Override
	@Transactional
	public Revision getRevision(long id) {
		return util.fetchById(id, Revision.class);
	}
	@Override
	@Transactional
	public List<Revision> getAllRevisions(Integer offset, Integer maxResults) {
		return util.listPagination(offset, maxResults, "Revision");
	}
	@Override
	@Transactional
	public List<Revision> getAllRevisions(Page page,Integer offset, Integer maxResults) {
		Query query = util.getSessionFactory().getCurrentSession().createQuery("from Revision as r where r.page = :page order by r.timestamp");
		query.setParameter("page", page);
	    query.setFirstResult( (offset!=null?offset:0));
	    query.setMaxResults(maxResults!=null?maxResults:10);
		List<Revision> list = query.list();
		return list;
	}
	@Override
	@Transactional
	 public Long count(){
		  return util.count(Revision.class);
		 }
	@Override
	@Transactional
	 public Long count(Page page){
		Query query = util.getSessionFactory().getCurrentSession().createQuery("from Revision as r where r.page = :page");
		query.setParameter("page", page);
		return new Long(query.list().size());
		 }
	@Override
	@Transactional
	 public Map<Date,Long> revisionInDays(){
		String q = "select  r.timestamp,count(r) from Revision r group by year(r.timestamp),month(r.timestamp), day(r.timestamp)";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		List<Object[]> list = query.list();
		Map<Date,Long> result = new TreeMap<Date, Long>();
        for(Object[] arr : list){
    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    		Date date = null;
    		try {
    			date = format.parse(arr[0].toString());
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	result.put(date, new Long(arr[1].toString()));
//            System.out.println(Arrays.toString(arr));
        }
		return result;
		 }
	@Override
	@Transactional
	// texto del namespace, cantidad de paginas
	public Map<String, Long> countRevisionsInNamespace() {
		String q = "select  n.value,sum(p.revisions.size) from Page p,Namespace n where p.ns=n.keyclave group by n.keyclave";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		List<Object[]> list = query.list();
		Map<String, Long> result = new TreeMap<String, Long>();
		for (Object[] arr : list) {
			if (arr[0].toString().isEmpty()) {
				arr[0] = "Articulo";
			}
			result.put(arr[0].toString(), new Long(arr[1].toString()));
			// System.out.println(Arrays.toString(arr));
		}
		return result;
	}
}
