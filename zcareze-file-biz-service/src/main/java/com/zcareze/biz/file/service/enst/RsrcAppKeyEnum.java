package com.zcareze.biz.file.service.enst;

/**
 * 应用资源端类型
 *                       
 * @Filename VResourceItemEnum.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author wzq
 *
 * @Email wangziqin@zcareze.com
 *       
 * @History
 * <li>Author: wzq</li>
 * <li>Date: 2018年12月27日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public enum RsrcAppKeyEnum {

	/** 居民 **/
	RESIDENT_PHONE("residentPhone", "居民"),
	/** 医生 **/
	DOCTOR("doctor", "医生"),
	/** 家庭 **/
	RESIDENT("resident", "家庭"),
	/** 微信 **/
	WECHAT("wechat", "微信"),
	/** 服务 **/
	SERVICE("service", "服务端"),
	/** pc端 **/
	PC("pc", "pc端")
	;
	
	private String value;
	private String name;
	private RsrcAppKeyEnum(String value, String name) {
		this.value = value;
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
}
