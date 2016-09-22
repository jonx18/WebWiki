package wikiAnalicis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.service.UserContributorService;

@Component
public class StartConfiguration implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private UserContributorService userContributorService;
	
	  @Override
	  public void onApplicationEvent(final ContextRefreshedEvent event) {
	    System.out.println("on start: "+UserContributor.anonimusID);
		if (UserContributor.anonimusID==-1) {
			Long id =userContributorService.getMinId();
			if (id!=null && id<0) {
				UserContributor.anonimusID= id - 1;
			}
			
			System.out.println("on start: "+UserContributor.anonimusID);
		}
	  }
	}
