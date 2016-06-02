package wikiAnalicis.service;

import java.util.List;

import wikiAnalicis.entity.Revision;

public interface RevisionService {
	public long createRevision(Revision revision);
	public Revision updateRevision(Revision revision);
	public void deleteRevision(long id);
	public List<Revision> getAllRevisions();
	public Revision getRevision(long id); 
	//public List<Revision> getAllRevisions(Page); 
}
