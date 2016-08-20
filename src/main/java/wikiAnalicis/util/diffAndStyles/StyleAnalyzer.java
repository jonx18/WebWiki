package wikiAnalicis.util.diffAndStyles;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import wikiAnalicis.entity.Category;

public class StyleAnalyzer {
	List<Delimiter> delimiters;

	public StyleAnalyzer(List<Delimiter> delimiters) {
		super();
		this.delimiters = delimiters;
	}
	public DiffContainer textDescomsition(LinkedList<ParagraphDiff> paragraphDiffs) {
		DiffContainer diffContainer = new DiffContainer(paragraphDiffs, delimiters);
		
		
		
		return diffContainer;
		
	}
	

	public static Map<String,List<String>> elementsInParagraph(ParagraphDiff paragraphDiff) {
		String tags="&lt;"+"&gt;";
		////System.out.println("Parrafo viejo");
		detectTags(paragraphDiff.getOldParagraph());
		////System.out.println("Parrafo nuevo");
		detectTags(paragraphDiff.getNewParagraph());
		return null;
	}
	
	private static List<String> detectTags(String paragraph) {
		//System.out.println("-----------"+paragraph+"-------------");
		Pattern pattern = Pattern.compile(Pattern.quote("<") + "(.*?)" + Pattern.quote(">"));
		// Pattern pattern = Pattern.compile("[[Categoría\\:(.*?)]]");
		Matcher matcher = pattern.matcher(paragraph);
		while (matcher.find()) {
			String str = matcher.group(1);
			//System.out.println(str);
			// //System.out.println(str.substring(4, str.length()));
			

		}

		
		return null;
	}

	
}
