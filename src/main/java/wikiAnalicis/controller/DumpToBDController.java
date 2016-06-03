package wikiAnalicis.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import wikiAnalicis.entity.Mediawiki;
import wikiAnalicis.entity.Namespace;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.Revision;
import wikiAnalicis.entity.Siteinfo;
import wikiAnalicis.entity.diff_match_patch;
import wikiAnalicis.entity.diff_match_patch.Diff;
import wikiAnalicis.service.RevisionService;
@Controller
public class DumpToBDController {
	private static final Logger LOGGER = Logger.getLogger(Revision.class);
	@Autowired
	private RevisionService revisionService;
	@RequestMapping("dumptobd")
	public ModelAndView dumpToBD() {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("revision", Revision.class);
		xStream.alias("page", Page.class);
		xStream.addImplicitCollection(Page.class, "revisions");
		xStream.alias("siteinfo", Siteinfo.class);
		xStream.aliasField("case", Siteinfo.class, "casee");
		xStream.alias("namespace", Namespace.class);
		xStream.useAttributeFor(Namespace.class, "key");
		xStream.aliasField("case", Namespace.class, "stringCase");
		xStream.alias("mediawiki", Mediawiki.class);
		xStream.addImplicitCollection(Mediawiki.class, "pages");
		Page page=null;
		try {
			page = (Page)xStream.fromXML(new FileInputStream("C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\pagesv2\\page18.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(page.toString());
	
		Siteinfo siteinfo=null;
		try {
			siteinfo = (Siteinfo)xStream.fromXML(new FileInputStream("C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\Wiki de prueba\\siteinfo.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(siteinfo.toString());
		
		Mediawiki mediawiki=null;
		try {
			mediawiki = (Mediawiki)xStream.fromXML(new FileInputStream("C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\Wiki de prueba\\eswikiversity-20160501-pages-meta-history.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mediawiki.toString());
		mediawiki=null;
		
		try {
			mediawiki = (Mediawiki)xStream.fromXML(new FileInputStream("C:\\Users\\Jonx\\Downloads\\WikiAnalicis\\eswikiversity-20160501\\xml\\eswikiversity-20160501-pages-meta-history.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mediawiki.toString());
		LinkedList<Diff> diffs = new LinkedList<Diff>();
		return new ModelAndView("diffList", "diffList", diffs);

	}
}
