package org.zhydevelop.andnerd.util;

import android.net.Uri;

/**
 * @author ChiChou
 *
 */
public class HuiwenURLBuilder {
	private final static int PER_PAGE = 20;
	private final static int BACKTRACK_DAYS = 3;

	/*
	 * TODO: 使用动态类，兼容5.0
	 * */
	private final static String URL_BASE = "http://huiwen.ujs.edu.cn:8080/",
			URL_OPAC = URL_BASE + "opac/",
			URL_DATA = URL_BASE + "data/",
			URL_READER = URL_BASE + "reader/";

	/**
	 * @param keyword
	 * @param page
	 * @param limit
	 * @return
	 */
	public static String search(String keyword, int page, int limit) {
		StringBuilder sb = new StringBuilder(URL_OPAC);		
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
		StringBuilder sb = new StringBuilder(URL_OPAC);		
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

	/**
	 * @return 本月热门关键词
	 */
	public static String top100() {
		return new StringBuilder(URL_OPAC).append("top100.php").toString();
	}

	/**
	 * @return 当天热门关键词
	 */
	public static String top10() {
		return new StringBuilder(URL_OPAC).append("ajax_topten.php").toString();
	}

	/**
	 * @return 单本图书页面
	 */
	public static String item(String id) {
		return new StringBuilder(URL_OPAC).append("item.php?marc_no=").append(id).toString();
	}
	
	/**
	 * @return 借阅了这本书的人还借了
	 */
	public static String related(String id) {
		return new StringBuilder(URL_DATA).append("data_visual_book.php?id=")
				.append(id).toString();
	}

	/**
	 * @return 登录接口
	 */
	public static String login() {
		return new StringBuilder(URL_READER).append("redr_verify.php").toString();
	}

	/**
	 * @param id
	 * @param rank
	 * @return 执行评分URL
	 */
	public static String score(String id, int rank) {
		if(rank < 0 || rank > 5) rank = 0;
		return new StringBuilder(URL_OPAC).append("ajax_score_it.php?marc_no=")
				.append("&rank=").append(rank).toString();
	}
	//TODO:登录后的功能
}
