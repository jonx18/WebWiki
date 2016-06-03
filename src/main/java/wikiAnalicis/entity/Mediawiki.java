package wikiAnalicis.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	@ManyToOne(cascade = CascadeType.ALL, targetEntity= Siteinfo.class)
	private Siteinfo siteinfo;
	@OneToMany(cascade = CascadeType.ALL, targetEntity= Page.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
	private List<Page> pages;
	
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
