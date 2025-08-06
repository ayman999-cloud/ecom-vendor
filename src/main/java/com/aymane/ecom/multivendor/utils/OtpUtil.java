package com.aymane.ecom.multivendor.utils;

import java.security.SecureRandom;
import java.util.Random;

public class OtpUtil {
    public static String generateOtp() {
        final int otpLength = 6;
        final Random random = new SecureRandom();
        final char[] chars = new char[otpLength];
        for (int i = 0; i < otpLength; i++) {
            chars[i] = (char) ('0' + random.nextInt(10));
        }
        return new String(chars);
    }
}
