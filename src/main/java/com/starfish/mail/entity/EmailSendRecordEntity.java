package com.starfish.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮件发送记录实体类
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Data
@TableName("t_email_send_record")
public class EmailSendRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 推送任务ID
     */
    private Long taskId;

    /**
     * 发件人邮箱
     */
    private String senderEmail;

    /**
     * 收件人邮箱
     */
    private String receiverEmail;

    /**
     * 邮件标题
     */
    private String emailTitle;

    /**
     * 邮件内容
     */
    private String emailContent;

    /**
     * 发送状态：0-待发送，1-发送成功，2-发送失败
     */
    private Integer sendStatus;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;
}
