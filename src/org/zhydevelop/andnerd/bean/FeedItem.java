/**
 * 
 */
package org.zhydevelop.andnerd.bean;

/**
 * @author ChiChou
 *
 */
public class FeedItem {
	private String title, link,	description;

	public FeedItem() {
		title = link = description = "";
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param title the title to set
	 */
	public FeedItem setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * @param link the link to set
	 */
	public FeedItem setLink(String link) {
		this.link = link;
		return this;
	}

	/**
	 * @param description the description to set
	 */
	public FeedItem setDescription(String description) {
		this.description = description;
		return this;
	}
	
	
}
