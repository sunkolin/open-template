package com.starfish.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * EmailSendRecordModel
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2019-12-20
 */
@Data
@TableName(value = "t_email_record")
public class EmailRecordEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private String sender;

    private String receiver;

    private Integer status;

    private String remark;

    private Date createTime;

    private Date updateTime;

}