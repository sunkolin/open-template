package com.starfish.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.EmailPushTaskEntity;
import com.starfish.mail.model.EmailPushTaskCreateRequest;
import com.starfish.mail.model.EmailPushTaskModel;
import com.starfish.mail.model.EmailPushTaskUpdateRequest;

/**
 * 邮件推送任务服务接口
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
public interface EmailPushTaskService {

    /**
     * 分页查询推送任务
     *
     * @param pageNum   页码
     * @param pageSize  每页大小
     * @param taskName  任务名称（可选）
     * @param status    任务状态（可选）
     * @return 分页结果
     */
    IPage<EmailPushTaskModel> pageTasks(int pageNum, int pageSize, String taskName, Integer status);

    /**
     * 根据ID查询任务详情
     *
     * @param id 任务ID
     * @return 任务模型
     */
    EmailPushTaskModel getTaskById(Long id);

    /**
     * 创建推送任务
     *
     * @param createRequest 创建请求
     * @return 任务ID
     */
    Long createTask(EmailPushTaskCreateRequest createRequest);

    /**
     * 更新推送任务
     *
     * @param updateRequest 更新请求
     * @return 影响行数
     */
    int updateTask(EmailPushTaskUpdateRequest updateRequest);

    /**
     * 删除推送任务
     *
     * @param id 任务ID
     * @return 影响行数
     */
    int deleteTask(Long id);

    /**
     * 启动推送任务
     *
     * @param id 任务ID
     * @return 是否启动成功
     */
    boolean startTask(Long id);

    /**
     * 停止推送任务
     *
     * @param id 任务ID
     * @return 是否停止成功
     */
    boolean stopTask(Long id);
}
