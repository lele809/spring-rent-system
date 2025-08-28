import request from './request'

// 费用查询参数接口
export interface PaymentQuery {
  keyword?: string
  paymentStatus?: number
  feeType?: string
  startDate?: string
  endDate?: string
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: string
}

// 费用统计信息接口
export interface PaymentStats {
  totalRentIncome: number
  totalUtilitiesIncome: number
  totalPendingAmount: number
  totalOverdueAmount: number
  paidCount: number
  unpaidCount: number
  totalCount: number
  monthlyIncome: number
  paymentRate: number
}

// 租房记录接口（通用）
export interface RentalRecord {
  id?: number
  roomNumber: string
  tenantName: string
  deposit: number
  monthlyRent: number
  waterFee?: number
  electricityFee?: number
  utilitiesFee?: number
  totalDue?: number
  paymentStatus: number
  checkInDate?: string
  checkOutDate?: string
  contractStartDate?: string
  contractEndDate?: string
  remarks?: string
  createdAt?: string
  updatedAt?: string
}

// 分页响应接口
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

// API响应接口
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 每月收入数据接口
export interface MonthlyIncomeData {
  months: string[]
  incomes: number[]
  currentMonthIncome: number
  previousMonthIncome: number
  growthRate: number
  averageIncome: number
  totalIncome: number
}

/**
 * 获取费用列表（分页）
 */
export const getPayments = (floor: string, params: PaymentQuery) => {
  return request.get<ApiResponse<PageResponse<RentalRecord>>>(`/rentals/${floor}`, {
    params
  })
}

/**
 * 获取费用详情
 */
export const getPaymentById = (floor: string, id: number) => {
  return request.get<ApiResponse<RentalRecord>>(`/rentals/${floor}/${id}`)
}

/**
 * 创建费用记录
 */
export const createPayment = (floor: string, data: RentalRecord) => {
  return request.post<ApiResponse<RentalRecord>>(`/rentals/${floor}`, data)
}

/**
 * 更新费用记录
 */
export const updatePayment = (floor: string, id: number, data: RentalRecord) => {
  return request.put<ApiResponse<RentalRecord>>(`/rentals/${floor}/${id}`, data)
}

/**
 * 删除费用记录
 */
export const deletePayment = (floor: string, id: number) => {
  return request.delete<ApiResponse<string>>(`/rentals/${floor}/${id}`)
}

/**
 * 获取费用统计信息
 */
export const getPaymentStats = (floor: string) => {
  return request.get<ApiResponse<PaymentStats>>(`/rentals/${floor}/stats`)
}

/**
 * 更新支付状态
 */
export const updatePaymentStatus = (floor: string, id: number, status: number) => {
  return request.patch<ApiResponse<string>>(`/rentals/${floor}/${id}/status`, {
    status
  })
}

/**
 * 批量更新支付状态
 */
export const batchUpdatePaymentStatus = (floor: string, ids: number[], status: number) => {
  return request.patch<ApiResponse<string>>(`/rentals/${floor}/batch/status`, {
    ids,
    status
  })
}

/**
 * 导出费用数据
 */
export const exportPayments = (floor: string, params: PaymentQuery) => {
  return request.get<ApiResponse<RentalRecord[]>>(`/rentals/${floor}/export`, {
    params
  })
}

/**
 * 获取即将到期的合同
 */
export const getExpiringContracts = (floor: string, days: number = 30) => {
  return request.get<ApiResponse<RentalRecord[]>>(`/rentals/${floor}/expiring`, {
    params: { days }
  })
}

/**
 * 获取每月收入趋势数据
 */
export const getMonthlyIncome = (floor: string, months: number = 6) => {
  return request.get<ApiResponse<MonthlyIncomeData>>(`/rentals/${floor}/monthly-income`, {
    params: { months }
  })
}