package com.starfish.mail.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfish.mail.context.UserContext;
import com.starfish.mail.entity.EmailPushTaskEntity;
import com.starfish.mail.entity.EmailSendRecordEntity;
import com.starfish.mail.entity.ReceiverEntity;
import com.starfish.mail.entity.SenderEntity;
import com.starfish.mail.enums.EmailSendStatusEnum;
import com.starfish.mail.enums.PushTaskStatusEnum;
import com.starfish.mail.mapper.EmailPushTaskMapper;
import com.starfish.mail.mapper.EmailSendRecordMapper;
import com.starfish.mail.mapper.ReceiverMapper;
import com.starfish.mail.mapper.SenderMapper;
import com.starfish.mail.model.EmailPushTaskCreateRequest;
import com.starfish.mail.model.EmailPushTaskModel;
import com.starfish.mail.model.EmailPushTaskUpdateRequest;
import com.starfish.mail.service.EmailPushTaskService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 邮件推送任务服务实现类
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Slf4j
@Service
public class EmailPushTaskServiceImpl implements EmailPushTaskService {

    @Resource
    private EmailPushTaskMapper emailPushTaskMapper;

    @Resource
    private EmailSendRecordMapper emailSendRecordMapper;

    @Resource
    private SenderMapper senderMapper;

    @Resource
    private ReceiverMapper receiverMapper;

    @Resource
    private com.starfish.mail.service.EmailService emailService;

    @Override
    public IPage<EmailPushTaskModel> pageTasks(int pageNum, int pageSize, String taskName, Integer status) {
        Page<EmailPushTaskEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<EmailPushTaskEntity> queryWrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(taskName)) {
            queryWrapper.like(EmailPushTaskEntity::getTaskName, taskName);
        }

        if (status != null) {
            queryWrapper.eq(EmailPushTaskEntity::getStatus, status);
        }

        queryWrapper.orderByDesc(EmailPushTaskEntity::getCreateTime);

        IPage<EmailPushTaskEntity> entityPage = emailPushTaskMapper.selectPage(page, queryWrapper);

        // 转换为Model
        Page<EmailPushTaskModel> modelPage = new Page<>(pageNum, pageSize, entityPage.getTotal());
        List<EmailPushTaskModel> modelList = entityPage.getRecords().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
        modelPage.setRecords(modelList);

        return modelPage;
    }

    @Override
    public EmailPushTaskModel getTaskById(Long id) {
        EmailPushTaskEntity entity = emailPushTaskMapper.selectById(id);
        return entity != null ? convertToModel(entity) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(EmailPushTaskCreateRequest createRequest) {
        EmailPushTaskEntity entity = new EmailPushTaskEntity();
        BeanUtils.copyProperties(createRequest, entity);

        // 处理收件人ID列表
        if (createRequest.getReceiverIds() != null && !createRequest.getReceiverIds().isEmpty()) {
            entity.setReceiverIds(
                    createRequest.getReceiverIds().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
            );
        }

        // 设置初始状态
        entity.setStatus(PushTaskStatusEnum.NOT_STARTED.getCode());
        entity.setTotalEmails(0);
        entity.setSuccessCount(0);
        entity.setFailCount(0);

        // 设置创建人信息
        Long userId = UserContext.getUserId();
        String username = UserContext.getUsername();
        entity.setCreateUserId(userId);
        entity.setCreateUserName(username);

        entity.setCreateTime(LocalDateTime.now());
        entity.setModifyTime(LocalDateTime.now());

        emailPushTaskMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateTask(EmailPushTaskUpdateRequest updateRequest) {
        EmailPushTaskEntity entity = new EmailPushTaskEntity();
        BeanUtils.copyProperties(updateRequest, entity);

        // 处理收件人ID列表
        if (updateRequest.getReceiverIds() != null && !updateRequest.getReceiverIds().isEmpty()) {
            entity.setReceiverIds(
                    updateRequest.getReceiverIds().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","))
            );
        }

        entity.setModifyTime(LocalDateTime.now());

        return emailPushTaskMapper.updateById(entity);
    }

    @Override
    public int deleteTask(Long id) {
        return emailPushTaskMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(Long id) {
        EmailPushTaskEntity task = emailPushTaskMapper.selectById(id);
        if (task == null) {
            log.warn("任务不存在: {}", id);
            return false;
        }

        // 只有未开始或已停止的任务才能启动
        if (!PushTaskStatusEnum.NOT_STARTED.getCode().equals(task.getStatus())
                && !PushTaskStatusEnum.STOPPED.getCode().equals(task.getStatus())) {
            log.warn("任务状态不允许启动: {}, 当前状态: {}", id, task.getStatus());
            return false;
        }

        // 系统内置发件人：获取所有有效发件人
        List<SenderEntity> senders = new ArrayList<>();
        if (task.getSenderType() == 1) {
            // 系统内置：查询所有状态为有效的发件人
            LambdaQueryWrapper<SenderEntity> senderQuery = new LambdaQueryWrapper<>();
            senderQuery.eq(SenderEntity::getStatus, 1);
            senders = senderMapper.selectList(senderQuery);
            
            if (senders.isEmpty()) {
                log.warn("没有可用的系统内置发件人: {}", id);
                return false;
            }
        }
        
        // 获取收件人列表
        List<String> receiverEmails = getReceiverEmails(task);
        if (receiverEmails.isEmpty()) {
            log.warn("任务没有收件人: {}", id);
            return false;
        }

        // 更新任务状态
        task.setStatus(PushTaskStatusEnum.RUNNING.getCode());
        // 总邮件数 = 发件人数 * 收件人数
        int totalEmails = (task.getSenderType() == 1 ? senders.size() : 1) * receiverEmails.size();
        task.setTotalEmails(totalEmails);
        task.setStartTime(LocalDateTime.now());
        task.setModifyTime(LocalDateTime.now());
        emailPushTaskMapper.updateById(task);

        // 创建发送记录
        for (String receiverEmail : receiverEmails) {
            if (task.getSenderType() == 1) {
                // 系统内置发件人：为每个发件人都创建发送记录
                for (SenderEntity sender : senders) {
                    EmailSendRecordEntity record = new EmailSendRecordEntity();
                    record.setTaskId(id);
                    record.setSenderEmail(sender.getSenderEmail());
                    record.setReceiverEmail(receiverEmail);
                    record.setEmailTitle(task.getEmailTitle());
                    record.setEmailContent(task.getEmailContent());
                    record.setSendStatus(EmailSendStatusEnum.PENDING.getCode());
                    record.setRetryCount(0);
                    record.setCreateTime(LocalDateTime.now());
                    record.setModifyTime(LocalDateTime.now());
                    emailSendRecordMapper.insert(record);
                }
            } else {
                // 手动输入发件人
                EmailSendRecordEntity record = new EmailSendRecordEntity();
                record.setTaskId(id);
                record.setSenderEmail(task.getSenderEmail());
                record.setReceiverEmail(receiverEmail);
                record.setEmailTitle(task.getEmailTitle());
                record.setEmailContent(task.getEmailContent());
                record.setSendStatus(EmailSendStatusEnum.PENDING.getCode());
                record.setRetryCount(0);
                record.setCreateTime(LocalDateTime.now());
                record.setModifyTime(LocalDateTime.now());
                emailSendRecordMapper.insert(record);
            }
        }

        // 异步执行邮件发送
        CompletableFuture.runAsync(() -> executeTask(id));

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopTask(Long id) {
        EmailPushTaskEntity task = emailPushTaskMapper.selectById(id);
        if (task == null) {
            log.warn("任务不存在: {}", id);
            return false;
        }

        // 只有运行中的任务才能停止
        if (!PushTaskStatusEnum.RUNNING.getCode().equals(task.getStatus())) {
            log.warn("任务状态不允许停止: {}, 当前状态: {}", id, task.getStatus());
            return false;
        }

        task.setStatus(PushTaskStatusEnum.STOPPED.getCode());
        task.setEndTime(LocalDateTime.now());
        task.setModifyTime(LocalDateTime.now());
        emailPushTaskMapper.updateById(task);

        return true;
    }

    /**
     * 执行推送任务
     */
    private void executeTask(Long taskId) {
        try {
            EmailPushTaskEntity task = emailPushTaskMapper.selectById(taskId);
            if (task == null || !PushTaskStatusEnum.RUNNING.getCode().equals(task.getStatus())) {
                return;
            }

            // 查询待发送的记录
            LambdaQueryWrapper<EmailSendRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EmailSendRecordEntity::getTaskId, taskId);
            queryWrapper.eq(EmailSendRecordEntity::getSendStatus, EmailSendStatusEnum.PENDING.getCode());
            List<EmailSendRecordEntity> pendingRecords = emailSendRecordMapper.selectList(queryWrapper);

            int successCount = 0;
            int failCount = 0;

            for (EmailSendRecordEntity record : pendingRecords) {
                // 检查任务是否被停止
                task = emailPushTaskMapper.selectById(taskId);
                if (task == null || !PushTaskStatusEnum.RUNNING.getCode().equals(task.getStatus())) {
                    break;
                }

                boolean sendResult = sendEmail(record);
                if (sendResult) {
                    successCount++;
                } else {
                    failCount++;
                }

                // 更新任务统计
                updateTaskStatistics(taskId, successCount, failCount);
            }

            // 更新任务最终状态
            task = emailPushTaskMapper.selectById(taskId);
            if (task != null && PushTaskStatusEnum.RUNNING.getCode().equals(task.getStatus())) {
                task.setStatus(PushTaskStatusEnum.COMPLETED.getCode());
                task.setEndTime(LocalDateTime.now());
                task.setModifyTime(LocalDateTime.now());
                emailPushTaskMapper.updateById(task);
            }

        } catch (Exception e) {
            log.error("执行推送任务失败: {}", taskId, e);
            // 更新任务状态为失败
            EmailPushTaskEntity task = emailPushTaskMapper.selectById(taskId);
            if (task != null) {
                task.setStatus(PushTaskStatusEnum.FAILED.getCode());
                task.setEndTime(LocalDateTime.now());
                task.setModifyTime(LocalDateTime.now());
                emailPushTaskMapper.updateById(task);
            }
        }
    }

    /**
     * 发送邮件
     */
    private boolean sendEmail(EmailSendRecordEntity record) {
        try {
            // 获取发件人密码/授权码
            String password = null;
            EmailPushTaskEntity task = emailPushTaskMapper.selectById(record.getTaskId());
            if (task != null) {
                if (task.getSenderType() == 1) {
                    // 系统内置发件人：从发件人表查询
                    LambdaQueryWrapper<SenderEntity> senderQuery = new LambdaQueryWrapper<>();
                    senderQuery.eq(SenderEntity::getSenderEmail, record.getSenderEmail());
                    senderQuery.eq(SenderEntity::getStatus, 1);
                    SenderEntity sender = senderMapper.selectOne(senderQuery);
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
            log.error("发送邮件失败: {}", record.getReceiverEmail(), e);
            record.setSendStatus(EmailSendStatusEnum.FAILED.getCode());
            record.setFailReason(e.getMessage());
            record.setModifyTime(LocalDateTime.now());
            emailSendRecordMapper.updateById(record);
            return false;
        }
    }

    /**
     * 更新任务统计
     */
    private void updateTaskStatistics(Long taskId, int successCount, int failCount) {
        EmailPushTaskEntity task = emailPushTaskMapper.selectById(taskId);
        if (task != null) {
            task.setSuccessCount(successCount);
            task.setFailCount(failCount);
            task.setModifyTime(LocalDateTime.now());
            emailPushTaskMapper.updateById(task);
        }
    }

    /**
     * 获取收件人邮箱列表
     */
    private List<String> getReceiverEmails(EmailPushTaskEntity task) {
        List<String> emails = new ArrayList<>();

        if (task.getReceiverType() == 1) {
            // 系统内置收件人：查询所有状态为可用的收件人
            LambdaQueryWrapper<ReceiverEntity> receiverQuery = new LambdaQueryWrapper<>();
            receiverQuery.eq(ReceiverEntity::getStatus, 1);
            List<ReceiverEntity> receivers = receiverMapper.selectList(receiverQuery);
            emails = receivers.stream()
                    .map(ReceiverEntity::getReceiver)
                    .collect(Collectors.toList());
        } else if (task.getReceiverType() == 2) {
            // 手动输入
            if (StrUtil.isNotBlank(task.getReceiverEmails())) {
                emails = Arrays.asList(task.getReceiverEmails().split(","));
            }
        }

        return emails;
    }

    /**
     * 获取发件人邮箱
     */
    private String getSenderEmail(Integer senderId) {
        if (senderId != null) {
            SenderEntity sender = senderMapper.selectById(senderId);
            return sender != null ? sender.getSenderEmail() : null;
        }
        return null;
    }

    /**
     * 转换为Model
     */
    private EmailPushTaskModel convertToModel(EmailPushTaskEntity entity) {
        EmailPushTaskModel model = new EmailPushTaskModel();
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
