/**
 * 
 */
package org.zhydevelop.andnerd.bean;

/**
 * @author ChiChou
 *
 */
public class AmazonItem {
	private String url, image, ISBN;

	public AmazonItem(String url, String image, String iSBN) {
		this.url = url;
		this.image = image;
		ISBN = iSBN;
	}
	
	/**
	 * @return the id
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the picture
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the iSBN
	 */
	public String getISBN() {
		return ISBN;
	}

	/**
	 * @param id the id to set
	 */
	public AmazonItem setId(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @param picture the picture to set
	 */
	public AmazonItem setImage(String image) {
		this.image = image;
		return this;
	}

	/**
	 * @param iSBN the iSBN to set
	 */
	public AmazonItem setISBN(String ISBN) {
		this.ISBN = ISBN;
		return this;
	}
	
}
