package wikiAnalicis.entity.diffAndStyles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import wikiAnalicis.entity.Revision;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "nodecontainer")
public abstract class NodeContainer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@Enumerated(EnumType.STRING)
	Delimiter delimiter = null;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	NodeContainer parent = null;
	@OneToMany(mappedBy = "parent", targetEntity = NodeContainer.class, fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	List<NodeContainer> childrens = new LinkedList<NodeContainer>();
	@Column(name = "haschanges")
	private Boolean hasChanges = false;

	public NodeContainer() {
		// TODO Auto-generated constructor stub
	}

	public void generateContent() {
		// StringBuilder builder = new StringBuilder();
		// if (parent==null) {
		// System.out.println("sin Padre");
		// for (ParagraphContainer paragraphContainer : paragraphs) {
		// if (diff==0) {
		// builder.append(paragraphContainer.paragraphDiff.getOldParagraph());
		// } else {
		// builder.append(paragraphContainer.paragraphDiff.getNewParagraph());
		// }
		// }
		// } else {
		// System.out.println("con Padre");
		// builder.append(parent.getContent());
		// }
		//
		// int inicio = builder.indexOf(openIndicator);
		// int fin = builder.lastIndexOf(closeIndicator);
		// //System.out.println(openIndicator+","+closeIndicator);
		// //System.out.println("inicio: "+inicio+" fin: "+fin+" largo:
		// "+builder.length());
		// this.content = builder.substring(inicio+openIndicator.length(), fin);
		// System.out.println(this.content);

	}

	public String componentsToString() {
		// if (content==null) {
		// generateContent();
		// }
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

	public Boolean hasDelimiter() {
		return this.delimiter != null;
	}

	public Boolean hasParent() {
		return this.parent != null;
	}

	public Delimiter getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(Delimiter delimiter) {
		this.delimiter = delimiter;
	}

	public Map<Delimiter, List<NodeContainer>> countStyles(Map<Delimiter, List<NodeContainer>> map) {
		if (this.hasDelimiter()) {
			map.get(delimiter).add(this);
		}
		for (NodeContainer nodeContainer : childrens) {
			nodeContainer.countStyles(map);
		}
		return map;
	}
}
