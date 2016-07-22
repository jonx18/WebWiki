package wikiAnalicis.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.CargaDumpDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.util.HibernateUtil;
@Repository
public class CargaDumpDAOimpl implements CargaDumpDAO {
	@Autowired
	private HibernateUtil util;
public CargaDumpDAOimpl() {
	// TODO Auto-generated constructor stub
}
	@Override
	public Revision createRevision(Revision revision) {
		Session session = util.getSessionFactory().getCurrentSession();

		SQLQuery query = session.createSQLQuery("INSERT INTO revision "
				+ "(id, comment, deleted, format, minor, model, parentid, sha1, text, timestamp, contributor_id, page_id)"
				+ " VALUES (:id, :comment, :deleted, :format, :minor, :model, :parentid, :sha1, :text, :timestamp, :contributor_id, :page_id)");
		query.setParameter("id", revision.getId());
		query.setParameter("comment", revision.getComment());
		query.setParameter("deleted", revision.getDeleted());
		query.setParameter("format", revision.getFormat());
		query.setParameter("minor", revision.getMinor());
		query.setParameter("model", revision.getModel());
		query.setParameter("parentid", revision.getParentid());
		query.setParameter("sha1", revision.getSha1());
		query.setParameter("text", revision.getText());
		query.setParameter("timestamp", revision.getTimestamp());
		query.setParameter("contributor_id", revision.getContributor().getId());
		query.setParameter("page_id", revision.getPage().getId());
		query.executeUpdate();
		return revision;
	}
@Override
public UserContributor mergeUserContributor(UserContributor userContributor) {
	Session session = util.getSessionFactory().getCurrentSession();
	Query query = session.createQuery("from UserContributor as u where u.username LIKE ?");
	query.setString(0, userContributor.getUsername());
	List<UserContributor> list= query.list();
	if (list.isEmpty()) {
		query = session.createSQLQuery("INSERT INTO usercontributor (deleted, ip, realId, username) VALUES (:deleted, :ip, :realId, :username)");
		query.setParameter("deleted", userContributor.getDeleted());
		query.setParameter("ip", userContributor.getIp());
		query.setParameter("realId", userContributor.getRealId());
		query.setParameter("username", userContributor.getUsername());

		query.executeUpdate();
		Long lastId = ((BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult()).longValue();
		userContributor.setId(lastId);
		

	} else {
		userContributor=list.get(0);
		userContributor.setDeleted(userContributor.getDeleted());
		userContributor.setIp(userContributor.getIp());
		userContributor.setRealId(userContributor.getRealId());
		userContributor.setUsername( userContributor.getUsername());
	}
	
	return userContributor;
}
@Override
public Page createPage(Page page) {
	Session session = util.getSessionFactory().getCurrentSession();

	SQLQuery query = session.createSQLQuery("INSERT INTO page (id, ns, redirect, title) VALUES (:id, :ns, :redirect, :title)");
	query.setParameter("id", page.getId());
	query.setParameter("ns", page.getNs());
	query.setParameter("redirect", page.getRedirect());
	query.setParameter("title", page.getTitle());

	query.executeUpdate();
	return page;
}
@Override
public Page createCategory(Page page) {
	page = this.createPage(page);
	Session session = util.getSessionFactory().getCurrentSession();

	SQLQuery query = session.createSQLQuery("INSERT INTO category (id) VALUES (:id)");
	query.setParameter("id", page.getId());

	query.executeUpdate();
	return page;

}
}
