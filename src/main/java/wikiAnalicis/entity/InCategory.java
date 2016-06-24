package wikiAnalicis.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class InCategory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name = "category_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	 private Category category;
	 @ManyToOne(optional = false,fetch=FetchType.EAGER)
	 @JoinColumn(name = "page_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	private Page page;
	 @ManyToOne(optional = false,fetch=FetchType.EAGER)
	 @JoinColumn(name = "revisionStart_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	private Revision revisionStart;
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name = "revisionEnd_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	private Revision revisionEnd;

	public InCategory() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Revision getRevisionStart() {
		return revisionStart;
	}

	public void setRevisionStart(Revision revisionStart) {
		this.revisionStart = revisionStart;
	}

	public Revision getRevisionEnd() {
		return revisionEnd;
	}

	public void setRevisionEnd(Revision revisionEnd) {
		this.revisionEnd = revisionEnd;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Boolean isActive(){
		return (this.revisionEnd == null);
	}  
}
