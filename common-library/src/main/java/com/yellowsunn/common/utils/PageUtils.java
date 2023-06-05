package com.yellowsunn.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PageUtils {
    public int currentPage(int page) {
        return Math.max(page, 0);
    }

    public int currentSize(int size, int maxSize) {
        if (size <= 0) {
            return 0;
        }
        return Math.min(size, maxSize);
    }
}
