package wikiAnalicis.util.diffAndStyles;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DiffContainer {
	LinkedList<ParagraphContainer> paragraphs = new LinkedList<ParagraphContainer>();
	LinkedList<ParagraphDiff> diffs; 
	public DiffContainer(List<ParagraphDiff> paragraphDiffs, List<Delimiter> delimiters) {
		for (ParagraphDiff paragraphDiff : paragraphDiffs) {
			this.paragraphs.add(new ParagraphContainer(paragraphDiff, delimiters));
		}
	}
	public void calculateDifferenes() {
		for (int i = 0; i < paragraphs.size(); i++) {
			ParagraphContainer container = paragraphs.get(i);
			container.calculateDifferenes();
		}
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
