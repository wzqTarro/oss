/**
 * zcareu Inc
 * Copyright (C) 2018 ALL Right Reserved
 */
package com.zcareze.biz.file.service.storemode;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.MatchMode;
import com.zcareze.biz.file.service.dto.UploadPolicyDTO;
import com.zcareze.biz.file.service.enst.FileTypeEnum;
import com.zcareze.biz.file.service.wrapper.CheckObjectIsExistsWrapper;
import com.zcareze.biz.file.service.wrapper.FileContentListWrapper;
import com.zcareze.biz.file.service.wrapper.UploadPolicyWrapper;
import com.zcareze.biz.file.service.wrapper.VisitUrlWrapper;
import com.zcareze.commons.method.annotation.ParamentValidate;
import com.zcareze.commons.method.annotation.ValidateRules;
import com.zcareze.commons.result.ResponseWrapper;
import com.zcareze.commons.result.StringWrapper;

/**
 * @author lveliu
 * @description
 * @date 2018-11-14
 */
public interface StoreInstance {

	/**
	 * 获取真实访问路径
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param fileTypeEnum
	 * @param relativeUrl
	 * @return
	 * @author wzq by 2018年12月20日 下午6:28:24
	 */
	@ParamentValidate(name = "文件类型", index = 0, rules = ValidateRules.NOT_NULL)
	VisitUrlWrapper getVisitUrl(FileTypeEnum fileTypeEnum, String relativeUrl, String accountId);

	/**
	 * 判断对象是否存在
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param fileTypeEnum
	 * @param relativeUrl
	 * @return
	 * @author wzq by 2018年12月24日 下午7:27:08
	 */
	@ParamentValidate(name = "文件类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "文件路径", index = 1, rules = ValidateRules.NOT_NULL)
	CheckObjectIsExistsWrapper checkObjectIsExists(FileTypeEnum fileTypeEnum, String relativeUrl);
    
	/**
     * 上传
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum 文件类型
     * @param object 上传内容
     * @param uploadUrl 上传路径
     * @author wzq by 2018年12月20日 下午5:50:54
     */
    @ParamentValidate(name = "文件类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "上传内容", index = 1, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "上传路径", index = 2, rules = ValidateRules.NOT_NULL)
    StringWrapper uploadObject(FileTypeEnum fileTypeEnum, Object object, String uploadUrl);
    
    /**
     * 获取对象内容
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnem 文件类型
     * @param objectUrl 访问对象路径
     * @return
     * @author wzq by 2018年12月24日 下午3:58:45
     */
    @ParamentValidate(name = "文件类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "访问对象路径", index = 1, rules = ValidateRules.NOT_NULL)
    StringWrapper getObjectContent(FileTypeEnum fileTypeEnum, String objectUrl);
    
    /**
     * 获取对象内容
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum
     * @param objectUrl
     * @return
     * @author wzq by 2018年12月24日 下午4:40:57
     */
    FileContentListWrapper getFileLineContent(FileTypeEnum fileTypeEnum, String objectUrl);
    
    /**
     * 下载文件到本地
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum
     * @param objectUrl
     * @param downFile
     * @return
     * @author wzq by 2018年12月24日 下午5:19:10
     */
    ResponseWrapper downloadObject(FileTypeEnum fileTypeEnum, String objectUrl, File downFile);
    
    /**
     * 删除
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum
     * @param relativeUrl
     * @author wzq by 2018年12月20日 下午6:08:12
     */
    @ParamentValidate(name = "上传类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "删除路径", index = 1, rules = ValidateRules.NOT_NULL)
    ResponseWrapper delObject(FileTypeEnum fileTypeEnum, String relativeUrl);
    
    /**
     * 复制
     * <p>
     * 应用领域：
     * </p>
     *
     * @param sourceFileTypeEnum
     * @param targetfileTypeEnum
     * @param sourceRelativeUrl
     * @param targetUrl
     * @return
     * @author wzq by 2018年12月24日 下午5:32:53
     */
    ResponseWrapper copyObject(FileTypeEnum sourceFileTypeEnum, FileTypeEnum targetFileTypeEnum, String sourceRelativeUrl, String targetRelativeUrl);
    
    /**
     * 移动
     * <p>
     * 应用领域：
     * </p>
     *
     * @param sourceFileTypeEnum
     * @param targetfileTypeEnum
     * @param sourceUrl
     * @param targetUrl
     * @author wzq by 2018年12月20日 下午6:29:44
     */
    StringWrapper moveObject(FileTypeEnum sourceFileTypeEnum, FileTypeEnum targetFileTypeEnum, String sourceRelativeUrl, String targetRelativeUrl);
    /**
     * 获取某个路径下文件夹列表
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum
     * @param directorys
     * @return
     * @author wzq by 2018年12月24日 下午9:46:36
     */
    @ParamentValidate(name = "文件类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "文件存储目录", index = 1, rules = ValidateRules.NOT_NULL)
    List<String> getDirectoryList(FileTypeEnum fileTypeEnum, String... directorys);
    /**
     * 获取某个路径下文件夹列表
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum
     * @param directorys
     * @return
     * @author wzq by 2018年12月24日 下午10:23:12
     */
    @ParamentValidate(name = "文件类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "文件存储目录", index = 1, rules = ValidateRules.NOT_NULL)
    List<String> getDirFileList(FileTypeEnum fileTypeEnum, String... directorys);
    /**
     * 获取临时上传授权url
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileTypeEnum
     * @param fileUrl
     * @param loginId
     * @return
     * @throws UnsupportedEncodingException
     * @author wzq by 2018年12月27日 上午10:55:53
     */
    UploadPolicyWrapper getUploadTemplatePolicy(FileTypeEnum fileTypeEnum, String fileUrl, String loginId, MatchMode matchMode);
}
