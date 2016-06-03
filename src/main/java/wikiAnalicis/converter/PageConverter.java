package wikiAnalicis.converter;
import java.util.LinkedList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.service.PageService;
public class PageConverter implements Converter{
	private PageService pageService;
	
	public PageConverter(PageService pageService) {
		super();
		this.pageService = pageService;
	}

	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Page.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Page page = new Page();
        reader.moveDown();
        System.out.println(reader.getNodeName());
        System.out.println("titulo"+reader.getValue());
        page.setTitle(reader.getValue());
        reader.moveUp();
        reader.moveDown();
        System.out.println(reader.getNodeName());
        System.out.println("ns"+reader.getValue());
        page.setNs(new Integer(reader.getValue()));
        reader.moveUp();
        reader.moveDown();
        System.out.println(reader.getNodeName());
        System.out.println("id"+reader.getValue());
        page.setId(new Long(reader.getValue()));
        reader.moveUp();
        page.setRevisions(new LinkedList<Revision>());
        pageService.mergePage(page);
        page= pageService.getPage(page.getId());
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            System.out.println(reader.getNodeName());
            if ("revision".equals(reader.getNodeName())) {
                    Revision revision = (Revision)context.convertAnother(page, Revision.class);
                    page.getRevisions().add(revision);
            } 
            reader.moveUp();
            pageService.mergePage(page);
            page= pageService.getPage(page.getId());
    }
		
		
		
		return page;
	}

}
