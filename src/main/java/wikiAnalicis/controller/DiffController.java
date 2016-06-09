package wikiAnalicis.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		differ.Diff_Timeout=0;
//		differ.Diff_EditCost=5;
//		differ.diff_cleanupEfficiency(diffs);
		differ.diff_cleanupSemantic(diffs);
		LinkedList<Diff[]> result = estructureComparision(diffs);	
		// differ.diff_cleanupEfficiency(diffs);
		// differ.diff_levenshtein(diffs);
		// differ.diff_cleanupMerge(diffs);
		Gson gson = new Gson();
		String json = gson.toJson(result, result.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json);
		return new ModelAndView("diffList", "diffResult", result);

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

}
