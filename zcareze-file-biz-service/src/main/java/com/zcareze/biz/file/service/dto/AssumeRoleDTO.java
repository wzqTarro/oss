package com.zcareze.biz.file.service.dto;

import java.io.Serializable;

/**
 * STS临时授权
 *                       
 * @Filename AssumeRoleDTO.java
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
public class AssumeRoleDTO implements Serializable{
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 5499451360811358840L;
	/** 临时访问id **/
	private String accessId;
	/** 临时访问密钥 **/
	private String accessKeyScret;
	/** 安全令牌 **/
	private String securityToken;
	/** 主机 **/
	private String host;
	/** 过期时间 **/
	private String expireEndTime;
	/** 空间名 **/
	private String spaceName;
	/** 域名 **/
	private String region;
	public String getAccessId() {
		return accessId;
	}
	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}
	public String getAccessKeyScret() {
		return accessKeyScret;
	}
	public void setAccessKeyScret(String accessKeyScret) {
		this.accessKeyScret = accessKeyScret;
	}
	public String getSecurityToken() {
		return securityToken;
	}
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getExpireEndTime() {
		return expireEndTime;
	}
	public void setExpireEndTime(String expireEndTime) {
		this.expireEndTime = expireEndTime;
	}
	public String getSpaceName() {
		return spaceName;
	}
	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}
