package com.zcareze.biz.file.service.wrapper;

import java.util.List;

import com.zcareze.commons.result.ResponseWrapper;

/**
 * 
 *                       
 * @Filename DirectoryListWrapper.java
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
public class DirectoryListWrapper extends ResponseWrapper{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = -7592236445037974288L;
	private List<String> folders;

	public List<String> getFolders() {
		return folders;
	}

	public void setFolders(List<String> folders) {
		this.folders = folders;
	}
	
}
