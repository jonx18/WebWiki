package wikiAnalicis.util.diffAndStyles;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import wikiAnalicis.entity.diffAndStyles.Diff;
import wikiAnalicis.entity.diffAndStyles.Operation;
import wikiAnalicis.entity.diffAndStyles.ParagraphDiff;


public class ParagraphDiffer {

	public ParagraphDiffer() {
		// TODO Auto-generated constructor stub
	}
	public LinkedList<ParagraphDiff> paragraphComparetor(String revText1, String revText2) {
		List<String> listParagraph1 = new LinkedList<String>();
		List<String> listParagraph2 = new LinkedList<String>();
		Scanner scanner = new Scanner(revText1);
		while (scanner.hasNextLine()) {
			String paragraph = scanner.nextLine();
			listParagraph1.add(paragraph);
		}
		scanner.close();
		scanner = new Scanner(revText2);
		while (scanner.hasNextLine()) {
			String paragraph = scanner.nextLine();
			listParagraph2.add(paragraph);
		}
		String[] aux = {};
		int[][] matrix = computeLevenshteinDistance(listParagraph1.toArray(aux), listParagraph2.toArray(aux));
		LinkedList<String[]> comparables = reverseLevenshteindDistance(listParagraph1.toArray(aux), listParagraph2.toArray(aux), matrix);
		LinkedList<ParagraphDiff> listListDiff= new LinkedList<ParagraphDiff>();
		diff_match_patch differ = new diff_match_patch();	
		for (String[] strings : comparables) {
			LinkedList<Diff> diffs = differ.diff_main(strings[0], strings[1]);
			differ.diff_cleanupSemantic(diffs);
			LinkedList<Diff[]> resultParagraph = estructureComparision(diffs);
			listListDiff.add(new ParagraphDiff(resultParagraph));
		}
		scanner.close();
		
		return listListDiff;

	}
	private LinkedList<String[]> reverseLevenshteindDistance(String[] listParagraph1, String[] listParagraph2,int[][] matrix){
		int i =listParagraph1.length;
		int j =listParagraph2.length;
		LinkedList<String[]> list = new LinkedList<String[]>();
		while(i!=0 || j!=0){
			if (i==0) {
				String[] par={"",listParagraph2[j-1]};
				list.addFirst(par);
				j--;
				continue;
			}
			if (j==0) {
				String[] par={listParagraph1[i-1],""};
				list.addFirst(par);
				i--;
				continue;
			}
			String[] par={listParagraph1[i-1],listParagraph2[j-1]};
			list.addFirst(par);
			int min = minimum(matrix[i-1][j-1], matrix[i-1][j], matrix[i][j-1]);
			if (matrix[i-1][j-1]==min) {
				i--;
				j--;
			} else {
				if (matrix[i-1][j]==min) {
					par[1]="";
					i--;
				} else {
					par[0]="";
					j--;
					
				}
			}
			
		}
		return list;
	}
	
	private int minimum(int a, int b, int c) {
		if (a <= b && a <= c) {
			return a;
		}
		if (b <= a && b <= c) {
			return b;
		}
		return c;
	}

	private int[][] computeLevenshteinDistance(String[] listParagraph1, String[] listParagraph2) {
		int[][] distance = new int[listParagraph1.length + 1][listParagraph2.length + 1];
		diff_match_patch differ = new diff_match_patch();
		for (int i = 0; i <= listParagraph1.length; i++) {
			distance[i][0] = i;
		}
		for (int j = 0; j <= listParagraph2.length; j++) {
			distance[0][j] = j;
		}
		for (int i = 1; i <= listParagraph1.length; i++) {
			for (int j = 1; j <= listParagraph2.length; j++) {
				LinkedList<Diff> diffs = differ.diff_main(listParagraph1[i - 1], listParagraph2[j - 1]);
				int leven = differ.diff_levenshtein(diffs);
				distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1,
								distance[i - 1][j - 1] + ((listParagraph1[i - 1] == listParagraph2[j - 1]) ? 0 : leven));
			}
		}
//		for (int i = 0; i <= listParagraph1.length; i++) {
//			StringBuilder s = new StringBuilder();
//			s.append("[ ");
//			for (int j = 0; j <= listParagraph2.length; j++) {
//				s.append(distance[i][j]+", ");
//			}
//			s.append("]");
//			System.out.println(s.toString());
//		}
		
//		return distance[listParagraph1.length][listParagraph2.length];
		return distance;
	}

	private LinkedList<Diff[]> estructureComparision(LinkedList<Diff> diffs) {
		LinkedList<Diff> deletionsEqualities = new LinkedList<Diff>();
		LinkedList<Diff> insertionsEqualities = new LinkedList<Diff>();
		splitDiffs(diffs, deletionsEqualities, insertionsEqualities);
		Iterator<Diff> deletionIterator = deletionsEqualities.iterator();
		Iterator<Diff> insertionIterator = insertionsEqualities.iterator();
		LinkedList<Diff[]> result = new LinkedList<Diff[]>();
		if (deletionsEqualities.isEmpty() || insertionsEqualities.isEmpty()) {
			if (deletionsEqualities.isEmpty()) {
				for (Diff diff : insertionsEqualities) {
					Diff[] par = { null, diff };
					result.add(par);
				}
			} else {
				for (Diff diff : deletionsEqualities) {
					Diff[] par = { diff, null };
					result.add(par);
				}
			}
		} else {
			Boolean fin = false;
			Diff d = deletionIterator.next();
			Diff i = insertionIterator.next();

			while (!fin) {
				Boolean deletContinue =false;
				Boolean insertContinue =false;
				if (d.operation == i.operation) {
					Diff[] par = { d, i };
					result.add(par);
					if (deletionIterator.hasNext()) {
						//d = deletionIterator.next();
						deletContinue=true;
					}
					if (insertionIterator.hasNext()) {
						//i = insertionIterator.next();
						insertContinue=true;
					}
				} else {
					if ((d.operation == Operation.DELETE) && (i.operation == Operation.INSERT)) {

						if (d.text.replaceAll("\\s+", "").trim().length()==0) {
//							System.out.println("d null");
							d=null;
						} 
						if (i.text.replaceAll("\\s+", "").trim().length()==0) {
//							System.out.println("i null");
							i=null;
						} 
						Diff[] par = { d, i };
						result.add(par);
						if (deletionIterator.hasNext()) {
							//d = deletionIterator.next();
							deletContinue=true;
						}
						if (insertionIterator.hasNext()) {
							//i = insertionIterator.next();
							insertContinue=true;
						}
					} else {
						if (d.operation == Operation.EQUAL) {
							Diff[] par = { null, i };
							result.add(par);
							if (insertionIterator.hasNext()) {
								//i = insertionIterator.next();
								insertContinue=true;
							}
						} else {
							Diff[] par = { d, null };
							result.add(par);
							if (deletionIterator.hasNext()) {
								//d = deletionIterator.next();
								deletContinue=true;
							}
						}
					}
				}
				if (!deletionIterator.hasNext() && !insertionIterator.hasNext()) {
					fin = true;
				}
				if (deletContinue) {
					d = deletionIterator.next();
				}
				if (insertContinue) {
					i = insertionIterator.next();
				}
			}
			while (insertionIterator.hasNext()) {
				Diff diff = (Diff) insertionIterator.next();
				Diff[] par = { null, diff };
				result.add(par);
			}
			while (deletionIterator.hasNext()) {
				Diff diff = (Diff) deletionIterator.next();
				Diff[] par = { diff, null };
				result.add(par);
			}
		}
		return result;
	}

	private void splitDiffs(LinkedList<Diff> diffs, LinkedList<Diff> deletionsEqualities,
			LinkedList<Diff> insertionsEqualities) {
		for (Diff diff : diffs) {
			if (diff.operation == Operation.EQUAL) {
				deletionsEqualities.addLast(diff);
				insertionsEqualities.addLast(diff);
			} else {
				if (diff.operation == Operation.DELETE) {
					deletionsEqualities.addLast(diff);
				} else {
					insertionsEqualities.addLast(diff);
				}
			}
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	static String readFileLines(String path, Charset encoding) throws IOException {
		List<String> encoded = Files.readAllLines(Paths.get(path), encoding);
		StringBuilder result = new StringBuilder();
		for (String string : encoded) {
			result.append(string + "\n");
		}
		return result.toString();
	}


	
}
