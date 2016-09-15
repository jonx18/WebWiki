package wikiAnalicis.entity.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import wikiAnalicis.entity.Revision;

@Entity
@Table(name="paragraphdiff")
public class ParagraphDiff {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name="ischange")
	boolean ischange=false;
	@OneToMany(targetEntity = Diff.class,fetch = FetchType.EAGER)
	@JoinTable(
	        name = "paragraphdiff_olddiffs",
	        joinColumns = @JoinColumn(
	            name = "paragraphdiff_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "olddiffs_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	List<Diff> oldDiffs;
	@OneToMany(targetEntity = Diff.class,fetch = FetchType.EAGER)
	@JoinTable(
	        name = "paragraphdiff_newdiffs",
	        joinColumns = @JoinColumn(
	            name = "paragraphdiff_id", 
	            referencedColumnName = "id"),
	        inverseJoinColumns = @JoinColumn(
	            name = "newdiffs_id", 
	            referencedColumnName = "id")
	    )
	@Cascade(CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 5)
	List<Diff> newDiffs;
	public ParagraphDiff() {
	}
	public ParagraphDiff(LinkedList<Diff[]> diffs) {
		diffs.getClass();
		//this.diffs=diffs;
		this.oldDiffs=new LinkedList<Diff>();
		this.newDiffs=new LinkedList<Diff>();
		for (Diff[] diffs2 : diffs) {
			this.oldDiffs.add(diffs2[0]);
			this.newDiffs.add(diffs2[1]);
			for (Diff diff : diffs2) {
				if (diff!=null && diff.operation!=Operation.EQUAL) {
					ischange=true;
				}
			}
		}
	}
	public String getOldParagraph(){
		int index=0;
		return makeParagraph(index);
	}
	public String getNewParagraph(){
		int index=1;
		return makeParagraph(index);
	}
	private String makeParagraph(int index) {
		StringBuilder builder = new StringBuilder();
		List<Diff> diffs=null;
		if (index==0) {
			diffs = this.oldDiffs;
		} else {
			diffs = this.newDiffs;
		}
		for (int i = 0; i < diffs.size(); i++) {
			if (diffs.get(i)!=null) {
				builder.append(diffs.get(i).text);
			}
		}
//		for (Diff[] diffs2 : diffs) {
//			if (diffs2[index]!=null) {
//				builder.append(diffs2[index].text);
//			}
//
//		}
		return builder.toString();
	}
	public List<Diff> getOldDiffs(){
		int index=0;
		return makeDiffsList(index);
	}
	public List<Diff>  getNewDiffs(){
		int index=1;
		return makeDiffsList(index);
	}
	private List<Diff>  makeDiffsList(int index) {
		List<Diff> diffsresult = new LinkedList<Diff>();
		List<Diff> diffs=null;
		if (index==0) {
			diffs = this.oldDiffs;
		} else {
			diffs = this.newDiffs;
		}
		for (int i = 0; i < diffs.size(); i++) {
			if (diffs.get(i)!=null) {
				diffsresult.add(diffs.get(i));
			}
			else{
				diffsresult.add(new Diff( (index==0)? Operation.DELETE:Operation.INSERT, ""));
			}
		}
//		for (Diff[] diffs2 : diffs) {
//			if (diffs2[index]!=null) {
//				diffsresult.add(diffs2[index]);
//			}
//			else{
//				diffsresult.add(new Diff( (index==0)? Operation.DELETE:Operation.INSERT, ""));
//			}
//
//		}
		return diffsresult;
	}
	public Boolean getChange() {
		return ischange;
	}

	public void setChange(Boolean change) {
		this.ischange = change;
	}

	public LinkedList<Diff[]> getDiffs() {
		LinkedList<Diff[]> result = new LinkedList<Diff[]>();
		for (int i = 0; i < this.oldDiffs.size(); i++) {
			result.add(new Diff[]{this.oldDiffs.get(i),this.newDiffs.get(i)});
		}
		return result;
	}
	public void setDiffs(LinkedList<Diff[]> diffs) {
		this.oldDiffs=new LinkedList<Diff>();
		this.newDiffs=new LinkedList<Diff>();
		for (Diff[] diffs2 : diffs) {
			this.oldDiffs.add(diffs2[0]);
			this.newDiffs.add(diffs2[1]);
			for (Diff diff : diffs2) {
				if (diff!=null && diff.operation!=Operation.EQUAL) {
					ischange=true;
				}
			}
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getIschange() {
		return ischange;
	}
	public void setIschange(Boolean ischange) {
		this.ischange = ischange;
	}
	public void setOldDiffs(List<Diff> oldDiffs) {
		this.oldDiffs = oldDiffs;
	}
	public void setNewDiffs(List<Diff> newDiffs) {
		this.newDiffs = newDiffs;
	}
	
}
