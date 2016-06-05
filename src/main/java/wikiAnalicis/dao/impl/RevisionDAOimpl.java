package wikiAnalicis.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.RevisionDAO;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
import wikiAnalicis.util.JPAUtil;
@Repository
public class RevisionDAOimpl implements RevisionDAO {
	@Autowired
	private JPAUtil util;
	public RevisionDAOimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public long createRevision(Revision revision) {
		return (Long) util.create(revision);
	}

	@Override
	public Revision updateRevision(Revision revision) {
		return util.update(revision);
	}

	@Override
	public void deleteRevision(long id) {
		Revision revision = new Revision();
		revision.setId(id);
		util.delete(revision);
	}

	@Override
	public List<Revision> getAllRevisions() {
		return util.fetchAll(Revision.class);
	}

	@Override
	public Revision getRevision(long id) {
		return util.fetchById(id, Revision.class);
	}

}
