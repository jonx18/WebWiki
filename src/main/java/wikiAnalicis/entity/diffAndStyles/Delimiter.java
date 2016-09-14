package wikiAnalicis.entity.diffAndStyles;

import java.util.LinkedList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public enum Delimiter {
	NONE("","","",false),
	NOWIKI("<nowiki>","<nowiki>","",false),
	BIG("<big>","<big>","",false),
	SMALL("<small>","<small>","",false),
	SUP("<sup>","<sup>","",false),
	SUB("<sub>","<sub>","",false),
	S("<s>","<s>","",false),
	BLOCKQUOTE("<blockquote>","<blockquote>","",false),
	INCLUDEONLY("<includeonly>","</includeonly>","",false),
	REFERENCE("<ref","</ref>","",false),
	HEADING2("==","==","",false),
	HEADING3("===","===","",false),
	HEADING4("====","====","",false),
	HEADING5("=====","=====","",false),
	ITALIC("''","''","",false),
	BLOD("'''","'''","",false),
	ITALICBLOD("'''''","'''''","",false),
	EXTERNAL("[","]","",false),
	INTERNAL("[[","]]","",false),
	NUMBEREDELEMENT("#","","",true),
	REDIRECTION("redirection","","",true),
	BULLETEDELEMENT("*","","",true),
	INDENT2("::","","",true),
	INDENT1(":","","",true);
	private String openIndicator;
	private String closeIndicator;
	private String name;
	private Boolean isFullParagraph;
	private Delimiter(String openIndicator, String closeIndicator, String name, Boolean isFullParagraph) {
		this.openIndicator = openIndicator;
		this.closeIndicator = closeIndicator;
		this.name = name;
		this.isFullParagraph = isFullParagraph;
	} 
	
	public Boolean getIsFullParagraph() {
		return isFullParagraph;
	}

	public void setIsFullParagraph(Boolean isFullParagraph) {
		this.isFullParagraph = isFullParagraph;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
							indexValues[j]=id*-1;
						}
						textBuilder.replace(index, index+this.getCloseIndicator().length(),StringUtils.repeat(" ",this.getCloseIndicator().length() ));
					}
				}
		} else
		{
			if (o>0) {
				int index = StringUtils.indexOf(StringUtils.trim(textBuilder.toString()), this.getOpenIndicator());
				if (index==0) {
					index = StringUtils.indexOf(textBuilder, this.getOpenIndicator());
					for (int j = index; j < index+this.getOpenIndicator().length(); j++) {
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
			if (!this.getOpenIndicator().equalsIgnoreCase(this.getCloseIndicator())) {
				myId=myId*-1;
			}
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
				containers.add(new TextContainer(text.substring(indiceAnterior, indiceDeAvance)));
			} else {
				int id = indexValues[indiceDeAvance];
				if (id<0) {
					id=id*-1;
				}
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
	public String toString() {
		String id = name();
		String minuscula = id.toLowerCase();
		return minuscula;
		}
	
}
