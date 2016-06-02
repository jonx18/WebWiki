package wikiAnalicis.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wikiAnalicis.dao.RevisionDAO;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.HibernateUtil;
@Repository
public class RevisionDAOimpl implements RevisionDAO {
	@Autowired
	private HibernateUtil hibernateUtil;
	public RevisionDAOimpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public long createRevision(Revision revision) {
		return (Long) hibernateUtil.create(revision);
	}

	@Override
	public Revision updateRevision(Revision revision) {
		return hibernateUtil.update(revision);
	}

	@Override
	public void deleteRevision(long id) {
		Revision revision = new Revision();
		revision.setId(id);
		hibernateUtil.delete(revision);
	}

	@Override
	public List<Revision> getAllRevisions() {
		return hibernateUtil.fetchAll(Revision.class);
	}

	@Override
	public Revision getRevision(long id) {
		return hibernateUtil.fetchById(id, Revision.class);
	}

}
