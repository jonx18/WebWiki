package wikiAnalicis.entity.statics;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.MapKeyTemporal;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import wikiAnalicis.entity.InCategory;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.diffAndStyles.Delimiter;
@Entity
@Table(name = "pagestatics")
public class PageStatistics {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	@OneToOne(optional = false,fetch=FetchType.EAGER)
	@JoinColumn(name = "page_id",unique=true)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private Page page;
	@Column(name = "totalrevisiones")
	private long totalRevisiones;
	@ElementCollection(fetch = FetchType.EAGER,targetClass = Long.class)
	@JoinTable(name = "distribuciondeaporte")
	@MapKeyColumn(name="nombre")
    @Column(name="cantidad")
	@Cascade(CascadeType.ALL)
	private Map<String, Long> distribucionDeAporte;
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "revisionesdia")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@Cascade(CascadeType.ALL)
	private Map<Date,Long> 	revisionesdia;
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "contenidodia")
	@MapKeyTemporal(TemporalType.TIMESTAMP)
	@Cascade(CascadeType.ALL)
	private Map<Date,Long> 	contenidoDia;
	@OneToMany(targetEntity = InCategory.class,fetch = FetchType.EAGER)
	@JoinTable(
	        name = "pagestatics_categories",
	        joinColumns = @JoinColumn(
	            name = "pagestatics_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "incategory_id", 
	            referencedColumnName = "id")
	    )
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<InCategory> categories; 
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(
	        name = "pagestatistics_date",
	    	        joinColumns = @JoinColumn(
	    		            name = "pagestatics_id", 
	    		            referencedColumnName = "id"))
	@Temporal(TemporalType.TIMESTAMP)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private List<Date> dates=null;
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "mapstylechanges")
	@MapKeyEnumerated(EnumType.STRING)
	@Cascade(CascadeType.ALL)
	@Column(columnDefinition="LONGBLOB",name="valores")
	private Map<Delimiter, Integer[]> mapStyleChanges;
	public PageStatistics() {
		// TODO Auto-generated constructor stub
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public Long getTotalRevisiones() {
		return totalRevisiones;
	}
	public void setTotalRevisiones(Long totalRevisiones) {
		this.totalRevisiones = totalRevisiones;
	}
//	public Map<String, Long> getDistribucionDeAporte() {
//		return distribucionDeAporte;
//	}
//	public void setDistribucionDeAporte(Map<String, Long> distribucionDeAporte) {
//		this.distribucionDeAporte = distribucionDeAporte;
//	}
	public Map<Date, Long> getRevisionesDia() {
		return new TreeMap<Date, Long>(revisionesdia);
	}
	public void setRevisionesDia(Map<Date, Long> revisionesDia) {
		this.revisionesdia = revisionesDia;
	}
	public Map<Date, Long> getContenidoDia() {
		return new TreeMap<Date, Long>(this.contenidoDia);
	}
	public void setContenidoDia(Map<Date, Long> contenidoDia) {
		this.contenidoDia = contenidoDia;
	}
	public List<InCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<InCategory> categories) {
		this.categories = categories;
	}
	public List<Date> getDates() {
		return dates;
	}
	public void setDates(List<Date> dates) {
		this.dates = dates;
	}
	public Date[] getDatesArray() {
		return dates.toArray(new Date[dates.size()]);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Map<String, Long> getDistribucionDeAporte() {
		return distribucionDeAporte;
	}
	public void setDistribucionDeAporte(Map<String, Long> distribucionDeAporte) {
		this.distribucionDeAporte = distribucionDeAporte;
	}
	public Map<Delimiter, Integer[]> getMapStyleChanges() {
		return mapStyleChanges;
	}
	public void setMapStyleChanges(Map<Delimiter, Integer[]> mapStyleChanges) {
		this.mapStyleChanges = mapStyleChanges;
	}
	
}
