package crawler;

public interface IThreadControll {
	
	/**
	 * Queues an url
	 *
	 * @param  url  an url to queue 
	 */
	void queue(String url);

	/**
	 * Called after each thread finishes execution with a code identifying the result.
	 *
	 * @param  url  an absolute URL giving the website url.
	 * @param statusCode result of the thread execution.
	 */
	void threadFinished(int statusCode, String url);
	
	/**
	 * Called after each thread finishes execution with a code identifying the result.
	 *
	 * @param  url  an absolute URL giving the website url.
	 * @param statusCode result of the thread execution.
	 * @param errorCode http error code.
	 */
	void threadFinished(int statusCode, String url, int errorCode);
	
	/**
	 * Returns the status of the Crawling
	 *
	 * @return The status of the crawling, true if running, false if not.
	 */
	boolean isRunning();
	
	/**
	 * Used to pull urls from the queue and execute the crawl.
	 * 
	 */
	void start();

}
