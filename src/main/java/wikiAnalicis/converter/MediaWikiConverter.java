package wikiAnalicis.converter;

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
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;

public class MediaWikiConverter implements Converter {

	private MediawikiService mediawikiService;

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
		reader.moveDown();
		Siteinfo siteinfo = (Siteinfo) context.convertAnother(mediawiki, Siteinfo.class);
		mediawiki.setSiteinfo(siteinfo);
		// System.out.println("pages");
		reader.moveUp();
		// System.out.println(reader.getNodeName());
		mediawiki.setPages(new LinkedList<Page>());
		mediawikiService.mergeMediawiki(mediawiki);
		mediawiki = mediawikiService.getAllMediawikis().get(0);
		Integer pageIndex = 0;
		List<Page> pages= new LinkedList<Page>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			// System.out.println(reader.getNodeName());
			if ("page".equals(reader.getNodeName())) {
				pageIndex++;
				System.out.println("Page "+pageIndex);
				//Page page = (Page) context.convertAnother(mediawiki, Page.class);
				context.convertAnother(null, Page.class);
				//pages.add(page);
				//mediawiki.getPages().add(page);
			}
			reader.moveUp();
			if (pageIndex%100 == 0) {
//				mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//				mediawiki.getPages().addAll(pages);
//				mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//				//mediawikiService.addPagesTo(mediawiki, pages);
//				mediawiki=mediawikiService.getMediawiki(mediawiki.getId());
				pages= new LinkedList<Page>();
			}
		}
//		mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//		mediawiki.getPages().addAll(pages);
//		mediawiki=mediawikiService.mergeMediawiki(mediawiki);
//		//mediawikiService.addPagesTo(mediawiki, pages);
//		mediawiki=mediawikiService.getMediawiki(mediawiki.getId());
		System.out.println("fin");
		// TODO Auto-generated method stub
		// Cargo datos basicos y lista vacia
		// Guardo
		// null
		// Mientras haya revisiones
		// Recupero el que null
		// cargo revicion
		// agrego a la lista
		// actualizo
		// null denuevo
		// fin
		// retorno con basicos
		return mediawiki;
	}

}
