package wikiAnalicis.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.Gson;

public class Mediawiki {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Siteinfo siteinfo;
	@OneToMany(cascade = CascadeType.ALL)
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
		return gson.toJson(this, getClass());
	}
	
}
