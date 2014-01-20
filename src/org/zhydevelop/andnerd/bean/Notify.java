/**
 * 
 */
package org.zhydevelop.andnerd.bean;

/**
 * @author ChiChou
 * 
 */
public class Notify extends FeedItem {
	//TODO:单独分离 馆藏地、日期等字段
	private String author, code, bar, lend, deadline, location;

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the bar
	 */
	public String getBar() {
		return bar;
	}

	/**
	 * @return the lend
	 */
	public String getLend() {
		return lend;
	}

	/**
	 * @return the deadline
	 */
	public String getDeadline() {
		return deadline;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param author the author to set
	 */
	public Notify setAuthor(String author) {
		this.author = author;
		return this;
	}

	/**
	 * @param code the code to set
	 */
	public Notify setCode(String code) {
		this.code = code;
		return this;
	}

	/**
	 * @param bar the bar to set
	 */
	public Notify setBar(String bar) {
		this.bar = bar;
		return this;
	}

	/**
	 * @param lend the lend to set
	 */
	public Notify setLend(String lend) {
		this.lend = lend;
		return this;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public Notify setDeadline(String deadline) {
		this.deadline = deadline;
		return this;
	}

	/**
	 * @param location the location to set
	 */
	public Notify setLocation(String location) {
		this.location = location;
		return this;
	}
	
}
