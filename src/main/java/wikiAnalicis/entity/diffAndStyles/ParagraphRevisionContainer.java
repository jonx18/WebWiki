package wikiAnalicis.entity.diffAndStyles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import wikiAnalicis.entity.Namespace;

@Entity
@Table(name = "paragraphrevisioncontainer")
public class ParagraphRevisionContainer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@ElementCollection(targetClass = Delimiter.class, fetch = FetchType.LAZY)
	@JoinTable(name = "paragraphrevisioncontainer_delimiter", joinColumns = @JoinColumn(name = "paragraphrevisioncontainer_id", referencedColumnName = "id") )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	@BatchSize(size = 10)
	@Enumerated(EnumType.STRING)
	private List<Delimiter> delimiters = new LinkedList<Delimiter>();
	@OneToMany(targetEntity = NodeContainer.class, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinTable(name = "paragraphrevisioncontainer_container", joinColumns = @JoinColumn(name = "paragraphrevisioncontainer_id", referencedColumnName = "id") , inverseJoinColumns = @JoinColumn(name = "container_id", referencedColumnName = "id") )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	@BatchSize(size = 10)
	private List<NodeContainer> containers = new LinkedList<NodeContainer>();

	public ParagraphRevisionContainer() {
		// TODO Auto-generated constructor stub
	}

	public ParagraphRevisionContainer(String text, List<Delimiter> delimiters) {
		this.delimiters.addAll(delimiters);

		this.textToComponents(text);
	}

	private void textToComponents(String text) {
		int[] indexValues = new int[text.length()];
		for (int i = 0; i < delimiters.size(); i++) {
			indexValues = delimiters.get(i).putIdArray(i, indexValues, text);
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i : indexValues) {
			stringBuilder.append("|" + i);
		}
		// Aca comienzo el codigo de conseguir componetes
		int indiceDeAvance = 0;
		while (indiceDeAvance < indexValues.length) {
			if (indexValues[indiceDeAvance] == 0) {
				int indiceAnterior = indiceDeAvance;
				while (indiceDeAvance < indexValues.length && indexValues[indiceDeAvance] == 0) {
					indiceDeAvance++;
				}
				containers.add(new TextContainer(text.substring(indiceAnterior, indiceDeAvance)));
			} else {
				int id = indexValues[indiceDeAvance];
				// System.out.println(id);
				if (id < 0) {
					id = id * -1;
				}
				Object[] par = delimiters.get(id).getComponentsFrom(text, indexValues, indiceDeAvance,
						new LinkedList<Delimiter>(this.delimiters));
				containers.add((StyleContainer) par[0]);
				indiceDeAvance = (Integer) par[1];
			}
		}
	}

	public void toDelimitedText() {
		StringBuilder prueba = new StringBuilder();
		for (NodeContainer nodeContainer : containers) {
			prueba.append(nodeContainer.componentsToString());
		}
		System.out.println("Resultado   " + prueba);
	}

	public NodeContainer[] setIntoArray(NodeContainer[] arrayOfDiff) {
		int index = 0;
		for (NodeContainer nodeContainer : this.getContainers()) {
			// System.out.println(nodeContainer.componentsToString());
			index = nodeContainer.setIntoArray(index, arrayOfDiff);
		}
		return arrayOfDiff;
	}

	public Map<Delimiter, List<NodeContainer>> countStyles() {
		HashMap<Delimiter, List<NodeContainer>> map = new HashMap<Delimiter, List<NodeContainer>>();
		for (Delimiter delimiter : this.delimiters) {
			map.put(delimiter, new LinkedList<NodeContainer>());
		}
		for (NodeContainer nodeContainer : containers) {
			map = (HashMap<Delimiter, List<NodeContainer>>) nodeContainer.countStyles(map);
		}
		return map;
	}

	public LinkedList<Delimiter> getDelimiters() {
		return new LinkedList<Delimiter>(this.delimiters);
	}

	public void setDelimiters(LinkedList<Delimiter> delimiters) {
		this.delimiters = delimiters;
	}

	public LinkedList<NodeContainer> getContainers() {
		return new LinkedList<NodeContainer>(this.containers);
	}

	public void setContainers(LinkedList<NodeContainer> containers) {
		this.containers = containers;
	}

}
