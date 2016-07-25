package wikiAnalicis.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;

public interface CargaDumpDAO {
	public Revision createRevision(Revision revision);

	public UserContributor mergeUserContributor(UserContributor userContributor);

	public Page createPage(Page page);

	public Page createCategory(Page page);

	public Mediawiki createMediaWiki(Mediawiki mediawiki);

	public void savePagesInWiki(Mediawiki mediawiki, List<Page> pages);

	
}
