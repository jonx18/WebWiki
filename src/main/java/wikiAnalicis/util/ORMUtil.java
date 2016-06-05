package wikiAnalicis.util;

import java.io.Serializable;
import java.util.List;

import wikiAnalicis.entity.Page;

public interface ORMUtil {
	public <T> Serializable create(final T entity);
	public <T> T merge(final T entity);
	public <T> T update(final T entity);
	public <T> void delete(final T entity);
	public <T> void delete(Serializable id, Class<T> entityClass);
	public <T> List<T> fetchAll(Class<T> entityClass);
	@SuppressWarnings("rawtypes")
	public <T> List fetchAll(String query);
	public <T> T fetchById(Serializable id, Class<T> entityClass);
	public <T> List<T> listPagination(Integer offset, Integer maxResults, String table);
	public <T> Long count(Class<T> entityClass);
}
