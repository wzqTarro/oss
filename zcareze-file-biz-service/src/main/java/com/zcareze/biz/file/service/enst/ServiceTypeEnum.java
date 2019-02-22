package com.zcareze.biz.file.service.enst;

/**
 * 服务包类型
 *                       
 * @Filename ServiceTypeEnum.java
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
public enum ServiceTypeEnum {

	/** 图标 **/
	ICON("icon", "图标", ".jpeg"),
	/** 服务内容 **/
	CONTENT("content", "服务内容", ".html"),
	/** 注意事项 **/
	CAUTION("caution", "注意事项", ".html")
	;
	private String code;
	private String name;
	private String suffix;
	private ServiceTypeEnum(String code, String name, String suffix) {
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
