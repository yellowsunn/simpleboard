package com.yellowsunn.common.constant;

import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;

@Getter
public enum StorageType {
    THUMBNAIL("thumbnail"),
    ARTICLE("article"),
    COMMENT("comment"),
    ;
    private final String path;

    StorageType(String path) {
        this.path = path;
    }

    public static StorageType convertFrom(String type) {
        StorageType storageType = EnumUtils.getEnumIgnoreCase(StorageType.class, type);
        if (storageType == null) {
            throw new IllegalArgumentException("지원하지 않는 Storage 타입입니다.");
        }
        return storageType;
    }
}
