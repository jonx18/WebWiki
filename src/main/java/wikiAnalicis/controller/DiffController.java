package wikiAnalicis.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
import wikiAnalicis.util.diff_match_patch;
import wikiAnalicis.util.diff_match_patch.Diff;
import wikiAnalicis.util.diff_match_patch.Operation;

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

		diff_match_patch differ = new diff_match_patch();
		LinkedList<Diff> diffs = differ.diff_main(revText1, revText2);
		differ.Diff_Timeout = 0;
		// differ.Diff_EditCost=5;
		// differ.diff_cleanupEfficiency(diffs);
		differ.diff_cleanupSemantic(diffs);

		LinkedList<Diff[]> result = estructureComparision(diffs);
		// differ.diff_cleanupEfficiency(diffs);
		// differ.diff_levenshtein(diffs);
		// differ.diff_cleanupMerge(diffs);
		Gson gson = new Gson();
		LinkedList<ParagraphDiff> listListDiff = paragraphComparetor(revText1, revText2);
		//String json = gson.toJson(result, result.getClass());
		String json = gson.toJson(listListDiff, listListDiff.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json);
		return new ModelAndView("diffList", "listListDiff", listListDiff);

	}

	private LinkedList<ParagraphDiff> paragraphComparetor(String revText1, String revText2) {
		List<String> listParagraph1 = new LinkedList<String>();
		List<String> listParagraph2 = new LinkedList<String>();
		Scanner scanner = new Scanner(revText1);
		while (scanner.hasNextLine()) {
			String paragraph = scanner.nextLine();
			listParagraph1.add(paragraph);
		}
		scanner = new Scanner(revText2);
		while (scanner.hasNextLine()) {
			String paragraph = scanner.nextLine();
			listParagraph2.add(paragraph);
		}
		String[] aux = {};
		int[][] matrix = computeLevenshteinDistance(listParagraph1.toArray(aux), listParagraph2.toArray(aux));
		LinkedList<String[]> comparables = reverseLevenshteindDistance(listParagraph1.toArray(aux), listParagraph2.toArray(aux), matrix);
		LinkedList<ParagraphDiff> listListDiff= new LinkedList<ParagraphDiff>();
		diff_match_patch differ = new diff_match_patch();	
		for (String[] strings : comparables) {
			LinkedList<Diff> diffs = differ.diff_main(strings[0], strings[1]);
			differ.diff_cleanupSemantic(diffs);
			LinkedList<Diff[]> resultParagraph = estructureComparision(diffs);
			listListDiff.add(new ParagraphDiff(resultParagraph));
		}
		return listListDiff;

	}
	private LinkedList<String[]> reverseLevenshteindDistance(String[] listParagraph1, String[] listParagraph2,int[][] matrix){
		int i =listParagraph1.length;
		int j =listParagraph2.length;
		LinkedList<String[]> list = new LinkedList<String[]>();
		while(i!=0 || j!=0){
			if (i==0) {
				String[] par={"",listParagraph2[j-1]};
				list.addFirst(par);
				j--;
				continue;
			}
			if (j==0) {
				String[] par={listParagraph1[i-1],""};
				list.addFirst(par);
				i--;
				continue;
			}
			String[] par={listParagraph1[i-1],listParagraph2[j-1]};
			list.addFirst(par);
			int min = minimum(matrix[i-1][j-1], matrix[i-1][j], matrix[i][j-1]);
			if (matrix[i-1][j-1]==min) {
				i--;
				j--;
			} else {
				if (matrix[i-1][j]==min) {
					i--;
				} else {
					j--;
				}
			}
			
		}
		return list;
	}
	
	private int minimum(int a, int b, int c) {
		if (a <= b && a <= c) {
			return a;
		}
		if (b <= a && b <= c) {
			return b;
		}
		return c;
	}

	private int[][] computeLevenshteinDistance(String[] listParagraph1, String[] listParagraph2) {
		int[][] distance = new int[listParagraph1.length + 1][listParagraph2.length + 1];
		diff_match_patch differ = new diff_match_patch();
		for (int i = 0; i <= listParagraph1.length; i++) {
			distance[i][0] = i;
		}
		for (int j = 0; j <= listParagraph2.length; j++) {
			distance[0][j] = j;
		}
		for (int i = 1; i <= listParagraph1.length; i++) {
			for (int j = 1; j <= listParagraph2.length; j++) {
				LinkedList<Diff> diffs = differ.diff_main(listParagraph1[i - 1], listParagraph2[j - 1]);
				int leven = differ.diff_levenshtein(diffs);
				distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1,
								distance[i - 1][j - 1] + ((listParagraph1[i - 1] == listParagraph2[j - 1]) ? 0 : leven));
			}
		}
//		for (int i = 0; i <= listParagraph1.length; i++) {
//			StringBuilder s = new StringBuilder();
//			s.append("[ ");
//			for (int j = 0; j <= listParagraph2.length; j++) {
//				s.append(distance[i][j]+", ");
//			}
//			s.append("]");
//			System.out.println(s.toString());
//		}
		
//		return distance[listParagraph1.length][listParagraph2.length];
		return distance;
	}

	private LinkedList<Diff[]> estructureComparision(LinkedList<Diff> diffs) {
		LinkedList<Diff> deletionsEqualities = new LinkedList<diff_match_patch.Diff>();
		LinkedList<Diff> insertionsEqualities = new LinkedList<diff_match_patch.Diff>();
		for (Diff diff : diffs) {
			if (diff.operation == Operation.EQUAL) {
				deletionsEqualities.addLast(diff);
				insertionsEqualities.addLast(diff);
			} else {
				if (diff.operation == Operation.DELETE) {
					deletionsEqualities.addLast(diff);
				} else {
					insertionsEqualities.addLast(diff);
				}
			}
		}
		Iterator<Diff> deletionIterator = deletionsEqualities.iterator();
		Iterator<Diff> insertionIterator = insertionsEqualities.iterator();
		LinkedList<Diff[]> result = new LinkedList<diff_match_patch.Diff[]>();
		if (deletionsEqualities.isEmpty() || insertionsEqualities.isEmpty()) {
			if (deletionsEqualities.isEmpty()) {
				for (Diff diff : insertionsEqualities) {
					Diff[] par = { null, diff };
					result.add(par);
				}
			} else {
				for (Diff diff : deletionsEqualities) {
					Diff[] par = { diff, null };
					result.add(par);
				}
			}
		} else {
			Boolean fin = false;
			Diff d = deletionIterator.next();
			Diff i = insertionIterator.next();
			while (!fin) {
				if (d.operation == i.operation) {
					Diff[] par = { d, i };
					result.add(par);
					if (deletionIterator.hasNext()) {
						d = deletionIterator.next();
					}
					if (insertionIterator.hasNext()) {
						i = insertionIterator.next();
					}
				} else {
					if ((d.operation == Operation.DELETE) && (i.operation == Operation.INSERT)) {
						Diff[] par = { d, i };
						result.add(par);
						if (deletionIterator.hasNext()) {
							d = deletionIterator.next();
						}
						if (insertionIterator.hasNext()) {
							i = insertionIterator.next();
						}
					} else {
						if (d.operation == Operation.EQUAL) {
							Diff[] par = { null, i };
							result.add(par);
							if (insertionIterator.hasNext()) {
								i = insertionIterator.next();
							}
						} else {
							Diff[] par = { d, null };
							result.add(par);
							if (deletionIterator.hasNext()) {
								d = deletionIterator.next();
							}
						}
					}
				}
				if (!deletionIterator.hasNext() && !insertionIterator.hasNext()) {
					fin = true;
				}
			}
			while (insertionIterator.hasNext()) {
				diff_match_patch.Diff diff = (diff_match_patch.Diff) insertionIterator.next();
				Diff[] par = { null, diff };
				result.add(par);
			}
			while (deletionIterator.hasNext()) {
				diff_match_patch.Diff diff = (diff_match_patch.Diff) deletionIterator.next();
				Diff[] par = { diff, null };
				result.add(par);
			}
		}
		return result;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	static String readFileLines(String path, Charset encoding) throws IOException {
		List<String> encoded = Files.readAllLines(Paths.get(path), encoding);
		StringBuilder result = new StringBuilder();
		for (String string : encoded) {
			result.append(string + "\n");
		}
		return result.toString();
	}
	public static class ParagraphDiff {
		Boolean change=false;
		LinkedList<Diff[]> diffs;
		public ParagraphDiff(LinkedList<Diff[]> diffs) {
			this.diffs=diffs;
			for (Diff[] diffs2 : diffs) {
				for (Diff diff : diffs2) {
					if (diff!=null && diff.operation!=Operation.EQUAL) {
						change=true;
					}
				}
			}
		}

		public Boolean getChange() {
			return change;
		}

		public void setChange(Boolean change) {
			this.change = change;
		}

		public LinkedList<Diff[]> getDiffs() {
			return diffs;
		}
		public void setDiffs(LinkedList<Diff[]> diffs) {
			this.diffs = diffs;
		}
		
		
		
	}
}
