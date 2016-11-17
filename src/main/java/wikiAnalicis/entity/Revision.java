package wikiAnalicis.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.google.gson.Gson;

import wikiAnalicis.entity.diffAndStyles.Delimiter;
import wikiAnalicis.entity.diffAndStyles.StyleContainer;
import wikiAnalicis.entity.diffAndStyles.TextContainer;

/**
 * Esta clase representa una revision hecha por un autor en una pagina
 * @see Page
 * @see UserContributor
 * @author Jonathan Martin
 *
 */
@Entity
@Table(name="revision")
public class Revision implements Identificable{
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="idOrGenerate")
    @GenericGenerator(name="idOrGenerate",
                      strategy="wikiAnalicis.util.UseIdOrGenerate")
    @Column(name="id")
    //representa el id de la revision que se obtiene del XML
	private Long id;
    //representa el id de la revision anterior a esta, si es 0 esla primera.
    @Column(name="parentid")
	private Long parentid;
    @Column(name="timestamp")
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
    @Column(name="comment")
	private String comment;
    @Column(name="minor")
	private	Boolean minor = false;//TODO no carga con xstream
    @Column(name="model")
	private String model;
    @Column(name="format")
	private String format;
    @Column(name="deleted")
	private Boolean deleted=false;
	@Column(columnDefinition="LONGVARCHAR",name="text")
	private String text;
    @Column(name="sha1")
	private String sha1;
	@Column(columnDefinition="LONGBLOB",name="names")
	@Cascade(CascadeType.ALL)
    private String[] categoryNames;
	
	public Revision() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Retorna el id de la Revision
	 * @return Long
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Retorna el id de la Revision anterior o 0 de no poseer.
	 * @return Long
	 */
	public Long getParentid() {
		return parentid;
	}
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	/**
	 * Retorna el momento en que la revision se realizo
	 * @return Date
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * Retorna el momento en que la revision se realizo en el formato original
	 * @return String
	 */
	public String getStringTimestamp() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
		String date = format.format(this.timestamp);
		return date;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * Permite setear a partir de un string bien formado el momento en que se realizo la revision.
	 * Utiliza de base el siguiente formato: "yyyy-MM-dd'T'HH:mm:ss'Z'"
	 * Ejemplos:
	 * "1993-07-05T11:30:00Z"
	 * "2011-08-02T12:00:00Z"
	 * @param timestamp
	 */
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
	/**
	 * Retorna el usuario que realizo la modificacion.
	 * @see UserContributor
	 * @return UserContributor
	 */
	public UserContributor getContributor() {
		return contributor;
	}
	public void setContributor(UserContributor contributor) {
		this.contributor = contributor;
	}
	/**
	 * Retorna el comentario que justifica la revision.
	 * @return String
	 */
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
	/**
	 * Retorna el texto de la revision.
	 * @return String
	 */
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
	/**
	 * Retorna si se trata de un ravision menor.
	 * @return Boolean
	 */
	public Boolean getMinor() {
		return minor;
	}
	public void setMinor(Boolean minor) {
		this.minor = minor;
	}
	/**
	 * Retorna la Page a la que pertenece.
	 * @see Page
	 * @return Page
	 */
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
	/**
	 * Retorna si se elimino la revision
	 * @return Boolean
	 */
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public Map<Delimiter,Integer> enumerateComponents() {
		HashMap<Delimiter, Integer> map = new HashMap<Delimiter, Integer>();
		StringBuilder texto = new StringBuilder(text);
		List<Delimiter> delimiters = Arrays.asList(Delimiter.values());
		Collections.sort(delimiters, new Comparator<Delimiter>() {
			@Override
			public int compare(Delimiter o1, Delimiter o2) {
				// TODO Auto-generated method stub
				return o1.getPriority().compareTo(o2.getPriority());
			}
		});
		for (Delimiter delimiter : delimiters) {
			int open = StringUtils.countMatches(texto, delimiter.getOpenIndicator());
			int close = StringUtils.countMatches(texto, delimiter.getCloseIndicator());
			if (delimiter.getIsFullParagraph()) {
				map.put(delimiter, open);
			}
			else {
				if (delimiter.getOpenIndicator().compareToIgnoreCase(delimiter.getCloseIndicator())!=0) {
					map.put(delimiter, open-(open-close));
				}
				else {
					map.put(delimiter, open/2);
				}
			}
			texto = new StringBuilder(StringUtils.remove(texto.toString(), delimiter.getOpenIndicator()));
			texto = new StringBuilder(StringUtils.remove(texto.toString(), delimiter.getCloseIndicator()));
		}
		return map;
	}

	
	public Map<Delimiter,Integer> textToComponents(List<Delimiter> delimiters) {
		int[] indexValues = new int[text.length()];
		HashMap<Delimiter, Integer> map = new HashMap<Delimiter, Integer>();
		for (int i = 0; i < delimiters.size(); i++) {
			indexValues = delimiters.get(i).putIdArray(i, indexValues, text);
//			if (delimiters.get(i).equals(Delimiter.BULLETEDELEMENT)) {
//				System.out.println(i);
//			}
//			if (delimiters.get(i).equals(Delimiter.NUMBEREDELEMENT)) {
//				System.out.println(i);
//			}
			map.put(delimiters.get(i), 0);
		}
//		StringBuilder stringBuilder = new StringBuilder();
//		for (int i : indexValues) {
//			stringBuilder.append("|" + i);
//		}
		// Aca comienzo el codigo de conseguir componetes
		int indiceDeAvance = 0;
		while (indiceDeAvance < indexValues.length) {
			if (indexValues[indiceDeAvance] == 0) {
				int indiceAnterior = indiceDeAvance;
				while (indiceDeAvance < indexValues.length && indexValues[indiceDeAvance] == 0) {
					indiceDeAvance++;
				}
				//containers.add(new TextContainer(text.substring(indiceAnterior, indiceDeAvance)));
			} else {
				int id = indexValues[indiceDeAvance];
				// System.out.println(id);
				if (id < 0) {
					id = id * -1;
				}
				Object[] par = delimiters.get(id).getCountsFrom(text, indexValues, indiceDeAvance,
						new LinkedList<Delimiter>(delimiters),map);
				map = (HashMap<Delimiter, Integer>)par[0];
				//map.put(delimiters.get(id), map.get(delimiters.get(id))+1);
				indiceDeAvance = (Integer) par[1];
			}
		}
	return map;
	}
	public String[] getCategoryNames() {
		return categoryNames;
	}
	public void setCategoryNames(String[] categoryNames) {
		this.categoryNames = categoryNames;
	}
	
	
}
