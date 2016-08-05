package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class Delimiter {
	private String openIndicator;
	private String closeIndicator;
	private Boolean isFullParagraph;

	public Delimiter(String openIndicator, String closeIndicator, Boolean isFullParagraph) {
		super();
		this.openIndicator = openIndicator;
		this.closeIndicator = closeIndicator;
		this.isFullParagraph = isFullParagraph;
	}
	public Delimiter(String openIndicator, Boolean isFullParagraph) {
		super();
		this.openIndicator = openIndicator;
		this.closeIndicator = "";
		this.isFullParagraph = isFullParagraph;
	}
	public Boolean getIsFullParagraph() {
		return isFullParagraph;
	}

	public void setIsFullParagraph(Boolean isFullParagraph) {
		this.isFullParagraph = isFullParagraph;
	}

	public String getOpenIndicator() {
		return openIndicator;
	}
	public void setOpenIndicator(String openIndicator) {
		this.openIndicator = openIndicator;
	}
	public String getCloseIndicator() {
		return closeIndicator;
	}
	public void setCloseIndicator(String closeIndicator) {
		this.closeIndicator = closeIndicator;
	}
	public Boolean arePair(String openIndicator,String closeIndicator){
		return this.openIndicator.equalsIgnoreCase(openIndicator) && this.closeIndicator.equalsIgnoreCase(closeIndicator);
	}
	public int[] putIdArray(int id, int[] indexValues, String text) {
		TreeMap<Integer, String> map = new TreeMap<Integer,String>();
		StringBuilder textBuilder = new StringBuilder(text);
		int o = StringUtils.countMatches(textBuilder, this.getOpenIndicator());
		int c = StringUtils.countMatches(textBuilder, this.getCloseIndicator());
		if (!this.getIsFullParagraph()) {
				
				for (int i = 0; i < o; i++) {
					int index = StringUtils.indexOf(textBuilder, this.getOpenIndicator());
					//map.put(index, this.getOpenIndicator());
					for (int j = index; j < index+this.getOpenIndicator().length(); j++) {
						indexValues[j]=id;
					}
					textBuilder.replace(index, index+this.getOpenIndicator().length(),StringUtils.repeat(" ",this.getOpenIndicator().length() ));
				}
				if (!this.getOpenIndicator().equalsIgnoreCase(this.getCloseIndicator())) {
					for (int i = 0; i < c; i++) {
						int index = StringUtils.indexOf(textBuilder, this.getCloseIndicator());
						//map.put(index, this.getCloseIndicator());
						for (int j = index; j < index+this.getCloseIndicator().length(); j++) {
							indexValues[j]=id;
						}
						textBuilder.replace(index, index+this.getCloseIndicator().length(),StringUtils.repeat(" ",this.getCloseIndicator().length() ));
					}
				}
		} else
		{
			if (o>0) {
				int index = StringUtils.indexOf(textBuilder, this.getOpenIndicator());
				if (index==0) {
					for (int j = index; j < index+this.getCloseIndicator().length(); j++) {
						indexValues[j]=id;
					}
				}
			}
		}
		return indexValues;
	}
	public Object[] getComponentsFrom(String text, int[] indexValues, int indiceDeAvance,
			LinkedList<Delimiter> delimiters) {
		LinkedList<NodeContainer> containers = new LinkedList<NodeContainer>();
		int myId = indexValues[indiceDeAvance];
		indiceDeAvance+= this.getOpenIndicator().length();
		int fin=indexValues.length;
		if (!this.getIsFullParagraph()) {
			for (int i = indiceDeAvance; i < indexValues.length; i++) {
				if (indexValues[i]==myId) {
					fin=i;
					break;
				}
			}
		}
		while(indiceDeAvance<fin){
			if (indexValues[indiceDeAvance]==0) {
				int indiceAnterior= indiceDeAvance;
				while (indiceDeAvance<fin && indexValues[indiceDeAvance]==0){
					indiceDeAvance++;
				}
				System.out.println("****************");
				containers.add(new TextContainer(text.substring(indiceAnterior, indiceDeAvance)));
			} else {
				int id = indexValues[indiceDeAvance];
				Object[] par = delimiters.get(id).getComponentsFrom(text,indexValues,indiceDeAvance,delimiters);
				containers.add((StyleContainer)par[0]);
				indiceDeAvance=(Integer)par[1];
			}
		}
		StyleContainer styleContainer = new StyleContainer(this, containers);
		indiceDeAvance+= this.getCloseIndicator().length();
		Object[] result = {styleContainer,indiceDeAvance};
		return result;
	}
}
