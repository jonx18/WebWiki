package wikiAnalicis.entity.diffAndStyles;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "paragraphcontainer")
public class ParagraphContainer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "paragraphdiff_id", unique = true)
	@Fetch(FetchMode.SELECT)
	@Cascade(CascadeType.ALL)
	@BatchSize(size = 5)
	private ParagraphDiff paragraphDiff;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "oldparagraph_id")
	@Fetch(FetchMode.SELECT)
	@Cascade(CascadeType.ALL)
	@BatchSize(size = 5)
	private ParagraphRevisionContainer oldParagraph;
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "newparagraph_id")
	@Fetch(FetchMode.SELECT)
	@Cascade(CascadeType.ALL)
	@BatchSize(size = 5)
	private ParagraphRevisionContainer newParagraph;
	@Column(name = "haschanges")
	private Boolean hasChanges = false;

	public ParagraphContainer() {
		// TODO Auto-generated constructor stub
	}

	public ParagraphContainer(ParagraphDiff paragraphDiff) {
		super();
		this.paragraphDiff = paragraphDiff;
	}

	public ParagraphContainer(ParagraphDiff paragraphDiff, List<Delimiter> delimiters) {
		super();
		this.paragraphDiff = paragraphDiff;
		this.oldParagraph = new ParagraphRevisionContainer(paragraphDiff.getOldParagraph(), delimiters);
		this.newParagraph = new ParagraphRevisionContainer(paragraphDiff.getNewParagraph(), delimiters);
	}

	public void calculateDifferenes() {
		int[] arrayOfDiff = createArrayOfDiff(this.getParagraphDiff().getOldDiffs(),
				this.getParagraphDiff().getOldParagraph().length());
		NodeContainer[] arrayOfNodes = createArrayOfNodes(this.getOldParagraph(),
				this.getParagraphDiff().getOldParagraph().length());
		matchNodesDiff(arrayOfDiff, arrayOfNodes);
		arrayOfDiff = createArrayOfDiff(this.getParagraphDiff().getNewDiffs(),
				this.getParagraphDiff().getNewParagraph().length());
		arrayOfNodes = createArrayOfNodes(this.getNewParagraph(), this.getParagraphDiff().getNewParagraph().length());
		matchNodesDiff(arrayOfDiff, arrayOfNodes);
	}

	private void matchNodesDiff(int[] arrayOfDiff, NodeContainer[] arrayOfNodes) {
		for (int i = 0; i < arrayOfNodes.length; i++) {
			NodeContainer nodeContainer = arrayOfNodes[i];
			if (arrayOfDiff[i] == 1) {
				if (!this.getHasChanges()) {
					this.setHasChanges(true);
				}

				nodeContainer.setHasChanges(true);

			}
		}
	}

	private int[] createArrayOfDiff(List<Diff> diffs, int length) {
		int[] arrayOfDiff = new int[length];
		int index = 0;
		for (Diff diff : diffs) {
			if (diff.operation.compareTo(Operation.EQUAL) != 0) {
				for (int i = index; i < index + diff.getText().length(); i++) {
					arrayOfDiff[i] = 1;
				}
			}
			index += diff.getText().length();
		}
		return arrayOfDiff;
	}

	private NodeContainer[] createArrayOfNodes(ParagraphRevisionContainer revisionContainer, int length) {
		NodeContainer[] arrayOfDiff = new NodeContainer[length];
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

	public Map<Delimiter, List<NodeContainer>> countOldStyles() {
		Map<Delimiter, List<NodeContainer>> map = this.getOldParagraph().countStyles();
		// System.out.println(this.getParagraphDiff().getOldParagraph());
		// for (Delimiter delimiter : map.keySet()) {
		// System.out.println("Delimiter: "+delimiter.getOpenIndicator()+"
		// Count:"+map.get(delimiter).size());
		// }
		return map;

	}

	public Map<Delimiter, List<NodeContainer>> countNewStyles() {
		Map<Delimiter, List<NodeContainer>> map = this.getNewParagraph().countStyles();
		// System.out.println(this.getParagraphDiff().getNewParagraph());
		// for (Delimiter delimiter : map.keySet()) {
		// System.out.println("Delimiter: "+delimiter.getOpenIndicator()+"
		// Count:"+map.get(delimiter).size());
		// }
		return map;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
