package wikiAnalicis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;


public interface DiffContainerService {
	public long createDiffContainer(DiffContainer diffContainer);

	public DiffContainer mergeDiffContainer(DiffContainer diffContainer);

	public DiffContainer updateDiffContainer(DiffContainer diffContainer);

	public void deleteDiffContainer(long id);

	public List<DiffContainer> getAllDiffContainers();

	public DiffContainer getDiffContainer(long id);
	public DiffContainer getDiffContainer(Revision revision);

	public List<DiffContainer> list(Integer offset, Integer maxResults);
	public List<DiffContainer> getAllDiffContainersOfPage(Page page);
	public DiffContainer loadParagraphOfDiffContainer(DiffContainer diffContainer);


}
