package com.starfish.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件发送状态枚举
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Getter
@AllArgsConstructor
public enum EmailSendStatusEnum {

    /**
     * 待发送
     */
    PENDING(0, "待发送"),

    /**
     * 发送成功
     */
    SUCCESS(1, "发送成功"),

    /**
     * 发送失败
     */
    FAILED(2, "发送失败");

    private final Integer code;
    private final String desc;

    public static EmailSendStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EmailSendStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
