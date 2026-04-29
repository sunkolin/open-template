<template>
  <div class="settings-container">
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
        <el-menu-item index="settings">
          <i class="el-icon-setting"></i>
          <span>个人设置</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <div class="content-wrapper">
        <h2 class="page-title"><i class="el-icon-setting"></i> 个人设置</h2>

        <!-- 基本信息设置 -->
        <div class="settings-section">
          <h3 class="section-title"><i class="el-icon-user"></i> 基本信息</h3>
          <el-form :model="profileForm" :rules="profileRules" ref="profileForm" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled />
              <div class="info-text">用户名不可修改</div>
            </el-form-item>

            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="profileForm.nickName" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleUpdateProfile" :loading="profileLoading" icon="el-icon-check">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-divider />

        <!-- 密码修改 -->
        <div class="settings-section">
          <h3 class="section-title"><i class="el-icon-lock"></i> 修改密码</h3>
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="120px">
            <el-form-item label="当前密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入当前密码" />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码（至少6位）" />
              <div class="info-text">密码长度不能少于6位</div>
            </el-form-item>

            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading" icon="el-icon-key">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getUserInfo, updateUserInfo, changePassword } from '@/api/user'
import { mapActions } from 'vuex'

export default {
  name: 'Settings',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    return {
      userName: '加载中...',
      activeMenu: 'settings',
      profileLoading: false,
      passwordLoading: false,
      profileForm: {
        username: '',
        nickName: '',
        email: ''
      },
      profileRules: {
        nickName: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入当前密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
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
          this.profileForm = {
            username: response.data.username,
            nickName: response.data.nickName,
            email: response.data.email
          }
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
        this.$message.error('加载用户信息失败')
      }
    },
    
    // 菜单选择
    handleMenuSelect(index) {
      this.activeMenu = index
    },
    
    // 更新个人信息
    async handleUpdateProfile() {
      try {
        await this.$refs.profileForm.validate()
        
        this.profileLoading = true
        
        await updateUserInfo({
          nickName: this.profileForm.nickName,
          email: this.profileForm.email
        })
        
        this.$message.success('保存成功')
        this.loadUserInfo()
      } catch (error) {
        console.error('更新失败:', error)
        this.$message.error(error.message || '更新失败')
      } finally {
        this.profileLoading = false
      }
    },
    
    // 修改密码
    async handleChangePassword() {
      try {
        await this.$refs.passwordForm.validate()
        
        this.passwordLoading = true
        
        await changePassword({
          oldPassword: this.passwordForm.oldPassword,
          newPassword: this.passwordForm.newPassword
        })
        
        this.$message.success('密码修改成功，请重新登录')
        
        // 清空表单
        this.passwordForm = {
          oldPassword: '',
          newPassword: '',
          confirmPassword: ''
        }
        
        // 延迟退出登录
        setTimeout(() => {
          this.logout()
          this.$router.push('/login')
        }, 1500)
      } catch (error) {
        console.error('修改密码失败:', error)
        this.$message.error(error.message || '修改密码失败')
      } finally {
        this.passwordLoading = false
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
.settings-container {
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

.settings-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 18px;
  color: #34495e;
  margin-bottom: 20px;
  font-weight: 600;
}

.info-text {
  color: #7f8c8d;
  font-size: 14px;
  margin-top: 5px;
}
</style>
