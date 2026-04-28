package com.starfish.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starfish.mail.entity.SenderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发件人Mapper
 */
@Mapper
public interface SenderMapper extends BaseMapper<SenderEntity> {
}
