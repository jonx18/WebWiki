package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;

public class StyleContainer extends NodeContainer {
	private Delimiter delimiter;
	
	public StyleContainer(Delimiter delimiter, LinkedList<NodeContainer> containers) {
		super();
		this.delimiter = delimiter;
		this.childrens = containers;
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
	text.append(":stylecomponent end|");
	return text.toString();
}
	
}
