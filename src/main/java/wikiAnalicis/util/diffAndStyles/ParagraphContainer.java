package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public class ParagraphContainer {
	private ParagraphDiff paragraphDiff;
	private ParagraphRevisionContainer oldParagraph;
	private ParagraphRevisionContainer newParagraph;
	List<NodeContainer> oldElements = new LinkedList<NodeContainer>();
	List<NodeContainer> newElements = new LinkedList<NodeContainer>();
	public ParagraphContainer(ParagraphDiff paragraphDiff) {
		super();
		this.paragraphDiff = paragraphDiff;
	}
	public ParagraphContainer(ParagraphDiff paragraphDiff,List<Delimiter> delimiters) {
		super();
		this.oldParagraph = new ParagraphRevisionContainer(paragraphDiff.getOldParagraph(),delimiters);
		this.newParagraph = new ParagraphRevisionContainer(paragraphDiff.getNewParagraph(),delimiters);
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
