package wikiAnalicis.util;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JPAUtil {

	@PersistenceContext
	private EntityManager entityManager;

	public JPAUtil() {
		// TODO Auto-generated constructor stub
	}

	public <T> Serializable create(final T entity) {
		entityManager.persist(entity);
		return null;
	}

	public <T> T merge(final T entity) {
		return entityManager.merge(entity);
	}

	public <T> T update(final T entity) {
		T entity2 = entityManager.merge(entity);
		entityManager.refresh(entity2);
		return entity;
	}

	public <T> void delete(final T entity) {
		T entity2 = entityManager.merge(entity);
		entityManager.remove(entity2);
	}

	public <T> void delete(Serializable id, Class<T> entityClass) {
		T entity = fetchById(id, entityClass);
		delete(entity);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> fetchAll(Class<T> entityClass) {
		return entityManager.createQuery(" FROM " + entityClass.getName()).getResultList();
	}

	@SuppressWarnings("rawtypes")
	public <T> List fetchAll(String query) {
		return entityManager.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> T fetchById(Serializable id, Class<T> entityClass) {
		return (T) entityManager.find(entityClass, id);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
