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

import wikiAnalicis.dao.DiffContainerDAO;
import wikiAnalicis.dao.InCategoryDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class DiffContainerDAOimpl implements DiffContainerDAO {
	@Autowired
	private HibernateUtil util;

	public DiffContainerDAOimpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createDiffContainer(DiffContainer diffContainer) {
		return (Long) util.create(diffContainer);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DiffContainer mergeDiffContainer(DiffContainer diffContainer) {
		DiffContainer p = (DiffContainer) util.getSessionFactory().getCurrentSession().merge(diffContainer);
		return p;
	}

	@Override
	public DiffContainer updateDiffContainer(DiffContainer diffContainer) {
		return util.update(diffContainer);
	}

	@Override
	public void deleteDiffContainer(long id) {
		DiffContainer diffContainer = getDiffContainer(id);
//		inCategory= mergeInCategory(inCategory);
		util.delete(diffContainer);
	}

	@Override
	public List<DiffContainer> getAllDiffContainers() {
		return util.fetchAll(DiffContainer.class);
	}

	@Override
	public DiffContainer getDiffContainer(long id) {
		return util.fetchById(id, DiffContainer.class);
	}
@Override
public DiffContainer getDiffContainer(Revision revision) {
	String q = "from DiffContainer d where d.oldRevision = :r order by d.oldRevision.id";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("r", revision);
	List<DiffContainer> list = (List<DiffContainer>)query.list();
	if (list.size()!=0) {
		return list.get(0);
	}
	return null;
}

	@Transactional
	public List<DiffContainer> list(Integer offset, Integer maxResults) {
		List<DiffContainer> list = util.listPagination(offset, maxResults, "DiffContainer");

		return list;
	}

	public Long count() {
		return util.count(InCategory.class);
	}

@Override
public List<DiffContainer> getAllDiffContainersOfPage(Page page) {
	String q = "from DiffContainer d where d.oldRevision.page = :p ";
	Query query = util.getSessionFactory().getCurrentSession().createQuery(q);
	query.setParameter("p", page);
	List<DiffContainer> list = (List<DiffContainer>)query.list();
	return list;
}

}
