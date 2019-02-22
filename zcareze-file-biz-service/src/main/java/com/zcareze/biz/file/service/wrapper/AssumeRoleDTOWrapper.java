package com.zcareze.biz.file.service.wrapper;

import com.zcareze.biz.file.service.dto.AssumeRoleDTO;
import com.zcareze.commons.result.ResponseWrapper;

/**
 * STS临时授权
 *                       
 * @Filename AssumeRoleDTOWrapper.java
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
 * <li>Date: 2018年12月26日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public class AssumeRoleDTOWrapper extends ResponseWrapper{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1651739179859307951L;
	private AssumeRoleDTO assumeRole;

	public AssumeRoleDTO getAssumeRole() {
		return assumeRole;
	}

	public void setAssumeRole(AssumeRoleDTO assumeRole) {
		this.assumeRole = assumeRole;
	}
}
