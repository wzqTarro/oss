package com.zcareze.biz.file.service.wrapper;

import com.zcareze.biz.file.service.dto.VisitUrlDTO;
import com.zcareze.commons.result.ResponseWrapper;

/**
 * 访问路径返回对象
 *                       
 * @Filename VisitUrlWrapper.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @Author wangziqin
 *
 * @Email wangziqin@zcareze.com
 *       
 * @History
 * <li>Author: wangziqin</li>
 * <li>Date: 2019年2月13日</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 *
 */
public class VisitUrlWrapper extends ResponseWrapper{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 4934539470175399983L;
	private VisitUrlDTO visitUrlDTO;

	public VisitUrlDTO getVisitUrlDTO() {
		return visitUrlDTO;
	}

	public void setVisitUrlDTO(VisitUrlDTO visitUrlDTO) {
		this.visitUrlDTO = visitUrlDTO;
	}
	
	
}
