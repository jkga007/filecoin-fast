package com.filecoin.common.utils;

import org.apache.shiro.crypto.hash.Sha256Hash;

public class MyTest {
    //    public static void main(String[] args) {
//        String a = new Sha256Hash("123456", "qGDlfFKdwaOywPnj1zAd").toHex();
//        System.out.println(a);
//    }
    public static void main(String[] args) throws Exception {
//        String email = "316955509@qq.com";
//        int i = email.indexOf("@");
//        int length = email.length();
//        String substring = email.substring(i+1, length);
//        System.out.println(substring);
//        String a = new Sha256Hash("admin", "YzcmCZNvbXocrsz9dm8e").toHex();
//        System.out.println(a);
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://data.block.cc/api/v1/tickers?market=bitfinex")
//                .get()
//                .build();
//
//        Response response = client.newCall(request).execute();
//        System.out.println(response.body().string());
        String a = new Sha256Hash("Hytera19932017", "YzcmCZNvbXocrsz9dm8e").toHex();
        System.out.println(a);
    }
}
