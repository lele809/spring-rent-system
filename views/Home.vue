<template>
  <div class="home-page">
    <!-- 背景装饰 -->
    <div class="page-background">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="home-container">
      <!-- 楼层概览标题 -->
      <div class="floor-header">
        <div class="header-content">
          <div class="floor-info">
            <el-icon size="32" color="#667eea">
              <OfficeBuilding />
            </el-icon>
            <h1>{{ floorTitle }}管理中心</h1>
          </div>
          <div class="current-time">
            <el-icon size="16"><Clock /></el-icon>
            {{ currentDateTime }}
          </div>
        </div>
      </div>

      <!-- 统计概览卡片 -->
      <div class="stats-overview">
        <div class="stats-card total-rooms">
          <div class="card-icon">
            <el-icon size="24"><House /></el-icon>
          </div>
          <div class="card-content">
            <div class="stat-number">{{ floorStats.totalRooms }}</div>
            <div class="stat-label">房间总数</div>
          </div>
          <div class="card-progress">
            <div class="progress-ring">
              <div class="progress-fill" :style="{ strokeDashoffset: 100 - floorStats.occupancyRate }"></div>
            </div>
          </div>
        </div>

        <div class="stats-card occupied-rooms">
          <div class="card-icon">
            <el-icon size="24"><UserFilled /></el-icon>
          </div>
          <div class="card-content">
            <div class="stat-number">{{ floorStats.occupiedRooms }}</div>
            <div class="stat-label">已入住</div>
          </div>
          <div class="occupancy-rate">{{ floorStats.occupancyRate }}%</div>
        </div>

        <div class="stats-card monthly-income">
          <div class="card-icon">
            <el-icon size="24"><Money /></el-icon>
          </div>
          <div class="card-content">
            <div class="stat-number">¥{{ floorStats.monthlyIncome.toLocaleString() }}</div>
            <div class="stat-label">月度收入</div>
          </div>
          <div class="income-trend">
            <el-icon size="14" color="#67c23a"><TrendCharts /></el-icon>
            +12%
          </div>
        </div>

        <div class="stats-card pending-payments">
          <div class="card-icon">
            <el-icon size="24"><Warning /></el-icon>
          </div>
          <div class="card-content">
            <div class="stat-number">{{ floorStats.pendingPayments }}</div>
            <div class="stat-label">待缴费</div>
          </div>
          <div class="urgent-badge" v-if="floorStats.pendingPayments > 0">
            <el-icon size="12"><Bell /></el-icon>
          </div>
        </div>
      </div>

      <!-- 每月收入趋势图 -->
      <div class="income-chart-section">
        <h2 class="section-title">
          <el-icon size="20"><TrendCharts /></el-icon>
          每月收入趋势
        </h2>

        <div class="chart-container">
          <div class="chart-header">
            <div class="chart-info">
              <div class="current-month-income">
                <span class="label">本月收入</span>
                <span class="amount">¥{{ currentMonthIncome.toLocaleString() }}</span>
              </div>
              <div class="income-change">
                <el-icon size="14" :color="incomeChangeColor">
                  <component :is="incomeChangeIcon" />
                </el-icon>
                <span :style="{ color: incomeChangeColor }">{{ incomeChangeText }}</span>
              </div>
            </div>
            <div class="chart-controls">
              <el-button-group>
                <el-button
                  :type="chartPeriod === 6 ? 'primary' : 'default'"
                  size="small"
                  @click="changeChartPeriod(6)"
                >
                  6个月
                </el-button>
                <el-button
                  :type="chartPeriod === 12 ? 'primary' : 'default'"
                  size="small"
                  @click="changeChartPeriod(12)"
                >
                  12个月
                </el-button>
              </el-button-group>
            </div>
          </div>

          <div class="chart-wrapper" v-loading="chartLoading">
            <v-chart
              class="income-chart"
              :option="chartOption"
              :loading="chartLoading"
              autoresize
            />
          </div>
        </div>
      </div>

      <!-- 最近动态 -->
      <div class="recent-activities">
        <h2 class="section-title">
          <el-icon size="20"><Clock /></el-icon>
          最近动态
        </h2>
        
        <div class="activity-list">
          <div class="activity-item" v-for="activity in recentActivities" :key="activity.id">
            <div class="activity-icon" :class="activity.type">
              <el-icon size="16">
                <component :is="getActivityIcon(activity.type)" />
              </el-icon>
            </div>
            <div class="activity-content">
              <div class="activity-text">{{ activity.content }}</div>
              <div class="activity-time">{{ formatTime(activity.timestamp) }}</div>
            </div>
          </div>
          
          <div class="activity-item empty" v-if="recentActivities.length === 0">
            <div class="empty-icon">
              <el-icon size="24" color="#d1d5db"><InfoFilled /></el-icon>
            </div>
            <div class="empty-text">暂无最近动态</div>
          </div>
        </div>
      </div>

      <!-- 快速提醒 -->
      <div class="quick-reminders" v-if="reminders.length > 0">
        <h2 class="section-title">
          <el-icon size="20"><Bell /></el-icon>
          重要提醒
        </h2>
        
        <div class="reminder-list">
          <div class="reminder-item" v-for="reminder in reminders" :key="reminder.id" :class="reminder.priority">
            <div class="reminder-icon">
              <el-icon size="16">
                <component :is="getReminderIcon(reminder.type)" />
              </el-icon>
            </div>
            <div class="reminder-content">
              <div class="reminder-text">{{ reminder.content }}</div>
              <div class="reminder-time">{{ reminder.dueDate }}</div>
            </div>
            <div class="reminder-action" @click="handleReminder(reminder)">
              <el-icon size="14"><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, computed, onMounted, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import {
  OfficeBuilding,
  Clock,
  House,
  UserFilled,
  Money,
  Warning,
  Bell,
  Grid,
  InfoFilled,
  Document,
  DocumentCopy,
  TrendCharts,
  ArrowRight,
  ArrowUp,
  ArrowDown
} from '@element-plus/icons-vue'
import { getMonthlyIncome, type MonthlyIncomeData } from '../api/payments'

// 注册ECharts组件
use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const route = useRoute()
const router = useRouter()

// 当前楼层
const currentFloor = computed(() => route.params.floor as string)

// 楼层标题
const floorTitle = computed(() => {
  return currentFloor.value === '5' ? '五楼' : '六楼'
})

// 当前时间
const currentDateTime = ref('')

// 图表相关数据
const chartLoading = ref(false)
const chartPeriod = ref(6)
const currentMonthIncome = computed(() => floorStats.value.monthlyIncome)

// 图表选项
const chartOption = ref({
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: 'rgba(102, 126, 234, 0.2)',
    borderWidth: 1,
    textStyle: {
      color: '#333',
      fontSize: 13
    },
    formatter: function(params: any) {
      const data = params[0];
      return `
        <div style="padding: 8px;">
          <div style="font-weight: bold; margin-bottom: 4px;">${data.name}</div>
          <div style="color: #667eea;">
            <span style="display: inline-block; width: 10px; height: 10px; background: #667eea; border-radius: 50%; margin-right: 6px;"></span>
            收入: ¥${data.value.toLocaleString()}
          </div>
        </div>
      `;
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '8%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月'],
    axisTick: {
      show: false
    },
    axisLine: {
      lineStyle: {
        color: '#e5e7eb'
      }
    },
    axisLabel: {
      color: '#6b7280',
      fontSize: 12,
      fontWeight: 500
    }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: '¥{value}',
      color: '#6b7280',
      fontSize: 12
    },
    axisLine: {
      show: false
    },
    axisTick: {
      show: false
    },
    splitLine: {
      lineStyle: {
        color: '#f3f4f6',
        type: 'dashed'
      }
    }
  },
  series: [{
    name: '月收入',
    type: 'line',
    data: [8000, 9500, 11000, 10500, 12000, 13500],
    smooth: true,
    symbol: 'circle',
    symbolSize: 8,
    lineStyle: {
      width: 3,
      color: '#667eea'
    },
    itemStyle: {
      color: '#667eea',
      borderColor: '#fff',
      borderWidth: 2
    },
    emphasis: {
      itemStyle: {
        color: '#667eea',
        borderColor: '#fff',
        borderWidth: 3,
        shadowBlur: 10,
        shadowColor: 'rgba(102, 126, 234, 0.3)'
      }
    },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0,
        y: 0,
        x2: 0,
        y2: 1,
        colorStops: [{
          offset: 0, color: 'rgba(102, 126, 234, 0.25)'
        }, {
          offset: 1, color: 'rgba(102, 126, 234, 0.02)'
        }]
      }
    }
  }]
})

// 收入变化相关计算属性
const incomeChangeText = computed(() => {
  const chartData = chartOption.value.series[0].data as number[]
  if (chartData.length < 2) return '+0%'
  
  const currentMonth = Number(chartData[chartData.length - 1])
  const previousMonth = Number(chartData[chartData.length - 2])
  const changeRate = (currentMonth - previousMonth) / previousMonth * 100
  const changeRateText = changeRate.toFixed(1)
  
  return changeRate >= 0 ? `+${changeRateText}%` : `${changeRateText}%`
})

const incomeChangeColor = computed(() => {
  const chartData = chartOption.value.series[0].data as number[]
  if (chartData.length < 2) return '#6b7280'
  
  const currentMonth = Number(chartData[chartData.length - 1])
  const previousMonth = Number(chartData[chartData.length - 2])
  
  return currentMonth >= previousMonth ? '#67c23a' : '#f56c6c'
})

const incomeChangeIcon = computed(() => {
  const chartData = chartOption.value.series[0].data as number[]
  if (chartData.length < 2) return ArrowUp
  
  const currentMonth = Number(chartData[chartData.length - 1])
  const previousMonth = Number(chartData[chartData.length - 2])
  
  return currentMonth >= previousMonth ? ArrowUp : ArrowDown
})

// 更改图表周期
const changeChartPeriod = (period: number) => {
  chartPeriod.value = period
  loadChartData()
}

// 加载图表数据
const loadChartData = async () => {
  chartLoading.value = true
  try {
    // 调用真实的API获取收入数据
    console.log(`请求${currentFloor.value}楼收入趋势数据，月份数：${chartPeriod.value}`)
    const response: any = await getMonthlyIncome(currentFloor.value, chartPeriod.value)
    
    console.log('API响应原始数据:', response)
    
    if (response.code === 200 && response.data) {
      const incomeData = response.data
      console.log('解析的收入数据:', incomeData)
      
      // 验证数据结构
      if (!incomeData.months || !incomeData.incomes) {
        console.error('API返回的数据结构不完整:', incomeData)
        loadFallbackData()
        return
      }
      
      // 更新图表数据
      const monthLabels = incomeData.months.map((month: string) => {
        // 将 "2025-01" 格式转换为 "1月" 格式
        if (typeof month === 'string' && month.includes('-')) {
          const [year, monthNum] = month.split('-')
          return `${parseInt(monthNum)}月`
        }
        return month
      })
      
      // 确保收入数据是数字数组
      const incomeValues = incomeData.incomes.map((income: any) => {
        const numValue = Number(income)
        return isNaN(numValue) ? 0 : numValue
      })
      
      console.log('处理后的月份标签:', monthLabels)
      console.log('处理后的收入数据:', incomeValues)
      
      // 更新图表配置
      chartOption.value.xAxis.data = monthLabels
      chartOption.value.series[0].data = incomeValues
      
      // 更新当前月收入
      if (incomeData.currentMonthIncome !== undefined && incomeData.currentMonthIncome !== null) {
        const currentIncome = Number(incomeData.currentMonthIncome)
        if (!isNaN(currentIncome)) {
          floorStats.value.monthlyIncome = currentIncome
        }
      }
      
      console.log(`成功加载${currentFloor.value}楼收入趋势数据`)
      console.log('图表X轴数据:', chartOption.value.xAxis.data)
      console.log('图表Y轴数据:', chartOption.value.series[0].data)
      console.log('当前月收入:', floorStats.value.monthlyIncome)
      
    } else {
      console.error('API返回错误:', (response as any).message || response)
      // 如果API失败，使用备用数据
      loadFallbackData()
    }
    
  } catch (error) {
    console.error('加载图表数据失败:', error)
    
    // 输出更详细的错误信息
    if ((error as any).response) {
      console.error('响应状态:', (error as any).response.status)
      console.error('响应数据:', (error as any).response.data)
      console.error('响应头:', (error as any).response.headers)
    } else if ((error as any).request) {
      console.error('请求信息:', (error as any).request)
    } else {
      console.error('错误信息:', (error as any).message)
    }
    
    // 如果请求失败，使用备用数据
    loadFallbackData()
  } finally {
    chartLoading.value = false
  }
}

// 备用数据加载函数（当API失败时使用）
const loadFallbackData = () => {
  // 根据楼层生成不同的收入数据
  const generateIncomeData = (period: number, floor: string) => {
    const baseIncome = floor === '5' ? 1800 : 1500 // 五楼单价更高
    const roomCount = floor === '5' ? 8 : 12
    const monthlyBase = baseIncome * roomCount * 0.8 // 假设80%入住率的基础收入
    
    const data = []
    const months = period === 6 
      ? ['1月', '2月', '3月', '4月', '5月', '6月']
      : ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    
    for (let i = 0; i < period; i++) {
      // 模拟季节性变化和随机波动
      const seasonalFactor = 1 + 0.1 * Math.sin((i + 1) * Math.PI / 6) // 季节性变化
      const randomFactor = 0.9 + Math.random() * 0.2 // 随机波动 ±10%
      const trendFactor = 1 + (i * 0.02) // 微小的增长趋势
      
      const income = Math.round(monthlyBase * seasonalFactor * randomFactor * trendFactor)
      data.push(income)
    }
    
    return { months, data }
  }
  
  const { months, data } = generateIncomeData(chartPeriod.value, currentFloor.value)
  
  chartOption.value.xAxis.data = months
  chartOption.value.series[0].data = data
  
  // 更新当前月收入（使用最后一个月的数据）
  if (data.length > 0) {
    floorStats.value.monthlyIncome = data[data.length - 1]
  }
}

// 楼层统计数据
const floorStats = ref({
  totalRooms: 0,
  occupiedRooms: 0,
  occupancyRate: 0,
  monthlyIncome: 0,
  pendingPayments: 0,
  activeTenants: 0,
  activeContracts: 0,
  totalRecords: 0
})

// 最近动态
const recentActivities = ref([
  {
    id: 1,
    type: 'payment',
    content: '501房间完成本月租金缴费',
    timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000) // 2小时前
  },
  {
    id: 2,
    type: 'checkin',
    content: '新租客张小明入住502房间',
    timestamp: new Date(Date.now() - 5 * 60 * 60 * 1000) // 5小时前
  },
  {
    id: 3,
    type: 'maintenance',
    content: '503房间水管维修完成',
    timestamp: new Date(Date.now() - 24 * 60 * 60 * 1000) // 1天前
  }
])

// 重要提醒
const reminders = ref([
  {
    id: 1,
    type: 'payment',
    priority: 'high',
    content: '505房间租金逾期3天',
    dueDate: '今天'
  },
  {
    id: 2,
    type: 'contract',
    priority: 'medium',
    content: '506房间合同即将到期',
    dueDate: '3天后'
  }
])

// 更新当前时间
const updateDateTime = () => {
  const now = new Date()
  currentDateTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 导航到指定页面
const navigateTo = (page: string) => {
  router.push(`/floor/${currentFloor.value}/${page}`)
}

// 获取活动图标
const getActivityIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    payment: Money,
    checkin: UserFilled,
    maintenance: Warning,
    contract: Document
  }
  return iconMap[type] || InfoFilled
}

// 获取提醒图标
const getReminderIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    payment: Money,
    contract: Document,
    maintenance: Warning
  }
  return iconMap[type] || Bell
}

// 处理提醒点击
const handleReminder = (reminder: any) => {
  // 根据提醒类型跳转到相应页面
  const pageMap: Record<string, string> = {
    payment: 'payments',
    contract: 'contracts',
    maintenance: 'rooms'
  }
  const page = pageMap[reminder.type] || 'rooms'
  navigateTo(page)
}

// 格式化时间
const formatTime = (timestamp: Date) => {
  const now = new Date()
  const diff = now.getTime() - timestamp.getTime()
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(hours / 24)
  
  if (days > 0) {
    return `${days}天前`
  } else if (hours > 0) {
    return `${hours}小时前`
  } else {
    return '刚刚'
  }
}

// 加载楼层统计数据
const loadFloorStats = async () => {
  try {
    // 这里应该调用真实的API
    // 暂时使用模拟数据
    if (currentFloor.value === '5') {
      floorStats.value = {
        totalRooms: 8,
        occupiedRooms: 6,
        occupancyRate: 75,
        monthlyIncome: 12000,
        pendingPayments: 2,
        activeTenants: 6,
        activeContracts: 6,
        totalRecords: 45
      }
    } else {
      floorStats.value = {
        totalRooms: 12,
        occupiedRooms: 10,
        occupancyRate: 83,
        monthlyIncome: 18500,
        pendingPayments: 1,
        activeTenants: 10,
        activeContracts: 10,
        totalRecords: 68
      }
    }
  } catch (error) {
    console.error('加载楼层统计数据失败:', error)
  }
}

// 监听楼层变化，重新加载数据
watch(currentFloor, () => {
  loadFloorStats()
  loadChartData()
})

onMounted(() => {
  updateDateTime()
  loadFloorStats()
  loadChartData() // 初始加载图表数据
  
  // 每分钟更新时间
  const timer = setInterval(updateDateTime, 60000)
  
  // 组件卸载时清理定时器
  const cleanup = () => {
    clearInterval(timer)
  }
  
  // 在组件卸载时清理
  return cleanup
})
</script>

<style scoped>
.home-page {
  min-height: calc(100vh - 160px);
  background: transparent;
  position: relative;
  overflow: hidden;
}

.page-background {
  position: absolute;
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

.floating-shapes {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.shape {
  position: absolute;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  filter: blur(40px);
}

.shape-1 {
  width: 120px;
  height: 120px;
  top: 20%;
  right: 15%;
  animation: float 15s ease-in-out infinite;
}

.shape-2 {
  width: 80px;
  height: 80px;
  bottom: 30%;
  left: 10%;
  animation: float 20s ease-in-out infinite reverse;
}

.shape-3 {
  width: 60px;
  height: 60px;
  top: 60%;
  right: 30%;
  animation: float 18s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

.home-container {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
}

/* 楼层头部 */
.floor-header {
  margin-bottom: 32px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.floor-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.floor-info h1 {
  margin: 0;
  font-size: 32px;
  font-weight: 800;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.current-time {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
}

/* 统计概览 */
.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stats-card {
  padding: 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  gap: 20px;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
}

.stats-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

.stats-card::before {
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

.stats-card:hover::before {
  transform: translateX(100%);
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.total-rooms .card-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.occupied-rooms .card-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.monthly-income .card-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.pending-payments .card-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: white;
}

.card-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 800;
  color: #1a202c;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 600;
}

.occupancy-rate,
.income-trend {
  font-size: 14px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 12px;
  background: rgba(103, 194, 58, 0.1);
  color: #67c23a;
  display: flex;
  align-items: center;
  gap: 4px;
}

.urgent-badge {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f56565;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

/* 功能快捷入口 */
.quick-actions {
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 24px;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 24px;
  padding: 0 8px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 24px;
}

.action-card {
  position: relative;
  padding: 32px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
}

.action-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
}

.card-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.rooms .card-background {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
}

.tenants .card-background {
  background: linear-gradient(135deg, rgba(79, 172, 254, 0.05) 0%, rgba(0, 242, 254, 0.05) 100%);
}

.rental-info .card-background {
  background: linear-gradient(135deg, rgba(67, 233, 123, 0.05) 0%, rgba(56, 249, 215, 0.05) 100%);
}

.contracts .card-background {
  background: linear-gradient(135deg, rgba(250, 112, 154, 0.05) 0%, rgba(254, 225, 64, 0.05) 100%);
}

.payments .card-background {
  background: linear-gradient(135deg, rgba(255, 159, 67, 0.05) 0%, rgba(255, 206, 84, 0.05) 100%);
}

.rental-records .card-background {
  background: linear-gradient(135deg, rgba(161, 196, 253, 0.05) 0%, rgba(194, 233, 251, 0.05) 100%);
}

.action-card:hover .card-background {
  opacity: 1;
}

.card-icon-large {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.rooms .card-icon-large {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.tenants .card-icon-large {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.rental-info .card-icon-large {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.contracts .card-icon-large {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: white;
}

.payments .card-icon-large {
  background: linear-gradient(135deg, #ff9f43 0%, #ffce56 100%);
  color: white;
}

.rental-records .card-icon-large {
  background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
  color: white;
}

.action-card:hover .card-icon-large {
  transform: scale(1.1) rotate(5deg);
}

.action-card h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a202c;
}

.action-card p {
  margin: 0 0 16px 0;
  color: #6b7280;
  font-size: 14px;
  line-height: 1.5;
}

.card-stats {
  font-size: 12px;
  color: #667eea;
  font-weight: 600;
  background: rgba(102, 126, 234, 0.1);
  padding: 4px 12px;
  border-radius: 12px;
  display: inline-block;
}

.hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1));
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.action-card:hover .hover-effect {
  transform: translateX(100%);
}

/* 最近动态 */
.recent-activities {
  margin-bottom: 32px;
}

.activity-list {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-item:hover {
  background: rgba(102, 126, 234, 0.03);
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.activity-icon.payment {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.activity-icon.checkin {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.activity-icon.maintenance {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.activity-content {
  flex: 1;
}

.activity-text {
  font-size: 15px;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: #9ca3af;
}

.activity-item.empty {
  flex-direction: column;
  text-align: center;
  padding: 40px 24px;
  gap: 12px;
}

.empty-text {
  color: #9ca3af;
  font-size: 14px;
}

/* 快速提醒 */
.quick-reminders {
  margin-bottom: 32px;
}

/* 收入趋势图表 */
.income-chart-section {
  margin-bottom: 32px;
}

.chart-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
  padding: 24px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.chart-info {
  display: flex;
  align-items: center;
  gap: 24px;
}

.current-month-income {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.current-month-income .label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.current-month-income .amount {
  font-size: 24px;
  font-weight: 800;
  color: #1a202c;
}

.income-change {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(103, 194, 58, 0.1);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
}

.chart-controls .el-button-group {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

.chart-wrapper {
  position: relative;
  width: 100%;
  height: 350px;
  border-radius: 12px;
  overflow: hidden;
}

.income-chart {
  width: 100% !important;
  height: 100% !important;
}

.reminder-list {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  overflow: hidden;
}

.reminder-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
}

.reminder-item:last-child {
  border-bottom: none;
}

.reminder-item:hover {
  background: rgba(102, 126, 234, 0.03);
}

.reminder-item.high {
  border-left: 4px solid #f56565;
}

.reminder-item.medium {
  border-left: 4px solid #f6ad55;
}

.reminder-icon {
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(245, 101, 101, 0.1);
  color: #f56565;
}

.reminder-content {
  flex: 1;
}

.reminder-text {
  font-size: 15px;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 4px;
}

.reminder-time {
  font-size: 12px;
  color: #f56565;
  font-weight: 500;
}

.reminder-action {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.reminder-action:hover {
  background: rgba(102, 126, 234, 0.2);
  transform: scale(1.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    padding: 0 16px;
  }

  .header-content {
    flex-direction: column;
    gap: 16px;
    text-align: center;
    padding: 24px 20px;
  }

  .floor-info h1 {
    font-size: 24px;
  }

  .stats-overview {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .chart-container {
    padding: 20px 16px;
  }

  .chart-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .chart-info {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .chart-wrapper {
    height: 280px;
  }

  .action-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .section-title {
    font-size: 20px;
  }

  .action-card {
    padding: 24px 20px;
  }

  .activity-item,
  .reminder-item {
    padding: 16px 20px;
  }
}

@media (max-width: 480px) {
  .stats-card {
    padding: 20px 16px;
  }

  .card-icon {
    width: 50px;
    height: 50px;
  }

  .stat-number {
    font-size: 24px;
  }

  .chart-container {
    padding: 16px 12px;
  }

  .chart-wrapper {
    height: 240px;
  }

  .current-month-income .amount {
    font-size: 20px;
  }

  .chart-controls .el-button-group .el-button {
    font-size: 12px;
    padding: 6px 12px;
  }

  .action-card {
    padding: 20px 16px;
  }

  .card-icon-large {
    width: 56px;
    height: 56px;
  }

  .action-card h3 {
    font-size: 18px;
  }
}
</style> 