package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
public class UserQueryRequest implements Serializable {

    /**
     * 页码（从1开始）
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 用户名（模糊查询）
     */
    private String userName;

    /**
     * 手机号（模糊查询）
     */
    private String mobile;

    /**
     * 邮箱（模糊查询）
     */
    private String email;

    /**
     * 昵称（模糊查询）
     */
    private String nickName;

}
