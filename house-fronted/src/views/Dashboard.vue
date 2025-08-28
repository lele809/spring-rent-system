<template>
  <div class="dashboard">
    <!-- 背景装饰保持原样 -->
    <div class="background-decoration">
      <div class="decoration-circle circle-1"></div>
      <div class="decoration-circle circle-2"></div>
      <div class="decoration-circle circle-3"></div>
    </div>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <div class="logo-container">
            <el-icon size="28" color="#409eff">
              <OfficeBuilding/>
            </el-icon>
            <h1>房客管理系统</h1>
          </div>
        </div>
        <div class="header-right">
          <el-button
              class="settings-btn"
              @click="goToSettings"
              circle
              size="large"
              :icon="Setting"
              title="系统设置"
          />
          <el-dropdown @command="handleCommand" trigger="hover">
            <div class="user-profile">
              <div class="avatar">
                <el-icon>
                  <User/>
                </el-icon>
              </div>
              <span class="username">{{ userStore.user?.adminName || '管理员' }}</span>
              <el-icon class="dropdown-arrow">
                <ArrowDown/>
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
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

      <el-main class="main">
        <!-- 简化的中心内容区域 -->
        <div class="dashboard-container">
          <div class="dashboard-card">
            <h1 class="system-title">
              <el-icon size="32">
                <OfficeBuilding/>
              </el-icon>
              租房管理系统
            </h1>
            <p class="system-subtitle">请选择要管理的楼层</p>
            <p class="welcome-date">今天是 {{ currentDate }}</p>

            <div class="floor-buttons">
              <button class="floor-btn floor-5" @click="goToFloor('5')">
                <el-icon size="24">
                  <OfficeBuilding/>
                </el-icon>
                <span>五楼管理</span>
                <div class="floor-stats-mini">
                  <span class="rooms-count">{{ floor5Stats.totalRooms }}间房</span>
                  <span class="occupancy-rate">入住率{{ floor5Stats.occupancyRate }}%</span>
                </div>
              </button>

              <button class="floor-btn floor-6" @click="goToFloor('6')">
                <el-icon size="24">
                  <OfficeBuilding/>
                </el-icon>
                <span>六楼管理</span>
                <div class="floor-stats-mini">
                  <span class="rooms-count">{{ floor6Stats.totalRooms }}间房</span>
                  <span class="occupancy-rate">入住率{{ floor6Stats.occupancyRate }}%</span>
                </div>
              </button>
            </div>

            <!-- 简化的统计概览 -->
            <div class="quick-overview">
              <div class="overview-item">
                <el-icon size="20" color="#409eff">
                  <House/>
                </el-icon>
                <span>总房间：{{ totalStats.totalRooms }}</span>
              </div>
              <div class="overview-item">
                <el-icon size="20" color="#67c23a">
                  <UserFilled/>
                </el-icon>
                <span>在住：{{ totalStats.activeTenants }}</span>
              </div>
              <div class="overview-item">
                <el-icon size="20" color="#f56c6c">
                  <Money/>
                </el-icon>
                <span>月收入：¥{{ totalStats.monthlyIncome.toLocaleString() }}</span>
              </div>
            </div>

            <p class="footer-text">
              <el-icon size="16">
                <InfoFilled/>
              </el-icon>
              点击对应楼层按钮进入管理界面
            </p>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, computed} from 'vue'
import {useRouter} from 'vue-router'
import {useUserStore} from '../stores/user'
import {ElMessage} from 'element-plus'
import {
  User,
  ArrowDown,
  OfficeBuilding,
  House,
  UserFilled,
  Money,
  Warning,
  SwitchButton,
  Setting,
  InfoFilled
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})

// 统计数据
const floor5Stats = ref({
  totalRooms: 0,
  occupancyRate: 0
})

const floor6Stats = ref({
  totalRooms: 0,
  occupancyRate: 0
})

const totalStats = ref({
  totalRooms: 0,
  activeTenants: 0,
  monthlyIncome: 0,
  unpaidCount: 0
})

// 前往楼层管理
const goToFloor = (floor: string) => {
  router.push(`/floor/${floor}`)
}

// 处理下拉菜单命令
const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
    router.push('/login')
    ElMessage.success('已退出登录')
  }
}

// 前往系统设置
const goToSettings = () => {
  router.push('/settings')
}

// 加载统计数据
const loadStats = async () => {
  try {
    // 这里应该调用API获取统计数据
    // 暂时使用模拟数据
    floor5Stats.value = {
      totalRooms: 8,
      occupancyRate: 75
    }

    floor6Stats.value = {
      totalRooms: 12,
      occupancyRate: 83
    }

    totalStats.value = {
      totalRooms: 20,
      activeTenants: 16,
      monthlyIncome: 28500,
      unpaidCount: 3
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

onMounted(() => {
  userStore.initUser()
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  loadStats()
})
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow-x: hidden;
}

/* 背景装饰保持原样 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.05;
  background: linear-gradient(45deg, #667eea, #764ba2);
}

.circle-1 {
  width: 400px;
  height: 400px;
  top: -200px;
  right: -200px;
  animation: float 8s ease-in-out infinite;
}

.circle-2 {
  width: 300px;
  height: 300px;
  bottom: -150px;
  left: -150px;
  animation: float 10s ease-in-out infinite reverse;
}

.circle-3 {
  width: 200px;
  height: 200px;
  top: 40%;
  right: 15%;
  animation: float 12s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-30px) rotate(180deg);
  }
}

/* 头部样式保持原样 */
.header {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  position: relative;
  z-index: 10;
  height: 70px;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo-container h1 {
  margin: 0;
  color: #1a202c;
  font-size: 26px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.settings-btn {
  background: rgba(64, 158, 255, 0.1) !important;
  border: 1px solid rgba(64, 158, 255, 0.2) !important;
  color: #409eff !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.settings-btn:hover {
  background: rgba(64, 158, 255, 0.2) !important;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(64, 158, 255, 0.25);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 20px;
  border-radius: 30px;
  background: rgba(99, 102, 241, 0.08);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
}

.user-profile:hover {
  background: rgba(99, 102, 241, 0.12);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.15);
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.username {
  color: #374151;
  font-weight: 600;
  font-size: 15px;
}

.dropdown-arrow {
  color: #6b7280;
  transition: transform 0.3s ease;
}

.user-profile:hover .dropdown-arrow {
  transform: rotate(180deg);
}

/* 主要内容区域 - 简化设计 */
.main {
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 70px);
  position: relative;
  z-index: 5;
}

.dashboard-container {
  max-width: 800px;
  width: 100%;
  padding: 40px;
}

.dashboard-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 32px;
  padding: 60px 50px;
  text-align: center;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  border: none;
  animation: slideInUp 0.8s ease-out;
}

.system-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin: 0 0 16px 0;
  color: #1a202c;
  font-size: 36px;
  font-weight: 800;
  letter-spacing: -1px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.system-subtitle {
  color: #4a5568;
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 12px 0;
}

.welcome-date {
  color: #718096;
  font-size: 16px;
  margin: 0 0 50px 0;
  font-weight: 500;
}

.floor-buttons {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.floor-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 40px 30px;
  background: rgba(255, 255, 255, 0.9);
  border: 2px solid transparent;
  border-radius: 24px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 18px;
  font-weight: 700;
  color: #2d3748;
  min-width: 200px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
}

.floor-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.floor-btn:hover::before {
  transform: translateX(100%);
}

.floor-5 {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  border-color: rgba(102, 126, 234, 0.2);
}

.floor-6 {
  background: linear-gradient(135deg, rgba(79, 172, 254, 0.1) 0%, rgba(0, 242, 254, 0.1) 100%);
  border-color: rgba(79, 172, 254, 0.2);
}

.floor-btn:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.floor-5:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.15) 0%, rgba(118, 75, 162, 0.15) 100%);
  border-color: rgba(102, 126, 234, 0.3);
}

.floor-6:hover {
  background: linear-gradient(135deg, rgba(79, 172, 254, 0.15) 0%, rgba(0, 242, 254, 0.15) 100%);
  border-color: rgba(79, 172, 254, 0.3);
}

.floor-stats-mini {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: #718096;
  font-weight: 500;
}

.quick-overview {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.overview-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 16px;
  font-size: 14px;
  color: #4a5568;
  font-weight: 600;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.footer-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #718096;
  font-size: 14px;
  margin: 0;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    padding: 0 24px;
    height: 65px;
  }

  .logo-container h1 {
    font-size: 22px;
  }

  .dashboard-container {
    padding: 20px;
  }

  .dashboard-card {
    padding: 40px 30px;
  }

  .system-title {
    font-size: 28px;
    flex-direction: column;
    gap: 12px;
  }

  .system-subtitle {
    font-size: 18px;
  }

  .floor-buttons {
    flex-direction: column;
    gap: 20px;
    align-items: center;
  }

  .floor-btn {
    width: 100%;
    max-width: 280px;
    padding: 32px 24px;
  }

  .quick-overview {
    flex-direction: column;
    gap: 12px;
    align-items: center;
  }

  .overview-item {
    width: 100%;
    max-width: 280px;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 0 20px;
    height: 60px;
  }

  .logo-container h1 {
    font-size: 18px;
  }

  .dashboard-container {
    padding: 16px;
  }

  .dashboard-card {
    padding: 30px 20px;
  }

  .system-title {
    font-size: 24px;
  }

  .system-subtitle {
    font-size: 16px;
  }

  .welcome-date {
    font-size: 14px;
  }

  .floor-btn {
    padding: 28px 20px;
    font-size: 16px;
  }

  .quick-overview {
    gap: 10px;
  }

  .overview-item {
    padding: 10px 16px;
    font-size: 13px;
  }

  .user-profile {
    padding: 8px 16px;
  }

  .username {
    display: none;
  }

  .avatar {
    width: 32px;
    height: 32px;
  }
}

/* 动画效果 */
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
</style>