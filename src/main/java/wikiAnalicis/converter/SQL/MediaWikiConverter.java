package wikiAnalicis.converter.SQL;

import java.util.LinkedList;
import java.util.List;

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
import wikiAnalicis.service.CargaDumpService;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;

public class MediaWikiConverter implements Converter {

	private CargaDumpService cargaDumpService;

	public MediaWikiConverter(CargaDumpService cargaDumpService) {
		super();
		this.cargaDumpService = cargaDumpService;
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
		reader.moveDown();
		Siteinfo siteinfo = (Siteinfo) context.convertAnother(mediawiki, Siteinfo.class);
		mediawiki.setSiteinfo(siteinfo);
		reader.moveUp();
		mediawiki = cargaDumpService.createMediaWiki(mediawiki);
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
				this.savePagesInWiki(mediawiki,pages);
				pages= new LinkedList<Page>();
			}
		}
		this.savePagesInWiki(mediawiki,pages);
		System.out.println("fin");
		return mediawiki;
	}

	private void savePagesInWiki(Mediawiki mediawiki, List<Page> pages) {
		cargaDumpService.savePagesInWiki(mediawiki,pages);
		
	}
	

}
