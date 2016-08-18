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
	
	/**
	public DiffContainer getStryleGraph(LinkedList<ParagraphDiff> paragraphDiffs) {
		DiffContainer diffContainer = new DiffContainer();
		LinkedList<NodeContainer> oldQueue = new LinkedList<NodeContainer>();
		LinkedList<NodeContainer> newQueue = new LinkedList<NodeContainer>();
		TextDelimiterIterator oldDelimiterIterator = new TextDelimiterIterator(delimiters);
		TextDelimiterIterator newDelimiterIterator = new TextDelimiterIterator(delimiters);
		for (ParagraphDiff paragraphDiff : paragraphDiffs) {
			oldDelimiterIterator.setText(paragraphDiff.getOldParagraph());
			newDelimiterIterator.setText(paragraphDiff.getNewParagraph());
			//System.out.println("parrafo viejo: '"+paragraphDiff.getOldParagraph()+"'");
			//System.out.println("parrafo nuevo: '"+paragraphDiff.getNewParagraph()+"'");
			ParagraphContainer paragraphContainer = new ParagraphContainer(paragraphDiff);
			diffContainer.addParagraphs(paragraphContainer);
			if (oldQueue.isEmpty()) {
				if (oldDelimiterIterator.hasNext()) {//hay mas delimitadores
					String patron = oldDelimiterIterator.next();//busco patron
					NodeContainer nodeContainer = new NodeContainer(patron, 0);
					//System.out.println("1-agrego parrafo");
					paragraphContainer.addOldElement(nodeContainer);
					nodeContainer.addParagraph(paragraphContainer);
					oldQueue.push(nodeContainer);
				}
			} else {
//				NodeContainer nodeContainer =oldQueue.pop();
//				paragraphContainer.addOldElement(nodeContainer);
//				nodeContainer.addParagraph(paragraphContainer);
//				oldQueue.push(nodeContainer);
				for (NodeContainer nodeContainer : oldQueue) {
					paragraphContainer.addOldElement(nodeContainer);
					//System.out.println("2-agrego parrafo");
					nodeContainer.addParagraph(paragraphContainer);
				}
			}
			if (newQueue.isEmpty()) {
				if (newDelimiterIterator.hasNext()) {//hay mas delimitadores
					String patron = newDelimiterIterator.next();//busco patron
					NodeContainer nodeContainer = new NodeContainer(patron, 1);
					//System.out.println("3-agrego parrafo");
					paragraphContainer.addNewElement(nodeContainer);
					nodeContainer.addParagraph(paragraphContainer);
					newQueue.push(nodeContainer);
				}
			} else {
//				NodeContainer nodeContainer =newQueue.pop();
//				paragraphContainer.addNewElement(nodeContainer);
//				nodeContainer.addParagraph(paragraphContainer);
//				newQueue.push(nodeContainer);
				for (NodeContainer nodeContainer : newQueue) {
					paragraphContainer.addNewElement(nodeContainer);
					//System.out.println("4-agrego parrafo");
					nodeContainer.addParagraph(paragraphContainer);
				}
			}
			while(oldDelimiterIterator.hasNext()){//hay mas delimitadores en 0
				String patron = oldDelimiterIterator.next();//busco patron
				NodeContainer nodeContainer =oldQueue.pop();
				if(oldDelimiterIterator.getDelimiterOpenBy(nodeContainer.openIndicator).arePair(nodeContainer.openIndicator, patron)){//es el cierre de nodeContainer.openIndicator()
					//System.out.println("cierra en viejo");

					if (!oldQueue.isEmpty()) {
						NodeContainer parent =oldQueue.pop();
						nodeContainer.setParent(parent);
						parent.addChil(nodeContainer);
						oldQueue.add(parent);
					}
					nodeContainer.setCloseIndicator(patron);//el que sea
				}
				else{
					oldQueue.add(nodeContainer);
					NodeContainer child = new NodeContainer(patron, 0);
					//System.out.println("5-agrego parrafo");
					paragraphContainer.addOldElement(child);
					child.addParagraph(paragraphContainer);
					oldQueue.push(child);
				}
			}
			while(newDelimiterIterator.hasNext()){//hay mas delimitadores en 1
				String patron = newDelimiterIterator.next();//busco patron
				NodeContainer nodeContainer =newQueue.pop();
				if(newDelimiterIterator.getDelimiterOpenBy(nodeContainer.openIndicator).arePair(nodeContainer.openIndicator, patron)){//es el cierre de nodeContainer.openIndicator()
					//System.out.println("cierra en nuevo");					
					if (!newQueue.isEmpty()) {
						NodeContainer parent =newQueue.pop();
						nodeContainer.setParent(parent);
						parent.addChil(nodeContainer);
						newQueue.add(parent);
					}
					nodeContainer.setCloseIndicator(patron);//el que sea
				}
				else{
					newQueue.add(nodeContainer);
					NodeContainer child = new NodeContainer(patron, 1);
					//System.out.println("6-agrego parrafo");
					paragraphContainer.addOldElement(child);
					child.addParagraph(paragraphContainer);
					newQueue.push(child);
				}
			}
		}
		return diffContainer;
	}
**/
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
