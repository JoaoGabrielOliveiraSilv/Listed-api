package br.com.devinno.listedapi.JWT;

public class SecurityConstants {
    public static final String SECRET_REGISTER = "SecretKeyToGenJWTs";
    public static final String SECRET_RECOVER_PASSWORD = "forgotPasswordInListed";
    public static final String ISSUER = "GreatCode-Listed-API"; 
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final long EXPIRATION_TIME_RECOVER_PASSWORD = 36_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
}