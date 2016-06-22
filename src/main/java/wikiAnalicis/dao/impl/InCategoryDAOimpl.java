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

import wikiAnalicis.dao.InCategoryDAO;
import wikiAnalicis.entity.InCategory;
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



}
