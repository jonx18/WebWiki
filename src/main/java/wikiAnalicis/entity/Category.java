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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
	       if (!(obj instanceof Category)){
	            return false;
	            }
	        if (obj == this){
	            return true;
	        }
	        Category rhs = (Category) obj;
	        return new EqualsBuilder().
	            // if deriving: appendSuper(super.equals(obj)).
	        		append(id, rhs.getId()).
	        		append(this.getTitle(), rhs.getTitle()).
	            isEquals();
		
	}
	@Override
	public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                append(id).
                append(this.getTitle()).
                toHashCode();

	}
	
}
