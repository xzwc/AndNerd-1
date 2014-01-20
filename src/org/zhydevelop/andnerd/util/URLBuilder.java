package org.zhydevelop.andnerd.util;

import android.net.Uri;

public class URLBuilder {
	private final static int PER_PAGE = 20;
	private final static int BACKTRACK_DAYS = 3;
	private final static String URL_BASE = "http://huiwen.ujs.edu.cn:8080/opac/";
	
	/**
	 * @param keyword
	 * @param page
	 * @param limit
	 * @return
	 */
	public static String search(String keyword, int page, int limit) {
		StringBuilder sb = new StringBuilder(URL_BASE);		
		String query = String.format(
			"openlink.php?strSearchType=title&strText=%s&page=%s&displaypg=%s",
			 Uri.encode(keyword), page, limit);
		sb.append(query);		
		return sb.toString();
	}
	
	/**
	 * @param keyword
	 * @return
	 */
	public static String search(String keyword) {
		return search(keyword, 0, PER_PAGE);
	}
	
	/**
	 * @param category
	 * @param days
	 * @param page
	 * @return
	 */
	public static String fresh(String category, int days, int page) {
		StringBuilder sb = new StringBuilder(URL_BASE);		
		String query = String.format(
			"newbook_cls_book.php?back_days=%s&loca_code=ALL&cls=%s&s_doctype=ALL&&page=%s",
			days, category, page);
		sb.append(query);		
		return sb.toString();
	}
	
	/**
	 * @param catetory
	 * @return
	 */
	public static String fresh(String catetory) {
		return fresh(catetory, BACKTRACK_DAYS, 0);
	}
}
