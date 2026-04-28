package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮件发送记录响应模型
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Data
public class EmailSendRecordModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
