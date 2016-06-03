package wikiAnalicis.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.Gson;

public class Namespace {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer key;
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
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
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
