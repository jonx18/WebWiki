package wikiAnalicis.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import wikiAnalicis.converter.NamespaceConverter;
import wikiAnalicis.converter.RevisionConverter;
import wikiAnalicis.converter.MediaWikiConverter;
import wikiAnalicis.converter.PageConverter;
import wikiAnalicis.converter.UserContributorConverter;
import wikiAnalicis.entity.Category;
import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.entity.diffAndStyles.Delimiter;
import wikiAnalicis.entity.statics.PageStatistics;
import wikiAnalicis.service.CargaDumpService;
import wikiAnalicis.service.CategoryService;
import wikiAnalicis.service.EmailService;
import wikiAnalicis.service.InCategoryService;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;
import wikiAnalicis.service.StatisticsService;
import wikiAnalicis.service.UserContributorService;

@Controller
@PropertySource({ "classpath:historyPath.properties" })
public class DumpToBDController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MediawikiService mediawikiService;
	@Autowired
	private PageService pageService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private InCategoryService inCategoryService;
	@Autowired
	private RevisionService revisionService;
	@Autowired
	private UserContributorService userContributorService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private Environment env;
	@Autowired
	private StatisticsService statisticsService;
	private Mediawiki mediawiki;
	

	@RequestMapping(value = "exportall", method = RequestMethod.GET)
	public String exportAll(Long id,HttpServletRequest request) {
		List<Page> pages = pageService.getAllPages();
		for (Page page : pages) {
			page = pageService.mergePage(page);
			PageStatistics pageStatistics = statisticsService.getPageStatistics(page);
			if (pageStatistics==null) {
				continue;
			}
			Map<Delimiter, Integer[]> mapStyleChanges=pageStatistics.getMapStyleChanges();
			Date[] dates=pageStatistics.getDatesArray();
			Long totalRevisiones = pageStatistics.getTotalRevisiones();
			Map<String, Long> distribucionDeAporte = pageStatistics.getDistribucionDeAporte();
			Map<Date, Long> revisionesDia = pageStatistics.getRevisionesDia();
			Map<Date, Long> contenidoDia = pageStatistics.getContenidoDia();
			Map<Date, String[]> categoriesNames = pageStatistics.getCategoriesNames();
			Gson gson = new Gson();
			String sdates = gson.toJson(dates, dates.getClass());
			String smapStyleChanges = gson.toJson(mapStyleChanges, mapStyleChanges.getClass());
			String stotalRevisiones = gson.toJson(totalRevisiones, totalRevisiones.getClass());
			String sdistribucionDeAporte = gson.toJson(distribucionDeAporte, distribucionDeAporte.getClass());
			String srevisionesDia = gson.toJson(revisionesDia, revisionesDia.getClass());
			String scontenidoDia = gson.toJson(contenidoDia, contenidoDia.getClass());
			String scategories = gson.toJson(categoriesNames, categoriesNames.getClass());
			String name = gson.toJson(page.getTitle(), page.getTitle().getClass());
			try{
			    //PrintWriter writer = new PrintWriter("C:\\Users\\Jonx\\Downloads\\WikiAnalisis\\exports\\page"+page.getId()+".txt", "UTF-8");
				 PrintWriter writer = new PrintWriter("B:\\exports\\page"+page.getId()+".txt", "UTF-8");
			    writer.println(sdates);
			    writer.println(smapStyleChanges);
			    writer.println(stotalRevisiones);
			    writer.println(sdistribucionDeAporte);
			    writer.println(srevisionesDia);
			    writer.println(scontenidoDia);
			    writer.println(scategories);
			    writer.println(name);
			    writer.close();
			} catch (Exception e) {
			   // do something
			}
		}

		return "forward:/index";
	}
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public String export(Long id,HttpServletRequest request) {
		Page page = pageService.getPage(id);
		page = pageService.mergePage(page);
		PageStatistics pageStatistics = statisticsService.getPageStatistics(page);
		Map<Delimiter, Integer[]> mapStyleChanges=pageStatistics.getMapStyleChanges();
		Date[] dates=pageStatistics.getDatesArray();
		Long totalRevisiones = pageStatistics.getTotalRevisiones();
		Map<String, Long> distribucionDeAporte = pageStatistics.getDistribucionDeAporte();
		Map<Date, Long> revisionesDia = pageStatistics.getRevisionesDia();
		Map<Date, Long> contenidoDia = pageStatistics.getContenidoDia();
		Map<Date, String[]> categoriesNames = pageStatistics.getCategoriesNames();
		Gson gson = new Gson();
		String sdates = gson.toJson(dates, dates.getClass());
		String smapStyleChanges = gson.toJson(mapStyleChanges, mapStyleChanges.getClass());
		String stotalRevisiones = gson.toJson(totalRevisiones, totalRevisiones.getClass());
		String sdistribucionDeAporte = gson.toJson(distribucionDeAporte, distribucionDeAporte.getClass());
		String srevisionesDia = gson.toJson(revisionesDia, revisionesDia.getClass());
		String scontenidoDia = gson.toJson(contenidoDia, contenidoDia.getClass());
		String scategories = gson.toJson(categoriesNames, categoriesNames.getClass());
		String name = gson.toJson(page.getTitle(), page.getTitle().getClass());
		try{
		    PrintWriter writer = new PrintWriter("C:\\Users\\Jonx\\Downloads\\WikiAnalisis\\page"+id+".txt", "UTF-8");
		    writer.println(sdates);
		    writer.println(smapStyleChanges);
		    writer.println(stotalRevisiones);
		    writer.println(sdistribucionDeAporte);
		    writer.println(srevisionesDia);
		    writer.println(scontenidoDia);
		    writer.println(scategories);
		    writer.println(scategories);
		    writer.close();
		} catch (Exception e) {
		   // do something
		}


		return "forward:/index";
	}
	@RequestMapping(value = "importPage", method = RequestMethod.GET)
	public String importPage(Long id,HttpServletRequest request) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream("C:\\Users\\Jonx\\Downloads\\WikiAnalisis\\page"+id+".txt"), "UTF-8"));
		} catch (UnsupportedEncodingException | FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Page page = pageService.getPage(id);
		page = pageService.mergePage(page);
		PageStatistics pageStatistics = statisticsService.getPageStatistics(page);
		if (pageStatistics==null) {
			pageStatistics= new PageStatistics();
			pageStatistics.setPage(page);
			statisticsService.createPageStatistics(pageStatistics);
		}
		Gson gson = new Gson();
		Date[] dates = new Date[1];
		Map<Delimiter, Integer[]> mapStyleChanges=new HashMap<Delimiter, Integer[]>();
		Long totalRevisiones = new Long(0);
		Map<String, Long> distribucionDeAporte = new HashMap<String, Long>();
		Map<Date, Long> revisionesDia = new HashMap<Date, Long>();
		Map<Date, Long> contenidoDia = new HashMap<Date, Long>();
		List<InCategory> categories = new LinkedList<InCategory>();
		try {
			dates = gson.fromJson(in.readLine(), dates.getClass());
			
			Map<String, ArrayList<Double>> mapStyleChanges2= gson.fromJson(in.readLine(), mapStyleChanges.getClass());
			for (String key : mapStyleChanges2.keySet()) {
				Integer[] integers= new Integer[mapStyleChanges2.get(key).size()];
				for (int i = 0; i < integers.length; i++) {
		    		Integer numero =mapStyleChanges2.get(key).get(i).intValue();
					integers[i] = numero ;
					
				}
	    		mapStyleChanges.put(Delimiter.valueOf(key.toUpperCase()), integers);
			}
			totalRevisiones = gson.fromJson(in.readLine(), totalRevisiones.getClass());
			Map<String, Double> temporalParaFechas = gson.fromJson(in.readLine(), distribucionDeAporte.getClass());
			for (String key : temporalParaFechas.keySet()) {
	    		Long numero =new Long (temporalParaFechas.get(key).intValue());
	    		distribucionDeAporte.put(key, numero);
			}
			temporalParaFechas = gson.fromJson(in.readLine(), revisionesDia.getClass());
			for (String key : temporalParaFechas.keySet()) {
	    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault());
	    		Date date = null;
	    		try {
	    			date = format.parse(key.toString());
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		
	    		Long numero =new Long (temporalParaFechas.get(key).intValue());
				revisionesDia.put(date, numero);
			}
			 
			temporalParaFechas = gson.fromJson(in.readLine(), contenidoDia.getClass());
			for (String key : temporalParaFechas.keySet()) {
	    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault());
	    		Date date = null;
	    		try {
	    			date = format.parse(key.toString());
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		Long numero =new Long (temporalParaFechas.get(key).intValue());
	    		contenidoDia.put(date, numero);
			}
			categories = gson.fromJson(in.readLine(), categories.getClass());

		} catch (JsonSyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageStatistics.setDates(Arrays.asList(dates));
		
		pageStatistics.setMapStyleChanges(mapStyleChanges);
		pageStatistics.setTotalRevisiones(totalRevisiones);
		pageStatistics.setDistribucionDeAporte(distribucionDeAporte);
		pageStatistics.setRevisionesDia(revisionesDia);
		pageStatistics.setContenidoDia(contenidoDia);
		pageStatistics.setCategories(categories);
		LOGGER.info("Mostrando Diff. Data : " + gson.toJson(pageStatistics.getRevisionesDia()));
		//LOGGER.info("Mostrando Diff. Data : " + gson.toJson(pageStatistics.getContenidoDia()));
		statisticsService.mergePageStatistics(pageStatistics);
		

		



		return "forward:/index";
	}

	@RequestMapping(value = "dumptobd", method = RequestMethod.GET)
	public ModelAndView dumpToBD(HttpServletRequest request) {
		Map<String, Long> times = new TreeMap<String, Long>();

		long startTime = System.currentTimeMillis();
		//dropDB();
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		times.put("1-dropDB", elapsedTime);

		startTime = System.currentTimeMillis();
		System.out.println("Cargando:");
		System.setProperty("jdk.xml.entityExpansionLimit", "0");
		System.setProperty("jdk.xml.totalEntitySizeLimit", "0");
		XStream xStream = configXStream(true,0);
		String historyPath = env.getProperty("history.path.shorlong");
		System.out.println(historyPath);
		try {
			mediawiki = historyXMLToDB(xStream, new FileInputStream(historyPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// pagesWithoutRevisions();
		System.out.println("Finalizo guardado");
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		times.put("2-historyXMLToDB-" + historyPath, elapsedTime);

		// dropDB();
		// aca van masprocesamintos

		startTime = System.currentTimeMillis();
		//asignacionCategorias();
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		times.put("3-asignacionCategorias", elapsedTime);
		//Locale locale = new Locale(mediawiki.getLang());
		Locale locale = localeResolver.resolveLocale(request);
		Map<String, Long> timesLabeled = new TreeMap<String, Long>();
		int index=0;
		for (String label : times.keySet()) {
			index++;
			timesLabeled.put(index+"- "+messageSource.getMessage("dumptodb.table."+(label.split("-")[1]), null, locale), times.get(label));
		}
		
		ModelAndView model = new ModelAndView("dumptodb");
		model.addObject("result", timesLabeled);
		return model;

	}
	@RequestMapping(value = "wikidrop", method = RequestMethod.GET)
	public ModelAndView wikidrop(HttpServletRequest request) {
		ModelAndView model = new ModelAndView("index");
		dropDB();
		return model;
	}
	@RequestMapping(value = "delrevisions")
	public String delRevisions(HttpServletRequest request ) {
		List<Page> pages = pageService.getAllPages();
		for (Page p : pages) {
			p=pageService.mergePage(p);
			List<Revision> rp = p.getRevisions();
			for (Revision revision : rp) {
				revisionService.deleteRevision(revision.getId());
			}
		}
		return "forward:/index";
	}
	@RequestMapping(value = "updaterevisions")
	public String updateRevisions(HttpServletRequest request ) {
		String userpc = System.getProperty("user.name");
		String hostname = "Unknown";
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		long startTimeFull = System.currentTimeMillis();
		List<Page> pages = pageService.getAllPages();
		for (Page p : pages) {
//			p=pageService.mergePage(p);
//			List<Revision> rp = p.getRevisions();
//			for (Revision revision : rp) {
//				revisionService.deleteRevision(revision.getId());
//			}
			
			String pagename= p.getRealTitle();
			if (pagename.trim().isEmpty()) {
				System.out.println("empty");
				continue;
			}
			
			try{
				request.setAttribute("pagename", pagename);
				this.urlToBD(request);
				request.setAttribute("drop", false);
				Page page = pageService.getPage(pagename);
				if (page == null) {
					String title = pagename.replace('_', ' ');
//					System.out.println(pagename);
//					System.out.println(title);
					page = pageService.getPage(title);
					if (page == null) {
						title = this.removeAccents(title);
						page = pageService.getPage(title);
					}
				}
				
				System.out.println("atributo--------------"+request.getAttribute("id"));
				if (request.getAttribute("id")==null) {
					request.setAttribute("id", page.getId());
				}else{
					request.setAttribute("id",request.getAttribute("id")+"\n"+ page.getId());
				}

				System.out.println("atributo--------------"+request.getAttribute("id"));
				} catch (Exception e) {
					long stopTimeFull = System.currentTimeMillis();
					long elapsedTimeFull = stopTimeFull - startTimeFull;
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(stopTimeFull);
					String fuente = "wikianalisis@gmail.com";
					String destino = "jonamar10@hotmail.com";
					String asunto = "ERROR de proceso urlToBDWithRedirection pagina: "+pagename+ " en "+hostname+":"+userpc;
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					String mensaje = "El proceso Fallo a las: "+calendar.getTime()+"\n "
							+ "Tardo:"+ elapsedTimeFull+" milisegundos\n "+"stack:\n "+sw.toString();
					LOGGER.info("error: " +asunto+" \n"+ mensaje);
					//emailService.enviar(fuente, destino, asunto, mensaje);
					
				}
			
		}

		return "forward:/statisticsPageOfWithRedirection";
	}
	@RequestMapping(value = "urlToBDWithRedirection", method = RequestMethod.POST)
	public String urlToBDWithRedirection(HttpServletRequest request ) {
		String userpc = System.getProperty("user.name");
		String hostname = "Unknown";
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		long startTimeFull = System.currentTimeMillis();
		Scanner scanner = new Scanner(request.getParameter("pagename"));
		while (scanner.hasNextLine()) {
			String pagename= scanner.nextLine().replace("\n", "").replace("\r", "");
			if (pagename.trim().isEmpty()) {
				System.out.println("empty");
				continue;
			}
			
			try{
				request.setAttribute("pagename", pagename);
				this.urlToBD(request);
				request.setAttribute("drop", false);
				Page page = pageService.getPage(pagename);
				if (page == null) {
					String title = pagename.replace('_', ' ');
//					System.out.println(pagename);
//					System.out.println(title);
					page = pageService.getPage(title);
					if (page == null) {
						title = this.removeAccents(title);
						page = pageService.getPage(title);
					}
				}
				
				System.out.println("atributo--------------"+request.getAttribute("id"));
				if (request.getAttribute("id")==null) {
					request.setAttribute("id", page.getId());
				}else{
					request.setAttribute("id",request.getAttribute("id")+"\n"+ page.getId());
				}

				System.out.println("atributo--------------"+request.getAttribute("id"));
				} catch (Exception e) {
					long stopTimeFull = System.currentTimeMillis();
					long elapsedTimeFull = stopTimeFull - startTimeFull;
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(stopTimeFull);
					String fuente = "wikianalisis@gmail.com";
					String destino = "jonamar10@hotmail.com";
					String asunto = "ERROR de proceso urlToBDWithRedirection pagina: "+pagename+ " en "+hostname+":"+userpc;
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					String mensaje = "El proceso Fallo a las: "+calendar.getTime()+"\n "
							+ "Tardo:"+ elapsedTimeFull+" milisegundos\n "+"stack:\n "+sw.toString();
					emailService.enviar(fuente, destino, asunto, mensaje);
					
				}
			
		}

		return "forward:/statisticsPageOfWithRedirection";
	}
	
	@RequestMapping(value = "urltobd", method = RequestMethod.POST)
	public ModelAndView urlToBD(HttpServletRequest request ) {
		String pagename;
		if (request.getAttribute("pagename")==null) {
			pagename= request.getParameter("pagename");
		}else{
			pagename= (String) request.getAttribute("pagename");
		}

		System.out.println(request.getParameter("drop"));
		System.out.println(pagename);
		//--------------- send an email
		String userpc = System.getProperty("user.name");
		String hostname = "Unknown";
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		long startTimeFull = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTimeFull);
		String fuente = "wikianalisis@gmail.com";
		String destino = "jonamar10@hotmail.com";
		String asunto = "Comienzo de proceso urltodb pagina: "+pagename+ " en "+hostname+":"+userpc;
		String mensaje = "El proceso comenzo a las: "+calendar.getTime() ;
		//emailService.enviar(fuente, destino, asunto, mensaje);
		
		//--------------
		Map<String, Long> times = new TreeMap<String, Long>();
		int step = 0;
		long startTime ;
		long stopTime;
		long elapsedTime;
		Boolean drop=true;
		if (request.getAttribute("pagename")!=null) {
			drop=false;
		}
		if (request.getParameter("drop")!=null&& drop) {
		startTime = System.currentTimeMillis();
		dropDB();
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		step++;
		times.put(step+"-dropDB", elapsedTime);
		}
//--------------------------------------------------------------------------------------------------
		startTime = System.currentTimeMillis();
		System.out.println("Cargando: " + pagename);
		XStream xStream = configXStream(true,0);
		InputStream historyPath=null;
		historyPath = downloadMainPage(pagename, xStream, historyPath);
		// pagesWithoutRevisions();
		System.out.println("Finalizo guardado");
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		step++;
		times.put(step+"-urlToDB-" + historyPath, elapsedTime);
		//--------------------------------------------------------------------------------------------------------------


		startTime = System.currentTimeMillis();
		//downloadCategories(pagename, xStream, historyPath);
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		step++;
		times.put(step+"-descargaCategorias", elapsedTime);
//-----------------------------------------------------------------------------------------------------------------		
		
		startTime = System.currentTimeMillis();
		//asignacionCategorias();
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		step++;
		times.put(step+"-asignacionCategorias", elapsedTime);
		//Locale locale = new Locale(mediawiki.getLang());
		Locale locale = localeResolver.resolveLocale(request);
		Map<String, Long> timesLabeled = new TreeMap<String, Long>();
		int index=0;
		for (String label : times.keySet()) {
			index++;
			timesLabeled.put(index+"- "+messageSource.getMessage("urltobd.table."+(label.split("-")[1]), null, locale), times.get(label));
		}
		
		//--------------- send an email
		long stopTimeFull = System.currentTimeMillis();
		long elapsedTimeFull = stopTimeFull - startTimeFull;
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stopTimeFull);
		fuente = "wikianalisis@gmail.com";
		destino = "jonamar10@hotmail.com";
		asunto = "Finalizacion de proceso urltodb pagina: "+pagename+ " en "+hostname+":"+userpc;
		mensaje = "El proceso termino a las: "+calendar.getTime()+"\n "
				+ "Tardo:"+ elapsedTimeFull+" milisegundos" ;
		//emailService.enviar(fuente, destino, asunto, mensaje);

		
		//--------------
		
		
		ModelAndView model = new ModelAndView("dumptodb");
		model.addObject("result", timesLabeled);
		return model;

	}
	/**
	 * @param pagename
	 * @param xStream
	 * @param historyPath
	 */
	private void downloadCategories(String pagename, XStream xStream, InputStream historyPath) {
		Page page = pageService.getPage(pagename);
		if (page == null) {
			String title = pagename.replace('_', ' ');
//			System.out.println(pagename);
//			System.out.println(title);
			page = pageService.getPage(title);
			if (page == null) {
				title = this.removeAccents(title);
				page = pageService.getPage(title);
			}
		}
		page = pageService.mergePage(page);
		List<Revision> revisions = page.getRevisions();
		for (Revision revision : revisions) {
//			List<String> categoriesNames = this.categoriesNamesFromText(revision.getText());
//			String[] names = new String[categoriesNames.size()];
//			names= categoriesNames.toArray(names);
//			revision.setCategoryNames(names);
//			revisionService.updateRevision(revision);
			List<String> categoriesNames = Arrays.asList(revision.getCategoryNames());
			List<String> newsCategories = new LinkedList<String>(); 
			for (String string : categoriesNames) {
				Category category = categoryService.getCategory(string);
				if (category == null) {
					// System.out.println("categoria recuperada id:
					// "+category.getId());
					newsCategories.add(string);
				} 
			}
			int rest=0;
			StringBuilder torequest= new StringBuilder();
			for (int i = 0; i < newsCategories.size(); i++) {
				if ((i%1000==0)&&(i>=1000)) {
					rest+=1000;
					try {
						historyPath = this.postRequest(torequest.toString(),"1",true,1000);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mediawiki = historyXMLToDB(xStream, historyPath);
					torequest= new StringBuilder();
				}
				String name = newsCategories.get(i-rest);
				if (torequest.length()>0) {
					torequest.append("\n");
				}
				torequest.append(name);
			}
			if (torequest.length()>0) {
				try {
					System.out.println(torequest.toString());
					historyPath = this.postRequest(torequest.toString(),"1",true,1000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mediawiki = historyXMLToDB(xStream, historyPath);
			}

		};
	}
	/**
	 * @param pagename
	 * @param xStream
	 * @param historyPath
	 * @return
	 */
	private InputStream downloadMainPage(String pagename, XStream xStream, InputStream historyPath) {
		String timestamp= "1";
		String oldTimestamp = "";
		while(!timestamp.equalsIgnoreCase(oldTimestamp))
		{
			try {
				historyPath = this.postRequest(pagename,timestamp,false,1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediawiki = historyXMLToDB(xStream, historyPath);
			Page page = pageService.getPage(pagename);
			if (page == null) {
				String title = pagename.replace('_', ' ');
//				System.out.println(pagename);
//				System.out.println(title);
				page = pageService.getPage(title);
				if (page == null) {
					title = this.removeAccents(title);
					page = pageService.getPage(title);
				}
			}
			System.out.println(pagename);
			page = pageService.mergePage(page);
			System.out.println("old = "+oldTimestamp+" actual = "+timestamp);
			oldTimestamp=timestamp;
			timestamp=page.getRevisions().get(page.getRevisions().size()-1).getStringTimestamp();
			
			System.out.println("new = "+timestamp);
		}
		return historyPath;
	}
	public InputStream postRequest(String page,String offset,Boolean curonly,Integer limit) throws ClientProtocolException, IOException {
		String url = "https://en.wikipedia.org/w/index.php?title=Special:Export";
		String USER_AGENT = "Mozilla/5.0";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		// add header
		post.setHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		urlParameters.add(new BasicNameValuePair("pages", "Pope"));
//		urlParameters.add(new BasicNameValuePair("offset", "2001-12-05T12:04:10Z"));
		urlParameters.add(new BasicNameValuePair("pages", page));
		if (!curonly) {
			urlParameters.add(new BasicNameValuePair("offset", offset));
			urlParameters.add(new BasicNameValuePair("limit", limit.toString()));
		} else {
			urlParameters.add(new BasicNameValuePair("curonly", "curonly"));
		}
		//urlParameters.add(new BasicNameValuePair("wpDownload", "wpDownload"));
		urlParameters.add(new BasicNameValuePair("action", "submit"));

		post.setEntity(new UrlEncodedFormEntity(urlParameters,"UTF-8"));

		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " );
		post.getEntity().writeTo(System.out);
		System.out.println("Response Code : " +
                                    response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

		StringBuilder result = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line+"\n");
		}

		//System.out.println(result.toString());
		InputStream stream = new ByteArrayInputStream(result.toString().getBytes(StandardCharsets.UTF_8));

		return stream;
	}
	private void asignacionCategorias() {
		// TODO Auto-generated method stub
		List<Page> pages = pageService.getAllPages();
		List<Page> pagesCategorised = inCategoryService.getAllCategorysedPages();
		for (Page page : pagesCategorised) {
			pages.remove(page);
		}
		Integer index = 0;
		for (Page page : pages) {
			page = pageService.mergePage(page);// para que levante revisiones
			List<Revision> revisions = page.getRevisions();
			List<Category> oldCategories = new LinkedList<Category>();
			int rev=0;
			for (Revision revision : revisions) {
				List<Category> newCategories = categoriesFromText(revision.getText());
				// System.out.println("news "+newCategories.size());
				Map<Category, Boolean> cambiosCategories = diffCategories(oldCategories, newCategories);
				// System.out.println("changes "+cambiosCategories.size());
				if (true) {
					rev++;
					System.out.println(rev+"-Revision: " + revision.getId());
					for (Category category : cambiosCategories.keySet()) {
						System.out.println(category.getTitle() + " se " + cambiosCategories.get(category));
					}
				}
				for (Category category : cambiosCategories.keySet()) {
					categoryService.getCategory(category.getId());
					if (cambiosCategories.get(category)) { // si se agrego
						InCategory inCategory = new InCategory();
						inCategory.setCategory(category);
						inCategory.setPage(page);
						inCategory.setRevisionStart(revision);
						inCategory = inCategoryService.mergeInCategory(inCategory); 
						if (page.isCategory()) {// si es categoria
							//System.out.println("agrege page category");
							category.getChildrens().add(inCategory);
							((Category) page).getParents().add(inCategory);
						} else {// si es page comun
							//System.out.println("agrege page page");
							category.getPages().add(inCategory);
						}
						inCategory = inCategoryService.mergeInCategory(inCategory); 
					} else {// si se elimino
						InCategory inCategory = new InCategory();
						if (page.isCategory()) {// si es categoria
							inCategory = category.getActiveChildren(page);
							while (inCategory != null) {
								//System.out.println("removi page category");
								inCategory.setRevisionEnd(revision);
								inCategory = category.getActiveChildren(page);
							}			
						} else {// si es page comun
							inCategory = category.getActivePage(page);
							while (inCategory != null) {
								//System.out.println("removi page page");
								inCategory.setRevisionEnd(revision);
								inCategory = category.getActivePage(page);
							}
						}
					}
					// System.out.println("children
					// "+category.getChildrens().size());
					// System.out.println("pages "+category.getPages().size());
					// System.out.println("parents
					// "+category.getParents().size());
					categoryService.mergeCategory(category);
					if (page.getNs().compareTo(14) == 0) {
						categoryService.mergeCategory((Category) page);
					}
					
					oldCategories = newCategories;
				}
			}			
			index++;
			System.out.println(index + " Page: " + page.getId() + " Is Category: " + page.isCategory());
		}
		List<Category> categories = categoryService.getAllCategorys();
		for (Category category : categories) {
			System.out.println("Categoria: "+category.getId()+" "+category.getTitle());
			System.out.println("Cantidad de padres: "+category.getParents().size());
			System.out.println("Cantidad de hijos: "+category.getChildrens().size());
			System.out.println("Cantidad de paginas: "+category.getPages().size());
		}
		
		// para cada pagina
		// Set de categorias viejas vacio
		// por cada revision
		// si texto no es nulo
		// Parseo Texto-obtengo categorias nuevas
		// Diff categorias viejas vs nuevas map<Categoria,agregada o quitada>
		// por cada uno del map
		// busco categoria
		// si es nula continue
		// si es agregada
		// creo el incategory
		// Asigno la categoria en el incategory
		// Asigno in category en la lista segun si es pagina o categoria(si es
		// categoria tambien en la de padres)
		// Si es Eliminada
		// Busco el incateogory en la categoria en la lista correspondiente
		// seteo la revision final
		// Categorias viejas = nuevas
		// for (Page page : pages) {
		// index++;
		// System.out.println(index+" Page: "+page.getId()+" Is Category:
		// "+page.isCategory());
		// }
	}

	private Map<Category, Boolean> diffCategories(List<Category> oldCategories, List<Category> newCategories) {
		// TODO Auto-generated method stub
		Set<Category> oldC = new HashSet<Category>(oldCategories);
		Set<Category> newC = new HashSet<Category>(newCategories);
		Set<Category> allC = new HashSet<Category>(newCategories);
		allC.addAll(oldC);
		
		Map<Category, Boolean> map = new HashMap<Category, Boolean>();
		for (Category category : allC) {
			if (oldC.contains(category) && newC.contains(category)) {// si esta en los dos
				continue;
			}
			if (!oldC.contains(category) && newC.contains(category)) {// si es nuevo
				map.put(category, true);
			}
			if (oldC.contains(category) && !newC.contains(category)) {// si se elimino
				map.put(category, false);
			}
		}
		return map;
	}
	public List<String> categoriesNamesFromText(String text) {
		List<String> categories = new LinkedList<String>();
		Locale locale = new Locale("EN");
		if (mediawiki!=null) {
			locale = new Locale(mediawiki.getLang());
		}
		String categoryWord = messageSource.getMessage("categoriesFromText.categoryWord", null, locale);
		//Pattern pattern = Pattern.compile(Pattern.quote("[[Catego") + "(.*?)" + Pattern.quote("]]"));
		Pattern pattern = Pattern.compile(Pattern.quote("[["+categoryWord+":") + "(.*?)" + Pattern.quote("]]"));
		 //Pattern pattern = Pattern.compile(Pattern.quote("[[Categoria\\:")+"(.*?)"+Pattern.quote("]]"));
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String str = matcher.group(1);
			//String str = matcher.group();
			//System.out.println(str);
			//System.out.println(str.substring(4, str.length()));

//			Category category = categoryService.getCategory("Categoria:" + str.substring(4, str.length()));
			while((str.length()-1 >= 0)&&(!Character.isLetterOrDigit((str.charAt(str.length()-1))))){
				str = str.substring(0, str.length()-1);
			//	System.out.println(str);
			}
			if (str.length()>0) {
				//System.out.println(categoryWord+":" + str);
				categories.add(categoryWord+":" + str);
			}
			
		}

		// categories.addAll(randomSample4(categoryService.getAllCategorys(),
		// 5));

		return categories;
	}
	
	private List<Category> categoriesFromText(String text) {
		List<Category> categories = new LinkedList<Category>();
		List<String> categoriesNames = this.categoriesNamesFromText(text);
		for (String string : categoriesNames) {
			Category category = categoryService.getCategory(string);
			if (category != null) {
				// System.out.println("categoria recuperada id:
				// "+category.getId());
				categories.add(category);
			} else {
				// System.out.println("null");
			}
		}
		return categories;
	}

	public static <T> Set<T> randomSample4(List<T> items, int m) {
		HashSet<T> res = new HashSet<T>(m);
		int n = items.size();
		Random rnd = new Random();
		for (int i = n - m; i < n; i++) {
			int pos = rnd.nextInt(i + 1);
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
			} else {
				System.out.println(page.getRevisions().size());
			}
		}
	}

	private void dropDB() {
		System.out.println("Dropeando Mediawiki");
//		for (Mediawiki mediawiki : mediawikiService.getAllMediawikis()) {
//			System.out.println(mediawiki.getSiteinfo().getSitename());
//			mediawikiService.deleteMediawiki(mediawiki.getId());
//		}
		mediawikiService.truncateAll();
	}

	private Mediawiki historyXMLToDB(XStream xStream, InputStream historyPath) {
		Mediawiki mediawiki = null;
		
		mediawiki = (Mediawiki) xStream.fromXML(historyPath);
		mediawikiService.mergeMediawiki(mediawiki);
		return mediawiki;
	}

	private XStream configXStream(Boolean saver,Integer namespace) {
		// configuro xstream
		XStream xStream = new XStream(new StaxDriver());
		xStream.alias("revision", Revision.class);
		xStream.alias("page", Page.class);
		xStream.addImplicitCollection(Page.class, "revisions");
		xStream.alias("siteinfo", Siteinfo.class);
		xStream.aliasField("case", Siteinfo.class, "casee");
		xStream.alias("namespace", Namespace.class);
		xStream.alias("mediawiki", Mediawiki.class);
		xStream.addImplicitCollection(Mediawiki.class, "pages");

		// converters
//		xStream.registerConverter(new MediaWikiConverter(cargaDumpService));
//		xStream.registerConverter(new PageConverter(cargaDumpService));
		if (saver) {
			MediaWikiConverter mediaWikiConverter=null;
			if (namespace!=null) {
				mediaWikiConverter = new MediaWikiConverter( mediawikiService,pageService,namespace);
				xStream.registerConverter(new PageConverter(pageService,revisionService,namespace));
			} else {
				mediaWikiConverter = new MediaWikiConverter( mediawikiService,pageService);
				xStream.registerConverter(new PageConverter(pageService,revisionService));
			}
			xStream.registerConverter(mediaWikiConverter);
			xStream.registerConverter(new NamespaceConverter());
			xStream.registerConverter(new RevisionConverter(this));
			xStream.registerConverter(new UserContributorConverter(userContributorService,mediaWikiConverter,messageSource));
//			xStream.registerConverter(new UserContributorConverter(cargaDumpService));
		}

		return xStream;
	}
	public String removeAccents(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

}
