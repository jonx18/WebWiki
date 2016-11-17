package wikiAnalicis.entity.diffAndStyles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public enum Delimiter {
	NONE("","","",false,Integer.MAX_VALUE),
	NOWIKI("<nowiki>","</nowiki>","",false,23),
	BIG("<big>","</big>","",false,22),
	SMALL("<small>","</small>","",false,21),
	SUP("<sup>","</sup>","",false,20),
	SUB("<sub>","</sub>","",false,19),
	S("<s>","</s>","",false,18),
	BLOCKQUOTE("<blockquote>","</blockquote>","",false,17),
	INCLUDEONLY("<includeonly>","</includeonly>","",false,16),
	REFERENCE("<ref","</ref>","",false,15),
	HEADING2("==","==","",false,4),
	HEADING3("===","===","",false,3),
	HEADING4("====","====","",false,2),
	HEADING5("=====","=====","",false,1),
	ITALIC("''","''","",false,7),
	BLOD("'''","'''","",false,6),
	ITALICBLOD("'''''","'''''","",false,5),
	EXTERNAL("[","]","",false,10),
	INTERNAL("[[","]]","",false,9),
	FILE("[[File","","",true,8),
	NUMBEREDELEMENT("#","","",true,12),
	REDIRECTION("redirection","","",true,11),
	BULLETEDELEMENT("*","","",true,13),
	INDENT2("::","","",true,14),
	INDENT1(":","","",true,15),
	INFOBOX("{{Infobox","","",true,24),
	WIKITABLE("{| class=\"wikitable","","",true,25),
	CITE("{{cite","","",true,26);
	private String openIndicator;
	private String closeIndicator;
	private String name;
	private Boolean isFullParagraph;
	private Integer priority;
	private Delimiter(String openIndicator, String closeIndicator, String name, Boolean isFullParagraph,Integer priority) {
		this.openIndicator = openIndicator;
		this.closeIndicator = closeIndicator;
		this.name = name;
		this.isFullParagraph = isFullParagraph;
		this.priority=priority;
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
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public int[] putIdArray(int id, int[] indexValues, String text) {
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

	public Object[] getCountsFrom(String text, int[] indexValues, int indiceDeAvance, LinkedList<Delimiter> delimiters,
			HashMap<Delimiter, Integer> map) {
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
				//containers.add(new TextContainer(text.substring(indiceAnterior, indiceDeAvance)));
			} else {
				int id = indexValues[indiceDeAvance];
				if (id<0) {
					id=id*-1;
				}
				Object[] par = delimiters.get(id).getCountsFrom(text,indexValues,indiceDeAvance,delimiters,map);
				map = (HashMap<Delimiter, Integer>)par[0];
			//	map.put(delimiters.get(id), map.get(delimiters.get(id))+1);
				indiceDeAvance=(Integer)par[1];
			}
		}
		indiceDeAvance+= this.getCloseIndicator().length();
		map.put(this, map.get(this)+1);
		Object[] result = {map,indiceDeAvance};
		return result;
	}
	
}
