package com.zcareze.biz.file.service.wrapper;

import com.zcareze.commons.result.ResponseWrapper;

/**
 * 判断对象是否存在数据返回类
 *                       
 * @Filename CheckObjectIsExistsWrapper.java
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
public class CheckObjectIsExistsWrapper extends ResponseWrapper{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -2546854741130305787L;

	private boolean isExists;

	public boolean isExists() {
		return isExists;
	}

	public void setExists(boolean isExists) {
		this.isExists = isExists;
	}
	
	
}
