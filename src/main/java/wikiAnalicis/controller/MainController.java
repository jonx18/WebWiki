package wikiAnalicis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.UserContributorService;
@Controller
public class MainController {
	@Autowired
	private MediawikiService mediawikiService;
	@Autowired
	private UserContributorService userContributorService;
	
	@RequestMapping(value = {"index", "/"},produces = "text/plain;charset=UTF-8")
	public ModelAndView getAllRevisions() {
		Mediawiki mediawiki = null;
		List<Mediawiki> allMediawikis = mediawikiService.getAllMediawikis();
		if (!allMediawikis.isEmpty()) {
			mediawiki = allMediawikis.get(0);
//			System.out.println(UserContributor.anonimusID);
//			if (UserContributor.anonimusID==-1) {
//				
//				UserContributor.anonimusID=userContributorService.getMinId();
//				System.out.println(UserContributor.anonimusID);
//			}
			
		} 
	return new ModelAndView("index", "mediawiki", mediawiki);
	}
}
