package wikiAnalicis.converter.SQL;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;

public class NamespaceConverter implements Converter {

	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Namespace.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Namespace namespace = new Namespace();
		//namespace.setId(new Long(reader.getAttribute("key"))); //TODO no me lo acepta
		namespace.setKey(new Integer(reader.getAttribute("key")));
		namespace.setStringCase(reader.getAttribute("case"));
		namespace.setValue(reader.getValue());
		return namespace;
	}

}
