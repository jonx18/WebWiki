package wikiAnalicis.dao;

import java.util.List;

import wikiAnalicis.entity.Mediawiki;


public interface MediawikiDAO {
	public long createMediawiki(Mediawiki mediawiki);
	public Mediawiki mergeMediawiki(Mediawiki mediawiki);
	public Mediawiki updateMediawiki(Mediawiki mediawiki);
	public void deleteMediawiki(long id);
	public List<Mediawiki> getAllMediawikis();
	public Mediawiki getMediawiki(long id); 
}
