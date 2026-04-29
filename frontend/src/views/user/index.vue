<template>
  <div class="user-container">
    <h1>用户管理</h1>
    <p>用户列表页面</p>
  </div>
</template>

<script>
export default {
  name: 'User',
  data() {
    return {}
  },
  mounted() {
    console.log('User page loaded')
  }
}
</script>

<style scoped>
.user-container {
  padding: 20px;
}

h1 {
  color: #333;
  margin-bottom: 20px;
}
</style>
