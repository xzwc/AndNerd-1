/**
 * 
 */
package org.zhydevelop.andnerd.bean;

/**
 * @author ChiChou
 *
 */
public class Category {
	private String code, title;
	private int children;
	/*
	 * Constructor
	 */
	public Category(String code, String title, int children) {
		this.code = code;
		this.title = title; 
		this.children = children;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the children
	 */
	public int getChildren() {
		return children;
	}
	/**
	 * @param code the code to set
	 */
	public Category setCode(String code) {
		this.code = code;
		return this;
	}

	/**
	 * @param title the title to set
	 */
	public Category setTitle(String title) {
		this.title = title;
		return this;
	}
	
	/**
	 * @param children the children to set
	 */	
	public Category setChildren(int children) {
		this.children = children;
		return this;
	}
}
