package wikiAnalicis.converter;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
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
		String text = reader.getValue();
		text = removeAccents(text);
		page.setTitle(text);
		reader.moveUp();
		reader.moveDown();
		page.setNs(new Integer(reader.getValue()));
		reader.moveUp();
		reader.moveDown();
		page.setId(new Long(reader.getValue()));
		reader.moveUp();
		if (page.getNs().compareTo(14)==0) {
			page=pageToCategory(page);
		}
		Page pageExistence = pageService.getPage(page.getId());
		if (pageExistence != null) {
			page = pageService.mergePage(pageExistence);
		}else{
			page.setId(pageService.createPage(page));
		}
		

		Integer indexRevision = 0;
		Integer charused = 0;
		List<Revision> revisions = new LinkedList<Revision>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();

			if ("revision".equals(reader.getNodeName())) {
				indexRevision++;
				Revision revision = (Revision) context.convertAnother(page, Revision.class);
				//Limitador de fecha retirar para version completa
//				Calendar calendar= Calendar.getInstance();
//						calendar.set(2010, Calendar.APRIL, 1);
//				if (revision.getTimestamp().after(calendar.getTime())) {
//					reader.moveUp();
//					break;
//					}
				revisions.add(revision);
			}else{
				if ("redirect".equals(reader.getNodeName())) {
					page.setRedirect(reader.getAttribute("title"));
					reader.moveUp();
					continue;
				}
			}
			reader.moveUp();
			if ((indexRevision % 50 == 0)){
				System.out.println("revisiones index: "+indexRevision+"en lista:"+revisions.size() +" caracteres: "+charused);
				for (Revision revision : revisions) {
					revision.setPage(page);//---------yo tendria que alcanzar + -Xms4096m -Xmx8192m -XX:PermSize=128m -XX:MaxPermSize=512m
//					revisionService.createRevision(revision);
				}
				revisionService.createAllRevisions(revisions);
				//--------------Comentame a ver que pasa
				
//				page=pageService.mergePage(page);
//				page.getRevisions().addAll(revisions);
//				page=pageService.mergePage(page);

				revisions = new LinkedList<Revision>();
			}
		}
		for (Revision revision : revisions) {
			revision.setPage(page);//---------yo tendria que alcanzar
//			revisionService.createRevision(revision);
		}
		revisionService.createAllRevisions(revisions);
		//--------------Comentame a ver que pasa
//		page=pageService.mergePage(page);
//		page.getRevisions().addAll(revisions);
//		page=pageService.mergePage(page);
//		if (page.getRevisions().isEmpty()) {
//			System.out.println("borrando");
//			pageService.deletePage(page.getId());
//			page=null;
//		}
		return page;
	}
	private Category pageToCategory(Page page) {
		Category category = new Category();
		category.setId(page.getId());
		category.setNs(page.getNs());
		category.setRedirect(page.getRedirect());
		category.setRevisions(page.getRevisions());
		category.setTitle(page.getTitle());
		return category;
	}
	public static String removeAccents(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}
}
