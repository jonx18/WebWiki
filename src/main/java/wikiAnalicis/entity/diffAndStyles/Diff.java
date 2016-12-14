package wikiAnalicis.entity.diffAndStyles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "diff")
/**
 * Class representing one diff operation.
 */
public class Diff {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	/**
	 * One of: INSERT, DELETE or EQUAL.
	 */
	@Enumerated(EnumType.STRING)
	public Operation operation;
	/**
	 * The text associated with this diff operation.
	 */
	@Column(columnDefinition="LONGTEXT",name="text")
	public String text;

	public Diff() {
		// TODO Auto-generated constructor stub
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Constructor. Initializes the diff with the provided values.
	 * 
	 * @param operation
	 *            One of INSERT, DELETE or EQUAL.
	 * @param text
	 *            The text being applied.
	 */
	public Diff(Operation operation, String text) {
		// Construct a diff with the specified operation and text.
		this.operation = operation;
		this.text = text;
	}

	/**
	 * Display a human-readable version of this Diff.
	 * 
	 * @return text version.
	 */
	public String toString() {
		String prettyText = this.text.replace('\n', '\u00b6');
		return "Diff(" + this.operation + ",\"" + prettyText + "\")";
	}

	/**
	 * Create a numeric hash value for a Diff. This function is not used by DMP.
	 * 
	 * @return Hash value.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = (operation == null) ? 0 : operation.hashCode();
		result += prime * ((text == null) ? 0 : text.hashCode());
		return result;
	}

	/**
	 * Is this Diff equivalent to another Diff?
	 * 
	 * @param obj
	 *            Another Diff to compare against.
	 * @return true or false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Diff other = (Diff) obj;
		if (operation != other.operation) {
			return false;
		}
		if (text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
