package com.starfish.mail.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.core.date.DateUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
public class JwtUtil {

    /**
     * 密钥
     */
    private static final String SECRET_KEY = "mailman-jwt-secret-key-2026-sunkolin";

    /**
     * Token过期时间(毫秒)- 24小时
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 生成Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return Token字符串
     */
    public static String generateToken(Long userId, String username) {
        Map<String, Object> payload = new HashMap<>(4);
        payload.put(JWTPayload.SUBJECT, userId);
        payload.put("username", username);
        payload.put(JWTPayload.ISSUED_AT, new Date());
        payload.put(JWTPayload.EXPIRES_AT, new Date(System.currentTimeMillis() + EXPIRE_TIME));

        return JWTUtil.createToken(payload, SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证Token是否有效
     *
     * @param token Token字符串
     * @return true有效，false无效
     */
    public static boolean verify(String token) {
        try {
            return JWTUtil.verify(token, SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token Token字符串
     * @return 用户ID
     */
    public static Long getUserId(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object userId = jwt.getPayloads().getObj(JWTPayload.SUBJECT);
            if (userId instanceof Number) {
                return ((Number) userId).longValue();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从Token中获取用户名
     *
     * @param token Token字符串
     * @return 用户名
     */
    public static String getUsername(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object username = jwt.getPayloads().getObj("username");
            return username != null ? username.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查Token是否过期
     *
     * @param token Token字符串
     * @return true已过期，false未过期
     */
    public static boolean isExpired(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object expiresAtObj = jwt.getPayloads().getObj(JWTPayload.EXPIRES_AT);
            if (expiresAtObj instanceof Date) {
                return ((Date) expiresAtObj).before(new Date());
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
