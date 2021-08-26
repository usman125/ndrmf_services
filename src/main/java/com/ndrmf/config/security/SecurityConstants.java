package com.ndrmf.config.security;

public class SecurityConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/user/signup";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String COMPLAINT_ADD = "/complaint/add";
    public static final String COMPLAINT_FIND_BY_USER = "/complaint/findByUser/**";
    public static final String COMPLAINT_FIND_USER = "/complaint/user/**";
    public static final String COMPLAINT_FIND = "/complaint/find/**";
    public static final String COMPLAINT_APPEAL = "/complaint/appeal";
}
