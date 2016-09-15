package wikiAnalicis.entity.diffAndStyles;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stylecontainer")
public class StyleContainer extends NodeContainer {

	public StyleContainer() {
		// TODO Auto-generated constructor stub
	}

	public StyleContainer(Delimiter delimiter, LinkedList<NodeContainer> containers) {
		super();
		this.delimiter = delimiter;
		this.childrens = containers;
		for (NodeContainer nodeContainer : this.childrens) {
			nodeContainer.setParent(this);
		}
	}

	public Delimiter getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(Delimiter delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public String componentsToString() {
		StringBuilder text = new StringBuilder();
		text.append("|stylecomponent start:");
		text.append(this.getDelimiter().getOpenIndicator());
		for (NodeContainer nodeContainer : childrens) {
			text.append("-textcomponent start:");
			text.append(nodeContainer.componentsToString());
			text.append("textcomponent end-");
		}
		text.append(this.getDelimiter().getCloseIndicator());
		text.append(":stylecomponent end Haschange: " + this.getHasChanges() + "|");
		return text.toString();
	}

	@Override
	public int setIntoArray(int index, NodeContainer[] arrayOfDiff) {
		for (int i = index; i < index + this.getDelimiter().getOpenIndicator().length(); i++) {
			if (i >= arrayOfDiff.length) {
				break;
			}
			arrayOfDiff[i] = this;
		}
		index += this.getDelimiter().getOpenIndicator().length();

		for (NodeContainer nodeContainer : childrens) {
			// System.out.println(nodeContainer.componentsToString());
			index = nodeContainer.setIntoArray(index, arrayOfDiff);
		}

		if (index >= arrayOfDiff.length) {// con que ponga la apertura alcanza
			arrayOfDiff[arrayOfDiff.length - 1] = this;
		} else {
			for (int i = index; i < index + this.getDelimiter().getCloseIndicator().length(); i++) {
				arrayOfDiff[i] = this;
			}
		}
		index += this.getDelimiter().getCloseIndicator().length();
		return index;
	}
}
