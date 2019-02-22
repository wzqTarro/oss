package com.zcareze.biz.file.service.enst;

/**
 * 应用资源类型
 *                       
 * @Filename VResourceTypeEnum.java
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
public enum RsrcAppTypeEnum {

	/** 安卓APK，0-全量更新**/
	ANDROID_0("android-0", "安卓APK", ".apk"),
	/** 安卓APK，1-增量更新 **/
	ANDROID_1("android-1", "安卓PATCH", ".patch"),
	/** 压缩包 **/
	WEBZIP("webzip", "压缩包", ".zip"),
	/** 服务端文件 **/
	SERVICE_WAR("service", "服务端文件", ".war")
	;
	private String value;
	private String name;
	private String suffix;
	private RsrcAppTypeEnum(String value, String name, String suffix) {
		this.value = value;
		this.name = name;
		this.suffix = suffix;
	}
	public String getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	public String getSuffix() {
		return suffix;
	}
}
