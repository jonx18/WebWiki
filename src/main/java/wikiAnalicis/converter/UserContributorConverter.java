package wikiAnalicis.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.UserContributor;
import wikiAnalicis.service.UserContributorService;

public class UserContributorConverter implements Converter {
	private UserContributorService userContributorService;
	public UserContributorConverter(UserContributorService userContributorService) {
		this.userContributorService=userContributorService;
	}

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
		if (reader.getAttribute("deleted")==null) {
			reader.moveDown();
			if ("username".equalsIgnoreCase(reader.getNodeName())) {
				userContributor.setUsername(reader.getValue());
				UserContributor userContributorTemp = userContributorService.getUserContributor(userContributor.getUsername());
				if (userContributorTemp == null) {
					userContributor.setId(null);
					userContributor.setIp("0.0.0.0");
				} else {
					userContributor=userContributorTemp;
				}
				reader.moveUp();
				reader.moveDown();
			}
			if ("id".equalsIgnoreCase(reader.getNodeName())) {
				userContributor.setRealId(new Long(reader.getValue()));
				reader.moveUp();
	
			}
			if ("ip".equalsIgnoreCase(reader.getNodeName())) {
				userContributor.setIp(reader.getValue());
				userContributor.setAnonimus();
				userContributor.setUsername("anonimo"+userContributor.getRealId());
				reader.moveUp();
			}
		} else {
			userContributor.setDeleted(true);
			userContributor.setIp("0.0.0.0");
			userContributor.setAnonimus();
			userContributor.setUsername("deleted"+userContributor.getRealId());
		}
		userContributorService.mergeUserContributor(userContributor);//para que los encuentre 1 a 1
		userContributor = userContributorService.getUserContributor(userContributor.getUsername());
		return userContributor;
	}

}
