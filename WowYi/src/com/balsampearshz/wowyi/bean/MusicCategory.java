package com.balsampearshz.wowyi.bean;

import java.io.Serializable;

public class MusicCategory implements Serializable {

	/**
	 * serialized id
	 */
	private static final long serialVersionUID = -6758923013621498488L;
	
	private String mc_id;
	
	private String mc_name;
	
	private String mc_cover;
	
	private String mc_desc;
	
	private String mc_status;
	
	private String mc_seq;
	
	/**
	 * @return the mc_id
	 */
	public String getMc_id() {
		return mc_id;
	}

	/**
	 * @param mc_id the mc_id to set
	 */
	public void setMc_id(String mc_id) {
		this.mc_id = mc_id;
	}

	/**
	 * @return the mc_name
	 */
	public String getMc_name() {
		return mc_name;
	}

	/**
	 * @param mc_name the mc_name to set
	 */
	public void setMc_name(String mc_name) {
		this.mc_name = mc_name;
	}

	/**
	 * @return the mc_cover
	 */
	public String getMc_cover() {
		return mc_cover;
	}

	/**
	 * @param mc_cover the mc_cover to set
	 */
	public void setMc_cover(String mc_cover) {
		this.mc_cover = mc_cover;
	}

	/**
	 * @return the mc_desc
	 */
	public String getMc_desc() {
		return mc_desc;
	}

	/**
	 * @param mc_desc the mc_desc to set
	 */
	public void setMc_desc(String mc_desc) {
		this.mc_desc = mc_desc;
	}

	/**
	 * @return the mc_status
	 */
	public String getMc_status() {
		return mc_status;
	}

	/**
	 * @param mc_status the mc_status to set
	 */
	public void setMc_status(String mc_status) {
		this.mc_status = mc_status;
	}

	/**
	 * @return the mc_seq
	 */
	public String getMc_seq() {
		return mc_seq;
	}

	/**
	 * @param mc_seq the mc_seq to set
	 */
	public void setMc_seq(String mc_seq) {
		this.mc_seq = mc_seq;
	}


	
}
