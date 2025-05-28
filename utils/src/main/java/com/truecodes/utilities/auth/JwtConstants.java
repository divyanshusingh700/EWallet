package com.truecodes.utilities.auth;

public class JwtConstants {
    public static final String SECRET_KEY = "mysecretkey"; // Store in env for production
    public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}

