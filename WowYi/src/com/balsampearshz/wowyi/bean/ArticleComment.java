package com.balsampearshz.wowyi.bean;

import java.io.Serializable;

public class ArticleComment implements Serializable {

	/**
	 * serialVersionUid 
	 */
	private static final long serialVersionUID = 5904152281148889553L;
	
	private  String a_id;
	
	private String aco_a_id;
	
	private String aco_content;
	
	private String aco_u_id;
	
	private String u_id;
	
	private String u_nickname;
	
	private String u_name;

	public String getA_id() {
		return a_id;
	}

	public void setA_id(String a_id) {
		this.a_id = a_id;
	}

	public String getAco_a_id() {
		return aco_a_id;
	}

	public void setAco_a_id(String aco_a_id) {
		this.aco_a_id = aco_a_id;
	}

	public String getAco_content() {
		return aco_content;
	}

	public void setAco_content(String aco_content) {
		this.aco_content = aco_content;
	}

	public String getAco_u_id() {
		return aco_u_id;
	}

	public void setAco_u_id(String aco_u_id) {
		this.aco_u_id = aco_u_id;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getU_nickname() {
		return u_nickname;
	}

	public void setU_nickname(String u_nickname) {
		this.u_nickname = u_nickname;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	
}
