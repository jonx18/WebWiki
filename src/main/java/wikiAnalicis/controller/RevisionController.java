package wikiAnalicis.controller;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;

@Controller
public class RevisionController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private RevisionService revisionService; 
	@Autowired
	private PageService pageService; 
	public RevisionController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping("createRevision")
	public ModelAndView createRevision(@ModelAttribute Revision revision) {
		LOGGER.info("Creating Revision. Data: "+revision);
		return new ModelAndView("revisionForm");
	}

	@RequestMapping("editRevision")
	public ModelAndView editRevision(@RequestParam long id, @ModelAttribute Revision revision) {
	LOGGER.info("Updating the Revision for the Id "+id);
	revision = revisionService.getRevision(id);
	return new ModelAndView("revisionForm", "revisionObject", revision);
	}

	@RequestMapping("saveRevision")
	public ModelAndView saveRevision(@ModelAttribute Revision revision) {
		System.out.println("aca esta"+revision);
	LOGGER.info("Saving the Revision. Data : "+revision);
	if(revisionService.getRevision(revision.getId())==null){ // if revision id is 0 then creating the revision other updating the revision
		System.out.println("crear");
		revisionService.createRevision(revision);
	} else {
		System.out.println("actualizar");
	revisionService.updateRevision(revision);
	}
	return new ModelAndView("redirect:getAllRevisions");
	}

	@RequestMapping("deleteRevision")
	public ModelAndView deleteRevision(@RequestParam long id) {
	LOGGER.info("Deleting the Revision. Id : "+id);
	revisionService.deleteRevision(id);
	return new ModelAndView("redirect:getAllRevisions");
	}

	@RequestMapping(value = {"getAllRevisions", "/"})
	public ModelAndView getAllRevisions() {
	LOGGER.info("Getting the all Revisions.");
	List<Revision> revisionList = revisionService.getAllRevisions();
	return new ModelAndView("revisionList", "revisionList", revisionList);
	}
	
	@RequestMapping(value = "getAllRevisionsOf")
	public ModelAndView list(Integer offset, Integer maxResults,Long parentId) {
		Page page = pageService.getPage(parentId);
		List<Revision> revisions = revisionService.getAllRevisions(page, offset, maxResults);
		ModelAndView model = new ModelAndView("revisionListOf");
		model.addObject("page",page );
		model.addObject("revisions",revisions );
		model.addObject("count", revisionService.count(page));
		model.addObject("offset", offset);
//		for (Page page : pages) {
//			System.out.println(page.getId()+" "+page.getTitle());
//		}
		return model;
	}
}
