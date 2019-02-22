package com.zcareze.biz.file.service.wrapper;

import java.util.List;

import com.zcareze.commons.result.ResponseWrapper;

/**
 * 访问路径列表数据返回
 *                       
 * @Filename FileUrlListWrapper.java
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
public class FileUrlListWrapper extends ResponseWrapper {

	
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -8978546818280793667L;
	
	private List<String> fileUrlList;

	public List<String> getFileUrlList() {
		return fileUrlList;
	}

	public void setFileUrlList(List<String> fileUrlList) {
		this.fileUrlList = fileUrlList;
	}

}
