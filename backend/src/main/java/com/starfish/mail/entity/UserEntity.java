package com.starfish.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UserModel
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2018-05-23
 */
@Data
@TableName(value = "t_user")
public class UserEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String mobile;

    private String password;

    private String nickName;

    private String userName;

    private String fullName;

    private String email;

    private Integer sex;

    private Date birthday;

    private String remark;

    private Date createTime;

    private Date modifyTime;

}