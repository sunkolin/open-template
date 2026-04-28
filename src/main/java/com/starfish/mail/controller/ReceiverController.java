package com.starfish.mail.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.starfish.mail.entity.ReceiverEntity;
import com.starfish.mail.model.ReceiverCreateRequest;
import com.starfish.mail.model.ReceiverQueryRequest;
import com.starfish.mail.model.ReceiverUpdateRequest;
import com.starfish.mail.model.Result;
import com.starfish.mail.service.ReceiverService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 收件人管理控制器
 *
 * @author sunkolin
 * @version 1.0.0
 * @since 2026-04-23
 */
@RestController
@RequestMapping("/receiver")
public class ReceiverController {

    @Resource
    private ReceiverService receiverService;

    /**
     * 分页查询收件人列表
     *
     * @param queryRequest 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Result<IPage<ReceiverEntity>> page(@RequestBody ReceiverQueryRequest queryRequest) {
        // 参数校验
        if (queryRequest.getPageNum() == null || queryRequest.getPageNum() < 1) {
            queryRequest.setPageNum(1);
        }
        if (queryRequest.getPageSize() == null || queryRequest.getPageSize() < 1 || queryRequest.getPageSize() > 100) {
            queryRequest.setPageSize(10);
        }

        IPage<ReceiverEntity> page = receiverService.selectPageByQuery(queryRequest);
        return Result.success(page);
    }

    /**
     * 根据ID查询收件人详情
     *
     * @param id 收件人ID
     * @return 收件人信息
     */
    @GetMapping("/{id}")
    public Result<ReceiverEntity> getById(@PathVariable Long id) {
        if (id == null) {
            return Result.fail(400, "收件人ID不能为空");
        }

        ReceiverEntity receiver = receiverService.getReceiverById(id);
        if (receiver == null) {
            return Result.fail(404, "收件人不存在");
        }

        return Result.success(receiver);
    }

    /**
     * 创建收件人
     *
     * @param createRequest 创建请求
     * @return 收件人ID
     */
    @PostMapping("/create")
    public Result<Long> create(@RequestBody ReceiverCreateRequest createRequest) {
        // 参数校验
        if (StrUtil.isBlank(createRequest.getReceiver())) {
            return Result.fail(400, "收件人邮箱不能为空");
        }

        // 检查收件人是否已存在
        ReceiverEntity existReceiver = receiverService.selectOne(
            new LambdaQueryWrapper<ReceiverEntity>()
                .eq(ReceiverEntity::getReceiver, createRequest.getReceiver())
        );
        if (existReceiver != null) {
            return Result.fail(400, "收件人已存在");
        }

        // 设置默认状态为可用
        if (createRequest.getStatus() == null) {
            createRequest.setStatus(1);
        }

        Long receiverId = receiverService.createReceiver(createRequest);
        return Result.success(receiverId);
    }

    /**
     * 更新收件人信息
     *
     * @param updateRequest 更新请求
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody ReceiverUpdateRequest updateRequest) {
        // 参数校验
        if (updateRequest.getId() == null) {
            return Result.fail(400, "收件人ID不能为空");
        }

        // 检查收件人是否存在
        ReceiverEntity existReceiver = receiverService.getReceiverById(updateRequest.getId());
        if (existReceiver == null) {
            return Result.fail(404, "收件人不存在");
        }

        int result = receiverService.updateReceiver(updateRequest);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.fail(500, "更新失败");
        }
    }

    /**
     * 删除收件人
     *
     * @param id 收件人ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (id == null) {
            return Result.fail(400, "收件人ID不能为空");
        }

        // 检查收件人是否存在
        ReceiverEntity existReceiver = receiverService.getReceiverById(id);
        if (existReceiver == null) {
            return Result.fail(404, "收件人不存在");
        }

        int result = receiverService.deleteReceiver(id);
        if (result > 0) {
            return Result.success();
        } else {
            return Result.fail(500, "删除失败");
        }
    }
}
