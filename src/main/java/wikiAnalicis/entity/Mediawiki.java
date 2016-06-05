package wikiAnalicis.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.google.gson.Gson;
@Entity
public class Mediawiki implements Identificable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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
	@OneToMany(orphanRemoval = true,fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	@BatchSize(size = 10)
	private List<Page> pages;//no sequiere borrar
	
	public Mediawiki() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Siteinfo getSiteinfo() {
		return siteinfo;
	}

	public void setSiteinfo(Siteinfo siteinfo) {
		this.siteinfo = siteinfo;
	}

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
	
}
