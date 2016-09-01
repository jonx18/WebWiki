package wikiAnalicis.entity;

import java.util.LinkedList;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.google.gson.Gson;

/**
 * Esta clase representa a la Wiki
 * @author Jonathan Martin
 *
 */
@Entity
@Table(name="mediawiki")
public class Mediawiki {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@OneToOne(targetEntity= Siteinfo.class,orphanRemoval = true)
	@JoinTable(
	        name = "mediawiki_siteinfo",
	        joinColumns = @JoinColumn(
	            name = "mediawiki_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "siteinfo_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	private Siteinfo siteinfo;
	@OneToMany(orphanRemoval = true,fetch = FetchType.LAZY)
	@JoinTable(
	        name = "mediawiki_page",
	        joinColumns = @JoinColumn(
	            name = "mediawiki_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "page_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<Page> pages= new LinkedList<Page>();//no sequiere borrar
	@Column(name="lang")
	private String lang;
	
	public Mediawiki() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Retorna el siteinfo de la wiki el cual contiene informacion como el nombre o los namespaces
	 * @see Siteinfo
	 * @return Siteinfo
	 */
	public Siteinfo getSiteinfo() {
		return siteinfo;
	}

	public void setSiteinfo(Siteinfo siteinfo) {
		this.siteinfo = siteinfo;
	}
	/**
	 * Retorna el listado de paginas contenidas en la wiki
	 * @see Page
	 * @return List<Page>
	 */
	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		List<Page> temp = pages;
		pages= null;
		String text = gson.toJson(this, getClass());
		pages = temp;
		return text;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
