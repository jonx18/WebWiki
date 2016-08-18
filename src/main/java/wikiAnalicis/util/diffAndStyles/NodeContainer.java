package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public abstract class NodeContainer {
	Delimiter delimiter;
	NodeContainer parent=null;
	List<NodeContainer> childrens = new LinkedList<NodeContainer>();
	private Boolean hasChanges=false;



	public void generateContent() {
//		StringBuilder builder = new StringBuilder();
//		if (parent==null) {
//			System.out.println("sin Padre");
//			for (ParagraphContainer paragraphContainer : paragraphs) {
//				if (diff==0) {
//					builder.append(paragraphContainer.paragraphDiff.getOldParagraph());
//				} else {
//					builder.append(paragraphContainer.paragraphDiff.getNewParagraph());
//				}			
//			}
//		} else {
//			System.out.println("con Padre");
//			builder.append(parent.getContent());
//		}
//
//		int inicio = builder.indexOf(openIndicator);
//		int fin = builder.lastIndexOf(closeIndicator);
//		//System.out.println(openIndicator+","+closeIndicator);
//		//System.out.println("inicio: "+inicio+" fin: "+fin+" largo: "+builder.length());
//		this.content = builder.substring(inicio+openIndicator.length(), fin);
//		System.out.println(this.content);
		
	}
	public String componentsToString() {
//		if (content==null) {
//			generateContent();
//		}
		return "";
	}
	
	public abstract int setIntoArray(int index, NodeContainer[] arrayOfDiff);
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
	public Boolean getHasChanges() {
		return hasChanges;
	}
	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	}
	
}
