package com.starfish.mail.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starfish.mail.context.User;
import com.starfish.mail.context.UserContext;
import com.starfish.mail.entity.UserEntity;
import com.starfish.mail.model.LoginRequest;
import com.starfish.mail.model.LoginResponse;
import com.starfish.mail.model.Result;
import com.starfish.mail.service.UserService;
import com.starfish.mail.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // 参数验证
        if (StrUtil.isBlank(loginRequest.getUsername()) || StrUtil.isBlank(loginRequest.getPassword())) {
            return Result.fail(400, "用户名和密码不能为空");
        }

        // 查询用户
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUserName, loginRequest.getUsername());
        UserEntity user = userService.selectOne(queryWrapper);

        if (user == null) {
            return Result.fail(401, "用户名或密码错误");
        }

        // 验证密码（这里使用MD5加密，实际生产环境建议使用BCrypt）
        String encryptedPassword = DigestUtil.md5Hex(loginRequest.getPassword());
        if (!encryptedPassword.equals(user.getPassword())) {
            return Result.fail(401, "用户名或密码错误");
        }

        // 生成JWT Token
        String token = JwtUtil.generateToken(user.getId(), user.getUserName());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUserName());
        response.setNickName(user.getNickName());
        response.setEmail(user.getEmail());
        response.setToken(token);

        return Result.success(response);
    }

    /**
     * 用户登出（前端清除Token即可）
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT是无状态的，前端清除localStorage中的Token即可
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/currentUser")
    public Result<LoginResponse> getCurrentUser() {
        User user = UserContext.getUser();
        if (user == null) {
            return Result.fail(401, "未登录");
        }

        UserEntity userEntity = userService.selectById(user.getUserId());
        if (userEntity == null) {
            return Result.fail(404, "用户不存在");
        }
        
        LoginResponse response = new LoginResponse();
        response.setUserId(userEntity.getId());
        response.setUsername(userEntity.getUserName());
        response.setNickName(userEntity.getNickName());
        response.setEmail(userEntity.getEmail());
        // Token由前端管理，这里不返回

        return Result.success(response);
    }
}
