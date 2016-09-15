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
import wikiAnalicis.dao.StatisticsDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;
import wikiAnalicis.entity.statics.PageStatistics;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class StatisticsDAOimpl implements StatisticsDAO {
	@Autowired
	private HibernateUtil util;

	public StatisticsDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createPageStatistics(PageStatistics pageStatistics) {
		return (Long) util.create(pageStatistics);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PageStatistics mergePageStatistics(PageStatistics pageStatistics) {
		PageStatistics p = (PageStatistics) util.getSessionFactory().getCurrentSession().merge(pageStatistics);
		return p;
	}

	@Override
	public PageStatistics updatePageStatistics(PageStatistics pageStatistics) {
		return util.update(pageStatistics);
	}

	@Override
	public void deletePageStatistics(long id) {
		PageStatistics pageStatistics = getPageStatistics(id);
//		inCategory= mergeInCategory(inCategory);
		util.delete(pageStatistics);
	}

	@Override
	public List<PageStatistics> getAllPagesStatistics() {
		return util.fetchAll(PageStatistics.class);
	}

	@Override
	public PageStatistics getPageStatistics(long id) {
		return util.fetchById(id, PageStatistics.class);
	}
@Override
public PageStatistics getPageStatistics(Page page) {
	String q = "from PageStatistics ps where ps.page = :p";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("p", page);
	List<PageStatistics> list = (List<PageStatistics>)query.list();
	if (list.size()!=0) {
		return list.get(0);
	}
	return null;
}

	@Transactional
	public List<PageStatistics> list(Integer offset, Integer maxResults) {
		List<PageStatistics> list = util.listPagination(offset, maxResults, "PageStatistics");

		return list;
	}

	public Long count() {
		return util.count(InCategory.class);
	}



}
