package wikiAnalicis.util.diffAndStyles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wikiAnalicis.entity.InCategory;

public class DiffContainer {
	LinkedList<ParagraphContainer> paragraphs = new LinkedList<ParagraphContainer>();
	LinkedList<ParagraphDiff> diffs; 
	public DiffContainer(List<ParagraphDiff> paragraphDiffs, List<Delimiter> delimiters) {
		for (ParagraphDiff paragraphDiff : paragraphDiffs) {
			this.paragraphs.add(new ParagraphContainer(paragraphDiff, delimiters));
		}
		this.calculateDifferenes();
		
	}
	public void calculateDifferenes() {
		for (int i = 0; i < paragraphs.size(); i++) {
			ParagraphContainer container = paragraphs.get(i);
			container.calculateDifferenes();
		}
	}
	/**
	 * Calcula los estilos dentro de dos reviciones.
	 * Retorna un mapa por delimitador que contiene un array de enteros con [valor antiguo,valor nuevo,Diff Absoluta]
	 * @see Delimiter
	 * @return Map<Delimiter, Integer[]>
	 */
	public Map<Delimiter, Integer[]> getStyleChanges() {
		Map<Delimiter, Integer[]> map = new HashMap<Delimiter, Integer[]>();
		for (int i = 0; i < paragraphs.size(); i++) {
			ParagraphContainer container = paragraphs.get(i);
			Map<Delimiter, List<NodeContainer>> oldStyles = container.countOldStyles();
			Map<Delimiter, List<NodeContainer>> newStyles = container.countNewStyles();
			System.out.println("Diferencias entre:");
			System.out.println(container.getParagraphDiff().getOldParagraph());
			System.out.println(container.getParagraphDiff().getNewParagraph());
			LinkedList<NodeContainer> oldC = new LinkedList<NodeContainer>();
			LinkedList<NodeContainer> newC = new LinkedList<NodeContainer>();
			for (Delimiter delimiter : oldStyles.keySet()) {

				//if ((newStyles.get(delimiter).size()-oldStyles.get(delimiter).size())!=0) {
				if (newStyles.get(delimiter).size()!=0 || oldStyles.get(delimiter).size()!=0) {
					System.out.print("Delimiter: "+delimiter.getOpenIndicator()+" Count: ");
					
					//importante
					int oldStylesSize=oldStyles.get(delimiter).size();
					int newStylesSize=newStyles.get(delimiter).size();
					int abs=Math.abs(newStyles.get(delimiter).size()-oldStyles.get(delimiter).size());
					if (map.containsKey(delimiter)) {
						Integer[] array = map.get(delimiter);
						oldStylesSize+=array[0];
						newStylesSize+=array[1];
						abs+=array[2];
					}
					map.put(delimiter, new Integer []{
							oldStylesSize,
							newStylesSize,
							abs});
				
					
					
					
					if ((newStyles.get(delimiter).size()-oldStyles.get(delimiter).size())>0) {
						System.out.println("+"+(newStyles.get(delimiter).size()-oldStyles.get(delimiter).size()));
					}else{
						System.out.println((newStyles.get(delimiter).size()-oldStyles.get(delimiter).size()));
					}
					for (NodeContainer nodeContainer : oldStyles.get(delimiter)) {
						//if (nodeContainer.getHasChanges()) {
							System.out.println(nodeContainer.getDelimiter().getOpenIndicator()+" change:"+nodeContainer.getHasChanges());
							oldC.add(nodeContainer);
						//}
					}
					for (NodeContainer nodeContainer : newStyles.get(delimiter)) {
						//if (nodeContainer.getHasChanges()) {
							System.out.println(nodeContainer.getDelimiter().getOpenIndicator()+" change:"+nodeContainer.getHasChanges());
							newC.add(nodeContainer);
						//}
					}
				}
				
			}
		}
		return map;
	}
	public LinkedList<ParagraphContainer> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(LinkedList<ParagraphContainer> paragraphs) {
		this.paragraphs = paragraphs;
	}
	public void addParagraphs(ParagraphContainer paragraph) {
		this.paragraphs.add(paragraph);
	}
}
