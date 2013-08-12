package com.balsampearshz.wowyi.bean;

import java.io.Serializable;

public class Article implements Serializable {

	/**
	 * serialized id 
	 */
	private static final long serialVersionUID = 1747747284085855560L;
	
	private String a_id;
	
	private String a_author;
	
	private String a_from;
	
	private String a_title;
	
	private String a_desc;
	
	private String a_content;
	
	private String a_seq;
	
	private String a_is_top;
	
	private String a_original_url;
	
	private String a_main_pic;
	
	private String ar_name;
	
	private String ar_url;

	public String getA_author() {
		return a_author;
	}

	public void setA_author(String a_author) {
		this.a_author = a_author;
	}

	public String getA_from() {
		return a_from;
	}

	public void setA_from(String a_from) {
		this.a_from = a_from;
	}

	public String getA_title() {
		return a_title;
	}

	public void setA_title(String a_title) {
		this.a_title = a_title;
	}

	public String getA_desc() {
		return a_desc;
	}

	public void setA_desc(String a_desc) {
		this.a_desc = a_desc;
	}

	public String getA_content() {
		return a_content;
	}

	public void setA_content(String a_content) {
		this.a_content = a_content;
	}

	public String getA_seq() {
		return a_seq;
	}

	public void setA_seq(String a_seq) {
		this.a_seq = a_seq;
	}

	public String getA_is_top() {
		return a_is_top;
	}

	public void setA_is_top(String a_is_top) {
		this.a_is_top = a_is_top;
	}

	public String getA_original_url() {
		return a_original_url;
	}

	public void setA_original_url(String a_original_url) {
		this.a_original_url = a_original_url;
	}

	public String getA_main_pic() {
		return a_main_pic;
	}

	public void setA_main_pic(String a_main_pic) {
		this.a_main_pic = a_main_pic;
	}

	public String getAr_name() {
		return ar_name;
	}
	public String getA_id() {
		return a_id;
	}

	public void setA_id(String a_id) {
		this.a_id = a_id;
	}

	public void setAr_name(String ar_name) {
		this.ar_name = ar_name;
	}

	public String getAr_url() {
		return ar_url;
	}

	public void setAr_url(String ar_url) {
		this.ar_url = ar_url;
	}
	
	
}
