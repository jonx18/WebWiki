package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public class ParagraphRevisionContainer {

	private LinkedList<Delimiter> delimiters = new LinkedList<Delimiter>();
	private LinkedList<NodeContainer> containers = new LinkedList<NodeContainer>();
	public ParagraphRevisionContainer(String text, List<Delimiter> delimiters) {
		this.delimiters.addAll(delimiters);
		this.textToComponents(text);
	}
	private void textToComponents(String text) {
		int[] indexValues = new int[text.length()];
	}
}
