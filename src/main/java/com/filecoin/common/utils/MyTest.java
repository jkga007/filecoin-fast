package com.filecoin.common.utils;

import org.apache.shiro.crypto.hash.Sha256Hash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTest {
//    public static void main(String[] args) {
//        String a = new Sha256Hash("123456", "qGDlfFKdwaOywPnj1zAd").toHex();
//        System.out.println(a);
//    }
    public static void main(String[] args) {
        String email = "316955509@qq.com";
        int i = email.indexOf("@");
        int length = email.length();
        String substring = email.substring(i+1, length);
        System.out.println(substring);


    }
}
