package com.zcareze.biz.file.service.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件访问路径返回值
 *                       
 * @Filename VisitUrlDTO.java
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
public class VisitUrlDTO implements Serializable{

	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 6874693533672301521L;

	/** 授权访问URL(请求无值，返回有值) */
	private String ossUrl;

	/** 授权失效时间(请求无值，返回有值，用于前端判断授权是否已经失效过期，过期了需重新获取授权) */
	private Date expire;

	public String getOssUrl() {
		return ossUrl;
	}

	public void setOssUrl(String ossUrl) {
		this.ossUrl = ossUrl;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}
	
	
}
