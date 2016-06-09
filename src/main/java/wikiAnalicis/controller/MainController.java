package wikiAnalicis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.service.MediawikiService;
@Controller
public class MainController {
	@Autowired
	private MediawikiService mediawikiService;
	
	@RequestMapping(value = {"index", "/"})
	public ModelAndView getAllRevisions() {
		Mediawiki mediawiki = null;
		List<Mediawiki> allMediawikis = mediawikiService.getAllMediawikis();
		if (!allMediawikis.isEmpty()) {
			mediawiki = allMediawikis.get(0);
		} 
	return new ModelAndView("index", "mediawiki", mediawiki);
	}
}
