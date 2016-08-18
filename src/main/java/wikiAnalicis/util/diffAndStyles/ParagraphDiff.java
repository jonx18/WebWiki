package wikiAnalicis.util.diffAndStyles;

import java.util.LinkedList;
import java.util.List;

import wikiAnalicis.util.diffAndStyles.diff_match_patch.Diff;
import wikiAnalicis.util.diffAndStyles.diff_match_patch.Operation;

public class ParagraphDiff {
	Boolean change=false;
	LinkedList<Diff[]> diffs;
	public ParagraphDiff(LinkedList<Diff[]> diffs) {
		this.diffs=diffs;
		for (Diff[] diffs2 : diffs) {
			for (Diff diff : diffs2) {
				if (diff!=null && diff.operation!=Operation.EQUAL) {
					change=true;
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
		for (Diff[] diffs2 : diffs) {
			if (diffs2[index]!=null) {
				builder.append(diffs2[index].text);
			}

		}
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
		for (Diff[] diffs2 : diffs) {
			if (diffs2[index]!=null) {
				diffsresult.add(diffs2[index]);
			}
			else{
				diffsresult.add(new Diff( (index==0)? Operation.DELETE:Operation.INSERT, ""));
			}

		}
		return diffsresult;
	}
	public Boolean getChange() {
		return change;
	}

	public void setChange(Boolean change) {
		this.change = change;
	}

	public LinkedList<Diff[]> getDiffs() {
		return diffs;
	}
	public void setDiffs(LinkedList<Diff[]> diffs) {
		this.diffs = diffs;
	}
}
