package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

public class ParagraphRevisionContainer {

	private LinkedList<Delimiter> delimiters = new LinkedList<Delimiter>();
	private LinkedList<NodeContainer> containers = new LinkedList<NodeContainer>();
	public ParagraphRevisionContainer(String text, List<Delimiter> delimiters) {
		this.delimiters.addAll(delimiters);
		this.textToComponents(text);
	}
	private void textToComponents(String text) {
		int[] indexValues = new int[text.length()];
		System.out.println(indexValues.length);
		for (int i = 0; i < delimiters.size(); i++) {

			indexValues = delimiters.get(i).putIdArray(i+1,indexValues,text);
		}
		System.out.println(text);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i : indexValues) {
			stringBuilder.append(i);
		}
		System.out.println(indexValues.length);
		System.out.println(stringBuilder);
		
		//despues denuevo los recorro y por cada delimiter con la id le paso los delimiters y el array y el text
		//entonces 
	}
}
