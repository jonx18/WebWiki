package wikiAnalicis.dao;

import java.util.List;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;


public interface PageDAO {
	public long createPage(Page page);
	public Page mergePage(Page page);
	public Page updatePage(Page page);
	public void deletePage(long id);
	public List<Page> getAllPages();
	public Page getPage(long id); 
	public void addRevisionsTo(Page Page,List<Revision> revisions);
}
