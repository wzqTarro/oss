package com.zcareze.biz.file.service.enst;

/**
 * 头像库类型枚举
 *                       
 * @Filename PhotoTypeEnum.java
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
 * <li>Date: 2019年1月2日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public enum PhotoTypeEnum {
	/** 账户 **/
	STAFF("staff", "账户", ".jpeg"),
	/** 职员 **/
	ACCOUNT("account", "职员", ".jpeg")
	;
	private String code;
	private String name;
	private String suffix;
	private PhotoTypeEnum(String code, String name, String suffix) {
		this.code = code;
		this.name = name;
		this.suffix = suffix;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getSuffix() {
		return suffix;
	}
}
