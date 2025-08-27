<template>
  <div class="rooms-management">
    <!-- 背景装饰 -->
    <div class="page-background">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
      <div class="bg-particles">
        <div class="particle" v-for="n in 15" :key="n" :style="getParticleStyle(n)"></div>
      </div>
    </div>

    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-title">
          <el-icon size="28" color="#667eea"><OfficeBuilding /></el-icon>
          <div class="title-content">
            <h1>{{ currentFloor }}楼房间管理</h1>
            <p class="header-subtitle">智能化房间信息管理系统</p>
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
            添加房间
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计概览卡片 -->
    <div class="stats-section">
      <el-row :gutter="24" class="stats-row">
        <el-col :span="6">
          <div class="stats-card primary-card enhanced-card" @click="filterByStatus('all')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ rooms?.length || 0 }}</div>
                <div class="stats-label">总房间数</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper primary-icon">
                  <el-icon class="stats-icon"><OfficeBuilding /></el-icon>
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
          <div class="stats-card success-card enhanced-card" @click="filterByStatus(2)">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ rooms?.filter(r => r.roomStatus === 2)?.length || 0 }}</div>
                <div class="stats-label">已出租</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper success-icon">
                  <el-icon class="stats-icon"><User /></el-icon>
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
          <div class="stats-card warning-card enhanced-card" @click="filterByStatus(1)">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ rooms?.filter(r => r.roomStatus === 1)?.length || 0 }}</div>
                <div class="stats-label">空闲房间</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper warning-icon">
                  <el-icon class="stats-icon"><Key /></el-icon>
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
          <div class="stats-card danger-card enhanced-card" @click="filterByStatus(3)">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ rooms?.filter(r => r.roomStatus === 3)?.length || 0 }}</div>
                <div class="stats-label">维修中</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper danger-icon">
                  <el-icon class="stats-icon"><Tools /></el-icon>
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
              <el-icon size="20"><Filter /></el-icon>
              筛选与搜索
            </div>
            <div class="operation-extra">
              <el-button 
                @click="clearAllFilters" 
                type="text" 
                class="clear-filters-btn"
                v-if="hasActiveFilters"
              >
                <el-icon><Refresh /></el-icon>
                清空筛选
              </el-button>
            </div>
          </div>
        </template>
        
        <!-- 桌面端搜索和筛选 -->
        <div class="desktop-filters-container">
          <el-select
            v-model="statusFilter"
            placeholder="房间状态"
            clearable
            size="large"
            class="status-filter enhanced-select"
            @change="handleStatusFilter"
          >
            <template #prefix>
              <el-icon><OfficeBuilding /></el-icon>
            </template>
            <el-option label="全部状态" value="" />
            <el-option label="空闲" value="1">
              <div class="status-option">
                <el-icon color="#22c55e"><CircleCheck /></el-icon>
                <span>空闲</span>
              </div>
            </el-option>
            <el-option label="已出租" value="2">
              <div class="status-option">
                <el-icon color="#f59e0b"><User /></el-icon>
                <span>已出租</span>
              </div>
            </el-option>
            <el-option label="维修中" value="3">
              <div class="status-option">
                <el-icon color="#ef4444"><Tools /></el-icon>
                <span>维修中</span>
              </div>
            </el-option>
            <el-option label="停用" value="4">
              <div class="status-option">
                <el-icon color="#6b7280"><Lock /></el-icon>
                <span>停用</span>
              </div>
            </el-option>
          </el-select>
          
          <el-select
            v-model="floorFilter"
            placeholder="楼层筛选"
            clearable
            size="large"
            class="floor-filter enhanced-select"
            @change="handleFloorFilter"
          >
            <template #prefix>
              <el-icon><OfficeBuilding /></el-icon>
            </template>
            <el-option label="一楼" :value="1" />
            <el-option label="二楼" :value="2" />
            <el-option label="三楼" :value="3" />
            <el-option label="四楼" :value="4" />
            <el-option label="五楼" :value="5" />
          </el-select>
          
          <el-input
            v-model="searchKeyword"
            placeholder="搜索房号..."
            clearable
            size="large"
            class="search-input enhanced-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <el-button 
            type="primary" 
            size="large" 
            @click="handleSearch"
            class="search-btn enhanced-btn"
          >
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
        </div>

        <!-- 移动端专用筛选布局 -->
        <div class="mobile-filters">
          <!-- 搜索框 -->
          <div class="mobile-filter-row">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索房号..."
              clearable
              size="large"
              class="search-input enhanced-input mobile-search"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
          
          <!-- 状态筛选 -->
          <div class="mobile-filter-row">
            <el-select
              v-model="statusFilter"
              placeholder="房间状态"
              clearable
              size="large"
              class="status-filter enhanced-select mobile-select"
              @change="handleStatusFilter"
            >
              <el-option label="全部状态" value="" />
              <el-option label="空闲" value="1">
                <div class="status-option">
                  <el-icon color="#22c55e"><CircleCheck /></el-icon>
                  <span>空闲</span>
                </div>
              </el-option>
              <el-option label="已出租" value="2">
                <div class="status-option">
                  <el-icon color="#f59e0b"><User /></el-icon>
                  <span>已出租</span>
                </div>
              </el-option>
              <el-option label="维修中" value="3">
                <div class="status-option">
                  <el-icon color="#ef4444"><Tools /></el-icon>
                  <span>维修中</span>
                </div>
              </el-option>
              <el-option label="停用" value="4">
                <div class="status-option">
                  <el-icon color="#6b7280"><Lock /></el-icon>
                  <span>停用</span>
                </div>
              </el-option>
            </el-select>
          </div>
          
          <!-- 排序筛选 -->
          <div class="mobile-filter-row">
            <el-select
              v-model="rentSort"
              placeholder="租金排序"
              clearable
              size="large"
              class="rent-sort enhanced-select mobile-select"
            >
              <el-option label="租金升序" value="asc" />
              <el-option label="租金降序" value="desc" />
            </el-select>
          </div>
          
          <!-- 搜索按钮 -->
          <div class="mobile-filter-row">
            <el-button 
              type="primary" 
              size="large" 
              @click="handleSearch"
              class="search-btn enhanced-btn mobile-search-btn"
            >
              <el-icon><Search /></el-icon>
              搜索房间
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 房间列表 -->
    <div class="table-container">
      <el-card class="table-card" shadow="never">
        <template #header>
          <div class="card-header">
            <div class="card-title">
              <el-icon size="20"><List /></el-icon>
              房间列表
            </div>
            <div class="card-extra">
              <el-tag type="info" size="small">共 {{ total }} 条记录</el-tag>
              <el-tag type="warning" size="small" class="scroll-tip" v-if="showScrollTip">
                <el-icon size="12"><ArrowRight /></el-icon>
                可左右滑动
              </el-tag>
            </div>
          </div>
        </template>

                  <div class="table-wrapper">
                    <el-table
          v-loading="loading"
          :data="rooms"
          class="modern-table"
          empty-text="暂无房间数据"
          :header-cell-style="{
            background: '#f8fafc',
            color: '#1a202c',
            fontWeight: '700',
            borderBottom: '2px solid #e2e8f0',
            fontSize: '15px',
            textAlign: 'left',
            writingMode: 'horizontal-tb',
            whiteSpace: 'nowrap',
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.05)',
            position: 'relative'
          }"
          :cell-style="{
            borderBottom: '1px solid #f1f5f9',
            padding: '18px 16px',
            textAlign: 'left',
            writingMode: 'horizontal-tb',
            verticalAlign: 'middle'
          }"
          :row-style="{
            transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
            cursor: 'pointer'
          }"
        >
              <el-table-column prop="roomNumber" label="房号" min-width="160" sortable>
                <template #default="{ row }">
                  <div class="room-number-card enhanced">
                    <div class="room-icon premium">
                      <el-icon size="20"><Key /></el-icon>
                      <div class="icon-glow"></div>
                    </div>
                    <div class="room-info">
                      <div class="room-header">
                        <span class="room-name">{{ row.roomNumber }}</span>
                        <div class="room-badge">
                          <el-icon size="10"><OfficeBuilding /></el-icon>
                        </div>
                      </div>
                      <span class="room-floor">{{ currentFloor }}楼</span>
                    </div>
                  </div>
                </template>
              </el-table-column>
          
              <el-table-column prop="roomType" label="房间类型" min-width="150">
                <template #default="{ row }">
                  <div class="room-type-badge">
                    <el-icon size="14"><OfficeBuilding /></el-icon>
                    <span>{{ row.roomType }}</span>
                  </div>
                </template>
              </el-table-column>
          
              <el-table-column prop="baseRent" label="基础租金" min-width="140" sortable>
                <template #default="{ row }">
                  <div class="price-card premium">
                    <div class="price-icon rent-icon">
                      <el-icon size="16" color="#f59e0b"><Money /></el-icon>
                      <div class="icon-shine"></div>
                    </div>
                    <div class="price-content">
                      <span class="price-amount highlight">¥{{ row.baseRent.toLocaleString() }}</span>
                      <span class="price-label">月租</span>
                    </div>
                    <div class="price-border"></div>
                  </div>
                </template>
              </el-table-column>
          
              <el-table-column prop="deposit" label="押金" min-width="140" sortable>
                <template #default="{ row }">
                  <div class="price-card premium">
                    <div class="price-icon deposit-icon">
                      <el-icon size="16" color="#10b981"><CreditCard /></el-icon>
                      <div class="icon-shine"></div>
                    </div>
                    <div class="price-content">
                      <span class="price-amount highlight">¥{{ row.deposit.toLocaleString() }}</span>
                      <span class="price-label">押金</span>
                    </div>
                    <div class="price-border"></div>
                  </div>
                </template>
              </el-table-column>
          
              <el-table-column prop="roomStatus" label="状态" min-width="160">
                <template #default="{ row }">
                  <div class="status-badge premium" :class="`status-${row.roomStatus}`">
                    <div class="status-indicator animated"></div>
                    <div class="status-content">
                      <div class="status-icon-wrapper">
                        <el-icon size="14">
                          <component :is="getStatusIcon(row.roomStatus)" />
                        </el-icon>
                        <div class="icon-ripple"></div>
                      </div>
                      <span class="status-text">{{ getStatusText(row.roomStatus) }}</span>
                    </div>
                    <div class="status-glow"></div>
                  </div>
                </template>
              </el-table-column>
          
              <el-table-column prop="waterMeterNumber" label="水表编号" min-width="100" show-overflow-tooltip>
                <template #default="{ row }">
                  <span class="meter-number">{{ row.waterMeterNumber || '-' }}</span>
                </template>
              </el-table-column>
              
              <el-table-column prop="electricityMeterNumber" label="电表编号" min-width="100" show-overflow-tooltip>
                <template #default="{ row }">
                  <span class="meter-number">{{ row.electricityMeterNumber || '-' }}</span>
                </template>
              </el-table-column>
          
              <el-table-column prop="updatedAt" label="更新时间" min-width="140" show-overflow-tooltip>
                <template #default="{ row }">
                  <div class="time-display">
                    <el-icon size="12" color="#9ca3af"><Clock /></el-icon>
                    <span>{{ formatDate(row.updatedAt) }}</span>
                  </div>
                </template>
              </el-table-column>
          
              <el-table-column label="操作" width="225" fixed="right">
                <template #default="{ row }">
                  <div class="action-buttons enhanced">
                    <div class="action-btn edit-btn premium" @click="editRoom(row)" title="编辑房间">
                      <div class="btn-icon animated">
                        <el-icon size="16"><Edit /></el-icon>
                        <div class="icon-bg"></div>
                      </div>
                      <span class="btn-text">编辑</span>
                      <div class="btn-ripple"></div>
                    </div>
                    <div 
                      class="action-btn delete-btn premium" 
                      @click="deleteRoom(row)"
                      :class="{ disabled: row.roomStatus === 2 }"
                      title="删除房间"
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
          </div>

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

    <!-- 添加/编辑房间对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingRoom ? '编辑房间信息' : '添加新房间'"
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
              <component :is="editingRoom ? 'Edit' : 'Plus'" />
            </el-icon>
            <span>{{ editingRoom ? '编辑房间信息' : '添加新房间' }}</span>
          </div>
        </div>
      </template>

      <div class="dialog-content">
        <el-form
          ref="roomFormRef"
          :model="roomForm"
          :rules="roomRules"
          label-width="100px"
          class="modern-form"
          label-position="top"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="房号" prop="roomNumber">
                <el-input 
                  v-model="roomForm.roomNumber" 
                  placeholder="请输入房号"
                  size="large"
                  class="form-input"
                >
                  <template #prefix>
                    <el-icon><Key /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="房间类型" prop="roomType">
                <el-select 
                  v-model="roomForm.roomType" 
                  placeholder="请选择房间类型" 
                  style="width: 100%;"
                  size="large"
                  class="form-select"
                >
                  <el-option label="单间" value="单间" />
                  <el-option label="一室一厅" value="一室一厅" />
                  <el-option label="两室一厅" value="两室一厅" />
                  <el-option label="套房" value="套房" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="基础租金" prop="baseRent">
                <el-input-number
                  v-model="roomForm.baseRent"
                  :min="0"
                  :precision="2"
                  placeholder="请输入基础租金"
                  style="width: 100%;"
                  size="large"
                  class="form-number"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="押金" prop="deposit">
                <el-input-number
                  v-model="roomForm.deposit"
                  :min="0"
                  :precision="2"
                  placeholder="请输入押金"
                  style="width: 100%;"
                  size="large"
                  class="form-number"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="房间状态" prop="roomStatus">
                <el-select 
                  v-model="roomForm.roomStatus" 
                  placeholder="请选择状态" 
                  style="width: 100%;"
                  size="large"
                  class="form-select"
                >
                  <el-option label="空闲" :value="1">
                    <div class="status-option">
                      <el-icon color="#67c23a"><CircleCheck /></el-icon>
                      <span>空闲</span>
                    </div>
                  </el-option>
                  <el-option label="已出租" :value="2">
                    <div class="status-option">
                      <el-icon color="#e6a23c"><User /></el-icon>
                      <span>已出租</span>
                    </div>
                  </el-option>
                  <el-option label="维修中" :value="3">
                    <div class="status-option">
                      <el-icon color="#f56c6c"><Tools /></el-icon>
                      <span>维修中</span>
                    </div>
                  </el-option>
                  <el-option label="停用" :value="4">
                    <div class="status-option">
                      <el-icon color="#909399"><Lock /></el-icon>
                      <span>停用</span>
                    </div>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <!-- 占位 -->
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="水表编号" prop="waterMeterNumber">
                <el-input 
                  v-model="roomForm.waterMeterNumber" 
                  placeholder="请输入水表编号"
                  size="large"
                  class="form-input"
                >
                  <template #prefix>
                    <el-icon><Memo /></el-icon>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电表编号" prop="electricityMeterNumber">
                <el-input 
                  v-model="roomForm.electricityMeterNumber" 
                  placeholder="请输入电表编号"
                  size="large"
                  class="form-input"
                >
                  <template #prefix>
                    <el-icon><Memo /></el-icon>
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
            @click="saveRoom" 
            :loading="saving"
            size="large"
            class="confirm-btn"
          >
            <el-icon v-if="!saving">
              <component :is="editingRoom ? 'Check' : 'Plus'" />
            </el-icon>
            {{ editingRoom ? '更新房间' : '添加房间' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox, ElForm } from 'element-plus'
import { 
  Plus, 
  Refresh, 
  Search, 
  Filter,
  OfficeBuilding,
  List,
  Key,
  Clock,
  Edit,
  Delete,
  CircleCheck,
  User,
  Tools,
  Lock,
  Memo,
  Check,
  Money,
  CreditCard,
  Warning,
  TrendCharts,
  ArrowLeft,
  ArrowRight
} from '@element-plus/icons-vue'
import { 
  getRooms, 
  searchRooms, 
  createRoom, 
  updateRoom, 
  deleteRoom as deleteRoomApi,
  type Room
} from '../api/rooms'

const route = useRoute()

// 当前楼层 - 添加默认值和验证
const currentFloor = computed(() => {
  const floor = route.params.floor as string
  console.log('路由楼层参数:', floor)
  // 如果没有楼层参数，默认为6楼
  return floor || '6'
})

// 表单引用
const roomFormRef = ref<InstanceType<typeof ElForm>>()

// 状态
const loading = ref(false)
const saving = ref(false)
const showAddDialog = ref(false)
const editingRoom = ref<Room | null>(null)

// 搜索和筛选
const searchKeyword = ref('')
const statusFilter = ref('')
const floorFilter = ref('')
const rentSort = ref('')

// 计算属性 - 是否有活动筛选
const hasActiveFilters = computed(() => {
  return !!(searchKeyword.value || statusFilter.value || floorFilter.value || rentSort.value)
})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 房间列表
const rooms = ref<Room[]>([])

// 显示滚动提示
const showScrollTip = ref(true)

// 房间表单
const roomForm = reactive({
  roomNumber: '',
  roomType: '单间',
  baseRent: 0,
  deposit: 0,
  roomStatus: 1,
  waterMeterNumber: '',
  electricityMeterNumber: ''
})

// 表单验证规则
const roomRules = {
  roomNumber: [
    { required: true, message: '请输入房号', trigger: 'blur' }
  ],
  roomType: [
    { required: true, message: '请选择房间类型', trigger: 'change' }
  ],
  baseRent: [
    { required: true, message: '请输入基础租金', trigger: 'blur' }
  ],
  deposit: [
    { required: true, message: '请输入押金', trigger: 'blur' }
  ],
  roomStatus: [
    { required: true, message: '请选择房间状态', trigger: 'change' }
  ]
}

// 获取状态标签类型
const getStatusTagType = (status: number) => {
  const typeMap: Record<number, string> = {
    1: 'success',  // 空闲
    2: 'warning',  // 已出租
    3: 'danger',   // 维修中
    4: 'info'      // 停用
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: number) => {
  const textMap: Record<number, string> = {
    1: '空闲',
    2: '已出租',
    3: '维修中',
    4: '停用'
  }
  return textMap[status] || '未知'
}

// 获取状态图标
const getStatusIcon = (status: number) => {
  const iconMap: Record<number, string> = {
    1: 'CircleCheck',
    2: 'User',
    3: 'Tools',
    4: 'Lock'
  }
  return iconMap[status] || 'CircleCheck'
}

// 生成粒子样式
const getParticleStyle = (index: number) => {
  const size = Math.random() * 4 + 2
  const left = Math.random() * 100
  const animationDelay = Math.random() * 20
  const animationDuration = Math.random() * 10 + 15
  
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${left}%`,
    animationDelay: `${animationDelay}s`,
    animationDuration: `${animationDuration}s`
  }
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

// 加载房间数据
const loadRooms = async () => {
  loading.value = true
  console.log('开始加载房间数据，楼层:', currentFloor.value)
  
  try {
    let response
    
    // 如果有搜索关键词，使用搜索API
    if (searchKeyword.value && searchKeyword.value.trim()) {
      console.log('使用搜索API，关键词:', searchKeyword.value.trim())
      response = await searchRooms(currentFloor.value, searchKeyword.value.trim())
      
      // 搜索API返回数组
      if (response && response.code === 200 && response.data) {
        const searchResults = Array.isArray(response.data) ? response.data : []
        rooms.value = searchResults
        total.value = searchResults.length
        console.log('✅ 搜索结果数量:', rooms.value.length)
      } else {
        rooms.value = []
        total.value = 0
        console.log('⚠️ 搜索失败:', response?.message || '未知错误')
      }
    } else {
      // 使用分页API
      const query: any = {
        page: currentPage.value - 1,
        size: pageSize.value
      }
      
      if (statusFilter.value) {
        query.status = Number(statusFilter.value)
      }
      
      console.log('使用分页API，查询参数:', query)
      console.log('API路径:', currentFloor.value === '6' ? '/api/rooms/new' : '/api/rooms/old')
      
      response = await getRooms(currentFloor.value, query)
      console.log('API响应:', response)
      
      if (response && response.code === 200 && response.data) {
        const pageData = response.data
        
        if (pageData.content && Array.isArray(pageData.content)) {
          // Spring Data JPA分页格式
          rooms.value = pageData.content
          total.value = pageData.totalElements || 0
          console.log('✅ 成功加载分页数据，数量:', rooms.value.length, '总数:', total.value)
        } else if (Array.isArray(pageData)) {
          // 直接数组格式
          rooms.value = pageData
          total.value = pageData.length
          console.log('✅ 成功加载数组数据，数量:', rooms.value.length)
        } else {
          console.warn('⚠️ 未知的数据格式:', pageData)
          rooms.value = []
          total.value = 0
        }
      } else {
        console.warn('⚠️ API响应为空或失败:', response?.message || '未知错误')
        rooms.value = []
        total.value = 0
      }
    }
    
  } catch (error: any) {
    console.error('❌ 加载房间数据失败:', error)
    ElMessage.error('加载房间数据失败: ' + (error.response?.data?.message || error.message))
    rooms.value = []
    total.value = 0
  } finally {
    loading.value = false
    console.log('房间数据加载完成，最终数量:', rooms.value.length)
  }
}

// 刷新数据
const refreshData = () => {
  loadRooms()
}

// 搜索
const handleSearch = () => {
  // 重置分页
  currentPage.value = 1
  loadRooms()
}

// 状态筛选
const handleStatusFilter = () => {
  currentPage.value = 1
  loadRooms()
}

// 楼层筛选
const handleFloorFilter = () => {
  currentPage.value = 1
  loadRooms()
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadRooms()
}

// 当前页改变
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadRooms()
}

// 编辑房间
const editRoom = (room: Room) => {
  editingRoom.value = room
  Object.assign(roomForm, room)
  showAddDialog.value = true
}

// 删除房间
const deleteRoom = async (room: Room) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除房间 ${room.roomNumber} 吗？`,
      '确认删除',
      {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用删除API
    await deleteRoomApi(currentFloor.value, room.id)
    
    ElMessage.success('删除成功')
    loadRooms()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除房间失败:', error)
      ElMessage.error('删除房间失败')
    }
  }
}

// 保存房间
const saveRoom = async () => {
  if (!roomFormRef.value) return
  
  const valid = await roomFormRef.value.validate().catch(() => false)
  if (!valid) return
  
  saving.value = true
  
  try {
    if (editingRoom.value) {
      // 更新房间
      await updateRoom(currentFloor.value, (editingRoom.value as Room).id, roomForm)
      ElMessage.success('更新成功')
    } else {
      // 创建房间
      await createRoom(currentFloor.value, roomForm)
      ElMessage.success('添加成功')
    }
    
    showAddDialog.value = false
    loadRooms()
  } catch (error) {
    console.error('保存房间失败:', error)
    ElMessage.error('保存房间失败')
  } finally {
    saving.value = false
  }
}

// 重置表单
const resetForm = () => {
  editingRoom.value = null
  Object.assign(roomForm, {
    roomNumber: '',
    roomType: '单间',
    baseRent: 0,
    deposit: 0,
    roomStatus: 1,
    waterMeterNumber: '',
    electricityMeterNumber: ''
  })
  if (roomFormRef.value) {
    roomFormRef.value.clearValidate()
  }
}

// 清空所有筛选条件
const clearAllFilters = () => {
  searchKeyword.value = ''
  statusFilter.value = ''
  floorFilter.value = ''
  currentPage.value = 1
  loadRooms()
}

// 统计卡片筛选方法
const filterByStatus = (status: string | number) => {
  if (status === 'all') {
    statusFilter.value = ''
  } else {
    statusFilter.value = String(status)
  }
  currentPage.value = 1
  loadRooms()
}



onMounted(() => {
  loadRooms()
})
</script>

<style scoped>
/* 主容器 */
.rooms-management {
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
  from { width: 0; }
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
}

/* 确保所有桌面端筛选器高度一致 */
.desktop-filters-container .search-input :deep(.el-input__wrapper),
.desktop-filters-container .status-filter :deep(.el-select__wrapper),
.desktop-filters-container .floor-filter :deep(.el-select__wrapper),
.desktop-filters-container .rent-sort :deep(.el-select__wrapper) {
  height: 48px !important;
  border-radius: 12px !important;
  display: flex !important;
  align-items: center !important;
}

.desktop-filters-container .search-btn {
  height: 48px !important;
  border-radius: 12px !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

/* 响应式筛选布局 */
.desktop-filters {
  display: block;
}

.desktop-filters-container {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
}

.desktop-filters-container .search-input {
  flex: 2;
  min-width: 200px;
}

.desktop-filters-container .status-filter,
.desktop-filters-container .floor-filter,
.desktop-filters-container .rent-sort {
  flex: 1;
  min-width: 150px;
}

.desktop-filters-container .search-btn {
  flex: 0 0 auto;
  min-width: 100px;
  padding: 0 24px;
}

.mobile-filters {
  display: none;
}

.mobile-filter-row {
  margin-bottom: 20px;
}

.mobile-filter-row:last-child {
  margin-bottom: 0;
}



.mobile-search,
.mobile-select {
  width: 100%;
}

.mobile-full-btn {
  width: 100%;
}

.mobile-search-btn {
  width: 100%;
  min-height: 48px;
  font-size: 16px;
  font-weight: 600;
  touch-action: manipulation;
  -webkit-tap-highlight-color: transparent;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 移动端筛选项动画 */
.mobile-filters {
  animation: mobileFiltersSlideIn 0.5s ease-out;
}

.mobile-filter-row {
  opacity: 0;
  animation: filterRowFadeIn 0.6s ease-out forwards;
}

.mobile-filter-row:nth-child(1) {
  animation-delay: 0.1s;
}

.mobile-filter-row:nth-child(2) {
  animation-delay: 0.15s;
}

.mobile-filter-row:nth-child(3) {
  animation-delay: 0.3s;
}

.mobile-filter-row:nth-child(4) {
  animation-delay: 0.45s;
}

@keyframes mobileFiltersSlideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes filterRowFadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 移动端下拉选项优化 */
@media (max-width: 768px) {
  .el-select-dropdown {
    border-radius: 12px !important;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15) !important;
  }
  
  .el-select-dropdown .el-select-dropdown__item {
    padding: 12px 16px !important;
    font-size: 16px !important;
    line-height: 1.5 !important;
    min-height: 44px !important;
  }
  
  .el-select-dropdown .el-select-dropdown__item:hover {
    background-color: rgba(102, 126, 234, 0.1) !important;
  }
  
  .status-option {
    padding: 4px 0;
    min-height: auto;
  }
}

/* 桌面端在中等屏幕的优化 */
@media (min-width: 769px) and (max-width: 1024px) {
  .desktop-filters-container {
    gap: 12px;
  }
  
  .desktop-filters-container .search-input {
    flex: 1.5;
    min-width: 180px;
  }
  
  .desktop-filters-container .status-filter,
  .desktop-filters-container .floor-filter,
  .desktop-filters-container .rent-sort {
    flex: 1;
    min-width: 120px;
  }
  
  .desktop-filters-container .search-btn {
    min-width: 80px;
    padding: 0 16px;
  }
}



.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  font-weight: 600;
}

.status-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-stats {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: nowrap;
}

/* 增强版统计卡片 */
.enhanced-stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
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
  min-width: 200px;
  flex-shrink: 0;
}

.enhanced-stat-card.premium-card {
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(255, 255, 255, 0.2);
}

.enhanced-stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 
    0 16px 48px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(102, 126, 234, 0.2);
}

.card-glow {
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

.hover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(102, 126, 234, 0.02);
  border-radius: 20px;
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.enhanced-stat-card:hover .hover-overlay {
  opacity: 1;
}

/* 保持原有的stat-card样式以防兼容性问题 */
.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  padding: 20px 24px;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.06);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.3));
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.stat-card:hover::before {
  transform: translateX(100%);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
}

/* 新的图标包装器样式 */
.stat-icon-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 18px;
  transition: all 0.3s ease;
  overflow: hidden;
}

.stat-icon-wrapper.primary-gradient {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.stat-icon-wrapper.warning-gradient {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(245, 158, 11, 0.3);
}

.stat-icon-wrapper.success-gradient {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
}

.enhanced-stat-card:hover .stat-icon-wrapper {
  transform: scale(1.1) rotate(5deg);
}

.icon-ripple {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%) scale(0);
  animation: iconRipple 2s ease-out infinite;
}

@keyframes iconRipple {
  0% { 
    transform: translate(-50%, -50%) scale(0);
    opacity: 1;
  }
  100% { 
    transform: translate(-50%, -50%) scale(2);
    opacity: 0;
  }
}

/* 保持原有图标样式 */
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: all 0.3s ease;
}

.total-rooms .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.rented-rooms .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(79, 172, 254, 0.3);
}

.available-rooms .stat-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(67, 233, 123, 0.3);
}

.stat-card:hover .stat-icon {
  transform: scale(1.1) rotate(5deg);
}

/* 新的主要内容区域 */
.stat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.stat-main .stat-number {
  font-size: 32px;
  font-weight: 800;
  color: #1a202c;
  margin-bottom: 4px;
  letter-spacing: -0.5px;
  line-height: 1;
}

.stat-main .stat-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 600;
  line-height: 1;
  white-space: nowrap;
}

/* 保持原有样式 */
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

/* 增强的趋势指示器 */
.stat-trend {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  position: relative;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.stat-trend.success {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.stat-trend.warning {
  background: rgba(245, 158, 11, 0.1);
  border: 1px solid rgba(245, 158, 11, 0.2);
}

.stat-trend.info {
  background: rgba(59, 130, 246, 0.1);
  border: 1px solid rgba(59, 130, 246, 0.2);
}

.trend-indicator {
  position: absolute;
  top: -2px;
  right: -2px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #10b981;
  animation: pulse 2s infinite;
}

.stat-trend.warning .trend-indicator {
  background: #f59e0b;
}

.stat-trend.info .trend-indicator {
  background: #3b82f6;
}

@keyframes pulse {
  0%, 100% { 
    opacity: 1; 
    transform: scale(1);
  }
  50% { 
    opacity: 0.7; 
    transform: scale(1.2);
  }
}

.trend-text {
  font-size: 12px;
  font-weight: 700;
  color: #059669;
}

.stat-trend.warning .trend-text {
  color: #d97706;
}

/* 操作工具栏 */
.toolbar-section {
  position: relative;
  z-index: 10;
  max-width: 1400px;
  margin: 0 auto 24px;
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

.toolbar-card.glass-card {
  background: rgba(255, 255, 255, 0.98) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
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

/* 保持原有工具栏样式 */
.toolbar {
  position: relative;
  z-index: 10;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 40px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
}

.toolbar-left {
  display: flex;
  gap: 16px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 增强按钮样式 */
.enhanced-btn {
  padding: 12px 24px !important;
  font-weight: 600 !important;
  letter-spacing: 0.5px !important;
  border-radius: 12px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  position: relative;
  overflow: hidden;
}

.enhanced-btn::before {
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

.enhanced-btn:hover::before {
  transform: translateX(100%);
}

.enhanced-btn:hover {
  transform: translateY(-2px) !important;
}

.enhanced-btn.primary-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3) !important;
}

.enhanced-btn.primary-btn:hover {
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4) !important;
}

.enhanced-btn.secondary-btn {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 1px solid rgba(102, 126, 234, 0.2) !important;
  color: #667eea !important;
  backdrop-filter: blur(10px) !important;
}

.enhanced-btn.secondary-btn:hover {
  background: rgba(102, 126, 234, 0.1) !important;
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15) !important;
}

.enhanced-btn.clear-btn {
  background: rgba(239, 68, 68, 0.1) !important;
  border: 1px solid rgba(239, 68, 68, 0.2) !important;
  color: #dc2626 !important;
  backdrop-filter: blur(10px) !important;
}

.enhanced-btn.clear-btn:hover {
  background: rgba(239, 68, 68, 0.15) !important;
  border-color: rgba(239, 68, 68, 0.4) !important;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25) !important;
}

/* 保持原有按钮样式 */
.primary-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  border-radius: 12px !important;
  padding: 12px 24px !important;
  font-weight: 600 !important;
  letter-spacing: 0.5px !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.primary-btn:hover {
  transform: translateY(-2px) !important;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4) !important;
}

.secondary-btn {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 1px solid rgba(102, 126, 234, 0.2) !important;
  color: #667eea !important;
  border-radius: 12px !important;
  padding: 12px 24px !important;
  font-weight: 600 !important;
  backdrop-filter: blur(10px) !important;
  transition: all 0.3s ease !important;
}

.secondary-btn:hover {
  background: rgba(102, 126, 234, 0.1) !important;
  border-color: rgba(102, 126, 234, 0.4) !important;
  transform: translateY(-1px) !important;
}

.clear-btn {
  background: rgba(239, 68, 68, 0.1) !important;
  border: 1px solid rgba(239, 68, 68, 0.2) !important;
  color: #dc2626 !important;
  border-radius: 12px !important;
  padding: 12px 24px !important;
  font-weight: 600 !important;
  backdrop-filter: blur(10px) !important;
  transition: all 0.3s ease !important;
}

.clear-btn:hover {
  background: rgba(239, 68, 68, 0.15) !important;
  border-color: rgba(239, 68, 68, 0.4) !important;
  transform: translateY(-1px) !important;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25) !important;
}

/* 增强的输入框和选择框样式 */
.search-container, .filter-container {
  position: relative;
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

.enhanced-select :deep(.el-select__wrapper) {
  border-radius: 12px !important;
  border: 1px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.2s ease !important;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.enhanced-select :deep(.el-select__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
}

.enhanced-select :deep(.el-select__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
}

.status-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 保持原有样式 */
.search-input {
  width: 100%;
  border-radius: 12px !important;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4);
  box-shadow: 0 6px 25px rgba(102, 126, 234, 0.15);
}

.floor-filter {
  width: 100%;
}

.floor-filter :deep(.el-select__wrapper) {
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.status-filter {
  width: 100%;
}

.status-filter :deep(.el-select__wrapper) {
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

/* 表格容器 */
.table-container {
  position: relative;
  z-index: 10;
  max-width: 100%;
  margin: 0 auto;
  padding: 0 40px 40px;
  overflow: hidden; /* 防止内容溢出 */
}

.table-card {
  border-radius: 24px !important;
  border: none !important;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08) !important;
  overflow: hidden;
  width: 100%;
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
  flex-wrap: wrap;
  gap: 12px;
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
  gap: 12px;
  flex-wrap: wrap;
}

.scroll-tip {
  display: flex;
  align-items: center;
  gap: 4px;
  background: linear-gradient(135deg, rgba(245, 158, 11, 0.1) 0%, rgba(217, 119, 6, 0.1) 100%) !important;
  border: 1px solid rgba(245, 158, 11, 0.3) !important;
  color: #d97706 !important;
  font-weight: 600;
  animation: pulseGlow 2s ease-in-out infinite;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

@keyframes pulseGlow {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.02);
  }
}

/* 表格样式 */
.modern-table {
  border-radius: 0 !important;
  width: 100% !important;
  table-layout: fixed !important; /* 使用固定布局防止溢出 */
}

.table-wrapper {
  width: 100%;
  overflow-x: auto;
  overflow-y: visible;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  background: white;
  /* 改善滚动体验 */
  -webkit-overflow-scrolling: touch;
  position: relative;
}

/* 表格内容容器 */
.table-wrapper .el-table {
  min-width: 1100px; /* 进一步减少最小宽度 */
}

/* 自定义滚动条样式 */
.table-wrapper::-webkit-scrollbar {
  height: 6px;
}

.table-wrapper::-webkit-scrollbar-track {
  background: rgba(243, 244, 246, 0.8);
  border-radius: 3px;
}

.table-wrapper::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 3px;
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
  background: rgba(102, 126, 234, 0.05) !important;
  box-shadow: inset 0 1px 0 rgba(102, 126, 234, 0.1) !important;
}

.modern-table :deep(.el-table__row) {
  transition: all 0.2s ease;
}

.modern-table :deep(.el-table th) {
  background: #f8fafc !important;
  color: #374151 !important;
  font-weight: 600 !important;
  border-bottom: none !important;
  padding: 16px 8px !important; /* 减少padding防止挤压 */
  text-align: left !important;
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

.modern-table :deep(.el-table td) {
  border-bottom: 1px solid #f1f5f9 !important;
  padding: 16px 8px !important; /* 减少padding防止挤压 */
  text-align: left !important;
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  vertical-align: middle !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

.modern-table :deep(.el-table th .cell) {
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  white-space: nowrap !important;
  text-align: left !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  padding: 0 !important;
  line-height: 1.4 !important;
}

.modern-table :deep(.el-table td .cell) {
  writing-mode: horizontal-tb !important;
  text-orientation: mixed !important;
  text-align: left !important;
  line-height: 1.5 !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
  padding: 0 !important;
}

/* 表格内容样式 */
.room-number-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 0;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.room-number-card.enhanced {
  padding: 8px 6px;
  border-radius: 12px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.room-number-card.enhanced:hover {
  background: rgba(102, 126, 234, 0.05);
  transform: translateY(-1px);
}

.room-icon {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: #667eea;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.25);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.room-icon.premium {
  width: 46px;
  height: 46px;
  background: #667eea;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.room-icon:hover {
  transform: scale(1.05) rotate(5deg);
  box-shadow: 0 12px 28px rgba(102, 126, 234, 0.4);
}

.icon-glow {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  opacity: 0;
  animation: iconGlow 3s ease-in-out infinite;
}

@keyframes iconGlow {
  0%, 100% { opacity: 0; transform: rotate(0deg); }
  50% { opacity: 1; transform: rotate(180deg); }
}

.room-info {
  display: flex;
  flex-direction: column;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.room-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 2px;
}

.room-badge {
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

.room-badge:hover {
  background: rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.4);
  transform: scale(1.1);
}

.room-name {
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

.room-floor {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.room-type-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  background: rgba(99, 102, 241, 0.1);
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
}

.room-type-badge:hover {
  background: rgba(99, 102, 241, 0.15);
  border-color: rgba(99, 102, 241, 0.3);
  transform: translateY(-1px);
}

.price-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 0;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.price-card.premium {
  padding: 10px 8px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(245, 158, 11, 0.1);
  position: relative;
  transition: all 0.3s ease;
  overflow: hidden;
}

.price-card.premium:hover {
  background: rgba(245, 158, 11, 0.05);
  border-color: rgba(245, 158, 11, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(245, 158, 11, 0.15);
}

.price-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: rgba(245, 158, 11, 0.6);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.price-card.premium:hover .price-border {
  opacity: 1;
}

.price-icon {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(245, 158, 11, 0.1);
  border: 1px solid rgba(245, 158, 11, 0.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.price-icon.rent-icon {
  background: rgba(245, 158, 11, 0.15);
  border-color: rgba(245, 158, 11, 0.3);
}

.price-icon.deposit-icon {
  background: rgba(16, 185, 129, 0.15);
  border-color: rgba(16, 185, 129, 0.3);
}

.price-icon:hover {
  background: rgba(245, 158, 11, 0.2);
  border-color: rgba(245, 158, 11, 0.4);
  transform: scale(1.1) rotate(10deg);
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.2);
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

.price-content {
  display: flex;
  flex-direction: column;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.price-amount {
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

.price-amount.highlight {
  color: #f59e0b;
  font-weight: 800;
  transition: all 0.3s ease;
}

.price-card.premium:hover .price-amount.highlight {
  transform: scale(1.05);
  text-shadow: 0 4px 8px rgba(245, 158, 11, 0.3);
}

.price-label {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border-radius: 12px;
  font-weight: 600;
  font-size: 12px;
  position: relative;
  overflow: visible;
  transition: all 0.3s ease;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  cursor: pointer;
  min-width: fit-content;
  width: auto;
  max-width: none;
  text-align: left;
}

.status-badge.premium {
  padding: 8px 12px;
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.status-badge:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.status-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.2);
  opacity: 0;
  animation: statusGlow 3s ease-in-out infinite;
}

@keyframes statusGlow {
  0%, 100% { opacity: 0; transform: translateX(-100%); }
  50% { opacity: 0.7; transform: translateX(100%); }
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  animation: pulse 2s infinite;
  flex-shrink: 0;
}

.status-indicator.animated {
  width: 10px;
  height: 10px;
  animation: statusPulse 2s ease-in-out infinite;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.3);
}

@keyframes statusPulse {
  0%, 100% { 
    opacity: 1; 
    transform: scale(1);
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.3);
  }
  50% { 
    opacity: 0.7; 
    transform: scale(1.2);
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.5);
  }
}

.status-icon-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-ripple {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  transform: translate(-50%, -50%) scale(0);
  animation: iconRipple 2s ease-out infinite;
}

@keyframes iconRipple {
  0% { 
    transform: translate(-50%, -50%) scale(0);
    opacity: 1;
  }
  100% { 
    transform: translate(-50%, -50%) scale(2);
    opacity: 0;
  }
}

.status-text {
  font-weight: 700;
  letter-spacing: 0.3px;
}

.status-content {
  display: flex;
  align-items: center;
  gap: 6px;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
}

.status-1 {
  background: rgba(34, 197, 94, 0.1);
  border: 1px solid rgba(34, 197, 94, 0.3);
  color: #15803d;
}

.status-1 .status-indicator {
  background: #22c55e;
}

.status-2 {
  background: rgba(245, 158, 11, 0.1);
  border: 1px solid rgba(245, 158, 11, 0.3);
  color: #d97706;
}

.status-2 .status-indicator {
  background: #f59e0b;
}

.status-3 {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  color: #dc2626;
}

.status-3 .status-indicator {
  background: #ef4444;
}

.status-4 {
  background: rgba(107, 114, 128, 0.1);
  border: 1px solid rgba(107, 114, 128, 0.3);
  color: #4b5563;
}

.status-4 .status-indicator {
  background: #6b7280;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

.meter-number {
  font-family: 'Courier New', monospace;
  color: #4a5568;
  font-weight: 500;
  writing-mode: horizontal-tb;
  text-orientation: mixed;
  white-space: nowrap;
  padding: 4px 8px;
  background: rgba(74, 85, 104, 0.05);
  border-radius: 8px;
  border: 1px solid rgba(74, 85, 104, 0.1);
  transition: all 0.3s ease;
}

.meter-number:hover {
  background: rgba(74, 85, 104, 0.1);
  border-color: rgba(74, 85, 104, 0.2);
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

.delete-btn:hover:not(.disabled) {
  background: rgba(239, 68, 68, 0.15);
  border-color: rgba(239, 68, 68, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25);
}

.delete-btn .btn-icon {
  background: rgba(239, 68, 68, 0.1);
}

.delete-btn.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none !important;
  box-shadow: none !important;
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

.form-input :deep(.el-input__wrapper),
.form-select :deep(.el-select__wrapper),
.form-number :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 1px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.2s ease !important;
}

.form-input :deep(.el-input__wrapper:hover),
.form-select :deep(.el-select__wrapper:hover),
.form-number :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
}

.form-input :deep(.el-input__wrapper.is-focus),
.form-select :deep(.el-select__wrapper.is-focus),
.form-number :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
}

.status-option {
  display: flex;
  align-items: center;
  gap: 8px;
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
@media (max-width: 1400px) {
  .table-wrapper .el-table {
    min-width: 1000px;
  }
}

@media (max-width: 1200px) {
  .header-content,
  .toolbar,
  .table-container {
    padding-left: 24px;
    padding-right: 24px;
  }

  .table-wrapper {
    /* 中等屏幕优化滚动条 */
    -webkit-overflow-scrolling: touch;
  }

  .table-wrapper .el-table {
    min-width: 950px;
  }

  /* 滚动提示样式 */
  .scroll-tip {
    animation: pulseGlow 2s ease-in-out infinite;
    background: linear-gradient(135deg, rgba(245, 158, 11, 0.1) 0%, rgba(217, 119, 6, 0.1) 100%) !important;
    border: 1px solid rgba(245, 158, 11, 0.3) !important;
    color: #d97706 !important;
  }
}

@media (max-width: 1024px) {
  .header-content,
  .toolbar,
  .table-container {
    padding-left: 20px;
    padding-right: 20px;
  }

  .table-wrapper .el-table {
    min-width: 900px;
  }

  .table-wrapper::-webkit-scrollbar {
    height: 6px;
  }

  .room-number-card,
  .price-card {
    gap: 8px;
  }

  .room-icon,
  .price-icon {
    width: 32px;
    height: 32px;
  }

  .room-icon.premium {
    width: 36px;
    height: 36px;
  }

  /* 优化表格内组件大小 */
  .room-name {
    font-size: 14px;
  }

  .price-amount {
    font-size: 14px;
  }

  .status-text {
    font-size: 12px;
  }
}

@media (max-width: 768px) {
  /* 切换到移动端布局 */
  .desktop-filters {
    display: none;
  }
  
  .desktop-filters-container {
    display: none;
  }
  
  .mobile-filters {
    display: block;
  }
  

  
  /* 操作区域移动端优化 */
  .operation-section {
    padding: 0 20px;
  }
  
  .operation-card {
    border-radius: 18px !important;
    padding: 24px 20px !important;
  }
  
  .operation-card :deep(.el-card__body) {
    padding: 24px 0 0 0 !important;
  }
  
  .operation-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
    padding: 0 0 20px 0;
  }
  
  .operation-title {
    font-size: 16px;
  }
  
  .clear-filters-btn {
    font-size: 12px;
    padding: 4px 8px !important;
  }
  
  /* 移动端输入框和选择器优化 */
  .mobile-search :deep(.el-input__wrapper),
  .mobile-select :deep(.el-select__wrapper) {
    border-radius: 14px !important;
    min-height: 48px !important;
    padding: 0 16px !important;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08) !important;
  }
  
  .mobile-search :deep(.el-input__inner),
  .mobile-select :deep(.el-select__input) {
    font-size: 16px !important; /* 防止iOS自动缩放 */
    line-height: 1.5 !important;
  }
  
  .mobile-search-btn {
    border-radius: 14px !important;
    min-height: 52px !important;
    padding: 14px 24px !important;
    font-size: 16px !important;
    font-weight: 600 !important;
    touch-action: manipulation;
    -webkit-tap-highlight-color: transparent;
    box-shadow: 0 4px 16px rgba(102, 126, 234, 0.3) !important;
  }
  
  .mobile-search-btn:active {
    transform: translateY(1px);
    box-shadow: 0 2px 8px rgba(102, 126, 234, 0.4) !important;
  }

  .header-content {
    flex-direction: column;
    gap: 20px;
    align-items: flex-start;
    padding: 24px 20px;
  }

  .header-stats {
    align-self: stretch;
    justify-content: space-around;
  }

  .header-title h1 {
    font-size: 24px;
  }

  .card-extra {
    flex-direction: column;
    gap: 8px;
    align-items: flex-end;
  }

  .scroll-tip {
    font-size: 11px;
    padding: 2px 6px;
  }

  .table-container {
    padding: 0 20px 20px;
  }

  .table-card {
    border-radius: 16px !important;
  }

  .table-wrapper {
    border-radius: 12px;
    /* 移动端滚动条样式 */
    -webkit-overflow-scrolling: touch;
  }

  .table-wrapper::-webkit-scrollbar {
    height: 4px;
  }

  .table-wrapper .el-table {
    min-width: 800px;
  }

  .modern-table {
    font-size: 13px !important;
  }

  .modern-table :deep(.el-table th) {
    padding: 10px 6px !important;
    font-size: 12px !important;
  }

  .modern-table :deep(.el-table td) {
    padding: 12px 6px !important;
  }

  /* 移动端隐藏部分列 */
  .modern-table :deep(.el-table__column):nth-child(7),
  .modern-table :deep(.el-table__column):nth-child(8) {
    display: none;
  }

  .room-number-card {
    gap: 8px;
  }

  .room-icon {
    width: 36px;
    height: 36px;
  }

  .room-name {
    font-size: 14px;
  }

  .price-card {
    gap: 8px;
  }

  .price-icon {
    width: 32px;
    height: 32px;
  }

  .price-amount {
    font-size: 14px;
  }

  .status-badge {
    padding: 4px 8px;
    font-size: 11px;
  }

  .action-btn {
    padding: 4px 8px;
    font-size: 11px;
  }

  .btn-icon {
    width: 16px;
    height: 16px;
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
  /* 小屏手机筛选器优化 */
  .operation-section {
    padding: 0 16px;
  }
  
  .operation-card {
    border-radius: 16px !important;
    padding: 20px 16px !important;
  }
  
  .operation-card :deep(.el-card__body) {
    padding: 20px 0 0 0 !important;
  }
  
  .operation-header {
    padding: 0 0 16px 0;
    gap: 12px;
  }
  
  .operation-title {
    font-size: 14px;
  }
  

  
  .mobile-filter-row {
    margin-bottom: 16px;
  }
  
  .mobile-search :deep(.el-input__wrapper),
  .mobile-select :deep(.el-select__wrapper) {
    min-height: 44px !important;
    border-radius: 12px !important;
    padding: 0 14px !important;
  }
  
  .mobile-search-btn {
    min-height: 48px !important;
    font-size: 15px !important;
    border-radius: 12px !important;
    padding: 12px 20px !important;
  }
  
  .status-option {
    padding: 6px 0;
  }
  
  .status-option span {
    font-size: 14px;
  }

  .header-content {
    padding: 20px 16px;
  }

  .header-title h1 {
    font-size: 20px;
  }

  .stat-number {
    font-size: 18px;
  }

  .table-container {
    padding: 0 16px 16px;
  }

  .table-wrapper {
    border-radius: 8px;
    -webkit-overflow-scrolling: touch;
  }

  .table-wrapper::-webkit-scrollbar {
    height: 3px;
  }

  .table-wrapper .el-table {
    min-width: 700px;
  }

  .table-wrapper::-webkit-scrollbar-thumb {
    background: rgba(102, 126, 234, 0.6);
  }

  .modern-table {
    font-size: 12px !important;
  }

  .modern-table :deep(.el-table th) {
    padding: 8px 4px !important;
    font-size: 11px !important;
  }

  .modern-table :deep(.el-table td) {
    padding: 10px 4px !important;
  }

  .room-number-card {
    gap: 4px;
    padding: 4px 0;
  }

  .room-icon {
    width: 28px;
    height: 28px;
  }

  .room-name {
    font-size: 11px;
  }

  .room-floor {
    font-size: 9px;
  }

  .room-type-badge {
    padding: 3px 6px;
    font-size: 9px;
  }

  .price-card {
    gap: 3px;
  }

  .price-icon {
    width: 22px;
    height: 22px;
  }

  .price-amount {
    font-size: 10px;
  }

  .price-label {
    font-size: 8px;
  }

  .status-badge {
    padding: 2px 4px;
    font-size: 8px;
    gap: 2px;
  }

  .meter-number {
    font-size: 9px;
    padding: 2px 4px;
  }

  .time-display {
    font-size: 9px;
  }

  .action-buttons {
    flex-direction: row;
    gap: 2px;
    justify-content: center;
  }

  .action-btn {
    padding: 2px 4px;
    font-size: 8px;
    gap: 1px;
  }

  .btn-icon {
    width: 10px;
    height: 10px;
  }

  .primary-btn,
  .secondary-btn {
    padding: 8px 12px !important;
    font-size: 12px !important;
  }

  /* 移动端完全隐藏水表电表列和更新时间列 */
  .modern-table :deep(.el-table__column):nth-child(7),
  .modern-table :deep(.el-table__column):nth-child(8),
  .modern-table :deep(.el-table__column):nth-child(9) {
    display: none !important;
  }
}



/* 增强动画效果 */
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

@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes shimmer {
  0% {
    background-position: -1000px 0;
  }
  100% {
    background-position: 1000px 0;
  }
}

.page-header,
.toolbar-section,
.table-container {
  animation: slideInUp 0.6s ease-out;
}

.toolbar-section {
  animation-delay: 0.1s;
}

.table-container {
  animation-delay: 0.2s;
}

.enhanced-stat-card {
  animation: fadeInScale 0.5s ease-out;
}

.enhanced-stat-card:nth-child(1) {
  animation-delay: 0.1s;
}

.enhanced-stat-card:nth-child(2) {
  animation-delay: 0.2s;
}

.enhanced-stat-card:nth-child(3) {
  animation-delay: 0.3s;
}

/* 加载状态动画 */
.loading-shimmer {
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 1000px 100%;
  animation: shimmer 2s infinite;
}

/* 表格行悬停增强 */
.modern-table :deep(.el-table__row:hover > td) {
  background: rgba(102, 126, 234, 0.05) !important;
  box-shadow: inset 0 1px 0 rgba(102, 126, 234, 0.1) !important;
  transform: translateY(-1px);
  transition: all 0.3s ease;
}

/* 表格内容渐入动画 */
.room-number-card,
.room-type-badge,
.price-card,
.status-badge,
.action-buttons {
  animation: fadeInScale 0.4s ease-out;
}

/* 悬停时的微妙发光效果 */
.room-number-card.enhanced:hover,
.price-card.premium:hover {
  box-shadow: 
    0 8px 20px rgba(102, 126, 234, 0.15),
    0 0 20px rgba(102, 126, 234, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

/* 状态徽章增强动画 */
.status-badge.premium:hover {
  animation: statusBounce 0.6s ease;
}

@keyframes statusBounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0) scale(1);
  }
  40% {
    transform: translateY(-4px) scale(1.05);
  }
  60% {
    transform: translateY(-2px) scale(1.02);
  }
}

/* Contracts.vue 风格的整体动画 */
.page-header,
.stats-section,
.operation-section,
.table-container {
  animation: slideInUp 0.6s ease-out;
}

.stats-section {
  animation-delay: 0.1s;
}

.operation-section {
  animation-delay: 0.2s;
}

.table-container {
  animation-delay: 0.3s;
}
</style>