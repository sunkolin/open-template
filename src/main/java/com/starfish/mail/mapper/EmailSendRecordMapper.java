package com.starfish.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starfish.mail.entity.EmailSendRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件发送记录Mapper
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@Mapper
public interface EmailSendRecordMapper extends BaseMapper<EmailSendRecordEntity> {
}
