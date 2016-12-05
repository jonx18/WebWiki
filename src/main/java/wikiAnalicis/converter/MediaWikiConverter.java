package wikiAnalicis.converter;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;

public class MediaWikiConverter implements Converter {
	private PageService pageService;
	private MediawikiService mediawikiService;
	private Integer namespace = null;
	private Locale lang;
	public MediaWikiConverter(MediawikiService mediawikiService) {
		super();
		this.mediawikiService = mediawikiService;
	}
	public MediaWikiConverter(MediawikiService mediawikiService,Integer namespace) {
		super();
		this.mediawikiService = mediawikiService;
		this.namespace = namespace;
	}
	public MediaWikiConverter(MediawikiService mediawikiService2, PageService pageService) {
		this.mediawikiService = mediawikiService2;
		this.pageService = pageService;
	}
	public MediaWikiConverter(MediawikiService mediawikiService2, PageService pageService, Integer namespace2) {
		this.mediawikiService = mediawikiService2;
		this.namespace = namespace2;
		this.pageService = pageService;
	}
	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Mediawiki.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Mediawiki mediawiki = new Mediawiki();
		List<Mediawiki> allMediawikis = mediawikiService.getAllMediawikis();
		if (!allMediawikis.isEmpty()) {
			mediawiki = allMediawikis.get(0);
		}
		if (reader.getAttribute("lang")!=null) {
//			xml:lang
			System.out.println("el lenguaje es: "+reader.getAttribute("lang"));
			mediawiki.setLang(reader.getAttribute("lang"));
			this.lang = new Locale(mediawiki.getLang());
		}
		reader.moveDown();
		Siteinfo siteinfo = (Siteinfo) context.convertAnother(mediawiki, Siteinfo.class);
		mediawiki.setSiteinfo(siteinfo);
		reader.moveUp();
		mediawiki=mediawikiService.mergeMediawiki(mediawiki);
		Integer pageIndex = 0;
		List<Page> pages= new LinkedList<Page>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			// System.out.println(reader.getNodeName());
			if ("page".equals(reader.getNodeName())) {
				pageIndex++;
//				System.out.println("Page "+pageIndex);
				Page page = (Page) context.convertAnother(mediawiki, Page.class);
				if(namespace!=null){
					//System.out.println("required namespace:"+namespace+" page namespace:"+page.getNs() );
					if (page.getNs().compareTo(namespace)==0) {
						System.out.println("Page "+pageIndex);
						pages.add(page);
					}
				}else{
					System.out.println("Page "+pageIndex);
					pages.add(page);
				}

				
			}
			reader.moveUp();
			if (pageIndex%50 == 0) {
//				mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//				if (!mediawiki.getPages().containsAll(pages)) {
//					mediawiki.getPages().addAll(pages);
//					mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//					pages= new LinkedList<Page>();
//				}
				for (Page page : pages) {
					page.setMediawiki(mediawiki);//---------yo tendria que alcanzar
//					revisionService.createRevision(revision);
				}
				pageService.createAllPages(pages);
				pages= new LinkedList<Page>();
			}
		}
//		mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//		if (!mediawiki.getPages().containsAll(pages)) {
//			mediawiki.getPages().addAll(pages);
//			mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//		}
		for (Page page : pages) {
			page.setMediawiki(mediawiki);//---------yo tendria que alcanzar
//			revisionService.createRevision(revision);
		}
		pageService.createAllPages(pages);
		System.out.println("fin");
		return mediawiki;
	}

	public Locale getLang() {
		return lang;
	}



	
}
