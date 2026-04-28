package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 密码修改请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
public class PasswordChangeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认新密码
     */
    private String confirmPassword;

}
