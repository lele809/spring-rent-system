import request from './request'

// 租房查询参数接口
export interface RentalQuery {
  keyword?: string
  paymentStatus?: number
  feeType?: string
  startDate?: string
  endDate?: string
  monthFilter?: string
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
  tenantPhone?: string
  deposit: number
  monthlyRent: number
  waterUsage?: number
  electricityUsage?: number
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

/**
 * 获取租房记录列表（分页）
 */
export const getRentals = (floor: string, params: RentalQuery) => {
  return request.get<ApiResponse<PageResponse<RentalRecord>>>(`/rentals/${floor}`, {
    params
  })
}

/**
 * 获取租房记录详情
 */
export const getRentalById = (floor: string, id: number) => {
  return request.get<ApiResponse<RentalRecord>>(`/rentals/${floor}/${id}`)
}

/**
 * 创建租房记录
 */
export const createRental = (floor: string, data: RentalRecord) => {
  return request.post<ApiResponse<RentalRecord>>(`/rentals/${floor}`, data)
}

/**
 * 更新租房记录
 */
export const updateRental = (floor: string, id: number, data: RentalRecord) => {
  return request.put<ApiResponse<RentalRecord>>(`/rentals/${floor}/${id}`, data)
}

/**
 * 删除租房记录
 */
export const deleteRental = (floor: string, id: number) => {
  return request.delete<ApiResponse<string>>(`/rentals/${floor}/${id}`)
}

/**
 * 获取费用统计信息
 */
export const getRentalStats = (floor: string) => {
  return request.get<ApiResponse<PaymentStats>>(`/rentals/${floor}/stats`)
}

/**
 * 更新支付状态
 */
export const updateRentalStatus = (floor: string, id: number, status: number) => {
  return request.patch<ApiResponse<string>>(`/rentals/${floor}/${id}/status`, {
    status
  })
}

/**
 * 批量更新支付状态
 */
export const batchUpdateRentalStatus = (floor: string, ids: number[], status: number) => {
  return request.patch<ApiResponse<string>>(`/rentals/${floor}/batch/status`, {
    ids,
    status
  })
}

/**
 * 导出租房数据
 */
export const exportRentals = (floor: string, params: RentalQuery) => {
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

// 为了向后兼容，保留payments相关导出
export const getPayments = getRentals
export const getPaymentById = getRentalById
export const createPayment = createRental
export const updatePayment = updateRental
export const deletePayment = deleteRental
export const getPaymentStats = getRentalStats
export const updatePaymentStatus = updateRentalStatus
export const batchUpdatePaymentStatus = batchUpdateRentalStatus
export const exportPayments = exportRentals

// 为了向后兼容，导出PaymentQuery类型
export type PaymentQuery = RentalQuery 