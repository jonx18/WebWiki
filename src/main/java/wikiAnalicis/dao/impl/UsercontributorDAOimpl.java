package wikiAnalicis.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	public long createUserContributor(UserContributor userContributor) {
		return (Long) util.create(userContributor);
	}
	@Override
	public UserContributor mergeUserContributor(UserContributor userContributor) {
		return util.merge(userContributor);
	}
	
	@Override
	public UserContributor updateUserContributor(UserContributor userContributor) {
		return util.update(userContributor);
	}

	@Override
	public void deleteUserContributor(long id) {
		UserContributor userContributor = new UserContributor();
		userContributor.setId(id);
		util.delete(userContributor);

	}

	@Override
	public List<UserContributor> getAllUserContributors() {
		return util.fetchAll(UserContributor.class);
	}

	@Override
	public UserContributor getUserContributor(long id) {
		return util.fetchById(id, UserContributor.class);
	}
	@Override
	public UserContributor getUserContributor(String username) {
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
