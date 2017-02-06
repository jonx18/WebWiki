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
	@Transactional
	public long createMediawiki(Mediawiki mediawiki) {
		return (Long) util.create(mediawiki);
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Mediawiki mergeMediawiki(Mediawiki mediawiki) {
		util.getSessionFactory().getCurrentSession().flush();
		util.getSessionFactory().getCurrentSession().clear();
		Mediawiki m = (Mediawiki)util.getSessionFactory().getCurrentSession().merge(mediawiki);
		m.getPages().size();
		return m;
	}
	
	@Override
	@Transactional
	public Mediawiki updateMediawiki(Mediawiki mediawiki) {
		return util.update(mediawiki);
	}

	@Override
	@Transactional
	public void deleteMediawiki(long id) {
		Mediawiki mediawiki = new Mediawiki();
		mediawiki.setId(id);
		util.delete(mediawiki);

	}

	@Override
	@Transactional
	public List<Mediawiki> getAllMediawikis() {
		return util.fetchAll(Mediawiki.class);
	}

	@Override
	@Transactional
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
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `categoriesnames`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `category`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `category_child`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `category_page`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `category_parent`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `contenidodia`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `delimiter_count`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `diff`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `diffcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `diffcontainer_paragraphcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `distribuciondeaporte`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `incategory`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `mapstylechanges`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `mediawiki`");
	        query.executeUpdate();
			//query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `mediawiki_page`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `mediawiki_siteinfo`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `namespace`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `nodecontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `page`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `pagestatics`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `pagestatics_categories`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `pagestatistics_date`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphdiff`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphdiff_newdiffs`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphdiff_olddiffs`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphrevisioncontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphrevisioncontainer_container`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `paragraphrevisioncontainer_delimiter`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `revision`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `revisionesdia`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `siteinfo`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `siteinfo_namespace`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `stylecontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `textcontainer`");
	        query.executeUpdate();
			query = util.getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM  `usercontributor`");
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
