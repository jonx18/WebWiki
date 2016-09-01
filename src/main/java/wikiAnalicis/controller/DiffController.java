package wikiAnalicis.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import javax.jws.soap.SOAPBinding.Style;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;
import wikiAnalicis.util.diffAndStyles.Delimiter;
import wikiAnalicis.util.diffAndStyles.DiffContainer;
import wikiAnalicis.util.diffAndStyles.NodeContainer;
import wikiAnalicis.util.diffAndStyles.ParagraphContainer;
import wikiAnalicis.util.diffAndStyles.ParagraphDiff;
import wikiAnalicis.util.diffAndStyles.ParagraphDiffer;
import wikiAnalicis.util.diffAndStyles.StyleAnalyzer;
import wikiAnalicis.util.diffAndStyles.diff_match_patch;
import wikiAnalicis.util.diffAndStyles.diff_match_patch.Diff;
import wikiAnalicis.util.diffAndStyles.diff_match_patch.Operation;

@Controller
public class DiffController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private RevisionService revisionService;
	@Autowired
	private PageService pageService;
	public DiffController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping("diffList")
	public ModelAndView showDiff(Long id, Long parentId) {
		String revText1 = revisionService.getRevision(parentId).getText();
		String revText2 = revisionService.getRevision(id).getText();
		// try {
		// revText1 = readFileLines(
		// "C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\pagesv2\\revisiones7777\\revision2text1text.txt",
		// StandardCharsets.UTF_8);
		// revText2 = readFileLines(
		// "C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\pagesv2\\revisiones7777\\revision3text1text.txt",
		// StandardCharsets.UTF_8);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

//		diff_match_patch differ = new diff_match_patch();
//		LinkedList<Diff> diffs = differ.diff_main(revText1, revText2);
//		differ.Diff_Timeout = 0;
//		// differ.Diff_EditCost=5;
//		// differ.diff_cleanupEfficiency(diffs);
//		differ.diff_cleanupSemantic(diffs);

		//LinkedList<Diff[]> result = estructureComparision(diffs);
		// differ.diff_cleanupEfficiency(diffs);
		// differ.diff_levenshtein(diffs);
		// differ.diff_cleanupMerge(diffs);
		Gson gson = new Gson();
		ParagraphDiffer differ = new ParagraphDiffer();
		LinkedList<ParagraphDiff> listListDiff = differ.paragraphComparetor(revText1, revText2);
		//String json = gson.toJson(result, result.getClass());
		DiffContainer diffContainer = cambiosContenido(listListDiff);
		Map<Delimiter, Integer[]> mapStyleChanges = diffContainer.getStyleChanges();
		String json = gson.toJson(listListDiff, listListDiff.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json);
		ModelAndView model = new ModelAndView("diffList");
		model.addObject("listListDiff", listListDiff);
		model.addObject("mapStyleChanges", mapStyleChanges);
		for (Delimiter delimiter : mapStyleChanges.keySet()) {
			//System.out.println(delimiter.getName()+" "+mapStyleChanges.get(delimiter)[0]+" "+mapStyleChanges.get(delimiter)[1]+" "+mapStyleChanges.get(delimiter)[2]);
		}
		return model;

	}
	@RequestMapping("diffStatisticsOfPage")
	public ModelAndView diffStatisticsOfPage(Long id) {
		Page page = pageService.getPage(id);
		page = pageService.mergePage(page);
		List<Revision> revisions = page.getRevisions();
		int size = revisions.size();
		List<Delimiter> delimiters = this.getDelimiters();
		Map<Delimiter, int[]> mapStyleChanges = new HashMap<Delimiter, int[]>();
		Date[] dates= new Date[size];
		for (Delimiter delimiter : delimiters) {
			int[] array= new int[size];
			mapStyleChanges.put(delimiter, array);
		}
		Revision oldRevision = revisions.get(0);
		dates[0]=oldRevision.getTimestamp();
		if (size>1) {
			for (int i = 1; i < size; i++) {
				Revision newRevision = revisions.get(i);		
				dates[i]=newRevision.getTimestamp();
				String revText1 = oldRevision.getText();
				String revText2 = newRevision.getText();
				ParagraphDiffer differ = new ParagraphDiffer();
				LinkedList<ParagraphDiff> listListDiff = differ.paragraphComparetor(revText1, revText2);
				DiffContainer diffContainer = cambiosContenido(listListDiff);
				Map<Delimiter, Integer[]> mapRevChanges = diffContainer.getStyleChanges();
				for (Delimiter delimiter : mapRevChanges.keySet()) {
					int[] array = mapStyleChanges.get(delimiter);
					array[i-1]=mapRevChanges.get(delimiter)[0];
					array[i]=mapRevChanges.get(delimiter)[1];
					mapStyleChanges.put(delimiter, array);
				}
				
				
				
				oldRevision = newRevision;
			}
		}else{
			String revText1 = oldRevision.getText();
			String revText2 = "";
			ParagraphDiffer differ = new ParagraphDiffer();
			LinkedList<ParagraphDiff> listListDiff = differ.paragraphComparetor(revText1, revText2);
			DiffContainer diffContainer = cambiosContenido(listListDiff);
			Map<Delimiter, Integer[]> mapRevChanges = diffContainer.getStyleChanges();
			for (Delimiter delimiter : mapRevChanges.keySet()) {
				mapStyleChanges.get(delimiter)[0]=mapRevChanges.get(delimiter)[0];
			}
		}
		//vaciar los que sumen 0
		LinkedList<Delimiter> remove = new LinkedList<Delimiter>();
		for (Delimiter delimiter : mapStyleChanges.keySet()) {
			int[] values = mapStyleChanges.get(delimiter);
			int sum=0;
			for (int i = 0; i < values.length; i++) {
				int integer = values[i];
				sum+=integer;
//				if (integer>0) {
//					break;
//				}
			}
			if(sum==0){
				remove.add(delimiter);
			}
		}
		for (Delimiter delimiter : remove) {
			mapStyleChanges.remove(delimiter);
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

	private DiffContainer cambiosContenido(LinkedList<ParagraphDiff> listListDiff) {
		List<Delimiter> delimiters = getDelimiters();
		StyleAnalyzer styleAnalyzer = new StyleAnalyzer(delimiters);
		DiffContainer diffContainer = styleAnalyzer.textDescomsition(listListDiff);
		//diffContainer.calculateDifferenes(); lo hago al crearlo
		//DiffContainer grafo = styleAnalyzer.getStryleGraph(listListDiff);
		//System.out.println("------------------------------------");
		//dfsTest(grafo);
		for (ParagraphDiff paragraphDiff : listListDiff) {
//			String oldParagraph = paragraphDiff.getOldParagraph();
//			String newParagraph = paragraphDiff.getNewParagraph();
//			System.out.println(oldParagraph);
//			System.out.println(newParagraph);
//			Integer countOld = 0;
//			Integer countNew = 0;
//			String[] substringsBetween = StringUtils.substringsBetween(oldParagraph, "==", "==");
//			if (substringsBetween!=null) {
//				for (String string : substringsBetween) {
//					System.out.println(string);
//				}
//				countOld = substringsBetween.length;
//			}
//			substringsBetween = StringUtils.substringsBetween(newParagraph, "==", "==");
//			if (substringsBetween!=null) {
//				for (String string : substringsBetween) {
//					System.out.println(string);
//				}
//				countNew = substringsBetween.length;
//			}
//
//			System.out.println("Cantida de '==' en Old: "+countOld+" en New: "+countNew);
//			StringUtils.substringsBetween(oldParagraph, "==", "==");
		
			//StyleAnalyzer.elementsInParagraph(paragraphDiff);
			for (Diff[] diffs : paragraphDiff.getDiffs()) {
				
				
				
			}
		}
		return diffContainer;
	}

	private List<Delimiter> getDelimiters() {
		List<Delimiter> delimiters = new LinkedList<Delimiter>();
//		String[] openIndicator ={"<nowiki>","<big>","<small>","<sup>","<sub>","<s>","<blockquote>","<includeonly>",
//				"<ref","=====","====","===","==","'''''","'''","''","#REDIRECCIÓN [[","[http://","[https://","[["};
//		String[] closeIndicator ={"</nowiki>","</big>","</small>","</sup>","</sub>","</s>","</blockquote>","</includeonly>",
//				"</ref>","=====","====","===","==","'''''","'''","''","]]","]","]","]]"};
		String[] openIndicator ={"","<nowiki>","<big>","<small>","<sup>","<sub>","<s>","<blockquote>","<includeonly>",
				"<ref","==","===","====","=====","''","'''","'''''","[","[["};
		String[] closeIndicator ={"","</nowiki>","</big>","</small>","</sup>","</sub>","</s>","</blockquote>","</includeonly>",
				"</ref>","==","===","====","=====","''","'''","'''''","]","]]"};
		String[] name ={"","Nowiki","Grande","Pequeño","Super indice","Sub-indice","Tachado","Bloque de Cita","includeonly",
				"referencia","Encabezado de 2.º nivel","Encabezado de 3.º nivel","Encabezado de 4.º nivel","Encabezado de 5.º nivel",
				"Cursiva","Negrita","Negrita & cursiva","Enlace Externo","Enlace Interno"};
		for (int i = 0; i < openIndicator.length; i++) {
			Delimiter d = new Delimiter(openIndicator[i], closeIndicator[i],name[i],false);
			delimiters.add(d);
		}
		String[] openIndicatorFull ={"#","#REDIRECCIÓN","*","::",":"};
		String[] nameFull ={"Elemento Enumerado","REDIRECCIÓN","Elemento Listado","Sangria 2","Sangria 1"};
		for (int i = 0; i < openIndicatorFull.length; i++) {
			Delimiter d = new Delimiter(openIndicatorFull[i],nameFull[i],true);
			delimiters.add(d);
		}
		return delimiters;
	}

}
