package wikiAnalicis.util.diffAndStyles;

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
		for (int i = 0; i < o; i++) {
			int index = StringUtils.indexOf(textBuilder, this.getOpenIndicator());
			//map.put(index, this.getOpenIndicator());
			for (int j = index; j < index+this.getOpenIndicator().length(); j++) {
				indexValues[j]=id;
			}
			textBuilder.replace(index, index+this.getOpenIndicator().length(),StringUtils.repeat(" ",this.getOpenIndicator().length() ));
		}	
		if (!this.getIsFullParagraph()) {
		
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
		} 
		return indexValues;
	}
}
