package wikiAnalicis.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Page;

public class MediaWikiConverter implements Converter {

	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(Mediawiki.class);
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
		// TODO Auto-generated method stub
		//Cargo datos basicos y lista vacia
		//Guardo
		//null
		//Mientras haya revisiones
			//Recupero el que null
			//cargo revicion
			//agrego a la lista
			//actualizo
			//null denuevo
		//fin
		//retorno con basicos
		return null;
	}

}
