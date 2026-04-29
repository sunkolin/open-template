package com.starfish.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮箱类型枚举
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Getter
@AllArgsConstructor
public enum EmailTypeEnum {

    /**
     * QQ邮箱
     */
    QQ("qq.com", "smtp.qq.com", 465, "SSL"),

    /**
     * 163邮箱
     */
    EMAIL_163("163.com", "smtp.163.com", 465, "SSL"),

    /**
     * 126邮箱
     */
    EMAIL_126("126.com", "smtp.126.com", 465, "SSL"),

    /**
     * 谷歌邮箱
     */
    GMAIL("gmail.com", "smtp.gmail.com", 587, "TLS"),

    /**
     * Outlook邮箱
     */
    OUTLOOK("outlook.com", "smtp-mail.outlook.com", 587, "TLS"),

    /**
     * Foxmail邮箱
     */
    FOXMAIL("foxmail.com", "smtp.qq.com", 465, "SSL");

    private final String domain;
    private final String smtpHost;
    private final Integer smtpPort;
    private final String protocol;

    /**
     * 根据邮箱地址获取邮箱类型
     */
    public static EmailTypeEnum getByEmail(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        String domain = email.split("@")[1].toLowerCase();
        for (EmailTypeEnum type : values()) {
            if (type.getDomain().equals(domain)) {
                return type;
            }
        }
        return null;
    }
}
