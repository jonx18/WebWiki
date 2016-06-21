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

	@OneToMany(mappedBy = "category",targetEntity = InCategory.class,fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<InCategory> pages=new LinkedList<InCategory>();
	@OneToMany(mappedBy = "category",targetEntity = InCategory.class,fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<InCategory> parents=new LinkedList<InCategory>();
	@OneToMany(mappedBy = "category",targetEntity = InCategory.class,fetch = FetchType.EAGER)
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
	public List<InCategory> getActiveChildrens() {
		LinkedList<InCategory>inCategories = new LinkedList<InCategory>();
		for (InCategory inCategory : childrens) {
			if (inCategory.isActive()) {
				inCategories.add(inCategory);
			}
		}
		
		return inCategories;
	}
	public List<InCategory> getActivePages() {
		LinkedList<InCategory>inCategories = new LinkedList<InCategory>();
		for (InCategory inCategory : pages) {
			if (inCategory.isActive()) {
				inCategories.add(inCategory);
			}
		}
		
		return inCategories;
	}
	public List<InCategory> getActiveParents() {
		LinkedList<InCategory>inCategories = new LinkedList<InCategory>();
		for (InCategory inCategory : parents) {
			if (inCategory.isActive()) {
				inCategories.add(inCategory);
			}
		}
		
		return inCategories;
	}
	public InCategory getActiveChildren(Page page) {
		List<InCategory>inCategories = this.getActiveChildrens();
		for (InCategory inCategory : inCategories) {
			if (inCategory.getPage().getId().compareTo(page.getId())==0) {
				return inCategory;
			}
		}
		
		return null;
	}
	public InCategory getActivePage(Page page) {
		List<InCategory>inCategories = this.getActivePages();
		for (InCategory inCategory : inCategories) {
			if (inCategory.getPage().getId().compareTo(page.getId())==0) {
				return inCategory;
			}
		}
		
		return null;
	}
	public InCategory getActiveParent(Page page) {
		List<InCategory>inCategories = this.getActiveParents();
		for (InCategory inCategory : inCategories) {
			if (inCategory.getPage().getId().compareTo(page.getId())==0) {
				return inCategory;
			}
		}
		
		return null;
	}
	
}
