package wikiAnalicis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.MediawikiDAO;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.service.MediawikiService;

@Service
@Transactional
public class MediawikiServiceImpl implements MediawikiService {
	@Autowired
	private MediawikiDAO mediawikiDAO;
	
	public MediawikiServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public long createMediawiki(Mediawiki mediawiki) {
		return mediawikiDAO.createMediawiki(mediawiki);
	}
	@Override
	public Mediawiki mergeMediawiki(Mediawiki mediawiki) {
		return mediawikiDAO.mergeMediawiki(mediawiki);
	}
	@Override
	public Mediawiki updateMediawiki(Mediawiki mediawiki) {
		return mediawikiDAO.updateMediawiki(mediawiki);
	}

	@Override
	public void deleteMediawiki(long id) {
		mediawikiDAO.deleteMediawiki(id);
	}

	@Override
	public List<Mediawiki> getAllMediawikis() {
		return mediawikiDAO.getAllMediawikis();
	}

	@Override
	public Mediawiki getMediawiki(long id) {
		return mediawikiDAO.getMediawiki(id);
	}

	@Override
	public void addPagesTo(Mediawiki mediawiki, List<Page> pages) {
		// TODO Auto-generated method stub
		mediawikiDAO.addPagesTo(mediawiki, pages);
	}
}
