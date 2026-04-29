<template>
  <div class="layout-container">
    <!-- 顶部导航栏 -->
    <div class="top-navbar">
      <div class="navbar-brand">
        <div class="nav-logo-icon">
          <i class="el-icon-s-promotion"></i>
        </div>
        <span>邮件推送系统</span>
      </div>
      <div class="user-info">
        <span><i class="el-icon-user"></i> {{ userName }}</span>
        <el-button size="small" @click="handleLogout" icon="el-icon-switch-button">退出登录</el-button>
      </div>
    </div>

    <!-- 左侧菜单栏 -->
    <div class="sidebar">
      <el-menu
        :default-active="currentMenuIndex"
        class="sidebar-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="/">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/user">
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/settings">
          <i class="el-icon-setting"></i>
          <span>个人设置</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user'

export default {
  name: 'Layout',
  data() {
    return {
      userName: '加载中...'
    }
  },
  computed: {
    // 根据当前路由获取菜单激活项
    currentMenuIndex() {
      return this.$route.meta.menuIndex || '/'
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    // 加载用户信息
    async loadUserInfo() {
      try {
        const response = await getUserInfo()
        if (response.code === 0 && response.data) {
          this.userName = response.data.nickName || response.data.username
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },
    
    // 菜单选择
    handleMenuSelect(index) {
      // 如果当前路由与点击的路由不同，才进行跳转
      if (this.$route.path !== index) {
        this.$router.push(index).catch(err => {
          // 忽略导航取消错误
          if (err.name !== 'NavigationDuplicated' && !err.message.includes('Navigation cancelled')) {
            console.error('路由跳转失败:', err)
          }
        })
      }
    },
    
    // 退出登录
    handleLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 清除token
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        // 跳转到登录页
        this.$router.push('/login')
        this.$message.success('已退出登录')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.layout-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

/* 顶部导航栏 */
.top-navbar {
  background: #ffffff;
  height: 60px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  padding: 0 24px;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #1a1a1a;
  font-weight: 700;
  font-size: 18px;
  text-decoration: none;
  letter-spacing: 0.5px;
}

.nav-logo-icon {
  width: 36px;
  height: 36px;
  background: #409EFF;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  flex-shrink: 0;
}

.user-info {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info span {
  color: #666;
  font-size: 14px;
}

/* 左侧菜单栏 */
.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  width: 240px;
  background: #fafafa;
  overflow-y: auto;
  transition: all 0.3s;
  z-index: 999;
  border-right: 1px solid #f0f0f0;
  padding: 12px;
}

/* 菜单样式优化 */
.sidebar-menu {
  border: none;
  background: transparent;
}

.sidebar-menu .el-menu-item {
  height: 44px;
  line-height: 44px;
  margin-bottom: 4px;
  border-radius: 8px;
  color: #666;
  font-size: 14px;
  transition: all 0.2s ease;
  padding-left: 16px !important;
}

.sidebar-menu .el-menu-item i {
  color: #999;
  font-size: 18px;
  margin-right: 12px;
  transition: all 0.2s ease;
}

.sidebar-menu .el-menu-item:hover {
  background: #f0f0f0;
  color: #1a1a1a;
}

.sidebar-menu .el-menu-item:hover i {
  color: #409EFF;
}

.sidebar-menu .el-menu-item.is-active {
  background: #E6F7FF;
  color: #409EFF;
  font-weight: 500;
}

.sidebar-menu .el-menu-item.is-active i {
  color: #409EFF;
}

/* 主内容区 */
.main-content {
  margin-left: 240px;
  margin-top: 60px;
  padding: 24px;
  min-height: calc(100vh - 60px);
  background: #f5f6fa;
}
</style>
