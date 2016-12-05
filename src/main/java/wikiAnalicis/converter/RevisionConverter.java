package wikiAnalicis.converter;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.controller.DumpToBDController;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.entity.UserContributor;

public class RevisionConverter implements Converter {
	DumpToBDController dumpToBDController;
	public RevisionConverter(DumpToBDController dumpToBDController) {
		this.dumpToBDController = dumpToBDController;
	}

	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Revision.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Revision revision = new Revision();
		reader.moveDown();
		if ("id".equalsIgnoreCase(reader.getNodeName())) {
			revision.setId(new Long(reader.getValue()));
			reader.moveUp();
			reader.moveDown();
		}

		if ("parentid".equalsIgnoreCase(reader.getNodeName())) {
			revision.setParentid(new Long(reader.getValue()));
			reader.moveUp();
			reader.moveDown();
		}
		if ("timestamp".equalsIgnoreCase(reader.getNodeName())) {
			revision.setTimestamp(reader.getValue());
			reader.moveUp();
			reader.moveDown();
		}

		if ("contributor".equalsIgnoreCase(reader.getNodeName())) {
			UserContributor contributor =(UserContributor) context.convertAnother(revision, UserContributor.class);
			revision.setContributor(contributor);
			reader.moveUp();
			reader.moveDown();
		}

		if ("minor".equalsIgnoreCase(reader.getNodeName())) {
			revision.setMinor(true);
			reader.moveUp();
			reader.moveDown();
		}
		if ("comment".equalsIgnoreCase(reader.getNodeName())) {
			revision.setComment(reader.getValue());
			reader.moveUp();
			reader.moveDown();
		}

		if ("model".equalsIgnoreCase(reader.getNodeName())) {
			revision.setModel(reader.getValue());
			reader.moveUp();
			reader.moveDown();
		}

		if ("format".equalsIgnoreCase(reader.getNodeName())) {
			revision.setFormat(reader.getValue());
			reader.moveUp();
			reader.moveDown();
		}

		if ("text".equalsIgnoreCase(reader.getNodeName())) {
			if (reader.getAttribute("deleted")!=null) {
				revision.setDeleted(true);
			}
			String text = null;
			try {
				text = reader.getValue();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(revision.getStringTimestamp());
				System.out.println(revision.getId());
			}

			reader.moveUp();
			reader.moveDown();
			text = removeAccents(text.toString());
			revision.setText(text);
			if (revision.getText()!=null) {
				List<String> categoriesNames = dumpToBDController.categoriesNamesFromText(revision.getText());
				String[] names = new String[categoriesNames.size()];
				names= categoriesNames.toArray(names);
				revision.setCategoryNames(names);
			}


		}

		if ("sha1".equalsIgnoreCase(reader.getNodeName())) {
			revision.setSha1(reader.getValue());
			reader.moveUp();

		}


		return revision;
	}
	public static String removeAccents(String text) {
	    return text == null ? null :
	        Normalizer.normalize(text, Form.NFD)
	            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

}
