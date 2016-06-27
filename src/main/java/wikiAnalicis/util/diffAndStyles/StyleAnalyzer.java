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

	
	public DiffContainer getStryleGraph(LinkedList<ParagraphDiff> paragraphDiffs) {
		DiffContainer diffContainer = new DiffContainer();
		LinkedList<NodeContainer> oldQueue = new LinkedList<StyleAnalyzer.NodeContainer>();
		LinkedList<NodeContainer> newQueue = new LinkedList<StyleAnalyzer.NodeContainer>();
		for (ParagraphDiff paragraphDiff : paragraphDiffs) {
			ParagraphContainer paragraphContainer = new ParagraphContainer(paragraphDiff);
			diffContainer.addParagraphs(paragraphContainer);
			if (oldQueue.isEmpty()) {
				if (true) {//hay mas delimitadores
					//busco patron
					NodeContainer nodeContainer = new NodeContainer("patron", 0);
					paragraphContainer.addOldElement(nodeContainer);
					oldQueue.push(nodeContainer);
				}
			} else {
				NodeContainer nodeContainer =oldQueue.pop();
				paragraphContainer.addOldElement(nodeContainer);
				nodeContainer.addParagraph(paragraphContainer);
				oldQueue.push(nodeContainer);
			}
			if (newQueue.isEmpty()) {
				if (true) {//hay mas delimitadores
					//busco patron
					NodeContainer nodeContainer = new NodeContainer("patron", 1);
					paragraphContainer.addNewElement(nodeContainer);
					newQueue.push(nodeContainer);
				}
			} else {
				NodeContainer nodeContainer =newQueue.pop();
				paragraphContainer.addNewElement(nodeContainer);
				nodeContainer.addParagraph(paragraphContainer);
				newQueue.push(nodeContainer);
			}
			Boolean hay = false;
			while(hay){//hay mas delimitadores en 0
				//busco patron
				NodeContainer nodeContainer =oldQueue.pop();
				if(true){//es el cierre de nodeContainer.openIndicator()
					nodeContainer.setCloseIndicator("patronCierre");//el que sea
					if (!oldQueue.isEmpty()) {
						NodeContainer parent =oldQueue.pop();
						parent.addChil(nodeContainer);
						oldQueue.add(parent);
					}
				}
				else{
					NodeContainer child = new NodeContainer("patron", 0);
					paragraphContainer.addOldElement(child);
					oldQueue.push(child);
				}
			}
			while(hay){//hay mas delimitadores en 1
				//busco patron
				NodeContainer nodeContainer =newQueue.pop();
				if(true){//es el cierre de nodeContainer.openIndicator()
					nodeContainer.setCloseIndicator("patronCierre");//el que sea
					if (!newQueue.isEmpty()) {
						NodeContainer parent =newQueue.pop();
						parent.addChil(nodeContainer);
						oldQueue.add(parent);
					}
				}
				else{
					NodeContainer child = new NodeContainer("patron", 0);
					paragraphContainer.addOldElement(child);
					newQueue.push(child);
				}
			}
		}
		return diffContainer;
	}
	public static Map<String,List<String>> elementsInParagraph(ParagraphDiff paragraphDiff) {
		String tags="&lt;"+"&gt;";
		//System.out.println("Parrafo viejo");
		detectTags(paragraphDiff.getOldParagraph());
		//System.out.println("Parrafo nuevo");
		detectTags(paragraphDiff.getNewParagraph());
		return null;
	}
	
	private static List<String> detectTags(String paragraph) {
		System.out.println("-----------"+paragraph+"-------------");
		Pattern pattern = Pattern.compile(Pattern.quote("<") + "(.*?)" + Pattern.quote(">"));
		// Pattern pattern = Pattern.compile("[[Categoría\\:(.*?)]]");
		Matcher matcher = pattern.matcher(paragraph);
		while (matcher.find()) {
			String str = matcher.group(1);
			System.out.println(str);
			// System.out.println(str.substring(4, str.length()));
			

		}

		
		return null;
	}
	public class TextDelimiterIterator {
		StringBuilder text=new StringBuilder();
		List<Delimiter> delimiters;
		Map<Integer,String> map = new TreeMap<Integer, String>();
		private Iterator<Integer> iterator;

		
		

		public TextDelimiterIterator(List<Delimiter> delimiters) {
			super();
			this.delimiters = delimiters;
		}
		public void setText(String text) {
			this.text = new StringBuilder(text);
			mapInitializer();
		}
		private void mapInitializer() {
			this.map = new TreeMap<Integer,String>();
			for (Delimiter delimiter : delimiters) {
				int o = StringUtils.countMatches(this.text, delimiter.openIndicator);
				int c = StringUtils.countMatches(this.text, delimiter.closeIndicator);
				int index=0;//puede nofuncionar probar
				for (int i = 0; i < o; i++) {
					index = StringUtils.indexOf(this.text, delimiter.openIndicator,index);
					map.put(index, delimiter.openIndicator);
					index+=delimiter.openIndicator.length();
				}
				index=0;//puede nofuncionar probar
				for (int i = 0; i < c; i++) {
					index = StringUtils.indexOf(this.text, delimiter.closeIndicator,index);
					map.put(index, delimiter.closeIndicator);
					index+=delimiter.closeIndicator.length();
				}
			}
			iterator = map.keySet().iterator();
		}

		public boolean hasNext() {			
			return iterator.hasNext();
		}


		public String next() {
			return map.get(iterator.next());
		}
		
	}
	public class Delimiter{
		String openIndicator;
		String closeIndicator;
		public Delimiter(String openIndicator, String closeIndicator) {
			super();
			this.openIndicator = openIndicator;
			this.closeIndicator = closeIndicator;
		}
		public String getOpenIndicator() {
			return openIndicator;
		}
		public void setOpenIndicator(String openIndicator) {
			this.openIndicator = openIndicator;
		}
		public String getCloseIndicator() {
			return closeIndicator;
		}
		public void setCloseIndicator(String closeIndicator) {
			this.closeIndicator = closeIndicator;
		}
		
		
	}
	public class DiffContainer{
		List<ParagraphContainer> paragraphs = new LinkedList<StyleAnalyzer.ParagraphContainer>();

		public List<ParagraphContainer> getParagraphs() {
			return paragraphs;
		}

		public void setParagraphs(List<ParagraphContainer> paragraphs) {
			this.paragraphs = paragraphs;
		}
		public void addParagraphs(ParagraphContainer paragraph) {
			this.paragraphs.add(paragraph);
		}
	}
	public class ParagraphContainer{
		ParagraphDiff paragraphDiff;
		List<NodeContainer> oldElements = new LinkedList<StyleAnalyzer.NodeContainer>();
		List<NodeContainer> newElements = new LinkedList<StyleAnalyzer.NodeContainer>();
		public ParagraphContainer(ParagraphDiff paragraphDiff) {
			super();
			this.paragraphDiff = paragraphDiff;
		}
		public ParagraphDiff getParagraphDiff() {
			return paragraphDiff;
		}
		public void setParagraphDiff(ParagraphDiff paragraphDiff) {
			this.paragraphDiff = paragraphDiff;
		}
		public List<NodeContainer> getOldElements() {
			return oldElements;
		}
		public void setOldElements(List<NodeContainer> oldElements) {
			this.oldElements = oldElements;
		}
		public List<NodeContainer> getNewElements() {
			return newElements;
		}
		public void setNewElements(List<NodeContainer> newElements) {
			this.newElements = newElements;
		}
		public void addNewElement(NodeContainer newElement) {
			this.newElements.add(newElement);
		}
		public void addOldElement(NodeContainer oldElement) {
			this.oldElements.add(oldElement);
		}
	}
	public class NodeContainer{
		String openIndicator;
		String closeIndicator;
		String content;
		int diff;
		List<ParagraphContainer> paragraphs = new LinkedList<StyleAnalyzer.ParagraphContainer>();
		List<NodeContainer> childrens = new LinkedList<StyleAnalyzer.NodeContainer>();

		public NodeContainer(String openIndicator,int diff) {
			super();
			this.openIndicator = openIndicator;
			this.diff=diff;
		}
		public String getOpenIndicator() {
			return openIndicator;
		}
		public void setOpenIndicator(String openIndicator) {
			this.openIndicator = openIndicator;
		}
		public String getCloseIndicator() {
			return closeIndicator;
		}
		public void setCloseIndicator(String closeIndicator) {
			this.closeIndicator = closeIndicator;
			
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public List<ParagraphContainer> getParagraphs() {
			return paragraphs;
		}
		public void addParagraph(ParagraphContainer paragraphContainer) {
			paragraphs.add(paragraphContainer);
		}
		public void setParagraphs(List<ParagraphContainer> paragraphs) {
			this.paragraphs = paragraphs;
		}
		public List<NodeContainer> getChildrens() {
			return childrens;
		}
		public void addChil(NodeContainer child) {
			this.childrens.add(child);
		}
		public void setChildrens(List<NodeContainer> childrens) {
			this.childrens = childrens;
		}
		
		
	}
	
}
