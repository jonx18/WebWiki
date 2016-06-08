package wikiAnalicis.dao;

import java.util.List;

import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;

public interface RevisionDAO {
	public long createRevision(Revision revision);
	public Revision updateRevision(Revision revision);
	public void deleteRevision(long id);
	public List<Revision> getAllRevisions();
	public Revision getRevision(long id); 
	public List<Revision> getAllRevisions(Integer offset, Integer maxResults);
	public List<Revision> getAllRevisions(Page page,Integer offset, Integer maxResults);
	public Long count();
	Long count(Page page);
	
}
