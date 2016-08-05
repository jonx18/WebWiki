package wikiAnalicis.util.diffAndStyles;

public class TextContainer extends NodeContainer {
	private String text;

	public TextContainer(String text) {
		super();
		this.delimiter=null;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String componentsToString() {
		// TODO Auto-generated method stub
		return this.getText();
	}
	
}
