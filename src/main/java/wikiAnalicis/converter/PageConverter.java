package wikiAnalicis.converter;

import java.util.LinkedList;
import java.util.List;

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
import wikiAnalicis.service.RevisionService;

public class PageConverter implements Converter {
	private PageService pageService;
	private RevisionService revisionService;

	public PageConverter(PageService pageService) {
		super();
		this.pageService = pageService;
	}

	public PageConverter(PageService pageService, RevisionService revisionService) {
		super();
		this.pageService = pageService;
		this.revisionService = revisionService;
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
		// System.out.println(reader.getNodeName());
		// System.out.println("titulo"+reader.getValue());
		page.setTitle(reader.getValue());
		reader.moveUp();
		reader.moveDown();
		// System.out.println(reader.getNodeName());
		// System.out.println("ns"+reader.getValue());
		page.setNs(new Integer(reader.getValue()));
		reader.moveUp();
		reader.moveDown();
		// System.out.println(reader.getNodeName());
		// System.out.println("id"+reader.getValue());
		page.setId(new Long(reader.getValue()));
		reader.moveUp();
		
		page.setRevisions(new LinkedList<Revision>());
		pageService.mergePage(page);
		page = pageService.getPage(page.getId());
		Integer indexRevision = 0;
		Integer charused = 0;
		List<Revision> revisions = new LinkedList<Revision>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			//System.out.println(reader.getNodeName());
			if ("revision".equals(reader.getNodeName())) {
				indexRevision++;
				Revision revision = (Revision) context.convertAnother(page, Revision.class);
				//charused+=revision.getText().length();
				//page.getRevisions().add(revision);
				revision.setPage(page);
				revisions.add(revision);
			}else{
				if ("redirect".equals(reader.getNodeName())) {
					page.setRedirect(reader.getAttribute("title"));
					reader.moveUp();
					continue;
				}
			}
			reader.moveUp();
			//if ((indexRevision % 100 == 0)||(charused>1500000)) {
			if ((indexRevision % 100 == 0)){
				//System.out.println("revisiones index: "+indexRevision+"en lista:"+revisions.size() +" caracteres: "+charused);
				page.getRevisions().addAll(revisions);
				page=pageService.mergePage(page);
				//charused=0;
				revisions = new LinkedList<Revision>();
				page = pageService.getPage(page.getId());
			}
//			if (page.getId()==11) {//salteo pag 7 por el tamaño
//				break;
//			}
		}
		page.getRevisions().addAll(revisions);
		page=pageService.mergePage(page);
		page = pageService.getPage(page.getId());
		//pageService.addRevisionsTo(page, revisions);
		return page;
	}

}
