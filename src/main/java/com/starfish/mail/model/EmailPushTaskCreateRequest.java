package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件推送任务创建请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Data
public class EmailPushTaskCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 推送任务名称
     */
    private String taskName;

    /**
     * 邮件标题
     */
    private String emailTitle;

    /**
     * 邮件内容
     */
    private String emailContent;

    /**
     * 发件人类型：1-系统内置发件人，2-手动输入
     */
    private Integer senderType;

    /**
     * 发件人ID（系统内置发件人时使用）
     */
    private Integer senderId;

    /**
     * 发件人邮箱（手动输入时使用）
     */
    private String senderEmail;

    /**
     * 发件人密码/授权码（手动输入时使用）
     */
    private String senderPassword;

    /**
     * 收件人类型：1-系统内置收件人，2-手动输入
     */
    private Integer receiverType;

    /**
     * 收件人ID列表（系统内置收件人时使用）
     */
    private java.util.List<Long> receiverIds;

    /**
     * 收件人邮箱列表（手动输入时使用，逗号分隔）
     */
    private String receiverEmails;
}
