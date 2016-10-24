package crawler;

import java.util.List;

public interface ICrawler {
	
	/**
	 * Queues the base url for the website to start the crawl.
	 *
	 * @param baseURL an absolute URL giving the website url.
	 */
	void queueBaseURL(String baseURL);
	
	/**
	 * Queues an url and associates it with a parent url to create a hierarchy.
	 *
	 * @param  child  an absolute URL to queue.
	 * @param parent an absolute URL  parent to the child.
	 */	
	void queueURL(String parent, String child);

	/**
	 * Add asset list to the url.
	 *
	 * @param  url an absolute URL to queue
	 * @param assets a List of strings to associate with the url.
	 */
	void addAssets(String url, List<String> assets);
}