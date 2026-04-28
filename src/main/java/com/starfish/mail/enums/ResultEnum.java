package com.starfish.mail.enums;

/**
 * 结果枚举
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2014-07-07
 */
@SuppressWarnings(value = "unused")
public enum ResultEnum {

    SUCCESS(200, "成功", "成功"),
    SYSTEM_EXCEPTION(500, "系统异常", "系统异常"),

    PARAM_EXCEPTION(1001, "参数错误", "参数错误"),
    USER_ALREADY_EXISTS(1002, "用户已经存在", "用户已经存在"),
    USER_NOT_EXISTS(1003, "用户不存在", "用户不存在"),
    PASSWORD_ERROR(1004, "密码错误", "密码错误"),
    USER_NOT_LOGIN(1054, "用户未登录", "用户未登录"),

    FINAL(9999, "系统异常", "系统异常");

    private Integer code;
    private String message;
    private String description;

    /**
     * @param code        code
     * @param message     message
     * @param description description
     */
    ResultEnum(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    /**
     * get the enum code
     *
     * @return code
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * get the message
     *
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * get the enum description
     *
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * get the enum by code
     *
     * @param code code
     * @return the enum
     */
    public static ResultEnum get(Integer code) {
        ResultEnum[] values = ResultEnum.values();
        ResultEnum v = null;
        for (ResultEnum value : values) {
            if (code.equals(value.getCode())) {
                v = value;
                break;
            }
        }
        return v;
    }

}
