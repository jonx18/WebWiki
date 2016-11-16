package wikiAnalicis.dao.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.InCategoryDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class InCategoryDAOimpl implements InCategoryDAO {
	@Autowired
	private HibernateUtil util;

	public InCategoryDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createInCategory(InCategory inCategory) {
		return (Long) util.create(inCategory);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public InCategory mergeInCategory(InCategory inCategory) {
		InCategory p = (InCategory) util.getSessionFactory().getCurrentSession().merge(inCategory);
		return p;
	}

	@Override
	public InCategory updateInCategory(InCategory inCategory) {
		return util.update(inCategory);
	}

	@Override
	public void deleteInCategory(long id) {
		InCategory inCategory = getInCategory(id);
//		inCategory= mergeInCategory(inCategory);
		util.delete(inCategory);
	}

	@Override
	public List<InCategory> getAllInCategorys() {
		return util.fetchAll(InCategory.class);
	}

	@Override
	public InCategory getInCategory(long id) {
		return util.fetchById(id, InCategory.class);
	}


	@Transactional
	public List<InCategory> list(Integer offset, Integer maxResults) {
		List<InCategory> list = util.listPagination(offset, maxResults, "InCategory");

		return list;
	}

	public Long count() {
		return util.count(InCategory.class);
	}
@Override
public List<InCategory> getAllInCategorysOfCategory(Category category) {
	String q = "from InCategory i where i.category = :c ";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("c", category);
	List<InCategory> list = (List<InCategory>)query.list();
	return list;
}
@Override
public List<InCategory> getAllInCategorysOfPage(Page page) {
	String q = "from InCategory i where i.page = :p ";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("p", page);
	List<InCategory> list = (List<InCategory>)query.list();
	return list;
}
@Override
public List<Page> getAllCategorysedPages() {
	String q = "select i.page from InCategory i ";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	List<Page> list = (List<Page>)query.list();
	HashSet<Page> set = new HashSet<Page>();
	set.addAll(list);
	list=new LinkedList<Page>();
	list.addAll(set);
	return list;
}
@Override
public Map<Date, String[]> getCategoriesNamesOfPage(Page page) {//ordenar
	String q = "select  r.timestamp,r.categoryNames from Page p join p.revisions r where p = :page ";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("page", page);
	List<Object[]> list = query.list();
	Map<Date,String[]> result = new TreeMap<Date, String[]>();
    for(Object[] arr : list){
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault());
		Date date = null;
		try {
			date = format.parse(arr[0].toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	result.put(date, (String[])arr[1]);
//        System.out.println(Arrays.toString(arr));  
    }
	return result;
}
}
