package com.zcareze.biz.file.service.enst;

/**
 * 系统的部署环境-常量定义
 *                       
 * @Filename DeploymentEnvironment.java
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
 * <li>Date: 2018年12月24日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public class DeploymentEnvironment {
	/**
	 * 线上部署正式环境
	 */
	public static final String ONLINE_OFFICIAL_ENVIRONMENT = "OnlineOfficial";

	/**
	 * 线上部署测试环境
	 */
	public static final String ONLINE_TEST_ENVIRONMENT = "OnlineTest";

	/**
	 * 本地部署开发环境
	 */
	public static final String LOCAL_DEVELOPMENT_ENVIRONMENT = "Develop";
	
	/**
	 *  公有读
	 */
	public static final String PUBLIC_READ = "public_read";

	/**
	 *  公有读写
	 */
	public static final String PUBLIC_READ_WRITE = "public_read_write";

	/**
	 *  私有
	 */
	public static final String PRIVATE = "private";
	
}
