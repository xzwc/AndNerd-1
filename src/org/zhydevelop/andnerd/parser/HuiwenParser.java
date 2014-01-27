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
	 * @param content 要操作的文本
	 * @param begin 起始TAG
	 * @param end 结束TAG
	 * @return begin和end之间的字符，如果未找到，返回空字符
	 */
	private String extract(String content, String begin, String end) {
		int a = content.indexOf(begin) + begin.length();
		int b = content.indexOf(end, a);		
		return a!= -1 && b != -1 ? content.substring(a, b) : "";
	}
	/**
	 * @return 结果总数
	 */
	public int getCount() {
		String count = extract(content, "<strong class=\"red\">", "</strong>");

		try {
			return Integer.valueOf(count);
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
	 * 解析单个项目页面
	 * TODO:支持评论，相关推荐
	 */
	public Book detail() {
		Book book = new Book();

		//使用DOM解析文档
		Document doc = Jsoup.parse(content);
		int assigned = 0;
		
		//评分
		String parts[] = doc.getElementById("score").html().split("star|\\.gif");
		if(parts.length == 3) book.setScore(parts[1]);

		//MARC_NO
		book.setId(doc.select("[name=marc_no]").val());
		
		//标题、出版社等
		Elements elems = doc.select(".booklist");
		String key, value;
		for(Element e : elems) {
			//是否解析完成
			if(assigned >= 5) break;
			
			key = e.select("dt").html();
			value = e.select("dd").text();
			
			if(key.startsWith("题名/责任者")) {
				//标题和作者
				parts = value.split("/");
				if(parts.length == 2) {
					book.setTitle(parts[0]);
					book.setAuthor(parts[1]);
					assigned += 2;
				}
			} else if(key.startsWith("出版发行项")) {
				//出版社
				book.setPublisher(value);
				assigned++;
			} else if(key.startsWith("ISBN及定价")) {
				//ISBN
				book.setISBN(value.split("/")[0]);
				assigned++;
			} else if(key.startsWith("提要文摘附注")) {
				//摘要
				book.setSummary(value);
				assigned++;
			}			
			//TODO:评论
		}
		
		//recycle
		elems = null;
		doc = null;
		
		//类别
		book.setCategory(extract(content, "<dt class=\"grey\">文献类型：", "</dt"));
		return book;	
	}
}