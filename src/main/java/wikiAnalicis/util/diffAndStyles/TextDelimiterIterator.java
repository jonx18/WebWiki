package wikiAnalicis.util.diffAndStyles;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import wikiAnalicis.entity.diffAndStyles.Delimiter;

public class TextDelimiterIterator {
	StringBuilder text=new StringBuilder();
	List<Delimiter> delimiters;
	Map<Integer,String> map = new TreeMap<Integer, String>();
	private Iterator<Integer> iterator;

	
	

	public TextDelimiterIterator(List<Delimiter> delimiters) {
		super();
		this.delimiters = delimiters;
	}
	public void setText(String text) {
		this.text = new StringBuilder(text);
		mapInitializer();
	}
	private void mapInitializer() {
		this.map = new TreeMap<Integer,String>();
		for (Delimiter delimiter : delimiters) {
			//System.out.println("buscando: "+delimiter.openIndicator);
			int o = StringUtils.countMatches(this.text, delimiter.openIndicator);
			int c = StringUtils.countMatches(this.text, delimiter.closeIndicator);
			//int index=0;//puede nofuncionar probar
			for (int i = 0; i < o; i++) {
//				index = StringUtils.indexOf(this.text, delimiter.openIndicator,index);
//				map.put(index, delimiter.openIndicator);
//				index+=delimiter.openIndicator.length();
				int index = StringUtils.indexOf(this.text, delimiter.openIndicator);
				map.put(index, delimiter.openIndicator);
				//System.out.println("index: "+index+" delimiter: "+delimiter.openIndicator);
				//System.out.println("beforeText: "+this.text);
				this.text.replace(index, index+delimiter.openIndicator.length(),StringUtils.repeat(" ",delimiter.openIndicator.length() ));
				//System.out.println("afterText: "+this.text);
			}
			if (!delimiter.openIndicator.equalsIgnoreCase(delimiter.closeIndicator)) {
				//index=0;//puede nofuncionar probar
				for (int i = 0; i < c; i++) {
//					index = StringUtils.indexOf(this.text, delimiter.closeIndicator,index);
//					map.put(index, delimiter.closeIndicator);
//					index+=delimiter.closeIndicator.length();
					int index = StringUtils.indexOf(this.text, delimiter.closeIndicator);
					map.put(index, delimiter.closeIndicator);
					//System.out.println("index: "+index+" delimiter: "+delimiter.closeIndicator);
					//System.out.println("beforeText: "+this.text);
					this.text.replace(index, index+delimiter.closeIndicator.length(),StringUtils.repeat(" ",delimiter.closeIndicator.length() ));
					//System.out.println("afterText: "+this.text);
				}
			}

		}
		for (Integer delimiter : map.keySet()) {
			System.out.println(delimiter+" "+map.get(delimiter));
		}
		iterator = map.keySet().iterator();
	}

	public boolean hasNext() {			
		return iterator.hasNext();
	}


	public String next() {
		return map.get(iterator.next());
	}
	public Delimiter getDelimiterOpenBy(String openIndicator) {
		for (Delimiter delimiter : delimiters) {
			if (delimiter.openIndicator.equalsIgnoreCase(openIndicator)) {
				return delimiter;
			}
		}
		return null;
	}
	public Delimiter getDelimiterCloseBy(String closeIndicator) {
		for (Delimiter delimiter : delimiters) {
			if (delimiter.closeIndicator.equalsIgnoreCase(closeIndicator)) {
				return delimiter;
			}
		}
		return null;
	}
}
