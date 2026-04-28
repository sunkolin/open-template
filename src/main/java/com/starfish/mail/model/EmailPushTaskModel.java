package com.starfish.mail.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮件推送任务响应模型
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Data
public class EmailPushTaskModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

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
     * 发件人邮箱
     */
    private String senderEmail;

    /**
     * 收件人类型：1-系统内置收件人，2-手动输入
     */
    private Integer receiverType;

    /**
     * 收件人ID列表（系统内置收件人时使用，逗号分隔）
     */
    private String receiverIds;

    /**
     * 收件人邮箱列表（手动输入时使用，逗号分隔）
     */
    private String receiverEmails;

    /**
     * 任务状态：0-未开始，1-运行中，2-已完成，3-已停止，4-失败
     */
    private Integer status;

    /**
     * 总邮件数
     */
    private Integer totalEmails;

    /**
     * 成功发送数
     */
    private Integer successCount;

    /**
     * 失败发送数
     */
    private Integer failCount;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
