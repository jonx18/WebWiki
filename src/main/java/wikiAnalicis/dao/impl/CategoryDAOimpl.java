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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.CategoryDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class CategoryDAOimpl implements CategoryDAO {
	@Autowired
	private HibernateUtil util;

	public CategoryDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createCategory(Category category) {
		return (Long) util.create(category);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Category mergeCategory(Category category) {
		Category p = (Category) util.getSessionFactory().getCurrentSession().merge(category);
		p.getRevisions().size();
		return p;
	}

	@Override
	public Category updateCategory(Category category) {
		return util.update(category);
	}

	@Override
	public void deleteCategory(long id) {
		Category category = getCategory(id);
//		category= mergeCategory(category);
		util.delete(category);
	}

	@Override
	public List<Category> getAllCategorys() {
		return util.fetchAll(Category.class);
	}

	@Override
	public Category getCategory(long id) {
		return util.fetchById(id, Category.class);
	}

	@Transactional
	public List<Category> list(Integer offset, Integer maxResults) {
		List<Category> list = util.listPagination(offset, maxResults, "Category");
		for (Category category : list) {
			category.getRevisions().size();
		}
		return list;
	}

	public Long count() {
		return util.count(Category.class);
	}

	@Override
	public void addRevisionsTo(Category category, List<Revision> revisions) {
		// EntityManager entityManager = util.getEntityManager();
		// EntityTransaction t = entityManager.getTransaction();
		// t.begin();
		// try {
		// category = entityManager.merge(category);
		// category.getRevisions().addAll(revisions);
		// } catch (Exception e) {
		// // TODO: handle exception
		// t.rollback();
		// }finally {
		// if (t.isActive()) {
		// t.commit();
		// }
		//
		// }
		category = util.merge(category);
		category.getRevisions().addAll(revisions);
		category = util.merge(category);
	}

	@Override
	public Double averageRevisionsInAllCategorys() {
		Query query = util.getSessionFactory().getCurrentSession().createQuery(
				"select avg(p.revisions.size), sum(p.revisions.size), max(p.revisions.size), count(p) from Category p");
		List<Object[]> list = query.list();
		// for(Object[] arr : list){
		// System.out.println(Arrays.toString(arr));
		// }
		return ((Double) list.get(0)[0]);
	}

	@Override
	// primer Long cantidad de revisiones, segundo cantidad de pag con esa
	// cantidad de revisiones
	public Map<Long, Long> countCategorysForNumberOfRevisions() {
		String q = "select  p.revisions.size,count(p) from Category p group by p.revisions.size";
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
	// una revision sin padre es la creacion de una pag
	public Map<Date, Long> newCategorysInDays() {
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
	public Map<Date, Long> revisionInDaysOf(Category category) {
		String q = "select  r.timestamp,count(r) from Category p join p.revisions r where p = :category group by year(r.timestamp),month(r.timestamp), day(r.timestamp)";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("category", category);
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
	public Map<Date, Long> contentInDaysOf(Category category) {
		String q = "select  r.timestamp,bit_length(r.text) from Category p join p.revisions r where p = :category ";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("category", category);
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
	public Map<String, Long> countColaboratorRevisionsInCategory(Category category) {
		String q = "select  c.realId,c.username ,count(r) from Category p join p.revisions r join r.contributor c where p = :category group by c.username";
		Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
		query.setParameter("category", category);
		List<Object[]> list = query.list();
		Map<String, Long> result = new TreeMap<String, Long>();
		for (Object[] arr : list) {
			if ((new Long(arr[0].toString())).compareTo(new Long(0))<0) {
				arr[1] = "Anonimo";
			}
			result.put(arr[1].toString(), new Long(arr[2].toString()));
			// System.out.println(Arrays.toString(arr));
		}
		return result;
	}

}
