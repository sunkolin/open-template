package com.starfish.mail.model;

import lombok.Data;

/**
 * 收件人更新请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-23
 */
@Data
public class ReceiverUpdateRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 收件人邮箱地址
     */
    private String receiver;

    /**
     * 状态：1-可用，2-不可用
     */
    private Integer status;

}
