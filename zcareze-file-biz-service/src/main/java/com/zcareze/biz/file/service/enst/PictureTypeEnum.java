package com.zcareze.biz.file.service.enst;

/**
 * 图片类型
 *                       
 * @Filename PictureTypeEnum.java
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
public enum PictureTypeEnum {
	/** 签约 **/
	CONTRACT("contract", "签约", ".jpeg"),
	/** 邀请 **/
	INVITE("invite", "邀请", ".jpeg"),
	/** 评论 **/
	APPRAISE("appraise", "评论", ".jpeg"),
	/** 随访 **/
	REFERRAL("referral", "随访", ".jpeg"),
	/** 居民档案图片 **/
	PORTRAITURL("portraiturl", "居民档案图片", ".jpeg");
	;
	private String code;
	private String name;
	private String suffix;
	private PictureTypeEnum(String code, String name, String suffix) {
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
