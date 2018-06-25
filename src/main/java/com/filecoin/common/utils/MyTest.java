package com.filecoin.common.utils;

import org.apache.shiro.crypto.hash.Sha256Hash;

public class MyTest {
    public static void main(String[] args) {
        String a = new Sha256Hash("123456", "qGDlfFKdwaOywPnj1zAd").toHex();
        System.out.println(a);
    }
}
