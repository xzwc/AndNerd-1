/**
 * 
 */
package org.zhydevelop.andnerd.bean;

/**
 * @author ChiChou
 *
 */
/**
 * @author ChiChou
 *
 */
public class Book {
	private String id, code, title, author, publisher, category;
	private int available, total;
	private int year;
	private String lendable;
	

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
	 * @param id the id to set
	 */
	public Book setId(String id) {
		this.id = id;
		return this;
	}
	/**
	 * @param code the code to set
	 */
	public Book setCode(String code) {
		this.code = code;
		return this;
	}
	/**
	 * @param title the title to set
	 */
	public Book setTitle(String title) {
		this.title = title;
		return this;
	}
	/**
	 * @param author the author to set
	 */
	public Book setAuthor(String author) {
		this.author = author;
		return this;
	}
	/**
	 * @param publisher the publisher to set
	 */
	public Book setPublisher(String publisher) {
		this.publisher = publisher;
		return this;
	}
	/**
	 * @param category the category to set
	 */
	public Book setCategory(String category) {
		this.category = category;
		return this;
	}
	/**
	 * @param avaliable the available to set
	 */
	public Book setAvailable(int available) {
		this.available = available;
		updateLendable();
		return this;
	}
	/**
	 * @param total the total to set
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
}
