package wikiAnalicis.service;

import java.util.List;

import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;

public interface RevisionService {
	public long createRevision(Revision revision);
	public Revision updateRevision(Revision revision);
	public void deleteRevision(long id);
	public List<Revision> getAllRevisions();
	public Revision getRevision(long id); 
	public List<Revision> getAllRevisions(Integer offset, Integer maxResults); 
	public List<Revision> getAllRevisions(Page page,Integer offset, Integer maxResults); 
	public Long count();
	public Long count(Page page);
}
