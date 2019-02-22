package com.zcareze.biz.file.service.wrapper;

import java.util.List;

import com.zcareze.commons.result.ResponseWrapper;

/**
 * 
 *                       
 * @Filename FileListContentWrapper.java
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
public class FileContentListWrapper extends ResponseWrapper {

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -1401522247714334046L;
	
	private List<String> fileContent;

	public List<String> getFileContent() {
		return fileContent;
	}

	public void setFileContent(List<String> fileContent) {
		this.fileContent = fileContent;
	}

}
