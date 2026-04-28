package com.starfish.mail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户响应模型
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * Token（用于后续请求验证）
     */
    private String token;

}
