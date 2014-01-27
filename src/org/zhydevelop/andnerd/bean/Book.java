/**
 * 
 */
package org.zhydevelop.andnerd.bean;

import java.io.Serializable;

/**
 * @author ChiChou
 *
 */
public class Book implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1976101528111980354L;
	private String id, code, title, author, publisher, category, isbn, summary;
	private int available, total; //可借和总数
	private int year; //出版年份
	private String lendable; //可借/总数
	private int score; //评分
	
	public int getYear(){
		return year;
	}

	public Book setYear(int year) {
		this.year = year;
		return this;
	}
	/**
	 * @return 编号
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return 索书号
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return 作者
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @return 出版社
	 */
	public String getPublisher() {
		return publisher;
	}
	/**
	 * @return 类别
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @return 可借复本
	 */
	public int getAvaliable() {
		return available;
	}

	/**
	 * @return 馆藏复本
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @return ISBN
	 */
	public String getISBN() {
		return isbn;
	}

	/**
	 * @return 摘要
	 */
	public String getSummary() {
		return summary;
	}	 

	/**
	 * @return 评分
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param id MARC编号
	 */
	public Book setId(String id) {
		this.id = id;
		return this;
	}
	/**
	 * @param code 索书号
	 */
	public Book setCode(String code) {
		this.code = code;
		return this;
	}
	/**
	 * @param title 标题
	 */
	public Book setTitle(String title) {
		this.title = title;
		return this;
	}
	/**
	 * @param author 作者
	 */
	public Book setAuthor(String author) {
		this.author = author;
		return this;
	}
	/**
	 * @param publisher 出版社
	 */
	public Book setPublisher(String publisher) {
		this.publisher = publisher;
		return this;
	}
	/**
	 * @param category 图书分类
	 */
	public Book setCategory(String category) {
		this.category = category;
		return this;
	}
	/**
	 * @param avaliable 可借数
	 */
	public Book setAvailable(int available) {
		this.available = available;
		updateLendable();
		return this;
	}
	/**
	 * @param total 馆藏总数
	 */
	public Book setTotal(int total) {
		this.total = total;
		updateLendable();
		return this;
	}
	
	/**
	 * 更新可借数
	 */
	private void updateLendable() {
		this.lendable = new StringBuilder(
				String.valueOf(this.available)
				).append("/").append(this.total).toString();
	}

	/**
	 * 返回可借数/总数
	 */
	public String getLendable() {
		return this.lendable;
	}
	


	/**
	 * @param ISBN 国际标准书号
	 */
	public Book setISBN(String ISBN) {
		isbn = ISBN == null ? ISBN : ISBN.replaceAll("[^0-9]", "");
		return this;
	}

	/**
	 * @param ISBN 国际标准书号
	 */
	public Book setISBN(long ISBN) {
		return setISBN(String.valueOf(ISBN));
	}

	/**
	 * @param summary 摘要
	 */
	public Book setSummary(String summary) {
		this.summary = summary;
		return this;
	}
	
	/**
	 * @param score 评分
	 */
	public Book setScore(int score) {
		this.score = score;
		return this;
	}
	
	/**
	 * @param score 评分
	 */
	public Book setScore(String score) {
		try {
			this.score = Integer.valueOf(score);
		} catch(NumberFormatException ex) {
			this.score = 0;
		}
		return this;
	}
}