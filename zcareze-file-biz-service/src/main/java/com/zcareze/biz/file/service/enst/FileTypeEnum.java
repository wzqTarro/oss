/**
 * zcareu Inc
 * Copyright (C) 2018 ALL Right Reserved
 */
package com.zcareze.biz.file.service.enst;

/**
 * @author lveliu
 * @description
 * @date 2018-11-14
 */
public enum FileTypeEnum {
    /**
     * 知识库
     */
	KNOW("OSS", 
			"zcareze-know-test", "know.test.zcareze.com", 
			"zcareze-know-test", "know.test.zcareze.com",
			"zcareze-know", "know.zcareze.com", 
		"public_read"),
    /**
     * 健康文档库
     */
    HDOC("OSS", 
    		"zcareze-hdoc-test", "zcareze-hdoc-test.oss-cn-hangzhou.aliyuncs.com", 
    		"zcareze-hdoc-test", "zcareze-hdoc-test.oss-cn-hangzhou.aliyuncs.com",
    		"zcareze-hdoc", "zcareze-hdoc.oss-cn-hangzhou.aliyuncs.com", 
    	"private"),
    /**
     * 照片
     */
    PHOTO("OSS", 
    		"zcareze-photo-test", "zcareze-photo-test.oss-cn-hangzhou.aliyuncs.com", 
    		"zcareze-photo-test", "zcareze-photo-test.oss-cn-hangzhou.aliyuncs.com",
    		"zcareze-photo", "zcareze-photo.oss-cn-hangzhou.aliyuncs.com", 
    	"public_read"),
    /**
     * 归档，存档
     */
    ARCHIVE("OSS", 
    		"zcareze-archive-test", "zcareze-archive-test.oss-cn-hangzhou.aliyuncs.com", 
    		"zcareze-archive-test", "zcareze-archive-test.oss-cn-hangzhou.aliyuncs.com",
    		"zcareze-archive", "zcareze-archive.oss-cn-hangzhou.aliyuncs.com", 
    		"private"),
    /**
     * 报表
     */
    REPORT("OSS", 
    		"zcareze-report-test", "zcareze-report-test.oss-cn-hangzhou.aliyuncs.com", 
    		"zcareze-report-test", "zcareze-report-test.oss-cn-hangzhou.aliyuncs.com",
    		"zcareze-report", "zcareze-report.oss-cn-hangzhou.aliyuncs.com", 
    		"public_read"),
    /**
     * 资源
     */
    RSRC("OSS", 
    		"zcareze-rsrc-test", "zcareze-rsrc-test.oss-cn-hangzhou.aliyuncs.com", 
    		"zcareze-rsrc-test", "zcareze-rsrc-test.oss-cn-hangzhou.aliyuncs.com",
    		"zcareze-rsrc", "zcareze-rsrc.oss-cn-hangzhou.aliyuncs.com", 
    	"public_read"),
	/**
	 * 业务
	 */
	BIZ("OSS", 
			"zcareze-biz-test", "zcareze-biz-test.oss-cn-hangzhou.aliyuncs.com", 
			"zcareze-biz-test", "zcareze-biz-test.oss-cn-hangzhou.aliyuncs.com",
			"zcareze-biz", "zcareze-biz.oss-cn-hangzhou.aliyuncs.com", 
		"private"),
	/**
	 * 语音
	 */
	AUDIO("OSS", 
			"zcareze-audio-test", "zcareze-audio-test.oss-cn-hangzhou.aliyuncs.com", 
			"zcareze-audio-test", "zcareze-audio-test.oss-cn-hangzhou.aliyuncs.com",
			"zcareze-audio", "zcareze-audio.oss-cn-hangzhou.aliyuncs.com", 
		"public_read"),
	/**
	 * 附件库
	 */
	ATT("OSS", 
			"zcareze-att-test", "192.168.0.124:8090", 
			"zcareze-att-test", "wap.test.zcareze.com/zcareze-att-test",
			"zcareze-att", "wap.zcareze.com/zcareze-att", 
		"public_read"),
	/**
	 * 图片
	 */
	PICTURE("OSS", 
			"zcareze-picture-test", "zcareze-picture-test.oss-cn-hangzhou.aliyuncs.com", 
			"zcareze-picture-test", "zcareze-picture-test.oss-cn-hangzhou.aliyuncs.com",
			"zcareze-picture", "zcareze-picture.oss-cn-hangzhou.aliyuncs.com", 
			"private")
	;

    private String storeMode;
    /** 本地开发测试存储空间 **/
    private String testBucketName;
    /**  **/
    private String testDomainName;
    /** 线上部署测试存储空间 **/
    private String onlineTestBucketName;
    /**  **/
    private String onlineTestDomainName;
    /** 正式存储空间 **/
    private String bucketName;
    /**  **/
    private String domainName;
    /** 存储空间权限  **/
    private String bucketPolicy;

    FileTypeEnum(String storeMode, String testBucketName, String testDomainName, 
    		String onlineTestBucketName, String onlineTestDomainName, String bucketName, String domainName, String bucketPolicy) {
        this.storeMode = storeMode;
        this.testBucketName = testBucketName;
        this.testDomainName = testDomainName;
        this.onlineTestBucketName = onlineTestBucketName;
        this.onlineTestDomainName = onlineTestDomainName;
        this.bucketName = bucketName;
        this.domainName = domainName;
        this.bucketPolicy = bucketPolicy;
    }

	public String getStoreMode() {
		return storeMode;
	}

	public String getTestBucketName() {
		return testBucketName;
	}

	public String getTestDomainName() {
		return testDomainName;
	}

	public String getBucketName() {
		return bucketName;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getBucketPolicy() {
		return bucketPolicy;
	}

	public String getOnlineTestBucketName() {
		return onlineTestBucketName;
	}

	public String getOnlineTestDomainName() {
		return onlineTestDomainName;
	}

	
}
