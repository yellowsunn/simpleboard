package com.yellowsunn.userservice.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;

@UtilityClass
public class Base64Handler {
    public String encode(String target) {
        if (target == null) {
            return null;
        }
        byte[] bytes = Base64.encodeBase64(target.getBytes());
        return new String(bytes);
    }
}
