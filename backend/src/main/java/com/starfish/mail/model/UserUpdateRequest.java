package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户更新请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@Data
public class UserUpdateRequest implements Serializable {

    private Long id;

    private String mobile;

    private String nickName;

    private String fullName;

    private String email;

    private Integer sex;

    private Date birthday;

    private String remark;

}
