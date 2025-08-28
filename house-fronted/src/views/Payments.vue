<template>
  <div class="payments-management">
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
            <h1>{{ currentFloor === '6' ? '六楼' : '五楼' }}费用管理</h1>
            <p class="header-subtitle">智能化租金与费用管理系统</p>
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
            添加收费记录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计概览卡片 -->
    <div class="stats-section">
      <el-row :gutter="24" class="stats-row">
        <el-col :span="6">
          <div class="stats-card primary-card enhanced-card" @click="filterByType('rent')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ formatMoney(stats.totalRentIncome) }}</div>
                <div class="stats-label">租金收入</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper primary-icon">
                  <el-icon class="stats-icon">
                    <House/>
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
          <div class="stats-card success-card enhanced-card" @click="filterByType('utilities')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ formatMoney(stats.totalUtilitiesIncome) }}</div>
                <div class="stats-label">水电费收入</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper success-icon">
                  <el-icon class="stats-icon">
                    <Lightning/>
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
          <div class="stats-card warning-card enhanced-card" @click="filterByStatus('unpaid')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ formatMoney(stats.totalPendingAmount) }}</div>
                <div class="stats-label">待收款</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper warning-icon">
                  <el-icon class="stats-icon">
                    <Clock/>
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
          <div class="stats-card danger-card enhanced-card" @click="filterByStatus('overdue')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">¥{{ formatMoney(stats.totalOverdueAmount) }}</div>
                <div class="stats-label">逾期欠款</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper danger-icon">
                  <el-icon class="stats-icon">
                    <Warning/>
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
            <el-select
                v-model="statusFilter"
                placeholder="支付状态"
                clearable
                size="large"
                class="status-filter enhanced-select"
            >
              <template #prefix>
                <el-icon>
                  <CircleCheck/>
                </el-icon>
              </template>
              <el-option label="全部状态" value=""/>
              <el-option label="已支付" :value="1"/>
              <el-option label="未支付" :value="2"/>
            </el-select>
          </el-col>
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
          <el-col :span="6">
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
          <el-col :span="5">
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

    <!-- 费用列表表格 -->
    <div class="table-container">
      <el-card class="table-card modern-table-card" shadow="hover">
        <template #header>
          <div class="table-header">
            <div class="table-title">
              <el-icon class="title-icon">
                <Money/>
              </el-icon>
              <span>费用详细信息</span>
            </div>
            <el-tag type="primary" size="large" class="count-tag">共 {{ pagination.total }} 条记录</el-tag>
          </div>
        </template>

        <el-table
            v-loading="loading"
            :data="paymentList"
            stripe
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="modern-table"
            :header-cell-style="{
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            color: '#fff',
            fontWeight: 'bold',
            borderBottom: 'none'
          }"
            :row-style="{ borderBottom: '1px solid #f0f2f5' }"
            :empty-text="paymentList.length === 0 && !loading ? '暂无数据，请检查数据库连接或创建新记录' : '加载中...'"
        >
          <el-table-column type="selection" width="55"/>

          <el-table-column prop="roomNumber" label="房号" width="100">
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

          <el-table-column label="费用明细" width="280">
            <template #default="{ row }">
              <div class="fee-details">
                <div class="fee-row">
                  <el-icon class="fee-icon" color="#67c23a">
                    <House/>
                  </el-icon>
                  <span class="fee-label">租金:</span>
                  <span class="fee-amount">¥{{ formatMoney(row.monthlyRent || 0) }}</span>
                </div>
                <div class="fee-row" v-if="row.waterFee || row.electricityFee || row.utilitiesFee">
                  <el-icon class="fee-icon" color="#409eff">
                    <Lightning/>
                  </el-icon>
                  <span class="fee-label">水电费:</span>
                  <span class="fee-amount">¥{{
                      formatMoney((Number(row.waterFee) || 0) + (Number(row.electricityFee) || 0) + (Number(row.utilitiesFee) || 0))
                    }}</span>
                </div>
                <div class="fee-row total-row">
                  <el-icon class="fee-icon" color="#f56c6c">
                    <Money/>
                  </el-icon>
                  <span class="fee-label">总计:</span>
                  <span class="fee-amount total-amount">¥{{ formatMoney(row.totalDue || 0) }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="支付状态" width="120">
            <template #default="{ row }">
              <el-tag
                  :type="getStatusType(row.paymentStatus)"
                  :icon="getStatusIcon(row.paymentStatus)"
                  effect="dark"
                  size="large"
                  class="status-tag"
              >
                {{ getStatusText(row.paymentStatus) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="合同期限" width="200">
            <template #default="{ row }">
              <div class="contract-period">
                <div class="period-row">
                  <el-icon color="#67c23a">
                    <Calendar/>
                  </el-icon>
                  <span>{{ row.contractStartDate || '未设置' }}</span>
                </div>
                <div class="period-row">
                  <el-icon color="#e6a23c">
                    <Calendar/>
                  </el-icon>
                  <span>{{ row.contractEndDate || '未设置' }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="创建时间" width="150">
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

          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons-group">
                <el-tooltip content="查看详情" placement="top">
                  <el-button type="primary" size="small" @click="viewPayment(row)" class="action-btn view-btn">
                    <el-icon>
                      <View/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="编辑记录" placement="top">
                  <el-button type="warning" size="small" @click="editPayment(row)" class="action-btn edit-btn">
                    <el-icon>
                      <Edit/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <!-- 只对未缴费的记录显示标记缴费按钮 -->
                <el-tooltip placement="top" v-if="row.paymentStatus !== 1">
                  <template #content>
                    <div class="tooltip-content">
                      <div>✅ 标记为已缴费</div>
                      <div>📋 自动同步到缴费记录页面</div>
                      <div>💰 当前状态: 未缴费</div>
                    </div>
                  </template>
                  <el-button 
                    type="success" 
                    size="small" 
                    @click="togglePaymentStatus(row)"
                    class="action-btn status-btn enhanced-status-btn"
                  >
                    <el-icon>
                      <CircleCheck/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                
                <!-- 已缴费的记录显示状态标识 -->
                <el-tag v-if="row.paymentStatus === 1" type="success" effect="dark" size="small" class="paid-status-tag">
                  <el-icon><CircleCheckFilled/></el-icon>
                  已缴费
                </el-tag>
                <el-tooltip content="删除记录" placement="top">
                  <el-button type="danger" size="small" @click="deletePayment(row)" class="action-btn delete-btn">
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
              @current-change="handleCurrentChange"
              background
              class="modern-pagination"
          />
        </div>
      </el-card>
    </div>

    <!-- 新建/编辑费用对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="80%"
        :before-close="handleDialogClose"
        class="modern-dialog"
        destroy-on-close
    >
      <el-form
          ref="paymentFormRef"
          :model="paymentForm"
          :rules="paymentRules"
          label-width="120px"
          class="payment-form modern-form"
      >
        <el-row :gutter="24">
          <!-- 基本信息 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <InfoFilled/>
                  </el-icon>
                  <span>基本信息</span>
                </div>
              </template>

              <el-form-item label="房间号" prop="roomNumber">
                <el-select
                    v-model="paymentForm.roomNumber"
                    placeholder="请选择已出租的房间"
                    style="width: 100%"
                    size="large"
                    :loading="loadingRooms"
                    @change="handleRoomChange"
                    filterable
                >
                  <el-option
                      v-for="room in rentedRooms"
                      :key="room.roomNumber"
                      :label="`${room.roomNumber} - ${room.tenantName}`"
                      :value="room.roomNumber"
                  />
                </el-select>
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  只显示已出租状态的房间，选择后会自动加载租客信息
                </div>
              </el-form-item>

              <el-form-item label="租客姓名" prop="tenantName">
                <el-input
                    v-model="paymentForm.tenantName"
                    placeholder="选择房间后会自动加载租客姓名"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  选择房间后会自动加载租客姓名，无需手动输入
                </div>
              </el-form-item>
            </el-card>
          </el-col>

          <!-- 费用信息 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <Money/>
                  </el-icon>
                  <span>费用信息</span>
                </div>
              </template>

              <el-form-item label="月租金" prop="monthlyRent">
                <el-input-number
                    v-model="paymentForm.monthlyRent"
                    :min="0"
                    :precision="2"
                    placeholder="月租金会自动从租房信息获取或设置，不可修改"
                    style="width: 100%"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  月租金会自动从租房信息获取或设置，不可修改
                </div>
              </el-form-item>

              <el-form-item label="押金" prop="deposit">
                <el-input-number
                    v-model="paymentForm.deposit"
                    :min="0"
                    :precision="2"
                    placeholder="押金会自动从租房信息获取或设置，不可修改"
                    style="width: 100%"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  押金会自动从租房信息获取或设置，不可修改
                </div>
              </el-form-item>

              <el-form-item label="缴费状态" prop="paymentStatus">
                <el-select v-model="paymentForm.paymentStatus" placeholder="选择支付状态" style="width: 100%"
                           size="large">
                  <el-option label="未缴费" :value="2"/>
                  <el-option label="已缴费" :value="1"/>
                </el-select>
              </el-form-item>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <!-- 水电费信息 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <Lightning/>
                  </el-icon>
                  <span>水电费信息</span>
                </div>
              </template>

              <el-form-item label="用水量(方)" prop="waterUsage">
                <el-input-number
                    v-model="paymentForm.waterUsage"
                    :min="0"
                    :precision="2"
                    placeholder="0"
                    style="width: 100%"
                    size="large"
                    @change="calculateTotalFee"
                >
                  <template #append>
                    <el-icon color="#409eff">
                      <Lightning/>
                    </el-icon>
                  </template>
                </el-input-number>
                <div class="form-tip">
                  <el-icon>
                    <Lightning/>
                  </el-icon>
                  输入用水量，系统将按{{ waterRate }}元/方自动计算费用
                </div>
              </el-form-item>

              <el-form-item label="用电量(度)" prop="electricityUsage">
                <el-input-number
                    v-model="paymentForm.electricityUsage"
                    :min="0"
                    :precision="2"
                    placeholder="0"
                    style="width: 100%"
                    size="large"
                    @change="calculateTotalFee"
                >
                  <template #append>
                    <el-icon color="#f56c6c">
                      <Lightning/>
                    </el-icon>
                  </template>
                </el-input-number>
                <div class="form-tip">
                  <el-icon>
                    <Lightning/>
                  </el-icon>
                  输入用电量，系统将按{{ electricityRate }}元/度自动计算费用
                </div>
              </el-form-item>
            </el-card>
          </el-col>

          <!-- 应缴费总额 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <Money/>
                  </el-icon>
                  <span>应缴费总额(元)</span>
                </div>
              </template>

              <el-form-item label="应缴费总额(元)" prop="totalDue">
                <el-input-number
                    v-model="paymentForm.totalDue"
                    :min="0"
                    :precision="2"
                    placeholder="自动计算"
                    style="width: 100%"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <Money/>
                  </el-icon>
                  自动计算：月租金 + (用水量×{{ waterRate }}) + (用电量×{{ electricityRate }})
                </div>
              </el-form-item>

              <el-form-item label="入住日期" prop="checkInDate">
                <el-date-picker
                    v-model="paymentForm.checkInDate"
                    type="date"
                    placeholder="yyyy/mm/日"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  入住日期将自动从租房信息获取或设置，不可修改
                </div>
              </el-form-item>
            </el-card>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <Document/>
                  </el-icon>
                  <span>备注信息</span>
                </div>
              </template>

              <el-form-item label="备注" prop="remarks">
                <el-input
                    v-model="paymentForm.remarks"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入备注信息"
                    size="large"
                />
              </el-form-item>
            </el-card>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose" size="large" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="handleSubmit" size="large" class="submit-btn">
            {{ isEditMode ? '更新记录' : '保存' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看费用详情对话框 -->
    <el-dialog
        v-model="viewDialogVisible"
        title="费用详情"
        width="70%"
        class="modern-dialog view-dialog"
    >
      <div v-if="viewPaymentData" class="payment-detail">
        <el-descriptions :column="2" border class="modern-descriptions">
          <el-descriptions-item label="房间号">{{ viewPaymentData.roomNumber }}</el-descriptions-item>
          <el-descriptions-item label="租客姓名">{{ viewPaymentData.tenantName }}</el-descriptions-item>
          <el-descriptions-item label="月租金">¥{{
              formatMoney(viewPaymentData.monthlyRent || 0)
            }}
          </el-descriptions-item>
          <el-descriptions-item label="押金">¥{{ formatMoney(viewPaymentData.deposit || 0) }}</el-descriptions-item>
          <el-descriptions-item label="水费">¥{{ formatMoney(viewPaymentData.waterFee || 0) }}</el-descriptions-item>
          <el-descriptions-item label="电费">¥{{
              formatMoney(viewPaymentData.electricityFee || 0)
            }}
          </el-descriptions-item>
          <el-descriptions-item label="其他费用">¥{{
              formatMoney(viewPaymentData.utilitiesFee || 0)
            }}
          </el-descriptions-item>
          <el-descriptions-item label="总费用">¥{{ formatMoney(viewPaymentData.totalDue || 0) }}</el-descriptions-item>
          <el-descriptions-item label="支付状态">
            <el-tag :type="getStatusType(viewPaymentData.paymentStatus)">
              {{ getStatusText(viewPaymentData.paymentStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="合同开始">{{
              viewPaymentData.contractStartDate || '未设置'
            }}
          </el-descriptions-item>
          <el-descriptions-item label="合同结束">{{
              viewPaymentData.contractEndDate || '未设置'
            }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(viewPaymentData.createdAt) }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="viewPaymentData.remarks" class="payment-remarks">
          <h4>备注信息</h4>
          <p>{{ viewPaymentData.remarks }}</p>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="viewDialogVisible = false" size="large">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, computed, onMounted} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage, ElMessageBox, type FormInstance, type FormRules} from 'element-plus'
import {
  Plus, Download, Refresh, Search, View, Edit, Delete, Money,
  CircleCheck, Warning, Clock, House, Lightning, Filter, OfficeBuilding,
  User, Calendar, InfoFilled, Document, RefreshRight, CircleCheckFilled
} from '@element-plus/icons-vue'
import type {
  RentalQuery, PaymentStats, RentalRecord, PageResponse
} from '../api/rentals'
import {
  getPayments, getPaymentStats, createPayment, updatePayment,
  deletePayment as deletePaymentApi, getPaymentById, updatePaymentStatus,
  getRentals
} from '../api/rentals'
import {getRoomsByStatus} from '../api/rooms'
import {getRentalInfoList} from '../api/rentalInfo'
import {addNewRentalRecord, addOldRentalRecord} from '../api/rentalRecords'

// 路由信息
const route = useRoute()
const currentFloor = computed(() => route.params.floor as string)

// 响应式数据
const loading = ref(false)
const paymentList = ref<RentalRecord[]>([])
const stats = ref<PaymentStats>({
  totalRentIncome: 0,
  totalUtilitiesIncome: 0,
  totalPendingAmount: 0,
  totalOverdueAmount: 0,
  paidCount: 0,
  unpaidCount: 0,
  totalCount: 0,
  monthlyIncome: 0,
  paymentRate: 0
})

// 搜索表单
const searchKeyword = ref('')
const statusFilter = ref<number | ''>('')
const typeFilter = ref('')
const monthFilter = ref('')

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const isEditMode = ref(false)
const currentEditId = ref<number>()

const dialogTitle = computed(() =>
    isEditMode.value ? '编辑收费记录' : '添加收费记录'
)

// 费用表单
const paymentFormRef = ref<FormInstance>()
const paymentForm = reactive<RentalRecord>({
  roomNumber: '',
  tenantName: '',
  deposit: 0,
  monthlyRent: 0,
  waterUsage: 0,
  electricityUsage: 0,
  waterFee: 0,
  electricityFee: 0,
  utilitiesFee: 0,
  totalDue: 0,
  paymentStatus: 1,
  checkInDate: '',
  contractStartDate: '',
  contractEndDate: '',
  remarks: ''
})

// 已出租房间列表
const rentedRooms = ref<any[]>([])
const loadingRooms = ref(false)

// 水电费单价
const waterRate = 3.5  // 3.5元/方
const electricityRate = 1.2  // 1.2元/度

// 查看费用数据
const viewPaymentData = ref<RentalRecord | null>(null)

// 表单验证规则
const paymentRules: FormRules = {
  roomNumber: [
    {required: true, message: '请选择房间号', trigger: 'change'}
  ],
  paymentStatus: [
    {required: true, message: '请选择缴费状态', trigger: 'change'}
  ],
  waterUsage: [
    {type: 'number', min: 0, message: '用水量不能小于0', trigger: 'blur'}
  ],
  electricityUsage: [
    {type: 'number', min: 0, message: '用电量不能小于0', trigger: 'blur'}
  ]
}

// 计算属性
const hasActiveFilters = computed(() => {
  return !!(searchKeyword.value || statusFilter.value || typeFilter.value || monthFilter.value)
})

// 获取状态类型
const getStatusType = (status: number) => {
  const statusMap: Record<number, string> = {
    1: 'success',  // 已支付
    2: 'danger'    // 未支付
  }
  return statusMap[status] || 'info'
}

// 获取状态图标
const getStatusIcon = (status: number) => {
  const iconMap: Record<number, any> = {
    1: CircleCheck,
    2: Clock
  }
  return iconMap[status] || Clock
}

// 获取状态文本
const getStatusText = (status: number) => {
  const textMap: Record<number, string> = {
    1: '已支付',
    2: '未支付'
  }
  return textMap[status] || '未知'
}

// 方法
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

const formatMoney = (amount: number | string) => {
  const num = typeof amount === 'string' ? parseFloat(amount) : amount
  if (isNaN(num)) return '0.00'
  return num.toLocaleString('zh-CN', {minimumFractionDigits: 2})
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

const filterByType = (type: string) => {
  typeFilter.value = type
  handleSearch()
}

const filterByStatus = (status: string) => {
  if (status === 'unpaid') {
    statusFilter.value = 2
  } else if (status === 'paid') {
    statusFilter.value = 1
  } else if (status === 'overdue') {
    statusFilter.value = 2
    // 可以添加逾期逻辑，这里暂时和未支付一样
  } else {
    statusFilter.value = ''
  }
  handleSearch()
}

// 新增按月份筛选的快捷方式
const filterByMonth = (monthType: string) => {
  const now = new Date()
  let targetDate = new Date()

  switch (monthType) {
    case 'current':
      // 当前月
      break
    case 'last':
      // 上个月
      targetDate.setMonth(now.getMonth() - 1)
      break
    case 'next':
      // 下个月
      targetDate.setMonth(now.getMonth() + 1)
      break
    default:
      monthFilter.value = ''
      handleSearch()
      return
  }

  const year = targetDate.getFullYear()
  const month = String(targetDate.getMonth() + 1).padStart(2, '0')
  monthFilter.value = `${year}-${month}`
  handleSearch()
}

const clearAllFilters = () => {
  searchKeyword.value = ''
  statusFilter.value = ''
  typeFilter.value = ''
  monthFilter.value = ''
  handleSearch()
}

// 获取费用列表
const fetchPayments = async () => {
  loading.value = true
  try {
    const params: RentalQuery = {
      keyword: searchKeyword.value || undefined,
      paymentStatus: statusFilter.value || undefined,
      feeType: typeFilter.value || undefined,
      monthFilter: monthFilter.value || undefined,
      page: pagination.page - 1,
      size: pagination.size
    }

    console.log('获取数据参数:', params)
    console.log('当前楼层:', currentFloor.value)

    const response = await getPayments(currentFloor.value, params) as any
    console.log('API响应:', response)

    if (response && response.code === 200) {
      paymentList.value = response.data.content || []
      pagination.total = response.data.totalElements || 0
      console.log('获取到的数据:', paymentList.value)
    } else {
      console.error('API响应错误:', response?.message)
      ElMessage.error('获取费用列表失败: ' + (response?.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取费用列表异常:', error)
    ElMessage.error('获取费用列表失败: ' + (error as any)?.message || '网络错误')
  } finally {
    loading.value = false
  }
}

// 获取统计信息
const fetchStats = async () => {
  try {
    console.log('获取统计信息，楼层:', currentFloor.value)
    const response = await getPaymentStats(currentFloor.value) as any
    console.log('统计信息响应:', response)

    if (response && response.code === 200) {
      stats.value = response.data
      console.log('统计数据:', stats.value)
    } else {
      console.error('统计信息响应错误:', response?.message)
      ElMessage.error('获取统计信息失败: ' + (response?.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取统计信息异常:', error)
    ElMessage.error('获取统计信息失败: ' + (error as any)?.message || '网络错误')
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.page = 1
  fetchPayments()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  fetchPayments()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  fetchPayments()
}

// 多选处理
const handleSelectionChange = (selection: RentalRecord[]) => {
  // 处理批量操作
}

// 显示新建对话框
const showAddDialog = () => {
  isEditMode.value = false
  resetForm()
  fetchRentedRooms() // 获取已出租房间列表
  dialogVisible.value = true
}

// 查看费用
const viewPayment = async (payment: RentalRecord) => {
  try {
    const response = await getPaymentById(currentFloor.value, payment.id!) as any
    if (response && response.code === 200) {
      viewPaymentData.value = response.data
      viewDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取费用详情失败')
  }
}

// 编辑费用
const editPayment = async (payment: RentalRecord) => {
  try {
    const response = await getPaymentById(currentFloor.value, payment.id!) as any
    if (response && response.code === 200) {
      isEditMode.value = true
      currentEditId.value = payment.id
      Object.assign(paymentForm, response.data)
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取费用详情失败')
  }
}

// 删除费用
const deletePayment = async (payment: RentalRecord) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除房间 "${payment.roomNumber}" 的费用记录吗？删除后将无法恢复！`,
        '确认删除',
        {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }
    )

    const response = await deletePaymentApi(currentFloor.value, payment.id!)
    if (response.data.code === 200) {
      ElMessage.success('删除成功')
      fetchPayments()
      fetchStats()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 切换支付状态
const togglePaymentStatus = async (payment: RentalRecord) => {
  try {
    const newStatus = payment.paymentStatus === 1 ? 2 : 1

    // 只有未缴费的记录才能标记为已缴费
    if (payment.paymentStatus === 1) {
      ElMessage.warning('该记录已经是缴费状态，无需重复操作')
      return
    }

    const confirmMessage = `确定要将房间 "${payment.roomNumber}" 标记为已缴费吗？\n\n✅ 标记后将自动创建缴费记录并同步到缴费记录管理页面\n💰 缴费金额：¥${formatMoney(payment.totalDue || payment.monthlyRent || 0)}\n👤 租客：${payment.tenantName}\n📅 缴费日期：${new Date().toLocaleDateString()}`

    await ElMessageBox.confirm(
        confirmMessage,
        '确认状态更改',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: false
        }
    )

    // 调用后端API更新支付状态，后端会自动创建缴费记录
    const response = await updatePaymentStatus(currentFloor.value, payment.id!, newStatus)
    if (response.data.code === 200) {
      // 显示成功消息
      if (newStatus === 1) {
        ElMessage({
          type: 'success',
          message: `✅ 缴费状态更新成功！\n💰 缴费记录已自动同步到缴费记录管理页面\n🏠 房间：${payment.roomNumber}\n👤 租客：${payment.tenantName}\n💵 金额：¥${formatMoney(payment.totalDue || payment.monthlyRent || 0)}`,
          duration: 5000,
          dangerouslyUseHTMLString: false
        })
        console.log('✅ 缴费状态更新成功，后端已自动创建缴费记录')
      } else {
        ElMessage.success('支付状态已更新')
      }
      
      fetchPayments()
      fetchStats()
    } else {
      throw new Error(response.data.message || '状态更新失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('状态更新失败:', error)
      ElMessage.error('状态更新失败: ' + ((error as any)?.message || '网络错误'))
    }
  }
}

// 创建缴费记录（现在后端已自动处理，此函数作为备用）
const createPaymentRecord = async (payment: RentalRecord, retryCount = 0) => {
  const maxRetries = 2
  
  try {
    console.log(`🔄 手动创建缴费记录 (尝试次数: ${retryCount + 1}/${maxRetries + 1}):`, payment)
    
    // 构建缴费记录数据
    const totalRentValue = payment.totalDue || payment.monthlyRent || 0
    
    // 创建匹配数据库字段的对象
    const rentalRecordData = {
      room_number: payment.roomNumber || '',
      tenant_name: payment.tenantName || '',
      total_rent: totalRentValue,
      payment_date: new Date().toISOString().split('T')[0]
    }

    console.log('📝 缴费记录数据:', rentalRecordData)

    // 数据验证
    if (!rentalRecordData.room_number) {
      throw new Error('房间号不能为空')
    }
    if (!rentalRecordData.tenant_name) {
      throw new Error('租客姓名不能为空')
    }
    if (!totalRentValue || totalRentValue <= 0) {
      throw new Error('缴费金额必须大于0')
    }

    // 根据楼层选择对应的API
    console.log(`🏢 当前楼层: ${currentFloor.value}, 选择API: ${currentFloor.value === '6' ? 'addNewRentalRecord' : 'addOldRentalRecord'}`)
    
    let response
    if (currentFloor.value === '6') {
      response = await addNewRentalRecord(rentalRecordData)
    } else {
      response = await addOldRentalRecord(rentalRecordData)
    }

    console.log('📡 手动缴费记录创建响应:', response)

    // 检查响应状态
    if (response.data && (response.data as any).code === 200) {
      console.log('✅ 手动缴费记录创建成功')
      return response
    } else {
      throw new Error((response.data as any)?.message || '缴费记录创建失败')
    }
  } catch (error) {
    console.error(`❌ 手动创建缴费记录失败 (尝试 ${retryCount + 1}):`, error)
    
    // 如果还有重试次数且不是数据验证错误，则重试
    if (retryCount < maxRetries && !(error as any)?.message?.includes('不能为空') && !(error as any)?.message?.includes('必须大于0')) {
      console.log(`🔄 正在重试手动创建缴费记录...`)
      await new Promise(resolve => setTimeout(resolve, 1000 * (retryCount + 1)))
      return createPaymentRecord(payment, retryCount + 1)
    }
    
    const errorMessage = (error as any)?.message || '未知错误'
    throw new Error(`手动缴费记录同步失败: ${errorMessage}`)
  }
}

// 导出数据
const exportData = () => {
  ElMessage.info('导出功能开发中...')
}

// 获取已出租房间列表（包含租客信息）
const fetchRentedRooms = async () => {
  try {
    loadingRooms.value = true
    
    // 同时获取租房信息和房间信息
    const [rentalInfoResponse, roomsResponse] = await Promise.all([
      getRentalInfoList(currentFloor.value, {
        page: 0,
        size: 1000, // 获取所有记录
        rentalStatus: 1 // 只获取正常入住的租客
      }),
      getRoomsByStatus(currentFloor.value, 2) // 获取已出租状态的房间
    ])
    
    if (rentalInfoResponse.data && rentalInfoResponse.data.code === 200 &&
        roomsResponse.data && roomsResponse.data.code === 200) {
      
      const rentalInfos = rentalInfoResponse.data.data.content
      const rooms = roomsResponse.data.data
      
      // 创建房间号到房间信息的映射
      const roomMap = new Map()
      rooms.forEach((room: any) => {
        roomMap.set(room.roomNumber, room)
      })
      
      // 将租房信息和房间信息合并
      rentedRooms.value = rentalInfos.map((rentalInfo: any) => {
        const roomInfo = roomMap.get(rentalInfo.roomNumber)
        return {
          roomNumber: rentalInfo.roomNumber,
          tenantName: rentalInfo.tenantName,
          tenantPhone: rentalInfo.phone,
          baseRent: roomInfo ? roomInfo.baseRent : 0,
          deposit: rentalInfo.deposit,
          checkInDate: rentalInfo.checkInDate,
          contractStartDate: '', // 租房信息中没有合同信息，后续可以从合同表获取
          contractEndDate: ''
        }
      })
      
      console.log('获取到的已出租房间列表:', rentedRooms.value)
      console.log('房间信息映射:', roomMap)
    }
  } catch (error) {
    console.error('获取已出租房间失败:', error)
    ElMessage.error('获取房间信息失败')
  } finally {
    loadingRooms.value = false
  }
}

// 处理房间选择变化
const handleRoomChange = async (roomNumber: string) => {
  if (!roomNumber) {
    // 清空相关字段
    paymentForm.tenantName = ''
    paymentForm.monthlyRent = 0
    paymentForm.deposit = 0
    paymentForm.checkInDate = ''
    paymentForm.contractStartDate = ''
    paymentForm.contractEndDate = ''
    calculateTotalFee()
    return
  }

  try {
    // 从已出租房间中找到对应的租客信息
    const selectedRoom = rentedRooms.value.find(room => room.roomNumber === roomNumber)
    if (selectedRoom) {
      // 自动填充租客信息
      paymentForm.tenantName = selectedRoom.tenantName || ''
      paymentForm.monthlyRent = selectedRoom.baseRent || 0
      paymentForm.deposit = selectedRoom.deposit || 0
      paymentForm.checkInDate = selectedRoom.checkInDate || ''
      paymentForm.contractStartDate = selectedRoom.contractStartDate || ''
      paymentForm.contractEndDate = selectedRoom.contractEndDate || ''

      // 重新计算总费用
      calculateTotalFee()

      console.log('已自动填充租客信息:', {
        tenantName: paymentForm.tenantName,
        monthlyRent: paymentForm.monthlyRent,
        deposit: paymentForm.deposit
      })
    } else {
      console.warn('未找到对应房间的租客信息:', roomNumber)
      ElMessage.warning('未找到该房间的租客信息，请检查房间是否已出租')
    }
  } catch (error) {
    console.error('获取房间租客信息失败:', error)
    ElMessage.error('获取房间信息失败')
  }
}

// 计算总费用
const calculateTotalFee = () => {
  const waterFee = (paymentForm.waterUsage || 0) * waterRate
  const electricityFee = (paymentForm.electricityUsage || 0) * electricityRate

  paymentForm.waterFee = waterFee
  paymentForm.electricityFee = electricityFee
  paymentForm.utilitiesFee = waterFee + electricityFee
  paymentForm.totalDue = (paymentForm.monthlyRent || 0) + waterFee + electricityFee
}

// 重置表单
const resetForm = () => {
  if (paymentFormRef.value) {
    paymentFormRef.value.resetFields()
  }
  Object.assign(paymentForm, {
    roomNumber: '',
    tenantName: '',
    deposit: 0,
    monthlyRent: 0,
    waterUsage: 0,
    electricityUsage: 0,
    waterFee: 0,
    electricityFee: 0,
    utilitiesFee: 0,
    totalDue: 0,
    paymentStatus: 2, // 默认为未缴费
    checkInDate: '',
    contractStartDate: '',
    contractEndDate: '',
    remarks: ''
  })
}

// 处理对话框关闭
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  if (!paymentFormRef.value) return

  try {
    await paymentFormRef.value.validate()

    if (isEditMode.value) {
      const response = await updatePayment(currentFloor.value, currentEditId.value!, paymentForm)
      if (response.data.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchPayments()
        fetchStats()
      }
    } else {
      const response = await createPayment(currentFloor.value, paymentForm)
      if (response.data.code === 200) {
        ElMessage.success('创建成功')
        dialogVisible.value = false
        fetchPayments()
        fetchStats()
      }
    }
  } catch (error) {
    ElMessage.error(isEditMode.value ? '更新失败' : '创建失败')
  }
}

// 初始化
onMounted(() => {
  fetchPayments()
  fetchStats()
})
</script>

<style scoped>
/* 继承Contracts.vue的样式 */
.payments-management {
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

.month-filter {
  width: 100%;
}

.search-row {
  margin-top: 0;
}

.enhanced-input,
.enhanced-select {
  border-radius: 12px;
}

.search-input {
  width: 100%;
}

.status-filter,
.type-filter {
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

.fee-details {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.fee-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.fee-icon {
  font-size: 16px;
}

.fee-label {
  color: #606266;
  min-width: 60px;
}

.fee-amount {
  font-weight: 600;
  color: #303133;
}

.total-row {
  border-top: 1px solid #f0f2f5;
  padding-top: 4px;
  font-weight: 700;
}

.total-amount {
  color: #f56c6c;
  font-size: 16px;
}

.status-tag {
  font-weight: 600;
  border-radius: 12px;
  padding: 6px 12px;
}

.contract-period {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
}

.period-row {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
}

.create-time {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #606266;
}

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

.edit-btn {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border: none;
}

.status-btn {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
}

.delete-btn {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border: none;
}

.enhanced-status-btn {
  position: relative;
  overflow: hidden;
}

.enhanced-status-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  transition: left 0.5s;
}

.enhanced-status-btn:hover::before {
  left: 100%;
}

.tooltip-content {
  text-align: center;
  line-height: 1.5;
}

.tooltip-content div {
  margin: 2px 0;
}

.paid-status-tag {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border: none;
  color: white;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
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
.payment-detail {
  max-height: 65vh;
  overflow-y: auto;
  padding-right: 8px;
}

.modern-descriptions {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

.payment-remarks {
  margin-top: 24px;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 12px;
  border: 1px solid #e4e7ed;
}

.payment-remarks h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-weight: 600;
}

.payment-remarks p {
  margin: 0;
  color: #606266;
  line-height: 1.8;
}

/* 表单提示样式 */
.form-tip {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.4;
}

.form-tip .el-icon {
  font-size: 14px;
  color: #409eff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .payments-management {
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
.modern-table-card {
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