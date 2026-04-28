package com.starfish.mail.model;

import lombok.Data;

/**
 * 收件人查询请求
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-23
 */
@Data
public class ReceiverQueryRequest {

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 收件人邮箱（模糊查询）
     */
    private String receiver;

    /**
     * 状态：1-可用，2-不可用
     */
    private Integer status;

}
