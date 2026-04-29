package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录请求模型
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 记住我
     */
    private Boolean rememberMe = false;

}
