package com.example.demo.utils;

public class Constants {
    //Security Constants....
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET = "test_secKEY";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
    public static final long EXPIRATION_TIME = 1000*3600*24*5; // 5 days
}
