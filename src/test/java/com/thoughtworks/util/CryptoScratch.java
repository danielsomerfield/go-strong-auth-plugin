package com.thoughtworks.util;

import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CryptoScratch {
    public static void main(String[] args) throws Exception {
//        PBEKeySpec spec = new PBEKeySpec("aSecret".toCharArray(), "E7XC9KUHPAUAYKHVEHG0".getBytes("UTF-8"), 10000, 256);
//        byte [] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded();
//        System.out.println(String.format("%s", Hex.encodeHexString(key)));

//        System.out.println(BCrypt.gensalt(10));
//        System.out.println(BCrypt.gensalt(5));
        System.out.println(BCrypt.hashpw("pazzword123", "$2a$10$xannybUjnYmLhbfwHsd3de"));
    }
}
