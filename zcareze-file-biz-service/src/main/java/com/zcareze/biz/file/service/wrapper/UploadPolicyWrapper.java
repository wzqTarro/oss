package com.zcareze.biz.file.service.wrapper;

import java.io.Serializable;

import com.zcareze.biz.file.service.dto.UploadPolicyDTO;
import com.zcareze.commons.result.ResponseWrapper;

/**
 * 临时上传授权
 *                       
 * @Filename TemporaryPolicyWrapper.java
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
public class UploadPolicyWrapper extends ResponseWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4466397758761458533L;
	private UploadPolicyDTO uploadPolicy;
	public UploadPolicyDTO getUploadPolicy() {
		return uploadPolicy;
	}
	public void setUploadPolicy(UploadPolicyDTO uploadPolicy) {
		this.uploadPolicy = uploadPolicy;
	}

	
	
}
