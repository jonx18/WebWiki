package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public class NodeContainer {
	String openIndicator;
	String closeIndicator;
	String content=null;
	int diff;
	NodeContainer parent=null;
	List<ParagraphContainer> paragraphs = new LinkedList<ParagraphContainer>();
	List<NodeContainer> childrens = new LinkedList<NodeContainer>();

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
		if (parent==null) {
			generateContent();
			for (NodeContainer nodeContainer : childrens) {
				nodeContainer.generateContent();
			}
		}

	}
	public void generateContent() {
		StringBuilder builder = new StringBuilder();
		if (parent==null) {
			System.out.println("sin Padre");
			for (ParagraphContainer paragraphContainer : paragraphs) {
				if (diff==0) {
					builder.append(paragraphContainer.paragraphDiff.getOldParagraph());
				} else {
					builder.append(paragraphContainer.paragraphDiff.getNewParagraph());
				}			
			}
		} else {
			System.out.println("con Padre");
			builder.append(parent.getContent());
		}

		int inicio = builder.indexOf(openIndicator);
		int fin = builder.lastIndexOf(closeIndicator);
		//System.out.println(openIndicator+","+closeIndicator);
		//System.out.println("inicio: "+inicio+" fin: "+fin+" largo: "+builder.length());
		this.content = builder.substring(inicio+openIndicator.length(), fin);
		System.out.println(this.content);
		
	}
	public String getContent() {
		if (content==null) {
			generateContent();
		}
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<ParagraphContainer> getParagraphs() {
		return paragraphs;
	}
	public void addParagraph(ParagraphContainer paragraphContainer) {
		//System.out.println("a: "+openIndicator);
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
	public NodeContainer getParent() {
		return parent;
	}
	public void setParent(NodeContainer parent) {
		this.parent = parent;
	}
}
