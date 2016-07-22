package wikiAnalicis.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.google.gson.Gson;

/**
 * Esta clase representa una Pagina de la wiki
 * @see MediaWiki
 * @author Jonathan Martin
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Page implements Identificable{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "IdOrGenerated")
	@GenericGenerator(name = "IdOrGenerated", strategy = "wikiAnalicis.util.UseIdOrGenerate")
	@Column(nullable = false)
	//Id de la Page, viene seteado del XML
	protected Long id;
	private String title;
	private Integer ns;// name space
	private String redirect;
	@OneToMany(mappedBy = "page",targetEntity = Revision.class,fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<Revision> revisions=new LinkedList<Revision>();

	public Page() {
		// TODO Auto-generated constructor stub
	}
/**
 * Retorna el titulo de la pagina
 * @return String
 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
/**
 * Retorna la Key del Namespace
 * @see Namespace
 * @return Integer
 */
	public Integer getNs() {
		return ns;
	}

	public void setNs(Integer ns) {
		this.ns = ns;
	}

	/**
	 * Retorna el id de la pagina
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
/**
 * Retorna una lista con las revisiones de la pagina
 * @see Revision
 * @return List<Revision>
 */
	public List<Revision> getRevisions() {
		return revisions;
	}

	public void setRevisions(List<Revision> revisions) {
		this.revisions = revisions;
	}
/**
 * Retorna el titulo de la pagina a la que redirecciona en caso de tener una.
 * @return String
 */
	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}

	/**
	 * Para diferenciar de la subclase en un listado de paginas
	 * @see Category
	 * @return Boolean
	 */
public Boolean isCategory() {
	// TODO Auto-generated method stub
	return false;
}
}
