package com.yellowsunn.simpleforum.security.encoder;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.util.StringUtils.hasText;

@Component
public class SHA256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String password) {
        if (hasAnyBlank(password)) {
            throw new IllegalArgumentException("Password must not contain spaces.");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(UTF_8));
            return byteToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("Password encoding failed.");
        }
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        if (hasText(password) && hasText(encodedPassword)) {
            return encode(password).equals(encodedPassword);
        } else {
            return false;
        }
    }

    private String byteToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    private boolean hasAnyBlank(String value) {
        return !hasText(value) || value.contains(" ");
    }
}
