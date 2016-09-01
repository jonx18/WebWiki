package wikiAnalicis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Esta clase representa el lapso de tiempo en el que una Page o Category fue contenida en una Category.
 * Limitando con la revision de entrada y de salida a dicha Category.
 * @see Page
 * @see Category
 * @see Revision
 * @author Jonathan Martin
 *
 */
@Entity
@Table(name="incategory")
public class InCategory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name = "category_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	 @Cascade(CascadeType.ALL)
	 //categoria que la contiene
	 private Category category;
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name = "page_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	 @Cascade(CascadeType.ALL)
	 //page o categoria contenida
	private Page page;
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name = "revisionstart_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	 @Cascade(CascadeType.ALL)
	 //revision de entrada a la categoria
	private Revision revisionStart;
	 @ManyToOne(fetch=FetchType.EAGER)
	 @JoinColumn(name = "revisionend_id")
	 @Fetch(FetchMode.SELECT)
	 @BatchSize(size = 5)
	 @Cascade(CascadeType.ALL)
	 //revision de salida de la categoria
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
/**
 * Se obtiene la Page o Category contenida 
 * @see Page
 * @return Page
 */
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
/**
 * Se obtiene la revision con la que se ingresa a la Category.
 * @see Revision
 * @return Revision
 */
	public Revision getRevisionStart() {
		return revisionStart;
	}

	public void setRevisionStart(Revision revisionStart) {
		this.revisionStart = revisionStart;
	}

	/**
	 * Se obtiene la revision con la que se deja la Category.
	 * @see Revision
	 * @return Revision
	 */
	public Revision getRevisionEnd() {
		return revisionEnd;
	}

	public void setRevisionEnd(Revision revisionEnd) {
		this.revisionEnd = revisionEnd;
	}
/**
 * Retorna la Category que contiene o contuvo a laPage o Category
 * @return
 */
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
/**
 * Retorna si la Category aun contiene a la Page o Category.
 * @return Boolean
 */
	public Boolean isActive(){
		return (this.revisionEnd == null);
	}  
}
