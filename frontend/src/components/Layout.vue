<template>
  <div class="layout-container">
    <!-- 顶部导航栏 -->
    <div class="top-navbar">
      <a class="navbar-brand" href="#">📧 邮件推送系统</a>
      <div class="user-info">
        <span><i class="el-icon-user"></i> {{ userName }}</span>
        <el-button size="small" @click="handleLogout" icon="el-icon-switch-button">退出登录</el-button>
      </div>
    </div>

    <!-- 左侧菜单栏 -->
    <div class="sidebar">
      <div class="sidebar-header">
        <div class="logo-icon">
          <i class="el-icon-s-promotion"></i>
        </div>
        <span class="logo-text">邮件推送系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        @select="handleMenuSelect"
      >
        <el-menu-item index="home" @click="$router.push('/')">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="user-manage" @click="$router.push('/user')">
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="settings" @click="$router.push('/settings')">
          <i class="el-icon-setting"></i>
          <span>个人设置</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user'
import { mapActions } from 'vuex'

export default {
  name: 'Layout',
  props: {
    activeMenu: {
      type: String,
      default: 'home'
    }
  },
  data() {
    return {
      userName: '加载中...'
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    ...mapActions(['logout']),
    
    // 加载用户信息
    async loadUserInfo() {
      try {
        const response = await getUserInfo()
        if (response.code === 200 && response.data) {
          this.userName = response.data.nickName || response.data.username
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },
    
    // 菜单选择
    handleMenuSelect(index) {
      this.$emit('menu-select', index)
    },
    
    // 退出登录
    handleLogout() {
      this.$confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.logout()
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 60px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.navbar-brand {
  color: white;
  font-weight: 600;
  font-size: 20px;
  text-decoration: none;
}

.user-info {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info span {
  color: white;
}

/* 左侧菜单栏 */
.sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  width: 260px;
  background: #ffffff;
  overflow-y: auto;
  transition: all 0.3s;
  z-index: 999;
  border-right: 1px solid #e8e8e8;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.sidebar-header {
  padding: 24px 20px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.sidebar-menu {
  border: none;
}

/* 主内容区 */
.main-content {
  margin-left: 260px;
  margin-top: 60px;
  padding: 30px;
  min-height: calc(100vh - 60px);
  background: #f5f6fa;
}
</style>
