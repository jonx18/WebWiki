package wikiAnalicis.entity.diffAndStyles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParagraphRevisionContainer {

	private LinkedList<Delimiter> delimiters = new LinkedList<Delimiter>();
	private LinkedList<NodeContainer> containers = new LinkedList<NodeContainer>();
	public ParagraphRevisionContainer(String text, List<Delimiter> delimiters) {
		this.delimiters.addAll(delimiters);

		this.textToComponents(text);
	}
	private void textToComponents(String text) {
		int[] indexValues = new int[text.length()];
		for (int i = 0; i < delimiters.size(); i++) {
			indexValues = delimiters.get(i).putIdArray(i,indexValues,text);
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i : indexValues) {
			stringBuilder.append("|"+i);
		}
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
				//System.out.println(id);
				if (id<0) {
					id=id*-1;
				}
				Object[] par = delimiters.get(id).getComponentsFrom(text,indexValues,indiceDeAvance,delimiters);
				containers.add((StyleContainer)par[0]);
				indiceDeAvance=(Integer)par[1];
			}
		}
	}
	public void toDelimitedText() {
		StringBuilder prueba = new StringBuilder();
		for (NodeContainer nodeContainer : containers) {
			prueba.append(nodeContainer.componentsToString());
		}
		System.out.println("Resultado   "+prueba);
	}
	public NodeContainer[] setIntoArray(NodeContainer[] arrayOfDiff) {
		int index=0;
		for (NodeContainer nodeContainer : this.getContainers()) {
			//System.out.println(nodeContainer.componentsToString());
			index=nodeContainer.setIntoArray(index,arrayOfDiff);
		}
		return arrayOfDiff;
	}
	public Map<Delimiter, List<NodeContainer>> countStyles() {
		HashMap<Delimiter, List<NodeContainer>> map = new HashMap<Delimiter, List<NodeContainer>>();
		for (Delimiter delimiter : this.delimiters) {
			map.put(delimiter, new LinkedList<NodeContainer>());
		}
		for (NodeContainer nodeContainer : containers) {
			map=(HashMap<Delimiter, List<NodeContainer>>) nodeContainer.countStyles(map);
		}
		return map;
	}
	public LinkedList<Delimiter> getDelimiters() {
		return delimiters;
	}
	public void setDelimiters(LinkedList<Delimiter> delimiters) {
		this.delimiters = delimiters;
	}
	public LinkedList<NodeContainer> getContainers() {
		return containers;
	}
	public void setContainers(LinkedList<NodeContainer> containers) {
		this.containers = containers;
	}
	
}
