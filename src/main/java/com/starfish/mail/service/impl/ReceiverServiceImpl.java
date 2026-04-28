package com.starfish.mail.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfish.mail.entity.ReceiverEntity;
import com.starfish.mail.mapper.ReceiverMapper;
import com.starfish.mail.model.ReceiverCreateRequest;
import com.starfish.mail.model.ReceiverQueryRequest;
import com.starfish.mail.model.ReceiverUpdateRequest;
import com.starfish.mail.service.ReceiverService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 收件人服务实现类
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-23
 */
@Service
public class ReceiverServiceImpl implements ReceiverService {

    @Resource
    private ReceiverMapper receiverMapper;

    @Override
    public ReceiverEntity selectOne(LambdaQueryWrapper<ReceiverEntity> queryWrapper) {
        return receiverMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<ReceiverEntity> selectPageByQuery(ReceiverQueryRequest queryRequest) {
        // 构建分页对象
        Page<ReceiverEntity> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<ReceiverEntity> queryWrapper = new LambdaQueryWrapper<>();
        
        // 模糊查询条件
        if (StrUtil.isNotBlank(queryRequest.getReceiver())) {
            queryWrapper.like(ReceiverEntity::getReceiver, queryRequest.getReceiver());
        }
        
        // 精确查询条件
        if (queryRequest.getStatus() != null) {
            queryWrapper.eq(ReceiverEntity::getStatus, queryRequest.getStatus());
        }
        
        // 按ID降序排列
        queryWrapper.orderByDesc(ReceiverEntity::getId);
        
        // 执行分页查询
        return receiverMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long createReceiver(ReceiverCreateRequest createRequest) {
        ReceiverEntity receiverEntity = new ReceiverEntity();
        receiverEntity.setReceiver(createRequest.getReceiver());
        receiverEntity.setStatus(createRequest.getStatus());
        receiverEntity.setCreateTime(new Date());
        receiverEntity.setUpdateTime(new Date());
        
        receiverMapper.insert(receiverEntity);
        return receiverEntity.getId();
    }

    @Override
    public int updateReceiver(ReceiverUpdateRequest updateRequest) {
        ReceiverEntity receiverEntity = new ReceiverEntity();
        receiverEntity.setId(updateRequest.getId());
        receiverEntity.setReceiver(updateRequest.getReceiver());
        receiverEntity.setStatus(updateRequest.getStatus());
        receiverEntity.setUpdateTime(new Date());
        
        return receiverMapper.updateById(receiverEntity);
    }

    @Override
    public int deleteReceiver(Long id) {
        return receiverMapper.deleteById(id);
    }

    @Override
    public ReceiverEntity getReceiverById(Long id) {
        return receiverMapper.selectById(id);
    }

}
