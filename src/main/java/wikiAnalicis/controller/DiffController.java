package wikiAnalicis.controller;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diffAndStyles.Delimiter;
import wikiAnalicis.entity.diffAndStyles.Diff;
import wikiAnalicis.entity.diffAndStyles.DiffContainer;
import wikiAnalicis.entity.diffAndStyles.ParagraphDiff;
import wikiAnalicis.entity.statics.PageStatistics;
import wikiAnalicis.service.DiffContainerService;
import wikiAnalicis.service.EmailService;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;
import wikiAnalicis.service.StatisticsService;
import wikiAnalicis.util.diffAndStyles.ParagraphDiffer;
import wikiAnalicis.util.diffAndStyles.StyleAnalyzer;

@Controller
public class DiffController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private RevisionService revisionService;
	@Autowired
	private PageService pageService;
	@Autowired
	private DiffContainerService diffContainerService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private EmailService emailService;
	private Locale langSeted=null;
	public DiffController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping("diffList")
	public ModelAndView showDiff(Long id, Long parentId,HttpServletRequest request) {
		Locale locale = localeResolver.resolveLocale(request);
		Revision revText1 = revisionService.getRevision(parentId);
		Revision revText2 = revisionService.getRevision(id);

		Gson gson = new Gson();

		//String json = gson.toJson(result, result.getClass());
		DiffContainer diffContainer = diffContainerService.getDiffContainer(revText1);
		List<ParagraphDiff> listListDiff;
		Map<Delimiter, Integer[]> mapStyleChanges;
		if (diffContainer==null) {
			diffContainer = cambiosContenido(revText1,revText2,locale);
			listListDiff = diffContainer.getParagraphDiffs();
			mapStyleChanges = diffContainer.getStyleChanges();
			diffContainerService.createDiffContainer(diffContainer);
		}
		else{
			diffContainer = diffContainerService.loadParagraphOfDiffContainer(diffContainer);
		}
		listListDiff = diffContainer.getParagraphDiffs();
		mapStyleChanges = diffContainer.getStyleChanges();
//		String json = gson.toJson(listListDiff, listListDiff.getClass());
//		LOGGER.info("Mostrando Diff. Data : " + json);
//		json = gson.toJson(mapStyleChanges, mapStyleChanges.getClass());
//		LOGGER.info("Mostrando mapStyleChanges. Data : " + json);
		ModelAndView model = new ModelAndView("diffList");
		model.addObject("listListDiff", listListDiff);
		model.addObject("mapStyleChanges", mapStyleChanges);
		for (Delimiter delimiter : mapStyleChanges.keySet()) {
			//System.out.println(delimiter.getName()+" "+mapStyleChanges.get(delimiter)[0]+" "+mapStyleChanges.get(delimiter)[1]+" "+mapStyleChanges.get(delimiter)[2]);
		}
		return model;

	}
	@RequestMapping("diffStatisticsOfPage")
	public ModelAndView diffStatisticsOfPage(Long id,HttpServletRequest request) {
		Locale locale = localeResolver.resolveLocale(request);
		Page page = pageService.getPage(id);
		page = pageService.mergePage(page);
		PageStatistics pageStatistics = statisticsService.getPageStatistics(page);
		Map<Delimiter, Integer[]> mapStyleChanges=null;
		Date[] dates=null;
		if (pageStatistics==null||pageStatistics.getDates().isEmpty()||true) {
			List<Revision> revisions = page.getRevisions();
			int size = revisions.size();
			List<Delimiter> delimiters = this.getDelimiters(locale);
			mapStyleChanges = new EnumMap<Delimiter, Integer[]>(Delimiter.class);
			dates= new Date[size];
			for (Delimiter delimiter : delimiters) {
				Integer[] array= new Integer[size];
				mapStyleChanges.put(delimiter, array);
			}
			Revision oldRevision = revisions.get(0);
			//Map<Delimiter, Integer> oldMap= oldRevision.textToComponents(delimiters);
			Map<Delimiter, Integer> oldMap= oldRevision.enumerateComponents();
			//System.out.println("enumerado: "+oldMap.get(Delimiter.NUMBEREDELEMENT)+"listado: "+oldMap.get(Delimiter.BULLETEDELEMENT));
			dates[0]=oldRevision.getTimestamp();
			if (size>1) {
				for (int i = 1; i < size; i++) {
					if(i%100==0){
					System.out.println(i+"- Revision: "+oldRevision.getId());
					}
					Revision newRevision = revisions.get(i);		
					dates[i]=newRevision.getTimestamp();
					//Map<Delimiter, Integer> newMap= newRevision.textToComponents(delimiters);
					Map<Delimiter, Integer> newMap= newRevision.enumerateComponents();
					//System.out.println("enumerado: "+newMap.get(Delimiter.NUMBEREDELEMENT)+"listado: "+newMap.get(Delimiter.BULLETEDELEMENT));
					DiffContainer diffContainer = diffContainerService.getDiffContainer(oldRevision);
					Map<Delimiter, Integer[]> mapRevChanges=null;
					if (diffContainer==null) {
						//diffContainer = cambiosContenido(oldRevision,newRevision,locale);
						//mapRevChanges = diffContainer.getStyleChanges();
						diffContainer = new DiffContainer(oldRevision, newRevision);
						mapRevChanges = diffContainer.lazyGetStyleChanges(oldMap, newMap);
						diffContainerService.createDiffContainer(diffContainer);
					}
					if (mapRevChanges==null) {
						mapRevChanges = diffContainer.lazyGetStyleChanges(oldMap, newMap);
					}
					for (Delimiter delimiter : mapRevChanges.keySet()) {
						Integer[] array = mapStyleChanges.get(delimiter);
						//if (i==1) {
							array[i-1]=mapRevChanges.get(delimiter)[0];
						//}
						array[i]=mapRevChanges.get(delimiter)[1];
						mapStyleChanges.put(delimiter, array);
					}
					
					
					
					oldRevision = newRevision;
					oldMap=newMap;
				}
			}else{
				DiffContainer diffContainer = diffContainerService.getDiffContainer(oldRevision);
				Map<Delimiter, Integer[]> mapRevChanges=null;
				if (diffContainer==null) {
					//diffContainer = cambiosContenido(oldRevision,null,locale);
					//mapRevChanges = diffContainer.getStyleChanges();
					diffContainer = new DiffContainer(oldRevision, null);
					Map<Delimiter, Integer> newMap= new HashMap<Delimiter, Integer>();
					for (Delimiter delimiter : delimiters) {
						newMap.put(delimiter, 0);
					}
					mapRevChanges = diffContainer.lazyGetStyleChanges(oldMap, newMap);
					diffContainerService.createDiffContainer(diffContainer);
				}
				if (mapRevChanges==null) {
					mapRevChanges = diffContainer.getStyleChanges();
				}
				for (Delimiter delimiter : mapRevChanges.keySet()) {
					mapStyleChanges.get(delimiter)[0]=mapRevChanges.get(delimiter)[0];
				}
			}
			//vaciar los que sumen 0
			LinkedList<Delimiter> remove = new LinkedList<Delimiter>();
			for (Delimiter delimiter : mapStyleChanges.keySet()) {
				Integer[] values = mapStyleChanges.get(delimiter);
				int sum=0;
				for (int i = 0; i < values.length; i++) {
					int integer;
					if (values[i]==null) {
						integer = 0;
						values[i]=0;
					}else{
						integer = values[i];
					}
					
					sum+=integer;
//					if (integer>0) {
//						break;
//					}
				}
				if(sum==0){
					remove.add(delimiter);
				}
			}
//			for (Delimiter delimiter : remove) {
//				mapStyleChanges.remove(delimiter);
//			}
			if (pageStatistics==null){
				pageStatistics= new PageStatistics();
				pageStatistics.setPage(page);
				statisticsService.createPageStatistics(pageStatistics);
			} 
			pageStatistics.setDates(Arrays.asList(dates));
			pageStatistics.setMapStyleChanges(mapStyleChanges);
			//pageStatistics.setTotalRevisiones(new Long(size));
			statisticsService.mergePageStatistics(pageStatistics);
		} else {
			System.out.println("cargado de base");
			dates = pageStatistics.getDatesArray();
			mapStyleChanges = pageStatistics.getMapStyleChanges();
		}
		
		

		Gson gson = new Gson();
		String json = gson.toJson(mapStyleChanges, mapStyleChanges.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json);
		json = gson.toJson(dates, dates.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json);

		ModelAndView model = new ModelAndView("diffStatisticsOfPage");
		model.addObject("mapStyleChanges", mapStyleChanges);
		model.addObject("dates", dates);
//		for (Delimiter delimiter : mapStyleChanges.keySet()) {
//			System.out.println(delimiter.getName()+" "+mapStyleChanges.get(delimiter)[0]+" "+mapStyleChanges.get(delimiter)[1]+" "+mapStyleChanges.get(delimiter)[2]);
//		}
		return model;

	}
	@RequestMapping("diffStatisticsOfPageWithRedirection")
	public String diffStatisticsOfPageWithRedirection(Long id,HttpServletRequest request) {
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
		String asunto = "Comienzo de proceso diffStatisticsOfPageWithRedirection en "+hostname+":"+userpc;
		String mensaje = "El proceso comenzo a las: "+calendar.getTime() ;
		emailService.enviar(fuente, destino, asunto, mensaje);
		//-------------------------------------------------------------------------
		Scanner scanner = new Scanner(request.getAttribute("id").toString());
		while (scanner.hasNextLine()) {
			String num= scanner.nextLine().replace("\n", "").replace("\r", "");
			if (num.trim().isEmpty()) {
				System.out.println("empty");
				continue;
			}
			try {
				System.out.println("diffStatisticsOfPageWithRedirection");
				id=new Long(num);
				this.diffStatisticsOfPage(id, request);
				//------------------------------------------------------------------------------
						long stopTimeFull = System.currentTimeMillis();
						long elapsedTimeFull = stopTimeFull - startTimeFull;
						calendar = Calendar.getInstance();
						calendar.setTimeInMillis(stopTimeFull);
						fuente = "wikianalisis@gmail.com";
						destino = "jonamar10@hotmail.com";
						asunto = "Finalizacion de proceso diffStatisticsOfPageWithRedirection "+num +" en "+hostname+":"+userpc;
						mensaje = "El proceso "+num+" termino a las: "+calendar.getTime()+"\n "
								+ "Tardo:"+ elapsedTimeFull+" milisegundos" ;
						emailService.enviar(fuente, destino, asunto, mensaje);
			} catch (Exception e) {
				long stopTimeFull = System.currentTimeMillis();
				long elapsedTimeFull = stopTimeFull - startTimeFull;
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(stopTimeFull);
				fuente = "wikianalisis@gmail.com";
				destino = "jonamar10@hotmail.com";
				asunto = "ERROR de proceso diffStatisticsOfPageWithRedirection "+num+" en "+hostname+":"+userpc;
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				 
				mensaje = "El proceso Fallo a las: "+calendar.getTime()+"\n "
						+ "Tardo:"+ elapsedTimeFull+" milisegundos\n "+"stack:\n "+sw.toString();
				emailService.enviar(fuente, destino, asunto, mensaje);
			}
		}
		

		
		return"forward:/index";
	}

	private DiffContainer cambiosContenido(Revision oldRevision,Revision newRevision,Locale locale) {
		List<Delimiter> delimiters = getDelimiters(locale);
		StyleAnalyzer styleAnalyzer = new StyleAnalyzer(delimiters);
		DiffContainer diffContainer = styleAnalyzer.textDescomsition(oldRevision,newRevision);
		
		return diffContainer;
	}

	private List<Delimiter> getDelimiters(Locale locale) {
		if (langSeted==null || langSeted != locale) {
			langSeted=locale;
			System.out.println("entro y el lenguaje es: "+langSeted);
			for (Delimiter delimiter : Delimiter.values()) {
				if (delimiter==Delimiter.NONE) {
					continue;
				}
				if(delimiter==Delimiter.REDIRECTION){
					delimiter.setOpenIndicator(messageSource.getMessage("delimiter.redirection.delimiter", null, locale));
				}
				delimiter.setName(messageSource.getMessage("delimiter."+delimiter.toString()+".name", null, locale));
			}
		}
		List<Delimiter> delimiters = Arrays.asList(Delimiter.values());
////		String[] openIndicator ={"<nowiki>","<big>","<small>","<sup>","<sub>","<s>","<blockquote>","<includeonly>",
////				"<ref","=====","====","===","==","'''''","'''","''","#REDIRECCI�N [[","[http://","[https://","[["};
////		String[] closeIndicator ={"</nowiki>","</big>","</small>","</sup>","</sub>","</s>","</blockquote>","</includeonly>",
////				"</ref>","=====","====","===","==","'''''","'''","''","]]","]","]","]]"};
//		String[] openIndicator ={"","<nowiki>","<big>","<small>","<sup>","<sub>","<s>","<blockquote>","<includeonly>",
//				"<ref","==","===","====","=====","''","'''","'''''","[","[["};
//		String[] closeIndicator ={"","</nowiki>","</big>","</small>","</sup>","</sub>","</s>","</blockquote>","</includeonly>",
//				"</ref>","==","===","====","=====","''","'''","'''''","]","]]"};
//		
//		String[] name ={"",
//				messageSource.getMessage("delimiter.nowiki.name", null, locale),
//				messageSource.getMessage("delimiter.big.name", null, locale),
//				messageSource.getMessage("delimiter.small.name", null, locale),
//				messageSource.getMessage("delimiter.sup.name", null, locale),
//				messageSource.getMessage("delimiter.sub.name", null, locale),
//				messageSource.getMessage("delimiter.s.name", null, locale),
//				messageSource.getMessage("delimiter.blockquote.name", null, locale),
//				messageSource.getMessage("delimiter.includeonly.name", null, locale),
//				messageSource.getMessage("delimiter.reference.name", null, locale),
//				messageSource.getMessage("delimiter.heading2.name", null, locale),
//				messageSource.getMessage("delimiter.heading3.name", null, locale),
//				messageSource.getMessage("delimiter.heading4.name", null, locale),
//				messageSource.getMessage("delimiter.heading5.name", null, locale),
//				messageSource.getMessage("delimiter.italic.name", null, locale),
//				messageSource.getMessage("delimiter.blod.name", null, locale),
//				messageSource.getMessage("delimiter.italicblod.name", null, locale),
//				messageSource.getMessage("delimiter.link.external.name", null, locale),
//				messageSource.getMessage("delimiter.link.internal.name", null, locale)
//				};
//		for (int i = 0; i < openIndicator.length; i++) {
//			Delimiter d = new Delimiter(openIndicator[i], closeIndicator[i],name[i],false);
//			delimiters.add(d);
//		}
//		String[] openIndicatorFull ={"#",
//				messageSource.getMessage("delimiter.redirection.delimiter", null, locale),
//				"*","::",":"};
//		String[] nameFull ={
//				messageSource.getMessage("delimiter.numberedelement.name", null, locale),
//				messageSource.getMessage("delimiter.redirection.name", null, locale),
//				messageSource.getMessage("delimiter.bulletedelement.name", null, locale),
//				messageSource.getMessage("delimiter.indent2.name", null, locale),
//				messageSource.getMessage("delimiter.indent1.name", null, locale)
//				};
//		for (int i = 0; i < openIndicatorFull.length; i++) {
//			Delimiter d = new Delimiter(openIndicatorFull[i],nameFull[i],true);
//			delimiters.add(d);
//		}
		return delimiters;
	}

}
