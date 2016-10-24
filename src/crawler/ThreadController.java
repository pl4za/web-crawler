package crawler;

import java.util.HashMap;
import java.util.Map;

public class ThreadController implements IThreadControll {

	private static IThreadControll instance = null;

	private static IDataControll dc = DataController.getInstance();
	private Map<String, Integer> URLtimeouts = new HashMap<String, Integer>();

	private int runningThreads = 0;
	private boolean running = false;

	ThreadController() {
	}

	public static IThreadControll getInstance() {
		if (instance == null) {
			instance = new ThreadController();
		}
		return instance;
	}

	private synchronized void decreaseRunnerThreads() {
		runningThreads--;
	}

	private synchronized void increaseRunnerThreads() {
		runningThreads++;
	}

	private void run() {
		if (running) {
			String _url = dc.getURLfromQueue();
			if (!_url.isEmpty()) {
				Thread thread = new Thread(
						new PageParser(_url, URLtimeouts.getOrDefault(_url, Options.DEFAULT_TIMEOUT)));
				thread.start();
				increaseRunnerThreads();
			}
		}
	}

	/*
	 * Queue
	 */

	/**
	 * {@inheritDoc}
	 */
	public void queue(String url) {
		dc.addURLToQueue(url);
		if (runningThreads < Options.MAX_SIMUL_THREADS && !dc.isQueueEmpty()) {
			run();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void queue(String url, int timeout) {
		dc.addURLToQueue(url);
		URLtimeouts.put(url, timeout);
		if (runningThreads < Options.MAX_SIMUL_THREADS && !dc.isQueueEmpty()) {
			run();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void threadFinished(int signal, String url) {
		decreaseRunnerThreads();
		if (signal == Options.CODE_FAILED) {
			GUI.getInstance().sendMessage("ERROR on " + url + ".");
		} else if (signal == Options.CODE_TIMEOUT) {
			int timeout = URLtimeouts.getOrDefault(url, Options.DEFAULT_TIMEOUT) * 2;
			GUI.getInstance().sendMessage("MAX TIMEOUT of " + timeout + "ms exceeded on " + url
					+ ". Increasing timeout and adding to queue.");
			if (timeout < Options.MAX_TIMEOUT) {
				queue(url, timeout);
				URLtimeouts.put(url, timeout);
			} else {
				GUI.getInstance().sendMessage("MAX TIMEOUT of " + timeout + "ms exceeded on " + url + ". SKIPPING.");
			}
		} else if (signal == Options.CODE_SUCCESS) {
			if (runningThreads < Options.MAX_SIMUL_THREADS && !dc.isQueueEmpty()) {
				run();
			} else if (runningThreads == 0 && dc.isQueueEmpty()) {
				running = false;
				IFileWrite f = new FileWritterXML(Options.FILENAME, dc.getSiteMap(), dc.getAssetList());
				f.writeMapToFile();
				GUI.getInstance().sendMessage("DONE: " + Options.FILENAME + " generated");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void threadFinished(int signal, String url, int statusCode) {
		decreaseRunnerThreads();
		if (signal == Options.CODE_FAILED) {
			GUI.getInstance().sendMessage("ERROR " + statusCode + " on " + url + ".");
		} else if (signal == Options.CODE_TIMEOUT || signal == Options.CODE_RESET) {
			int timeout = URLtimeouts.getOrDefault(url, Options.DEFAULT_TIMEOUT) * 2;
			GUI.getInstance().sendMessage("MAX TIMEOUT of " + timeout + "ms exceeded on " + url
					+ ". Increasing timeout and adding to queue.");
			if (timeout < Options.MAX_TIMEOUT) {
				queue(url, timeout);
				URLtimeouts.put(url, timeout);
			} else {
				GUI.getInstance().sendMessage("MAX TIMEOUT of " + timeout + "ms exceeded on " + url + ". SKIPPING.");
			}
		} else if (signal == Options.CODE_SUCCESS) {
			if (runningThreads < Options.MAX_SIMUL_THREADS && !dc.isQueueEmpty()) {
				run();
			} else if (runningThreads == 0 && dc.isQueueEmpty()) {
				running = false;
				IFileWrite f = new FileWritterXML(Options.FILENAME, dc.getSiteMap(), dc.getAssetList());
				f.writeMapToFile();
				GUI.getInstance().sendMessage("DONE: " + Options.FILENAME + " generated");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/**
	 * {@inheritDoc}
	 */
	public void start() {
		running = true;
		run();
	}

}