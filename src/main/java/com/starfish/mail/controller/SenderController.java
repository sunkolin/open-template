package com.starfish.mail.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.SenderEntity;
import com.starfish.mail.model.Result;
import com.starfish.mail.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 发件人管理Controller
 */
@RestController
@RequestMapping("/sender")
public class SenderController {

    @Autowired
    private SenderService senderService;

    /**
     * 分页查询发件人
     */
    @PostMapping("/page")
    public Result<IPage<SenderEntity>> page(@RequestBody SenderQueryRequest request) {
        int pageNum = request.getPageNum() != null ? request.getPageNum() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        
        IPage<SenderEntity> page = senderService.pageSenders(pageNum, pageSize, 
                request.getSenderEmail(), request.getStatus());
        
        return Result.success(page);
    }

    /**
     * 根据ID查询发件人
     */
    @GetMapping("/{id}")
    public Result<SenderEntity> getById(@PathVariable Integer id) {
        SenderEntity sender = senderService.getById(id);
        if (sender == null) {
            return Result.fail(1003, "发件人不存在");
        }
        // 不返回密码和授权码
        sender.setEmailPassword(null);
        sender.setAuthCode(null);
        return Result.success(sender);
    }

    /**
     * 新增发件人
     */
    @PostMapping("/create")
    public Result<Void> create(@RequestBody SenderEntity sender) {
        // 检查邮箱是否已存在
        SenderEntity existSender = senderService.getByEmail(sender.getSenderEmail());
        if (existSender != null) {
            return Result.fail(1002, "该邮箱已存在");
        }
        
        sender.setStatus(sender.getStatus() != null ? sender.getStatus() : 1);
        sender.setCreateTime(LocalDateTime.now());
        sender.setModifyTime(LocalDateTime.now());
        
        senderService.save(sender);
        return Result.success();
    }

    /**
     * 更新发件人
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody SenderEntity sender) {
        if (sender.getId() == null) {
            return Result.fail(1001, "ID不能为空");
        }
        
        SenderEntity existSender = senderService.getById(sender.getId());
        if (existSender == null) {
            return Result.fail(1003, "发件人不存在");
        }
        
        // 如果修改了邮箱，检查新邮箱是否已被使用
        if (!existSender.getSenderEmail().equals(sender.getSenderEmail())) {
            SenderEntity emailExist = senderService.getByEmail(sender.getSenderEmail());
            if (emailExist != null && !emailExist.getId().equals(sender.getId())) {
                return Result.fail(1001, "该邮箱已被使用");
            }
        }
        
        sender.setModifyTime(LocalDateTime.now());
        senderService.updateById(sender);
        return Result.success();
    }

    /**
     * 删除发件人
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        SenderEntity sender = senderService.getById(id);
        if (sender == null) {
            return Result.fail(1003, "发件人不存在");
        }
        
        senderService.removeById(id);
        return Result.success();
    }

    /**
     * 查询所有有效发件人
     */
    @GetMapping("/list")
    public Result<List<SenderEntity>> list() {
        List<SenderEntity> senders = senderService.lambdaQuery()
                .eq(SenderEntity::getStatus, 1)
                .orderByDesc(SenderEntity::getCreateTime)
                .list();
        
        // 不返回密码和授权码
        senders.forEach(s -> {
            s.setEmailPassword(null);
            s.setAuthCode(null);
        });
        
        return Result.success(senders);
    }

    /**
     * 查询请求类
     */
    @lombok.Data
    static class SenderQueryRequest {
        private Integer pageNum;
        private Integer pageSize;
        private String senderEmail;
        private Integer status;
    }
}
