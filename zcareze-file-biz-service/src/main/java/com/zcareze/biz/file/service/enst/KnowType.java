package com.zcareze.biz.file.service.enst;

/**
 * 知识库
 *                       
 * @Filename KnowType.java
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
public class KnowType {
	/** 问卷 **/
	public static final String QNA = "qna";
	/** 文章 **/
	public static final String ARTICLE = "article";
	
	/**
	 * 知识库-问卷
	 *                       
	 * @Filename QnaTypeEnum.java
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
	public enum QnaTypeEnum {
		/** 插图 **/
		FIGURE("插图", "", ".jpeg"),
		/** 公共问卷图标 **/
		PUBLIC_ICON("公共问卷图标", "", ".jpeg"),
		/** 问卷图标 **/
		ICON("问卷图标", "qna_icon", ".jpeg"),
		;
		private String name;
		private String fileName;
		private String suffix;
		private QnaTypeEnum(String name, String fileName, String suffix) {
			this.name = name;
			this.fileName = fileName;
			this.suffix = suffix;
		}
		
		public String getFileName() {
			return fileName;
		}

		public String getName() {
			return name;
		}
		public String getSuffix() {
			return suffix;
		}
	}
	
	/**
	 * 知识库-文章
	 *                       
	 * @Filename KnowType.java
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
	public enum ArticleTypeEnum {
		/** 图文（网页） **/
		PIC_KNOW("图文（网页）", ""),
		/** 音频 **/
		MUSIC("音频", "1.mp3"),
		/** 视频 **/
		VIDEO("视频", "1.mp4")
		;
		private String name;
		private String fileName;
		private ArticleTypeEnum(String name, String fileName) {
			this.name = name;
			this.fileName = fileName;
		}
		public String getName() {
			return name;
		}
		public String getFileName() {
			return fileName;
		}
		
	}
}
