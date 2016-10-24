package crawler;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileWritterXML implements IFileWrite {

	String filename;
	String separator;
	Map<String, List<String>> assetmap;
	Map<String, Map> sitemap;

	FileWritterXML(String filename, Map<String, Map> sitemap, Map<String, List<String>> assetmap) {
		this.filename = filename;
		this.sitemap = sitemap;
		this.assetmap = assetmap;
	}

	public void writeMapToFile() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			// Document root
			Element rootElement = doc.createElement("webcrawler");
			doc.appendChild(rootElement);
			iterateSiteMap(sitemap, doc, rootElement);
			// Save xml
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private void iterateSiteMap(Map<String, Map> sublist, Document doc, Element rootElement) {
		Iterator<Entry<String, Map>> it = sublist.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Map> entry = it.next();
			Element link = doc.createElement("url");
			link.setAttribute("value", entry.getKey());
			Element assets = doc.createElement("assets");
			if (Options.INCLUDE_ASSETS) {
				link.appendChild(assets);
				writeAssetList(entry.getKey(), assets, doc);
			}
			rootElement.appendChild(link);
			iterateSiteMap(entry.getValue(), doc, link);
		}
	}

	private void writeAssetList(String url, Element parent, Document doc) {
		List<String> assets = assetmap.get(url);
		if (assets!=null && !assets.isEmpty()) {
			for (String asset : assets) {
				String content[] = asset.split(Options.SEPARATOR);
				Element e = doc.createElement(content[0]);
				e.setAttribute("url", content[1]);
				parent.appendChild(e);
			}
		}
	}
}
