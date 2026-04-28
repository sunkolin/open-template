package com.starfish.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starfish.mail.entity.EmailPushTaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件推送任务Mapper
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Mapper
public interface EmailPushTaskMapper extends BaseMapper<EmailPushTaskEntity> {
}
