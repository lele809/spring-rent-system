import request from './request'

export interface Room {
  id: number
  roomNumber: string
  roomType: string
  baseRent: number
  deposit: number
  roomStatus: number
  waterMeterNumber?: string
  electricityMeterNumber?: string
  createdAt?: string
  updatedAt?: string
}

export interface RoomQuery {
  page?: number
  size?: number
  keyword?: string
  status?: number
  floor?: number
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

/**
 * 获取房间列表
 */
export const getRooms = (floor: string, query: RoomQuery = {}): Promise<ApiResponse<PageResponse<Room>>> => {
  const endpoint = floor === '6' ? '/rooms/new' : '/rooms/old'
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
 * 根据状态获取房间
 */
export const getRoomsByStatus = (floor: string, status: number): Promise<ApiResponse<Room[]>> => {
  const endpoint = floor === '6' ? `/rooms/new/status/${status}` : `/rooms/old/status/${status}`
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 搜索房间
 */
export const searchRooms = (floor: string, keyword: string): Promise<ApiResponse<Room[]>> => {
  const endpoint = floor === '6' ? '/rooms/new/search' : '/rooms/old/search'
  return request({
    url: endpoint,
    method: 'get',
    params: { keyword }
  })
}

/**
 * 创建房间
 */
export const createRoom = (floor: string, room: Partial<Room>): Promise<ApiResponse<Room>> => {
  const endpoint = floor === '6' ? '/rooms/new' : '/rooms/old'
  return request({
    url: endpoint,
    method: 'post',
    data: room
  })
}

/**
 * 更新房间
 */
export const updateRoom = (floor: string, id: number, room: Partial<Room>): Promise<ApiResponse<Room>> => {
  const endpoint = floor === '6' ? `/rooms/new/${id}` : `/rooms/old/${id}`
  return request({
    url: endpoint,
    method: 'put',
    data: room
  })
}

/**
 * 删除房间
 */
export const deleteRoom = (floor: string, id: number): Promise<ApiResponse<string>> => {
  const endpoint = floor === '6' ? `/rooms/new/${id}` : `/rooms/old/${id}`
  return request({
    url: endpoint,
    method: 'delete'
  })
}

/**
 * 根据ID获取房间详情
 */
export const getRoomById = (floor: string, id: number): Promise<ApiResponse<Room>> => {
  const endpoint = floor === '6' ? `/rooms/new/${id}` : `/rooms/old/${id}`
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 获取空闲房间列表
 */
export const getAvailableRooms = (floor: string) => {
  const endpoint = floor === '6' ? '/rooms/new/available' : '/rooms/old/available'
  return request({
    url: endpoint,
    method: 'get'
  })
} 