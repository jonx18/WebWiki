package wikiAnalicis.dao.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.MediawikiDAO;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
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

}
