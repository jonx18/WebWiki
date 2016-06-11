package wikiAnalicis.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.google.gson.Gson;

@Entity
public class Revision implements Identificable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="idOrGenerate")
    @GenericGenerator(name="idOrGenerate",
                      strategy="wikiAnalicis.util.UseIdOrGenerate")
	private Long id;
	private Long parentid;
	private Date timestamp = Calendar.getInstance().getTime();
	@OneToOne(fetch=FetchType.EAGER, targetEntity = UserContributor.class)
	@JoinColumn(name = "contributor_id")
	@Cascade({CascadeType.SAVE_UPDATE,CascadeType.MERGE,CascadeType.REFRESH,
		CascadeType.REPLICATE,CascadeType.DETACH,CascadeType.PERSIST,CascadeType.LOCK,CascadeType.SAVE_UPDATE})
	@Fetch(FetchMode.JOIN)
	@BatchSize(size = 5)
	private UserContributor contributor;
	@ManyToOne(optional = false,fetch=FetchType.LAZY)
	@JoinColumn(name = "page_id")
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	private Page page;
	private String comment;
	private	Boolean minor = false;//TODO no carga con xstream
	private String model;
	private String format;
	private Boolean deleted=false;
	@Column(columnDefinition="LONGBLOB")
	private String text;
	private String sha1;
	
	public Revision() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentid() {
		return parentid;
	}
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setTimestamp(String timestamp) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
		Date date = null;
		try {
			date = format.parse(timestamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.timestamp = date;
	}
	public UserContributor getContributor() {
		return contributor;
	}
	public void setContributor(UserContributor contributor) {
		this.contributor = contributor;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	
	public Boolean getMinor() {
		return minor;
	}
	public void setMinor(Boolean minor) {
		this.minor = minor;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		return gson.toJson(this, getClass());
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
}
