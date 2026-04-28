package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 个人信息更新请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
public class ProfileUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

}
