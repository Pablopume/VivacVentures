package com.example.vivacventures.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
@Component
public class Utils {
    private Utils() {
    }

    public static String randomBytes() {
        SecureRandom sr = new SecureRandom();
        byte[] bits = new byte[32];
        sr.nextBytes(bits);
        return Base64.getUrlEncoder().encodeToString(bits);
    }
}
