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

/**
 * @author ChiChou
 *
 */
public class HuiwenParser {
	private String content;
	
	//TODO:version
	public HuiwenParser(String content) {
		this.content = content;
	}
	
	public int getCount() {
		String TAG_START = "<strong class=\"red\">", TAG_END = "</strong>";
		int start = content.indexOf(TAG_START) + TAG_START.length();
		int end = content.indexOf(TAG_END, start);
		return Integer.valueOf(content.substring(start, end));
	}
	
	public List<Book> parseBooks() {
		List<Book> books = new ArrayList<Book>();
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
	    	books.add(book);
		}
		
		return books;
	}
}