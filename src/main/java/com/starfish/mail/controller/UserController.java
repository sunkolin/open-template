package com.starfish.mail.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.UserEntity;
import com.starfish.mail.model.Result;
import com.starfish.mail.model.UserCreateRequest;
import com.starfish.mail.model.UserQueryRequest;
import com.starfish.mail.model.UserUpdateRequest;
import com.starfish.mail.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param queryRequest 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Result<IPage<UserEntity>> page(@RequestBody UserQueryRequest queryRequest) {
        // 参数校验
        if (queryRequest.getPageNum() == null || queryRequest.getPageNum() < 1) {
            queryRequest.setPageNum(1);
        }
        if (queryRequest.getPageSize() == null || queryRequest.getPageSize() < 1 || queryRequest.getPageSize() > 100) {
            queryRequest.setPageSize(10);
        }

        IPage<UserEntity> page = userService.selectPageByQuery(queryRequest);
        return Result.success(page);
    }

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<UserEntity> getById(@PathVariable Long id) {
        if (id == null) {
            return Result.fail(400, "用户ID不能为空");
        }

        UserEntity user = userService.getUserById(id);
        if (user == null) {
            return Result.fail(404, "用户不存在");
        }

        return Result.success(user);
    }

    /**
     * 创建用户
     *
     * @param createRequest 创建请求
     * @return 用户ID
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody UserCreateRequest createRequest) {
        // 参数校验
        if (StrUtil.isBlank(createRequest.getUserName())) {
            return Result.fail(400, "用户名不能为空");
        }
        if (StrUtil.isBlank(createRequest.getPassword())) {
            return Result.fail(400, "密码不能为空");
        }

        // 检查用户名是否已存在
        UserEntity existUser = userService.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUserName, createRequest.getUserName())
        );
        if (existUser != null) {
            return Result.fail(400, "用户名已存在");
        }

        Long userId = userService.createUser(createRequest);
        return Result.success(userId);
    }

    /**
     * 更新用户信息
     *
     * @param updateRequest 更新请求
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody UserUpdateRequest updateRequest) {
        // 参数校验
        if (updateRequest.getId() == null) {
            return Result.fail(400, "用户ID不能为空");
        }

        // 检查用户是否存在
        UserEntity existUser = userService.getUserById(updateRequest.getId());
        if (existUser == null) {
            return Result.fail(404, "用户不存在");
        }

        int result = userService.updateUser(updateRequest);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.fail(500, "更新失败");
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (id == null) {
            return Result.fail(400, "用户ID不能为空");
        }

        // 检查用户是否存在
        UserEntity existUser = userService.getUserById(id);
        if (existUser == null) {
            return Result.fail(404, "用户不存在");
        }

        int result = userService.deleteUser(id);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.fail(500, "删除失败");
        }
    }
}
