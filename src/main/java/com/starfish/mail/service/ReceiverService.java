package com.starfish.mail.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.ReceiverEntity;
import com.starfish.mail.model.ReceiverCreateRequest;
import com.starfish.mail.model.ReceiverQueryRequest;
import com.starfish.mail.model.ReceiverUpdateRequest;

/**
 * 收件人服务接口
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-23
 */
public interface ReceiverService {

    /**
     * 根据条件查询单个收件人
     *
     * @param queryWrapper 查询条件
     * @return 收件人实体
     */
    ReceiverEntity selectOne(LambdaQueryWrapper<ReceiverEntity> queryWrapper);

    /**
     * 分页查询收件人（使用MyBatisPlus分页）
     *
     * @param queryRequest 查询条件
     * @return 分页结果
     */
    IPage<ReceiverEntity> selectPageByQuery(ReceiverQueryRequest queryRequest);

    /**
     * 创建收件人
     *
     * @param createRequest 创建请求
     * @return 收件人ID
     */
    Long createReceiver(ReceiverCreateRequest createRequest);

    /**
     * 更新收件人
     *
     * @param updateRequest 更新请求
     * @return 影响行数
     */
    int updateReceiver(ReceiverUpdateRequest updateRequest);

    /**
     * 删除收件人
     *
     * @param id 收件人ID
     * @return 影响行数
     */
    int deleteReceiver(Long id);

    /**
     * 根据ID查询收件人
     *
     * @param id 收件人ID
     * @return 收件人实体
     */
    ReceiverEntity getReceiverById(Long id);

}
