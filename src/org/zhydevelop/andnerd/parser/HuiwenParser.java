/**
 * 
 */
package org.zhydevelop.andnerd.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zhydevelop.andnerd.bean.Book;

import android.text.Html;

/**
 * @author ChiChou
 */
public class HuiwenParser {
	private String content;

	//TODO:version
	public HuiwenParser(String content) {
		this.content = content;
	}

	/**
	 * @return 结果总数
	 */
	public int getCount() {
		String TAG_START = "<strong class=\"red\">", TAG_END = "</strong>";
		int start = content.indexOf(TAG_START) + TAG_START.length();
		int end = content.indexOf(TAG_END, start);

		try {
			return Integer.valueOf(content.substring(start, end));
		} catch (NumberFormatException e) {
			return 0;
		}		
	}

	/**
	 * @return 书籍列表
	 */
	public List<Book> parseBooks() {
		ArrayList<Book> books = new ArrayList<Book>();
		Document doc = Jsoup.parse(content);

		Elements elements = doc.select(".list_books");

		for(Element element : elements) {
			Book book = new Book();
			//标题
			Element elemAnchor = element.select("a").first();
			book.setTitle(elemAnchor.text());
			//MARC-NO
			String url = elemAnchor.attr("href");
			String parts[] = url.split("\\?marc_no=");			    	
			book.setId(parts.length == 2 ? parts[1] : "");

			//馆藏、可借			    	
			Matcher matcher = Pattern.compile("\\d+").matcher(element.select("span").html());
			if(matcher.find()) {
				book.setTotal(Integer.valueOf(matcher.group()));
				book.setAvailable(matcher.find() ? Integer.valueOf(matcher.group()) : 0);
			} else {
				book.setTotal(0).setAvailable(0);
			}
			//索书号
			parts = element.select("h3").html().split("</a>\\s*");
			book.setCode(parts.length > 1 ? parts[1] : "");
			//作者和出版社
			parts = element.select("p").html().split("<(/span|br\\s*/*)>\\s*|&nbsp;");
			if(parts.length == 5) {
				book.setAuthor(parts[2]).setPublisher(parts[3])
				.setYear(Integer.valueOf(parts[4].replaceAll("[^0-9]+", "")));
			}
			//类别
			book.setCategory(element.select(".doc_type_class").html());
			books.add(book);	    	
		}
		books.trimToSize();
		return books;
	}

	/**
	 * @param content HTML正文
	 * @return 解析的关键词列表
	 * @author ChiChou
	 */
	public List<String> parseKeywords() {
		//特征码
		String TAG_OPEN = "')\">", TAG_CLOSE = "</a>";
		//返回值
		ArrayList<String> keywords = new ArrayList<String>();
		//区间(a,b)
		int pos = content.indexOf(TAG_OPEN), a, b;
		//找不到特征字符
		if(pos == -1) return keywords;

		//逐个搜索关键词
		do {
			a = pos + TAG_OPEN.length();
			b = content.indexOf(TAG_CLOSE, a);
			keywords.add(Html.fromHtml(content.substring(a, b)).toString());
			pos = content.indexOf(TAG_OPEN, b);
		} while(pos != -1);

		keywords.trimToSize();
		return keywords;
	}


	/**
	 * @return
	 */
	public Book detail() {
		Book book = new Book();

		//解析ISBN
		//		String TAG_ISBN_OPEN = "http://book.douban.com/isbn/", TAG_ISBN_CLOSE = "/";
		//		int pos = content.indexOf(TAG_ISBN_OPEN), a, b;
		//		if(pos == -1) return null; //参数不正确		
		//		a = pos + TAG_ISBN_OPEN.length();
		//		b = content.indexOf(TAG_ISBN_CLOSE, a);
		//		book.setISBN(Html.fromHtml(content.substring(a, b)).toString());
		//		
		//

		return book;		
	}
}