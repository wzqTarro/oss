/**
 * zcareu Inc
 * Copyright (C) 2018 ALL Right Reserved
 */
package com.zcareze.biz.file.service;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.MatchMode;
import com.zcareze.biz.file.service.dto.UploadPolicyDTO;
import com.zcareze.biz.file.service.dto.VisitUrlDTO;
import com.zcareze.biz.file.service.enst.BizType;
import com.zcareze.biz.file.service.enst.BizType.TriageTypeEnum;
import com.zcareze.biz.file.service.enst.Constants;
import com.zcareze.biz.file.service.enst.FileTypeEnum;
import com.zcareze.biz.file.service.enst.KnowType;
import com.zcareze.biz.file.service.enst.KnowType.ArticleTypeEnum;
import com.zcareze.biz.file.service.enst.KnowType.QnaTypeEnum;
import com.zcareze.biz.file.service.enst.PhotoTypeEnum;
import com.zcareze.biz.file.service.enst.PictureTypeEnum;
import com.zcareze.biz.file.service.enst.ServiceNameEnum;
import com.zcareze.biz.file.service.enst.ServiceTypeEnum;
import com.zcareze.biz.file.service.enst.Suffix;
import com.zcareze.biz.file.service.enst.Suffix.SignSuffixEnum;
import com.zcareze.biz.file.service.enst.RsrcAppKeyEnum;
import com.zcareze.biz.file.service.enst.RsrcAppTypeEnum;
import com.zcareze.biz.file.service.enst.RsrcType;
import com.zcareze.biz.file.service.enst.RsrcType.HomepageKeyEnum;
import com.zcareze.biz.file.service.factory.FileInstanceFactory;
import com.zcareze.biz.file.service.storemode.StoreInstance;
import com.zcareze.biz.file.service.wrapper.FileUrlListWrapper;
import com.zcareze.biz.file.service.wrapper.UploadPolicyWrapper;
import com.zcareze.biz.file.service.wrapper.VisitUrlListWrapper;
import com.zcareze.biz.file.service.wrapper.VisitUrlWrapper;
import com.zcareze.commons.ZcarezeConstant;
import com.zcareze.commons.method.annotation.ParamentValidate;
import com.zcareze.commons.method.annotation.ValidateRules;
import com.zcareze.commons.result.ResponseWrapper;
import com.zcareze.commons.result.StringWrapper;
import com.zcareze.commons.result.WrapperErrEunm;
import com.zcareze.commons.utils.DateUtil;
import com.zcareze.commons.utils.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author lveliu
 * @description
 * @date 2018-11-14
 */
@Service(value = "fileBizService")
public class FileBizService {

	/**
	 * 生成服务包相对路径
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param serviceType
	 * @param cloudId
	 * @param serviceId
	 * @return
	 * @author wzq by 2018年12月27日 下午12:14:39
	 */
	private String generateServiceRelativeUrl(ServiceTypeEnum serviceType, String cloudId, String serviceId) {
		StringBuilder builder = new StringBuilder(ServiceNameEnum.SERVICE.getEnglishName());
		builder.append(Constants.SPLIT);
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(serviceId);
		builder.append(Constants.SPLIT);
		builder.append(serviceType.getCode());
		builder.append(serviceType.getSuffix());
		return builder.toString();
	}
	/**
	 * 生成图片库相对路径
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param pictureType
	 * @param cloudId
	 * @param serviceId
	 * @return
	 * @author wzq by 2019年1月2日 上午11:12:28
	 */
	private String generatePictureRelativeUrl(PictureTypeEnum pictureType, String cloudId, String serviceId, Integer index, String phone) {
		StringBuilder builder = new StringBuilder();
		builder.append(pictureType.getCode());
		builder.append(Constants.SPLIT);
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(DateUtil.getDateTime("yyyy", new Date()));
		builder.append(Constants.SPLIT);
		builder.append(serviceId);
		switch (pictureType) {
		case CONTRACT: 
			builder.append("_");
			builder.append(index);
			break;
		case INVITE:
			builder.append(Constants.SPLIT);
			builder.append(phone);
			break;
		case APPRAISE:
			builder.append("_");
			builder.append(index);
			break;
		case REFERRAL:
			builder.append("_");
			builder.append(index);
			break;
		default:
			break;
		}
		
		builder.append(pictureType.getSuffix());
		return builder.toString();
	}
	/**
	 * 生成头像库相对路径
	 * <p>
	 * 应用领域：职员图片、账户头像、组织图片
	 * </p>
	 *
	 * @param photeTypeEnum 
	 * @param cloudId
	 * @param userId
	 * @return
	 * @author wzq by 2019年1月2日 下午5:00:32
	 */
	private String generatePhotoRelativeUrl(PhotoTypeEnum photeTypeEnum, String cloudId, String userId) {
		StringBuilder builder = new StringBuilder();
		builder.append(photeTypeEnum.getCode());
		builder.append(Constants.SPLIT);
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(userId);
		builder.append(photeTypeEnum.getSuffix());
		return builder.toString();
	}
	/**
	 * 生成问卷相对路径
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param qnaTypeEnum
	 * @param cloudId
	 * @param qnaId
	 * @param answerCode
	 * @return
	 * @author wzq by 2019年1月2日 下午5:48:15
	 */
	private String generateQnaRelativeUrl(QnaTypeEnum qnaTypeEnum, String cloudId, String qnaId, String answerCode) {
		StringBuilder builder = new StringBuilder();
		builder.append(KnowType.QNA);
		builder.append(Constants.SPLIT);
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(qnaId);
		builder.append(Constants.SPLIT);
		switch (qnaTypeEnum) {
		case FIGURE:
			builder.append(answerCode);
			break;
		case PUBLIC_ICON:
			builder.append(qnaId);
			break;
		case ICON:
			builder.append(qnaTypeEnum.getFileName());
			break;
		default:
			break;
		}
		builder.append(qnaTypeEnum.getSuffix());
		return builder.toString();
	}
	/**
	 * 生成文章相对路径
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param articleTypeEnum
	 * @param articleId
	 * @return
	 * @author wzq by 2019年1月3日 上午10:13:10
	 */
	private String generateArticleRelativeUrl(ArticleTypeEnum articleTypeEnum, String articleId) {
		StringBuilder builder = new StringBuilder();
		builder.append(KnowType.ARTICLE);
		builder.append(Constants.SPLIT);
		builder.append(articleId);
		builder.append(Constants.SPLIT);
		builder.append(articleTypeEnum.getFileName());
		return builder.toString();
	}
	/**
	 * 生成健康指导预录音频
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param cloudId
	 * @param guidanceId
	 * @return
	 * @author wzq by 2019年1月3日 下午3:55:00
	 */
	private String generateGuidanceRelativeUrl(String cloudId, String guidanceId) {
		StringBuilder builder = new StringBuilder();
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(BizType.GUIDANCE);
		builder.append(Constants.SPLIT);
		builder.append(guidanceId);
		builder.append(Constants.SPLIT);
		builder.append(StringUtils.getUUID());
		builder.append(".mp3");
		return builder.toString();
	}
	 /**
     * 生成智能分诊相对路径
     * <p>
     * 应用领域：
     * </p>
     *
     * @param triageTypeEnum
     * @param cloudId
     * @param id
     * @return
     * @author wzq by 2019年1月3日 下午4:44:16
     */
    private String generateTriageRelativeUrl(TriageTypeEnum triageTypeEnum, String cloudId, Integer id) {
    	StringBuilder builder = new StringBuilder();
    	builder.append(cloudId);
    	builder.append(Constants.SPLIT);
    	builder.append(BizType.TRIAGE);
    	builder.append(Constants.SPLIT);
    	builder.append(triageTypeEnum.getCode());
    	builder.append(Constants.SPLIT);
    	builder.append(id);
    	builder.append(triageTypeEnum.getSuffix());
    	return builder.toString();
    }
	/**
	 * 判断相对路径开头是否为"/"
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param relativeUrl
	 * @return
	 * @author wzq by 2018年12月28日 下午2:47:08
	 */
	private String checkRelativeUrl(String relativeUrl) {
		if (relativeUrl.startsWith("/")) {
			return relativeUrl.substring(1);
		}
		return relativeUrl;
	}
	
	// 获取真实访问路径-start
    /**
     * 真实访问路径-服务包文件
     * <p>
     * 应用领域：
     * </p>
     *
     * @param relativeUrl 相对路径
     * @param loginId
     * @return
     * @author wzq by 2018年12月20日 下午5:35:47
     */
	@ParamentValidate(name = "相对路径", rules = ValidateRules.NOT_NULL, index = 0)
    public StringWrapper getServiceIconVisitUrl(String relativeUrl, String loginId) {
		StringWrapper wrapper = new StringWrapper();
		// 相对路径开头不能为斜杠
		relativeUrl = checkRelativeUrl(relativeUrl);
		
		// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);
        VisitUrlWrapper visitWrapper = instance.getVisitUrl(FileTypeEnum.KNOW, relativeUrl, loginId);
        
        if (!visitWrapper.isSuccess()) {
        	wrapper.setErrcode(visitWrapper.getErrcode());
        	wrapper.setErrmsg(visitWrapper.getErrmsg());
        	return wrapper;
        }
        wrapper.setStrValue(visitWrapper.getVisitUrlDTO().getOssUrl());
        return wrapper;
    }
	/**
     * 获取真实访问路径-分诊症状简图、分诊自测步骤简图、.....
     * <p>
     * 应用领域：
     * </p>
     *
     * @param fileKeyList 文件地址KEY数据列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2019年1月3日 下午4:54:00
     */
    @ParamentValidate(name = "文件地址列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getTriageFileVisitUrl(List<String> fileKeyList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < fileKeyList.size(); i++) {
    		String relativeUrl = fileKeyList.get(i);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.BIZ, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    /**
     * 服务端上传-应用资源
     * <p>
     * 应用领域：
     * </p>
     *
     * @param vResourceItemEnum 端类型
     * @param rsrcAppTypeEnum 资源类型
     * @param firmType 厂商类别
     * @param file 文件
     * @param fileName 文件名
     * @return
     * @throws OSSException
     * @throws ClientException
     * @throws UnsupportedEncodingException
     * @author wzq by 2018年12月27日 下午3:32:56
     */
    @ParamentValidate(name = "端类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "资源类型", index = 1, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "文件", index = 3, rules = ValidateRules.NOT_NULL)
    public StringWrapper uploadRsrcFile(RsrcAppKeyEnum rsrcAppKeyEnum, RsrcAppTypeEnum rsrcAppTypeEnum, 
    		Integer firmType, File file){
    	
    	// 资源类型为安卓时，厂商类别不能为null
    	if (rsrcAppTypeEnum.equals(RsrcAppTypeEnum.ANDROID_0) || rsrcAppTypeEnum.equals(RsrcAppTypeEnum.ANDROID_1)) {
			if (firmType == null) {
				StringWrapper wrapper = new StringWrapper();
				wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
				wrapper.setErrmsg("厂商类别不能为空");
				return wrapper;
			}
		}
    	
    	if (RsrcAppKeyEnum.PC.equals(rsrcAppKeyEnum)) {
    		if ((file.length() / 1024.0 / 1024.0) > 20) {
    			StringWrapper wrapper = new StringWrapper();
        		wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
        		wrapper.setErrmsg("上传文件大小不能大于20M");
        		return wrapper;
    		}
    	} else if (RsrcAppKeyEnum.SERVICE.equals(rsrcAppKeyEnum)) {
    		if ((file.length() / 1024.0 / 1024.0) > 100) {
    			StringWrapper wrapper = new StringWrapper();
        		wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
        		wrapper.setErrmsg("上传文件大小不能大于100M");
        		return wrapper;
    		}
    	} else {
    		if ((file.length() / 1024.0 / 1024.0) > 10) {
        		StringWrapper wrapper = new StringWrapper();
        		wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
        		wrapper.setErrmsg("上传文件大小不能大于10M");
        		return wrapper;
        	}
    	}
    	// 文件名
        String fileName = file.getName();
    	
    	// 拼接存储路径
    	StringBuffer uploadUrl = new StringBuffer("versionResource");
    	uploadUrl.append(Constants.SPLIT);
    	uploadUrl.append(rsrcAppKeyEnum.getValue());
    	uploadUrl.append(Constants.SPLIT);
    	uploadUrl.append(rsrcAppTypeEnum.getValue());
    	if (firmType != null && (rsrcAppTypeEnum.equals(RsrcAppTypeEnum.ANDROID_0) || rsrcAppTypeEnum.equals(RsrcAppTypeEnum.ANDROID_1))) {
    		uploadUrl.append(Constants.SPLIT);
    		uploadUrl.append(firmType);
    	}
    	uploadUrl.append(Constants.SPLIT);
    	uploadUrl.append(fileName);
    		
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);
        StringWrapper wrapper = instance.uploadObject(FileTypeEnum.RSRC, file, uploadUrl.toString());
        return wrapper;
    }
    /**
     * 获取真实访问路径-应用资源
     * <p>
     * 应用领域：
     * </p>
     *
     * @param relativeUrl
     * @return
     * @author wzq by 2018年12月28日 上午10:56:55
     */
    @ParamentValidate(name = "存储路径", index = 0, rules = ValidateRules.NOT_NULL)
    public StringWrapper getRsrcFileVisitUrl(String relativeUrl, String loginId) {
    	StringWrapper wrapper = new StringWrapper();
    	// 相对路径开头不能为斜杠
    	relativeUrl = checkRelativeUrl(relativeUrl);
    	
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);
        VisitUrlWrapper visitWrapper = instance.getVisitUrl(FileTypeEnum.RSRC, relativeUrl, loginId);
        
        if (!visitWrapper.isSuccess()) {
        	wrapper.setErrcode(visitWrapper.getErrcode());
        	wrapper.setErrmsg(visitWrapper.getErrmsg());
        	return wrapper;
        }
        wrapper.setStrValue(visitWrapper.getVisitUrlDTO().getOssUrl());
        return wrapper;
    }
    /**
     * 获取真实访问路径列表-应用资源
     * <p>
     * 应用领域：
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2019年1月11日 上午10:56:55
     */
    @ParamentValidate(name = "存储路径", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getRsrcFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.RSRC, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    /**
     * 获取真实访问路径-业务库
     * <p>
     * 应用领域：健康指导预录音频、智能分诊(症状简图、步骤简图)、资格证书、首页配置
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午2:55:43
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getBizFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.BIZ, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    /**
     * 获取真实访问路径-知识库
     * <p>
     * 应用领域：文章（图文、音频、视频）、问卷（问题插图、公开问卷图标、问卷图标）、微信、协议、服务包（图标、服务内容、注意事项）
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param accountId 账户ID
     * @return
     * @author wzq by 2018年12月28日 下午2:57:13
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getKnowFileVisitUrl(List<String> relativeUrlList, String accountId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.KNOW, relativeUrl, accountId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    public VisitUrlWrapper getKnowFileVisitUrl(String relativeUrl, String accountId) {
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);
    	relativeUrl = checkRelativeUrl(relativeUrl);
    	VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.KNOW, relativeUrl, accountId);
    	return fileUrlWrapper;
    }
    /**
     * 获取真实访问路径-图片库
     * <p>
     * 应用领域：签约、邀请、评价、随访、居民档案图片
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午2:58:18
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getPictureFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PICTURE);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.PICTURE, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    /**
     * 获取真实访问路径-头像库
     * <p>
     * 应用领域：账户头像、职员头像、组织机构介绍照片 
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午3:01:54
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getPhotoFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PHOTO);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.PHOTO, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    /**
     * 获取真实访问路径-报表库
     * <p>
     * 应用领域：xslt、xml
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午3:04:34
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getReportFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.REPORT);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.REPORT, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    public VisitUrlWrapper getReportFileVisitUrl(String relativeUrl, String loginId) {
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.REPORT);
    	relativeUrl = checkRelativeUrl(relativeUrl);
    	VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.REPORT, relativeUrl, loginId);
    	return fileUrlWrapper;
    }
    /**
     * 获取真实访问路径-归档库
     * <p>
     * 应用领域： 即时通讯、支付、导入
     * </p>
     *
     * @param relativeUrlList 文件存储列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午3:05:39
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getArchiveFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.ARCHIVE);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.ARCHIVE, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    public VisitUrlWrapper getArchiveFileVisitUrl(String relativeUrl, String loginId) {
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.ARCHIVE);
    	relativeUrl = checkRelativeUrl(relativeUrl);
    	VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.ARCHIVE, relativeUrl, loginId);
    	return fileUrlWrapper;
    }
    /**
     * 获取真实访问路径-健康文档库
     * <p>
     * 应用领域：
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午3:06:42
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public VisitUrlListWrapper getHdocFileVisitUrl(List<String> relativeUrlList, String accountId) {
    	VisitUrlListWrapper wrapper = new VisitUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.HDOC);
    	List<VisitUrlDTO> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.HDOC, relativeUrl, accountId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO());
    	}
    	wrapper.setVisitUrlDTO(fileUrlList);
    	return wrapper;
    }
    public VisitUrlWrapper getHDocVisitUrl(String relativeUrl, String accountId) {
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.HDOC);
        relativeUrl = checkRelativeUrl(relativeUrl);
        return instance.getVisitUrl(FileTypeEnum.HDOC, relativeUrl, accountId);
    }
    /**
     * 获取真实访问路径-语音库
     * <p>
     * 应用领域：
     * </p>
     *
     * @param relativeUrlList 文件路径列表
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2018年12月28日 下午3:07:29
     */
    @ParamentValidate(name = "文件路径列表", index = 0, rules = ValidateRules.NOT_NULL)
    public FileUrlListWrapper getAudioFileVisitUrl(List<String> relativeUrlList, String loginId) {
    	FileUrlListWrapper wrapper = new FileUrlListWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.AUDIO);
    	List<String> fileUrlList = new ArrayList<>();
    	for (int i = 0; i < relativeUrlList.size(); i++) {
    		String relativeUrl = relativeUrlList.get(i);
    		relativeUrl = checkRelativeUrl(relativeUrl);
    		VisitUrlWrapper fileUrlWrapper = instance.getVisitUrl(FileTypeEnum.AUDIO, relativeUrl, loginId);
    		if (!fileUrlWrapper.isSuccess()) {
    			wrapper.setErrcode(fileUrlWrapper.getErrcode());
    			wrapper.setErrmsg(fileUrlWrapper.getErrmsg());
    			return wrapper;
    		}
    		fileUrlList.add(fileUrlWrapper.getVisitUrlDTO().getOssUrl());
    	}
    	wrapper.setFileUrlList(fileUrlList);
    	return wrapper;
    }
    public VisitUrlWrapper getAudioFileVisitUrl(String relativeUrl, String accountId) {
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.AUDIO);
        relativeUrl = checkRelativeUrl(relativeUrl);
        return instance.getVisitUrl(FileTypeEnum.AUDIO, relativeUrl, accountId);
    }
	// 获取真实访问路径-end
    
    // 获取host-start
    /**
     * 获取首页配置host
     * <p>
     * 应用领域：
     * </p>
     *
     * @return
     * @author wangziqin by 2019年1月11日 上午10:45:16
     */
    public StringWrapper getHomepageHost() {
    	StringWrapper wrapper = new StringWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);

    	VisitUrlWrapper visitUrlWrapper = instance.getVisitUrl(FileTypeEnum.RSRC, null, null);
    	if (!visitUrlWrapper.isSuccess()) {
    		wrapper.setErrcode(visitUrlWrapper.getErrcode());
    		wrapper.setErrmsg(visitUrlWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	wrapper.setStrValue(visitUrlWrapper.getVisitUrlDTO().getOssUrl());
    	return wrapper;
    }  
    // 获取host-end
    
	// 上传临时授权-start
    /**
     * 获取上传临时授权-服务包文件
     * <p>
     * 应用领域：
     * </p>
     *
     * @param serviceType 类型
     * @param cloudId 区域ID
     * @param serviceId 服务包ID
     * @param loginId 登陆ID
     * @return
     * @throws UnsupportedEncodingException
     * @author wzq by 2018年12月27日 下午1:13:57
     */
    @ParamentValidate(name = "登陆ID", rules = ValidateRules.NOT_NULL, index = 3)
    @ParamentValidate(name = "服务包ID", rules = ValidateRules.NOT_NULL, index = 2)
    @ParamentValidate(name = "区域ID", rules = ValidateRules.NOT_NULL, index = 1)
    @ParamentValidate(name = "类型", rules = ValidateRules.NOT_NULL, index = 0)
    public UploadPolicyWrapper getServicePutPolicyUrl(ServiceTypeEnum serviceType, String cloudId, String serviceId, String loginId) {
    	
    	// 相对路径
    	// 相对路径
    	String relativeUrl = generateServiceRelativeUrl(serviceType, cloudId, serviceId);
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);
    	return instance.getUploadTemplatePolicy(FileTypeEnum.KNOW, relativeUrl, loginId, MatchMode.Exact);
    }
    
    /**
     * 获取上传临时授权-签约 
     * <p>
     * 应用领域：签约现场照片存储
     * </p>
     *
     * @param cloudId 区域云ID
     * @param contractId 签约ID
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月2日 上午10:51:22
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "签约ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getContractPutPolicy(String cloudId, String contractId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PICTURE);
    	
    	List<UploadPolicyDTO> policyList = new ArrayList<>();
    	
    	// 相对路径,签约两张授权
    	for (int i = 1; i <= 2; i++) {
    		String relativeUrl = generatePictureRelativeUrl(PictureTypeEnum.CONTRACT, cloudId, contractId, i, null);
    		UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PICTURE, relativeUrl, accountId, MatchMode.Exact);
    		if (!uploadPolicyWrapper.isSuccess()) {
    			wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    			wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    			return wrapper;
    		}
    		UploadPolicyDTO policy = uploadPolicyWrapper.getUploadPolicy();
    		policyList.add(policy);
    	}
    	
    	String policyStr = JSON.toJSONString(policyList);
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    
    /**
     * 获取上传临时授权-邀请
     * <p>
     * 应用领域：邀请身份证照片存储。精确匹配KEY，只能用于当前邀请身份证照片上传
     * </p>
     *
     * @param cloudId 区域云ID
     * @param inviteId 邀请ID
     * @param phone 手机号
     * @param accountId 登陆ID
     * @return
     * @author wzq by 2019年1月2日 下午3:24:29
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "邀请ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 2, name = "手机号", rules = ValidateRules.NOT_NULL)
    public StringWrapper getInvitePutPolicy(String cloudId, String inviteId, String phone, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	// 相对路径
    	String relativeUrl = generatePictureRelativeUrl(PictureTypeEnum.INVITE, cloudId, inviteId, ZcarezeConstant.STATUS_1, phone);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PICTURE);
    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PICTURE, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-评价
     * <p>
     * 应用领域：评价照片存储。精确匹配KEY，只能用于当前评价照片上传
     * </p>
     *
     * @param cloudId 区域云ID
     * @param appraiseId 评价记录ID
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月2日 下午3:52:37
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "评价记录ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getAppraisePutPolicy(String cloudId, String appraiseId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PICTURE);
    	
    	List<UploadPolicyDTO> policyList = new ArrayList<>();
    	// 相对路径,签约三张授权
    	for (int i = 1; i <= 3; i++) {
    		String relativeUrl = generatePictureRelativeUrl(PictureTypeEnum.APPRAISE, cloudId, appraiseId, i, null);
    		UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PICTURE, relativeUrl, accountId, MatchMode.Exact);
    		if (!uploadPolicyWrapper.isSuccess()) {
    			wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    			wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    			return wrapper;
    		}
    		UploadPolicyDTO policy = uploadPolicyWrapper.getUploadPolicy();
    		policyList.add(policy);
    	}
    	
    	String policyStr = JSON.toJSONString(policyList);
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-健康事件ID
     * <p>
     * 应用领域：健康事件随访照片存储。精确匹配KEY，只能用于当前健康事件随访照片上传
     * </p>
     *
     * @param cloudId 区域云ID
     * @param rsdtReferralId 健康事件ID
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月2日 下午3:59:29
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "健康事件ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getReferralPutPolicy(String cloudId, String rsdtReferralId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PICTURE);
    	
    	List<UploadPolicyDTO> policyList = new ArrayList<>();
    	// 相对路径,签约两张张授权
    	for (int i = 1; i <= 2; i++) {
    		String relativeUrl = generatePictureRelativeUrl(PictureTypeEnum.REFERRAL, cloudId, rsdtReferralId, i, null);
    		UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PICTURE, relativeUrl, accountId, MatchMode.Exact);
    		if (!uploadPolicyWrapper.isSuccess()) {
    			wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    			wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    			return wrapper;
    		}
    		UploadPolicyDTO policy = uploadPolicyWrapper.getUploadPolicy();
    		policyList.add(policy);
    	}
    	
    	String policyStr = JSON.toJSONString(policyList);
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    
    /**
     * 获取上传临时授权-职员照片
     * <p>
     * 应用领域：头像库
     * </p>
     *
     * @param cloudId 区域云ID
     * @param staffId 职员ID
     * @return
     * @author wzq by 2019年1月2日 下午5:10:28
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "职员ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getPhotoPutPolicyByStaffId(String cloudId, String staffId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = generatePhotoRelativeUrl(PhotoTypeEnum.STAFF, cloudId, staffId);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PHOTO); 
    	
    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PHOTO, relativeUrl, staffId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-账户头像
     * <p>
     * 应用领域：头像库
     * </p>
     *
     * @param cloudId 区域云ID
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月3日 下午5:36:50
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "账户ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getPhotoPutPolicyByaccountId(String cloudId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = generatePhotoRelativeUrl(PhotoTypeEnum.ACCOUNT, cloudId, accountId);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PHOTO);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PHOTO, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-组织机构介绍照片
     * <p>
     * 应用领域：头像库
     * </p>
     *
     * @param cloudId 区域云ID
     * @param orgId 组织ID
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月3日 下午5:38:03
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "组织ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getPhotoPutPolicyByOrgId(String cloudId, String orgId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	StringBuilder builder = new StringBuilder();
    	builder.append("org");
    	builder.append(Constants.SPLIT);
    	builder.append(cloudId);
    	builder.append(Constants.SPLIT);
    	builder.append(orgId);
    	builder.append(Suffix.JPEG);
    	String relativeUrl = builder.toString();
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.KNOW, relativeUrl, accountId, MatchMode.Exact);
    	if (uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-居民预约上传的病情图片
     * <p>
     * 应用领域：健康文档库
     * </p>
     *
     * @param cloudId 区域云ID
     * @param residentId 居民ID
     * @param rsdtPreBookId 居民预约ID
     * @param photoIndex 图片个数
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月3日 下午5:43:03
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "居民ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 2, name = "居民预约ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 3, name = "图片个数", rules = ValidateRules.NOT_NULL)
    public StringWrapper getPhotoPutPolicyByRsdtPreBookId(String cloudId, String residentId, String rsdtPreBookId, 
    		Integer photoIndex, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	StringBuilder builder = new StringBuilder();
    	builder.append(cloudId);
    	builder.append(Constants.SPLIT);
    	builder.append("xml");
    	builder.append(Constants.SPLIT);
    	builder.append(DateUtil.getDateTime("yyyy", new Date()));
    	builder.append(Constants.SPLIT);
    	builder.append(residentId);
    	builder.append(Constants.SPLIT);
    	builder.append(rsdtPreBookId);
    	builder.append(Constants.SPLIT);
    	builder.append(photoIndex);
    	builder.append(Suffix.JPEG);
    	String relativeUrl = builder.toString();
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.HDOC);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.HDOC, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-问题插图
     * <p>
     * 应用领域：问卷
     * </p>
     *
     * @param cloudId
     * @param qnaId
     * @param answerCode
     * @param loginId
     * @return
     * @author wzq by 2019年1月2日 下午5:56:15
     */
    @ParamentValidate(index = 0, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "问卷ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 2, name = "问题编号", rules = ValidateRules.NOT_NULL)
    public StringWrapper getQnaFigurePutPolicy(String cloudId, String qnaId, String answerCode, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = generateQnaRelativeUrl(QnaTypeEnum.FIGURE, cloudId, qnaId, answerCode);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.KNOW, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	wrapper.setStrValue(JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy()));
    	return wrapper;
    }
    /**
     * 获取上传授权-公共问卷图标、问卷图标
     * <p> 
     * 应用领域：问卷
     * </p>
     *
     * @param qnaTypeEnum 图标类型
     * @param cloudId 区域云ID
     * @param qnaId 问卷ID
     * @param accountId 账户ID
     * @return
     * @author wangziqin by 2019年2月21日 上午10:44:41
     */
    @ParamentValidate(index = 0, name = "图标类型", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "区域云ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 2, name = "问卷ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getQnaIconPutPolicy(QnaTypeEnum qnaTypeEnum, String cloudId, String qnaId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 只能选择图标类型
    	if (!qnaTypeEnum.equals(QnaTypeEnum.ICON) && !qnaTypeEnum.equals(QnaTypeEnum.PUBLIC_ICON)) {
    		wrapper.setError("只能选择图标类型");
    		return wrapper;
    	}
    	
    	// 相对路径
    	String relativeUrl = generateQnaRelativeUrl(qnaTypeEnum, cloudId, qnaId, null);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.KNOW, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	wrapper.setStrValue(JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy()));
    	return wrapper;
    }
    /**
     * 获取上传临时授权- 图文(网页)；音频；视频
     * <p>
     * 应用领域：文章 
     * </p>
     *
     * @param articleTypeEnum 类型   图文(网页)；音频；视频
     * @param articleId 文章ID
     * @return
     * @author wzq by 2019年1月3日 上午10:05:55
     */
    @ParamentValidate(index = 0, name = "文章类型", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "文章ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getArticlePutPolicy(ArticleTypeEnum articleTypeEnum, String articleId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = generateArticleRelativeUrl(articleTypeEnum, articleId);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);

    	// 音频、视频均为精准拼配，图文为模糊匹配
    	MatchMode matchMode = MatchMode.Exact;
    	if (articleTypeEnum.equals(ArticleTypeEnum.PIC_KNOW)) {
    		matchMode = MatchMode.StartWith;
    	}
    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.KNOW, relativeUrl, accountId, matchMode);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-文章
     * <p>
     * 应用领域：
     * </p>
     *
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月3日 下午3:27:34
     */
    public StringWrapper getArticlePutPolicyOld(String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = "article/";
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.KNOW);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.KNOW, relativeUrl, accountId, MatchMode.StartWith);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    
    /**
     * 获取上传临时授权-分诊症状简图
     * <p>
     * 应用领域：
     * </p>
     *
     * @param cloudId 区域ID
     * @param symptomId 分诊症状ID
     * @param loginId 登陆ID
     * @return
     * @author wzq by 2019年1月3日 下午4:37:56
     */
    @ParamentValidate(index = 0, name = "区域ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "分诊症状ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 2, name = "登陆ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getTriageSymptomPutPolicy(String cloudId, Integer symptomId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = generateTriageRelativeUrl(TriageTypeEnum.SYMPTOM, cloudId, symptomId);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.BIZ, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
    }
    /**
     * 获取上传临时授权-分诊自诊步骤简图
     * <p>
     * 应用领域：
     * </p>
     *
     * @param cloudId 区域云ID
     * @param stepId 分诊自测步骤ID
     * @param accountId 账户ID
     * @return
     * @author wzq by 2019年1月3日 下午4:51:56
     */
    @ParamentValidate(index = 0, name = "区域ID", rules = ValidateRules.NOT_NULL)
    @ParamentValidate(index = 1, name = "分诊自诊步骤ID", rules = ValidateRules.NOT_NULL)
    public StringWrapper getTriageStepPutPolicy(String cloudId, Integer stepId, String accountId) {
    	StringWrapper wrapper = new StringWrapper();
    	
    	// 相对路径
    	String relativeUrl = generateTriageRelativeUrl(TriageTypeEnum.STEP, cloudId, stepId);
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);

    	UploadPolicyWrapper uploadPolicyWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.BIZ, relativeUrl, accountId, MatchMode.Exact);
    	if (!uploadPolicyWrapper.isSuccess()) {
    		wrapper.setErrcode(uploadPolicyWrapper.getErrcode());
    		wrapper.setErrmsg(uploadPolicyWrapper.getErrmsg());
    		return wrapper;
    	}
    	
    	String policyStr = JSON.toJSONString(uploadPolicyWrapper.getUploadPolicy());
    	wrapper.setStrValue(policyStr);
    	return wrapper;
	}

	/**
	 * 获取上传临时授权-应用资源
	 * <p>
	 * 应用领域：首页配置-图片
	 * </p>
	 *
	 * @param homepageKeyEnum 客户端类型
	 * @param homepageId      首页ID
	 * @param cloudId         区域云ID
	 * @return
	 * @author wangziqin by 2019年1月9日 下午3:33:27
	 */
	@ParamentValidate(name = "客户端类型", index = 0, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "首页ID", index = 0, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "区域云ID", index = 0, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "登陆ID", index = 0, rules = ValidateRules.NOT_NULL)
	public UploadPolicyWrapper getHomepageResIconFile(HomepageKeyEnum homepageKeyEnum, String homepageId,
			String cloudId, String loginId) {

		// 相对路径
		StringBuilder builder = new StringBuilder(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(RsrcType.HOMEPAGERES);
		builder.append(Constants.SPLIT);
		builder.append(homepageKeyEnum.getValue());
		builder.append(Constants.SPLIT);
		builder.append(homepageId);
		builder.append(Constants.SPLIT);
		builder.append("icon");
		builder.append(Constants.SPLIT);
		String relativeUrl = builder.toString();

		StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);

		return instance.getUploadTemplatePolicy(FileTypeEnum.RSRC, relativeUrl, loginId, MatchMode.StartWith);
	}

	/**
	 * 获取上传临时授权-业务库
	 * <p>
	 * 应用领域：即时通讯
	 * </p>
	 *
	 * @param cloudId
	 * @param serviceId
	 * @param loginId
	 * @return
	 * @author chenrj by 2019年1月11日 下午8:01:57
	 */
	@ParamentValidate(name = "区域云ID", index = 0, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "业务ID", index = 1, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "登陆ID", index = 2, rules = ValidateRules.NOT_NULL)
	public StringWrapper getChatPutTemplatePolicy(String cloudId, String serviceId, String loginId) {
		StringWrapper wrapper = new StringWrapper();

		// 相对路径
		StringBuilder builder = new StringBuilder();
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(BizType.CHAT);
		builder.append(Constants.SPLIT);
		builder.append(DateUtil.getDateTime("yyyyMM", new Date()));
		builder.append(Constants.SPLIT);
		builder.append(serviceId);
		builder.append(Constants.SPLIT);
		String relativeUrl = builder.toString();

		StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);
		UploadPolicyWrapper uploadWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.BIZ, relativeUrl, loginId,
				MatchMode.StartWith);
		if (!uploadWrapper.isSuccess()) {
			wrapper.setErrcode(uploadWrapper.getErrcode());
			wrapper.setErrmsg(uploadWrapper.getErrmsg());
			return wrapper;
		}

		wrapper.setStrValue(JSON.toJSONString(uploadWrapper.getUploadPolicy()));
		return wrapper;
	}

	/**
	 * 获取上传临时授权-图片库
	 * <p>
	 * 应用领域：居民档案图片
	 * </p>
	 *
	 * @param cloudId    区域云ID
	 * @param residentId 居民ID
	 * @param accountId  账户ID
	 * @return
	 * @author wangziqin by 2019年1月21日 下午3:55:52
	 */
	@ParamentValidate(name = "区域ID", index = 0, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "居民ID", index = 1, rules = ValidateRules.NOT_NULL)
	@ParamentValidate(name = "账户ID", index = 2, rules = ValidateRules.NOT_NULL)
	public StringWrapper getPortraitUrlTemplatePolicy(String cloudId, String residentId, String accountId) {
		StringWrapper wrapper = new StringWrapper();
		StringBuilder builder = new StringBuilder();
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(PictureTypeEnum.PORTRAITURL.getCode());
		builder.append(Constants.SPLIT);
		builder.append(DateUtil.getDateTime("yyyyMM", new Date()));
		builder.append(Constants.SPLIT);
		builder.append(residentId);
		builder.append(PictureTypeEnum.PORTRAITURL.getSuffix());
		String relativeUrl = builder.toString();

		StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.PICTURE);
		UploadPolicyWrapper uploadWrapper = instance.getUploadTemplatePolicy(FileTypeEnum.PICTURE, relativeUrl,
				accountId, MatchMode.Exact);
		if (!uploadWrapper.isSuccess()) {
			wrapper.setErrcode(uploadWrapper.getErrcode());
			wrapper.setErrmsg(uploadWrapper.getErrmsg());
			return wrapper;
		}

		wrapper.setStrValue(JSON.toJSONString(uploadWrapper.getUploadPolicy()));
		return wrapper;
	}
	/**
	 * 获取上传临时授权-健康指导预录音频
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param cloudId    区域ID
	 * @param guidanceId 指导ID
	 * @param accountId  账户ID
	 * @return
	 * @author wzq by 2019年1月3日 下午3:53:21
	 */
	@ParamentValidate(index = 0, name = "区域ID", rules = ValidateRules.NOT_NULL)
	@ParamentValidate(index = 1, name = "指导ID", rules = ValidateRules.NOT_NULL)
	public UploadPolicyWrapper getGuidancePutPolicy(String cloudId, String guidanceId, String accountId) {

		// 相对路径
		String relativeUrl = generateGuidanceRelativeUrl(cloudId, guidanceId);

		StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);

		return instance.getUploadTemplatePolicy(FileTypeEnum.BIZ, relativeUrl, accountId, MatchMode.Exact);
	}

	/**
	 * 获取上传临时授权-干预项目图标
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param cloudId    区域ID
	 * @param itemId     项目ID
	 * @param accountId  账户ID
	 * @return
	 * @author wzq by 2019年2月13日 下午3:53:21
	 */
	@ParamentValidate(index = 0, name = "区域ID", rules = ValidateRules.NOT_NULL)
	@ParamentValidate(index = 1, name = "项目ID", rules = ValidateRules.NOT_NULL)
	public StringWrapper getReferralItemPutPolicy(String cloudId, String itemId, String accountId) {
		StringWrapper wrapper = new StringWrapper();
		
		// 相对路径
		StringBuilder builder = new StringBuilder();
		builder.append(cloudId);
		builder.append(Constants.SPLIT);
		builder.append(BizType.REFERRAL_ITEM);
		builder.append(Constants.SPLIT);
		builder.append(itemId);
		builder.append(".jpeg");
		String relativeUrl = builder.toString();

		StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);

		UploadPolicyWrapper policyWrapper =  instance.getUploadTemplatePolicy(FileTypeEnum.BIZ, relativeUrl, accountId, MatchMode.Exact);
		if (!policyWrapper.isSuccess()) {
			wrapper.setErrcode(policyWrapper.getErrcode());
			wrapper.setErrmsg(policyWrapper.getErrmsg());
			return wrapper;
		}
		wrapper.setStrValue(JSON.toJSONString(policyWrapper.getUploadPolicy()));
		return wrapper;
	}
	/**
     * 获取上传临时授权-应用资源
     * <p>
     * 应用领域：首页配置-JSON文件
     * </p>
     *
     * @param homepageKeyEnum 客户端类型
     * @param homepageId 首页ID
     * @param cloudId 区域云ID
     * @return
     * @author wangziqin by 2019年1月9日 下午3:26:02
     */
    @ParamentValidate(name = "客户端类型", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "首页ID", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "区域云ID", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "登陆ID", index = 0, rules = ValidateRules.NOT_NULL)
    public UploadPolicyWrapper getHomepageResJSONFile(HomepageKeyEnum homepageKeyEnum, String homepageId, String cloudId, 
    		String loginId) {
    	
    	// 相对路径
    	StringBuilder builder = new StringBuilder(cloudId);
    	builder.append(Constants.SPLIT);
    	builder.append(RsrcType.HOMEPAGERES);
    	builder.append(Constants.SPLIT);
    	builder.append(homepageKeyEnum.getValue());
    	builder.append(Constants.SPLIT);
    	builder.append(homepageId);
    	builder.append(Constants.SPLIT);
    	builder.append(homepageId);
    	builder.append(".json");
    	String relativeUrl = builder.toString();
    	
    	StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);

    	return instance.getUploadTemplatePolicy(FileTypeEnum.RSRC, relativeUrl, loginId, MatchMode.Exact);
    }
    // 上传临时授权-end 
    
    // 服务端上传-start
    /**
     * 服务端上传-业务库
     * <p>
     * 应用领域：电子签名
     * </p>
     *
     * @param cloudId
     * @param instructionsId
     * @param recordId
     * @param signSuffixEnum
     * @author wangziqin by 2019年1月15日 下午5:04:56
     */
    @ParamentValidate(name = "上传内容", index = 0, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "区域ID", index = 1, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "指令ID", index = 2, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "指令变更记录ID", index = 3, rules = ValidateRules.NOT_NULL)
    @ParamentValidate(name = "后缀名", index = 4, rules = ValidateRules.NOT_NULL)
    public StringWrapper uploadSignFile(String uploadContent, String cloudId, String instructionsId, 
    		String recordId, SignSuffixEnum signSuffixEnum) {
    	
    	StringBuilder builder = new StringBuilder();
    	builder.append(cloudId);
    	builder.append(Constants.SPLIT);
    	builder.append(BizType.SIGN);
    	builder.append(Constants.SPLIT);
    	builder.append(DateUtil.getDateTime("yyyyMM", new Date()));
    	builder.append(Constants.SPLIT);
    	builder.append(instructionsId);
    	builder.append(Constants.SPLIT);
    	builder.append(recordId);
    	builder.append(signSuffixEnum.getValue());
    	// 相对路径
    	String relativeUrl = builder.toString();
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.BIZ);
        StringWrapper wrapper = instance.uploadObject(FileTypeEnum.BIZ, uploadContent, relativeUrl);
        return wrapper;
    }
    // 服务端上传-end
    
    // 读取文件内容-start
    /**
     * 读取应用资源库文件内容
     * <p>
     * 应用领域：首页配置
     * </p>
     *
     * @param relativeUrl
     * @return
     * @author wangziqin by 2019年1月15日 下午5:34:19
     */
    public StringWrapper getRsrcFileContent(String relativeUrl) {
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.RSRC);
        StringWrapper wrapper = instance.getObjectContent(FileTypeEnum.RSRC, relativeUrl);
        return wrapper;
	}
    // 读取文件内容-end
    
    // 删除文件-start
    /**
     * 删除阿里云语音文件
     * <p>
     * 应用领域：
     * </p>
     *
     * @param relativeUrl 文件相对路径
     * @return
     * @author wzq by 2019年1月3日 下午4:58:20
     */
    @ParamentValidate(name = "文件路径", index = 0, rules = ValidateRules.NOT_NULL)
    public ResponseWrapper delAudioByRelativeUrl(String relativeUrl) {
    	// 相对路径开头不能为斜杠 
    	relativeUrl = checkRelativeUrl(relativeUrl);
    	
    	// 获取存储实例
        StoreInstance instance = FileInstanceFactory.getInstance(FileTypeEnum.AUDIO);
        ResponseWrapper wrapper = instance.delObject(FileTypeEnum.AUDIO, relativeUrl);
        return wrapper;
    }
    // 删除文件-end
}
