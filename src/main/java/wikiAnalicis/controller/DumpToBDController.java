package wikiAnalicis.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Properties;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import wikiAnalicis.converter.MediaWikiConverter;
import wikiAnalicis.converter.PageConverter;
import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.service.MediawikiService;
import wikiAnalicis.service.PageService;
import wikiAnalicis.service.RevisionService;
import wikiAnalicis.util.diff_match_patch;
import wikiAnalicis.util.diff_match_patch.Diff;
@Controller
@PropertySource({ "classpath:historyPath.properties" })
public class DumpToBDController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private MediawikiService mediawikiService;
	@Autowired
	private PageService pageService;
	@Autowired
	private Environment env;
	
	@RequestMapping("dumptobd")
	public ModelAndView dumpToBD() {

		System.out.println("Cargando:");
		System.out.println(env.getProperty("history.path.test"));
		
		XStream xStream = configXStream();
		String historyPath = env.getProperty("history.path.test");
		historyXMLToDB(xStream, historyPath);
		System.out.println("Finalizo guardado");
		//aca van masprocesamintos
		LinkedList<Diff> diffs = new LinkedList<Diff>();
		return new ModelAndView("diffList", "diffList", diffs);

	}
	private void historyXMLToDB(XStream xStream, String historyPath) {
		Mediawiki mediawiki=null;
		try {
			mediawiki = (Mediawiki)xStream.fromXML(new FileInputStream(historyPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediawikiService.mergeMediawiki(mediawiki);
	}
	private XStream configXStream() {
		//configuro xstream
		XStream xStream = new XStream(new StaxDriver());
		xStream.alias("revision", Revision.class);
		xStream.alias("page", Page.class);
		xStream.addImplicitCollection(Page.class, "revisions");
		xStream.alias("siteinfo", Siteinfo.class);
		xStream.aliasField("case", Siteinfo.class, "casee");
		xStream.alias("namespace", Namespace.class);
		xStream.aliasAttribute( Namespace.class, "keyclave","key");
		xStream.aliasAttribute( Namespace.class, "stringCase","case");
		xStream.alias("mediawiki", Mediawiki.class);
		xStream.addImplicitCollection(Mediawiki.class, "pages");
		
		//converters
		xStream.registerConverter(new MediaWikiConverter(mediawikiService));
		xStream.registerConverter(new PageConverter(pageService));
		return xStream;
	}
}
