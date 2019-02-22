package com.zcareze.biz.file.service.enst;

public class RsrcType {
	/** 首页配置 **/
	public static final String HOMEPAGERES = "homepageRes";
	
	/**
	 * 首页配置客户端类型
	 *                       
	 * @Filename HomepageType.java
	 *
	 * @Description 
	 *
	 * @Version 1.0
	 *
	 * @Author wangziqin
	 *
	 * @Email wangziqin@zcareze.com
	 *       
	 * @History
	 * <li>Author: wangziqin</li>
	 * <li>Date: 2019年1月9日</li>
	 * <li>Version: 1.0</li>
	 * <li>Content: create</li>
	 *
	 */
	public enum HomepageKeyEnum {
		/** 居民 **/
		RESIDENT_APP("residentApp", "居民App"),
		/** 医生 **/
		DOCTOR_APP("doctorApp", "医生App"),
		/** 微信 **/
		WECHAT("wechat", "微信"),
		/** pc端 **/
		PC("pc", "pc端")
		;
		
		private String value;
		private String name;
		private HomepageKeyEnum(String value, String name) {
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
}
