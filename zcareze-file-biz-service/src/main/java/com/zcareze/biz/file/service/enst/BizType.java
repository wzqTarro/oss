package com.zcareze.biz.file.service.enst;

/**
 * 业务库
 *                       
 * @Filename BizType.java
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
 * <li>Date: 2019年1月3日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public class BizType {
	/** 健康指导预录音频 **/
	public static final String GUIDANCE = "guidance";
	/** 智能分诊 **/
	public static final String TRIAGE = "triage";
	/** 资格证书 **/
	public static final String LICENSES = "licenses";
	/** 即时通讯 **/
	public static final String CHAT = "chat";
	/** 电子签名 **/
	public static final String SIGN = "sign";
	/** 干预项目图标 **/
	public static final String REFERRAL_ITEM = "referral_item";
	
	/**
	 * 智能分诊
	 *                       
	 * @Filename BizType.java
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
	 * <li>Date: 2019年1月3日</li>
	 * <li>Version: 1.0</li>
	 * <li>Content: create</li>
	 *
	 */
	public enum TriageTypeEnum {
		SYMPTOM("symptom", "症状简图", ".jpeg"),
		STEP("step", "步骤简图", ".jpeg")
		;
		private String code;
		private String name;
		private String suffix;
		private TriageTypeEnum(String code, String name, String suffix) {
			this.code = code;
			this.name = name;
			this.suffix = suffix;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSuffix() {
			return suffix;
		}
		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
	}
	
}
