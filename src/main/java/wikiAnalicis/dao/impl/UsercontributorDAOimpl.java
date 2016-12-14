package wikiAnalicis.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.MediawikiDAO;
import wikiAnalicis.dao.UserContributorDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class UsercontributorDAOimpl implements UserContributorDAO {

	@Autowired
	private HibernateUtil util;
	
	public UsercontributorDAOimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	@Transactional
	public long createUserContributor(UserContributor userContributor) {
		return (Long) util.create(userContributor);
	}
	@Override
	@Transactional
	public UserContributor mergeUserContributor(UserContributor userContributor) {
		util.getSessionFactory().getCurrentSession().flush();
		util.getSessionFactory().getCurrentSession().clear();
		return util.merge(userContributor);
	}
	
	@Override
	@Transactional
	public UserContributor updateUserContributor(UserContributor userContributor) {
		return util.update(userContributor);
	}

	@Override
	@Transactional
	public void deleteUserContributor(long id) {
		UserContributor userContributor = new UserContributor();
		userContributor.setId(id);
		util.delete(userContributor);

	}

	@Override
	@Transactional
	public List<UserContributor> getAllUserContributors() {
		return util.fetchAll(UserContributor.class);
	}

	@Override
	@Transactional
	public UserContributor getUserContributor(long id) {
		return util.fetchById(id, UserContributor.class);
	}
	@Override
	@Transactional
	public UserContributor getUserContributor(String username) {
		util.getSessionFactory().getCurrentSession().flush();
		util.getSessionFactory().getCurrentSession().clear();
		Query query = util.getSessionFactory().getCurrentSession().createQuery("from UserContributor as u where u.username LIKE ?");
		query.setString(0, username);
		List<UserContributor> list= query.list();
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
		 
	}
	@Override
	@Transactional
	public Long getMinId() {
		// 
		Query query = util.getSessionFactory().getCurrentSession().createQuery("select min(u.realId) from UserContributor u");
		List<Long> list= query.list();
		if (list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

}
