package wikiAnalicis.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import wikiAnalicis.converter.MediaWikiConverter;
import wikiAnalicis.converter.NamespaceConverter;
import wikiAnalicis.converter.PageConverter;
import wikiAnalicis.converter.RevisionConverter;
import wikiAnalicis.converter.UserContributorConverter;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.service.CategoryService;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;
import wikiAnalicis.service.UserContributorService;
import wikiAnalicis.util.diff_match_patch;
import wikiAnalicis.util.diff_match_patch.Diff;
@Controller
@PropertySource({ "classpath:historyPath.properties" })
public class DumpToBDController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private MediawikiService mediawikiService;
	@Autowired
	private PageService pageService;
	@Autowired
	private CategoryService categoryService; 
	@Autowired
	private RevisionService revisionService;
	@Autowired
	private UserContributorService userContributorService;
	@Autowired
	private Environment env;
	
	@RequestMapping("dumptobd")
	public ModelAndView dumpToBD() {
		Map<String, Long> times = new HashMap<String, Long>();
		
		long startTime = System.currentTimeMillis();
		//dropDB();
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    times.put("1- Vaciado de Base de Datos", elapsedTime);
	    
	    
	    startTime = System.currentTimeMillis();
		System.out.println("Cargando:");
		System.out.println(env.getProperty("history.path.test"));	
		XStream xStream = configXStream();
		String historyPath = env.getProperty("history.path");
		//historyXMLToDB(xStream, historyPath);
		//pagesWithoutRevisions();
		System.out.println("Finalizo guardado");
	    stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    times.put("2- Carga de: "+historyPath, elapsedTime);
		
		//dropDB();
		//aca van masprocesamintos
	    
	    startTime = System.currentTimeMillis();
	    asignacionCategorias();
	    stopTime = System.currentTimeMillis();
	    elapsedTime = stopTime - startTime;
	    times.put("3- Asignacion de Categorias", elapsedTime);
		ModelAndView model = new ModelAndView("dumptodb");
		model.addObject("result", times);
		return model;

	}
	private void asignacionCategorias() {
		// TODO Auto-generated method stub
		//para cada pagina
			//Set de categorias viejas vacio
			//por cada revision
				//si texto no es nulo
					//Parseo Texto-obtengo categorias nuevas
				//Diff categorias viejas vs nuevas map<Categoria,agregada o quitada> 
				//por cada uno del map
					//busco categoria
					//si es agregada 
						//creo el incategory
						//Asigno la categoria en el incategory
						//Asigno in category en la lista segun si es pagina o categoria(si es categoria tambien en la de padres)
					//Si es Eliminada
						//Busco el incateogory en la categoria en la lista correspondiente
						//seteo la revision final
				//Categorias viejas = nuevas
		List<Page> pages = pageService.getAllPages();
		Integer index =0; 
		for (Page page : pages) {
			index++;
			System.out.println(index+" Page: "+page.getId()+" Is Category: "+page.isCategory());
		}
	}
	private void pagesWithoutRevisions() {
		List<Page> pages = pageService.getAllPages();
		for (Page page : pages) {
			page = pageService.mergePage(page);
			if (page.getRevisions().isEmpty()) {
				pageService.deletePage(page.getId());
			}
			else{
				System.out.println(page.getRevisions().size());
			}
		}
	}
	private void dropDB() {
		System.out.println("Dropeando Mediawiki");
		for (Mediawiki mediawiki : mediawikiService.getAllMediawikis()) {
			System.out.println(mediawiki.getSiteinfo().getSitename());
			mediawikiService.deleteMediawiki(mediawiki.getId());
		}
		mediawikiService.truncateAll();
	}
	private void historyXMLToDB(XStream xStream, String historyPath) {
		Mediawiki mediawiki=null;
		try {
			mediawiki = (Mediawiki)xStream.fromXML(new FileInputStream(historyPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediawikiService.mergeMediawiki(mediawiki);
	}
	private XStream configXStream() {
		//configuro xstream
		XStream xStream = new XStream(new StaxDriver());
		xStream.alias("revision", Revision.class);
		xStream.alias("page", Page.class);
		xStream.addImplicitCollection(Page.class, "revisions");
		xStream.alias("siteinfo", Siteinfo.class);
		xStream.aliasField("case", Siteinfo.class, "casee");
		xStream.alias("namespace", Namespace.class);
		xStream.alias("mediawiki", Mediawiki.class);
		xStream.addImplicitCollection(Mediawiki.class, "pages");
		
		//converters
		xStream.registerConverter(new MediaWikiConverter(mediawikiService));
		xStream.registerConverter(new PageConverter(pageService));
		xStream.registerConverter(new NamespaceConverter());
		xStream.registerConverter(new RevisionConverter());
		xStream.registerConverter(new UserContributorConverter(userContributorService));
		return xStream;
	}


}
