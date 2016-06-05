package wikiAnalicis.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;

public class UserContributorConverter implements Converter {

	@Override
	public boolean canConvert(Class arg0) {
		return arg0.equals(UserContributor.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		UserContributor userContributor = new UserContributor();
		reader.moveDown();
		if ("username".equalsIgnoreCase(reader.getNodeName())) {
			userContributor.setUsername(reader.getValue());
			userContributor.setIp("0.0.0.0");
			reader.moveUp();
			reader.moveDown();
		}
		if ("id".equalsIgnoreCase(reader.getNodeName())) {
			userContributor.setId(new Long(reader.getValue()));
			reader.moveUp();
		}
		if ("ip".equalsIgnoreCase(reader.getNodeName())) {
			userContributor.setIp(reader.getValue());
			userContributor.setUsername("anonimo");
			userContributor.setAnonimus();
			reader.moveUp();
		}
		return userContributor;
	}

}
