package wikiAnalicis.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
/**
 * Esta clase es una subclase de Page para especificar las categorias con su comportamiento particular.
 * @see Page
 * @see MediaWiki
 * @author Jonathan Martin
 *
 */
@Entity
public class Category extends Page {

	@ManyToMany(targetEntity = InCategory.class,fetch = FetchType.EAGER)
	@JoinTable(
	        name = "category_page",
	        joinColumns = @JoinColumn(
	            name = "category_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "inCategory_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	//representa a las paginas contenidas en la categoria no otras categorias
	private List<InCategory> pages=new LinkedList<InCategory>();
	@ManyToMany(targetEntity = InCategory.class,fetch = FetchType.EAGER)
	@JoinTable(
	        name = "category_parent",
	        joinColumns = @JoinColumn(
	            name = "category_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "inCategory_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	//Representa a las categorais que contienen a esta.
	private List<InCategory> parents=new LinkedList<InCategory>();
	@ManyToMany(targetEntity = InCategory.class,fetch = FetchType.EAGER)
	@JoinTable(
	        name = "category_child",
	        joinColumns = @JoinColumn(
	            name = "category_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "inCategory_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	//Representa a las categorias contenidas por estas.
	private List<InCategory> childrens=new LinkedList<InCategory>();
	
	public Category() {
		// TODO Auto-generated constructor stub
	}
	
	


/**
 * Contiene las paginas que contiene o contuvo esta categoria
 * @see InCategory
 * @return List<InCategory>
 */
	public List<InCategory> getPages() {
		return pages;
	}


	public void setPages(List<InCategory> pages) {
		this.pages = pages;
	}

	/**
	 * Contiene las categorias que contienen o contuvieron a esta categoria
	 * @see InCategory
	 * @return List<InCategory>
	 */
	public List<InCategory> getParents() {
		return parents;
	}


	public void setParents(List<InCategory> parents) {
		this.parents = parents;
	}

	/**
	 * Contiene las categorias que contiene o contuvo esta categoria
	 * @see InCategory
	 * @return List<InCategory>
	 */
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
	/**
	 * Contiene las categorias que contiene esta categoria
	 * @see InCategory
	 * @return List<InCategory>
	 */
	public List<InCategory> getActiveChildrens() {
		LinkedList<InCategory>inCategories = new LinkedList<InCategory>();
		for (InCategory inCategory : childrens) {
			if (inCategory.isActive()) {
				inCategories.add(inCategory);
			}
		}
		
		return inCategories;
	}
	/**
	 * Contiene las paginas que contiene esta categoria
	 * @see InCategory
	 * @return List<InCategory>
	 */
	public List<InCategory> getActivePages() {
		LinkedList<InCategory>inCategories = new LinkedList<InCategory>();
		for (InCategory inCategory : pages) {
			if (inCategory.isActive()) {
				inCategories.add(inCategory);
			}
		}
		
		return inCategories;
	}
	/**
	 * Contiene las categorias que contienen a esta categoria
	 * @see InCategory
	 * @return List<InCategory>
	 */
	public List<InCategory> getActiveParents() {
		LinkedList<InCategory>inCategories = new LinkedList<InCategory>();
		for (InCategory inCategory : parents) {
			if (inCategory.isActive()) {
				inCategories.add(inCategory);
			}
		}
		
		return inCategories;
	}
	/**
	 * En base a una Page obtiene el el InCategory abierto de este listado con esa Page
	 * @see Page
	 * @see InCategory
	 * @return InCategory
	 */
	public InCategory getActiveChildren(Page page) {
		List<InCategory>inCategories = this.getActiveChildrens();
		for (InCategory inCategory : inCategories) {
			if (inCategory.getPage().getId().compareTo(page.getId())==0) {
				return inCategory;
			}
		}
		
		return null;
	}
	/**
	 * En base a una Page obtiene el el InCategory abierto de este listado con esa Page
	 * @see Page
	 * @see InCategory
	 * @return InCategory
	 */
	public InCategory getActivePage(Page page) {
		List<InCategory>inCategories = this.getActivePages();
		for (InCategory inCategory : inCategories) {
			if (inCategory.getPage().getId().compareTo(page.getId())==0) {
				return inCategory;
			}
		}
		
		return null;
	}
	/**
	 * En base a una Page obtiene el el InCategory abierto de este listado con esa Page
	 * @see Page
	 * @see InCategory
	 * @return InCategory
	 */
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
