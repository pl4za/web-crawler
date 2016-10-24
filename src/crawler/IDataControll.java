package crawler;

import java.util.List;
import java.util.Map;

public interface IDataControll {

	/**
	 * Checks if the specified url has been previously crawled.
	 *
	 * @param  url to check
	 * @return true if the url has been crawled, false if not.
	 */
	boolean isURLVisited(String url);
	
	/**
	 * Add url to visited list. This ensures that a given url is only crawled once.
	 *
	 * @param  url  an absolute URL
	 */	
	void addVisitedLink(String url);

	/**
	 * Add asset list to the url.
	 *
	 * @param  url an absolute URL to queue
	 * @param assets a List of strings to associate with the url.
	 */
	void addAssets(String url, List<String> assets);

	/**
	 * Check if the queue to crawl is empty.
	 *
	 * @return  true if the queue to crawl is empty, false if not.
	 */	
	boolean isQueueEmpty();

	/**
	 * Add url to the crawl queue.
	 *
	 * @param  url  an absolute URL
	 */	
	void addURLToQueue(String url);

	/**
	 * Get the oldest url from the queue and remove it.
	 *
	 * @return  url
	 */	
	String getURLfromQueue();
	
	/**
	 * Returns the number of visited links visited so far by the crawl.
	 * 
	 * @return The number of links visited
	 */	
	int getVisitedUrlNumber();

	/**
	 * Inserts a given url in the sitemap, associating it to the corresponding father.
	 * 
	 * @param  url  an absolute URL
	 * @paramn parent an absolute URL
	 */	
	void insertURLInSiteMap(String parent, String url);

	/**
	 * Returns the sitemap with the parent-child relation.
	 *
	 * @return  A map with the sitemap.
	 */	
	Map<String, Map> getSiteMap();

	/**
	 * Returns the asset list containing each url as the map key, and a list of assets for each one.
	 *
	 * @return Map with unique urls as keys and a list of assets for each one.
	 */	
	Map<String, List<String>> getAssetList();

}
