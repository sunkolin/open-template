<template>
  <div class="settings-content">
    <h2 class="page-title"><i class="el-icon-setting"></i> 个人设置</h2>

    <!-- 基本信息设置 -->
    <div class="settings-section">
      <h3 class="section-title"><i class="el-icon-user"></i> 基本信息</h3>
      <el-form :model="profileForm" :rules="profileRules" ref="profileForm" label-width="100px">
        <el-form-item label="昵称" prop="profileNickName">
          <el-input v-model="profileForm.nickName" placeholder="请输入昵称" id="profileNickName" />
        </el-form-item>

        <el-form-item label="邮箱" prop="profileEmail">
          <el-input v-model="profileForm.email" placeholder="请输入邮箱" id="profileEmail" />
        </el-form-item>

        <el-form-item label="手机号" prop="profileMobile">
          <el-input v-model="profileForm.mobile" placeholder="请输入手机号" id="profileMobile" />
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
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入当前密码" id="oldPassword" />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password id="newPassword" />
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" id="confirmPassword" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading" icon="el-icon-key">
            修改密码
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getUserInfo, updateUserInfo, changePassword } from '@/api/user'

export default {
  name: 'UserSettings',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }

    return {
      profileLoading: false,
      passwordLoading: false,
      profileForm: {
        nickName: '',
        email: '',
        mobile: ''
      },
      profileRules: {
        profileNickName: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        profileEmail: [
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
          { required: true, message: '请输入新密码', trigger: 'blur' }
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
    // 加载用户信息
    async loadUserInfo() {
      try {
        const response = await getUserInfo()
        if (response.code === 0 && response.data) {
          this.profileForm = {
            nickName: response.data.nickName,
            email: response.data.email,
            mobile: response.data.mobile
          }
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
        this.$message.error('加载用户信息失败')
      }
    },
    
    // 更新个人信息
    async handleUpdateProfile() {
      try {
        await this.$refs.profileForm.validate()
        
        this.profileLoading = true
        
        await updateUserInfo({
          nickName: this.profileForm.nickName,
          email: this.profileForm.email,
          mobile: this.profileForm.mobile
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
          newPassword: this.passwordForm.newPassword,
          confirmPassword: this.passwordForm.confirmPassword
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
          // 清除token
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          this.$router.push('/login')
        }, 1500)
      } catch (error) {
        console.error('修改密码失败:', error)
        this.$message.error(error.message || '修改密码失败')
      } finally {
        this.passwordLoading = false
      }
    }
  }
}
</script>

<style scoped>
.settings-content {
  width: 100%;
}

.page-title {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 20px;
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
