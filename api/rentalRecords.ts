import request from './request'

// 缴费记录查询参数接口
export interface RentalRecordsQuery {
  keyword?: string
  startDate?: string
  endDate?: string
  monthFilter?: string
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: string
}

// 缴费记录接口（前端使用）
export interface RentalRecord {
  id?: number
  roomNumber: string
  tenantName: string
  totalRent: number
  paymentDate: string
  createdAt?: string
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

/**
 * 获取缴费记录列表（分页）
 */
export const getRentalRecords = (floor: string, params: RentalRecordsQuery) => {
  return request.get<PageResponse<RentalRecord>>(`/rental-records/${floor}`, { params })
}

/**
 * 导出缴费记录
 */
export const exportRentalRecords = (floor: string, params: RentalRecordsQuery) => {
  return request.get<RentalRecord[]>(`/rental-records/${floor}/export`, { params })
}

/**
 * 新增缴费记录（新楼）
 */
export const addNewRentalRecord = (record: RentalRecord) => {
  return request.post<RentalRecord>('/rental-records/new', record)
}

/**
 * 新增缴费记录（旧楼）
 */
export const addOldRentalRecord = (record: RentalRecord) => {
  return request.post<RentalRecord>('/rental-records/old', record)
}

/**
 * 删除缴费记录
 */
export const deleteRentalRecord = (floor: string, id: number) => {
  return request.delete(`/rental-records/${floor}/${id}`)
}

/**
 * 根据房号获取缴费记录
 */
export const getRentalRecordsByRoom = (floor: string, roomNumber: string) => {
  return request.get<RentalRecord[]>(`/rental-records/${floor}/room/${roomNumber}`)
}

/**
 * 根据租客姓名获取缴费记录
 */
export const getRentalRecordsByTenant = (floor: string, tenantName: string) => {
  return request.get<RentalRecord[]>(`/rental-records/${floor}/tenant/${tenantName}`)
}

/**
 * 获取月度缴费统计
 */
export const getMonthlyTotal = (floor: string, year: number, month: number) => {
  return request.get<number>(`/rental-records/${floor}/monthly-total`, {
    params: { year, month }
  })
}

/**
 * 根据日期范围获取缴费记录
 */
export const getRentalRecordsByDateRange = (floor: string, startDate: string, endDate: string) => {
  return request.get<RentalRecord[]>(`/rental-records/${floor}/date-range`, {
    params: { startDate, endDate }
  })
} 