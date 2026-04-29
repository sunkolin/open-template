package com.starfish.mail.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfish.mail.entity.UserEntity;
import com.starfish.mail.mapper.UserMapper;
import com.starfish.mail.model.UserCreateRequest;
import com.starfish.mail.model.UserQueryRequest;
import com.starfish.mail.model.UserUpdateRequest;
import com.starfish.mail.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * UserServiceImpl
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2018-05-23
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int deleteById(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public Long insert(UserEntity entity) {
        userMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public UserEntity selectById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public int updateById(UserEntity record) {
        return userMapper.updateById(record);
    }

    @Override
    public UserEntity selectOne(LambdaQueryWrapper<UserEntity> queryWrapper) {
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<UserEntity> selectPageByQuery(UserQueryRequest queryRequest) {
        // 构建分页对象
        Page<UserEntity> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        
        // 模糊查询条件
        if (StrUtil.isNotBlank(queryRequest.getUserName())) {
            queryWrapper.like(UserEntity::getUserName, queryRequest.getUserName());
        }
        if (StrUtil.isNotBlank(queryRequest.getMobile())) {
            queryWrapper.like(UserEntity::getMobile, queryRequest.getMobile());
        }
        if (StrUtil.isNotBlank(queryRequest.getEmail())) {
            queryWrapper.like(UserEntity::getEmail, queryRequest.getEmail());
        }
        if (StrUtil.isNotBlank(queryRequest.getNickName())) {
            queryWrapper.like(UserEntity::getNickName, queryRequest.getNickName());
        }
        
        // 按ID降序排列
        queryWrapper.orderByDesc(UserEntity::getId);
        
        // 执行分页查询
        return userMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long createUser(UserCreateRequest createRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(createRequest.getMobile());
        // 密码加密
        if (StrUtil.isNotBlank(createRequest.getPassword())) {
            userEntity.setPassword(DigestUtil.md5Hex(createRequest.getPassword()));
        }
        userEntity.setNickName(createRequest.getNickName());
        userEntity.setUserName(createRequest.getUserName());
        userEntity.setFullName(createRequest.getFullName());
        userEntity.setEmail(createRequest.getEmail());
        userEntity.setSex(createRequest.getSex());
        userEntity.setBirthday(createRequest.getBirthday());
        userEntity.setRemark(createRequest.getRemark());
        userEntity.setCreateTime(new Date());
        userEntity.setModifyTime(new Date());
        
        userMapper.insert(userEntity);
        return userEntity.getId();
    }

    @Override
    public int updateUser(UserUpdateRequest updateRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(updateRequest.getId());
        userEntity.setMobile(updateRequest.getMobile());
        userEntity.setNickName(updateRequest.getNickName());
        userEntity.setFullName(updateRequest.getFullName());
        userEntity.setEmail(updateRequest.getEmail());
        userEntity.setSex(updateRequest.getSex());
        userEntity.setBirthday(updateRequest.getBirthday());
        userEntity.setRemark(updateRequest.getRemark());
        userEntity.setModifyTime(new Date());
        
        return userMapper.updateById(userEntity);
    }

    @Override
    public int deleteUser(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public Long selectMaxId() {
        Page<UserEntity> page = new Page<>(1, 1);
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserEntity::getId);
        
        IPage<UserEntity> result = userMapper.selectPage(page, queryWrapper);
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            return result.getRecords().get(0).getId();
        }
        return 0L;
    }

}
