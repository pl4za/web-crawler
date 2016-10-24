package crawler;

/**
* This is a multi-threaded webcrawler.
* Options can be set in the Options class.
*
* @author  Jason Costa
* @version 1.0
* @since 2016-10-19 
*/

public class Main {

	public static void main(String[] args) {
		GUI.getInstance().sendMessage("<html>Starting crawl on: <br>"+Options.BASE_URL+"</html>");
		ICrawler crawler = new Crawler();
		crawler.queueBaseURL(Options.BASE_URL);
	}
	
}
