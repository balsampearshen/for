package com.balsampearshz.wowyi.bean;

import java.io.Serializable;

public class ImageCategory implements Serializable {

	/**
	 * serialized id
	 */
	private static final long serialVersionUID = -3817747880144443240L;

	private String ic_id;
	
	private String ic_name;
	
	private String ic_desc;
	
	private String ic_cover;
	
	private String ic_status;
	
	private String ic_is_public;

	public String getIc_id() {
		return ic_id;
	}

	public void setIc_id(String ic_id) {
		this.ic_id = ic_id;
	}

	public String getIc_name() {
		return ic_name;
	}

	public void setIc_name(String ic_name) {
		this.ic_name = ic_name;
	}

	public String getIc_desc() {
		return ic_desc;
	}

	public void setIc_desc(String ic_desc) {
		this.ic_desc = ic_desc;
	}

	public String getIc_cover() {
		return ic_cover;
	}

	public void setIc_cover(String ic_cover) {
		this.ic_cover = ic_cover;
	}

	public String getIc_status() {
		return ic_status;
	}

	public void setIc_status(String ic_status) {
		this.ic_status = ic_status;
	}

	public String getIc_is_public() {
		return ic_is_public;
	}

	public void setIc_is_public(String ic_is_public) {
		this.ic_is_public = ic_is_public;
	}
	
}
