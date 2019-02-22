package com.zcareze.biz.file.service.enst;

/**
 * 业务名称枚举类
 *                       
 * @Filename ServiceNameEnum.java
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
public enum ServiceNameEnum {

	SERVICE("服务包", "service"),
	;
	private String name;
	private String englishName;
	private ServiceNameEnum(String name, String englishName) {
		this.name = name;
		this.englishName = englishName;
	}
	public String getName() {
		return name;
	}
	public String getEnglishName() {
		return englishName;
	}
	
}
