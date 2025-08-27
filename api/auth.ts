import request from './request'

export interface LoginRequest {
  adminName: string
  password: string
}

export interface Admin {
  id: number
  adminName: string
  lastLogin: string
  password?: string
}

export interface AdminFormData {
  id?: number
  adminName: string
  password: string
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 用户登录
 */
export const login = (data: LoginRequest): Promise<ApiResponse<Admin>> => {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 创建管理员（测试用）
 */
export const createAdmin = (adminName: string, password: string): Promise<ApiResponse<string>> => {
  return request({
    url: '/auth/create-admin',
    method: 'post',
    params: { adminName, password }
  })
}

/**
 * 测试API
 */
export const testApi = (): Promise<ApiResponse<string>> => {
  return request({
    url: '/auth/test',
    method: 'get'
  })
}

/**
 * 获取管理员列表
 */
export const getAdminList = (): Promise<ApiResponse<Admin[]>> => {
  return request({
    url: '/auth/admin-list',
    method: 'get'
  })
}

/**
 * 更新管理员信息
 */
export const updateAdmin = (id: number, data: AdminFormData): Promise<ApiResponse<string>> => {
  return request({
    url: `/auth/update-admin/${id}`,
    method: 'post',
    data
  })
}

/**
 * 删除管理员
 */
export const deleteAdmin = (id: number): Promise<ApiResponse<string>> => {
  return request({
    url: `/auth/delete-admin/${id}`,
    method: 'post'
  })
}