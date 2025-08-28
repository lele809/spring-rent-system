<template>
  <div class="contracts-management">
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
            <Document/>
          </el-icon>
          <div class="title-content">
            <h1>{{ currentFloor === '6' ? '六楼' : '五楼' }}合同管理</h1>
            <p class="header-subtitle">智能化租房合同管理系统</p>
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
            新建合同
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
                <div class="stats-number">{{ stats.totalContracts }}</div>
                <div class="stats-label">总合同数</div>
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
          <div class="stats-card success-card enhanced-card" @click="filterByStatus('active')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ stats.activeContracts }}</div>
                <div class="stats-label">有效合同</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper success-icon">
                  <el-icon class="stats-icon">
                    <CircleCheck/>
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
          <div class="stats-card warning-card enhanced-card" @click="filterByStatus('expiring')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ stats.expiringContracts }}</div>
                <div class="stats-label">即将到期</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper warning-icon">
                  <el-icon class="stats-icon">
                    <Warning/>
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
          <div class="stats-card danger-card enhanced-card" @click="filterByStatus('expired')">
            <div class="card-glow"></div>
            <div class="stats-content">
              <div class="stats-left">
                <div class="stats-number">{{ stats.expiredContracts }}</div>
                <div class="stats-label">已过期</div>
              </div>
              <div class="stats-right">
                <div class="stats-icon-wrapper danger-icon">
                  <el-icon class="stats-icon">
                    <CircleClose/>
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
                placeholder="合同状态"
                clearable
                size="large"
                class="status-filter enhanced-select"
            >
              <template #prefix>
                <el-icon>
                  <Document/>
                </el-icon>
              </template>
              <el-option label="全部状态" value=""/>
              <el-option label="生效中" value="1"/>
              <el-option label="即将到期" value="1"/>
              <el-option label="已过期" value="2"/>
              <el-option label="已终止" value="3"/>
            </el-select>
          </el-col>
          <el-col :span="7">
            <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                size="large"
                class="date-picker enhanced-input"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="handleSearch"
            />
          </el-col>
          <el-col :span="6">
            <el-input
                v-model="searchKeyword"
                placeholder="搜索合同号、租客姓名..."
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
          <el-col :span="4">
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

    <!-- 合同列表表格 -->
    <div class="table-container">
      <el-card class="table-card modern-table-card" shadow="hover">
        <template #header>
          <div class="table-header">
            <div class="table-title">
              <el-icon class="title-icon">
                <Document/>
              </el-icon>
              <span>合同详细信息</span>
            </div>
            <el-tag type="primary" size="large" class="count-tag">共 {{ pagination.total }} 份合同</el-tag>
          </div>
        </template>

        <el-table
            v-loading="loading"
            :data="contractList"
            stripe
            style="width: 100%"
            @selection-change="handleSelectionChange"
            class="modern-table"
            empty-text="暂无合同数据"
            :header-cell-style="{
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          color: '#fff',
          fontWeight: 'bold',
          borderBottom: 'none'
        }"
            :row-style="{ borderBottom: '1px solid #f0f2f5' }"
        >
          <el-table-column type="selection" width="55"/>

          <el-table-column prop="contractNumber" label="合同编号" width="160">
            <template #default="{ row }">
              <div class="contract-number-cell">
                <el-tag type="primary" effect="light" class="contract-tag">
                  {{ row.contractNumber }}
                </el-tag>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="roomNumber" label="房号" width="150">
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

          <el-table-column prop="tenantName" label="租客信息" width="200">
            <template #default="{ row }">
              <div class="tenant-info">
                <div class="tenant-avatar">
                  <el-icon>
                    <User/>
                  </el-icon>
                </div>
                <div class="tenant-details">
                  <div class="tenant-name">{{ row.tenantName }}</div>
                  <div class="tenant-phone">{{ row.tenantPhone }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="monthlyRent" label="租金信息" width="200">
            <template #default="{ row }">
              <div class="rent-info">
                <div class="rent-cell">
                  <el-icon class="money-icon">
                    <Money/>
                  </el-icon>
                  <span class="rent-amount">¥{{ row.monthlyRent }}</span>
                  <span class="rent-period">/月</span>
                </div>
                <div class="deposit-cell">
                  <el-icon class="deposit-icon" color="#f56c6c">
                    <CreditCard/>
                  </el-icon>
                  <span class="deposit-amount">押金: ¥{{ row.deposit || row.monthlyRent * 2 }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="合同期限" width="220">
            <template #default="{ row }">
              <div class="contract-period">
                <div class="period-row">
                  <div class="period-item start-date">
                    <el-icon color="#67c23a">
                      <Calendar/>
                    </el-icon>
                    <span>{{ row.contractStartDate }}</span>
                  </div>
                  <div class="period-divider">
                    <el-icon>
                      <Right/>
                    </el-icon>
                  </div>
                  <div class="period-item end-date">
                    <el-icon color="#e6a23c">
                      <Calendar/>
                    </el-icon>
                    <span>{{ row.contractEndDate }}</span>
                  </div>
                </div>
                <div class="period-duration">
                  <el-icon color="#909399">
                    <Clock/>
                  </el-icon>
                  <span>{{ row.contractDuration || 12 }}个月 · {{ row.paymentMethod || '月付' }}</span>
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="状态" width="130">
            <template #default="{ row }">
              <el-tag
                  :type="getStatusType(row.contractStatus)"
                  :icon="getStatusIcon(row.contractStatus)"
                  effect="dark"
                  size="large"
                  class="status-tag"
              >
                {{ getStatusText(row.contractStatus) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="创建时间" width="200">
            <template #default="{ row }">
              <div class="create-time">
                <el-icon color="#909399">
                  <Clock/>
                </el-icon>
                <span>{{ row.createdAt || '2024-01-01' }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <div class="action-buttons-group">
                <el-tooltip content="查看详情" placement="top">
                  <el-button type="primary" size="small" @click="viewContract(row)" class="action-btn view-btn">
                    <el-icon>
                      <View/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-dropdown @command="command => handleDownloadCommand(command, row)" trigger="click">
                  <el-button 
                    type="success" 
                    size="small" 
                    class="action-btn download-btn"
                    :loading="row.id && downloadingContracts.has(row.id)"
                    :disabled="row.id && downloadingContracts.has(row.id)"
                  >
                    <el-icon v-if="!(row.id && downloadingContracts.has(row.id))">
                      <Download/>
                    </el-icon>
                    <span v-if="row.id && downloadingContracts.has(row.id)" style="font-size: 12px;">下载中</span>
                    <el-icon v-if="!(row.id && downloadingContracts.has(row.id))" style="margin-left: 5px;">
                      <ArrowDown/>
                    </el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="download-text">
                        <el-icon><Document/></el-icon>
                        下载文本格式
                      </el-dropdown-item>
                      <el-dropdown-item command="download-pdf">
                        <el-icon><Download/></el-icon>
                        下载PDF格式
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
                <el-tooltip content="编辑合同" placement="top">
                  <el-button type="warning" size="small" @click="editContract(row)" class="action-btn edit-btn">
                    <el-icon>
                      <Edit/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="续签合同" placement="top">
                  <el-button type="info" size="small" @click="renewContract(row)" class="action-btn renew-btn">
                    <el-icon>
                      <RefreshRight/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip content="删除合同" placement="top">
                  <el-button type="danger" size="small" @click="deleteContract(row)" class="action-btn delete-btn">
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

    <!-- 新建/编辑合同对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="85%"
        :before-close="handleDialogClose"
        class="modern-dialog"
        destroy-on-close
    >
      <el-form
          ref="contractFormRef"
          :model="contractForm"
          :rules="contractRules"
          label-width="120px"
          class="contract-form modern-form"
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

              <el-form-item label="合同编号" prop="contractNumber">
                <el-input v-model="contractForm.contractNumber" placeholder="系统自动生成" size="large"/>
              </el-form-item>

              <el-form-item label="房间号" prop="roomNumber">
                <el-select
                    v-model="contractForm.roomNumber"
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
                  只显示已出租状态的房间，选择后会自动填充租客信息
                </div>
              </el-form-item>

              <el-form-item label="月租金" prop="monthlyRent">
                <el-input-number
                    v-model="contractForm.monthlyRent"
                    :min="0"
                    :precision="2"
                    placeholder="选择房间后会自动填充月租金"
                    style="width: 100%"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  选择房间后会自动从租房记录获取，如未获取到可手动输入
                </div>
              </el-form-item>

              <el-form-item label="押金" prop="deposit">
                <el-input-number
                    v-model="contractForm.deposit"
                    :min="0"
                    :precision="2"
                    placeholder="选择房间后会自动填充押金"
                    style="width: 100%"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  选择房间后会自动从租房信息获取，无需手动输入
                </div>
              </el-form-item>
            </el-card>
          </el-col>

          <!-- 租客信息 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <User/>
                  </el-icon>
                  <span>租客信息</span>
                </div>
              </template>

              <el-form-item label="租客姓名" prop="tenantName">
                <el-input
                    v-model="contractForm.tenantName"
                    placeholder="选择房间后会自动填充租客姓名"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  选择房间后会自动填充，无需手动输入
                </div>
              </el-form-item>

              <el-form-item label="联系电话" prop="tenantPhone">
                <el-input
                    v-model="contractForm.tenantPhone"
                    placeholder="选择房间后会自动填充联系电话"
                    size="large"
                    :disabled="true"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  选择房间后会自动填充，无需手动输入
                </div>
              </el-form-item>

              <el-form-item label="身份证号" prop="tenantIdCard">
                <el-input v-model="contractForm.tenantIdCard" placeholder="请输入身份证号" size="large"/>
              </el-form-item>

              <el-form-item label="房东姓名" prop="landlordName">
                <el-input v-model="contractForm.landlordName" placeholder="请输入房东姓名" size="large"/>
              </el-form-item>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <!-- 合同期限 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <Calendar/>
                  </el-icon>
                  <span>合同期限</span>
                </div>
              </template>

              <el-form-item label="合同开始日期" prop="contractStartDate">
                <el-date-picker
                    v-model="contractForm.contractStartDate"
                    type="date"
                    placeholder="默认为当天日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                    size="large"
                />
                <div class="form-tip">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  默认为当天日期，可以修改为实际合同开始日期
                </div>
              </el-form-item>

              <el-form-item label="合同结束日期" prop="contractEndDate">
                <el-date-picker
                    v-model="contractForm.contractEndDate"
                    type="date"
                    placeholder="选择结束日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                    size="large"
                />
              </el-form-item>

              <el-form-item label="合同期限(月)" prop="contractDuration">
                <el-input-number
                    v-model="contractForm.contractDuration"
                    :min="1"
                    placeholder="请输入合同期限"
                    style="width: 100%"
                    size="large"
                />
              </el-form-item>

              <el-form-item label="付款方式" prop="paymentMethod">
                <el-select v-model="contractForm.paymentMethod" placeholder="选择付款方式" style="width: 100%"
                           size="large">
                  <el-option label="月付" value="月付"/>
                  <el-option label="季付" value="季付"/>
                  <el-option label="半年付" value="半年付"/>
                  <el-option label="年付" value="年付"/>
                </el-select>
              </el-form-item>
            </el-card>
          </el-col>

          <!-- 其他信息 -->
          <el-col :span="12">
            <el-card class="form-card gradient-card" shadow="hover">
              <template #header>
                <div class="form-card-header">
                  <el-icon class="card-icon">
                    <Setting/>
                  </el-icon>
                  <span>其他信息</span>
                </div>
              </template>

              <el-form-item label="合同状态" prop="contractStatus">
                <el-select v-model="contractForm.contractStatus" placeholder="选择合同状态" style="width: 100%"
                           size="large">
                  <el-option label="生效中" :value="1"/>
                  <el-option label="已到期" :value="2"/>
                  <el-option label="提前终止" :value="3"/>
                  <el-option label="续签" :value="4"/>
                </el-select>
              </el-form-item>

              <el-form-item label="包含水电费" prop="utilitiesIncluded">
                <el-select v-model="contractForm.utilitiesIncluded" placeholder="是否包含水电费" style="width: 100%"
                           size="large">
                  <el-option label="不包含" :value="0"/>
                  <el-option label="包含" :value="1"/>
                </el-select>
              </el-form-item>

              <el-form-item label="水费单价" prop="waterRate">
                <el-input-number
                    v-model="contractForm.waterRate"
                    :min="0"
                    :precision="2"
                    placeholder="请输入水费单价"
                    style="width: 100%"
                    size="large"
                />
              </el-form-item>

              <el-form-item label="电费单价" prop="electricityRate">
                <el-input-number
                    v-model="contractForm.electricityRate"
                    :min="0"
                    :precision="2"
                    placeholder="请输入电费单价"
                    style="width: 100%"
                    size="large"
                />
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

              <el-form-item label="合同条款" prop="contractTerms">
                <el-input
                    v-model="contractForm.contractTerms"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入合同条款"
                    size="large"
                />
              </el-form-item>

              <el-form-item label="特殊约定" prop="specialAgreement">
                <el-input
                    v-model="contractForm.specialAgreement"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入特殊约定"
                    size="large"
                />
              </el-form-item>

              <el-form-item label="备注" prop="remarks">
                <el-input
                    v-model="contractForm.remarks"
                    type="textarea"
                    :rows="3"
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
            {{ isEditMode ? '更新合同' : '创建合同' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看合同详情对话框 -->
    <el-dialog
        v-model="viewDialogVisible"
        title="合同详情"
        width="80%"
        class="modern-dialog view-dialog"
    >
      <div v-if="viewContract" class="contract-detail">
        <el-descriptions :column="2" border class="modern-descriptions">
          <el-descriptions-item label="合同编号">{{ viewContractData.contractNumber }}</el-descriptions-item>
          <el-descriptions-item label="房间号">{{ viewContractData.roomNumber }}</el-descriptions-item>
          <el-descriptions-item label="租客姓名">{{ viewContractData.tenantName }}</el-descriptions-item>
          <el-descriptions-item label="租客电话">{{ viewContractData.tenantPhone }}</el-descriptions-item>
          <el-descriptions-item label="租客身份证">{{ viewContractData.tenantIdCard }}</el-descriptions-item>
          <el-descriptions-item label="房东姓名">{{ viewContractData.landlordName || '张房东' }}</el-descriptions-item>
          <el-descriptions-item label="房东电话">{{
              viewContractData.landlordPhone || '138****8888'
            }}
          </el-descriptions-item>
          <el-descriptions-item label="月租金">¥{{ viewContractData.monthlyRent }}</el-descriptions-item>
          <el-descriptions-item label="押金">¥{{ viewContractData.deposit }}</el-descriptions-item>
          <el-descriptions-item label="合同开始">{{ viewContractData.contractStartDate }}</el-descriptions-item>
          <el-descriptions-item label="合同结束">{{ viewContractData.contractEndDate }}</el-descriptions-item>
          <el-descriptions-item label="合同期限">{{ viewContractData.contractDuration }}个月</el-descriptions-item>
          <el-descriptions-item label="付款方式">{{ viewContractData.paymentMethod }}</el-descriptions-item>
          <el-descriptions-item label="水费单价">¥{{ viewContractData.waterRate || 3.5 }}/吨</el-descriptions-item>
          <el-descriptions-item label="电费单价">¥{{
              viewContractData.electricityRate || 0.6
            }}/度
          </el-descriptions-item>
          <el-descriptions-item label="包含水电费">{{
              viewContractData.utilitiesIncluded ? '包含' : '不包含'
            }}
          </el-descriptions-item>
          <el-descriptions-item label="合同状态">
            <el-tag :type="getStatusType(viewContractData.contractStatus)">
              {{ getStatusText(viewContractData.contractStatus) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 合同条款信息 -->
        <div v-if="viewContractData.contractTerms || viewContractData.specialAgreement || viewContractData.remarks"
             class="contract-additional-info">
          <div v-if="viewContractData.contractTerms" class="contract-terms">
            <h4>合同条款</h4>
            <p>{{ viewContractData.contractTerms }}</p>
          </div>

          <div v-if="viewContractData.specialAgreement" class="special-agreement">
            <h4>特殊约定</h4>
            <p>{{ viewContractData.specialAgreement }}</p>
          </div>

          <div v-if="viewContractData.remarks" class="contract-remarks">
            <h4>备注信息</h4>
            <p>{{ viewContractData.remarks }}</p>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="viewDialogVisible = false" size="large">关闭</el-button>
          <el-button type="primary" @click="printContract" size="large">
            <el-icon>
              <Printer/>
            </el-icon>
            打印合同
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, computed, onMounted, nextTick} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage, ElMessageBox, type FormInstance, type FormRules} from 'element-plus'
import {
  Plus, Download, Refresh, Search, View, Edit, Delete, Document,
  CircleCheck, Warning, CircleClose, Calendar, InfoFilled, User,
  Setting, Printer, Money, Right, Filter, OfficeBuilding, Avatar, Clock, CreditCard, RefreshRight, ArrowDown
} from '@element-plus/icons-vue'
import type {Contract, ContractQuery, ContractStats} from '../api/contracts'
import {
  getContracts, getContractStats, createContract, updateContract,
  deleteContract as deleteContractApi, getContractById, getContractsWithFilters,
  downloadContract as downloadContractApi, downloadContractPdf as downloadContractPdfApi
} from '../api/contracts'
import {getRentalInfoList, type RentalInfo} from '../api/rentalInfo'
import {getRentals} from '../api/rentals'

// 路由信息
const route = useRoute()
const currentFloor = computed(() => {
  const floor = route.params.floor as string
  console.log('路由楼层参数:', floor)
  // 如果没有楼层参数，默认为6楼
  return floor || '6'
})

// 响应式数据
const loading = ref(false)
const contractList = ref<Contract[]>([])
const stats = ref<ContractStats>({
  totalContracts: 0,
  activeContracts: 0,
  expiringContracts: 0,
  expiredContracts: 0
})

// 搜索表单
const searchForm = reactive<ContractQuery & { dateRange?: string[] }>({
  keyword: '',
  contractStatus: undefined,
  roomNumber: '',
  page: 0,
  size: 20,
  dateRange: undefined
})

// 新增数据
const searchKeyword = ref('')
const statusFilter = ref('')
const dateRange = ref<string[]>([])
const amountSort = ref('')

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
    isEditMode.value ? '编辑合同' : '新建合同'
)

// 合同表单
const contractFormRef = ref<FormInstance>()
const contractForm = reactive<Contract>({
  contractNumber: '',
  roomNumber: '',
  tenantName: '',
  tenantPhone: '',
  tenantIdCard: '',
  landlordName: '',
  landlordPhone: '',
  monthlyRent: 0,
  deposit: 0,
  contractStartDate: '',
  contractEndDate: '',
  contractDuration: 12,
  paymentMethod: '月付',
  rentDueDate: 1,
  contractStatus: 1,
  utilitiesIncluded: 0,
  waterRate: 3.5,
  electricityRate: 0.6,
  contractTerms: '',
  specialAgreement: '',
  remarks: ''
})

// 查看合同数据
const viewContractData = ref<Contract>({} as Contract)

// 已出租房间列表
const rentedRooms = ref<RentalInfo[]>([])
const loadingRooms = ref(false)

// 表单验证规则
const contractRules: FormRules = {
  contractNumber: [
    {required: true, message: '请输入合同编号', trigger: 'blur'}
  ],
  roomNumber: [
    {required: true, message: '请输入房间号', trigger: 'blur'}
  ],
  tenantName: [
    {required: true, message: '请输入租客姓名', trigger: 'blur'}
  ],
  tenantPhone: [
    {required: true, message: '请输入联系电话', trigger: 'blur'},
    {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
  ],
  tenantIdCard: [
    {required: true, message: '请输入身份证号', trigger: 'blur'}
  ],
  monthlyRent: [
    {required: true, message: '请输入月租金', trigger: 'blur'}
  ],
  deposit: [
    {required: true, message: '请输入押金', trigger: 'blur'}
  ],
  contractStartDate: [
    {required: true, message: '请选择合同开始日期', trigger: 'change'}
  ],
  contractEndDate: [
    {required: true, message: '请选择合同结束日期', trigger: 'change'}
  ]
}

// 获取状态类型
const getStatusType = (status: number) => {
  const statusMap: Record<number, string> = {
    1: 'success',  // 生效中
    2: 'danger',   // 已到期
    3: 'warning',  // 提前终止
    4: 'info'      // 续签
  }
  return statusMap[status] || 'info'
}

// 获取状态图标
const getStatusIcon = (status: number) => {
  const iconMap: Record<number, any> = {
    1: CircleCheck,
    2: CircleClose,
    3: Warning,
    4: Document
  }
  return iconMap[status] || Document
}

// 获取状态文本
const getStatusText = (status: number) => {
  const textMap: Record<number, string> = {
    1: '生效中',
    2: '已到期',
    3: '提前终止',
    4: '续签'
  }
  return textMap[status] || '未知'
}

// 新增方法
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

const filterByStatus = (status: string) => {
  // 将状态字符串映射为对应的数值
  const statusMap: Record<string, string> = {
    'all': '',       // 全部状态
    'active': '1',   // 生效中
    'expiring': '1', // 即将到期（也是生效中状态）
    'expired': '2',  // 已过期
    'terminated': '3' // 已终止
  }

  statusFilter.value = statusMap[status] || ''
  handleSearch()
}

const hasActiveFilters = computed(() => {
  return !!(searchKeyword.value || statusFilter.value || (dateRange.value && dateRange.value.length > 0) || amountSort.value)
})

const clearAllFilters = () => {
  searchKeyword.value = ''
  statusFilter.value = ''
  dateRange.value = []
  amountSort.value = ''
  handleSearch()
}

// 获取合同列表
const fetchContracts = async () => {
  loading.value = true
  try {
    // 处理合同状态参数，确保只传递有效数值
    let contractStatusParam = undefined
    if (statusFilter.value && statusFilter.value !== '') {
      const statusNum = Number(statusFilter.value)
      if (!isNaN(statusNum)) {
        contractStatusParam = statusNum
      }
    }

    // 处理日期范围参数
    let startDate = undefined
    let endDate = undefined
    if (dateRange.value && Array.isArray(dateRange.value) && dateRange.value.length === 2) {
      startDate = dateRange.value[0]
      endDate = dateRange.value[1]
    }

    // 清理参数，避免传递空字符串
    const cleanKeyword = searchKeyword.value?.trim()

    const params = {
      keyword: cleanKeyword || undefined,
      contractStatus: contractStatusParam,
      page: pagination.page - 1,
      size: pagination.size
    }

    console.log('搜索参数:', {
      floor: currentFloor.value,
      searchKeyword: searchKeyword.value,
      statusFilter: statusFilter.value,
      dateRange: dateRange.value,
      params
    })

    // 始终使用getContracts API，它已经支持keyword和contractStatus筛选
    const response = await getContracts(currentFloor.value, params) as any

    console.log('合同API响应:', response)
    if (response && response.code === 200) {
      contractList.value = response.data.content || []
      pagination.total = response.data.totalElements || 0
      console.log('✅ 成功加载合同数据，数量:', contractList.value.length, '总数:', pagination.total)
    } else {
      console.error('API响应错误:', response?.message)
      ElMessage.error('获取合同列表失败: ' + (response?.message || '未知错误'))
    }
  } catch (error) {
    console.error('获取合同列表异常:', error)
    ElMessage.error('获取合同列表失败: ' + (error as any)?.message || '网络错误')
  } finally {
    loading.value = false
  }
}

// 获取统计信息
const fetchStats = async () => {
  try {
    const response = await getContractStats(currentFloor.value) as any
    console.log('统计API响应:', response)
    if (response && response.code === 200) {
      stats.value = response.data
      console.log('✅ 成功加载统计数据:', stats.value)
    }
  } catch (error) {
    ElMessage.error('获取统计信息失败')
  }
}

// 搜索处理
const handleSearch = () => {
  console.log('🔍 开始搜索:', {
    searchKeyword: searchKeyword.value,
    statusFilter: statusFilter.value,
    currentFloor: currentFloor.value
  })
  pagination.page = 1
  fetchContracts()
}

// 测试搜索功能
const testSearch = (testKeyword: string) => {
  console.log('🧪 测试搜索:', testKeyword)
  searchKeyword.value = testKeyword
  handleSearch()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  fetchContracts()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  fetchContracts()
}

// 多选处理
const handleSelectionChange = (selection: Contract[]) => {
  // 处理批量操作
}

// 显示新建对话框
const showAddDialog = () => {
  isEditMode.value = false
  resetForm()
  generateContractNumber()
  fetchRentedRooms() // 获取已出租房间列表
  dialogVisible.value = true
}

// 查看合同
const viewContract = async (contract: Contract) => {
  try {
    const response = await getContractById(currentFloor.value, contract.id!) as any
    if (response && response.code === 200) {
      viewContractData.value = response.data
      viewDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取合同详情失败')
  }
}

// 编辑合同
const editContract = async (contract: Contract) => {
  try {
    const response = await getContractById(currentFloor.value, contract.id!) as any
    if (response && response.code === 200) {
      isEditMode.value = true
      currentEditId.value = contract.id
      Object.assign(contractForm, response.data)
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取合同详情失败')
  }
}

// 删除合同
const deleteContract = async (contract: Contract) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除合同 "${contract.contractNumber}" 吗？删除后将无法恢复！`,
        '确认删除',
        {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }
    )

    const response = await deleteContractApi(currentFloor.value, contract.id!) as any
    if (response && response.code === 200) {
      ElMessage.success('删除成功')
      fetchContracts()
      fetchStats()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 续签合同
const renewContract = (contract: Contract) => {
  ElMessage.info('续签功能开发中...')
}

// 下载状态管理
const downloadingContracts = ref<Set<number>>(new Set())

// 处理下载命令
const handleDownloadCommand = (command: string, contract?: Contract) => {
  if (!contract) return
  
  switch (command) {
    case 'download-text':
      downloadContract(contract)
      break
    case 'download-pdf':
      downloadContractPdf(contract)
      break
    case 'preview':
      previewContract(contract)
      break
    default:
      break
  }
}

// 预览合同内容
const previewContract = async (contract: Contract) => {
  try {
    console.log('👁️ 预览合同:', contract.contractNumber)
    ElMessage.info('正在加载合同内容...')
    
    // 这里可以实现预览功能，比如在对话框中显示合同内容
    // 暂时使用查看详情功能代替
    viewContract(contract)
    
  } catch (error) {
    console.error('❌ 预览合同失败:', error)
    ElMessage.error('预览合同失败')
  }
}

// 下载合同
const downloadContract = async (contract: Contract) => {
  if (!contract.id) {
    ElMessage.error('合同ID无效')
    return
  }
  
  // 防止重复下载
  if (downloadingContracts.value.has(contract.id)) {
    ElMessage.warning('合同正在下载中，请稍候...')
    return
  }
  
  try {
    console.log('🔽 开始下载合同:', contract.contractNumber)
    
    // 添加到下载中列表
    downloadingContracts.value.add(contract.id)
    
    // 显示加载提示
    const loadingMessage = ElMessage.info({
      message: '正在生成合同文件...',
      duration: 0, // 不自动关闭
      showClose: false
    })
    
    // 调用下载API
    const blob = await downloadContractApi(currentFloor.value, contract.id)
    
    // 关闭加载提示
    loadingMessage.close()
    
    // 检查blob大小
    if (blob.size === 0) {
      throw new Error('合同文件为空')
    }
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    
    // 设置文件名
    const fileName = `合同_${contract.contractNumber}_${contract.tenantName}.txt`
    link.download = fileName
    
    // 触发下载
    document.body.appendChild(link)
    link.click()
    
    // 清理
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    console.log('✅ 合同下载成功:', fileName)
    ElMessage.success(`合同 ${contract.contractNumber} 下载成功！`)
    
  } catch (error) {
    console.error('❌ 合同下载失败:', error)
    
    // 根据错误类型显示不同的提示
    let errorMessage = '合同下载失败'
    if ((error as any)?.message) {
      // 使用API返回的具体错误信息
      errorMessage = (error as any).message
    } else if ((error as any)?.response?.status === 404) {
      errorMessage = '合同不存在或已被删除'
    } else if ((error as any)?.response?.status === 500) {
      errorMessage = '服务器生成合同失败，请稍后重试'
    } else {
      errorMessage = '合同下载失败，请稍后重试'
    }
    
    ElMessage.error(errorMessage)
  } finally {
    // 从下载中列表移除
    downloadingContracts.value.delete(contract.id)
  }
}

// 下载合同PDF
const downloadContractPdf = async (contract: Contract) => {
  if (!contract.id) {
    ElMessage.error('合同ID无效')
    return
  }
  
  // 防止重复下载
  if (downloadingContracts.value.has(contract.id)) {
    ElMessage.warning('合同正在下载中，请稍候...')
    return
  }
  
  try {
    console.log('🔽 开始下载合同PDF:', contract.contractNumber)
    
    // 添加到下载中列表
    downloadingContracts.value.add(contract.id)
    
    // 显示加载提示
    const loadingMessage = ElMessage.info({
      message: '正在生成PDF文件...',
      duration: 0, // 不自动关闭
      showClose: false
    })
    
    // 调用PDF下载API
    const blob = await downloadContractPdfApi(currentFloor.value, contract.id)
    
    // 关闭加载提示
    loadingMessage.close()
    
    // 检查blob大小
    if (blob.size === 0) {
      throw new Error('PDF文件为空')
    }
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    
    // 设置文件名
    const fileName = `合同_${contract.contractNumber}_${contract.tenantName}.pdf`
    link.download = fileName
    
    // 触发下载
    document.body.appendChild(link)
    link.click()
    
    // 清理
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    console.log('✅ 合同PDF下载成功:', fileName)
    ElMessage.success(`合同PDF ${contract.contractNumber} 下载成功！`)
    
  } catch (error) {
    console.error('❌ 合同PDF下载失败:', error)
    
    // 根据错误类型显示不同的提示
    let errorMessage = '合同PDF下载失败'
    if ((error as any)?.message) {
      // 使用API返回的具体错误信息
      errorMessage = (error as any).message
    } else if ((error as any)?.response?.status === 404) {
      errorMessage = '合同不存在或已被删除'
    } else if ((error as any)?.response?.status === 500) {
      errorMessage = '服务器生成PDF失败，请稍后重试'
    } else {
      errorMessage = '合同PDF下载失败，请稍后重试'
    }
    
    ElMessage.error(errorMessage)
  } finally {
    // 从下载中列表移除
    downloadingContracts.value.delete(contract.id)
  }
}

// 导出合同
const exportContracts = () => {
  ElMessage.info('导出功能开发中...')
}

// 刷新列表
const refreshList = () => {
  fetchContracts()
  fetchStats()
}

// 生成合同编号
const generateContractNumber = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
  contractForm.contractNumber = `HT${year}${month}${day}${random}`
}

// 获取已出租房间列表
const fetchRentedRooms = async () => {
  try {
    loadingRooms.value = true
    const response = await getRentalInfoList(currentFloor.value, {
      page: 0,
      size: 1000, // 获取所有记录
      rentalStatus: 1 // 只获取已出租的房间
    }) as any
    if (response && response.code === 200) {
      rentedRooms.value = response.data.content || []
      console.log('已出租房间列表:', rentedRooms.value)
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
    contractForm.tenantName = ''
    contractForm.tenantPhone = ''
    contractForm.monthlyRent = 0
    contractForm.deposit = 0
    contractForm.contractStartDate = ''
    return
  }

  try {
    // 从已出租房间中找到对应的租客信息
    const selectedRoom = rentedRooms.value.find(room => room.roomNumber === roomNumber)
    if (selectedRoom) {
      // 自动填充租客信息
      contractForm.tenantName = selectedRoom.tenantName || ''
      contractForm.tenantPhone = selectedRoom.phone || ''
      contractForm.deposit = selectedRoom.deposit || 0

      // 设置合同开始日期为当天
      contractForm.contractStartDate = new Date().toISOString().split('T')[0]

      // 尝试从租房记录API获取月租金信息
      try {
        const rentalResponse = await getRentals(currentFloor.value, {
          keyword: roomNumber,
          page: 0,
          size: 1
        })

        if (rentalResponse.data && rentalResponse.data.code === 200) {
          const rentalData = rentalResponse.data.data.content
          if (rentalData && rentalData.length > 0) {
            const rental = rentalData.find(r => r.roomNumber === roomNumber)
            if (rental && rental.monthlyRent) {
              contractForm.monthlyRent = rental.monthlyRent
            }
          }
        }
      } catch (error) {
        console.warn('获取租金信息失败，可能需要手动输入:', error)
      }

      console.log('已自动填充租客信息:', {
        tenantName: contractForm.tenantName,
        tenantPhone: contractForm.tenantPhone,
        deposit: contractForm.deposit,
        monthlyRent: contractForm.monthlyRent,
        contractStartDate: contractForm.contractStartDate
      })

      ElMessage.success('已自动填充房间信息')
    } else {
      console.warn('未找到对应房间的租客信息:', roomNumber)
      ElMessage.warning('未找到该房间的租客信息')
    }
  } catch (error) {
    console.error('处理房间选择失败:', error)
    ElMessage.error('获取房间信息失败')
  }
}

// 重置表单
const resetForm = () => {
  if (contractFormRef.value) {
    contractFormRef.value.resetFields()
  }
  Object.assign(contractForm, {
    contractNumber: '',
    roomNumber: '',
    tenantName: '',
    tenantPhone: '',
    tenantIdCard: '',
    landlordName: '',
    landlordPhone: '',
    monthlyRent: 0,
    deposit: 0,
    contractStartDate: '',
    contractEndDate: '',
    contractDuration: 12,
    paymentMethod: '月付',
    rentDueDate: 1,
    contractStatus: 1,
    utilitiesIncluded: 0,
    waterRate: 3.5,
    electricityRate: 0.6,
    contractTerms: '',
    specialAgreement: '',
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
  if (!contractFormRef.value) return

  try {
    await contractFormRef.value.validate()

    if (isEditMode.value) {
      const response = await updateContract(currentFloor.value, currentEditId.value!, contractForm) as any
      if (response && response.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchContracts()
        fetchStats()
      }
    } else {
      const response = await createContract(currentFloor.value, contractForm) as any
      if (response && response.code === 200) {
        ElMessage.success('创建成功')
        dialogVisible.value = false
        fetchContracts()
        fetchStats()
      }
    }
  } catch (error) {
    ElMessage.error(isEditMode.value ? '更新失败' : '创建失败')
  }
}

// 打印合同
const printContract = () => {
  window.print()
}

// 初始化
onMounted(() => {
  fetchContracts()
  fetchStats()
})
</script>

<style scoped>
.contracts-management {
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

.modern-input,
.modern-select,
.modern-date-picker {
  border-radius: 12px;
}

.search-input {
  width: 100%;
}

.status-filter,
.amount-sort {
  width: 100%;
}

.search-input-wrapper {
  position: relative;
}

.search-icon {
  color: #909399;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12px;
  font-weight: 600;
}

.export-btn,
.refresh-btn {
  border-radius: 12px;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.export-btn:hover,
.refresh-btn:hover {
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

.modern-table :deep(.el-table__body-wrapper) {
  overflow-x: auto;
}

.modern-table :deep(.el-table__header-wrapper) {
  overflow-x: auto;
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

.contract-number-cell {
  display: flex;
  align-items: center;
}

.contract-tag {
  font-weight: 600;
  border-radius: 8px;
  padding: 6px 12px;
}

.room-tag {
  font-weight: 600;
  border-radius: 8px;
  font-size: 14px;
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

.landlord-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.landlord-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.landlord-details {
  flex: 1;
}

.landlord-name {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
  margin-bottom: 2px;
}

.landlord-phone {
  font-size: 12px;
  color: #909399;
}

.rent-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.rent-cell {
  display: flex;
  align-items: center;
  gap: 6px;
}

.money-icon {
  color: #67c23a;
  font-size: 16px;
}

.rent-amount {
  font-weight: 700;
  color: #67c23a;
  font-size: 16px;
}

.rent-period {
  font-size: 12px;
  color: #909399;
}

.deposit-cell {
  display: flex;
  align-items: center;
  gap: 6px;
}

.deposit-icon {
  font-size: 16px;
}

.deposit-amount {
  font-size: 14px;
  color: #f56c6c;
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

.period-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.period-divider {
  display: flex;
  justify-content: center;
  color: #c0c4cc;
}

.period-duration {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
}

.status-tag {
  font-weight: 600;
  border-radius: 12px;
  padding: 6px 12px;
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

.download-btn {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
  position: relative;
  overflow: hidden;
}

.download-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.download-btn.is-loading {
  pointer-events: none;
}

.download-btn.is-loading::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  animation: downloadShimmer 1.5s infinite;
}

@keyframes downloadShimmer {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}

.edit-btn {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border: none;
}

.delete-btn {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border: none;
}

.renew-btn {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
.contract-detail {
  max-height: 65vh;
  overflow-y: auto;
  padding-right: 8px;
}

.modern-descriptions {
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

.contract-additional-info {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.contract-terms,
.special-agreement,
.contract-remarks {
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e4e7ed;
}

.contract-terms {
  background: linear-gradient(135deg, #e8f5e8 0%, #f0f9ff 100%);
}

.special-agreement {
  background: linear-gradient(135deg, #fff7ed 0%, #fef3c7 100%);
}

.contract-remarks {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.contract-terms h4,
.special-agreement h4,
.contract-remarks h4 {
  margin: 0 0 12px 0;
  color: #303133;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.contract-terms h4::before {
  content: "📋";
  font-size: 16px;
}

.special-agreement h4::before {
  content: "⚠️";
  font-size: 16px;
}

.contract-remarks h4::before {
  content: "📝";
  font-size: 16px;
}

.contract-terms p,
.special-agreement p,
.contract-remarks p {
  margin: 0;
  color: #606266;
  line-height: 1.8;
  text-align: justify;
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

.create-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #606266;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .contracts-management {
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

  .action-buttons {
    flex-wrap: wrap;
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