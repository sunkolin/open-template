<template>
  <div class="home-content">
    <div class="welcome-section">
      <h1>欢迎回来！</h1>
      <p>这是一个功能强大的邮件批量推送管理平台，帮助您高效管理邮件发送任务</p>
    </div>

    <div class="feature-grid">
      <div class="feature-card" @click="$router.push('/user')">
        <i class="el-icon-user"></i>
        <h3>用户管理</h3>
        <p>管理系统用户和权限设置</p>
      </div>
      <div class="feature-card" @click="$message.info('功能开发中...')">
        <i class="el-icon-data-analysis"></i>
        <h3>数据统计</h3>
        <p>查看邮件发送统计和分析报告</p>
      </div>
      <div class="feature-card" @click="$router.push('/settings')">
        <i class="el-icon-setting"></i>
        <h3>系统设置</h3>
        <p>配置系统参数和个性化选项</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage'
}
</script>

<style scoped>
.home-content {
  width: 100%;
}

.page-title {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #409EFF;
  font-weight: 600;
}

/* 欢迎卡片 */
.welcome-section {
  text-align: center;
  padding: 40px 20px;
}

.welcome-section h1 {
  color: #409EFF;
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
  background: #409EFF;
  border-radius: 10px;
  padding: 25px;
  color: white;
  transition: all 0.3s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(64, 158, 255, 0.4);
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
