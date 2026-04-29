<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-icon">
          <i class="el-icon-s-promotion"></i>
        </div>
        <h2>邮件推送系统</h2>
        <p>Mail Management System</p>
      </div>
      
      <div class="login-body">
        <el-form :model="loginForm" :rules="rules" ref="loginForm" @submit.native.prevent="handleLogin">
          <!-- 邮箱或手机号 -->
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入邮箱或手机号"
              prefix-icon="el-icon-user"
              size="large"
            />
          </el-form-item>
          
          <!-- 密码 -->
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              size="large"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
          
          <!-- 记住我 -->
          <el-form-item>
            <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          </el-form-item>
          
          <!-- 登录按钮 -->
          <el-form-item>
            <el-button 
              type="primary" 
              class="btn-login" 
              size="large"
              :loading="loading"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    
    <div class="text-center text-gray mt-4">
      <small>&copy; 2026 邮件推送系统. All rights reserved.</small>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/user'

export default {
  name: 'LoginPage',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        rememberMe: false
      },
      rules: {
        username: [
          { required: true, message: '请输入邮箱或手机号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    // 检查登录状态
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.$router.push('/')
      }
    },
    
    // 处理登录
    async handleLogin() {
      try {
        await this.$refs.loginForm.validate()
        
        this.loading = true
        
        const response = await login(this.loginForm)
        
        if (response.code === 0 && response.data && response.data.token) {
          // 保存token
          localStorage.setItem('token', response.data.token)
          
          this.$message.success('登录成功，正在跳转...')
          
          // 延迟跳转到主页
          setTimeout(() => {
            this.$router.push('/')
          }, 1000)
        } else {
          this.$message.error(response.message || '登录失败，请重试')
        }
      } catch (error) {
        console.error('登录失败:', error)
        this.$message.error(error.message || '网络错误，请重试')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  background: #f5f7fa;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  padding: 20px;
  position: relative;
}

.login-card {
  max-width: 420px;
  width: 100%;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  padding: 40px 36px;
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.login-header .logo-icon {
  width: 56px;
  height: 56px;
  background: #409EFF;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  margin: 0 auto 16px;
}

.login-header h2 {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #1a1a1a;
  letter-spacing: 0.5px;
}

.login-header p {
  margin: 8px 0 0 0;
  color: #999;
  font-size: 13px;
}

.login-body {
  padding: 0;
}

.btn-login {
  width: 100%;
  background: #409EFF;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  height: 44px;
}

.btn-login:hover {
  background: #66b1ff;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.text-gray {
  color: #999;
}

.mt-4 {
  margin-top: 24px;
}

.text-center {
  text-align: center;
}

/* 版权信息固定在底部 */
.login-container > .text-center {
  position: absolute;
  bottom: 24px;
  left: 0;
  right: 0;
  margin: 0;
  font-size: 13px;
  color: #999;
}
</style>
