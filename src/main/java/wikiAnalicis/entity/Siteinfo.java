package wikiAnalicis.entity;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.Gson;

public class Siteinfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String sitename;
	private String dbname;
	private String base;
	private String generator;
	private String casee;//problema con el nombre solucionar;
	@OneToMany(cascade = CascadeType.ALL)
	private ArrayList<Namespace> namespaces;//TODO necesitoun converter
	
	public Siteinfo() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public String getCasee() {
		return casee;
	}

	public void setCasee(String casee) {
		this.casee = casee;
	}


	public ArrayList<Namespace> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(ArrayList<Namespace> namespaces) {
		this.namespaces = namespaces;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}
	
}
