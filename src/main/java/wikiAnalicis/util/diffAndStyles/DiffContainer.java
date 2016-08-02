package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public class DiffContainer {
	LinkedList<ParagraphContainer> paragraphs = new LinkedList<ParagraphContainer>();

	public DiffContainer(List<ParagraphDiff> paragraphDiffs, List<Delimiter> delimiters) {
		for (ParagraphDiff paragraphDiff : paragraphDiffs) {
			this.paragraphs.add(new ParagraphContainer(paragraphDiff, delimiters));
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
