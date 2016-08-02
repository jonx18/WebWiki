package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;

public class StyleContainer extends NodeContainer {
	private Delimiter delimiter;
	private LinkedList<NodeContainer> containers = new LinkedList<NodeContainer>();
	public StyleContainer(Delimiter delimiter) {
		super(openIndicator, diff);
		this.delimiter = delimiter;
	}
	
}
