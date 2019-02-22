package com.zcareze.biz.file.service.wrapper;

import java.util.List;

import com.zcareze.biz.file.service.dto.UploadPolicyDTO;
import com.zcareze.commons.result.ResponseWrapper;

/**
 * 多条授权信息数据传输对象
 *                       
 * @Filename UploadPolicyListWrapper.java
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
public class UploadPolicyListWrapper extends ResponseWrapper{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 875544770690462103L;
	/** 授权 **/
	private List<UploadPolicyDTO> uploadPolicyList;
	public List<UploadPolicyDTO> getUploadPolicyList() {
		return uploadPolicyList;
	}
	public void setUploadPolicyList(List<UploadPolicyDTO> uploadPolicyList) {
		this.uploadPolicyList = uploadPolicyList;
	}
}
