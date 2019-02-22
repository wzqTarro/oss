/**
 * zcareu Inc
 * Copyright (C) 2018 ALL Right Reserved
 */
package com.zcareze.biz.file.service.storemode;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zcareze.biz.file.service.dto.AssumeRoleDTO;
import com.zcareze.biz.file.service.dto.UploadPolicyDTO;
import com.zcareze.biz.file.service.dto.VisitUrlDTO;
import com.zcareze.biz.file.service.enst.Constants;
import com.zcareze.biz.file.service.enst.DeploymentEnvironment;
import com.zcareze.biz.file.service.enst.FileTypeEnum;
import com.zcareze.biz.file.service.wrapper.CheckObjectIsExistsWrapper;
import com.zcareze.biz.file.service.wrapper.FileContentListWrapper;
import com.zcareze.biz.file.service.wrapper.UploadPolicyWrapper;
import com.zcareze.biz.file.service.wrapper.VisitUrlWrapper;
import com.zcareze.commons.file.FileUtils;
import com.zcareze.commons.properties.PropertiesUtils;
import com.zcareze.commons.result.ResponseWrapper;
import com.zcareze.commons.result.StringWrapper;
import com.zcareze.commons.result.WrapperErrEunm;
import com.zcareze.commons.utils.StringUtils;

/**
 * @author lveliu
 * @description 阿里云OSS存储实现
 * @date 2018-11-14
 */
public class AliOssImpl implements StoreInstance {

	// sts endpoint
	private static final String STS_ENDPOINT = "sts.aliyuncs.com";
	
	// 外网访问地址
	private static final String ENDPOINT = PropertiesUtils.getStringProperties("oss.endpoint");
	
	// 用户标识
	private static final String ACCESS_KEY_ID  = PropertiesUtils.getStringProperties("access.id");
	
	// 用户密钥
	private static final String ACCESS_KEY_SECRET = PropertiesUtils.getStringProperties("access.key");
	
	// 角色标识（RoleArn 需要在 RAM 控制台上获取）
	private static final String ROLE_ARN = PropertiesUtils.getStringProperties("oss.roleArn");
	
	// 环境
	private static final String ENVIRONMENT = PropertiesUtils.getStringProperties("oss.deploymentEnvironment");
	
	// OSS签名过期时长。单位：毫秒，一小时
	private static final long EXPIRE_TIME = 1000 * 60 * 60 * 1;
	
	// OSS上传文件大小。10M
	private static final long FILE_MAXIMUM = 1024 * 1024 * 10;
	
	// OSS的STS授权过期时长。单位：秒，1小时
	private static long STS_EXPIRE_TIME = 60 * 60;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AliOssImpl.class);
	
	/**
	 * 判断当前环境
	 * @param fileTypeEnum
	 * @return 存储空间名
	 */
	private String checkEnvironment(FileTypeEnum fileTypeEnum) {
		// 本地开发
		if (DeploymentEnvironment.LOCAL_DEVELOPMENT_ENVIRONMENT.equals(ENVIRONMENT)) {
			return fileTypeEnum.getTestBucketName();
		
		// 线上测试
		} else if (DeploymentEnvironment.ONLINE_TEST_ENVIRONMENT.equals(ENVIRONMENT)) {
			return fileTypeEnum.getOnlineTestBucketName();
			
		// 正式
		} else if (DeploymentEnvironment.ONLINE_OFFICIAL_ENVIRONMENT.equals(ENVIRONMENT)) {
			return fileTypeEnum.getBucketName();
		}
		return fileTypeEnum.getTestBucketName();
	}
	
	private String getDomainName(FileTypeEnum fileTypeEnum) {
		// 本地开发
		if (DeploymentEnvironment.LOCAL_DEVELOPMENT_ENVIRONMENT.equals(ENVIRONMENT)) {
			return fileTypeEnum.getTestDomainName();

			// 线上测试
		} else if (DeploymentEnvironment.ONLINE_TEST_ENVIRONMENT.equals(ENVIRONMENT)) {
			return fileTypeEnum.getOnlineTestDomainName();

			// 正式
		} else if (DeploymentEnvironment.ONLINE_OFFICIAL_ENVIRONMENT.equals(ENVIRONMENT)) {
			return fileTypeEnum.getDomainName();
		}
		return fileTypeEnum.getTestDomainName();
	}
	
	private String getUrlPrefix() {
		String prefix = "http://";
		if (DeploymentEnvironment.ONLINE_OFFICIAL_ENVIRONMENT.equals(ENVIRONMENT)) {
			prefix = "https://";
		}
		return prefix;
	}
	
	/**
	 * 组装文件路径
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param domainName
	 * @param fileName
	 * @return
	 * @author wzq by 2018年12月24日 下午6:09:57
	 */
	private StringWrapper buildPath(String domainName, String fileName) {
		StringWrapper wrapper = new StringWrapper();
		if (StringUtils.isNotBlank(fileName) && !fileName.contains("http")) {
			StringBuilder sb = new StringBuilder();
			if (!fileName.substring(0, 1).equals("/")) {
				fileName = "/" + fileName;
			}
			sb.append(getUrlPrefix()).append(domainName).append(fileName);
			wrapper.setStrValue(sb.toString());
			return wrapper;
		}
		wrapper.setStrValue(fileName);
		return wrapper;
	}
	
    @Override
    public VisitUrlWrapper getVisitUrl(FileTypeEnum fileTypeEnum, String relativeUrl, String accountId) {
    	VisitUrlWrapper wrapper = new VisitUrlWrapper();
    	VisitUrlDTO visitUrl = new VisitUrlDTO();
    	String urlStr = null;
    
		String bucketName = checkEnvironment(fileTypeEnum);

		if (DeploymentEnvironment.PRIVATE.equals(fileTypeEnum.getBucketPolicy())) {
			if (StringUtils.isBlank(relativeUrl)) {
				wrapper.setError("路径不能为空");
				return wrapper;
			}
			
			AssumeRoleDTO assumeRole = getOssStsAssumeRole(relativeUrl, accountId);

			if (assumeRole == null) {
				wrapper.setError("授权失败");
				return wrapper;
			}

			OSSClient ossClient = new OSSClient(ENDPOINT, assumeRole.getAccessId(), assumeRole.getAccessKeyScret(),
					assumeRole.getSecurityToken());

			// 设置URL过期时间为1小时。
			Date expiration = new Date(new Date().getTime() + EXPIRE_TIME);

			visitUrl.setExpire(expiration);

			// 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
			URL url = ossClient.generatePresignedUrl(bucketName, relativeUrl, expiration);

			urlStr = url.toString();

			// 关闭OSSClient。
			ossClient.shutdown();
		} else {
			String domainName = null;

			// 本地开发
			if (DeploymentEnvironment.LOCAL_DEVELOPMENT_ENVIRONMENT.equals(ENVIRONMENT)) {
				domainName = fileTypeEnum.getTestDomainName();

				// 线上测试
			} else if (DeploymentEnvironment.ONLINE_TEST_ENVIRONMENT.equals(ENVIRONMENT)) {
				domainName = fileTypeEnum.getOnlineTestDomainName();

				// 正式
			} else if (DeploymentEnvironment.ONLINE_OFFICIAL_ENVIRONMENT.equals(ENVIRONMENT)) {
				domainName = fileTypeEnum.getDomainName();
			}
			String http = getUrlPrefix();
			StringBuffer url = new StringBuffer(http);
			url.append(domainName);
			url.append(Constants.SPLIT);
			if (StringUtils.isNotBlank(relativeUrl)) {
				url.append(relativeUrl);
			}
			urlStr = url.toString();
		}
		// 如果是正式库
		if (DeploymentEnvironment.ONLINE_OFFICIAL_ENVIRONMENT.equals(ENVIRONMENT)) {
			if (!urlStr.startsWith("https")) { 
				urlStr = urlStr.replace("http", "https");
			}
		}
		// 本身为全路径
		if (StringUtils.isNotBlank(relativeUrl) && relativeUrl.contains("http")) {
			urlStr = relativeUrl;
		}
    	visitUrl.setOssUrl(urlStr);
    	wrapper.setVisitUrlDTO(visitUrl);
    	return wrapper;
    }
    @Override
    public CheckObjectIsExistsWrapper checkObjectIsExists(FileTypeEnum fileTypeEnum, String relativeUrl) {
    	String bucketName = checkEnvironment(fileTypeEnum);
		
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		boolean objectExist = ossClient.doesObjectExist(bucketName, relativeUrl);
		
		CheckObjectIsExistsWrapper wrapper = new CheckObjectIsExistsWrapper();
		wrapper.setExists(objectExist);
		return wrapper;
    }
    
	@Override
	public StringWrapper uploadObject(FileTypeEnum fileTypeEnum, Object object, String uploadUrl) {
		StringWrapper wrapper = new StringWrapper();
		String bucketName = checkEnvironment(fileTypeEnum);
		
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		/*boolean objectExist = ossClient.doesObjectExist(bucketName, uploadUrl);
		if (objectExist == true) {
			wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
			wrapper.setErrmsg("上传路径对象已存在");
			return wrapper;
		}*/
		
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setLastModified(new Date());
		objectMeta.setContentEncoding("UTF-8");
		if (object instanceof File) { 
			
			// 上传文件	
			File file = (File) object;

			objectMeta.setContentLength(file.length());
			objectMeta.setLastModified(new Date(file.lastModified()));
			ossClient.putObject(bucketName, uploadUrl, file, objectMeta);
		} else if (object instanceof String) {
			
			// 上传字符串
			String content = (String) object;
			try {
				ossClient.putObject(bucketName, uploadUrl, new ByteArrayInputStream(content.getBytes("UTF-8")), objectMeta);
			} catch (OSSException | com.aliyun.oss.ClientException | UnsupportedEncodingException e) {
				LOGGER.error("获取内容utf-8编码格式的byte数组失败", e);
				wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
				wrapper.setErrmsg("上传失败");
				return wrapper;
			}
		} else if (object instanceof byte[]) {
			
			// 上传Byte数组
			byte[] content = (byte[]) object;
			ossClient.putObject(bucketName, uploadUrl, new ByteArrayInputStream(content), objectMeta);
		} else if (object instanceof InputStream) {
			
			// 上传网络或文件流
			InputStream content = (InputStream) object;
			ossClient.putObject(bucketName, uploadUrl, content, objectMeta);
		}
		
		// 关闭OSSClient
		ossClient.shutdown();
		wrapper.setStrValue(uploadUrl);
		return wrapper;
	}
	
	@Override
	public StringWrapper getObjectContent(FileTypeEnum fileTypeEnum, String objectUrl) {
		StringWrapper wrapper = new StringWrapper();
		String bucketName = checkEnvironment(fileTypeEnum);
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		OSSObject ossObject = ossClient.getObject(bucketName, objectUrl);
		BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
		StringBuilder inputLine = new StringBuilder();
		String s = null;
		try {
			while ((s = reader.readLine()) != null) {
				inputLine.append(s);
			}
		} catch (IOException e) {
			LOGGER.error("读取内容失败", e);
			wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
			wrapper.setErrmsg("获取失败");
			return wrapper;
		}
		wrapper.setStrValue(inputLine.toString());
		return wrapper;
	}
	
	@Override
	public FileContentListWrapper getFileLineContent(FileTypeEnum fileTypeEnum, String objectUrl) {
		FileContentListWrapper wrapper = new FileContentListWrapper();
		String bucketName = checkEnvironment(fileTypeEnum);
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		OSSObject ossObject = ossClient.getObject(bucketName, objectUrl);
		List<String> fileContent = null;
		try {
			fileContent = FileUtils.readLines(ossObject.getObjectContent());
		} catch (IOException e) {
			LOGGER.error("读取内容失败", e);
			wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
			wrapper.setErrmsg("获取失败");
			return wrapper;
		}
		wrapper.setFileContent(fileContent);
		return wrapper;
	}
	
	@Override
	public ResponseWrapper downloadObject(FileTypeEnum fileTypeEnum, String objectUrl, File downFile) {
		String bucketName = checkEnvironment(fileTypeEnum);
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		ossClient.getObject(new GetObjectRequest(bucketName, objectUrl), downFile);
		return new ResponseWrapper();
	}
	
	@Override
	public ResponseWrapper delObject(FileTypeEnum fileTypeEnum, String relativeUrl) {
		String bucketName = checkEnvironment(fileTypeEnum);
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

		// 删除文件。
		ossClient.deleteObject(bucketName, relativeUrl);

		// 关闭OSSClient。
		ossClient.shutdown();	
		return new ResponseWrapper();
	}
	
	@Override
	public ResponseWrapper copyObject(FileTypeEnum sourceFileTypeEnum, FileTypeEnum targetFileTypeEnum,
			String sourceRelativeUrl, String targetRelativeUrl) {
		// 原对象存储空间
		String bucketName = checkEnvironment(sourceFileTypeEnum);
		
		// 复制后对象存储空间
		String targetBucketName = checkEnvironment(targetFileTypeEnum);
				
		// 创建OSSClient实例
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

		// 复制文件。
		ossClient.copyObject(bucketName, sourceRelativeUrl, targetBucketName, targetRelativeUrl);

		// 关闭OSSClient。
		ossClient.shutdown();	
		return new ResponseWrapper();
	}

	@Override
	public StringWrapper moveObject(FileTypeEnum sourceFileTypeEnum, FileTypeEnum targetFileTypeEnum, 
			String sourceRelativeUrl, String targetRelativeUrl) {
		StringWrapper wrapper = new StringWrapper();
		
		// 目标存储空间
		String targetSpaceName = checkEnvironment(targetFileTypeEnum);
		
		// 原存储空间
		String sourceSpaceName = checkEnvironment(sourceFileTypeEnum);
		
		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		
		// 原对象是否存在
		boolean objectExist = ossClient.doesObjectExist(sourceSpaceName, sourceRelativeUrl);
		if (objectExist == false) {
			wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
			wrapper.setErrmsg("原对象不存在");
			return wrapper;
		}
		
		// 修改后路径是否存在对象
		objectExist = ossClient.doesObjectExist(targetSpaceName, targetRelativeUrl);
		if (objectExist) {
			wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
			wrapper.setErrmsg("修改后路径已存在对象");
			return wrapper;
		}
		
		// 拷贝文件。
		copyObject(sourceFileTypeEnum, targetFileTypeEnum, sourceRelativeUrl, targetRelativeUrl);
		
		// 删除原文件
		delObject(sourceFileTypeEnum, sourceRelativeUrl);
		
		// 关闭OSSClient。
		ossClient.shutdown();
		wrapper.setStrValue(targetRelativeUrl);
		return wrapper;
	}

	/**
	 * STS临时授权
	 * <p>
	 * 应用领域：
	 * </p>
	 *
	 * @param fileUrl
	 * @return
	 * @author wzq by 2018年12月24日 下午9:19:13
	 */
	private AssumeRoleDTO getOssStsAssumeRole(String fileUrl, String accountId) {
		if (StringUtils.isBlank(fileUrl)) {
			return null;
		}
		// 权限配置
		StringBuffer policyBuf = new StringBuffer("{\n");
		policyBuf.append("\"Version\": \"1\", \n");
		policyBuf.append("\"Statement\": [\n");
		policyBuf.append("{\n");
		policyBuf.append("\"Action\": [\n");
		policyBuf.append("\"oss:*\"\n");
		policyBuf.append("], \n");
		policyBuf.append("\"Resource\": [\n");
		policyBuf.append("\"acs:oss:*:*:*\"\n");
		policyBuf.append("], \n");
		policyBuf.append("\"Effect\": \"Allow\"\n");
		policyBuf.append("}\n");
		policyBuf.append("]\n");
		policyBuf.append("}");
		String policy = policyBuf.toString();

		try {
			// 添加endpoint
			DefaultProfile.addEndpoint("", "", "Sts", STS_ENDPOINT);

			// 构造default profile
			IClientProfile profile = DefaultProfile.getProfile("", ACCESS_KEY_ID, ACCESS_KEY_SECRET);

			// 用profile构造client
			DefaultAcsClient client = new DefaultAcsClient(profile);
			final AssumeRoleRequest request = new AssumeRoleRequest();
			request.setMethod(MethodType.POST);
			request.setRoleArn(ROLE_ARN);
			request.setProtocol(ProtocolType.HTTPS);
			request.setAcceptFormat(FormatType.JSON);

			if (StringUtils.isBlank(accountId)) {
				accountId = StringUtils.getUUID();
			}
			// roleSessionName区别不同的token, 需要2个或2个以上的字符
			request.setRoleSessionName(accountId);

			// 设置过期时间
			request.setDurationSeconds(STS_EXPIRE_TIME);

			// 授权配置
			request.setPolicy(policy);

			final AssumeRoleResponse response = client.getAcsResponse(request);
			AssumeRoleDTO assumeRole = new AssumeRoleDTO(); 
			assumeRole.setAccessId(response.getCredentials().getAccessKeyId());
			assumeRole.setAccessKeyScret(response.getCredentials().getAccessKeySecret());
			assumeRole.setSecurityToken(response.getCredentials().getSecurityToken());
			assumeRole.setExpireEndTime(response.getCredentials().getExpiration());
			return assumeRole;
		
		} catch (ClientException e) {
			LOGGER.error("临时授权获取失败", e);
			return null;
		}
	}
	
	@Override
	public UploadPolicyWrapper getUploadTemplatePolicy(FileTypeEnum fileTypeEnum, String fileUrl, String loginId, MatchMode matchMode){
		UploadPolicyWrapper wrapper = new UploadPolicyWrapper();
		
		//AssumeRoleDTO assumeRole = getOssStsAssumeRole(fileUrl, loginId);
		
		/*if (assumeRole == null) {
			wrapper.setErrcode(WrapperErrEunm.ERR_MESSAGE.code());
			wrapper.setErrmsg("授权失败");
			return wrapper;
		}*/
		
		// 临时访问密钥
		//String accessKeyId = assumeRole.getAccessId();
		//String accessKeySecret = assumeRole.getAccessKeyScret();
		
		// 安全令牌
		//String securityToken = assumeRole.getSecurityToken();
		
		// 用户拿到STS临时凭证后，通过其中的安全令牌（SecurityToken）和临时访问密钥（AccessKeyId和AccessKeySecret）生成OSSClient。
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		
		// 设置URL过期时间为1小时。
		long expireTime = System.currentTimeMillis() + EXPIRE_TIME;
		Date expiration = new Date(expireTime);

		PolicyConditions policyConds = new PolicyConditions();
		
		// 限制文件大小
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, FILE_MAXIMUM);
        
        // 限制路径
        if (StringUtils.isNotBlank(fileUrl)) {
        	policyConds.addConditionItem(matchMode, PolicyConditions.COND_KEY, fileUrl);
        }
        
        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
		byte[] binaryData = null;
		try {
			binaryData = postPolicy.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("上传授权权限配置转byte数组失败", e);
		}
		String encodedPolicy = BinaryUtil.toBase64String(binaryData);
		String postSignature = ossClient.calculatePostSignature(postPolicy);

		String host = getUrlPrefix() + getDomainName(fileTypeEnum);
		
		/*Map<String, String> respMap = new LinkedHashMap<String, String>();
		respMap.put("accessid", accessKeyId);
		respMap.put("policy", encodedPolicy);
		respMap.put("signature", postSignature);
		respMap.put("host", host);
		respMap.put("expire", String.valueOf(expireTime));
		respMap.put("bucket", bucketName);*/
        
		UploadPolicyDTO uploadPolicy = new UploadPolicyDTO();
		uploadPolicy.setAccessid(ACCESS_KEY_ID);
		uploadPolicy.setExpire(String.valueOf(expireTime));
		uploadPolicy.setFileKey(fileUrl);
		uploadPolicy.setHost(host);
		uploadPolicy.setPolicy(encodedPolicy);
		uploadPolicy.setSignature(postSignature);
		
		// 关闭OSSClient。
		ossClient.shutdown();

		wrapper.setUploadPolicy(uploadPolicy);
		return wrapper;
	}
	
	@Override
	public List<String> getDirectoryList(FileTypeEnum fileTypeEnum, String... directorys) {
		String bucketName = checkEnvironment(fileTypeEnum);
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		StringBuffer prefix = new StringBuffer();
		for (String directory : directorys) {
			prefix.append(directory).append("/");
		}
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(prefix.toString());
		ObjectListing listing = ossClient.listObjects(listObjectsRequest);
		List<String> commonPrefixs = new ArrayList<>();
		for (String commonPrefix : listing.getCommonPrefixes()) {
			String path = commonPrefix.replace(prefix.toString(), "");
			if (StringUtils.isNotEmpty(path)) {
				path = path.replace("/", "");
				commonPrefixs.add(path);
			}
		}
		return commonPrefixs;
	}
	
	@Override
	public List<String> getDirFileList(FileTypeEnum fileTypeEnum, String... directorys) {
		String bucketName = checkEnvironment(fileTypeEnum);
		OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
		StringBuffer prefix = new StringBuffer();
		for (String directory : directorys) {
			prefix.append(directory).append("/");
		}
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
		listObjectsRequest.setDelimiter("/");
		listObjectsRequest.setPrefix(prefix.toString());
		ObjectListing listing = ossClient.listObjects(listObjectsRequest);
		List<String> Objects = new ArrayList<>();
		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
			Objects.add(objectSummary.getKey());
		}
		return Objects;
	}
	public static void main(String[] args) {
		String str = "http://oss-cn-hangzhou.aliyuncs.com";
		System.err.println(str.replace("http", "https"));
	}
}
