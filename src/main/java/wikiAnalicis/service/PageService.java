package wikiAnalicis.service;

import java.util.List;

import wikiAnalicis.entity.Page;


public interface PageService {
	public long createPage(Page page);
	public Page mergePage(Page page);
	public Page updatePage(Page page);
	public void deletePage(long id);
	public List<Page> getAllPages();
	public Page getPage(long id); 
}
