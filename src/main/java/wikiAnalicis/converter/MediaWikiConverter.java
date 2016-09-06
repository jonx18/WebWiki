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
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;

public class MediaWikiConverter implements Converter {

	private MediawikiService mediawikiService;
	private Locale lang;
	public MediaWikiConverter(MediawikiService mediawikiService) {
		super();
		this.mediawikiService = mediawikiService;
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
		Integer pageIndex = 0;
		List<Page> pages= new LinkedList<Page>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			// System.out.println(reader.getNodeName());
			if ("page".equals(reader.getNodeName())) {
				pageIndex++;
				System.out.println("Page "+pageIndex);
				Page page = (Page) context.convertAnother(mediawiki, Page.class);
				pages.add(page);
			}
			reader.moveUp();
			if (pageIndex%100 == 0) {
				mediawiki=mediawikiService.mergeMediawiki(mediawiki);
				if (!mediawiki.getPages().containsAll(pages)) {
					mediawiki.getPages().addAll(pages);
					mediawiki=mediawikiService.mergeMediawiki(mediawiki);
					pages= new LinkedList<Page>();
				}
				
			}
		}
		mediawiki=mediawikiService.mergeMediawiki(mediawiki);
		if (!mediawiki.getPages().containsAll(pages)) {
			mediawiki.getPages().addAll(pages);
			mediawiki=mediawikiService.mergeMediawiki(mediawiki);
		}

		System.out.println("fin");
		return mediawiki;
	}

	public Locale getLang() {
		return lang;
	}



	
}
