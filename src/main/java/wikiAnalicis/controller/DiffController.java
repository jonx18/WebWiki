package wikiAnalicis.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.diff_match_patch;
import wikiAnalicis.entity.diff_match_patch.Diff;
import wikiAnalicis.service.RevisionService;

@Controller
public class DiffController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private RevisionService revisionService;

	public DiffController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping("diffList")
	public ModelAndView showDiff() {
		String revText1 = null;
		String revText2 = null ;
		try {
			revText1 = readFileLines(
					"C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\pagesv2\\revisiones7777\\revision2text1text.txt",
					StandardCharsets.UTF_8);
			revText2 = readFileLines(
					"C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\pagesv2\\revisiones7777\\revision3text1text.txt",
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		diff_match_patch differ = new diff_match_patch();
		LinkedList<Diff> diffs = differ.diff_main(revText1, revText2);
		differ.diff_cleanupSemantic(diffs);
		//differ.diff_cleanupEfficiency(diffs);
		//differ.diff_levenshtein(diffs);
		//differ.diff_cleanupMerge(diffs);
		Gson gson= new Gson();
		String json = gson.toJson(diffs, diffs.getClass());
		LOGGER.info("Mostrando Diff. Data : " + json );
		return new ModelAndView("diffList", "diffList", diffs);

	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	static String readFileLines(String path, Charset encoding) 
			  throws IOException 
			{
			  List<String> encoded = Files.readAllLines(Paths.get(path), encoding);
			  StringBuilder result=new StringBuilder();
			  for (String string : encoded) {
				result.append(string+"\n");
			}
			  return result.toString();
			}

}
