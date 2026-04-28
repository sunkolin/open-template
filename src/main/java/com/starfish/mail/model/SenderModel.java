package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;

/**
 * SenderModel
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2017-05-25
 */
@Data
public class SenderModel implements Serializable {

    /**
     * 用户
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码是否正确
     */
    private Boolean status;

    /**
     * 是否使用此账号发送邮件
     */
    private Boolean use;

}
