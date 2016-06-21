package wikiAnalicis.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		dropDB();
	    long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    times.put("1- Vaciado de Base de Datos", elapsedTime);
	    
	    
	    startTime = System.currentTimeMillis();
		System.out.println("Cargando:");
		System.out.println(env.getProperty("history.path.test"));	
		XStream xStream = configXStream();
		String historyPath = env.getProperty("history.path.test");
		historyXMLToDB(xStream, historyPath);
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
		List<Page> pages = pageService.getAllPages();
		Integer index =0; 
		for (Page page : pages) {
			page = pageService.mergePage(page);// para que levante revisiones
			List<Revision>revisions= page.getRevisions();
			List<Category> oldCategories = new LinkedList<Category>();
			for (Revision revision : revisions) {
				List<Category> newCategories = categoriesFromText(revision.getText());
//				System.out.println("news "+newCategories.size());
				Map<Category, Boolean> cambiosCategories = diffCategories(oldCategories,newCategories);
//				System.out.println("changes "+cambiosCategories.size());
				for (Category category : cambiosCategories.keySet()) {
					categoryService.getCategory(category.getId());
					if (cambiosCategories.get(category)) { // si se agrego
						InCategory inCategory = new InCategory();
						inCategory.setCategory(category);
						inCategory.setPage(page);
						inCategory.setRevisionStart(revision);
						if (page.getNs().compareTo(14)==0) {//si es categoria
							category.getChildrens().add(inCategory);
							((Category)page).getParents().add(inCategory);
						}
						else {//si es page comun
							category.getPages().add(inCategory);
						}
					} else {//si se elimino
						InCategory inCategory = new InCategory();
						if (page.getNs().compareTo(14)==0) {//si es categoria
							inCategory = category.getActiveChildren(page);
							inCategory.setRevisionEnd(revision);
							inCategory = ((Category)page).getActiveChildren(page);
							inCategory.setRevisionEnd(revision);
						}
						else {//si es page comun
							inCategory = category.getActiveChildren(page);
							inCategory.setRevisionEnd(revision);
						}
					}
//					System.out.println("children "+category.getChildrens().size());
//					System.out.println("pages "+category.getPages().size());
//					System.out.println("parents "+category.getParents().size());
					categoryService.mergeCategory(category);
				}
			}
			index++;
			System.out.println(index+" Page: "+page.getId()+" Is Category: "+page.isCategory());
		}
		//para cada pagina
			//Set de categorias viejas vacio
			//por cada revision
				//si texto no es nulo
					//Parseo Texto-obtengo categorias nuevas
				//Diff categorias viejas vs nuevas map<Categoria,agregada o quitada> 
				//por cada uno del map
					//busco categoria
					//si es nula continue
					//si es agregada 
						//creo el incategory
						//Asigno la categoria en el incategory
						//Asigno in category en la lista segun si es pagina o categoria(si es categoria tambien en la de padres)
					//Si es Eliminada
						//Busco el incateogory en la categoria en la lista correspondiente
						//seteo la revision final
				//Categorias viejas = nuevas
//		for (Page page : pages) {
//			index++;
//			System.out.println(index+" Page: "+page.getId()+" Is Category: "+page.isCategory());
//		}
	}
	private Map<Category, Boolean> diffCategories(List<Category> oldCategories,
			List<Category> newCategories) {
		// TODO Auto-generated method stub
		Set<Category> oldC = new HashSet<Category>(oldCategories);
		Set<Category> newC = new HashSet<Category>(newCategories);
		Set<Category> allC = new HashSet<Category>(newCategories);
		allC.addAll(oldC);
		Set<Category> interseC = new HashSet<Category>(newCategories);
		interseC.retainAll(oldC);
		allC.removeAll(interseC);
		Map<Category, Boolean> map= new HashMap<Category, Boolean>();
		for (Category category : allC) {
			if (!oldCategories.contains(category)) {
				map.put(category, true);
			} else {
				map.put(category, false);
			}
		}
		
		return map;
	}
	private List<Category> categoriesFromText(String text) {
		// TODO controlar categorias que no existen
		Pattern pattern = Pattern.compile(Pattern.quote("[[Catego") + "(.*?)" + Pattern.quote("]]"));
	  //  Pattern pattern = Pattern.compile("[[Categoría\\:(.*?)]]");
	    Matcher matcher = pattern.matcher(text);
	    while (matcher.find()) {
	    	String str = matcher.group(1);
			System.out.println(str);
	    	System.out.println(str.substring(4, str.length()));
	        //System.out.println(matcher.group(1));
	    }
		List<Category> categories = new LinkedList<Category>();
		categories.addAll(randomSample4(categoryService.getAllCategorys(), 5));
		
		return categories;
	}
	public static <T> Set<T> randomSample4(List<T> items, int m){
	    HashSet<T> res = new HashSet<T>(m);
	    int n = items.size();
	    Random rnd = new Random();
	    for(int i=n-m;i<n;i++){
	        int pos = rnd.nextInt(i+1);
	        T item = items.get(pos);
	        if (res.contains(item))
	            res.add(items.get(i));
	        else
	            res.add(item);
	    }
	    return res;
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
