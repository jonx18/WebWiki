package wikiAnalicis.util.diffAndStyles;

public class Delimiter {
	String openIndicator;
	String closeIndicator;
	public Delimiter(String openIndicator, String closeIndicator) {
		super();
		this.openIndicator = openIndicator;
		this.closeIndicator = closeIndicator;
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
}
