<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>📧 邮件推送系统</h2>
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
    
    <div class="text-center mt-3 text-white">
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
        
        if (response.code === 200 && response.data && response.data.token) {
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  max-width: 450px;
  width: 100%;
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.login-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  text-align: center;
}

.login-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
}

.login-header p {
  margin: 10px 0 0 0;
  opacity: 0.9;
  font-size: 14px;
}

.login-body {
  padding: 40px 30px;
}

.btn-login {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.btn-login:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.text-white {
  color: white;
}

.mt-3 {
  margin-top: 20px;
}

.text-center {
  text-align: center;
}

/* 版权信息固定在底部 */
.login-container > .text-center {
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
  margin: 0;
}
</style>
