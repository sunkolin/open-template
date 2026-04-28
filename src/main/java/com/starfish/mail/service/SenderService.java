package com.starfish.mail.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starfish.mail.entity.SenderEntity;

/**
 * 发件人Service
 */
public interface SenderService extends IService<SenderEntity> {

    /**
     * 分页查询发件人
     */
    IPage<SenderEntity> pageSenders(int pageNum, int pageSize, String senderEmail, Integer status);

    /**
     * 根据邮箱查询发件人
     */
    SenderEntity getByEmail(String senderEmail);
}
