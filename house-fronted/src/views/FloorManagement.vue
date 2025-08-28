<template>
  <div class="floor-management">
    <!-- 背景装饰 -->
    <div class="page-background">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-particles">
        <div class="particle" v-for="n in 8" :key="n" :style="getParticleStyle(n)"></div>
      </div>
    </div>

    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside
          :width="sidebarCollapsed ? '80px' : '280px'"
          class="sidebar glass-sidebar"
          :class="{
          'collapsed': sidebarCollapsed,
          'mobile-open': mobileMenuOpen 
        }"
      >
        <div class="sidebar-header">
          <el-button
              v-if="!sidebarCollapsed"
              type="text"
              @click="goBack"
              class="back-btn enhanced-back-btn"
              size="large"
          >
            <el-icon>
              <ArrowLeft/>
            </el-icon>
            返回首页
          </el-button>
          <div class="sidebar-title" v-if="!sidebarCollapsed">
            <el-icon size="24" color="#667eea">
              <OfficeBuilding/>
            </el-icon>
            <h3>{{ floorTitle }}管理</h3>
          </div>
          <!-- 折叠状态下的简化标题 -->
          <div class="sidebar-title-collapsed" v-if="sidebarCollapsed">
            <el-icon size="24" color="#667eea">
              <OfficeBuilding/>
            </el-icon>
          </div>
        </div>

        <el-menu
            :default-active="activeMenu"
            class="sidebar-menu enhanced-menu"
            @select="handleMenuSelect"
            :unique-opened="true"
            mode="vertical"
        >
          <el-menu-item index="home" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <House/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">主页</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
          <el-menu-item index="tenants" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <UserFilled/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">联系人管理</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
          <el-menu-item index="rooms" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <House/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">房间管理</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
          <el-menu-item index="rentalInfo" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <House/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">租房信息</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
          <el-menu-item index="contracts" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <Document/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">合同管理</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
          <el-menu-item index="payments" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <Money/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">费用管理</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
          <el-menu-item index="rental-records" class="menu-item enhanced-item">
            <div class="menu-item-content">
              <div class="menu-icon-wrapper">
                <el-icon size="20">
                  <DocumentCopy/>
                </el-icon>
                <div class="icon-bg"></div>
              </div>
              <span class="menu-text" v-if="!sidebarCollapsed">缴费记录</span>
              <div class="menu-indicator" v-if="!sidebarCollapsed"></div>
            </div>
          </el-menu-item>
        </el-menu>

        <!-- 侧边栏底部装饰 -->
        <div class="sidebar-footer" v-if="!sidebarCollapsed">
          <div class="footer-decoration">
            <div class="decoration-line"></div>
            <div class="decoration-text">房客管理系统</div>
            <div class="decoration-line"></div>
          </div>
        </div>
      </el-aside>

      <!-- 主内容区 -->
      <el-container class="content-container">
        <el-header class="content-header glass-header">
          <div class="header-left">
            <!-- 侧边栏切换按钮 -->
            <el-button
                @click="toggleSidebar"
                class="sidebar-toggle-btn"
                type="text"
                size="large"
            >
              <el-icon>
                <Expand v-if="sidebarCollapsed"/>
                <Fold v-else/>
              </el-icon>
            </el-button>
            <div class="breadcrumb-container">
              <h2 class="page-title">{{ pageTitle }}</h2>
              <el-breadcrumb separator="/" class="enhanced-breadcrumb">
                <el-breadcrumb-item class="breadcrumb-item">
                  <el-icon>
                    <OfficeBuilding/>
                  </el-icon>
                  {{ floorTitle }}
                </el-breadcrumb-item>
                <el-breadcrumb-item class="breadcrumb-item active">
                  {{ pageTitle }}
                </el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleCommand" class="user-dropdown enhanced-dropdown">
              <span class="user-profile">
                <div class="user-avatar">
                  <el-icon><User/></el-icon>
                </div>
                <span class="username">{{ userStore.user?.adminName || '管理员' }}</span>
                <el-icon class="dropdown-arrow"><ArrowDown/></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu class="enhanced-dropdown-menu">
                  <el-dropdown-item command="adminList" class="dropdown-item">
                    <el-icon>
                      <UserFilled/>
                    </el-icon>
                    管理员列表
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout" class="dropdown-item">
                    <el-icon>
                      <SwitchButton/>
                    </el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <el-main class="content-main enhanced-main">
          <div class="content-wrapper">
            <router-view/>
          </div>
        </el-main>
      </el-container>
    </el-container>

    <!-- 管理员表单对话框 -->
    <el-dialog
        v-model="adminFormDialogVisible"
        :title="isEditMode ? '编辑管理员' : '新增管理员'"
        width="500px"
        class="admin-form-dialog"
        @close="resetAdminForm"
    >
      <el-form
          ref="adminFormRef"
          :model="adminForm"
          :rules="adminFormRules"
          label-width="100px"
          class="admin-form"
      >
        <el-form-item label="管理员名称" prop="adminName">
          <el-input
              v-model="adminForm.adminName"
              placeholder="请输入管理员名称"
              clearable
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
              v-model="adminForm.password"
              type="password"
              placeholder="请输入密码"
              show-password
              clearable
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="adminFormDialogVisible = false">取消</el-button>
          <el-button
              type="primary"
              @click="submitAdminForm"
              :loading="adminFormSubmitting"
          >
            {{ isEditMode ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 管理员列表对话框 -->
    <el-dialog
        v-model="adminListDialogVisible"
        title="管理员列表"
        width="700px"
        class="admin-list-dialog"
    >
      <div v-loading="adminListLoading" class="admin-list-content">
        <div class="admin-list-header">
          <el-button 
              type="primary" 
              @click="showAdminForm()"
              class="add-admin-btn"
          >
            <el-icon><Plus/></el-icon>
            新增管理员
          </el-button>
        </div>
        <el-table 
            :data="adminList" 
            style="width: 100%"
            class="admin-table"
            empty-text="暂无管理员数据"
        >
          <el-table-column prop="id" label="ID" width="80" align="center"/>
          <el-table-column prop="adminName" label="管理员名称" min-width="120"/>
          <el-table-column prop="lastLogin" label="最后登录时间" min-width="160">
            <template #default="scope">
              <span v-if="scope.row.lastLogin">
                {{ new Date(scope.row.lastLogin).toLocaleString() }}
              </span>
              <span v-else class="text-gray-400">从未登录</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" align="center">
            <template #default="scope">
              <el-button
                  size="small"
                  type="primary"
                  @click="showAdminForm(scope.row)"
                  class="edit-btn"
              >
                <el-icon><Edit/></el-icon>
                编辑
              </el-button>
              <el-button
                  size="small"
                  type="danger"
                  @click="handleDeleteAdmin(scope.row)"
                  class="delete-btn"
              >
                <el-icon><Delete/></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="adminListDialogVisible = false" type="primary">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, computed, onMounted, onUnmounted, watch, nextTick} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {useUserStore} from '../stores/user'
import {ElMessage, ElMessageBox} from 'element-plus'
import {getAdminList, createAdmin, updateAdmin, deleteAdmin, type Admin, type AdminFormData} from '../api/auth'
import {
  ArrowLeft,
  House,
  UserFilled,
  Document,
  DocumentCopy,
  Money,
  User,
  ArrowDown,
  Setting,
  OfficeBuilding,
  SwitchButton,
  Expand,
  Fold,
  Plus,
  Edit,
  Delete
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 组件状态标识
const isComponentMounted = ref(false)
const isComponentDestroyed = ref(false)

// 侧边栏折叠状态
const sidebarCollapsed = ref(false)
// 移动端侧边栏显示状态
const mobileMenuOpen = ref(false)

// 管理员列表相关状态
const adminList = ref<Admin[]>([])
const adminListDialogVisible = ref(false)
const adminListLoading = ref(false)

// 管理员表单相关状态
const adminFormDialogVisible = ref(false)
const adminFormSubmitting = ref(false)
const isEditMode = ref(false)
const adminFormRef = ref()
const adminForm = ref<AdminFormData>({
  adminName: '',
  password: ''
})

// 表单验证规则
const adminFormRules = {
  adminName: [
    { required: true, message: '请输入管理员名称', trigger: 'blur' },
    { min: 2, max: 50, message: '管理员名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不少于 6 个字符', trigger: 'blur' }
  ]
}

// 当前楼层
const currentFloor = computed(() => route.params.floor as string)

// 楼层标题
const floorTitle = computed(() => {
  return currentFloor.value === '5' ? '五楼' : '六楼'
})

// 页面标题
const pageTitle = computed(() => {
  const titleMap: Record<string, string> = {
    'home': '主页',
    'rooms': '房间管理',
    'tenants': '联系人管理',
    'rentalInfo': '租房信息',
    'contracts': '合同管理',
    'payments': '费用管理',
    'rental-records': '缴费记录'
  }
  const currentPageType = titleMap[route.name as string] || titleMap[activeMenu.value] || '管理系统'
  return `${currentPageType}`
})

// 当前激活的菜单
const activeMenu = ref('home')

// 生成粒子样式
const getParticleStyle = (index: number) => {
  const size = Math.random() * 3 + 1
  const left = Math.random() * 100
  const top = Math.random() * 100
  const animationDelay = Math.random() * 8
  const animationDuration = Math.random() * 15 + 10

  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    top: `${top}%`,
    animationDelay: `${animationDelay}s`,
    animationDuration: `${animationDuration}s`
  }
}

// 切换侧边栏
const toggleSidebar = () => {
  // 添加组件状态检查
  if (isComponentDestroyed.value) return

  try {
    // 检查是否为移动端
    if (window.innerWidth <= 768) {
      mobileMenuOpen.value = !mobileMenuOpen.value
    } else {
      sidebarCollapsed.value = !sidebarCollapsed.value
    }
  } catch (error) {
    console.warn('切换侧边栏时发生错误:', error)
  }
}

// 监听窗口大小变化 - 改进版本
const handleResize = () => {
  // 添加防抖和组件状态检查
  if (isComponentDestroyed.value) return

  try {
    if (window && window.innerWidth > 768) {
      mobileMenuOpen.value = false
    }
  } catch (error) {
    console.warn('处理窗口大小变化时发生错误:', error)
  }
}

// 返回首页
const goBack = () => {
  if (isComponentDestroyed.value) return

  try {
    router.push('/')
  } catch (error) {
    console.warn('路由跳转时发生错误:', error)
  }
}

// 菜单选择处理 - 改进版本
const handleMenuSelect = async (key: string) => {
  if (isComponentDestroyed.value) return

  try {
    console.log('Menu selected:', key)
    console.log('Current activeMenu:', activeMenu.value)
    activeMenu.value = key
    console.log('Updated activeMenu:', activeMenu.value)

    // 等待下一个tick确保状态更新完成
    await nextTick()

    router.push(`/floor/${currentFloor.value}/${key}`)
  } catch (error) {
    console.warn('菜单选择处理时发生错误:', error)
  }
}

// 获取管理员列表
const fetchAdminList = async () => {
  if (isComponentDestroyed.value) return

  try {
    adminListLoading.value = true
    const response = await getAdminList()
    if (response.code === 200) {
      adminList.value = response.data || []
      if (adminList.value.length === 0) {
        ElMessage.warning('暂无管理员数据')
      }
    } else {
      ElMessage.error(response.message || '获取管理员列表失败')
    }
  } catch (error) {
    console.warn('获取管理员列表时发生错误:', error)
    ElMessage.error('无法连接到服务器，请确保后端服务已启动')
  } finally {
    adminListLoading.value = false
  }
}

// 显示管理员列表
const showAdminList = async () => {
  if (isComponentDestroyed.value) return

  try {
    adminListDialogVisible.value = true
    await fetchAdminList()
  } catch (error) {
    console.warn('显示管理员列表时发生错误:', error)
  }
}

// 下拉菜单命令处理
const handleCommand = (command: string) => {
  if (isComponentDestroyed.value) return

  try {
    if (command === 'logout') {
      userStore.logout()
      router.push('/login')
      ElMessage.success('已退出登录')
    } else if (command === 'adminList') {
      showAdminList()
    }
  } catch (error) {
    console.warn('处理下拉菜单命令时发生错误:', error)
  }
}

// 重置管理员表单
const resetAdminForm = () => {
  if (isComponentDestroyed.value) return

  try {
    adminForm.value = {
      adminName: '',
      password: ''
    }
    isEditMode.value = false
    adminFormRef.value?.clearValidate()
  } catch (error) {
    console.warn('重置表单时发生错误:', error)
  }
}

// 显示管理员表单对话框
const showAdminForm = (admin?: Admin) => {
  if (isComponentDestroyed.value) return

  try {
    if (admin) {
      // 编辑模式
      isEditMode.value = true
      adminForm.value = {
        id: admin.id,
        adminName: admin.adminName,
        password: '' // 编辑时密码留空，用户可选择修改
      }
    } else {
      // 新增模式
      isEditMode.value = false
      adminForm.value = {
        adminName: '',
        password: ''
      }
    }
    adminFormDialogVisible.value = true
  } catch (error) {
    console.warn('显示管理员表单时发生错误:', error)
  }
}

// 提交管理员表单
const submitAdminForm = async () => {
  if (isComponentDestroyed.value) return

  try {
    // 表单验证
    const valid = await adminFormRef.value?.validate()
    if (!valid) return

    adminFormSubmitting.value = true

    if (isEditMode.value && adminForm.value.id) {
      // 更新管理员
      const response = await updateAdmin(adminForm.value.id, adminForm.value)
      if (response.code === 200) {
        ElMessage.success('管理员更新成功')
        adminFormDialogVisible.value = false
        await fetchAdminList() // 刷新列表
      } else {
        ElMessage.error(response.message || '更新管理员失败')
      }
    } else {
      // 创建新管理员
      const response = await createAdmin(adminForm.value.adminName, adminForm.value.password)
      if (response.code === 200) {
        ElMessage.success('管理员创建成功')
        adminFormDialogVisible.value = false
        await fetchAdminList() // 刷新列表
      } else {
        ElMessage.error(response.message || '创建管理员失败')
      }
    }
  } catch (error) {
    console.warn('提交管理员表单时发生错误:', error)
    ElMessage.error('操作失败，请稍后重试')
  } finally {
    adminFormSubmitting.value = false
  }
}

// 删除管理员
const handleDeleteAdmin = async (admin: Admin) => {
  if (isComponentDestroyed.value) return

  try {
    await ElMessageBox.confirm(
      `确定要删除管理员 "${admin.adminName}" 吗？此操作不可撤销。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    const response = await deleteAdmin(admin.id)
    if (response.code === 200) {
      ElMessage.success('管理员删除成功')
      await fetchAdminList() // 刷新列表
    } else {
      ElMessage.error(response.message || '删除管理员失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.warn('删除管理员时发生错误:', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }
}

// 监听路由变化 - 改进版本
watch(() => route.name, (newName) => {
  if (isComponentDestroyed.value) return

  try {
    if (newName) {
      activeMenu.value = newName as string
    }
  } catch (error) {
    console.warn('路由变化监听时发生错误:', error)
  }
}, {immediate: true})

// 监听路由路径变化，确保激活状态正确 - 改进版本
watch(() => route.path, () => {
  if (isComponentDestroyed.value) return

  try {
    // 从路径中提取当前页面类型
    const pathParts = route.path.split('/')
    const currentPage = pathParts[pathParts.length - 1]
    if (currentPage && ['home', 'rooms', 'tenants', 'rentalInfo', 'contracts', 'payments', 'rental-records', 'setting'].includes(currentPage)) {
      activeMenu.value = currentPage
    }
  } catch (error) {
    console.warn('路径变化监听时发生错误:', error)
  }
}, {immediate: true})

onMounted(async () => {
  try {
    isComponentMounted.value = true
    isComponentDestroyed.value = false

    // 等待DOM渲染完成
    await nextTick()

    // 初始化激活菜单
    if (route.name) {
      activeMenu.value = route.name as string
    } else {
      // 如果没有路由名称，从路径中提取
      const pathParts = route.path.split('/')
      const currentPage = pathParts[pathParts.length - 1]
      if (currentPage && ['home', 'rooms', 'tenants', 'rentalInfo', 'contracts', 'payments', 'setting'].includes(currentPage)) {
        activeMenu.value = currentPage
      } else {
        // 默认激活主页
        activeMenu.value = 'home'
      }
    }

    // 监听窗口大小变化 - 添加被动事件监听器
    window.addEventListener('resize', handleResize, {passive: true})

  } catch (error) {
    console.warn('组件挂载时发生错误:', error)
  }
})

onUnmounted(() => {
  try {
    // 标记组件已销毁
    isComponentDestroyed.value = true
    isComponentMounted.value = false

    // 移除窗口大小监听器
    if (window && window.removeEventListener) {
      window.removeEventListener('resize', handleResize)
    }
  } catch (error) {
    console.warn('组件卸载时发生错误:', error)
  }
})
</script>

<style scoped>
.floor-management {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.page-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

.bg-gradient-1,
.bg-gradient-2 {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.1;
  filter: blur(80px);
}

.bg-gradient-1 {
  background: radial-gradient(circle at 20% 30%, #4facfe 0%, transparent 50%);
}

.bg-gradient-2 {
  background: radial-gradient(circle at 80% 70%, #00f2fe 0%, transparent 50%);
}

.bg-shape {
  position: absolute;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 50%;
  filter: blur(40px);
}

.bg-shape-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  right: 10%;
  animation: float 20s ease-in-out infinite;
}

.bg-shape-2 {
  width: 150px;
  height: 150px;
  bottom: 20%;
  left: 15%;
  animation: float 25s ease-in-out infinite reverse;
}

.bg-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: particleFloat linear infinite;
}

@keyframes particleFloat {
  0% {
    transform: translateY(100%) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100%) rotate(360deg);
    opacity: 0;
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

/* 主容器 */
.main-container {
  height: 100vh;
  position: relative;
  z-index: 1;
}

/* 侧边栏样式 */
.glass-sidebar {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-right: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 折叠状态样式 */
.glass-sidebar.collapsed {
  width: 80px !important;
}

.glass-sidebar.collapsed .sidebar-header {
  padding: 24px 12px;
  text-align: center;
}

.glass-sidebar.collapsed .enhanced-menu {
  padding: 0 8px;
}

.glass-sidebar.collapsed .menu-item-content {
  justify-content: center;
  padding: 16px 8px;
}

.glass-sidebar.collapsed .menu-icon-wrapper {
  margin-right: 0;
}

/* 折叠状态下的标题样式 */
.sidebar-title-collapsed {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 8px 0;
}

/* 切换按钮样式 */
.sidebar-toggle-btn {
  margin-right: 16px;
  padding: 12px;
  border-radius: 12px;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  color: #667eea;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 44px;
  height: 44px;
}

.sidebar-toggle-btn:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.3);
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

/* 头部左侧布局调整 */
.header-left {
  display: flex;
  align-items: center;
}

.breadcrumb-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.sidebar-header {
  padding: 24px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  background: rgba(248, 250, 252, 0.8);
}

.enhanced-back-btn {
  width: 100%;
  margin-bottom: 16px;
  padding: 12px 16px;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  border-radius: 12px;
  color: #667eea;
  font-weight: 600;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
}

.enhanced-back-btn:hover {
  background: rgba(102, 126, 234, 0.15);
  border-color: rgba(102, 126, 234, 0.3);
  transform: translateX(-4px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.sidebar-title h3 {
  margin: 0;
  color: #1a202c;
  font-size: 20px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 菜单样式 */
.enhanced-menu {
  border: none;
  background: transparent;
  padding: 0 12px;
}

.menu-item.enhanced-item {
  margin: 8px 0;
  border-radius: 16px;
  padding: 0;
  height: auto;
  line-height: normal;
  background: transparent;
  border: 1px solid transparent;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.menu-item.enhanced-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1));
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.menu-item.enhanced-item:hover::before {
  transform: translateX(100%);
}

.menu-item.enhanced-item:hover {
  background: rgba(102, 126, 234, 0.08);
  border-color: rgba(102, 126, 234, 0.15);
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.menu-item.enhanced-item.is-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

/* 添加更具体的激活状态选择器 */
.enhanced-menu .el-menu-item.is-active,
.enhanced-menu .el-menu-item.is-active.enhanced-item {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: white !important;
  border-color: transparent !important;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3) !important;
}

.enhanced-menu .el-menu-item.is-active .menu-text,
.enhanced-menu .el-menu-item.is-active.enhanced-item .menu-text {
  color: white !important;
}

.enhanced-menu .el-menu-item.is-active .menu-icon-wrapper,
.enhanced-menu .el-menu-item.is-active.enhanced-item .menu-icon-wrapper {
  background: rgba(255, 255, 255, 0.2) !important;
  border-color: rgba(255, 255, 255, 0.3) !important;
}

.enhanced-menu .el-menu-item.is-active .menu-indicator,
.enhanced-menu .el-menu-item.is-active.enhanced-item .menu-indicator {
  opacity: 1 !important;
  background: rgba(255, 255, 255, 0.8) !important;
}

.menu-item-content {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  width: 100%;
  position: relative;
}

.menu-icon-wrapper {
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  margin-right: 12px;
}

.menu-item.enhanced-item.is-active .menu-icon-wrapper {
  background: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.3);
}

.icon-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.1);
  opacity: 0;
  transition: all 0.3s ease;
}

.menu-item.enhanced-item:hover .icon-bg {
  opacity: 1;
  transform: scale(1.1);
}

.menu-text {
  font-size: 15px;
  font-weight: 600;
  flex: 1;
  color: #374151;
  transition: all 0.3s ease;
}

.menu-item.enhanced-item.is-active .menu-text {
  color: white;
}

.menu-indicator {
  width: 4px;
  height: 20px;
  border-radius: 2px;
  background: rgba(102, 126, 234, 0.3);
  opacity: 0;
  transition: all 0.3s ease;
}

.menu-item.enhanced-item.is-active .menu-indicator {
  opacity: 1;
  background: rgba(255, 255, 255, 0.8);
}

/* 侧边栏底部 */
.sidebar-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  background: rgba(248, 250, 252, 0.8);
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.footer-decoration {
  display: flex;
  align-items: center;
  gap: 8px;
}

.decoration-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #667eea, transparent);
}

.decoration-text {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 500;
}

/* 内容区域 */
.content-container {
  position: relative;
}

.glass-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 32px;
  height: 80px;
}

.page-title {
  margin: 0;
  color: #1a202c;
  font-size: 24px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.enhanced-breadcrumb :deep(.el-breadcrumb__item) {
  font-weight: 500;
}

.breadcrumb-item {
  color: #6b7280;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: color 0.3s ease;
}

.breadcrumb-item.active {
  color: #667eea;
  font-weight: 600;
}

/* 用户下拉 */
.enhanced-dropdown {
  cursor: pointer;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  border-radius: 20px;
  background: rgba(102, 126, 234, 0.08);
  border: 1px solid rgba(102, 126, 234, 0.15);
  transition: all 0.3s ease;
}

.user-profile:hover {
  background: rgba(102, 126, 234, 0.12);
  border-color: rgba(102, 126, 234, 0.25);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.15);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.username {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.dropdown-arrow {
  color: #9ca3af;
  transition: transform 0.3s ease;
}

.user-profile:hover .dropdown-arrow {
  transform: rotate(180deg);
}

.enhanced-dropdown-menu {
  border-radius: 12px;
  border: none;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  transition: all 0.3s ease;
}

.dropdown-item:hover {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
}

/* 主内容 */
.enhanced-main {
  background: transparent;
  padding: 24px 32px;
  position: relative;
}

.content-wrapper {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  min-height: calc(100vh - 160px);
  padding: 24px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .glass-sidebar {
    width: 220px !important;
  }

  .glass-sidebar.collapsed {
    width: 70px !important;
  }

  .sidebar-header {
    padding: 20px 16px;
  }

  .enhanced-menu {
    padding: 0 8px;
  }

  .menu-item-content {
    padding: 14px 16px;
  }

  .menu-text {
    font-size: 14px;
  }

  .sidebar-toggle-btn {
    margin-right: 12px;
    min-width: 40px;
    height: 40px;
  }
}

@media (max-width: 768px) {
  .glass-sidebar {
    position: fixed;
    z-index: 1000;
    width: 280px !important;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
  }

  .glass-sidebar.mobile-open {
    transform: translateX(0);
  }

  .glass-sidebar.collapsed {
    width: 80px !important;
  }

  .glass-sidebar.collapsed.mobile-open {
    transform: translateX(0);
  }

  .glass-header {
    padding: 0 20px;
    height: 70px;
  }

  .page-title {
    font-size: 20px;
  }

  .enhanced-main {
    padding: 20px;
  }

  .content-wrapper {
    border-radius: 16px;
    padding: 20px;
  }

  .sidebar-toggle-btn {
    margin-right: 8px;
    min-width: 36px;
    height: 36px;
  }
}

/* 动画效果 */
@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.glass-sidebar {
  animation: slideInLeft 0.6s ease-out;
}

.content-container {
  animation: slideInRight 0.6s ease-out;
}

.content-wrapper {
  animation: slideInUp 0.8s ease-out;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 管理员列表对话框样式 */
:deep(.admin-list-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.admin-list-dialog .el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 24px;
  margin: 0;
}

:deep(.admin-list-dialog .el-dialog__title) {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

:deep(.admin-list-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-size: 20px;
}

:deep(.admin-list-dialog .el-dialog__body) {
  padding: 24px;
  background: rgba(255, 255, 255, 0.98);
}

.admin-list-content {
  min-height: 200px;
}

:deep(.admin-table) {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

:deep(.admin-table .el-table__header) {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

:deep(.admin-table .el-table__header th) {
  background: transparent;
  color: #374151;
  font-weight: 600;
  border-bottom: 2px solid #e5e7eb;
}

:deep(.admin-table .el-table__body tr:hover) {
  background: rgba(102, 126, 234, 0.05);
}

:deep(.admin-table .el-table__body td) {
  border-bottom: 1px solid #f3f4f6;
  color: #374151;
}

.text-gray-400 {
  color: #9ca3af;
  font-style: italic;
}

:deep(.admin-list-dialog .el-dialog__footer) {
  padding: 16px 24px 24px;
  background: rgba(255, 255, 255, 0.98);
  border-top: 1px solid #e5e7eb;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}

:deep(.admin-list-dialog .dialog-footer .el-button) {
  padding: 10px 24px;
  border-radius: 8px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

:deep(.admin-list-dialog .dialog-footer .el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

/* 管理员列表头部样式 */
.admin-list-header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.add-admin-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 12px 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.add-admin-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

/* 操作按钮样式 */
.edit-btn, .delete-btn {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  margin: 0 2px;
  transition: all 0.3s ease;
}

.edit-btn {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border: none;
}

.edit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(79, 172, 254, 0.3);
}

.delete-btn {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8e53 100%);
  border: none;
}

.delete-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
}

/* 管理员表单对话框样式 */
:deep(.admin-form-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

:deep(.admin-form-dialog .el-dialog__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 24px;
  margin: 0;
}

:deep(.admin-form-dialog .el-dialog__title) {
  color: white;
  font-size: 18px;
  font-weight: 600;
}

:deep(.admin-form-dialog .el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-size: 20px;
}

:deep(.admin-form-dialog .el-dialog__body) {
  padding: 24px;
  background: rgba(255, 255, 255, 0.98);
}

.admin-form {
  margin-bottom: 20px;
}

:deep(.admin-form .el-form-item__label) {
  font-weight: 600;
  color: #374151;
}

:deep(.admin-form .el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1px solid #e5e7eb;
  transition: all 0.3s ease;
}

:deep(.admin-form .el-input__wrapper:hover) {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

:deep(.admin-form .el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

:deep(.admin-form-dialog .el-dialog__footer) {
  padding: 16px 24px 24px;
  background: rgba(255, 255, 255, 0.98);
  border-top: 1px solid #e5e7eb;
}

:deep(.admin-form-dialog .dialog-footer .el-button) {
  padding: 10px 24px;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

:deep(.admin-form-dialog .dialog-footer .el-button--primary) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

:deep(.admin-form-dialog .dialog-footer .el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}
</style>