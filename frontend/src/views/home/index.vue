<template>
  <div class="home-container">
    <h1>欢迎使用 Starfish 邮件系统</h1>
    <p>首页内容</p>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {}
  },
  mounted() {
    console.log('Home page loaded')
  }
}
</script>

<style scoped>
.home-container {
  padding: 20px;
  text-align: center;
}

h1 {
  color: #333;
  margin-bottom: 20px;
}
</style>
