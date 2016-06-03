package wikiAnalicis.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.MediawikiDAO;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.util.HibernateUtil;

@Repository
public class MediawikiDAOimpl implements MediawikiDAO {

	@Autowired
	private HibernateUtil hibernateUtil;
	
	public MediawikiDAOimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public long createMediawiki(Mediawiki mediawiki) {
		return (Long) hibernateUtil.create(mediawiki);
	}
	@Override
	public Mediawiki mergeMediawiki(Mediawiki mediawiki) {
		return hibernateUtil.merge(mediawiki);
	}
	
	@Override
	public Mediawiki updateMediawiki(Mediawiki mediawiki) {
		return hibernateUtil.update(mediawiki);
	}

	@Override
	public void deleteMediawiki(long id) {
		Mediawiki mediawiki = new Mediawiki();
		mediawiki.setId(id);
		hibernateUtil.delete(mediawiki);

	}

	@Override
	public List<Mediawiki> getAllMediawikis() {
		return hibernateUtil.fetchAll(Mediawiki.class);
	}

	@Override
	public Mediawiki getMediawiki(long id) {
		return hibernateUtil.fetchById(id, Mediawiki.class);
	}

}
