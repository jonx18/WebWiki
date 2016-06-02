package wikiAnalicis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.RevisionDAO;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.service.RevisionService;
@Service
@Transactional
public class RevisionServiceImpl implements RevisionService {
	@Autowired
	private RevisionDAO revisionDAO;
	public RevisionServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public long createRevision(Revision revision) {
		return revisionDAO.createRevision(revision);
	}

	@Override
	public Revision updateRevision(Revision revision) {
		return revisionDAO.updateRevision(revision);
	}

	@Override
	public void deleteRevision(long id) {
		revisionDAO.deleteRevision(id);
	}

	@Override
	public List<Revision> getAllRevisions() {
		return revisionDAO.getAllRevisions();
	}

	@Override
	public Revision getRevision(long id) {
		return revisionDAO.getRevision(id);
	}

}
