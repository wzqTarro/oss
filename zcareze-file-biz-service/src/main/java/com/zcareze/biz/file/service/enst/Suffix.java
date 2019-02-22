package com.zcareze.biz.file.service.enst;

/**
 * 文件后缀
 *                       
 * @Filename Suffix.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author chenrj
 *
 * @Email chenrongjun@zcareze.com
 *       
 * @History
 * <li>Author: chenrj</li>
 * <li>Date: 2019年1月15日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public class Suffix {
	/** jpeg **/
	public static final String JPEG = ".jpeg";
	
	/**
	 * 电子签名文件后缀
	 *                       
	 * @Filename Suffix.java
	 *
	 * @Description 
	 *
	 * @Version 1.0
	 *
	 * @Author chenrj
	 *
	 * @Email chenrongjun@zcareze.com
	 *       
	 * @History
	 * <li>Author: chenrj</li>
	 * <li>Date: 2019年1月15日</li>
	 * <li>Version: 1.0</li>
	 * <li>Content: create</li>
	 *
	 */
	public enum SignSuffixEnum {
		JSON(".json"),
		SIGNATURE(".signature");
		private String value;
		private SignSuffixEnum(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}
}
