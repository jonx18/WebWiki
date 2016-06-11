package wikiAnalicis.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
@Entity
public class Category extends Page {

	@OneToMany(mappedBy = "category",targetEntity = InCategory.class,fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<InCategory> pages=new LinkedList<InCategory>();
	@OneToMany(mappedBy = "category",targetEntity = InCategory.class,fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<InCategory> parents=new LinkedList<InCategory>();
	@OneToMany(mappedBy = "category",targetEntity = InCategory.class,fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<InCategory> childrens=new LinkedList<InCategory>();
	
	public Category() {
		// TODO Auto-generated constructor stub
	}
	
	



	public List<InCategory> getPages() {
		return pages;
	}


	public void setPages(List<InCategory> pages) {
		this.pages = pages;
	}


	public List<InCategory> getParents() {
		return parents;
	}


	public void setParents(List<InCategory> parents) {
		this.parents = parents;
	}


	public List<InCategory> getChildrens() {
		return childrens;
	}


	public void setChildrens(List<InCategory> childrens) {
		this.childrens = childrens;
	}


	@Override
	public Boolean isCategory() {
		// TODO Auto-generated method stub
		return true;
	}

}
