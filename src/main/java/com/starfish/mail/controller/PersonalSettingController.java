package com.starfish.mail.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.starfish.mail.context.User;
import com.starfish.mail.context.UserContext;
import com.starfish.mail.entity.UserEntity;
import com.starfish.mail.model.PasswordChangeRequest;
import com.starfish.mail.model.ProfileUpdateRequest;
import com.starfish.mail.model.Result;
import com.starfish.mail.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 个人设置控制器
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-22
 */
@RestController
@RequestMapping("/auth")
public class PersonalSettingController {

    @Resource
    private UserService userService;

    /**
     * 更新个人信息
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody ProfileUpdateRequest request) {
        User user = UserContext.getUser();
        if (user == null) {
            return Result.fail(401, "未登录");
        }

        UserEntity userEntity = userService.selectById(user.getUserId());
        if (userEntity == null) {
            return Result.fail(404, "用户不存在");
        }

        // 更新昵称
        if (StrUtil.isNotBlank(request.getNickName())) {
            userEntity.setNickName(request.getNickName());
        }

        // 更新邮箱
        if (StrUtil.isNotBlank(request.getEmail())) {
            // 简单的邮箱格式验证
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return Result.fail(400, "邮箱格式不正确");
            }
            userEntity.setEmail(request.getEmail());
        }

        userEntity.setModifyTime(new Date());
        userService.updateById(userEntity);

        return Result.success();
    }

    /**
     * 修改密码
     *
     * @param request 密码修改请求
     * @return 修改结果
     */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody PasswordChangeRequest request) {
        User user = UserContext.getUser();
        if (user == null) {
            return Result.fail(401, "未登录");
        }

        // 验证参数
        if (StrUtil.isBlank(request.getOldPassword()) || 
            StrUtil.isBlank(request.getNewPassword()) || 
            StrUtil.isBlank(request.getConfirmPassword())) {
            return Result.fail(400, "请填写所有密码字段");
        }

        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return Result.fail(400, "两次输入的新密码不一致");
        }

        // 验证新密码长度
        if (request.getNewPassword().length() < 6) {
            return Result.fail(400, "新密码长度不能少于6位");
        }

        UserEntity userEntity = userService.selectById(user.getUserId());
        if (userEntity == null) {
            return Result.fail(404, "用户不存在");
        }

        // 验证旧密码
        String encryptedOldPassword = DigestUtil.md5Hex(request.getOldPassword());
        if (!encryptedOldPassword.equals(userEntity.getPassword())) {
            return Result.fail(400, "旧密码错误");
        }

        // 更新密码
        String encryptedNewPassword = DigestUtil.md5Hex(request.getNewPassword());
        userEntity.setPassword(encryptedNewPassword);
        userEntity.setModifyTime(new Date());
        userService.updateById(userEntity);

        return Result.success();
    }
}
