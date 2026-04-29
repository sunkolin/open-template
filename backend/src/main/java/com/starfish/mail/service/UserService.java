package com.starfish.mail.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.UserEntity;
import com.starfish.mail.model.UserCreateRequest;
import com.starfish.mail.model.UserQueryRequest;
import com.starfish.mail.model.UserUpdateRequest;

import java.util.List;

/**
 * IUserService
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2018-05-23
 */
public interface UserService {

    int deleteById(Long id);

    Long insert(UserEntity record);

    UserEntity selectById(Long id);

    int updateById(UserEntity record);

    /**
     * 根据条件查询单个用户
     *
     * @param queryWrapper 查询条件
     * @return 用户实体
     */
    UserEntity selectOne(LambdaQueryWrapper<UserEntity> queryWrapper);

    /**
     * 分页查询用户（使用MyBatisPlus分页）
     *
     * @param queryRequest 查询条件
     * @return 分页结果
     */
    IPage<UserEntity> selectPageByQuery(UserQueryRequest queryRequest);

    /**
     * 创建用户
     *
     * @param createRequest 创建请求
     * @return 用户ID
     */
    Long createUser(UserCreateRequest createRequest);

    /**
     * 更新用户
     *
     * @param updateRequest 更新请求
     * @return 影响行数
     */
    int updateUser(UserUpdateRequest updateRequest);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteUser(Long id);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    UserEntity getUserById(Long id);

    /**
     * 查询全表最大ID
     *
     * @return 结果
     */
    Long selectMaxId();

}
