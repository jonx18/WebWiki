package wikiAnalicis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.CargaDumpDAO;
import wikiAnalicis.dao.CategoryDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.service.CargaDumpService;
@Service
@Transactional
public class CargaDumpServiceImpl implements CargaDumpService {
	@Autowired
	private CargaDumpDAO cargaDumpDAO;
	public CargaDumpServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Revision createRevision(Revision revision) {
		// TODO Auto-generated method stub
		return cargaDumpDAO.createRevision(revision);
	}
@Override
public UserContributor mergeUserContributor(UserContributor userContributor) {
	return cargaDumpDAO.mergeUserContributor(userContributor) ;
}
@Override
public Page createPage(Page page) {
	return cargaDumpDAO.createPage(page);
}
@Override
public Page createCategory(Page page) {
	return cargaDumpDAO.createCategory(page);
}
}
