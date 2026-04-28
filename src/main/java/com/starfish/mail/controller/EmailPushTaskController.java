package com.starfish.mail.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.model.EmailPushTaskCreateRequest;
import com.starfish.mail.model.EmailPushTaskModel;
import com.starfish.mail.model.EmailPushTaskUpdateRequest;
import com.starfish.mail.model.Result;
import com.starfish.mail.service.EmailPushTaskService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件推送任务控制器
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@RestController
@RequestMapping("/api/push-task")
public class EmailPushTaskController {

    @Resource
    private EmailPushTaskService emailPushTaskService;

    /**
     * 分页查询推送任务
     */
    @GetMapping("/page")
    public Result<IPage<EmailPushTaskModel>> pageTasks(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) Integer status) {
        IPage<EmailPushTaskModel> page = emailPushTaskService.pageTasks(pageNum, pageSize, taskName, status);
        return Result.success(page);
    }

    /**
     * 根据ID查询任务详情
     */
    @GetMapping("/{id}")
    public Result<EmailPushTaskModel> getTaskById(@PathVariable Long id) {
        EmailPushTaskModel task = emailPushTaskService.getTaskById(id);
        if (task == null) {
            return Result.fail(404, "任务不存在");
        }
        return Result.success(task);
    }

    /**
     * 创建推送任务
     */
    @PostMapping
    public Result<Long> createTask(@RequestBody EmailPushTaskCreateRequest createRequest) {
        Long taskId = emailPushTaskService.createTask(createRequest);
        return Result.success(taskId);
    }

    /**
     * 更新推送任务
     */
    @PutMapping
    public Result<Void> updateTask(@RequestBody EmailPushTaskUpdateRequest updateRequest) {
        int result = emailPushTaskService.updateTask(updateRequest);
        if (result > 0) {
            return Result.success();
        }
        return Result.fail(500, "更新失败");
    }

    /**
     * 删除推送任务
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTask(@PathVariable Long id) {
        int result = emailPushTaskService.deleteTask(id);
        if (result > 0) {
            return Result.success();
        }
        return Result.fail(500, "删除失败");
    }

    /**
     * 启动推送任务
     */
    @PostMapping("/{id}/start")
    public Result<Void> startTask(@PathVariable Long id) {
        boolean success = emailPushTaskService.startTask(id);
        if (success) {
            return Result.success();
        }
        return Result.fail(500, "启动失败");
    }

    /**
     * 停止推送任务
     */
    @PostMapping("/{id}/stop")
    public Result<Void> stopTask(@PathVariable Long id) {
        boolean success = emailPushTaskService.stopTask(id);
        if (success) {
            return Result.success();
        }
        return Result.fail(500, "停止失败");
    }
}
