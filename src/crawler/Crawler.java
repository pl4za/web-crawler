package crawler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

public class Crawler implements ICrawler {

	private static IThreadControll tc;
	private static IDataControll dc;

	Crawler() {
		tc = ThreadController.getInstance();
		dc = DataController.getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void queueBaseURL(String url) {
		load(null, url);
		tc.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void queueURL(String parent, String url) {
		load(parent, url);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addAssets(String url, List<String> assets) {
		dc.addAssets(url, assets);
	}

	private void load(String parent, String url) {
		if (url != null && !url.isEmpty() && (dc.getVisitedUrlNumber() < Options.MAX_PAGES_TO_LOAD)) {
			try {
				url = url.split("#")[0];
				URI uri = new URI(url);
				String encodedURL = uri.toASCIIString();
				if (checkURL(encodedURL)) {
					char ch = encodedURL.charAt(encodedURL.length() - 1);
					if (ch == '/') {
						encodedURL = encodedURL.substring(0, encodedURL.length() - 1);
					}
					if (!dc.isURLVisited(encodedURL)) {
						dc.insertURLInSiteMap(parent, encodedURL);
						dc.addVisitedLink(encodedURL);
						tc.queue(encodedURL);
					}
				} else {
					System.out.println("Invalid URL");
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkURL(String url) {
		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (urlValidator.isValid(url)) {
			return true;
		} else {
			return false;
		}
	}

}
