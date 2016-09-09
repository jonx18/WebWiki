package wikiAnalicis.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public Long count(Page page);
	public Map<Date, Long> revisionInDays();
	public Map<String, Long> countRevisionsInNamespace();
	public void createAllRevisions(List<Revision> revisions);
	
}
