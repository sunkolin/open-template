package com.starfish.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfish.mail.entity.EmailSendRecordEntity;
import com.starfish.mail.enums.EmailSendStatusEnum;
import com.starfish.mail.mapper.EmailSendRecordMapper;
import com.starfish.mail.model.EmailSendRecordModel;
import com.starfish.mail.service.EmailSendRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邮件发送记录服务实现类
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Slf4j
@Service
public class EmailSendRecordServiceImpl implements EmailSendRecordService {

    @Resource
    private EmailSendRecordMapper emailSendRecordMapper;

    @Resource
    private com.starfish.mail.service.EmailService emailService;

    @Resource
    private com.starfish.mail.mapper.EmailPushTaskMapper emailPushTaskMapper;

    @Resource
    private com.starfish.mail.mapper.SenderMapper senderMapper;

    @Override
    public IPage<EmailSendRecordModel> pageRecords(int pageNum, int pageSize, Long taskId, Integer sendStatus) {
        Page<EmailSendRecordEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EmailSendRecordEntity> queryWrapper = new LambdaQueryWrapper<>();

        if (taskId != null) {
            queryWrapper.eq(EmailSendRecordEntity::getTaskId, taskId);
        }

        if (sendStatus != null) {
            queryWrapper.eq(EmailSendRecordEntity::getSendStatus, sendStatus);
        }

        queryWrapper.orderByDesc(EmailSendRecordEntity::getCreateTime);

        IPage<EmailSendRecordEntity> entityPage = emailSendRecordMapper.selectPage(page, queryWrapper);

        // 转换为Model
        Page<EmailSendRecordModel> modelPage = new Page<>(pageNum, pageSize, entityPage.getTotal());
        List<EmailSendRecordModel> modelList = entityPage.getRecords().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
        modelPage.setRecords(modelList);

        return modelPage;
    }

    @Override
    public EmailSendRecordModel getRecordById(Long id) {
        EmailSendRecordEntity entity = emailSendRecordMapper.selectById(id);
        return entity != null ? convertToModel(entity) : null;
    }

    @Override
    public boolean resendEmail(Long id) {
        EmailSendRecordEntity record = emailSendRecordMapper.selectById(id);
        if (record == null) {
            log.warn("发送记录不存在: {}", id);
            return false;
        }

        // 只有失败的记录才能重新发送
        if (!EmailSendStatusEnum.FAILED.getCode().equals(record.getSendStatus())) {
            log.warn("记录状态不允许重发: {}, 当前状态: {}", id, record.getSendStatus());
            return false;
        }

        try {
            // 重置记录状态
            record.setSendStatus(EmailSendStatusEnum.PENDING.getCode());
            record.setFailReason(null);
            record.setRetryCount(record.getRetryCount() + 1);
            record.setSendTime(null);
            record.setModifyTime(LocalDateTime.now());
            emailSendRecordMapper.updateById(record);

            // 获取发件人密码/授权码
            String password = null;
            com.starfish.mail.entity.EmailPushTaskEntity task = emailPushTaskMapper.selectById(record.getTaskId());
            if (task != null) {
                if (task.getSenderType() == 1) {
                    // 系统内置发件人：从发件人表查询
                    com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.starfish.mail.entity.SenderEntity> senderQuery = 
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
                    senderQuery.eq(com.starfish.mail.entity.SenderEntity::getSenderEmail, record.getSenderEmail());
                    senderQuery.eq(com.starfish.mail.entity.SenderEntity::getStatus, 1);
                    com.starfish.mail.entity.SenderEntity sender = senderMapper.selectOne(senderQuery);
                    if (sender != null) {
                        password = sender.getAuthCode() != null ? sender.getAuthCode() : sender.getEmailPassword();
                    }
                } else if (task.getSenderType() == 2) {
                    // 手动输入的发件人
                    password = task.getSenderPassword();
                }
            }

            if (password == null || password.isEmpty()) {
                throw new RuntimeException("发件人密码/授权码不能为空");
            }

            // 调用邮件发送服务
            boolean result = emailService.sendEmail(
                    record.getSenderEmail(),
                    password,
                    record.getReceiverEmail(),
                    record.getEmailTitle(),
                    record.getEmailContent()
            );

            if (result) {
                record.setSendStatus(EmailSendStatusEnum.SUCCESS.getCode());
                record.setSendTime(LocalDateTime.now());
                record.setModifyTime(LocalDateTime.now());
                emailSendRecordMapper.updateById(record);
                return true;
            } else {
                throw new RuntimeException("邮件发送失败");
            }
        } catch (Exception e) {
            log.error("重新发送邮件失败: {}", id, e);
            record.setSendStatus(EmailSendStatusEnum.FAILED.getCode());
            record.setFailReason(e.getMessage());
            record.setModifyTime(LocalDateTime.now());
            emailSendRecordMapper.updateById(record);
            return false;
        }
    }

    /**
     * 转换为Model
     */
    private EmailSendRecordModel convertToModel(EmailSendRecordEntity entity) {
        EmailSendRecordModel model = new EmailSendRecordModel();
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
