<template>
  <div class="rent-info-management">
    <!-- 背景装饰 -->
    <div class="page-background">
      <div class="bg-gradient-1"></div>
      <div class="bg-gradient-2"></div>
      <div class="bg-shape bg-shape-1"></div>
      <div class="bg-shape bg-shape-2"></div>
      <div class="bg-shape bg-shape-3"></div>
    </div>

    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-title">
          <el-icon size="28" color="#667eea"><House /></el-icon>
          <div class="title-content">
            <h1>{{ currentFloor }}楼租房信息</h1>
            <p class="header-subtitle">查看{{ currentFloor }}楼所有房间的租赁详细信息</p>
          </div>
        </div>
        <div class="header-stats">
          <div class="stat-item">
            <el-icon><Calendar /></el-icon>
            <span>{{ currentDate }}</span>
          </div>
          <div class="stat-item">
            <el-icon><Clock /></el-icon>
            <span>{{ currentTime }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计概览卡片 -->
    <div class="stats-section">
      <el-row :gutter="24" class="stats-row">
        <el-col :span="6">
          <div class="stats-card total-card enhanced-card">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ rentalInfoList.length }}</div>
                <div class="stats-label">总租房数</div>
              </div>
              <div class="stats-right">
                <el-icon size="32" color="#667eea"><House /></el-icon>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card paid-card enhanced-card">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ paidCount }}</div>
                <div class="stats-label">已缴费</div>
              </div>
              <div class="stats-right">
                <el-icon size="32" color="#52c41a"><CircleCheck /></el-icon>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card unpaid-card enhanced-card">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ unpaidCount }}</div>
                <div class="stats-label">未缴费</div>
              </div>
              <div class="stats-right">
                <el-icon size="32" color="#ff4d4f"><CircleClose /></el-icon>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card deposit-card enhanced-card">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ totalDeposit.toFixed(2) }}</div>
                <div class="stats-label">总押金</div>
              </div>
              <div class="stats-right">
                <el-icon size="32" color="#faad14"><Money /></el-icon>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 筛选与搜索 -->
    <div class="filter-search-section">
      <div class="section-header">
        <h3 class="section-title">
          <el-icon><Search /></el-icon>
          筛选与搜索
        </h3>
      </div>
      
      <div class="search-filter-content">
        <div class="search-row">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索姓名、房号或电话..."
            size="large"
            class="search-input"
            @input="handleSearch"
            clearable
          >
            <template #suffix>
              <el-button type="primary" class="search-btn">
                搜索
              </el-button>
            </template>
          </el-input>
        </div>
        
        <div class="filter-actions">
          <div class="filter-buttons-wrapper">
            <el-radio-group v-model="filterStatus" @change="handleFilter" class="filter-buttons">
              <el-radio-button value="all">全部</el-radio-button>
              <el-radio-button value="paid">已缴费</el-radio-button>
              <el-radio-button value="unpaid">未缴费</el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="action-buttons">
            <el-dropdown @command="handleExport" class="export-dropdown">
              <el-button type="info" size="default" class="action-btn">
                <el-icon><Download /></el-icon>
                导出信息
                <el-icon class="dropdown-arrow"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="all">
                    <el-icon color="#52c41a"><Select /></el-icon>
                    导出所有信息
                  </el-dropdown-item>
                  <el-dropdown-item command="paid">
                    <el-icon color="#52c41a"><CircleCheck /></el-icon>
                    导出已缴费信息
                  </el-dropdown-item>
                  <el-dropdown-item command="unpaid">
                    <el-icon color="#ff4d4f"><CircleClose /></el-icon>
                    导出未缴费信息
                  </el-dropdown-item>
                  <el-dropdown-item divided command="summary">
                    <el-icon color="#1890ff"><Document /></el-icon>
                    租房信息汇总
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button 
              type="primary" 
              size="default" 
              @click="showAddDialog = true"
              class="primary-btn"
            >
              <el-icon><Plus /></el-icon>
              添加租房信息
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 租房信息表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="filteredRentalInfoList"
        stripe
        style="width: 100%"
        class="rental-info-table"
        empty-text="暂无租房信息"
        row-class-name="rental-info-row"
      >
        <el-table-column prop="roomNumber" label="房号" min-width="100">
          <template #default="{ row }">
            <div class="room-badge">
              <el-icon><House /></el-icon>
              {{ row.roomNumber }}
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="tenantName" label="租客姓名" min-width="120">
          <template #default="{ row }">
            <div class="tenant-info">
              <el-icon><User /></el-icon>
              <span class="tenant-name">{{ row.tenantName }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="phone" label="电话" min-width="130">
          <template #default="{ row }">
            <div class="phone-info">
              <el-icon><Phone /></el-icon>
              <span>{{ row.phone }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="deposit" label="押金" min-width="100">
          <template #default="{ row }">
            <div class="money-amount">
              <el-icon><Money /></el-icon>
              <span>¥{{ row.deposit.toFixed(2) }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="occupantCount" label="入住人数" min-width="100">
          <template #default="{ row }">
            <div class="occupant-info">
              <el-icon><User /></el-icon>
              <span>{{ row.occupantCount }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="checkInDate" label="入住时间" min-width="120">
          <template #default="{ row }">
            <div class="date-info">
              <el-icon><Calendar /></el-icon>
              <span>{{ row.checkInDate || '未入住' }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="rentalStatus" label="缴费状态" min-width="100">
          <template #default="{ row }">
            <el-tag 
              :type="row.rentalStatus === 1 ? 'success' : 'danger'"
              class="payment-status"
            >
              <el-icon>
                <CircleCheck v-if="row.rentalStatus === 1" />
                <CircleClose v-else />
              </el-icon>
              {{ row.rentalStatus === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="remarks" label="备注" min-width="120">
          <template #default="{ row }">
            <span class="remarks-text" :title="row.remarks">
              {{ row.remarks || '无' }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" min-width="180" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button
                type="primary"
                size="small"
                @click="viewRentalInfo(row)"
                class="action-btn"
              >
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button
                type="warning"
                size="small"
                @click="editRentalInfo(row)"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="deleteRentalInfo(row)"
                class="action-btn"
              >
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页器 -->
    <div class="pagination-section">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalElements"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </div>

    <!-- 添加/编辑租房信息对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="dialogTitle"
      width="800px"
      class="rental-info-dialog"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
        class="rental-info-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房号" prop="roomNumber">
              <el-select
                v-model="formData.roomNumber"
                placeholder="请选择空闲房间"
                style="width: 100%"
                :disabled="isEditing"
              >
                <el-option
                  v-for="room in availableRooms"
                  :key="room.roomNumber"
                  :label="`${room.roomNumber} - ${room.roomType} (基础租金: ¥${room.baseRent})`"
                  :value="room.roomNumber"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="租客姓名" prop="tenantName">
              <el-input
                v-model="formData.tenantName"
                placeholder="请输入租客姓名"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="formData.phone"
                placeholder="请输入手机号码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="押金金额" prop="deposit">
              <el-input-number
                v-model="formData.deposit"
                :min="0"
                :step="100"
                style="width: 100%"
                placeholder="200.00"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入住人数" prop="occupantCount">
              <el-input-number
                v-model="formData.occupantCount"
                :min="1"
                :max="10"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入住时间" prop="checkInDate">
              <el-date-picker
                v-model="formData.checkInDate"
                type="date"
                placeholder="选择入住时间"
                style="width: 100%"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="缴费状态" prop="rentalStatus">
              <el-select
                v-model="formData.rentalStatus"
                placeholder="选择缴费状态"
                style="width: 100%"
              >
                <el-option label="已缴费" :value="1" />
                <el-option label="未缴费" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注信息" prop="remarks">
          <el-input
            v-model="formData.remarks"
            type="textarea"
            :rows="4"
            placeholder="请输入备注信息（选填）"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="saveRentalInfo" :loading="saveLoading">
            {{ isEditing ? '更新' : '保存' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="租房信息详情"
      width="600px"
      class="view-dialog"
    >
      <div v-if="viewData" class="detail-content">
        <div class="detail-header">
          <div class="detail-icon">
            <el-icon size="48" color="#667eea"><House /></el-icon>
          </div>
          <div class="detail-title">
            <h3>房号 {{ viewData.roomNumber }}</h3>
            <el-tag 
              :type="viewData.rentalStatus === 1 ? 'success' : 'danger'"
              size="large"
            >
              {{ viewData.rentalStatus === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </div>
        </div>
        
        <div class="detail-cards">
          <div class="detail-card">
            <div class="card-header">
              <el-icon><User /></el-icon>
              <span>租客信息</span>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="label">租客姓名:</span>
                <span class="value">{{ viewData.tenantName }}</span>
              </div>
              <div class="info-row">
                <span class="label">联系电话:</span>
                <span class="value">{{ viewData.phone }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-card">
            <div class="card-header">
              <el-icon><Money /></el-icon>
              <span>财务信息</span>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="label">押金金额:</span>
                <span class="value money">¥{{ viewData.deposit.toFixed(2) }}</span>
              </div>
              <div class="info-row">
                <span class="label">入住时间:</span>
                <span class="value">{{ viewData.checkInDate || '未入住' }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-card">
            <div class="card-header">
              <el-icon><User /></el-icon>
              <span>入住信息</span>
            </div>
            <div class="card-body">
              <div class="info-row">
                <span class="label">入住人数:</span>
                <span class="value">{{ viewData.occupantCount }} 人</span>
              </div>
              <div class="info-row">
                <span class="label">创建时间:</span>
                <span class="value">{{ viewData.createdAt }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-card full-width">
            <div class="card-header">
              <el-icon><Document /></el-icon>
              <span>备注信息</span>
            </div>
            <div class="card-body">
              <div class="remarks-content">
                <p>{{ viewData.remarks || '暂无备注信息' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
          <el-button type="primary" @click="editFromView">
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  House, User, Phone, Money, Calendar, Clock, Search, Download, Plus,
  CircleCheck, CircleClose, List, ArrowDown, View, Edit, Delete, Select,
  Document
} from '@element-plus/icons-vue'
import {
  getRentalInfoList,
  getRentalInfoById,
  createRentalInfo,
  updateRentalInfo,
  deleteRentalInfo as deleteRentalInfoApi,
  getAvailableRooms,
  type RentalInfo,
  type RentalInfoQuery
} from '../api/rentalInfo'

// 路由
const route = useRoute()

// 响应式数据
const loading = ref(false)
const saveLoading = ref(false)
const currentFloor = computed(() => route.params.floor as string || '6')
const searchKeyword = ref('')
const filterStatus = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const totalElements = ref(0)

// 租房信息列表
const rentalInfoList = ref<RentalInfo[]>([])
const availableRooms = ref<any[]>([])

// 对话框状态
const showAddDialog = ref(false)
const showViewDialog = ref(false)
const isEditing = ref(false)
const viewData = ref<RentalInfo | null>(null)

// 表单数据和验证
const formRef = ref<FormInstance>()
const formData = reactive<Partial<RentalInfo>>({
  roomNumber: '',
  tenantName: '',
  phone: '',
  deposit: 200,
  occupantCount: 1,
  checkInDate: '',
  rentalStatus: 1,
  remarks: ''
})

const formRules: FormRules = {
  roomNumber: [{ required: true, message: '请选择房号', trigger: 'change' }],
  tenantName: [{ required: true, message: '请输入租客姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  deposit: [{ required: true, message: '请输入押金金额', trigger: 'blur' }],
  occupantCount: [{ required: true, message: '请输入入住人数', trigger: 'blur' }],
  checkInDate: [{ required: true, message: '请选择入住时间', trigger: 'change' }],
  rentalStatus: [{ required: true, message: '请选择缴费状态', trigger: 'change' }]
}

// 计算属性
const currentDate = computed(() => {
  return new Date().toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
})

const currentTime = ref('')
const updateCurrentTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
}

const paidCount = computed(() => {
  return rentalInfoList.value.filter(item => item.rentalStatus === 1).length
})

const unpaidCount = computed(() => {
  return rentalInfoList.value.filter(item => item.rentalStatus === 2).length
})

const totalDeposit = computed(() => {
  return rentalInfoList.value.reduce((sum, item) => sum + item.deposit, 0)
})

const dialogTitle = computed(() => {
  return isEditing.value ? '编辑租房信息' : '添加租房信息'
})

const filteredRentalInfoList = computed(() => {
  let list = rentalInfoList.value

  // 状态筛选
  if (filterStatus.value === 'paid') {
    list = list.filter(item => item.rentalStatus === 1)
  } else if (filterStatus.value === 'unpaid') {
    list = list.filter(item => item.rentalStatus === 2)
  }

  // 搜索筛选
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    list = list.filter(item =>
      item.roomNumber.toLowerCase().includes(keyword) ||
      item.tenantName.toLowerCase().includes(keyword) ||
      item.phone.includes(keyword)
    )
  }

  return list
})

// 方法
const loadRentalInfoList = async () => {
  try {
    loading.value = true
    const query: RentalInfoQuery = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchKeyword.value || undefined,
      rentalStatus: filterStatus.value === 'all' ? undefined : 
                   filterStatus.value === 'paid' ? 1 : 2
    }
    
    const response = await getRentalInfoList(currentFloor.value, query) as any
    if (response && response.code === 200) {
      rentalInfoList.value = response.data.content
      totalElements.value = response.data.totalElements
    } else {
      ElMessage.error(response?.message || '加载失败')
    }
  } catch (error) {
    console.error('加载租房信息失败:', error)
    ElMessage.error('加载租房信息失败')
  } finally {
    loading.value = false
  }
}

const loadAvailableRooms = async () => {
  try {
    const response = await getAvailableRooms(currentFloor.value) as any
    if (response && response.code === 200) {
      availableRooms.value = response.data
    }
  } catch (error) {
    console.error('加载可用房间失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadRentalInfoList()
}

const handleFilter = () => {
  currentPage.value = 1
  loadRentalInfoList()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadRentalInfoList()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadRentalInfoList()
}

const resetForm = () => {
  Object.assign(formData, {
    roomNumber: '',
    tenantName: '',
    phone: '',
    deposit: 200,
    occupantCount: 1,
    checkInDate: '',
    rentalStatus: 1,
    remarks: ''
  })
  formRef.value?.clearValidate()
}

const saveRentalInfo = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        saveLoading.value = true
        let result
        
        if (isEditing.value && formData.id) {
          result = await updateRentalInfo(currentFloor.value, formData.id, formData)
        } else {
          result = await createRentalInfo(currentFloor.value, formData)
        }
        
        if (result && result.code === 200) {
          ElMessage.success(result.message)
          showAddDialog.value = false
          loadRentalInfoList()
          resetForm()
        } else {
          ElMessage.error(result?.message || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      } finally {
        saveLoading.value = false
      }
    }
  })
}

const viewRentalInfo = (row: RentalInfo) => {
  viewData.value = row
  showViewDialog.value = true
}

const editRentalInfo = (row: RentalInfo) => {
  isEditing.value = true
  Object.assign(formData, row)
  showAddDialog.value = true
}

const editFromView = () => {
  if (viewData.value) {
    showViewDialog.value = false
    editRentalInfo(viewData.value)
  }
}

const deleteRentalInfo = async (row: RentalInfo) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条租房信息记录吗？此操作不可恢复。',
      '确认删除',
      {
        confirmButtonText: '确认删除',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
    
    const response = await deleteRentalInfoApi(currentFloor.value, row.id) as any
    if (response && response.code === 200) {
      ElMessage.success(response.message)
      loadRentalInfoList()
    } else {
      ElMessage.error(response?.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleExport = (command: string) => {
  ElMessage.info(`导出${command}功能尚未实现`)
}

// 监听对话框关闭
watch(showAddDialog, (newVal) => {
  if (!newVal) {
    isEditing.value = false
    resetForm()
  }
})

// 生命周期
onMounted(() => {
  updateCurrentTime()
  setInterval(updateCurrentTime, 1000)
  loadRentalInfoList()
  loadAvailableRooms()
})
</script>

<style scoped>
/* 主容器 */
.rent-info-management {
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

.header-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  color: white;
  font-weight: 600;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.stat-item:hover {
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

.stats-card.total-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stats-card.paid-card {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
}

.stats-card.unpaid-card {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stats-card.deposit-card {
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

/* 筛选与搜索区域样式 */
.filter-search-section {
  margin-bottom: 32px;
  width: 100%;
  max-width: 1600px;
  margin-left: auto;
  margin-right: auto;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.section-header {
  background: #f8fafc;
  padding: 20px 24px;
  border-bottom: 1px solid #e2e8f0;
}

.section-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1a202c;
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-filter-content {
  padding: 24px;
}

.search-row {
  margin-bottom: 20px;
}

/* 搜索输入框样式 */
.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 8px !important;
  border: 1px solid #d1d5db !important;
  box-shadow: none !important;
  transition: all 0.2s ease !important;
  background: white;
  height: 40px;
}

.search-input :deep(.el-input__wrapper:hover) {
  border-color: #9ca3af !important;
}

.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6 !important;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1) !important;
}

.search-input :deep(.el-input__inner) {
  font-size: 14px;
  color: #374151;
  padding-right: 80px;
}

.search-input :deep(.el-input__inner::placeholder) {
  color: #9ca3af;
}

.search-btn {
  position: absolute;
  right: 4px;
  top: 50%;
  transform: translateY(-50%);
  height: 32px;
  padding: 0 16px;
  font-size: 14px;
  border-radius: 6px;
}

.filter-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.filter-buttons-wrapper {
  flex: 1;
}

.filter-buttons :deep(.el-radio-button__inner) {
  border-radius: 6px;
  border: 1px solid #d1d5db;
  background: white;
  transition: all 0.2s ease;
  padding: 8px 16px;
  font-weight: 500;
  font-size: 14px;
  margin-right: 8px;
}

.filter-buttons :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

.filter-buttons :deep(.el-radio-button__inner:hover) {
  border-color: #9ca3af;
}

.action-buttons {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-btn {
  border-radius: 6px;
  font-weight: 500;
  font-size: 14px;
  padding: 8px 16px;
  height: 36px;
  transition: all 0.2s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.primary-btn {
  background: #3b82f6;
  border-color: #3b82f6;
  border-radius: 6px;
  font-weight: 500;
  font-size: 14px;
  padding: 8px 16px;
  height: 36px;
}

.primary-btn:hover {
  background: #2563eb;
  border-color: #2563eb;
  transform: translateY(-1px);
}

/* 表格容器 */
.table-section {
  width: 100%;
  max-width: 1600px;
  margin: 0 auto 32px;
}

/* 表格样式 */
.rental-info-table {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  width: 100%;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
}

.rental-info-table :deep(.el-table__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.rental-info-table :deep(.el-table th) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: #fff !important;
  font-weight: bold !important;
  border-bottom: none !important;
  padding: 16px 12px !important;
}

.rental-info-table :deep(.el-table__row:hover > td) {
  background-color: rgba(102, 126, 234, 0.05) !important;
}

.rental-info-table :deep(.el-table__row) {
  transition: all 0.2s ease;
  border-bottom: 1px solid #f0f2f5;
}

.rental-info-table :deep(.el-table td) {
  border-bottom: 1px solid #f1f5f9 !important;
  padding: 16px 12px !important;
}

/* 表格内容样式 */
.room-badge, .tenant-info, .phone-info, .money-amount, .occupant-info, .date-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 0;
}

.room-badge {
  padding: 8px 14px;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.1) 0%, rgba(139, 69, 194, 0.1) 100%);
  border: 1px solid rgba(99, 102, 241, 0.2);
  border-radius: 14px;
  color: #6366f1;
  font-weight: 600;
  font-size: 13px;
  width: fit-content;
  transition: all 0.3s ease;
}

.room-badge:hover {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.15) 0%, rgba(139, 69, 194, 0.15) 100%);
  border-color: rgba(99, 102, 241, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.25);
}

.tenant-info {
  font-weight: 700;
  color: #1a202c;
}

.phone-info {
  font-family: 'Courier New', monospace;
  color: #10b981;
  font-weight: 600;
}

.money-amount {
  color: #52c41a;
  font-weight: 700;
  font-size: 15px;
}

.payment-status {
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.remarks-text {
  color: #6b7280;
  font-size: 13px;
  max-width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: flex-start;
  align-items: center;
}

.action-btn {
  border-radius: 8px;
  padding: 6px 12px;
  font-weight: 600;
  font-size: 12px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-btn:hover {
  transform: translateY(-1px);
}

/* 分页 */
.pagination-section {
  padding: 24px 0;
  display: flex;
  justify-content: center;
}

.pagination :deep(.el-pager li) {
  border-radius: 8px;
  margin: 0 2px;
  transition: all 0.2s ease;
  background: rgba(255, 255, 255, 0.9);
  color: #374151;
}

.pagination :deep(.el-pager li:hover) {
  background: rgba(102, 126, 234, 0.1);
}

.pagination :deep(.el-pager li.is-active) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.pagination :deep(.el-pagination__total),
.pagination :deep(.el-pagination__sizes),
.pagination :deep(.el-pagination__jump) {
  color: rgba(255, 255, 255, 0.9);
  font-weight: 600;
}

/* 对话框样式 */
.rental-info-dialog :deep(.el-dialog) {
  border-radius: 24px !important;
  background: rgba(255, 255, 255, 0.98) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
}

.rental-info-dialog :deep(.el-dialog__header) {
  padding: 24px 32px !important;
  background: rgba(248, 250, 252, 0.8) !important;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06) !important;
  border-radius: 24px 24px 0 0 !important;
}

.rental-info-form {
  padding: 32px;
}

.rental-info-form :deep(.el-form-item__label) {
  color: #374151 !important;
  font-weight: 600 !important;
  font-size: 14px !important;
  margin-bottom: 8px !important;
}

.rental-info-form :deep(.el-input__wrapper) {
  border-radius: 12px !important;
  border: 1px solid #e5e7eb !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04) !important;
  transition: all 0.2s ease !important;
}

.rental-info-form :deep(.el-input__wrapper:hover) {
  border-color: rgba(102, 126, 234, 0.4) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1) !important;
}

.rental-info-form :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea !important;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1) !important;
}

/* 详情对话框 */
.view-dialog :deep(.el-dialog) {
  border-radius: 24px !important;
  background: rgba(255, 255, 255, 0.98) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
}

.detail-content {
  padding: 32px;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
  border-radius: 15px;
}

.detail-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.detail-title h3 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #2c3e50;
}

.detail-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.detail-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s ease;
}

.detail-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.12);
}

.detail-card.full-width {
  grid-column: 1 / -1;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 700;
  color: #1a202c;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(102, 126, 234, 0.2);
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 8px 0;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  color: #6b7280;
  font-weight: 600;
  font-size: 14px;
}

.value {
  color: #1a202c;
  font-weight: 700;
  font-size: 15px;
}

.value.money {
  color: #52c41a;
  font-size: 20px;
  font-weight: 800;
}

.remarks-content {
  background: rgba(248, 250, 252, 0.8);
  padding: 20px;
  border-radius: 12px;
  border-left: 4px solid #667eea;
  backdrop-filter: blur(10px);
}

.remarks-content p {
  margin: 0;
  color: #374151;
  line-height: 1.6;
  font-size: 15px;
}

.dialog-footer {
  padding: 24px 32px;
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  background: rgba(248, 250, 252, 0.5);
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 0 0 24px 24px;
}

.dialog-footer .el-button {
  border-radius: 12px !important;
  padding: 12px 24px !important;
  font-weight: 600 !important;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .rent-info-management,
  .page-header,
  .stats-section,
  .filter-search-section,
  .table-section {
    padding-left: 20px;
    padding-right: 20px;
  }
}

@media (max-width: 768px) {
  .rent-info-management {
    padding: 16px;
  }

  .header-content {
    flex-direction: column;
    gap: 20px;
    align-items: flex-start;
  }

  .header-stats {
    width: 100%;
    justify-content: space-between;
  }

  .title-content h1 {
    font-size: 24px;
  }

  .header-subtitle {
    font-size: 14px;
  }

  .stats-row .el-col {
    margin-bottom: 16px;
  }

  .filter-actions {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }

  .filter-buttons-wrapper {
    width: 100%;
  }

  .action-buttons {
    justify-content: center;
    width: 100%;
  }

  .detail-cards {
    grid-template-columns: 1fr;
  }

  .dialog-footer {
    flex-direction: column;
  }

  .dialog-footer .el-button {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .rent-info-management {
    padding: 12px;
  }

  .title-content h1 {
    font-size: 20px;
  }

  .header-subtitle {
    font-size: 12px;
  }

  .stats-number {
    font-size: 24px;
  }

  .stats-label {
    font-size: 12px;
  }

  .action-buttons {
    gap: 8px;
  }

  .action-btn {
    padding: 4px 8px;
    font-size: 11px;
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
.stats-section,
.filter-search-section,
.table-section {
  animation: slideInUp 0.6s ease-out;
}

.stats-section {
  animation-delay: 0.1s;
}

.filter-search-section {
  animation-delay: 0.2s;
}

.table-section {
  animation-delay: 0.3s;
}

/* 表格内容渐入动画 */
.room-badge,
.tenant-info,
.phone-info,
.money-amount,
.occupant-info,
.date-info,
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

/* 统计卡片动画 */
.stats-card:nth-child(1) { animation-delay: 0.1s; }
.stats-card:nth-child(2) { animation-delay: 0.2s; }
.stats-card:nth-child(3) { animation-delay: 0.3s; }
.stats-card:nth-child(4) { animation-delay: 0.4s; }
</style>