package wikiAnalicis.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.CargaDumpDAO;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;
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
@Override
public Mediawiki createMediaWiki(Mediawiki mediawiki) {
	Session session = util.getSessionFactory().getCurrentSession();
	//CREO WIKI
	SQLQuery query = session.createSQLQuery("INSERT INTO mediawiki (id) VALUES (DEFAULT)");
	query.executeUpdate();
	Long lastId = ((BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult()).longValue();
	query.executeUpdate();
	mediawiki.setId(lastId);
	//CREO SITEINFO
	query = session.createSQLQuery("INSERT INTO siteinfo ( base, casee, dbname, generator, sitename) "
			+ "VALUES (:base, :casee, :dbname, :generator, :sitename)");
	query.setParameter("base", mediawiki.getSiteinfo().getBase());
	query.setParameter("casee", mediawiki.getSiteinfo().getCasee());
	query.setParameter("dbname", mediawiki.getSiteinfo().getDbname());
	query.setParameter("generator", mediawiki.getSiteinfo().getGenerator());
	query.setParameter("sitename", mediawiki.getSiteinfo().getSitename());
	query.executeUpdate();
	lastId = ((BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult()).longValue();
	query.executeUpdate();
	mediawiki.getSiteinfo().setId(lastId);
	//UNO WIKI Y SITEINFO
	query = session.createSQLQuery("INSERT INTO mediawiki_siteinfo ( siteinfo_id, mediawiki_id) "
			+ "VALUES ( :siteinfo_id, :mediawiki_id)");
	query.setParameter("mediawiki_id", mediawiki.getId());
	query.setParameter("siteinfo_id", mediawiki.getSiteinfo().getId());
	query.executeUpdate();
	//CREO NAMESPACES
	for (Namespace namespace : mediawiki.getSiteinfo().getNamespaces()) {
		//CREO NAMESPACE
		query = session.createSQLQuery("INSERT INTO namespace ( keyclave, stringCase, value) "
				+ "VALUES ( :keyclave, :stringCase, :value) ");
		query.setParameter("keyclave", namespace.getKey());
		query.setParameter("stringCase",namespace.getStringCase());
		query.setParameter("value", namespace.getValue());
		query.executeUpdate();
		lastId = ((BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult()).longValue();
		query.executeUpdate();
		namespace.setId(lastId);
		//UNO SITEINFO Y NAMESPACE
		query = session.createSQLQuery("INSERT INTO siteinfo_namespace ( Siteinfo_id, namespaces_id) "
				+ "VALUES ( :Siteinfo_id, :namespaces_id)");
		query.setParameter("namespaces_id", namespace.getId());
		query.setParameter("Siteinfo_id", mediawiki.getSiteinfo().getId());
		query.executeUpdate();
	}
	return mediawiki;
}
@Override
public void savePagesInWiki(Mediawiki mediawiki, List<Page> pages) {
	Session session = util.getSessionFactory().getCurrentSession();
	//CREO WIKI
	SQLQuery query;
	for (Page page : pages) {
		query = session.createSQLQuery("INSERT INTO mediawiki_page ( Mediawiki_id, pages_id) "
				+ "VALUES ( :Mediawiki_id, :pages_id)");
		query.setParameter("Mediawiki_id", mediawiki.getId());
		query.setParameter("pages_id", page.getId());
		query.executeUpdate();
	}
	
}
}
