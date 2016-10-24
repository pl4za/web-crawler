package crawler;

public final class Options {
	/**
	 * Options for the crawler
	 */
	static final String BASE_URL = "https://gocardless.com/";
	static final int MAX_PAGES_TO_LOAD = 5000;
	static final int MAX_SIMUL_THREADS = 15;
	static final boolean INCLUDE_ASSETS = true;
	public static final String SEPARATOR = "--";
	public static final boolean SAME_DOMAIN = true;
	public static final String FILENAME = "treemap.xml";
	// CODES
	static final int CODE_FAILED = 0;
	static final int CODE_SUCCESS = 1;
	static final int CODE_TIMEOUT = 2;
	static final int CODE_MALFORMED_URL = 3;
	public static final int CODE_INTERRUPTED = 4;
	public static final int CODE_RESET = 5;
	// TIMEOUTS
	static final int DEFAULT_TIMEOUT = 2000;
	public static final int MAX_TIMEOUT = 8000;
}
