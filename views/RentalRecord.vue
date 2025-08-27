<template>
  <div class="rental-record-management">
    <!-- 背景装饰 -->
    <div class="page-background">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
      <div class="bg-particles">
        <div class="particle" v-for="n in 12" :key="n" :style="getParticleStyle(n)"></div>
      </div>
    </div>

    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-title">
          <el-icon size="28" color="#667eea">
            <Money/>
          </el-icon>
          <div class="title-content">
            <h1>缴费记录管理</h1>
            <p class="header-subtitle">智能化租客缴费记录管理系统</p>
          </div>
        </div>
        <div class="header-actions">
          <el-button
              type="primary"
              size="large"
              @click="showAddDialog"
              class="primary-btn enhanced-btn"
          >
            <el-icon>
              <Plus/>
            </el-icon>
            新增记录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 楼层选择器 -->
    <div class="floor-selector-section">
      <el-card class="floor-card modern-card" shadow="never">
        <template #header>
          <div class="floor-header">
            <div class="floor-title">
              <el-icon size="20">
                <OfficeBuilding/>
              </el-icon>
              楼层选择
            </div>
          </div>
        </template>
        <div class="floor-selector">
          <el-radio-group v-model="selectedFloor" @change="handleFloorChange" class="floor-radio-group" size="large">
            <el-radio-button 
              v-for="option in floorOptions" 
              :key="option.value" 
              :value="option.value"
              class="floor-radio-btn"
            >
              {{ option.label }}
            </el-radio-button>
          </el-radio-group>
        </div>
      </el-card>
    </div>

    <!-- 统计概览卡片 -->
    <div class="stats-section">
      <el-row :gutter="24" class="stats-row">
        <el-col :span="6">
          <div class="stats-card primary-card enhanced-card">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ statsData.totalRecords }}</div>
                <div class="stats-label">总记录数</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper primary-icon">
                  <el-icon class="stats-icon">
                    <Document/>
                  </el-icon>
                  <div class="icon-ripple"></div>
                </div>
              </div>
            </div>
            <div class="stats-progress">
              <div class="progress-bar primary-progress"></div>
            </div>
            <div class="hover-effect"></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card success-card enhanced-card" @click="filterByMonth('current')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ formatCurrency(statsData.thisMonthTotal) }}</div>
                <div class="stats-label">本月收入</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper success-icon">
                  <el-icon class="stats-icon">
                    <TrendCharts/>
                  </el-icon>
                  <div class="icon-ripple"></div>
                </div>
              </div>
            </div>
            <div class="stats-progress">
              <div class="progress-bar success-progress"></div>
            </div>
            <div class="hover-effect"></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card warning-card enhanced-card" @click="filterByMonth('last')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ formatCurrency(statsData.lastMonthTotal) }}</div>
                <div class="stats-label">上月收入</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper warning-icon">
                  <el-icon class="stats-icon">
                    <DataLine/>
                  </el-icon>
                  <div class="icon-ripple"></div>
                </div>
              </div>
            </div>
            <div class="stats-progress">
              <div class="progress-bar warning-progress"></div>
            </div>
            <div class="hover-effect"></div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card danger-card enhanced-card" @click="filterByDateRange('week')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ statsData.recentRecords }}</div>
                <div class="stats-label">近7天记录</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper danger-icon">
                  <el-icon class="stats-icon">
                    <Calendar/>
                  </el-icon>
                  <div class="icon-ripple"></div>
                </div>
              </div>
            </div>
            <div class="stats-progress">
              <div class="progress-bar danger-progress"></div>
            </div>
            <div class="hover-effect"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 操作栏 -->
    <div class="operation-section">
      <el-card class="operation-card modern-card" shadow="never">
        <template #header>
          <div class="operation-header">
            <div class="operation-title">
              <el-icon size="20">
                <Filter/>
              </el-icon>
              筛选与搜索
            </div>
            <div class="operation-extra">
              <div class="month-shortcuts">
                <el-button
                    @click="filterByMonth('last')"
                    type="text"
                    size="small"
                    class="month-shortcut-btn"
                >
                  上月
                </el-button>
                <el-button
                    @click="filterByMonth('current')"
                    type="text"
                    size="small"
                    class="month-shortcut-btn"
                >
                  本月
                </el-button>
              </div>
              <el-button
                  @click="clearAllFilters"
                  type="text"
                  class="clear-filters-btn"
                  v-if="hasActiveFilters"
              >
                <el-icon>
                  <Refresh/>
                </el-icon>
                清空筛选
              </el-button>
            </div>
          </div>
        </template>

        <!-- 搜索和筛选 -->
        <el-row :gutter="20" class="search-row">
          <el-col :span="5">
            <el-date-picker
                v-model="monthFilter"
                type="month"
                placeholder="选择月份"
                clearable
                size="large"
                class="month-filter enhanced-input"
                format="YYYY-MM"
                value-format="YYYY-MM"
                @change="handleSearch"
            >
              <template #prefix>
                <el-icon>
                  <Calendar/>
                </el-icon>
              </template>
            </el-date-picker>
          </el-col>
          <el-col :span="8">
            <el-input
                v-model="searchKeyword"
                placeholder="搜索房号、租客姓名..."
                clearable
                size="large"
                class="search-input enhanced-input"
                @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon>
                  <Search/>
                </el-icon>
              </template>
            </el-input>
          </el-col>
          <el-col :span="3">
            <el-button
                type="primary"
                size="large"
                @click="handleSearch"
                class="search-btn enhanced-btn"
            >
              <el-icon>
                <Search/>
              </el-icon>
              搜索
            </el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 缴费记录列表表格 -->
    <div class="table-container">
      <el-card class="table-card modern-table-card" shadow="hover">
        <template #header>
          <div class="table-header">
            <div class="table-title">
              <el-icon class="title-icon">
                <Money/>
              </el-icon>
              <span>缴费记录详细信息 - {{ selectedFloor }}楼</span>
            </div>
            <div class="table-actions">
              <el-button
                  @click="batchDelete"
                  type="danger"
                  size="large"
                  class="batch-delete-btn enhanced-btn"
                  :disabled="selectedRecords.length === 0"
                  v-if="selectedRecords.length > 0"
              >
                <el-icon>
                  <Delete/>
                </el-icon>
                批量删除 ({{ selectedRecords.length }})
              </el-button>
              <el-tag type="primary" size="large" class="count-tag">共 {{ pagination.total }} 条记录</el-tag>
            </div>
          </div>
        </template>

        <el-table
            :data="tableData"
            v-loading="loading"
            stripe
            style="width: 100%"
            class="modern-table"
            :header-cell-style="{
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            color: '#fff',
            fontWeight: 'bold',
            borderBottom: 'none'
          }"
            :row-style="{ borderBottom: '1px solid #f0f2f5' }"
            :empty-text="(tableData && tableData.length === 0) && !loading ? '暂无缴费记录，请创建新记录' : '加载中...'"
            @sort-change="handleSortChange"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"/>

          <el-table-column prop="id" label="记录ID" width="100" sortable="custom">
            <template #default="{ row }">
              <div class="record-id-cell">
                <el-tag type="info" effect="dark" class="record-id-tag">{{ row.id }}</el-tag>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="roomNumber" label="房间号" width="180" sortable="custom">
            <template #default="{ row }">
              <div class="room-cell">
                <div class="room-icon">
                  <el-icon>
                    <OfficeBuilding/>
                  </el-icon>
                </div>
                <el-tag type="info" effect="dark" class="room-tag">{{ row.roomNumber }}</el-tag>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="tenantName" label="租客信息" width="180">
            <template #default="{ row }">
              <div class="tenant-info">
                <div class="tenant-avatar">
                  <el-icon>
                    <User/>
                  </el-icon>
                </div>
                <div class="tenant-details">
                  <div class="tenant-name">{{ row.tenantName }}</div>
                  <div class="tenant-phone">租客：{{ row.tenantName }}</div>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="totalRent" label="缴费金额" width="200">
            <template #default="{ row }">
              <div class="amount-details">
                <div class="amount-row">
                  <el-icon class="amount-icon" color="#f56c6c">
                    <Money/>
                  </el-icon>
                  <span class="amount-label">金额:</span>
                  <span class="amount-value total-amount">¥{{ formatCurrency(row.totalRent) }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="paymentDate" label="缴费日期" width="200" sortable="custom">
            <template #default="{ row }">
              <div class="date-info">
                <el-icon color="#67c23a">
                  <Calendar/>
                </el-icon>
                <div class="date-content">
                  <span class="date-text">{{ formatDate(row.paymentDate) }}</span>
                  <span class="month-tag">{{ formatMonth(row.paymentDate) }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="createdAt" label="创建时间" width="200">
            <template #default="{ row }">
              <div class="create-time">
                <el-icon color="#909399">
                  <Clock/>
                </el-icon>
                <div class="time-info">
                  <span class="date-text">{{ formatDate(row.createdAt) }}</span>
                  <span class="month-tag">{{ formatMonth(row.createdAt) }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="250" fixed="right">
            <template #default="{ row, $index }">
              <div class="action-buttons-group">
                <el-tooltip content="查看详情" placement="top">
                  <el-button type="primary" size="small" @click="viewRecord(row)" class="action-btn view-btn">
                    <el-icon>
                      <View/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除记录" placement="top">
                  <el-button type="danger" size="small" @click="deleteRecord(row, $index)"
                             class="action-btn delete-btn">
                    <el-icon>
                      <Delete/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
              :current-page="pagination.page"
              :page-size="pagination.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handlePageChange"
              background
              class="modern-pagination"
          />
        </div>
      </el-card>
    </div>

    <!-- 新增记录对话框 -->
    <el-dialog
        v-model="addDialog.visible"
        title="新增缴费记录"
        width="600px"
        class="modern-dialog"
        destroy-on-close
        @close="resetAddForm"
    >
      <el-form
          ref="addFormRef"
          :model="addDialog.form"
          :rules="addDialog.rules"
          label-width="100px"
          class="add-form modern-form"
      >
        <el-card class="form-card gradient-card" shadow="hover">
          <template #header>
            <div class="form-card-header">
              <el-icon class="card-icon">
                <InfoFilled/>
              </el-icon>
              <span>记录信息</span>
            </div>
          </template>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="房号" prop="roomNumber">
                <el-input
                    v-model="addDialog.form.roomNumber"
                    placeholder="请输入房号"
                    clearable
                    size="large"
                >
                  <template #prefix>
                    <el-icon>
                      <House/>
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="租客姓名" prop="tenantName">
                <el-input
                    v-model="addDialog.form.tenantName"
                    placeholder="请输入租客姓名"
                    clearable
                    size="large"
                >
                  <template #prefix>
                    <el-icon>
                      <User/>
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="缴费金额" prop="totalRent">
                <el-input
                    v-model.number="addDialog.form.totalRent"
                    placeholder="请输入缴费金额"
                    type="number"
                    step="0.01"
                    size="large"
                >
                  <template #prefix>
                    <el-icon>
                      <Money/>
                    </el-icon>
                  </template>
                  <template #append>元</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="缴费日期" prop="paymentDate">
                <el-date-picker
                    v-model="addDialog.form.paymentDate"
                    type="date"
                    placeholder="选择缴费日期"
                    style="width: 100%"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    size="large"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addDialog.visible = false" size="large" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="handleAddRecord" size="large" class="submit-btn">
            <el-icon>
              <Check/>
            </el-icon>
            确认新增
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看记录对话框 -->
    <el-dialog
        v-model="viewDialog.visible"
        title="缴费记录详情"
        width="600px"
        class="modern-dialog view-dialog"
    >
      <div v-if="viewDialog.record" class="record-detail">
        <el-descriptions :column="2" border class="modern-descriptions">
          <el-descriptions-item label="记录ID">{{ viewDialog.record.id }}</el-descriptions-item>
          <el-descriptions-item label="房号">{{ viewDialog.record.roomNumber }}</el-descriptions-item>
          <el-descriptions-item label="租客姓名">{{ viewDialog.record.tenantName }}</el-descriptions-item>
          <el-descriptions-item label="缴费金额">¥{{
              formatCurrency(viewDialog.record.totalRent)
            }}
          </el-descriptions-item>
          <el-descriptions-item label="缴费日期">{{ formatDate(viewDialog.record.paymentDate) }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{
              formatDateTime(viewDialog.record.createdAt)
            }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="viewDialog.visible = false" size="large">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, computed, onMounted, watch} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {
  Search, Plus, Download, Refresh, View, Delete, Check,
  House, User, Money, Calendar, Clock, Document,
  TrendCharts, DataLine, OfficeBuilding, Filter, InfoFilled
} from '@element-plus/icons-vue'
import {
  getRentalRecords,
  addNewRentalRecord,
  addOldRentalRecord,
  deleteRentalRecord,
  exportRentalRecords,
  getMonthlyTotal,
  getRentalRecordsByDateRange,
  type RentalRecord,
  type RentalRecordsQuery,
  type PageResponse
} from '../api/rentalRecords'

// 路由
const router = useRouter()
const route = useRoute()

// 楼层选择 - 从路由参数初始化
const selectedFloor = ref(route.params.floor as string || '5')
const floorOptions = [
  {label: '五楼', value: '5'},
  {label: '六楼', value: '6'}
]

// 搜索表单
const searchKeyword = ref('')
const monthFilter = ref('')
const dateRange = ref<[Date, Date] | null>(null)

// 表格数据
const tableData = ref<RentalRecord[]>([])
const loading = ref(false)

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 排序
const sortConfig = reactive({
  sortBy: 'paymentDate',
  sortDirection: 'asc'
})

// 统计数据
const statsData = reactive({
  totalRecords: 0,
  thisMonthTotal: 0,
  lastMonthTotal: 0,
  recentRecords: 0
})

// 新增对话框
const addDialog = reactive({
  visible: false,
  form: {
    roomNumber: '',
    tenantName: '',
    totalRent: 0,
    paymentDate: ''
  } as RentalRecord,
  rules: {
    roomNumber: [
      {required: true, message: '请输入房号', trigger: 'blur'}
    ],
    tenantName: [
      {required: true, message: '请输入租客姓名', trigger: 'blur'}
    ],
    totalRent: [
      {required: true, message: '请输入缴费金额', trigger: 'blur'},
      {type: 'number', min: 0.01, message: '缴费金额必须大于0', trigger: 'blur'}
    ],
    paymentDate: [
      {required: true, message: '请选择缴费日期', trigger: 'change'}
    ]
  }
})

// 查看对话框
const viewDialog = reactive({
  visible: false,
  record: null as RentalRecord | null
})

// 批量删除相关
const selectedRecords = ref<RentalRecord[]>([])

const addFormRef = ref()

// 计算属性
const hasActiveFilters = computed(() => {
  return !!(searchKeyword.value || monthFilter.value || dateRange.value)
})

// 背景粒子效果
const getParticleStyle = (index: number) => {
  const size = Math.random() * 4 + 2
  const left = Math.random() * 100
  const top = Math.random() * 100
  const animationDelay = Math.random() * 5
  const animationDuration = Math.random() * 10 + 10

  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    top: `${top}%`,
    animationDelay: `${animationDelay}s`,
    animationDuration: `${animationDuration}s`
  }
}

// 获取缴费记录列表
const fetchRecords = async () => {
  loading.value = true
  try {
    const params: RentalRecordsQuery = {
      keyword: searchKeyword.value || undefined,
      monthFilter: monthFilter.value || undefined,
      page: pagination.page - 1,
      size: pagination.size,
      sortBy: sortConfig.sortBy,
      sortDirection: sortConfig.sortDirection
    }

    // 处理日期范围
    if (dateRange.value) {
      params.startDate = formatDateForAPI(dateRange.value[0])
      params.endDate = formatDateForAPI(dateRange.value[1])
    }

    const response = await getRentalRecords(selectedFloor.value, params) as any
    console.log('获取缴费记录 - 楼层:', selectedFloor.value, '响应:', response)

    if (response && response.code === 200) {
      const data = response.data as PageResponse<RentalRecord>
      tableData.value = data?.content || []
      pagination.total = data?.totalElements || 0
      statsData.totalRecords = data?.totalElements || 0
    } else {
      console.error('API响应错误:', response?.message)
      ElMessage.error('获取缴费记录失败: ' + (response?.message || '未知错误'))
      // 设置空数据
      tableData.value = []
      pagination.total = 0
      statsData.totalRecords = 0
    }

  } catch (error) {
    console.error('获取缴费记录失败:', error)
    ElMessage.error('获取缴费记录失败')
    // 确保在错误情况下也有默认值
    tableData.value = []
    pagination.total = 0
    statsData.totalRecords = 0
  } finally {
    loading.value = false
  }
}

// 获取统计数据
const fetchStats = async () => {
  try {
    const now = new Date()
    const currentYear = now.getFullYear()
    const currentMonth = now.getMonth() + 1
    const lastMonth = currentMonth === 1 ? 12 : currentMonth - 1
    const lastMonthYear = currentMonth === 1 ? currentYear - 1 : currentYear

    // 获取本月和上月统计
    const [thisMonth, lastMonthData] = await Promise.all([
      getMonthlyTotal(selectedFloor.value, currentYear, currentMonth),
      getMonthlyTotal(selectedFloor.value, lastMonthYear, lastMonth)
    ])

    statsData.thisMonthTotal = (thisMonth.data as any)?.data || 0
    statsData.lastMonthTotal = (lastMonthData.data as any)?.data || 0

    // 获取近7天记录数（通过日期范围查询）
    const weekAgo = new Date()
    weekAgo.setDate(weekAgo.getDate() - 7)
    const today = new Date()

    const weekRecords = await getRentalRecordsByDateRange(
        selectedFloor.value,
        formatDateForAPI(weekAgo),
        formatDateForAPI(today)
    )
    const weekData = (weekRecords.data as any)?.data
    statsData.recentRecords = (weekData && Array.isArray(weekData)) ? weekData.length : 0

  } catch (error) {
    console.error('获取统计数据失败:', error)
    // 确保在错误情况下也有默认值
    statsData.thisMonthTotal = 0
    statsData.lastMonthTotal = 0
    statsData.recentRecords = 0
  }
}

// 楼层改变处理
const handleFloorChange = async () => {
  try {
    console.log('楼层切换到:', selectedFloor.value)
    pagination.page = 1
    
    // 更新路由中的楼层参数，这样FloorManagement.vue的标题也会更新
    // 使用正确的路径而不是路由名称
    const currentRoutePath = 'rental-records'  // 固定使用正确的路径
    console.log('路由跳转到:', `/floor/${selectedFloor.value}/${currentRoutePath}`)
    await router.push(`/floor/${selectedFloor.value}/${currentRoutePath}`)
    
    // 获取新楼层的数据
    fetchRecords()
    fetchStats()
  } catch (error) {
    console.error('楼层切换失败:', error)
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.page = 1
  fetchRecords()
}

// 分页处理
const handlePageChange = (page: number) => {
  pagination.page = page
  fetchRecords()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.page = 1
  fetchRecords()
}

// 多选处理
const handleSelectionChange = (selection: RentalRecord[]) => {
  selectedRecords.value = selection
}

// 排序处理
const handleSortChange = ({prop, order}: any) => {
  if (prop) {
    sortConfig.sortBy = prop
    sortConfig.sortDirection = order === 'ascending' ? 'asc' : 'desc'
    fetchRecords()
  }
}

// 按月份筛选
const filterByMonth = (monthType: string) => {
  const now = new Date()
  let targetDate = new Date()

  switch (monthType) {
    case 'current':
      // 本月
      break
    case 'last':
      // 上月
      targetDate.setMonth(targetDate.getMonth() - 1)
      break
  }

  const year = targetDate.getFullYear()
  const month = String(targetDate.getMonth() + 1).padStart(2, '0')
  monthFilter.value = `${year}-${month}`

  handleSearch()
}

// 按日期范围筛选
const filterByDateRange = (rangeType: string) => {
  const now = new Date()
  const start = new Date()

  switch (rangeType) {
    case 'week':
      start.setDate(start.getDate() - 7)
      break
    case 'month':
      start.setMonth(start.getMonth() - 1)
      break
  }

  dateRange.value = [start, now]
  handleSearch()
}

// 清空所有筛选
const clearAllFilters = () => {
  searchKeyword.value = ''
  monthFilter.value = ''
  dateRange.value = null
  handleSearch()
}

// 显示新增对话框
const showAddDialog = () => {
  resetAddForm()
  addDialog.visible = true
}

// 重置新增表单
const resetAddForm = () => {
  addDialog.form = {
    roomNumber: '',
    tenantName: '',
    totalRent: 0,
    paymentDate: ''
  }
  if (addFormRef.value) {
    addFormRef.value.resetFields()
  }
}

// 新增记录
const handleAddRecord = async () => {
  if (!addFormRef.value) return

  try {
    await addFormRef.value.validate()

    const record = {...addDialog.form}

    if (selectedFloor.value === '6') {
      await addNewRentalRecord(record)
    } else {
      await addOldRentalRecord(record)
    }

    ElMessage.success('新增缴费记录成功')
    addDialog.visible = false
    fetchRecords()
    fetchStats()

  } catch (error) {
    console.error('新增缴费记录失败:', error)
    ElMessage.error('新增缴费记录失败')
  }
}

// 查看记录
const viewRecord = (record: RentalRecord) => {
  viewDialog.record = record
  viewDialog.visible = true
}

// 删除记录
const deleteRecord = async (record: RentalRecord, index: number) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除房号为 ${record.roomNumber} 的缴费记录吗？`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await deleteRentalRecord(selectedFloor.value, record.id!)
    ElMessage.success('删除成功')
    fetchRecords()
    fetchStats()

  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const batchDelete = async () => {
  if (selectedRecords.value.length === 0) {
    ElMessage.warning('请先选择要删除的记录')
    return
  }

  try {
    const recordCount = selectedRecords.value.length
    const roomNumbers = selectedRecords.value.map(record => record.roomNumber).join('、')

    await ElMessageBox.confirm(
        `确定要删除这 ${recordCount} 条缴费记录吗？\n房号：${roomNumbers}`,
        '批量删除确认',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: false
        }
    )

    // 并行删除所有选中的记录
    const deletePromises = selectedRecords.value.map(record =>
        deleteRentalRecord(selectedFloor.value, record.id!)
    )

    await Promise.all(deletePromises)

    ElMessage.success(`成功删除 ${recordCount} 条记录`)
    selectedRecords.value = [] // 清空选择
    fetchRecords()
    fetchStats()

  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出记录
const exportRecords = async () => {
  try {
    const params: RentalRecordsQuery = {
      keyword: searchKeyword.value || undefined,
      monthFilter: monthFilter.value || undefined
    }

    if (dateRange.value) {
      params.startDate = formatDateForAPI(dateRange.value[0])
      params.endDate = formatDateForAPI(dateRange.value[1])
    }

    const response = await exportRentalRecords(selectedFloor.value, params)

    // 这里可以添加导出到Excel的逻辑
    ElMessage.success('导出成功')

  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 格式化函数
const formatCurrency = (amount: any) => {
  const num = Number(amount)
  return (isNaN(num) ? 0 : num).toLocaleString('zh-CN', {minimumFractionDigits: 2})
}

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '未设置'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

const formatMonth = (dateStr?: string) => {
  if (!dateStr) return '未知月份'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  return `${year}-${month}`
}

const formatDateTime = (dateStr?: string) => {
  if (!dateStr) return '未设置'
  return new Date(dateStr).toLocaleString('zh-CN')
}

const formatDateForAPI = (date: Date) => {
  return date.toISOString().split('T')[0]
}

// 监听路由参数变化，同步楼层选择
watch(() => route.params.floor, (newFloor) => {
  if (newFloor && newFloor !== selectedFloor.value) {
    console.log('楼层切换:', selectedFloor.value, '->', newFloor)
    selectedFloor.value = newFloor as string
    // 重置分页并刷新数据
    pagination.page = 1
    fetchRecords()
    fetchStats()
  }
}, { immediate: true })

// 页面初始化
onMounted(async () => {
  try {
    // 确保楼层选择与路由参数同步
    const routeFloor = route.params.floor as string
    if (routeFloor) {
      selectedFloor.value = routeFloor
    }
    
    await Promise.all([fetchRecords(), fetchStats()])
  } catch (error) {
    console.error('页面初始化失败:', error)
    // 确保有默认值
    tableData.value = []
    pagination.total = 0
    statsData.totalRecords = 0
    statsData.thisMonthTotal = 0
    statsData.lastMonthTotal = 0
    statsData.recentRecords = 0
  }
})
</script>

<style scoped>
/* 继承Payments.vue的样式 */
.rental-record-management {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
}

/* 背景装饰样式 */
.page-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

.bg-gradient-1,
.bg-gradient-2 {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  opacity: 0.1;
  filter: blur(100px);
}

.bg-gradient-2 {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  opacity: 0.08;
}

.bg-shape {
  position: absolute;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  filter: blur(50px);
}

.bg-shape-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
}

.bg-shape-2 {
  width: 200px;
  height: 200px;
  bottom: 100px;
  right: -50px;
}

.bg-shape-3 {
  width: 400px;
  height: 400px;
  bottom: 200px;
  left: 50%;
  transform: translateX(-50%);
}

.bg-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: particleAnimation 10s infinite;
}

@keyframes particleAnimation {
  0% {
    transform: translate(0, 0) scale(0.5);
    opacity: 0.5;
  }
  25% {
    transform: translate(50px, -50px) scale(0.7);
    opacity: 0.7;
  }
  50% {
    transform: translate(-30px, 30px) scale(0.9);
    opacity: 0.9;
  }
  75% {
    transform: translate(20px, -20px) scale(0.6);
    opacity: 0.6;
  }
  100% {
    transform: translate(0, 0) scale(0.5);
    opacity: 0.5;
  }
}

/* 页面头部样式 */
.page-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 32px;
  box-shadow: 0 8px 32px rgba(102, 126, 234, 0.3);
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 1600px;
  margin-left: auto;
  margin-right: auto;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-content h1 {
  color: white;
  font-size: 32px;
  margin: 0 0 8px 0;
  font-weight: 700;
}

.header-subtitle {
  color: rgba(255, 255, 255, 0.8);
  font-size: 16px;
  margin: 0;
}

.primary-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  font-weight: 600;
  padding: 12px 24px;
  border-radius: 12px;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.primary-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(255, 255, 255, 0.2);
}

/* 楼层选择器样式 */
.floor-selector-section {
  margin-bottom: 32px;
  width: 100%;
  max-width: 1600px;
  margin-left: auto;
  margin-right: auto;
}

.floor-selector {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

.floor-radio-group {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.floor-radio-btn {
  border-radius: 0;
  font-weight: 600;
  padding: 12px 24px;
  min-width: 80px;
  transition: all 0.3s ease;
}

.floor-radio-btn:deep(.el-radio-button__inner) {
  border-radius: 0;
  border: 1px solid #dcdfe6;
  background: white;
  color: #606266;
  font-weight: 600;
  padding: 12px 24px;
  transition: all 0.3s ease;
}

.floor-radio-btn:first-child:deep(.el-radio-button__inner) {
  border-top-left-radius: 12px;
  border-bottom-left-radius: 12px;
}

.floor-radio-btn:last-child:deep(.el-radio-button__inner) {
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
}

.floor-radio-btn:deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: #667eea;
  color: white;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.floor-radio-btn:deep(.el-radio-button__inner:hover) {
  border-color: #667eea;
  color: #667eea;
}

.floor-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 16px;
}

.floor-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  color: #303133;
}

.floor-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
}

.floor-selector {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

/* 统计卡片样式 */
.stats-section {
  margin-bottom: 32px;
  width: 100%;
  max-width: 1600px;
  margin-left: auto;
  margin-right: auto;
}

.stats-row {
  margin-top: 0;
}

.stats-card {
  background: white;
  border-radius: 20px;
  padding: 24px;
  height: 140px;
  position: relative;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
}

.stats-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.stats-card.primary-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stats-card.success-card {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
}

.stats-card.warning-card {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stats-card.danger-card {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stats-card.enhanced-card {
  position: relative;
  overflow: hidden;
}

.card-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
  z-index: 1;
}

.stats-card:hover .card-glow {
  opacity: 1;
}

.icon-ripple {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
  transition: transform 0.5s ease-out, opacity 0.5s ease-out;
  z-index: 2;
}

.stats-card:hover .icon-ripple {
  transform: translate(-50%, -50%) scale(1);
  opacity: 1;
}

.hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.05) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
  z-index: 1;
}

.stats-card:hover .hover-effect {
  opacity: 1;
}

.stats-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: calc(100% - 20px);
  position: relative;
  z-index: 2;
}

.stats-number {
  font-size: 36px;
  font-weight: 800;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 16px;
  font-weight: 500;
  opacity: 0.9;
}

.stats-icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  position: relative;
  overflow: hidden;
}

.stats-icon {
  font-size: 32px;
  color: white;
  z-index: 3;
}

.stats-progress {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: rgba(255, 255, 255, 0.2);
}

.progress-bar {
  height: 100%;
  border-radius: 2px;
  animation: progressAnimation 2s ease-in-out;
}

.primary-progress {
  background: rgba(255, 255, 255, 0.8);
  width: 85%;
}

.success-progress {
  background: rgba(255, 255, 255, 0.8);
  width: 92%;
}

.warning-progress {
  background: rgba(255, 255, 255, 0.8);
  width: 68%;
}

.danger-progress {
  background: rgba(255, 255, 255, 0.8);
  width: 45%;
}

@keyframes progressAnimation {
  from {
    width: 0;
  }
}

/* 操作区域样式 */
.operation-section {
  margin-bottom: 32px;
  width: 100%;
  max-width: 1600px;
  margin-left: auto;
  margin-right: auto;
}

.operation-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 24px;
}

.operation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.operation-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  color: #303133;
}

.operation-extra {
  display: flex;
  align-items: center;
  gap: 16px;
}

.month-shortcuts {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(102, 126, 234, 0.1);
}

.month-shortcut-btn {
  color: #667eea;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.month-shortcut-btn:hover {
  background: rgba(102, 126, 234, 0.1);
  color: #5a67d8;
}

.clear-filters-btn {
  color: #909399;
  font-size: 14px;
}

.clear-filters-btn:hover {
  color: #667eea;
}

.search-row {
  margin-top: 0;
}

.enhanced-input {
  border-radius: 12px;
}

.search-input {
  width: 100%;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  font-weight: 600;
}

.export-btn {
  border-radius: 12px;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.export-btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
}

/* 表格容器 */
.table-container {
  width: 100%;
  max-width: 1600px;
  margin: 0 auto;
}

/* 表格样式 */
.modern-table-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  width: 100%;
  margin: 0 auto;
}

.modern-table {
  border-radius: 12px;
  overflow: hidden;
  width: 100%;
  table-layout: auto;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.table-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.batch-delete-btn {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border: none;
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.batch-delete-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(255, 154, 158, 0.3);
}

.batch-delete-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.table-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  color: #303133;
}

.title-icon {
  color: #667eea;
}

.count-tag {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 20px;
}

.record-id-cell {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.record-id-tag {
  font-weight: 600;
  border-radius: 8px;
  font-size: 12px;
  font-family: 'Courier New', monospace;
}

.room-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.room-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
}

.room-tag {
  font-weight: 600;
  border-radius: 8px;
  font-size: 14px;
}

.tenant-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.tenant-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.tenant-details {
  flex: 1;
}

.tenant-name {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
  margin-bottom: 2px;
}

.tenant-phone {
  font-size: 12px;
  color: #909399;
}

.amount-details {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.amount-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.amount-icon {
  font-size: 16px;
}

.amount-label {
  color: #606266;
  min-width: 40px;
}

.amount-value {
  font-weight: 600;
  color: #303133;
}

.total-amount {
  color: #f56c6c;
  font-size: 16px;
  font-weight: 700;
}

.date-info,
.create-time {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #606266;
}

.date-content,
.time-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.date-text {
  font-size: 13px;
  color: #606266;
}

.month-tag {
  font-size: 11px;
  color: #909399;
  background: rgba(144, 147, 153, 0.1);
  padding: 1px 4px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.action-buttons-group {
  display: flex;
  gap: 6px;
}

.action-btn {
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.view-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.delete-btn {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border: none;
}

/* 分页样式 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 32px;
}

.modern-pagination {
  border-radius: 12px;
}

/* 对话框样式 */
.modern-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.modern-form {
  max-height: 65vh;
  overflow-y: auto;
  padding-right: 8px;
}

.modern-form::-webkit-scrollbar {
  width: 6px;
}

.modern-form::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.modern-form::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 3px;
}

.gradient-card {
  border-radius: 16px;
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
  margin-bottom: 24px;
}

.gradient-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.form-card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #303133;
}

.card-icon {
  color: #667eea;
  font-size: 18px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 0;
}

.cancel-btn {
  border-radius: 12px;
  padding: 12px 24px;
  font-weight: 600;
}

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  padding: 12px 24px;
  font-weight: 600;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

/* 查看详情样式 */
.record-detail {
  max-height: 65vh;
  overflow-y: auto;
  padding-right: 8px;
}

.modern-descriptions {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .rental-record-management {
    padding: 16px;
  }

  .page-header {
    padding: 20px;
    margin-bottom: 20px;
  }

  .header-content {
    flex-direction: column;
    gap: 16px;
  }

  .stats-card {
    height: 120px;
    padding: 16px;
  }

  .stats-number {
    font-size: 28px;
  }

  .action-buttons-group {
    flex-wrap: wrap;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.stats-card,
.operation-card,
.modern-table-card,
.floor-card {
  animation: fadeInUp 0.6s ease-out;
}

.stats-card:nth-child(1) {
  animation-delay: 0.1s;
}

.stats-card:nth-child(2) {
  animation-delay: 0.2s;
}

.stats-card:nth-child(3) {
  animation-delay: 0.3s;
}

.stats-card:nth-child(4) {
  animation-delay: 0.4s;
}
</style> 