package com.zcareze.biz.file.service.dto;

import java.io.Serializable;

/**
 * 上传临时授权数据传输对象
 *                       
 * @Filename UploadPolicyDTO.java
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
public class UploadPolicyDTO implements Serializable{
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -7848645844800581588L;
	/** 临时授权ID **/
	private String accessid;
	/** 权限 **/
	private String policy;
	/** 签名 **/
	private String signature;
	/** 地址 **/
	private String host;
	/** 过期时间 **/
	private String expire;
	/** 授权key **/
	private String fileKey;
	
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}
	public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	public String getAccessid() {
		return accessid;
	}
	public void setAccessid(String accessid) {
		this.accessid = accessid;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
