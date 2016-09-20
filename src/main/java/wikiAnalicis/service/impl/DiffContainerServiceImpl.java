package wikiAnalicis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wikiAnalicis.dao.DiffContainerDAO;
import wikiAnalicis.dao.InCategoryDAO;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;
import wikiAnalicis.service.DiffContainerService;
import wikiAnalicis.service.InCategoryService;

@Service
@Transactional
public class DiffContainerServiceImpl implements DiffContainerService {
	@Autowired
	private DiffContainerDAO containerDAO;

	public DiffContainerServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createDiffContainer(DiffContainer diffContainer) {
		return containerDAO.createDiffContainer(diffContainer);
	}

	@Override
	public DiffContainer mergeDiffContainer(DiffContainer diffContainer) {
		return containerDAO.mergeDiffContainer(diffContainer);
	}

	@Override
	public DiffContainer updateDiffContainer(DiffContainer diffContainer) {
		return containerDAO.updateDiffContainer(diffContainer);
	}

	@Override
	public void deleteDiffContainer(long id) {
		containerDAO.deleteDiffContainer(id);
	}

	@Override
	public List<DiffContainer> getAllDiffContainers() {
		return containerDAO.getAllDiffContainers();
	}

	@Override
	public DiffContainer getDiffContainer(long id) {
		return containerDAO.getDiffContainer(id);
	}

	@Override
	public DiffContainer getDiffContainer(Revision revision) {
		return containerDAO.getDiffContainer(revision);
	}

	@Override
	public List<DiffContainer> list(Integer offset, Integer maxResults) {
		return containerDAO.list(offset, maxResults);
	}

	@Override
	public List<DiffContainer> getAllDiffContainersOfPage(Page page) {
		return containerDAO.getAllDiffContainersOfPage(page);
	}
@Override
public DiffContainer loadParagraphOfDiffContainer(DiffContainer diffContainer) {
	return containerDAO.loadParagraphOfDiffContainer(diffContainer);
}


}
