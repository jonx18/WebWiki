package wikiAnalicis.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	@Autowired
	private MessageSource messageSource;
	
	public PageDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	@Transactional
	public long createPage(Page page) {
		util.getSessionFactory().getCurrentSession().flush();
		util.getSessionFactory().getCurrentSession().clear();
		return (Long) util.create(page);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Page mergePage(Page page) {
		util.getSessionFactory().getCurrentSession().flush();
		util.getSessionFactory().getCurrentSession().clear();
		Page p = (Page) util.getSessionFactory().getCurrentSession().merge(page);
		p.getRevisions().size();
		util.getSessionFactory().getCurrentSession().flush();
		return p;
	}

	@Override
	@Transactional
	public void createAllPages(List<Page> pages) {
		for (Page page : pages) {
			util.merge(page);
		}
		util.getSessionFactory().getCurrentSession().flush();
		util.getSessionFactory().getCurrentSession().clear();
	}
	
	@Override
	@Transactional
	public Page updatePage(Page page) {
		return util.update(page);
	}

	@Override
	@Transactional
	public void deletePage(long id) {
		Page page = getPage(id);
//		page= mergePage(page);
		util.delete(page);
	}
	@Override
	@Transactional
	public void deletePages(List<Page> pages) {
		int index=1;
		for (Page page : pages) {
			util.delete(page);
			if (index%1000==0) {
				
				System.out.println(index);
				util.getSessionFactory().getCurrentSession().flush();
				util.getSessionFactory().getCurrentSession().clear();		
			}
			index++;
		}


	}
	@Override
	@Transactional
	public List<Page> getAllPages() {
		return util.fetchAll(Page.class);
	}

	@Override
	@Transactional
	public Page getPage(long id) {
		return util.fetchById(id, Page.class);
	}
	@Override
	@Transactional
	public Page getPage(String title) {
		String q = "from Page p where p.title = :title ";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("title", title);
		List<Page> list = (List<Page>)query.list();
		if (list.size()!=0) {
			return list.get(0);
		}
		return null;
	}

	@Transactional
	public List<Page> list(Integer offset, Integer maxResults) {
		List<Page> list = util.listPagination(offset, maxResults, "Page");
		for (Page page : list) {
			page.getRevisions().size();
		}
		return list;
	}
	@Transactional
	public Long count() {
		return util.count(Page.class);
	}

	@Override
	@Transactional
	public void addRevisionsTo(Page page, List<Revision> revisions) {
		// EntityManager entityManager = util.getEntityManager();
		// EntityTransaction t = entityManager.getTransaction();
		// t.begin();
		// try {
		// page = entityManager.merge(page);
		// page.getRevisions().addAll(revisions);
		// } catch (Exception e) {
		// // TODO: handle exception
		// t.rollback();
		// }finally {
		// if (t.isActive()) {
		// t.commit();
		// }
		//
		// }
		page = util.merge(page);
		page.getRevisions().addAll(revisions);
		page = util.merge(page);
	}

	@Override
	@Transactional
	public Double averageRevisionsInAllPages() {
		Query query = util.getSessionFactory().getCurrentSession().createQuery(
				"select avg(p.revisions.size), sum(p.revisions.size), max(p.revisions.size), count(p) from Page p");
		List<Object[]> list = query.list();
		// for(Object[] arr : list){
		// System.out.println(Arrays.toString(arr));
		// }
		return ((Double) list.get(0)[0]);
	}

	@Override
	@Transactional
	// primer Long cantidad de revisiones, segundo cantidad de pag con esa
	// cantidad de revisiones
	public Map<Long, Long> countPagesForNumberOfRevisions() {
		String q = "select  p.revisions.size,count(p) from Page p group by p.revisions.size";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		List<Object[]> list = query.list();
		Map<Long, Long> result = new TreeMap<Long, Long>();
		for (Object[] arr : list) {
			result.put(new Long(arr[0].toString()), new Long(arr[1].toString()));
			// System.out.println(Arrays.toString(arr));
		}
		return result;
	}

	@Override
	@Transactional
	// texto del namespace, cantidad de paginas
	public Map<String, Long> countPagesInNamespace(Locale locale) {
		String q = "select  n.value,count(p) from Page p,Namespace n where p.ns=n.keyclave group by n.keyclave";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		List<Object[]> list = query.list();
		Map<String, Long> result = new TreeMap<String, Long>();
		String defaultWord = messageSource.getMessage("countPagesInNamespace.defaultWord", null, locale);
		for (Object[] arr : list) {
			if (arr[0].toString().isEmpty()) {
				arr[0] = defaultWord;
			}
			result.put(arr[0].toString(), new Long(arr[1].toString()));
			// System.out.println(Arrays.toString(arr));
		}
		return result;
	}

	@Override
	@Transactional
	// una revision sin padre es la creacion de una pag
	public Map<Date, Long> newPagesInDays() {
		String q = "select  r.timestamp,count(r) from Revision r where r.parentid is null group by year(r.timestamp),month(r.timestamp), day(r.timestamp)";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		List<Object[]> list = query.list();
		Map<Date, Long> result = new TreeMap<Date, Long>();
		for (Object[] arr : list) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			Date date = null;
			try {
				date = format.parse(arr[0].toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.put(date, new Long(arr[1].toString()));
			// System.out.println(Arrays.toString(arr));
		}
		return result;
	}

	@Override
	@Transactional
	// una revision sin padre es la creacion de una pag, y tengo un map de
	// namespace con su map de dias
	public Map<String, TreeMap<Date, Long>> newPagesForNamespacesInDays(Locale locale) {
		String q = "select  n.value,r.timestamp,count(r) from Revision r,Namespace n where r.parentid is null and r.page.ns = n.keyclave group by r.page.ns,year(r.timestamp),month(r.timestamp), day(r.timestamp)";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		List<Object[]> list = query.list();
		Map<String, TreeMap<Date, Long>> result = new TreeMap<String, TreeMap<Date, Long>>();
		String defaultWord = messageSource.getMessage("newPagesForNamespacesInDays.defaultWord", null, locale);
		for (Object[] arr : list) {
			if (arr[0].toString().isEmpty()) {
				arr[0] = defaultWord;
			}
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			Date date = null;
			try {
				date = format.parse(arr[1].toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!result.containsKey(arr[0].toString())) {
				result.put(arr[0].toString(), new TreeMap<Date, Long>());
			}
			result.get(arr[0].toString()).put(date, new Long(arr[2].toString()));
//          System.out.println(Arrays.toString(arr));
		}
		return result;
	}
	@Override
	@Transactional
	public Map<Date, Long> revisionInDaysOf(Page page) {
		String q = "select  cast(r.timestamp as date),count(r) from Page p join p.revisions r where p = :page group by cast(r.timestamp as date)";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("page", page);
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
	public Map<Date, Long> contentInDaysOf(Page page) {
		String q = "select  r.timestamp,bit_length(r.text) from Page p join p.revisions r where p = :page ";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("page", page);
		List<Object[]> list = query.list();
		Map<Date,Long> result = new TreeMap<Date, Long>();
        for(Object[] arr : list){
//    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault());
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
	public Map<String, Long> countColaboratorRevisionsInPage(Page page,Locale locale) {
		String q = "select  c.realId,c.username ,count(r) from Page p join p.revisions r join r.contributor c where p = :page group by c.username";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("page", page);
		List<Object[]> list = query.list();
		Map<String, Long> result = new TreeMap<String, Long>();
		String anonymous = messageSource.getMessage("countColaboratorRevisionsInPage.anonymous", null, locale);
		for (Object[] arr : list) {
			if ((new Long(arr[0].toString())).compareTo(new Long(0))<0) {
				arr[1] = anonymous;
			}
			result.put(arr[1].toString(), new Long(arr[2].toString()));
			// System.out.println(Arrays.toString(arr));
		}
		return result;
	}
@Override
@Transactional
public List<Page> getAllPagesInNamespace(Integer ns) {
	String q = "from Page p where p.ns = :ns ";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("ns", ns);
	List<Page> list = (List<Page>)query.list();
	return list;
}
}
