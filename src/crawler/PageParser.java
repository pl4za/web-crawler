package crawler;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser implements Runnable {

	private static IThreadControll tc = ThreadController.getInstance();
	private String url;
	private int timeout = Options.DEFAULT_TIMEOUT;

	PageParser(String url, int timeout) {
		this.url = url;
		this.timeout = timeout;
	}

	PageParser(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			Document doc = con.timeout(timeout).get();
			ICrawler crawler = new Crawler();
			if (Options.INCLUDE_ASSETS) {
				List<String> assets = extractAssets(doc);
				crawler.addAssets(url, assets);
			}
			extractLinks(crawler, doc);
			tc.threadFinished(Options.CODE_SUCCESS, url);
		} catch (SocketTimeoutException e) {
			tc.threadFinished(Options.CODE_TIMEOUT, url);
		} catch (SocketException e) {
			tc.threadFinished(Options.CODE_RESET, url);
		} catch (HttpStatusException e) {
			tc.threadFinished(Options.CODE_FAILED, url, e.getStatusCode());
		} catch (UnsupportedMimeTypeException e) {
			e.printStackTrace();
			tc.threadFinished(Options.CODE_FAILED, url);
		} catch (IOException e) {
			e.printStackTrace();
			tc.threadFinished(Options.CODE_FAILED, url);
		}
	}

	private void extractLinks(ICrawler crawler, Document doc) {
		Elements elements = doc.select("a[href]");
		for (Element e : elements) {
			String innerURL = e.attr("abs:href");
			if (Options.SAME_DOMAIN) {
				if (getHost(innerURL).equals(getHost(Options.BASE_URL))) {
					crawler.queueURL(url, innerURL);
				}
			} else {
				crawler.queueURL(url, innerURL);
			}
		}
	}

	private List<String> extractAssets(Document doc) {
		Elements images = doc.select("img");
		Elements imports = doc.select("link[href]");
		ArrayList<String> assets = new ArrayList<String>();
		for (Element e : images) {
			String value = e.attr("abs:src");
			if (!value.isEmpty()) {
				assets.add(e.tagName() + Options.SEPARATOR + value);
			}
		}
		for (Element e : imports) {
			String value = e.attr("abs:href");
			if (!value.isEmpty()) {
				String lastChar = (value.substring(value.length() - 1));
				if (lastChar != null && !lastChar.equals("/"))
					assets.add(e.tagName() + Options.SEPARATOR + value);
			}
		}
		return assets;
	}

	private static String getHost(String url) {
		if (url == null || url.length() == 0) {
			return "";
		}
		int doubleslash = url.indexOf("//");
		if (doubleslash == -1) {
			doubleslash = 0;
		} else {
			doubleslash += 2;
		}
		int end = url.indexOf('/', doubleslash);
		end = end >= 0 ? end : url.length();
		int port = url.indexOf(':', doubleslash);
		end = (port > 0 && port < end) ? port : end;
		return url.substring(doubleslash, end);
	}

}
