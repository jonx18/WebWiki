package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

import wikiAnalicis.util.diffAndStyles.diff_match_patch.Diff;
import wikiAnalicis.util.diffAndStyles.diff_match_patch.Operation;

public class ParagraphContainer {
	private ParagraphDiff paragraphDiff;
	private ParagraphRevisionContainer oldParagraph;
	private ParagraphRevisionContainer newParagraph;
	private Boolean hasChanges=false;
	public ParagraphContainer(ParagraphDiff paragraphDiff) {
		super();
		this.paragraphDiff = paragraphDiff;
	}
	public ParagraphContainer(ParagraphDiff paragraphDiff,List<Delimiter> delimiters) {
		super();
		this.paragraphDiff = paragraphDiff;
		this.oldParagraph = new ParagraphRevisionContainer(paragraphDiff.getOldParagraph(),delimiters);
		this.newParagraph = new ParagraphRevisionContainer(paragraphDiff.getNewParagraph(),delimiters);
	}
	public void calculateDifferenes() {
		System.out.println("parrafo viejo: '"+this.getParagraphDiff().getOldParagraph());
		this.getOldParagraph().toDelimitedText();
		int[] arrayOfDiff = createArrayOfDiff(this.getParagraphDiff().getOldDiffs(), this.getParagraphDiff().getOldParagraph().length());
		NodeContainer[] arrayOfNodes = createArrayOfNodes(this.getOldParagraph(), this.getParagraphDiff().getOldParagraph().length());
		
		System.out.println("arrayOfNodes:[");
		for (int i = 0; i < arrayOfNodes.length; i++) {
			if (arrayOfNodes[i]==null) {
				System.out.print("null, ");
			}else{
			System.out.print("Esta, ");}
		}
		System.out.println("]");
		matchNodesDiff(arrayOfDiff, arrayOfNodes);
		this.getOldParagraph().toDelimitedText();
		System.out.println("parrafo nuevo: '"+this.getParagraphDiff().getNewParagraph());
		this.getNewParagraph().toDelimitedText();
		arrayOfDiff = createArrayOfDiff(this.getParagraphDiff().getNewDiffs(), this.getParagraphDiff().getNewParagraph().length());
		arrayOfNodes = createArrayOfNodes(this.getNewParagraph(), this.getParagraphDiff().getNewParagraph().length());

		System.out.println("arrayOfNodes:[");
		for (int i = 0; i < arrayOfNodes.length; i++) {
			if (arrayOfNodes[i]==null) {
				System.out.print("null, ");
			}else{
			System.out.print("Esta, ");}
		}
		System.out.println("]");
		matchNodesDiff(arrayOfDiff, arrayOfNodes);
		this.getNewParagraph().toDelimitedText();
	}
	private void matchNodesDiff(int[] arrayOfDiff, NodeContainer[] arrayOfNodes) {
		for (int i = 0; i < arrayOfNodes.length; i++) {
			NodeContainer nodeContainer = arrayOfNodes[i];
			if (arrayOfDiff[i]==1) {
				if (!this.getHasChanges()) {
					this.setHasChanges(true);
				}

				nodeContainer.setHasChanges(true);

				
			}
		}
	}
	private int[] createArrayOfDiff(List<Diff> diffs,int length) {
		int[] arrayOfDiff= new int[length];
		int index=0;
		for (Diff diff : diffs) {
			if (diff.operation.compareTo(Operation.EQUAL)!=0) {
				for (int i = index; i < index+diff.getText().length(); i++) {
					arrayOfDiff[i]=1;
				}
			}
			index+=diff.getText().length();
		}
		return arrayOfDiff;
	}
	private NodeContainer[] createArrayOfNodes(ParagraphRevisionContainer revisionContainer,int length) {
		NodeContainer[] arrayOfDiff= new NodeContainer[length];
		revisionContainer.setIntoArray(arrayOfDiff);
		return arrayOfDiff;
	}
	public ParagraphDiff getParagraphDiff() {
		return paragraphDiff;
	}
	public void setParagraphDiff(ParagraphDiff paragraphDiff) {
		this.paragraphDiff = paragraphDiff;
	}
	public Boolean getHasChanges() {
		return hasChanges;
	}
	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	}
	public ParagraphRevisionContainer getOldParagraph() {
		return oldParagraph;
	}
	public void setOldParagraph(ParagraphRevisionContainer oldParagraph) {
		this.oldParagraph = oldParagraph;
	}
	public ParagraphRevisionContainer getNewParagraph() {
		return newParagraph;
	}
	public void setNewParagraph(ParagraphRevisionContainer newParagraph) {
		this.newParagraph = newParagraph;
	} 
	
}
