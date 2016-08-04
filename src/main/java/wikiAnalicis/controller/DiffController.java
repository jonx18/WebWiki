package wikiAnalicis.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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

import wikiAnalicis.entity.Revision;
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
		cambiosContenido(listListDiff);
		String json = gson.toJson(listListDiff, listListDiff.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json);
		return new ModelAndView("diffList", "listListDiff", listListDiff);

	}

	private void cambiosContenido(LinkedList<ParagraphDiff> listListDiff) {
		// TODO Auto-generated method stub
		List<Delimiter> delimiters = new LinkedList<Delimiter>();
		String[] openIndicator ={"<nowiki>","<big>","<small>","<sup>","<sub>","<s>","<blockquote>","<includeonly>",
				"<ref","==","===","====","=====","''","'''","'''''","#REDIRECCIÓN [[","[http://","[https://","[["};
		String[] closeIndicator ={"</nowiki>","</big>","</small>","</sup>","</sub>","</s>","</blockquote>","</includeonly>",
				"</ref>","==","===","====","=====","''","'''","'''''","]]","]","]","]]"};
		for (int i = 0; i < openIndicator.length; i++) {
			Delimiter d = new Delimiter(openIndicator[i], closeIndicator[i],false);
			delimiters.add(d);
		}
		String[] openIndicatorFull ={"#","*","::",":"};
		for (int i = 0; i < openIndicatorFull.length; i++) {
			Delimiter d = new Delimiter(openIndicatorFull[i],true);
			delimiters.add(d);
		}
		StyleAnalyzer styleAnalyzer = new StyleAnalyzer(delimiters);
		styleAnalyzer.textDescomsition(listListDiff);
		//DiffContainer grafo = styleAnalyzer.getStryleGraph(listListDiff);
		System.out.println("------------------------------------");
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
		
	}
	public void dfsTest(DiffContainer diffContainer) {
		Set<NodeContainer> oldNodeContainers = new HashSet<NodeContainer>();
		Set<NodeContainer> newNodeContainers = new HashSet<NodeContainer>();
		System.out.println("----------OLD-------------------");
		for (ParagraphContainer paragraphContainer : diffContainer.getParagraphs()) {
			for (NodeContainer nodeContainer : paragraphContainer.getOldElements()) {
				dfsElementsTest(nodeContainer, oldNodeContainers);
			}
		}
		System.out.println("----------NEW-------------------");
		for (ParagraphContainer paragraphContainer : diffContainer.getParagraphs()) {
			for (NodeContainer nodeContainer : paragraphContainer.getNewElements()) {
				dfsElementsTest(nodeContainer, newNodeContainers);
			}
		}
	}
	public void dfsElementsTest(NodeContainer nodeContainer,Set<NodeContainer> nodeContainers) {
		if (!nodeContainers.contains(nodeContainer)) {
			nodeContainers.add(nodeContainer);
			System.out.println(nodeContainer.getContent());
			for (NodeContainer nodeContainer2 : nodeContainer.getChildrens()) {
				dfsElementsTest(nodeContainer2, nodeContainers);
			}
		}
	}
}
