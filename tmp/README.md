# 临时文件说明

## 目录用途

本目录用于临时存放不再使用但需要保留的文件，以便后续参考或迁移。

## 已移动的文件

### static/ - 原有前端静态资源（已废弃）

**移动时间**: 2026-04-29  
**原因**: 前端已使用Vue重构，原有HTML+Bootstrap代码不再使用  
**原位置**: `backend/src/main/resources/static/`  
**新位置**: `backend/src/main/resources/tmp/static/`

#### 包含内容：
- `login.html` - 原登录页面
- `index.html` - 原首页
- `users.html` - 原用户管理页面
- `settings.html` - 原个人设置页面
- `css/` - 原样式文件
  - `login.css`
  - `index.css`
  - `users.css`
  - `settings.css`
- `js/` - 原JavaScript文件
  - `login.js`
  - `index.js`
  - `users.js`
  - `settings.js`
- `vendor/` - 第三方库
  - Bootstrap
  - Bootstrap Icons

#### 替代方案：
这些功能已在Vue项目中重构，新代码位于：
```
frontend/
├── src/
│   ├── views/
│   │   ├── login/index.vue      # 登录页面
│   │   ├── home/index.vue       # 首页
│   │   ├── user/index.vue       # 用户管理
│   │   └── settings/index.vue   # 个人设置
│   ├── components/
│   │   └── Layout.vue           # 公共布局组件
│   └── ...
```

## 注意事项

1. **不要删除**: 这些文件暂时保留作为参考，确认Vue版本稳定运行后再决定是否删除
2. **不参与构建**: tmp目录下的文件不会被打包到最终的JAR文件中（除非特别配置）
3. **参考用途**: 可以作为Vue重构的对比参考，或用于回滚（如果需要）

## 清理计划

在以下条件满足后，可以安全删除此目录：
- [x] Vue前端已完成重构
- [ ] Vue前端已充分测试并上线运行
- [ ] 确认不再需要原有代码作为参考
- [ ] 团队一致同意删除

预计清理时间：Vue版本稳定运行1-2个月后
