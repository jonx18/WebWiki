package wikiAnalicis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;
/**
 * Esta clase representa un Namespace de una Mediawiki.
 * @see Siteinfo
 * @author Jonathan Martin
 *
 */
@Entity
@Table(name="namespace")
public class Namespace{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="keyclave")
	private Integer keyclave;
	@Column(name="stringcase")
	private String stringCase;
	@Column(name="value")
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
/**
 * Retorna la clave del namespace con el que se identifica en las Pages
 * @see Page
 * @return Integer
 */
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
/**
 * Retorna el nombre del namespace
 * @return String
 */
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
