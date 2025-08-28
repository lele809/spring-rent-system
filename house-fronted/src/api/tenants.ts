import request from './request'

export interface Tenant {
  id: number
  name: string
  roomId: string
  phone: string
  idCard: string
  createdAt?: string
}

export interface TenantQuery {
  page?: number
  size?: number
  keyword?: string
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
 * 获取租客列表
 */
export const getTenants = (floor: string, query: TenantQuery = {}): Promise<ApiResponse<PageResponse<Tenant>>> => {
  const endpoint = floor === '6' ? '/tenants/new' : '/tenants/old'
  return request({
    url: endpoint,
    method: 'get',
    params: query
  })
}

/**
 * 搜索租客
 */
export const searchTenants = (floor: string, keyword: string): Promise<ApiResponse<Tenant[]>> => {
  const endpoint = floor === '6' ? '/tenants/new/search' : '/tenants/old/search'
  return request({
    url: endpoint,
    method: 'get',
    params: { keyword }
  })
}

/**
 * 获取租客详情
 */
export const getTenantById = (floor: string, id: number): Promise<ApiResponse<Tenant>> => {
  const endpoint = floor === '6' ? `/tenants/new/${id}` : `/tenants/old/${id}`
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 创建租客
 */
export const createTenant = (floor: string, tenant: Omit<Tenant, 'id' | 'createdAt'>): Promise<ApiResponse<Tenant>> => {
  const endpoint = floor === '6' ? '/tenants/new' : '/tenants/old'
  return request({
    url: endpoint,
    method: 'post',
    data: tenant
  })
}

/**
 * 更新租客
 */
export const updateTenant = (floor: string, id: number, tenant: Omit<Tenant, 'id' | 'createdAt'>): Promise<ApiResponse<Tenant>> => {
  const endpoint = floor === '6' ? `/tenants/new/${id}` : `/tenants/old/${id}`
  return request({
    url: endpoint,
    method: 'put',
    data: tenant
  })
}

/**
 * 删除租客
 */
export const deleteTenant = (floor: string, id: number): Promise<ApiResponse<string>> => {
  const endpoint = floor === '6' ? `/tenants/new/${id}` : `/tenants/old/${id}`
  return request({
    url: endpoint,
    method: 'delete'
  })
}
