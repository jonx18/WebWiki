package wikiAnalicis.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.gson.Gson;
/**
 * Esta clase contiene la informacion referente a una wiki en particular.
 * Contiene el nombre del sitio, los namespaces.
 * @see Mediawiki
 * @author Jonathan Martin
 *
 */
@Entity
public class Siteinfo {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String sitename;
	private String dbname;
	private String base;
	private String generator;
	private String casee;//problema con el nombre solucionar;
	@OneToMany( targetEntity= Namespace.class, fetch = FetchType.EAGER,orphanRemoval=true)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
    @BatchSize(size = 10)
	private List<Namespace> namespaces= new LinkedList<Namespace>();//TODO necesitoun converter
	
	
	public Siteinfo() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
/**
 * Retorna el nombre de la MediaWiki.
 * @return String
 */
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

/**
 * Retorna un listado de los namespaces.
 * @see Namespace
 * @return List<Namespace>
 */
	public List<Namespace> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(List<Namespace> namespaces) {
		this.namespaces = namespaces;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}
	
}
