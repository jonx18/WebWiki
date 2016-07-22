package wikiAnalicis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;

public interface CargaDumpService {
	public Revision createRevision(Revision revision);

	public UserContributor mergeUserContributor(UserContributor userContributor);

	public Page createPage(Page page);

	public Page createCategory(Page page);

	
}
