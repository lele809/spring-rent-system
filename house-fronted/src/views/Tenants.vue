<template>
  <div class="tenants-management">
    <!-- 背景装饰 -->
    <div class="page-background">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
      <div class="bg-particles">
        <div class="particle" v-for="n in 10" :key="n" :style="getParticleStyle(n)"></div>
      </div>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="main-layout">
      <!-- 主内容区 -->
      <div class="main-content">
        <!-- 页面标题区域 -->
        <div class="page-header">
          <div class="header-content">
            <div class="header-title">
              <el-icon size="28" color="#667eea"><UserFilled /></el-icon>
              <div class="title-content">
                <h1>{{ currentFloor }}楼联系人管理</h1>
                <p class="header-subtitle">智能化租客信息与关系管理系统</p>
              </div>
            </div>
            <div class="header-actions">
              <el-button 
                type="primary" 
                size="large" 
                @click="showAddDialog = true"
                class="primary-btn enhanced-btn"
              >
                <el-icon><Plus /></el-icon>
                添加联系人
              </el-button>
            </div>
          </div>
        </div>

    <!-- 统计概览卡片 -->
    <div class="stats-section">
      <el-row :gutter="24" class="stats-row">
        <el-col :span="6">
          <div class="stats-card primary-card enhanced-card" @click="handleFilterTenants('all')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ tenants?.length || 0 }}</div>
                <div class="stats-label">总联系人</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper primary-icon">
                  <el-icon class="stats-icon"><UserFilled /></el-icon>
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
          <div class="stats-card success-card enhanced-card" @click="handleFilterTenants('active')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ tenants?.length || 0 }}</div>
                <div class="stats-label">活跃联系人</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper success-icon">
                  <el-icon class="stats-icon"><Avatar /></el-icon>
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
          <div class="stats-card warning-card enhanced-card" @click="handleFilterTenants('recent')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ tenants?.filter(t => isRecentTenant(t))?.length || 0 }}</div>
                <div class="stats-label">新增联系人</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper warning-icon">
                  <el-icon class="stats-icon"><Clock /></el-icon>
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
          <div class="stats-card danger-card enhanced-card" @click="handleFilterTenants('pending')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">0</div>
                <div class="stats-label">待审核</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper danger-icon">
                  <el-icon class="stats-icon"><TrendCharts /></el-icon>
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
          <div class="responsive-filter-header">
            <!-- 桌面端头部 -->
            <div class="desktop-header">
              <div class="operation-header">
                <div class="operation-title">
                  <el-icon size="20"><Filter /></el-icon>
                  <span class="title-text">筛选与搜索</span>
                </div>
                <div class="operation-extra">
                  <el-button 
                    @click="refreshData"
                    type="success" 
                    size="default"
                    class="refresh-btn"
                  >
                    <el-icon><Refresh /></el-icon>
                    <span class="btn-text">刷新数据</span>
                  </el-button>
                  <el-button 
                    v-if="hasActiveFilters"
                    @click="clearFilters" 
                    type="text" 
                    class="clear-filters-btn"
                  >
                    <el-icon><Refresh /></el-icon>
                    <span class="btn-text">清空筛选</span>
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 移动端头部 -->
            <div class="mobile-header">
              <div class="mobile-operation-header">
                <div class="mobile-title">
                  <el-icon size="18"><Filter /></el-icon>
                  <span class="title-text">筛选与搜索</span>
                </div>
                <div class="mobile-actions">
                  <el-button 
                    @click="refreshData"
                    type="success" 
                    size="small"
                    class="mobile-refresh-btn"
                  >
                    <el-icon size="16"><Refresh /></el-icon>
                  </el-button>
                  <el-button 
                    v-if="hasActiveFilters"
                    @click="clearFilters" 
                    type="text" 
                    size="small"
                    class="mobile-clear-btn"
                  >
                    <el-icon size="16"><Refresh /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </template>
        
        <!-- 搜索区域 -->
        <div class="search-section">
          <el-row :gutter="20" class="search-row">
            <el-col :xs="24" :sm="12" :md="10" :lg="10" :xl="10">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索姓名、房号或电话..."
                clearable
                size="large"
                class="search-input enhanced-input"
                @keyup.enter="handleSearch"
                @clear="handleSearch"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Search /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :xs="24" :sm="8" :md="6" :lg="4" :xl="6">
              <el-select
                v-model="roomFilter"
                placeholder="筛选房间号"
                clearable
                size="large"
                class="room-filter enhanced-input"
                @change="handleRoomFilter"
                style="width: 100%"
              >
                <template #prefix>
                  <el-icon class="input-icon"><Key /></el-icon>
                </template>
                <el-option
                  v-for="room in allRooms"
                  :key="room"
                  :label="room"
                  :value="room"
                >
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-icon size="16"><Key /></el-icon>
                    <span>{{ room }}</span>
                  </div>
                </el-option>
              </el-select>
            </el-col>
            <el-col :xs="24" :sm="4" :md="8" :lg="5" :xl="8">
              <div class="search-actions">
                <el-button 
                  type="primary" 
                  size="large" 
                  @click="handleSearch"
                  class="search-btn enhanced-btn"
                >
                  <el-icon><Search /></el-icon>
                  <span class="btn-text">搜索</span>
                </el-button>
                <el-button 
                  v-if="hasActiveFilters"
                  type="default" 
                  size="large" 
                  @click="clearAllFilters"
                  class="clear-btn enhanced-btn"
                >
                  <el-icon><Refresh /></el-icon>
                  <span class="btn-text">清空</span>
                </el-button>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>

    <!-- 联系人列表表格 -->
    <div class="table-container">
      <el-card class="table-card modern-table-card" shadow="hover">
        <template #header>
          <div class="table-header">
            <div class="table-title">
              <el-icon class="title-icon"><UserFilled /></el-icon>
              <span>联系人详细信息</span>
            </div>
            <div class="table-actions">
              <el-tag type="primary" size="large" class="count-tag">共 {{ total }} 条记录</el-tag>
              <el-button 
                type="primary" 
                size="default"
                @click="showAddDialog = true"
                class="add-tenant-btn"
              >
                <el-icon><Plus /></el-icon>
                <span>添加联系人</span>
              </el-button>
            </div>
          </div>
        </template>

        <el-table
          v-loading="loading"
          :data="tenants"
          stripe
          style="width: 100%"
          class="modern-table"
          empty-text="暂无联系人数据"
          :header-cell-style="{ 
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            color: '#fff',
            fontWeight: 'bold',
            borderBottom: 'none'
          }"
          :row-style="{ borderBottom: '1px solid #f0f2f5' }"
        >
            <el-table-column prop="name" label="姓名" width="180">
              <template #default="{ row }">
                <div class="tenant-name-card enhanced">
                  <div class="tenant-avatar premium">
                    <el-icon size="22"><Avatar /></el-icon>
                    <div class="avatar-glow"></div>
                  </div>
                  <div class="tenant-info">
                    <div class="tenant-header">
                    <span class="tenant-name">{{ row.name }}</span>
                      <div class="tenant-badge">
                        <el-icon size="12"><UserFilled /></el-icon>
                      </div>
                    </div>
                    <span class="tenant-id">ID: {{ row.id }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="roomId" label="房号" width="160">
              <template #default="{ row }">
                <div class="room-badge premium">
                  <div class="room-icon-wrapper">
                    <el-icon size="18"><Key /></el-icon>
                    <div class="icon-glow"></div>
                  </div>
                  <span class="room-text">{{ row.roomId }}</span>
                  <div class="room-border"></div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="phone" label="电话" width="190">
              <template #default="{ row }">
                <div class="phone-display premium">
                  <div class="phone-icon">
                    <el-icon size="18" color="#10b981"><Phone /></el-icon>
                    <div class="icon-shine"></div>
                  </div>
                  <div class="phone-content">
                    <span class="phone-number">{{ row.phone }}</span>
                    <span class="phone-label">手机</span>
                  </div>
                  <div class="phone-border"></div>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="idCard" label="身份证号" width="260">
              <template #default="{ row }">
                <div class="id-card-display">
                  <div class="id-card-icon">
                    <el-icon size="16" color="#6366f1"><CreditCard /></el-icon>
                  </div>
                  <span class="id-card-number premium">{{ row.idCard }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column prop="createdAt" label="创建时间" width="180">
              <template #default="{ row }">
                <div class="time-display">
                  <el-icon size="14" color="#9ca3af"><Clock /></el-icon>
                  <span>{{ formatDate(row.createdAt) }}</span>
                </div>
              </template>
            </el-table-column>
            
            <el-table-column label="操作" width="280" fixed="right">
              <template #default="{ row }">
                <div class="action-buttons enhanced">
                  <div class="action-btn edit-btn premium" @click="editTenant(row)" title="编辑租客">
                    <div class="btn-icon animated">
                      <el-icon size="16"><Edit /></el-icon>
                      <div class="icon-bg"></div>
                    </div>
                    <span class="btn-text">编辑</span>
                    <div class="btn-ripple"></div>
                  </div>
                  <div 
                    class="action-btn delete-btn premium" 
                    @click="deleteTenant(row)"
                    title="删除租客"
                  >
                    <div class="btn-icon animated">
                      <el-icon size="16"><Delete /></el-icon>
                      <div class="icon-bg"></div>
                    </div>
                    <span class="btn-text">删除</span>
                    <div class="btn-ripple"></div>
                  </div>
                </div>
              </template>
            </el-table-column>
          </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            :current-page="currentPage"
            :page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            class="modern-pagination"
          />
        </div>
      </el-card>
    </div>

    <!-- 添加/编辑租客对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingTenant ? '编辑租客信息' : '添加新租客'"
      width="600px"
      @closed="resetForm"
      class="modern-dialog"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      append-to-body
    >
      <template #header="{ titleId, titleClass }">
        <div class="dialog-header">
          <div class="dialog-title" :id="titleId" :class="titleClass">
            <el-icon size="24" color="#667eea">
              <component :is="editingTenant ? 'Edit' : 'Plus'" />
            </el-icon>
            <span>{{ editingTenant ? '编辑租客信息' : '添加新租客' }}</span>
          </div>
        </div>
      </template>

      <div class="dialog-content">
        <el-form
          ref="tenantFormRef"
          :model="tenantForm"
          :rules="tenantRules"
          label-width="100px"
          class="modern-form"
          label-position="top"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系人姓名" prop="name">
                <el-input 
                  v-model="tenantForm.name" 
                  placeholder="请输入联系人真实姓名（2-10个字符）"
                  size="large"
                  class="form-input"
                  maxlength="10"
                  show-word-limit
                  clearable
                  @input="tenantForm.name = tenantForm.name.replace(/[^\u4e00-\u9fa5a-zA-Z\s]/g, '')"
                >
                  <template #prefix>
                    <el-icon><Avatar /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="房间号码" prop="roomId">
                <el-select 
                  v-model="tenantForm.roomId" 
                  placeholder="请选择空闲房间"
                  size="large"
                  class="form-input"
                  clearable
                  filterable
                  :loading="loadingRooms"
                  style="width: 100%"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                  <el-option
                    v-for="room in availableRooms"
                    :key="room.id"
                    :label="`${room.roomNumber} (${room.roomType})`"
                    :value="room.roomNumber"
                  >
                    <div style="display: flex; align-items: center; justify-content: space-between;">
                      <span>
                        <el-icon style="margin-right: 8px;"><Key /></el-icon>
                        {{ room.roomNumber }}
                      </span>
                      <span style="color: #8492a6; font-size: 12px;">
                        {{ room.roomType }} | ¥{{ room.baseRent }}
                      </span>
                    </div>
                  </el-option>
                  <template v-if="availableRooms.length === 0 && !loadingRooms">
                    <el-option disabled value="" label="暂无空闲房间" />
                  </template>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="手机号码" prop="phone">
                <el-input 
                  v-model="tenantForm.phone" 
                  placeholder="请输入11位手机号码"
                  size="large"
                  class="form-input"
                  maxlength="11"
                  clearable
                  @input="tenantForm.phone = tenantForm.phone.replace(/[^\d]/g, '')"
                >
                  <template #prefix>
                    <el-icon><Phone /></el-icon>
                  </template>
                  <template #suffix v-if="tenantForm.phone && /^1[3-9]\d{9}$/.test(tenantForm.phone)">
                    <el-icon color="#67c23a" size="16">
                      <Check />
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="身份证号码" prop="idCard">
                <el-input 
                  v-model="tenantForm.idCard" 
                  placeholder="请输入18位身份证号码"
                  size="large"
                  class="form-input"
                  maxlength="18"
                  clearable
                  @input="tenantForm.idCard = tenantForm.idCard.replace(/[^0-9Xx]/g, '').toUpperCase()"
                >
                  <template #prefix>
                    <el-icon><CreditCard /></el-icon>
                  </template>
                  <template #suffix v-if="tenantForm.idCard && /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(tenantForm.idCard)">
                    <el-icon color="#67c23a" size="16">
                      <Check />
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button 
            @click="showAddDialog = false"
            size="large"
            class="cancel-btn"
          >
            取消
          </el-button>
          <el-button 
            type="primary" 
            @click="saveTenant" 
            :loading="saving"
            size="large"
            class="confirm-btn"
          >
            <el-icon v-if="!saving">
              <component :is="editingTenant ? 'Check' : 'Plus'" />
            </el-icon>
            {{ editingTenant ? '更新租客' : '添加租客' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox, ElForm } from 'element-plus'
import { 
  Plus, 
  Refresh, 
  Search,
  UserFilled,
  Avatar,
  TrendCharts,
  List,
  Key,
  Phone,
  Clock,
  Edit,
  Delete,
  CreditCard,
  Check,
  Filter
} from '@element-plus/icons-vue'
import { 
  getTenants, 
  searchTenants, 
  createTenant, 
  updateTenant, 
  deleteTenant as deleteTenantApi,
  type Tenant
} from '../api/tenants'
import { getAvailableRooms, type Room } from '../api/rooms'

const route = useRoute()

// 当前楼层
const currentFloor = computed(() => route.params.floor as string)

// 表单引用
const tenantFormRef = ref<InstanceType<typeof ElForm>>()

// 状态
const loading = ref(false)
const saving = ref(false)
const showAddDialog = ref(false)
const editingTenant = ref<Tenant | null>(null)

// 搜索和筛选
const searchKeyword = ref('')
const roomFilter = ref('')
const statusFilter = ref('')
const dateRange = ref('')
const sortOrder = ref('')

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 租客列表
const tenants = ref<Tenant[]>([])

// 空闲房间列表
const availableRooms = ref<Room[]>([])
const loadingRooms = ref(false)

// 所有房间列表（用于筛选）
const allRooms = ref<string[]>([])

// 计算属性
const hasActiveFilters = computed(() => {
  return !!searchKeyword.value || !!roomFilter.value
})

// 租客表单
const tenantForm = reactive({
  name: '',
  roomId: '',
  phone: '',
  idCard: ''
})

// 表单验证规则
const tenantRules = {
  name: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度应为2-10个字符', trigger: 'blur' },
    { 
      validator: (rule: any, value: string, callback: Function) => {
        if (value && !/^[\u4e00-\u9fa5a-zA-Z\s]+$/.test(value)) {
          callback(new Error('姓名只能包含中文、英文和空格'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  roomId: [
    { required: true, message: '请选择房间号码', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { 
      validator: (rule: any, value: string, callback: Function) => {
        if (!value) {
          callback(new Error('请输入手机号码'))
        } else if (!/^1[3-9]\d{9}$/.test(value)) {
          callback(new Error('请输入正确的11位手机号码（以1开头）'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  idCard: [
    { required: true, message: '请输入身份证号码', trigger: 'blur' },
    { 
      validator: (rule: any, value: string, callback: Function) => {
        if (!value) {
          callback(new Error('请输入身份证号码'))
        } else if (!/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(value)) {
          callback(new Error('请输入正确的18位身份证号码'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 生成粒子样式
const getParticleStyle = (index: number) => {
  const size = Math.random() * 4 + 2
  const left = Math.random() * 100
  const top = Math.random() * 100
  const animationDelay = Math.random() * 10
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

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

// 判断是否为最近新增的租客
const isRecentTenant = (tenant: Tenant) => {
  if (!tenant.createdAt) return false
  const now = new Date()
  const createdAt = new Date(tenant.createdAt)
  const oneMonthAgo = new Date(now.getFullYear(), now.getMonth() - 1, now.getDate())
  return createdAt >= oneMonthAgo
}

// 统计卡片筛选处理
const handleFilterTenants = (type: string) => {
  // 这里可以根据类型进行不同的筛选逻辑
  // 例如筛选活跃用户、最近新增用户等
  console.log('筛选类型:', type)
}

// 加载租客数据
const loadTenants = async () => {
  loading.value = true
  try {
    console.log('开始加载租客数据，楼层:', currentFloor.value)
    let response
    
    // 如果有搜索关键词，使用搜索API
    if (searchKeyword.value && searchKeyword.value.trim()) {
      console.log('使用搜索API，关键词:', searchKeyword.value.trim())
      response = await searchTenants(currentFloor.value, searchKeyword.value.trim())
      let filteredData = response.data.data || []
      
      // 如果有房间筛选，进一步过滤数据
      if (roomFilter.value) {
        filteredData = filteredData.filter((tenant: Tenant) => tenant.roomId === roomFilter.value)
      }
      
      tenants.value = filteredData
      total.value = filteredData.length
    } else {
      // 否则使用分页API
      const query = {
        page: currentPage.value - 1, // 后端页码从0开始
        size: pageSize.value
      }
      
      console.log('使用分页API，查询参数:', query)
      response = await getTenants(currentFloor.value, query)
      console.log('API响应:', response)
      
      if (response && response.code === 200 && response.data && response.data.content) {
        let filteredData = response.data.content
        
        // 如果有房间筛选，进一步过滤数据
        if (roomFilter.value) {
          filteredData = filteredData.filter((tenant: Tenant) => tenant.roomId === roomFilter.value)
        }
        
        tenants.value = filteredData
        total.value = roomFilter.value ? filteredData.length : response.data.totalElements
        console.log('设置租客数据，数量:', tenants.value.length)
      } else {
        console.warn('API响应格式异常:', response)
        tenants.value = []
        total.value = 0
      }
    }
    
  } catch (error: any) {
    console.error('加载租客数据失败:', error)
    ElMessage.error('加载租客数据失败: ' + (error.response?.data?.message || error.message))
    // 确保即使出错也有默认值
    tenants.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 加载空闲房间列表
const loadAvailableRooms = async () => {
  loadingRooms.value = true
  try {
    const response = await getAvailableRooms(currentFloor.value) as any
    
    console.log('获取空闲房间响应:', response)
    
    // 正确访问axios响应结构：response直接是ApiResponse
    if (response && response.code === 200) {
      availableRooms.value = response.data || []
      console.log('空闲房间加载成功:', availableRooms.value.length, '间')
    } else {
      const errorMessage = response?.message || '获取空闲房间列表失败'
      console.error('获取空闲房间失败:', errorMessage)
      ElMessage.error(errorMessage)
    }
  } catch (error: any) {
    console.error('加载空闲房间异常:', error)
    ElMessage.error('获取空闲房间失败，请重试')
  } finally {
    loadingRooms.value = false
  }
}

// 加载所有房间列表（用于筛选）
const loadAllRooms = async () => {
  try {
    // 从当前租客数据中提取所有房间号
    const roomsFromTenants = tenants.value.map(tenant => tenant.roomId).filter(Boolean)
    
    // 从空闲房间中提取房间号
    const roomsFromAvailable = availableRooms.value.map(room => room.roomNumber).filter(Boolean)
    
    // 合并并去重
    const uniqueRooms = new Set([...roomsFromTenants, ...roomsFromAvailable])
    const allRoomNumbers = Array.from(uniqueRooms).sort()
    
    allRooms.value = allRoomNumbers
    console.log('加载所有房间列表成功:', allRooms.value.length, '间')
  } catch (error: any) {
    console.error('加载所有房间列表失败:', error)
  }
}

// 刷新数据
const refreshData = () => {
  loadTenants()
  loadAvailableRooms()
  // 延迟加载所有房间列表，确保租客和空闲房间数据先加载完成
  setTimeout(() => {
    loadAllRooms()
  }, 500)
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadTenants()
}

// 房间筛选
const handleRoomFilter = () => {
  currentPage.value = 1
  loadTenants()
}

// 清空筛选
const clearFilters = () => {
  searchKeyword.value = ''
  currentPage.value = 1
  loadTenants()
}

// 清空所有筛选
const clearAllFilters = () => {
  searchKeyword.value = ''
  roomFilter.value = ''
  currentPage.value = 1
  loadTenants()
}

// 重置所有筛选条件
const handleReset = () => {
  searchKeyword.value = ''
  statusFilter.value = ''
  dateRange.value = ''
  sortOrder.value = ''
  currentPage.value = 1
  loadTenants()
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadTenants()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadTenants()
}

// 编辑租客
const editTenant = (tenant: Tenant) => {
  editingTenant.value = tenant
  Object.assign(tenantForm, {
    name: tenant.name,
    roomId: tenant.roomId,
    phone: tenant.phone,
    idCard: tenant.idCard
  })
  showAddDialog.value = true
}

// 删除租客
const deleteTenant = async (tenant: Tenant) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除租客 ${tenant.name} 吗？`,
      '确认删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用删除API
    await deleteTenantApi(currentFloor.value, tenant.id)
    
    ElMessage.success('删除成功')
    loadTenants()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除租客失败:', error)
      ElMessage.error('删除租客失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

// 保存租客
const saveTenant = async () => {
  if (!tenantFormRef.value) return
  
  // 表单验证
  const valid = await tenantFormRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.warning('请检查表单信息是否填写正确')
    return
  }
  
  saving.value = true
  
  try {
    // 数据预处理
    const tenantData = {
      name: tenantForm.name.trim(),
      roomId: tenantForm.roomId.trim(),
      phone: tenantForm.phone.trim(),
      idCard: tenantForm.idCard.trim().toUpperCase()
    }
    
    // 额外的客户端验证
    if (!tenantData.name || !tenantData.roomId || !tenantData.phone || !tenantData.idCard) {
      ElMessage.error('所有字段都不能为空')
      return
    }
    
    console.log('正在保存联系人数据:', tenantData)
    
    if (editingTenant.value) {
      // 更新租客
      const response = await updateTenant(currentFloor.value, editingTenant.value.id, tenantData)
      console.log('更新联系人成功:', response)
      ElMessage.success('联系人信息更新成功！')
    } else {
      // 创建租客
      const response = await createTenant(currentFloor.value, tenantData)
      console.log('创建联系人成功:', response)
      ElMessage({
        type: 'success',
        message: '新联系人添加成功！',
        duration: 3000,
        showClose: true
      })
    }
    
    showAddDialog.value = false
    resetForm()
    
    // 延迟刷新数据，确保后端数据已更新
    setTimeout(() => {
      loadTenants()
    }, 500)
    
  } catch (error: any) {
    console.error('保存租客失败:', error)
    
    // 根据不同错误类型显示不同信息
    let errorMessage = '操作失败'
    if (error.response?.status === 400) {
      errorMessage = error.response.data?.message || '数据验证失败，请检查输入信息'
    } else if (error.response?.status === 409) {
      errorMessage = '联系人信息冲突（手机号或身份证可能已存在）'
    } else if (error.response?.status === 500) {
      errorMessage = '服务器内部错误，请稍后重试'
    } else if (error.message) {
      errorMessage = error.message
    }
    
    ElMessage({
      type: 'error',
      message: errorMessage,
      duration: 5000,
      showClose: true
    })
  } finally {
    saving.value = false
  }
}

// 重置表单
const resetForm = () => {
  editingTenant.value = null
  Object.assign(tenantForm, {
    name: '',
    roomId: '',
    phone: '',
    idCard: ''
  })
  if (tenantFormRef.value) {
    tenantFormRef.value.clearValidate()
  }
}

onMounted(async () => {
  await loadTenants()
  await loadAvailableRooms()
  // 加载所有房间列表用于筛选
  loadAllRooms()
})

// 监听对话框显示，每次打开时刷新空闲房间
watch(showAddDialog, (newValue) => {
  if (newValue) {
    loadAvailableRooms()
  }
})
</script>

<style scoped>
/* 主容器 */
.tenants-management {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  width: 100%;
  margin: 0;
}

/* 主布局 */
.main-layout {
  width: 100%;
}

/* 主内容区 */
.main-content {
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

.bg-gradient-1 {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%);
  animation: gradientShift 20s ease-in-out infinite;
}

.bg-gradient-2 {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 80% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  animation: gradientShift 25s ease-in-out infinite reverse;
}

@keyframes gradientShift {
  0%, 100% { transform: scale(1) rotate(0deg); opacity: 0.3; }
  50% { transform: scale(1.1) rotate(10deg); opacity: 0.1; }
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.06;
  background: linear-gradient(45deg, rgba(255, 255, 255, 0.2), rgba(147, 197, 253, 0.2));
  animation: float 15s ease-in-out infinite;
  filter: blur(1px);
}

.bg-shape-1 {
  width: 400px;
  height: 400px;
  top: -150px;
  right: -150px;
  animation-delay: 0s;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.15) 0%, transparent 70%);
}

.bg-shape-2 {
  width: 300px;
  height: 300px;
  bottom: -100px;
  left: -100px;
  animation-delay: 7s;
  background: radial-gradient(circle, rgba(139, 69, 194, 0.15) 0%, transparent 70%);
}

.bg-shape-3 {
  width: 200px;
  height: 200px;
  top: 30%;
  right: 15%;
  animation-delay: 14s;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.15) 0%, transparent 70%);
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
  background: rgba(255, 255, 255, 0.4);
  border-radius: 50%;
  animation: particleFloat linear infinite;
}

@keyframes particleFloat {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-10vh) rotate(360deg);
    opacity: 0;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-30px) rotate(180deg); }
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

/* 统计卡片样式 */
.stats-section {
  margin-bottom: 32px;
  width: 100%;
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
  from { width: 0; }
}

/* 操作区域样式 */
.operation-section {
  margin-bottom: 32px;
  width: 100%;
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
  gap: 8px;
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
  align-items: center;
}

/* 响应式筛选头部 */
.responsive-filter-header {
  width: 100%;
}

.desktop-header {
  display: block;
}

.mobile-header {
  display: none;
}

.operation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  min-height: 40px;
}

.operation-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-text-color-primary);
  font-weight: 600;
  font-size: 16px;
}

.title-text {
  white-space: nowrap;
}

.operation-extra {
  display: flex;
  align-items: center;
  gap: 12px;
}

.refresh-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-weight: 600;
  margin-right: 8px;
}

.refresh-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
}

.clear-filters-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--el-color-primary);
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.clear-filters-btn:hover {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.mobile-operation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 12px 0;
}

.mobile-title {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--el-text-color-primary);
  font-weight: 600;
  font-size: 15px;
  flex: 1;
}

.mobile-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.mobile-refresh-btn {
  padding: 6px;
  min-width: auto;
  border-radius: 6px;
  margin-right: 6px;
  transition: all 0.3s ease;
}

.mobile-refresh-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(22, 163, 74, 0.3);
}

.mobile-clear-btn {
  padding: 6px;
  min-width: auto;
  border-radius: 6px;
  color: var(--el-color-primary);
  transition: all 0.3s ease;
}

.mobile-clear-btn:hover {
  background-color: var(--el-color-primary-light-9);
}

/* 搜索区域 */
.search-section {
  width: 100%;
}

/* 房间筛选器 */
.room-filter :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 2px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.3s ease !important;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  height: 48px;
}

.room-filter :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
  transform: translateY(-1px);
}

.room-filter :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
  transform: translateY(-2px);
}

/* 搜索操作区域 */
.search-actions {
  display: flex;
  gap: 12px;
  width: 100%;
  justify-content: flex-start;
}

.clear-btn {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 1px solid #e5e7eb !important;
  color: #6b7280 !important;
  border-radius: 12px !important;
  font-weight: 600 !important;
  transition: all 0.3s ease !important;
}

.clear-btn:hover {
  background: #f9fafb !important;
  border-color: #d1d5db !important;
  transform: translateY(-1px) !important;
}

/* 响应式断点 */
@media (max-width: 768px) {
  .operation-section {
    margin: 0 16px;
  }
  
  /* 头部响应式 */
  .desktop-header {
    display: none;
  }
  
  .mobile-header {
    display: block;
  }
  
  .operation-card {
    border-radius: 16px;
    overflow: hidden;
  }
  
  /* 移动端头部优化 */
  .mobile-title {
    font-size: 15px;
  }
  
  .operation-title {
    font-size: 15px;
  }
}

@media (max-width: 480px) {
  .operation-section {
    margin: 0 12px;
  }
  
  /* 头部小屏幕优化 */
  .mobile-title {
    font-size: 14px;
  }
  
  .title-text {
    font-size: 14px;
  }
  
  .mobile-clear-btn {
    padding: 4px;
  }
  
  .clear-filters-btn {
    padding: 5px 8px;
    font-size: 12px;
  }
  
  .search-btn .btn-text,
  .clear-btn .btn-text {
    display: none; /* 在很小的屏幕上按钮只显示图标 */
  }

  .search-actions {
    flex-direction: row;
    justify-content: center;
    gap: 8px;
  }

  
  .search-input :deep(.el-input__inner) {
    font-size: 14px;
  }
}

/* 搜索输入框样式 */
.search-input :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 2px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.3s ease !important;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  height: 48px;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
  transform: translateY(-1px);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
  transform: translateY(-2px);
}

.input-icon {
  color: #9ca3af;
  transition: color 0.3s ease;
  font-size: 18px;
}

.search-input:hover .input-icon {
  color: #667eea;
}

.search-input :deep(.el-input__inner) {
  font-size: 16px;
  color: #374151;
}

.search-input :deep(.el-input__inner::placeholder) {
  color: #9ca3af;
}

/* 搜索按钮样式 */
.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
  height: 48px;
  width: 100%;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

/* 表格容器 */
.table-container {
  width: 100%;
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

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  width: 100%;
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

.table-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.count-tag {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 20px;
}

.add-tenant-btn {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border: none;
  border-radius: 10px;
  font-weight: 600;
  padding: 8px 16px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.3);
}

.add-tenant-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.4);
}

.header-stats {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: nowrap;
}

.quick-stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  padding: 20px 24px;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  min-width: 160px;
  flex-shrink: 0;
}

/* 水平布局样式 */
.quick-stat-card.horizontal-layout {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  min-width: 200px;
}

.quick-stat-card.horizontal-layout .stat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.quick-stat-card.horizontal-layout .stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 2px;
  line-height: 1;
}

.quick-stat-card.horizontal-layout .stat-label {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
  line-height: 1;
  white-space: nowrap;
}

.quick-stat-card.horizontal-layout .stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  flex-shrink: 0;
}

.quick-stat-card.horizontal-layout .stat-trend {
  width: 28px;
  height: 28px;
  border-radius: 6px;
  flex-shrink: 0;
}

.quick-stat-card.horizontal-layout .stat-trend span {
  font-size: 10px;
  font-weight: 600;
}

.quick-stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
}

.income-card .stat-icon {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.3);
}

.pending-card .stat-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;
}

.stat-icon::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(255,255,255,0.2) 0%, transparent 50%, rgba(255,255,255,0.2) 100%);
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.quick-stat-card:hover .stat-icon::before {
  transform: translateX(100%);
}

.stat-content {
  flex: 1;
}

.stat-number {
  display: block;
  font-size: 28px;
  font-weight: 800;
  color: #1a202c;
  margin-bottom: 4px;
  letter-spacing: -0.5px;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 600;
}

.stat-trend {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: rgba(16, 185, 129, 0.1);
}

.stat-trend.positive {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
}

.stat-trend.positive .el-icon {
  color: #059669;
}

.stat-trend.positive span {
  font-size: 12px;
  font-weight: 700;
  color: #059669;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1) rotate(5deg);
}

/* 统计卡片区域 */
.stats-section {
  position: relative;
  z-index: 10;
  margin: 0 0 24px;
  padding: 0 40px;
}

.enhanced-stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  padding: 24px;
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  min-height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-align: center;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
}

.enhanced-stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
}

.enhanced-stat-card .card-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.5s ease;
  z-index: -1;
}

.enhanced-stat-card:hover .card-glow {
  opacity: 1;
}

.stat-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 16px;
}

.stat-icon-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.enhanced-stat-card:hover .stat-icon-wrapper {
  transform: scale(1.1);
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
}

.enhanced-stat-card .stat-icon-wrapper.primary-icon {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.3);
}

.enhanced-stat-card .stat-icon-wrapper.success-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
}

.enhanced-stat-card .stat-icon-wrapper.warning-icon {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(245, 158, 11, 0.3);
}

.stat-badge {
  font-size: 14px;
  color: #6b7280;
  font-weight: 600;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
}

.stat-body {
  margin-top: 16px;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
}

.stat-amount {
  font-size: 36px;
  font-weight: 800;
  color: #1a202c;
  margin-bottom: 4px;
  letter-spacing: -0.5px;
}

.stat-description {
  font-size: 14px;
  color: #6b7280;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e5e7eb;
  border-radius: 4px;
  margin-top: 12px;
  overflow: hidden;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
}

.progress-fill {
  height: 100%;
  border-radius: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: width 0.3s ease-in-out;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
}

.warning-fill {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.hover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 24px;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.enhanced-stat-card:hover .hover-overlay {
  opacity: 1;
}

/* 操作工具栏 */
.toolbar-section {
  position: relative;
  z-index: 10;
  margin: 0 0 24px;
  padding: 0 40px;
}

.toolbar-card {
  border-radius: 24px !important;
  border: none !important;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08) !important;
  overflow: hidden;
}

.toolbar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 700;
  color: #2d3748;
}

.toolbar-actions {
  display: flex;
  gap: 16px;
}

.filter-row {
  margin-top: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.search-actions {
  display: flex;
  gap: 16px;
}

.enhanced-input :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 1px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.2s ease !important;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.enhanced-input :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
}

.enhanced-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
}

.enhanced-btn {
  padding: 12px 24px !important;
  font-weight: 600 !important;
  letter-spacing: 0.5px !important;
  border-radius: 12px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.enhanced-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4) !important;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3) !important;
}

.search-btn:hover {
  transform: translateY(-1px) !important;
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4) !important;
}

.clear-btn {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 1px solid #e5e7eb !important;
  color: #6b7280 !important;
}

.clear-btn:hover {
  background: #f9fafb !important;
  border-color: #d1d5db !important;
}

/* 表格容器 */
.table-container {
  position: relative;
  z-index: 10;
  margin: 0 0 40px;
  padding: 0 40px;
}

.table-card {
  border-radius: 24px !important;
  border: none !important;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08) !important;
  overflow: hidden;
}

.table-card :deep(.el-card__header) {
  background: rgba(248, 250, 252, 0.8) !important;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06) !important;
  padding: 20px 24px !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 700;
  color: #2d3748;
}

.card-extra {
  display: flex;
  align-items: center;
}

/* 表格样式 */
.modern-table {
  border-radius: 0 !important;
  width: 100% !important;
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
  overflow-y: visible;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  background: white;
}

/* 自定义滚动条样式 */
.table-wrapper::-webkit-scrollbar {
  height: 8px;
}

.table-wrapper::-webkit-scrollbar-track {
  background: rgba(243, 244, 246, 0.8);
  border-radius: 4px;
}

.table-wrapper::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
  transition: all 0.3s ease;
}

.table-wrapper::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
}

/* Firefox 滚动条样式 */
.table-wrapper {
  scrollbar-width: thin;
  scrollbar-color: #667eea rgba(243, 244, 246, 0.8);
}

.modern-table :deep(.el-table__header) {
  background: #f8fafc;
}

.modern-table :deep(.el-table__row:hover > td) {
  background-color: rgba(102, 126, 234, 0.05) !important;
}

.modern-table :deep(.el-table__row) {
  transition: all 0.2s ease;
}

.modern-table :deep(.el-table th) {
  background: #f8fafc !important;
  color: #374151 !important;
  font-weight: 600 !important;
  border-bottom: none !important;
  padding: 16px 12px !important;
  text-align: left !important;
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  white-space: nowrap !important;
}

.modern-table :deep(.el-table td) {
  border-bottom: 1px solid #f1f5f9 !important;
  padding: 16px 12px !important;
  text-align: left !important;
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  vertical-align: middle !important;
}

.modern-table :deep(.el-table th .cell) {
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  white-space: nowrap !important;
  text-align: left !important;
}

.modern-table :deep(.el-table td .cell) {
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  text-align: left !important;
  line-height: 1.5 !important;
}

/* 表格内容样式 */
.tenant-name-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 0;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.tenant-name-card.enhanced {
  padding: 8px 6px;
  border-radius: 12px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.tenant-name-card.enhanced:hover {
  background: rgba(102, 126, 234, 0.05);
  transform: translateY(-1px);
}

.tenant-avatar {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.25);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.tenant-avatar.premium {
  width: 46px;
  height: 46px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.tenant-avatar:hover {
  transform: scale(1.05) rotate(5deg);
  box-shadow: 0 12px 28px rgba(102, 126, 234, 0.4);
}

.avatar-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  opacity: 0;
  animation: avatarGlow 3s ease-in-out infinite;
}

@keyframes avatarGlow {
  0%, 100% { opacity: 0; transform: rotate(0deg); }
  50% { opacity: 1; transform: rotate(180deg); }
}

.tenant-info {
  display: flex;
  flex-direction: column;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.tenant-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 2px;
}

.tenant-badge {
  width: 20px;
  height: 20px;
  border-radius: 6px;
  background: rgba(102, 126, 234, 0.1);
  border: 1px solid rgba(102, 126, 234, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #667eea;
  transition: all 0.3s ease;
}

.tenant-badge:hover {
  background: rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.4);
  transform: scale(1.1);
}

.tenant-name {
  font-size: 16px;
  font-weight: 700;
  color: #1a202c;
  line-height: 1.3;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
}

.tenant-id {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.room-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(139, 69, 194, 0.1) 100%);
  border: 1px solid rgba(99, 102, 241, 0.2);
  border-radius: 14px;
  color: #6366f1;
  font-weight: 600;
  font-size: 13px;
  width: fit-content;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.room-badge.premium {
  padding: 10px 16px;
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.15);
}

.room-badge:hover {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(139, 69, 194, 0.15) 100%);
  border-color: rgba(99, 102, 241, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.25);
}

.room-icon-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  opacity: 0;
  animation: iconGlow 2s ease-in-out infinite;
}

@keyframes iconGlow {
  0%, 100% { opacity: 0; transform: scale(0.8); }
  50% { opacity: 1; transform: scale(1.2); }
}

.room-text {
  font-weight: 700;
  letter-spacing: 0.3px;
}

.room-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: rgba(99, 102, 241, 0.6);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.room-badge.premium:hover .room-border {
  opacity: 1;
}

.phone-display {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 0;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.phone-display.premium {
  padding: 10px 8px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(16, 185, 129, 0.1);
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;
}

.phone-display.premium:hover {
  background: rgba(16, 185, 129, 0.05);
  border-color: rgba(16, 185, 129, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(16, 185, 129, 0.15);
}

.phone-icon {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(16, 185, 129, 0.15);
  border: 1px solid rgba(16, 185, 129, 0.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.phone-icon:hover {
  background: rgba(16, 185, 129, 0.2);
  border-color: rgba(16, 185, 129, 0.4);
  transform: scale(1.1) rotate(10deg);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
}

.icon-shine {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: rgba(255, 255, 255, 0.4);
  opacity: 0;
  animation: iconShine 4s ease-in-out infinite;
}

@keyframes iconShine {
  0%, 90%, 100% { 
    opacity: 0; 
    transform: translateX(-100%) translateY(-100%) rotate(45deg); 
  }
  5%, 15% { 
    opacity: 1; 
    transform: translateX(100%) translateY(100%) rotate(45deg); 
  }
}

.phone-content {
  display: flex;
  flex-direction: column;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.phone-number {
  font-size: 15px;
  font-weight: 700;
  color: #1a202c;
  line-height: 1.3;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  overflow: visible;
  text-overflow: clip;
  max-width: none;
  font-family: 'Courier New', monospace;
  color: #10b981;
  transition: all 0.3s ease;
}

.phone-display.premium:hover .phone-number {
  transform: scale(1.05);
  text-shadow: 0 4px 8px rgba(16, 185, 129, 0.3);
}

.phone-label {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.phone-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: rgba(16, 185, 129, 0.6);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.phone-display.premium:hover .phone-border {
  opacity: 1;
}

.id-card-display {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 0;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.id-card-icon {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(99, 102, 241, 0.1);
  border: 1px solid rgba(99, 102, 241, 0.2);
  transition: all 0.3s ease;
}

.id-card-icon:hover {
  background: rgba(99, 102, 241, 0.2);
  border-color: rgba(99, 102, 241, 0.3);
  transform: scale(1.1);
}

.id-card-number {
  font-family: 'Courier New', monospace;
  color: #4a5568;
  font-weight: 500;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  padding: 6px 12px;
  background: rgba(74, 85, 104, 0.05);
  border-radius: 10px;
  border: 1px solid rgba(74, 85, 104, 0.1);
  transition: all 0.3s ease;
  font-size: 13px;
  letter-spacing: 1px;
}

.id-card-number.premium {
  padding: 8px 14px;
  border-radius: 12px;
  background: rgba(99, 102, 241, 0.08);
  border-color: rgba(99, 102, 241, 0.15);
  color: #6366f1;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.08);
}

.id-card-number:hover {
  background: rgba(99, 102, 241, 0.12);
  border-color: rgba(99, 102, 241, 0.25);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.15);
}

.time-display {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #6b7280;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  padding: 4px 0;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 8px;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
}

.action-buttons.enhanced {
  gap: 12px;
  padding: 4px 0;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid transparent;
  position: relative;
  overflow: hidden;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  min-width: fit-content;
  flex: none;
}

.action-btn.premium {
  padding: 8px 14px;
  border-radius: 12px;
  font-weight: 700;
  gap: 6px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.action-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.3);
  transform: translateX(-100%);
  transition: transform 0.5s;
}

.action-btn:hover::before {
  transform: translateX(100%);
}

.btn-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  border-radius: 4px;
  transition: all 0.3s ease;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  flex-shrink: 0;
  position: relative;
}

.btn-icon.animated {
  width: 22px;
  height: 22px;
  border-radius: 6px;
}

.icon-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.2);
  opacity: 0;
  transition: all 0.3s ease;
}

.action-btn:hover .icon-bg {
  opacity: 1;
  transform: scale(1.1);
}

.btn-ripple {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%) scale(0);
  opacity: 0;
  transition: all 0.4s ease;
}

.action-btn:active .btn-ripple {
  transform: translate(-50%, -50%) scale(1.5);
  opacity: 1;
  transition: all 0.1s ease;
}

.btn-text {
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  flex-shrink: 0;
}

.edit-btn {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.2);
  color: #2563eb;
}

.edit-btn:hover {
  background: rgba(59, 130, 246, 0.15);
  border-color: rgba(59, 130, 246, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.25);
}

.edit-btn .btn-icon {
  background: rgba(59, 130, 246, 0.1);
}

.delete-btn {
  background: rgba(239, 68, 68, 0.1);
  border-color: rgba(239, 68, 68, 0.2);
  color: #dc2626;
}

.delete-btn:hover {
  background: rgba(239, 68, 68, 0.15);
  border-color: rgba(239, 68, 68, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25);
}

.delete-btn .btn-icon {
  background: rgba(239, 68, 68, 0.1);
}

/* 分页 */
.pagination-container {
  padding: 24px 0 0;
  display: flex;
  justify-content: center;
}

.modern-pagination :deep(.el-pager li) {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.2s ease;
}

.modern-pagination :deep(.el-pager li:hover) {
  background: rgba(102, 126, 234, 0.1);
}

.modern-pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 对话框样式 */
.modern-dialog :deep(.el-dialog) {
  border-radius: 24px !important;
  background: rgba(255, 255, 255, 0.98) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
}

.modern-dialog :deep(.el-dialog__header) {
  padding: 0 !important;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06) !important;
}

.dialog-header {
  padding: 24px 32px;
  background: rgba(248, 250, 252, 0.8);
}

.dialog-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: 700;
  color: #1a202c;
}

.dialog-content {
  padding: 32px;
}

.modern-form :deep(.el-form-item__label) {
  color: #374151 !important;
  font-weight: 600 !important;
  font-size: 14px !important;
  margin-bottom: 8px !important;
}

.form-input :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 1px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.2s ease !important;
}

.form-input :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
}

.form-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
}

.dialog-footer {
  padding: 24px 32px;
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  background: rgba(248, 250, 252, 0.5);
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.cancel-btn {
  border-radius: 12px !important;
  padding: 12px 24px !important;
  font-weight: 600 !important;
  background: rgba(255, 255, 255, 0.9) !important;
  border: 1px solid #e5e7eb !important;
  color: #6b7280 !important;
}

.cancel-btn:hover {
  background: #f9fafb !important;
  border-color: #d1d5db !important;
}

.confirm-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  border-radius: 12px !important;
  padding: 12px 24px !important;
  font-weight: 600 !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3) !important;
}

.confirm-btn:hover {
  transform: translateY(-1px) !important;
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4) !important;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .tenants-management {
    padding: 20px;
  }
  
  .stats-section,
  .operation-section,
  .table-container {
    padding-left: 20px;
    padding-right: 20px;
  }
}

@media (max-width: 768px) {
  .tenants-management {
    padding: 16px;
  }
  
  .page-header {
    padding: 24px 20px;
    margin-bottom: 24px;
  }
  
  .header-content {
    flex-direction: column;
    gap: 20px;
    align-items: flex-start;
  }

  .header-title h1 {
    font-size: 24px;
  }

  .header-subtitle {
    font-size: 12px;
  }

  .stats-section,
  .operation-section,
  .table-container {
    padding-left: 12px;
    padding-right: 12px;
  }

  .search-row {
    gap: 12px !important;
  }

  .search-actions {
    flex-direction: column;
    gap: 8px;
  }

  .table-card {
    border-radius: 16px !important;
  }

  .table-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .table-actions {
    width: 100%;
    justify-content: space-between;
  }

  .add-tenant-btn {
    padding: 6px 12px;
    font-size: 14px;
  }

  .dialog-content {
    padding: 24px 20px;
  }

  .dialog-footer {
    padding: 20px;
    flex-direction: column;
  }

  .cancel-btn,
  .confirm-btn {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .tenants-management {
    padding: 12px;
  }
  
  .page-header {
    padding: 20px 16px;
    margin-bottom: 20px;
  }

  .header-title h1 {
    font-size: 20px;
  }

  .header-subtitle {
    font-size: 10px;
  }

  .stats-section,
  .operation-section,
  .table-container {
    padding-left: 8px;
    padding-right: 8px;
  }

  .primary-btn,
  .secondary-btn {
    padding: 10px 16px !important;
    font-size: 14px !important;
  }

  .action-buttons {
    flex-direction: row;
    gap: 4px;
    justify-content: center;
  }

  .action-btn {
    padding: 3px 6px;
    font-size: 10px;
    gap: 2px;
  }

  .btn-icon {
    width: 14px;
    height: 14px;
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

.page-header,
.toolbar-section,
.stats-section,
.table-container {
  animation: slideInUp 0.6s ease-out;
}

.toolbar-section {
  animation-delay: 0.1s;
}

.stats-section {
  animation-delay: 0.2s;
}

.table-container {
  animation-delay: 0.3s;
}

/* Contracts.vue 风格的动画效果 */
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
.table-card {
  animation: fadeInUp 0.6s ease-out;
}

.stats-card:nth-child(1) { animation-delay: 0.1s; }
.stats-card:nth-child(2) { animation-delay: 0.2s; }
.stats-card:nth-child(3) { animation-delay: 0.3s; }
.stats-card:nth-child(4) { animation-delay: 0.4s; }

/* 增强的动画效果 */
@keyframes pulse {
  0%, 100% { 
    opacity: 1; 
    transform: scale(1);
  }
  50% { 
    opacity: 0.7; 
    transform: scale(1.05);
  }
}

/* 表格行悬停增强 */
.modern-table :deep(.el-table__row:hover > td) {
  background-color: rgba(102, 126, 234, 0.05) !important;
  box-shadow: inset 0 1px 0 rgba(102, 126, 234, 0.1) !important;
  transform: translateY(-1px);
  transition: all 0.3s ease;
}

/* 表格内容渐入动画 */
.tenant-name-card,
.room-badge,
.phone-display,
.id-card-display,
.action-buttons {
  animation: contentFadeIn 0.6s ease-out;
}

@keyframes contentFadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 悬停时的微妙发光效果 */
.tenant-name-card.enhanced:hover,
.room-badge.premium:hover,
.phone-display.premium:hover {
  box-shadow: 
    0 8px 20px rgba(102, 126, 234, 0.15),
    0 0 20px rgba(102, 126, 234, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

/* 身份证号码特殊悬停效果 */
.id-card-number.premium:hover {
  box-shadow: 
    0 4px 12px rgba(99, 102, 241, 0.15),
    0 0 15px rgba(99, 102, 241, 0.1);
  text-shadow: 0 1px 2px rgba(99, 102, 241, 0.2);
}

/* 统计卡片增强动画 */
.enhanced-stat-card:hover .stat-icon {
  animation: iconBounce 0.6s ease;
}

@keyframes iconBounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0) scale(1) rotate(0deg);
  }
  40% {
    transform: translateY(-8px) scale(1.1) rotate(5deg);
  }
  60% {
    transform: translateY(-4px) scale(1.05) rotate(-3deg);
  }
}
</style>