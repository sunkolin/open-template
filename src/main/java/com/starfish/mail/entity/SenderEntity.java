package com.starfish.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 发件人实体类
 */
@Data
@TableName("t_sender")
public class SenderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 发件人邮箱
     */
    private String senderEmail;

    /**
     * 邮箱密码
     */
    private String emailPassword;

    /**
     * 授权码
     */
    private String authCode;

    /**
     * 状态（1有效，2无效）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;
}
