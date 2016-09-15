package wikiAnalicis.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.Delimiter;
import wikiAnalicis.entity.statics.PageStatistics;
import wikiAnalicis.service.EmailService;
import wikiAnalicis.service.InCategoryService;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;
import wikiAnalicis.service.StatisticsService;

@Controller
public class PageController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private PageService pageService;
	@Autowired
	private RevisionService revisionService; 
	@Autowired
	private InCategoryService inCategoryService;
	@Autowired
	private MediawikiService mediawikiService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private EmailService emailService;
	
	public PageController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "statisticsPageOf")
	public ModelAndView statisticsPageOf(Long parentId) {
		ModelAndView model = new ModelAndView("statisticsPageOf");
		Locale locale = new Locale(mediawikiService.getLang());
		Page page = pageService.getPage(parentId);
		model.addObject("page", page);
		//---------------------------------------------------------------------------------------
		Long totalRevisiones = null;
		Map<String, Long> distribucionDeAporte = null;
		Map<Date,Long> revisionesDia = null;
		Map<Date,Long> contenidoDia = null;
		List<InCategory> categories = null;
		//---------------------------------------------------------------------------------------
		PageStatistics pageStatistics = statisticsService.getPageStatistics(page);
		if (pageStatistics==null||pageStatistics.getTotalRevisiones()==0) {
		//----------------------------------------------------------------------------------------
		totalRevisiones = revisionService.count(page);
		// ---------------------------------------------------------------------------------------
		distribucionDeAporte = pageService.countColaboratorRevisionsInPage(page,locale);
		//---------------------------------------------------------------------------------------
		revisionesDia = pageService.revisionInDaysOf(page);
		//---------------------------------------------------------------------------------------
		contenidoDia= pageService.contentInDaysOf(page);
		//------------------InCategorys-----------------------
		categories = inCategoryService.getAllInCategorysOfPage(page);
		//---------------------------------------------------------------------------------------------
		if (pageStatistics==null){
			pageStatistics= new PageStatistics();
			pageStatistics.setPage(page);
			statisticsService.createPageStatistics(pageStatistics);
		} 
		pageStatistics.setTotalRevisiones(totalRevisiones);
		pageStatistics.setDistribucionDeAporte(distribucionDeAporte);
		pageStatistics.setRevisionesDia(revisionesDia);
		pageStatistics.setContenidoDia(contenidoDia);
		pageStatistics.setCategories(categories);
		statisticsService.mergePageStatistics(pageStatistics);
		}else{
			totalRevisiones = pageStatistics.getTotalRevisiones();
			distribucionDeAporte = pageStatistics.getDistribucionDeAporte();
			revisionesDia = pageStatistics.getRevisionesDia();
			contenidoDia = pageStatistics.getContenidoDia();
			categories = pageStatistics.getCategories();
			System.out.println("cargado de base");
		}
		//----------------------------------------------------------------------------------------------
		LinkedList<Object[]> distribucionDeAportetoJS = new LinkedList<Object[]>();
		for (String key : distribucionDeAporte.keySet()) {
			distribucionDeAportetoJS.add(new Object[] { key, distribucionDeAporte.get(key) });
		}
		LinkedList<Object[]> revisionesDiatoJS= new LinkedList<Object[]>();
		for (Date key : revisionesDia.keySet()) {
			revisionesDiatoJS.add(new Object[]{key,revisionesDia.get(key)});
		}	
		LinkedList<Object[]> contenidoDiatoJS= new LinkedList<Object[]>();
		for (Date key : contenidoDia.keySet()) {
			contenidoDiatoJS.add(new Object[]{key,contenidoDia.get(key)});
		}	
		//----------------------------------------------------------------------------------------
		model.addObject("totalRevisiones", totalRevisiones);
		Gson gson = new Gson();
		model.addObject("distribucionDeAporte", gson.toJson(distribucionDeAportetoJS));
		LOGGER.info("Mostrando Diff. Data : " + gson.toJson(distribucionDeAportetoJS));
		model.addObject("revisionesDia", gson.toJson(revisionesDiatoJS));
		LOGGER.info("Mostrando Diff. Data : " + gson.toJson(revisionesDiatoJS));
		model.addObject("contenidoDia", gson.toJson(contenidoDiatoJS));
		LOGGER.info("Mostrando Diff. Data : " + gson.toJson(contenidoDiatoJS));
		model.addObject("categories", categories);
		return model;
	}

	@RequestMapping(value = "/listPages")
	public ModelAndView list(Integer offset, Integer maxResults) {
		List<Page> pages = pageService.list(offset, maxResults);
		ModelAndView model = new ModelAndView("listPages");
		model.addObject("pages", pages);
		model.addObject("count", pageService.count());
		model.addObject("offset", offset);
		// for (Page page : pages) {
		// System.out.println(page.getId()+" "+page.getTitle());
		// }
		return model;
	}

	@RequestMapping(value = { "statisticsPages" })
	public ModelAndView statisticsReviews() {
		ModelAndView model = new ModelAndView("statisticsPages");
		Locale locale = new Locale(mediawikiService.getLang());
		// ---------------------------------------------------------------------------------------
		Long totalPaginas = pageService.count();
		model.addObject("totalPaginas", totalPaginas);
		// ---------------------------------------------------------------------------------------
		
		Map<String, Long> paginasEnNamespace = pageService.countPagesInNamespace(locale);
		LinkedList<Object[]> toJS = new LinkedList<Object[]>();
		for (String key : paginasEnNamespace.keySet()) {
			toJS.add(new Object[] { key, paginasEnNamespace.get(key) });
		}
		Gson gson = new Gson();
		model.addObject("paginasEnNamespace", gson.toJson(toJS));
		// ---------------------------------------------------------------------------------------
		Map<Date, Long> nuevasPaginasDia = pageService.newPagesInDays();
		toJS = new LinkedList<Object[]>();
		// toJS.add(new Object[]{"Tiempo","Nº de Revisiones"});
		for (Date key : nuevasPaginasDia.keySet()) {
			toJS.add(new Object[] { key, nuevasPaginasDia.get(key) });
		}
		model.addObject("nuevasPaginasDia", gson.toJson(toJS));
		// ---------------------------------------------------------------------------------------
		Map<String, TreeMap<Date, Long>> nuevasPaginasPorNamespaceDia = pageService.newPagesForNamespacesInDays(locale);
		toJS = new LinkedList<Object[]>();
		// toJS.add(new Object[]{"Tiempo","Nº de Revisiones"});
		LinkedList<Object> encabezado = new LinkedList<Object>();
		encabezado.addFirst("Tiempo");
		Set<Date> dates = new TreeSet<Date>();
		for (String key : nuevasPaginasPorNamespaceDia.keySet()) {
			encabezado.addLast(key);
			dates.addAll(nuevasPaginasPorNamespaceDia.get(key).keySet());
		}
		toJS.add(encabezado.toArray());
		for (Date keyDate : dates) {
			LinkedList<Object> list = new LinkedList<Object>();
			list.addFirst(keyDate);
			for (String key : nuevasPaginasPorNamespaceDia.keySet()) {
				if (nuevasPaginasPorNamespaceDia.get(key).containsKey(keyDate)) {
					list.addLast(nuevasPaginasPorNamespaceDia.get(key).get(keyDate));
				} else {
					list.addLast(0);
				}

			}
			toJS.add(list.toArray());
		}
		model.addObject("nuevasPaginasPorNamespaceDia", gson.toJson(toJS));
		// //---------------------------------------------------------------------------------------
		// Map<Long,Long> paginasConXRevisiones=
		// pageService.countPagesForNumberOfRevisions();
		// LinkedList<Object[]> toJS= new LinkedList<Object[]>();
		// toJS.add(new Object[]{"Revisiones","Nº de Paginas"});
		// for (Long key : paginasConXRevisiones.keySet()) {
		// toJS.add(new Object[]{key,paginasConXRevisiones.get(key)});
		// }
		// gson = new Gson();
		// model.addObject("paginasConXRevisiones", gson.toJson(toJS));

		return model;
	}
	@RequestMapping(value = "statisticsPageOfWithRedirection")
	public String statisticsPageOfWithRedirection(Long id,HttpServletRequest request) {
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
		String asunto = "Comienzo de proceso statisticsPageOfWithRedirection en "+hostname+":"+userpc;
		String mensaje = "El proceso comenzo a las: "+calendar.getTime() ;
		emailService.enviar(fuente, destino, asunto, mensaje);
		//-------------------------------------------------------------------------
		System.out.println("statisticsPageOfWithRedirection");
		System.out.println(id);
		id=new Long(request.getAttribute("id").toString());
		System.out.println(id);
		statisticsPageOf(id);
		//------------------------------------------------------------------------------
		long stopTimeFull = System.currentTimeMillis();
		long elapsedTimeFull = stopTimeFull - startTimeFull;
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stopTimeFull);
		fuente = "wikianalisis@gmail.com";
		destino = "jonamar10@hotmail.com";
		asunto = "Finalizacion de proceso statisticsPageOfWithRedirection en "+hostname+":"+userpc;
		mensaje = "El proceso termino a las: "+calendar.getTime()+"\n "
				+ "Tardo:"+ elapsedTimeFull+" milisegundos" ;
		emailService.enviar(fuente, destino, asunto, mensaje);
		
		
		return"forward:/diffStatisticsOfPageWithRedirection";
	}
}
