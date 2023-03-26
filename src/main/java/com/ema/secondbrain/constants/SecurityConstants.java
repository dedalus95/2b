package com.ema.secondbrain.constants;

public class SecurityConstants {


    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String JWT_SECRET = "NAYg8t6v9y$B&E)O@pcQrThWdaq5M8w?z%CUF-JeFpRgUkXn3Rlu8x/L?D(G+DbU";

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";
    public static final int TOKEN_EXPIRATION= 30*60*1000;
    public static final int REFRESH_TOKEN_EXPIRATION= 60*60*1000;


}
