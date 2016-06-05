package wikiAnalicis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import wikiAnalicis.entity.Page;
import wikiAnalicis.service.PageService;

@Controller
public class PageController {
	@Autowired
	private PageService pageService;

	public PageController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/listPages")
	public ModelAndView list(Integer offset, Integer maxResults) {
		List<Page> pages = pageService.list(offset, maxResults);
		ModelAndView model = new ModelAndView("listPages");
		model.addObject("pages",pages );
		model.addObject("count", pageService.count());
		model.addObject("offset", offset);
//		for (Page page : pages) {
//			System.out.println(page.getId()+" "+page.getTitle());
//		}
		return model;
	}

}
