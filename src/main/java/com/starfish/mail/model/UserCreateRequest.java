package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户创建请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
public class UserCreateRequest implements Serializable {

    private String mobile;

    private String password;

    private String nickName;

    private String userName;

    private String fullName;

    private String email;

    private Integer sex;

    private Date birthday;

    private String remark;

}
