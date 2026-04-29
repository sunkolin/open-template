<template>
  <div class="home-container">
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
        <el-menu-item index="home">
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
      <div class="content-wrapper">
        <h2 class="page-title">欢迎使用邮件推送系统</h2>
        
        <div class="welcome-section">
          <h1>👋 欢迎回来！</h1>
          <p>这是一个功能强大的邮件批量推送管理平台，帮助您高效管理邮件发送任务</p>
        </div>

        <div class="feature-grid">
          <div class="feature-card" @click="switchToSection('user-manage')">
            <i class="el-icon-user"></i>
            <h3>用户管理</h3>
            <p>管理系统用户和权限设置</p>
          </div>
          <div class="feature-card" @click="$message.info('功能开发中...')">
            <i class="el-icon-data-analysis"></i>
            <h3>数据统计</h3>
            <p>查看邮件发送统计和分析报告</p>
          </div>
          <div class="feature-card" @click="$message.info('功能开发中...')">
            <i class="el-icon-setting"></i>
            <h3>系统设置</h3>
            <p>配置系统参数和个性化选项</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user'
import { mapActions } from 'vuex'

export default {
  name: 'HomePage',
  data() {
    return {
      userName: '加载中...',
      activeMenu: 'home'
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
      this.activeMenu = index
      this.switchToSection(index)
    },
    
    // 切换到指定视图
    switchToSection(sectionName) {
      if (sectionName === 'user-manage') {
        this.$router.push('/user')
      } else if (sectionName === 'settings') {
        this.$router.push('/settings')
      }
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
.home-container {
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

.content-wrapper {
  background: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  min-height: calc(100vh - 180px);
}

.page-title {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #667eea;
  font-weight: 600;
}

/* 欢迎卡片 */
.welcome-section {
  text-align: center;
  padding: 40px 20px;
}

.welcome-section h1 {
  color: #667eea;
  margin-bottom: 15px;
  font-size: 32px;
}

.welcome-section p {
  color: #7f8c8d;
  font-size: 16px;
}

/* 功能卡片 */
.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.feature-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  padding: 25px;
  color: white;
  transition: all 0.3s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
}

.feature-card i {
  font-size: 48px;
  margin-bottom: 15px;
}

.feature-card h3 {
  font-size: 20px;
  margin-bottom: 10px;
}

.feature-card p {
  opacity: 0.9;
  font-size: 14px;
}
</style>
