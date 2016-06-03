package wikiAnalicis.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;
@Entity
public class Namespace implements Identificable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer keyclave;
	private String stringCase;
	private String value;
	
	public Namespace() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getKey() {
		return keyclave;
	}

	public void setKey(Integer key) {
		this.keyclave = key;
	}

	public String getStringCase() {
		return stringCase;
	}

	public void setStringCase(String stringCase) {
		this.stringCase = stringCase;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}
}
