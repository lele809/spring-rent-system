import request from './request'

// 租房信息接口
export interface RentalInfo {
  id?: number
  roomNumber: string
  tenantName: string
  phone: string
  deposit: number
  occupantCount: number
  checkInDate: string
  rentalStatus: number
  remarks?: string
  createdAt?: string
  updatedAt?: string
}

// 租房信息查询参数
export interface RentalInfoQuery {
  page?: number
  size?: number
  keyword?: string
  rentalStatus?: number
}

// API响应接口
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 分页响应接口
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

/**
 * 获取租房信息列表
 */
export const getRentalInfoList = (floor: string, query: RentalInfoQuery = {}): Promise<ApiResponse<PageResponse<RentalInfo>>> => {
  const endpoint = floor === '6' ? '/rental-info/new' : '/rental-info/old'
  return request({
    url: endpoint,
    method: 'get',
    params: {
      page: query.page || 0,
      size: query.size || 20,
      ...query
    }
  })
}

/**
 * 根据ID获取租房信息详情
 */
export const getRentalInfoById = (floor: string, id: number): Promise<ApiResponse<RentalInfo>> => {
  const endpoint = floor === '6' ? `/rental-info/new/${id}` : `/rental-info/old/${id}`
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 创建租房信息
 */
export const createRentalInfo = (floor: string, data: Partial<RentalInfo>): Promise<ApiResponse<RentalInfo>> => {
  const endpoint = floor === '6' ? '/rental-info/new' : '/rental-info/old'
  return request({
    url: endpoint,
    method: 'post',
    data
  })
}

/**
 * 更新租房信息
 */
export const updateRentalInfo = (floor: string, id: number, data: Partial<RentalInfo>): Promise<ApiResponse<RentalInfo>> => {
  const endpoint = floor === '6' ? `/rental-info/new/${id}` : `/rental-info/old/${id}`
  return request({
    url: endpoint,
    method: 'put',
    data
  })
}

/**
 * 删除租房信息
 */
export const deleteRentalInfo = (floor: string, id: number): Promise<ApiResponse<string>> => {
  const endpoint = floor === '6' ? `/rental-info/new/${id}` : `/rental-info/old/${id}`
  return request({
    url: endpoint,
    method: 'delete'
  })
}

/**
 * 获取可用房间列表
 */
export const getAvailableRooms = (floor: string): Promise<ApiResponse<any[]>> => {
  return request({
    url: `/rental-info/${floor}/available-rooms`,
    method: 'get'
  })
}

/**
 * 搜索租房信息
 */
export const searchRentalInfos = (floor: string, keyword?: string, rentalStatus?: number, page = 0, size = 20): Promise<ApiResponse<PageResponse<RentalInfo>>> => {
  const endpoint = floor === '6' ? '/rental-info/new' : '/rental-info/old'
  return request({
    url: endpoint,
    method: 'get',
    params: {
      page,
      size,
      keyword,
      rentalStatus
    }
  })
}

/**
 * 获取租房信息统计
 */
export const getRentalInfoStats = (floor: string): Promise<ApiResponse<any>> => {
  return request({
    url: `/rental-info/${floor}/stats`,
    method: 'get'
  })
}