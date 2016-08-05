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
//		for (int i = 0; i < delimiters.size(); i++) {
//			System.out.println("delimiter id: "+i+" open: "+delimiters.get(i).getOpenIndicator());
//			indexValues = delimiters.get(i).putIdArray(i,indexValues,text);
//		}
		System.out.println(text);
		StringBuilder stringBuilder = new StringBuilder();
		for (int i : indexValues) {
			stringBuilder.append("|"+i);
		}
		System.out.println(stringBuilder);
		//Aca comienzo el codigo de conseguir componetes
		int indiceDeAvance=0;
		while(indiceDeAvance<indexValues.length){
			if (indexValues[indiceDeAvance]==0) {
				int indiceAnterior= indiceDeAvance;
				while (indiceDeAvance<indexValues.length && indexValues[indiceDeAvance]==0){
					indiceDeAvance++;
				}
				containers.add(new TextContainer(text.substring(indiceAnterior, indiceDeAvance)));
			} else {
				int id = indexValues[indiceDeAvance];
				Object[] par = delimiters.get(id).getComponentsFrom(text,indexValues,indiceDeAvance,delimiters);
				containers.add((StyleContainer)par[0]);
				indiceDeAvance=(Integer)par[1];
			}
		}
		if (text.contains("YO<sub>1</sub>+YO<sub>2</sub>==YO<sup>YO</sup>==")) {
			System.out.println("ej");
		}
		StringBuilder prueba = new StringBuilder();
		for (NodeContainer nodeContainer : containers) {
			prueba.append(nodeContainer.componentsToString());
		}
		System.out.println("Resultado   "+prueba);
	}
}