package wikiAnalicis.entity.diffAndStyles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "textcontainer")
public class TextContainer extends NodeContainer {
	@Column(columnDefinition="LONGTEXT",name="text")
	private String text;

	public TextContainer() {
		// TODO Auto-generated constructor stub
	}

	public TextContainer(String text) {
		super();
		this.delimiter = null;
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
		return this.getText() + " Change: " + this.getHasChanges();
	}

	@Override
	public int setIntoArray(int index, NodeContainer[] arrayOfDiff) {
		for (int i = index; i < index + this.getText().length(); i++) {
			arrayOfDiff[i] = this;
		}
		return index + this.getText().length();
	}

}
