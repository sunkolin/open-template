package com.starfish.mail.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.model.EmailSendRecordModel;
import com.starfish.mail.model.Result;
import com.starfish.mail.service.EmailSendRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件发送记录控制器
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-24
 */
@RestController
@RequestMapping("/api/send-record")
public class EmailSendRecordController {

    @Resource
    private EmailSendRecordService emailSendRecordService;

    /**
     * 分页查询发送记录
     */
    @GetMapping("/page")
    public Result<IPage<EmailSendRecordModel>> pageRecords(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Integer sendStatus) {
        IPage<EmailSendRecordModel> page = emailSendRecordService.pageRecords(pageNum, pageSize, taskId, sendStatus);
        return Result.success(page);
    }

    /**
     * 根据ID查询发送记录
     */
    @GetMapping("/{id}")
    public Result<EmailSendRecordModel> getRecordById(@PathVariable Long id) {
        EmailSendRecordModel record = emailSendRecordService.getRecordById(id);
        if (record == null) {
            return Result.fail(404, "记录不存在");
        }
        return Result.success(record);
    }

    /**
     * 重新发送邮件
     */
    @PostMapping("/{id}/resend")
    public Result<Void> resendEmail(@PathVariable Long id) {
        boolean success = emailSendRecordService.resendEmail(id);
        if (success) {
            return Result.success();
        }
        return Result.fail(500, "重发失败");
    }
}
