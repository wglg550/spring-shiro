package com.spring.shiro.demo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * @author Mr.Li
 * @create 2018-07-12 14:23
 * @desc JWT工具类
 **/
@Slf4j
public class JwtUtil {

    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    //    private static final long EXPIRE_TIME = 60 * 1000;
    private static final long ADVANCE_EXPIRE_TIME = 5;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, Long userId, String secret) {
        try {
            //根据密码生成JWT效验器
            secret = userId + "YuiopB0009Xn";
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userId", String.valueOf(userId))
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getClaim(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
//            Map<String, Claim> map = jwt.getClaims();
//            Claim claim = map.get("exp");
//            log.info("map.get(exp):{}", claim.asLong());
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param userId 用户名
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(Long userId, String secret) {
        secret = userId + "YuiopB0009Xn";
        Long start = System.currentTimeMillis();
        Date date = new Date(start + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("userId", String.valueOf(userId))
                .withClaim("currentTimeMillis", String.valueOf(start))
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }
}
