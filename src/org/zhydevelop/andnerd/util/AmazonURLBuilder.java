/**
 * 
 */
package org.zhydevelop.andnerd.util;

/**
 * @author ChiChou
 *
 */
public class AmazonURLBuilder {
	//http://www.amazon.cn/gp/aw/s/ref=is_box_?k=ISBN 
	private static String BASE = "http://www.amazon.cn",
			SEARCH = BASE + "/gp/aw/s/?k=";
	
	public static String search(String keyword) {
		return new StringBuilder(SEARCH).append(keyword).toString();
	}
	
	public static String fullUrl(String path) {
		return new StringBuilder(BASE).append(path).toString();
	}
}
