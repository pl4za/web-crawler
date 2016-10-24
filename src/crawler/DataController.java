package crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataController implements IDataControll {

	private static IDataControll instance = null;

	private Map<String, Map> siteMap = new ConcurrentHashMap<String, Map>();
	private Map<String, List<String>> assetMap = new ConcurrentHashMap<String, List<String>>();
	private List<String> visitedList = Collections.synchronizedList(new ArrayList<String>());
	private Set<String> queuedSet = Collections.synchronizedSet(new LinkedHashSet<String>());

	private int nVisitedLinks = 0;

	DataController() {
	}

	public static IDataControll getInstance() {
		if (instance == null) {
			instance = new DataController();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addVisitedLink(String url) {
		visitedList.add(url);
		nVisitedLinks++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAssets(String url, List<String> assets) {
		assetMap.put(url, assets);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isURLVisited(String url) {
		return visitedList.contains(url);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVisitedUrlNumber() {
		return nVisitedLinks;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, List<String>> getAssetList() {
		return assetMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isQueueEmpty() {
		return queuedSet.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void addURLToQueue(String url) {
		queuedSet.add(url);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized String getURLfromQueue() {
		Iterator<String> i = queuedSet.iterator();
		if (i.hasNext()) {
			String url = i.next();
			i.remove();
			return url;
		}
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertURLInSiteMap(String parent, String url) {
		if (parent == null || parent.isEmpty()) {
			siteMap.put(url, new ConcurrentHashMap<String, Map>());
		} else {
			Map<String, Map> sublist = getSubList(parent, siteMap);
			if (sublist != null) {
				sublist.put(url, new ConcurrentHashMap<String, Map>());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Map> getSiteMap() {
		return siteMap;
	}
	
	private Map<String, Map> getSubList(String parent, Map<String, Map> sublist) {
		if (sublist.containsKey(parent)) {
			return sublist.get(parent);
		}
		Iterator<Entry<String, Map>> it = sublist.entrySet().iterator();
		if (it.hasNext()) {
			return getSubList(parent, it.next().getValue());
		}
		return sublist;
	}

	
}
