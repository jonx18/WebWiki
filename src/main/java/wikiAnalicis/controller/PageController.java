package wikiAnalicis.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import wikiAnalicis.entity.Page;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;

@Controller
public class PageController {
	@Autowired
	private PageService pageService;
	@Autowired
	private RevisionService revisionService; 
	
	public PageController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "statisticsPageOf")
	public ModelAndView statisticsPageOf(Long parentId) {
		ModelAndView model = new ModelAndView("statisticsPageOf");
		Page page = pageService.getPage(parentId);
		model.addObject("page", page);
		//---------------------------------------------------------------------------------------
		Long totalRevisiones = revisionService.count(page);
		model.addObject("totalRevisiones", totalRevisiones);
		// ---------------------------------------------------------------------------------------
		Map<String, Long> distribucionDeAporte = pageService.countColaboratorRevisionsInPage(page);
		LinkedList<Object[]> toJS = new LinkedList<Object[]>();
		for (String key : distribucionDeAporte.keySet()) {
			toJS.add(new Object[] { key, distribucionDeAporte.get(key) });
		}
		Gson gson = new Gson();
		model.addObject("distribucionDeAporte", gson.toJson(toJS));
		//---------------------------------------------------------------------------------------
		Map<Date,Long> 	revisionesDia= pageService.revisionInDaysOf(page);
		toJS= new LinkedList<Object[]>();
//		toJS.add(new Object[]{"Tiempo","Nº de Revisiones"});
		for (Date key : revisionesDia.keySet()) {
			toJS.add(new Object[]{key,revisionesDia.get(key)});
		}	
		gson = new Gson();
		model.addObject("revisionesDia", gson.toJson(toJS));
		//---------------------------------------------------------------------------------------
		Map<Date,Long> 	contenidoDia= pageService.contentInDaysOf(page);
		toJS= new LinkedList<Object[]>();
//		toJS.add(new Object[]{"Tiempo","Nº de Revisiones"});
		for (Date key : contenidoDia.keySet()) {
			toJS.add(new Object[]{key,contenidoDia.get(key)});
		}	
		gson = new Gson();
		model.addObject("contenidoDia", gson.toJson(toJS));
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
		// ---------------------------------------------------------------------------------------
		Long totalPaginas = pageService.count();
		model.addObject("totalPaginas", totalPaginas);
		// ---------------------------------------------------------------------------------------
		Map<String, Long> paginasEnNamespace = pageService.countPagesInNamespace();
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
		Map<String, TreeMap<Date, Long>> nuevasPaginasPorNamespaceDia = pageService.newPagesForNamespacesInDays();
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

}
