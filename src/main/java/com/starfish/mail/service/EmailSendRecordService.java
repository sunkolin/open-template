package com.starfish.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.EmailSendRecordEntity;
import com.starfish.mail.model.EmailSendRecordModel;

/**
 * 邮件发送记录服务接口
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
public interface EmailSendRecordService {

    /**
     * 分页查询发送记录
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param taskId     任务ID（可选）
     * @param sendStatus 发送状态（可选）
     * @return 分页结果
     */
    IPage<EmailSendRecordModel> pageRecords(int pageNum, int pageSize, Long taskId, Integer sendStatus);

    /**
     * 根据ID查询发送记录
     *
     * @param id 记录ID
     * @return 发送记录模型
     */
    EmailSendRecordModel getRecordById(Long id);

    /**
     * 重新发送邮件
     *
     * @param id 记录ID
     * @return 是否成功
     */
    boolean resendEmail(Long id);
}
