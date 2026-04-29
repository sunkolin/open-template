<template>
  <div class="user-content">
    <h2 class="page-title"><i class="el-icon-user"></i> 用户管理</h2>

    <!-- 搜索工具栏 -->
    <div class="toolbar">
      <div class="search-form">
        <el-input v-model="searchParams.nickName" placeholder="昵称" clearable style="width: 150px; margin-right: 10px;" id="searchNickName" />
        <el-input v-model="searchParams.email" placeholder="邮箱" clearable style="width: 200px; margin-right: 10px;" id="searchEmail" />
        <el-input v-model="searchParams.mobile" placeholder="手机号" clearable style="width: 150px; margin-right: 10px;" id="searchMobile" />
        <el-button type="primary" @click="handleSearch" icon="el-icon-search">搜索</el-button>
        <el-button @click="resetSearch" icon="el-icon-refresh">重置</el-button>
      </div>
      <el-button type="success" @click="showCreateModal" icon="el-icon-plus">新增用户</el-button>
    </div>

    <!-- 表格 -->
    <el-table
      :data="userList"
      v-loading="loading"
      border
      stripe
      style="width: 100%; margin-top: 20px;"
    >
      <el-table-column prop="id" label="ID" align="center" />
      <el-table-column prop="nickName" label="昵称" align="center" />
      <el-table-column prop="email" label="邮箱" align="center" />
      <el-table-column prop="mobile" label="手机号" align="center" />
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" icon="el-icon-edit">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" icon="el-icon-delete">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      style="margin-top: 20px; text-align: right;"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="userForm" :rules="rules" ref="userForm" label-width="100px">
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="userForm.nickName" placeholder="请输入昵称" id="nickName" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" id="password" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" id="email" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="userForm.mobile" placeholder="请输入手机号" id="mobile" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserList, createUser, updateUser, deleteUser } from '@/api/user'

export default {
  name: 'UserManagement',
  data() {
    return {
      loading: false,
      userList: [],
      searchParams: {
        nickName: '',
        email: '',
        mobile: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '新增用户',
      submitLoading: false,
      userForm: {
        id: null,
        nickName: '',
        password: '',
        email: '',
        mobile: ''
      },
      rules: {
        nickName: [
          { required: true, message: '请输入昵称', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    // 加载用户列表
    async loadUsers() {
      this.loading = true
      try {
        const params = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          ...this.searchParams
        }
        
        const response = await getUserList(params)
        
        if (response.code === 0) {
          this.userList = response.data.records || []
          this.total = response.data.total || 0
        }
      } catch (error) {
        console.error('加载用户列表失败:', error)
        this.$message.error('加载用户列表失败')
      } finally {
        this.loading = false
      }
    },
    
    // 搜索
    handleSearch() {
      this.currentPage = 1
      this.loadUsers()
    },
    
    // 重置搜索
    resetSearch() {
      this.searchParams = {
        nickName: '',
        email: '',
        mobile: ''
      }
      this.handleSearch()
    },
    
    // 分页大小变化
    handleSizeChange(val) {
      this.pageSize = val
      this.loadUsers()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadUsers()
    },
    
    // 显示新增对话框
    showCreateModal() {
      this.dialogTitle = '新增用户'
      this.userForm = {
        id: null,
        nickName: '',
        password: '',
        email: '',
        mobile: ''
      }
      this.dialogVisible = true
    },
    
    // 编辑用户
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.userForm = {
        id: row.id,
        nickName: row.nickName,
        password: '',
        email: row.email,
        mobile: row.mobile
      }
      this.dialogVisible = true
    },
    
    // 删除用户
    handleDelete(row) {
      this.$confirm('确定要删除该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteUser(row.id)
          this.$message.success('删除成功')
          this.loadUsers()
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    
    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.userForm.validate()
        
        this.submitLoading = true
        
        if (this.userForm.id) {
          // 编辑
          await updateUser(this.userForm)
          this.$message.success('更新成功')
        } else {
          // 新增
          await createUser(this.userForm)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.loadUsers()
      } catch (error) {
        console.error('提交失败:', error)
        this.$message.error(error.message || '操作失败')
      } finally {
        this.submitLoading = false
      }
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.$refs.userForm.resetFields()
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hours = String(d.getHours()).padStart(2, '0')
      const minutes = String(d.getMinutes()).padStart(2, '0')
      const seconds = String(d.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.user-content {
  width: 100%;
}

.page-title {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 20px;
  font-weight: 600;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  align-items: center;
}
</style>
