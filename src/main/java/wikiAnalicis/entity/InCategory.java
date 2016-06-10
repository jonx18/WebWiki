package wikiAnalicis.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
//@Entity
public class InCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private Category category;
	private Categorizable categorizable;
	private Revision revisionStart;
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
	public Categorizable getCategorizable() {
		return categorizable;
	}
	public void setCategorizable(Categorizable categorizable) {
		this.categorizable = categorizable;
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
	
	
	
	
}
