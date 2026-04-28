package com.starfish.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfish.mail.entity.SenderEntity;
import com.starfish.mail.mapper.SenderMapper;
import com.starfish.mail.service.SenderService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 发件人Service实现类
 */
@Service
public class SenderServiceImpl extends ServiceImpl<SenderMapper, SenderEntity> implements SenderService {

    @Override
    public IPage<SenderEntity> pageSenders(int pageNum, int pageSize, String senderEmail, Integer status) {
        Page<SenderEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SenderEntity> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(senderEmail)) {
            wrapper.like(SenderEntity::getSenderEmail, senderEmail);
        }
        
        if (status != null) {
            wrapper.eq(SenderEntity::getStatus, status);
        }
        
        wrapper.orderByDesc(SenderEntity::getCreateTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public SenderEntity getByEmail(String senderEmail) {
        LambdaQueryWrapper<SenderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SenderEntity::getSenderEmail, senderEmail);
        return this.getOne(wrapper);
    }
}
