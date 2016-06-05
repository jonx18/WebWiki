package wikiAnalicis.util;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUtil implements ORMUtil {

	@Autowired
	private SessionFactory sessionFactory;

	public HibernateUtil() {
		// TODO Auto-generated constructor stub
	}

	public <T> Serializable create(final T entity) {
		return sessionFactory.getCurrentSession().save(entity);
	}

	public <T> T merge(final T entity) {
		sessionFactory.getCurrentSession().merge(entity);
		return entity;
	}

	public <T> T update(final T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	public <T> void delete(final T entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	public <T> void delete(Serializable id, Class<T> entityClass) {
		T entity = fetchById(id, entityClass);
		delete(entity);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchAll(Class<T> entityClass) {
		return sessionFactory.getCurrentSession().createQuery(" FROM " + entityClass.getName()).list();
	}

	@SuppressWarnings("rawtypes")
	public <T> List fetchAll(String query) {
		return sessionFactory.getCurrentSession().createSQLQuery(query).list();
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchById(Serializable id, Class<T> entityClass) {
		return (T) sessionFactory.getCurrentSession().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> listPagination(Integer offset, Integer maxResults, String table) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("from "+table);
	      query.setFirstResult(maxResults!=null?maxResults:10 * (offset!=null?offset:1 - 1));
	      query.setMaxResults(maxResults!=null?maxResults:10);
		List<T> list = query.list();
		return list;
	}

	@Override
	public <T> Long count(Class<T> entityClass) {
		return (Long) sessionFactory.openSession().createCriteria(entityClass).setProjection(Projections.rowCount())
				.uniqueResult();
	}
}
