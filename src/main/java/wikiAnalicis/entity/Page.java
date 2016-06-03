package wikiAnalicis.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.Gson;

public class Page {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private Integer ns;//name space
	@OneToMany(cascade = CascadeType.ALL)
	private ArrayList<Revision> revisions;
	public Page() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNs() {
		return ns;
	}

	public void setNs(Integer ns) {
		this.ns = ns;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public ArrayList<Revision> getRevisions() {
		return revisions;
	}

	public void setRevisions(ArrayList<Revision> revisions) {
		this.revisions = revisions;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}
	
	
}
