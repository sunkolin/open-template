package com.starfish.mail.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件推送任务状态枚举
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Getter
@AllArgsConstructor
public enum PushTaskStatusEnum {

    /**
     * 未开始
     */
    NOT_STARTED(0, "未开始"),

    /**
     * 运行中
     */
    RUNNING(1, "运行中"),

    /**
     * 已完成
     */
    COMPLETED(2, "已完成"),

    /**
     * 已停止
     */
    STOPPED(3, "已停止"),

    /**
     * 失败
     */
    FAILED(4, "失败");

    private final Integer code;
    private final String desc;

    public static PushTaskStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PushTaskStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
