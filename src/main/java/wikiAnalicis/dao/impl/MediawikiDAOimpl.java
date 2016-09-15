package wikiAnalicis.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.persister.collection.AbstractCollectionPersister;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.MediawikiDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.ORMUtil;

@Repository
public class MediawikiDAOimpl implements MediawikiDAO {

	@Autowired
	private HibernateUtil util;
	
	public MediawikiDAOimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public long createMediawiki(Mediawiki mediawiki) {
		return (Long) util.create(mediawiki);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Mediawiki mergeMediawiki(Mediawiki mediawiki) {
		Mediawiki m = (Mediawiki)util.getSessionFactory().getCurrentSession().merge(mediawiki);
		m.getPages().size();
		return m;
	}
	
	@Override
	public Mediawiki updateMediawiki(Mediawiki mediawiki) {
		return util.update(mediawiki);
	}

	@Override
	public void deleteMediawiki(long id) {
		Mediawiki mediawiki = new Mediawiki();
		mediawiki.setId(id);
		util.delete(mediawiki);

	}

	@Override
	public List<Mediawiki> getAllMediawikis() {
		return util.fetchAll(Mediawiki.class);
	}

	@Override
	public Mediawiki getMediawiki(long id) {
		return util.fetchById(id, Mediawiki.class);
	}
	@Override
	public void addPagesTo(Mediawiki mediawiki, List<Page> pages) {
		mediawiki = util.merge(mediawiki);
		mediawiki.getPages().addAll(pages);
		mediawiki = util.merge(mediawiki);
	}
	@Override
	public void truncateAll() {
		if (this.getAllMediawikis().size()>0) {
			SQLQuery query = util.getSessionFactory().getCurrentSession().createSQLQuery("SET foreign_key_checks = 0;");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `category`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `category_child`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `category_page`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `category_parent`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `contenidodia`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `delimiter_count`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `diff`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `diffcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `diffcontainer_paragraphcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `distribuciondeaporte`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `incategory`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `mapstylechanges`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `mediawiki`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `mediawiki_page`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `mediawiki_siteinfo`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `namespace`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `nodecontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `page`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `pagestatics`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `pagestatics_categories`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `pagestatistics_dates`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphdiff`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphdiff_newdiffs`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphdiff_olddiffs`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphrevisioncontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphrevisioncontainer_container`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `paragraphrevisioncontainer_delimiter`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `revision`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `revisionesdia`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `siteinfo`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `siteinfo_namespace`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `stylecontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `textcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("TRUNCATE `usercontributor`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("SET foreign_key_checks = 1;");
	        // todo - generify this to all tables
	        query.executeUpdate();
		}
		
//		System.out.println("InCategory.class");
//		deletAllFrom(InCategory.class);
//		System.out.println("Mediawiki.class");
//		deletAllFrom(Mediawiki.class);
//		System.out.println("Siteinfo.class");
//		deletAllFrom(Siteinfo.class);
//		System.out.println("Namespace.class");
//		deletAllFrom(Namespace.class);
//
//		System.out.println("Page.class");
//		deletAllFrom(Page.class);
//		System.out.println("Category.class");
//		deletAllFrom(Category.class);
//		System.out.println("UserContributor.class");
//		deletAllFrom(UserContributor.class);
//
////		deletAllFrom(Mediawiki.class);
////		deletAllFrom(Mediawiki.class);
	}
	private void deletAllFrom(Class clase) {
		List<Object> list = util.fetchAll(clase);
		for (Object object : list) {
			util.delete(object);
		}
	}
	@Override
	public String getLang() {
		return this.getMediawiki(1).getLang();
	}
}
