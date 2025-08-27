<template>
  <div class="login-container">
    <div class="login-form">
      <div class="form-header">
        <h1>房客管理系统</h1>
        <p>请登录您的管理员账号</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form-content"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="adminName">
          <el-input
            v-model="loginForm.adminName"
            size="large"
            placeholder="请输入管理员用户名"
            prefix-icon="User"
            :disabled="loading"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            size="large"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
            :disabled="loading"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleLogin"
            class="login-btn"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="form-footer">
        <el-divider>测试功能</el-divider>
        <div class="test-actions">
          <el-button size="small" @click="testApi" :loading="testLoading">
            测试API连接
          </el-button>
          <el-button size="small" @click="showCreateAdmin = true">
            创建管理员
          </el-button>
        </div>
      </div>
    </div>
    
    <!-- 创建管理员对话框 -->
    <el-dialog
      v-model="showCreateAdmin"
      title="创建管理员账号"
      width="400px"
    >
      <el-form :model="createAdminForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="createAdminForm.adminName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="createAdminForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateAdmin = false">取消</el-button>
          <el-button type="primary" @click="handleCreateAdmin" :loading="createLoading">
            创建
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { login, createAdmin, testApi as testApiRequest } from '../api/auth'
import type { LoginRequest } from '../api/auth'
import { ElMessage, ElForm } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 组件状态
const isComponentDestroyed = ref(false)

// 表单引用
const loginFormRef = ref<InstanceType<typeof ElForm>>()

// 登录表单
const loginForm = reactive<LoginRequest>({
  adminName: '',
  password: ''
})

// 创建管理员表单
const createAdminForm = reactive({
  adminName: '',
  password: ''
})

// 状态
const loading = ref(false)
const testLoading = ref(false)
const createLoading = ref(false)
const showCreateAdmin = ref(false)

// 表单验证规则
const loginRules = {
  adminName: [
    { required: true, message: '请输入管理员用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 处理登录 - 改进版本
const handleLogin = async () => {
  if (isComponentDestroyed.value) return
  
  try {
    if (!loginFormRef.value) return
    
    const valid = await loginFormRef.value.validate().catch(() => false)
    if (!valid) return
    
    loading.value = true
    
    const response = await login(loginForm)
    
    if (response.code === 200 && response.data) {
      userStore.setUser(response.data)
      ElMessage.success('登录成功')
      
      // 等待下一个tick确保状态更新完成
      await nextTick()
      
      if (!isComponentDestroyed.value) {
        router.push('/')
      }
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请检查网络连接')
  } finally {
    if (!isComponentDestroyed.value) {
      loading.value = false
    }
  }
}

// 测试API连接 - 改进版本
const testApi = async () => {
  if (isComponentDestroyed.value) return
  
  testLoading.value = true
  
  try {
    const response = await testApiRequest()
    if (response.code === 200) {
      ElMessage.success('API连接正常')
    }
  } catch (error) {
    console.error('API测试失败:', error)
    ElMessage.error('API连接失败，请检查后端服务是否启动')
  } finally {
    if (!isComponentDestroyed.value) {
      testLoading.value = false
    }
  }
}

// 创建管理员 - 改进版本
const handleCreateAdmin = async () => {
  if (isComponentDestroyed.value) return
  
  if (!createAdminForm.adminName || !createAdminForm.password) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  createLoading.value = true
  
  try {
    const response = await createAdmin(createAdminForm.adminName, createAdminForm.password)
    if (response.code === 200) {
      ElMessage.success('管理员创建成功')
      if (!isComponentDestroyed.value) {
        showCreateAdmin.value = false
        createAdminForm.adminName = ''
        createAdminForm.password = ''
      }
    }
  } catch (error) {
    console.error('创建管理员失败:', error)
    ElMessage.error('创建管理员失败')
  } finally {
    if (!isComponentDestroyed.value) {
      createLoading.value = false
    }
  }
}

onMounted(async () => {
  try {
    isComponentDestroyed.value = false
    
    // 等待DOM渲染完成
    await nextTick()
    
    // 如果已经登录，直接跳转到首页
    userStore.initUser()
    if (userStore.isLoggedIn && !isComponentDestroyed.value) {
      router.push('/')
    }
  } catch (error) {
    console.warn('登录组件挂载时发生错误:', error)
  }
})

onUnmounted(() => {
  try {
    isComponentDestroyed.value = true
  } catch (error) {
    console.warn('登录组件卸载时发生错误:', error)
  }
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.login-form {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
}

.form-header {
  text-align: center;
  margin-bottom: 30px;
}

.form-header h1 {
  color: #303133;
  margin-bottom: 10px;
  font-size: 28px;
  font-weight: 600;
}

.form-header p {
  color: #909399;
  font-size: 14px;
}

.login-form-content {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
}

.form-footer {
  text-align: center;
}

.test-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 15px;
}

.el-divider {
  margin: 20px 0;
}

@media (max-width: 480px) {
  .login-form {
    padding: 30px 20px;
  }
  
  .form-header h1 {
    font-size: 24px;
  }
  
  .test-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .test-actions .el-button {
    width: 120px;
  }
}
</style>