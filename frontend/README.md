# Starfish Mail System - Frontend

## 项目简介
Starfish 邮件系统的前端项目，基于 Vue 2 + Element UI 构建。

## 技术栈
- Vue 2.7
- Vue Router 3
- Vuex 3
- Element UI 2.15
- Axios 1.6
- Vue CLI 5

## 目录结构
```
frontend/
├── public/               # 静态资源（不参与打包）
│   ├── index.html        # HTML模板
│   └── favicon.ico       # 网站图标
├── src/
│   ├── api/              # API接口（按模块拆分）
│   │   └── user.js       # 用户模块接口
│   ├── assets/           # 静态资源（会被打包）
│   ├── components/       # 公共组件
│   ├── router/           # 路由配置
│   ├── store/            # 状态管理（Vuex）
│   ├── utils/            # 工具类
│   │   └── request.js    # Axios封装
│   ├── views/            # 页面组件
│   │   ├── home/         # 首页
│   │   ├── user/         # 用户管理
│   │   └── login/        # 登录页
│   ├── App.vue           # 根组件
│   └── main.js           # 入口文件
├── .env.development      # 开发环境配置
├── .env.production       # 生产环境配置
├── vue.config.js         # Vue配置
├── package.json          # 依赖配置
└── babel.config.js       # Babel配置
```

## 快速开始

### 安装依赖
```bash
npm install
```

### 开发环境运行
```bash
npm run dev
```
访问: http://localhost:8081

### 生产环境打包
```bash
npm run build
```

### 代码检查
```bash
npm run lint
```

## 环境配置

### 开发环境 (.env.development)
- 后端接口地址: http://localhost:8080
- 前端端口: 8081

### 生产环境 (.env.production)
- 后端接口地址: /api (通过nginx代理)

## 功能模块
- [ ] 用户登录/登出
- [ ] 用户管理
- [ ] 邮件管理
- [ ] 个人信息设置

## 注意事项
1. 确保后端服务已启动（默认端口8080）
2. 开发环境会自动代理API请求到后端
3. Token存储在localStorage中
4. 路由守卫会自动检查登录状态
