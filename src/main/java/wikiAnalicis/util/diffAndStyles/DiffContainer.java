package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public class DiffContainer {
	List<ParagraphContainer> paragraphs = new LinkedList<ParagraphContainer>();

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
