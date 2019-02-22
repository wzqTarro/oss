/**
 * zcareu Inc
 * Copyright (C) 2018 ALL Right Reserved
 */
package com.zcareze.biz.file.service.factory;

import com.zcareze.biz.file.service.enst.FileTypeEnum;
import com.zcareze.biz.file.service.storemode.AliOssImpl;
import com.zcareze.biz.file.service.storemode.LocalImpl;
import com.zcareze.biz.file.service.storemode.StoreInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lveliu
 * @description
 * @date 2018-11-14
 */
public class FileInstanceFactory {

    private static final String FILE_OSS = "OSS";

    private static final Map<String, StoreInstance> STORE_CACHE = new ConcurrentHashMap<>();

    public static StoreInstance getInstance(FileTypeEnum fileTypeEnum) {
        if (STORE_CACHE.containsKey(fileTypeEnum.getStoreMode())) {
            return STORE_CACHE.get(fileTypeEnum.getStoreMode());
        } else {
            StoreInstance storeInstance = null;
            if (FILE_OSS.equals(fileTypeEnum.getStoreMode())) {
                storeInstance = new AliOssImpl();
            } else {
                //storeInstance = new LocalImpl();
            }
            STORE_CACHE.put(fileTypeEnum.getStoreMode(), storeInstance);
            return storeInstance;
        }
    }

}
