package com.util.tokenutil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token 工具类
 *
 * @author zt1994 2019/7/9 18:19
 */
public class TokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 过期时间 5 小时，正式运行时更新为 15 分钟
     */
    public static final long EXPIRE_TIME = 5 * 60 * 60 * 1000;

    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";


    /**
     * 校验 token 是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            logger.info("校验 token 失败:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 生成签名,15min后过期
     *
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username, String userId) {
        try {
            // 过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // 附带username，userId信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("userId", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            logger.info("生成签名失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * appToken 生成签名,15min后过期
     *
     * @param appId
     * @param tenantId
     * @return
     */
    public static String appSignToken(String appId, String tenantId) {
        try {
            // 过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // 附带username，userId信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("appId", appId)
                    .withClaim("tenantId", tenantId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            logger.info("生成签名失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * 获得 token 中的信息无需 secret 解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            logger.info("获得 token 中的用户名失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * 获得 token 中的信息无需 secret 解密也能获得
     *
     * @param token
     * @return
     */
    public static String getAppId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("appId").asString();
        } catch (JWTDecodeException e) {
            logger.info("获得 token 中的用户名失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * 获得 token 中的信息无需 secret 解密也能获得
     *
     * @param token
     * @return
     */
    public static String getTenantId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("tenantId").asString();
        } catch (JWTDecodeException e) {
            logger.info("获得 token 中的用户名失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * 获取 userId
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            logger.info("获得 token 中的 userId 失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * 获取Token有效时间
     *
     * @param token
     * @return
     */
    public static Date getExpireTime(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            logger.info("获得 token 中的用户名失败:{}", e.getMessage());
            return null;
        }
    }


    /**
     * token是否过期
     *
     * @return true：过期
     */
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }


}
