package com.starfish.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 收件人实体类
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-23
 */
@Data
@TableName(value = "t_receiver")
public class ReceiverEntity implements Serializable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收件人邮箱地址
     */
    private String receiver;

    /**
     * 状态：1-可用，2-不可用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
