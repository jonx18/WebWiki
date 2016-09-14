package wikiAnalicis.entity.diffAndStyles;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wikiAnalicis.entity.Revision;
import wikiAnalicis.util.diffAndStyles.ParagraphDiffer;

public class DiffContainer {
	Revision oldRevision;
	Revision newRevision;
	LinkedList<ParagraphContainer> paragraphs = new LinkedList<ParagraphContainer>();
	EnumMap<Delimiter, Integer[]> map = null;
	public DiffContainer(Revision oldRevision,Revision newRevision, List<Delimiter> delimiters) {
		this.oldRevision = oldRevision;
		this.newRevision = newRevision;
		ParagraphDiffer differ = new ParagraphDiffer();
		List<ParagraphDiff> paragraphDiffs=null;
		if (newRevision==null) {
			paragraphDiffs = differ.paragraphComparetor(oldRevision.getText(), "");
		}else {
			paragraphDiffs = differ.paragraphComparetor(oldRevision.getText(), newRevision.getText());
		}

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
	public List<ParagraphDiff> getParagraphDiffs() {
		List<ParagraphDiff> paragraphDiffs = new LinkedList<ParagraphDiff>();
		for (ParagraphContainer paragraphContainer : paragraphs) {
			paragraphDiffs.add(paragraphContainer.getParagraphDiff());
		}
		return paragraphDiffs;

	}
	/**
	 * Calcula los estilos dentro de dos reviciones.
	 * Retorna un mapa por delimitador que contiene un array de enteros con [valor antiguo,valor nuevo,Diff Absoluta]
	 * @see Delimiter
	 * @return Map<Delimiter, Integer[]>
	 */
	public Map<Delimiter, Integer[]> getStyleChanges() {
		if (this.map == null) {
			this.map = new EnumMap<Delimiter, Integer[]>(Delimiter.class); 
			for (int i = 0; i < paragraphs.size(); i++) {
				ParagraphContainer container = paragraphs.get(i);
				Map<Delimiter, List<NodeContainer>> oldStyles = container.countOldStyles();
				Map<Delimiter, List<NodeContainer>> newStyles = container.countNewStyles();
				//System.out.println("Diferencias entre:");
				//System.out.println(container.getParagraphDiff().getOldParagraph());
				//System.out.println(container.getParagraphDiff().getNewParagraph());
				LinkedList<NodeContainer> oldC = new LinkedList<NodeContainer>();
				LinkedList<NodeContainer> newC = new LinkedList<NodeContainer>();
				for (Delimiter delimiter : oldStyles.keySet()) {

					//if ((newStyles.get(delimiter).size()-oldStyles.get(delimiter).size())!=0) {
					if (newStyles.get(delimiter).size()!=0 || oldStyles.get(delimiter).size()!=0) {
						//System.out.print("Delimiter: "+delimiter.getOpenIndicator()+" Count: ");
						
						//importante
						int oldStylesSize=oldStyles.get(delimiter).size();
						int newStylesSize=newStyles.get(delimiter).size();
						int abs=Math.abs(newStylesSize-oldStylesSize);
						//System.out.println(abs);
						if (this.map.containsKey(delimiter)) {
							Integer[] array = this.map.get(delimiter);
							oldStylesSize+=array[0];
							newStylesSize+=array[1];
							abs=Math.abs(newStylesSize-oldStylesSize);
						}
						this.map.put(delimiter, new Integer []{
								oldStylesSize,
								newStylesSize,
								abs});
					
						
						
						
						if ((newStyles.get(delimiter).size()-oldStyles.get(delimiter).size())>0) {
							//System.out.println("+"+(newStyles.get(delimiter).size()-oldStyles.get(delimiter).size()));
						}else{
							//System.out.println((newStyles.get(delimiter).size()-oldStyles.get(delimiter).size()));
						}
						for (NodeContainer nodeContainer : oldStyles.get(delimiter)) {
							//if (nodeContainer.getHasChanges()) {
								//System.out.println(nodeContainer.getDelimiter().getOpenIndicator()+" change:"+nodeContainer.getHasChanges());
								oldC.add(nodeContainer);
							//}
						}
						for (NodeContainer nodeContainer : newStyles.get(delimiter)) {
							//if (nodeContainer.getHasChanges()) {
								//System.out.println(nodeContainer.getDelimiter().getOpenIndicator()+" change:"+nodeContainer.getHasChanges());
								newC.add(nodeContainer);
							//}
						}
					}
					
				}
			}
		}
		
		return this.map;
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
	public Revision getOldRevision() {
		return oldRevision;
	}
	public void setOldRevision(Revision oldRevision) {
		this.oldRevision = oldRevision;
	}
	public Revision getNewRevision() {
		return newRevision;
	}
	public void setNewRevision(Revision newRevision) {
		this.newRevision = newRevision;
	}
	public EnumMap<Delimiter, Integer[]> getMap() {
		return map;
	}
	public void setMap(EnumMap<Delimiter, Integer[]> map) {
		this.map = map;
	}
	
}
