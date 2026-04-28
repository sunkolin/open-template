package com.starfish.mail.enums;

/**
 * 选择类型
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2014-07-07
 */
@SuppressWarnings(value = "unused")
public enum ChooseTypeEnum {

    SEQUENCE(1, "顺序", "顺序"),

    RANDOM(2, "随机", "随机"),

    FINAL(9999, "未知", "未知");

    private Integer code;
    private String message;
    private String description;

    /**
     * @param code        code
     * @param message     message
     * @param description description
     */
    ChooseTypeEnum(Integer code, String message, String description) {
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
    public static ChooseTypeEnum get(Integer code) {
        ChooseTypeEnum[] values = ChooseTypeEnum.values();
        ChooseTypeEnum v = null;
        for (ChooseTypeEnum value : values) {
            if (code.equals(value.getCode())) {
                v = value;
                break;
            }
        }
        return v;
    }

}
