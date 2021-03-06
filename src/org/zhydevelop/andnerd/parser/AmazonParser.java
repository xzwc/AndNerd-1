package org.zhydevelop.andnerd.parser;

import org.zhydevelop.andnerd.bean.AmazonItem;

/**
 * @author ChiChou
 *
 */
public class AmazonParser {
	/**
	 * @param html HTML源文件
	 * @return 亚马逊商品信息对象
	 */
	public static AmazonItem parse(String html) {
		String ISBN_BEGIN = "<title>", ISBN_END = " - 所有类别",
			ANCHOR_BEGIN = "href=\"", ANCHOR_END = "\"",
			IMAGE_BEGIN = "src=\"", IMAGE_END = ANCHOR_END;
		
		int pos = html.indexOf("pgResults"), a, b;
		String url, image, ISBN;

		//无效内容
		if(html.indexOf("非常抱歉") > -1 || pos == -1)
			return null;

		//解析ISBN
		a = html.indexOf(ISBN_BEGIN) + ISBN_BEGIN.length();
		b = html.indexOf(ISBN_END, a);
		ISBN = html.substring(a, b);
		
		//解析链接
		a = html.indexOf(ANCHOR_BEGIN, pos) + ANCHOR_BEGIN.length();
		b = html.indexOf(ANCHOR_END, a);
		url = html.substring(a, b);
		
		//解析图片链接
		a = html.indexOf(IMAGE_BEGIN, b) + IMAGE_BEGIN.length();
		b = html.indexOf(IMAGE_END, a);
		image = html.substring(a, b);
		
		return new AmazonItem(url, image, ISBN);
	} 
}
