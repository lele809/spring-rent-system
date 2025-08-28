import request from './request'
import axios from 'axios'

export interface Contract {
  id?: number
  contractNumber: string
  roomNumber: string
  tenantName: string
  tenantPhone: string
  tenantIdCard: string
  landlordName?: string
  landlordPhone?: string
  monthlyRent: number
  deposit: number
  contractStartDate: string
  contractEndDate: string
  contractDuration: number
  paymentMethod?: string
  rentDueDate?: number
  contractStatus: number
  utilitiesIncluded?: number
  waterRate?: number
  electricityRate?: number
  contractTerms?: string
  specialAgreement?: string
  remarks?: string
  createdAt?: string
  updatedAt?: string
}

export interface ContractQuery {
  page?: number
  size?: number
  keyword?: string
  contractStatus?: number
  roomNumber?: string
  startDate?: string
  endDate?: string
  sortBy?: string
  sortDirection?: string
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

export interface ContractStats {
  totalContracts: number
  activeContracts: number
  expiringContracts: number
  expiredContracts: number
}

/**
 * 获取合同列表
 */
export const getContracts = (floor: string, query: ContractQuery = {}): Promise<ApiResponse<PageResponse<Contract>>> => {
  const endpoint = floor === '6' ? '/contracts/new' : '/contracts/old'
  return request({
    url: endpoint,
    method: 'get',
    params: {
      page: query.page || 0,
      size: query.size || 20,
      keyword: query.keyword,
      contractStatus: query.contractStatus,
      roomNumber: query.roomNumber
    }
  })
}

/**
 * 根据ID获取合同详情
 */
export const getContractById = (floor: string, id: number): Promise<ApiResponse<Contract>> => {
  const endpoint = floor === '6' ? `/contracts/new/${id}` : `/contracts/old/${id}`
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 根据状态获取合同列表
 */
export const getContractsByStatus = (floor: string, status: number): Promise<ApiResponse<Contract[]>> => {
  const endpoint = floor === '6' ? `/contracts/new/status/${status}` : `/contracts/old/status/${status}`
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 搜索合同（通过关键词搜索合同编号或租客姓名）
 */
export const searchContracts = (floor: string, keyword: string, query: ContractQuery = {}): Promise<ApiResponse<PageResponse<Contract>>> => {
  const endpoint = floor === '6' ? '/contracts/new/search' : '/contracts/old/search'
  return request({
    url: endpoint,
    method: 'get',
    params: {
      keyword,
      page: query.page || 0,
      size: query.size || 20
    }
  })
}

/**
 * 高级搜索合同（支持多种筛选条件）
 */
export const getContractsWithFilters = (floor: string, query: ContractQuery = {}): Promise<ApiResponse<PageResponse<Contract>>> => {
  const endpoint = floor === '6' ? '/contracts/new' : '/contracts/old'
  return request({
    url: endpoint,
    method: 'get',
    params: {
      page: query.page || 0,
      size: query.size || 20,
      keyword: query.keyword || undefined,
      contractStatus: query.contractStatus || undefined,
      roomNumber: query.roomNumber || undefined
    }
  })
}

/**
 * 创建合同
 */
export const createContract = (floor: string, contract: Contract): Promise<ApiResponse<Contract>> => {
  const endpoint = floor === '6' ? '/contracts/new' : '/contracts/old'
  return request({
    url: endpoint,
    method: 'post',
    data: contract
  })
}

/**
 * 更新合同
 */
export const updateContract = (floor: string, id: number, contract: Contract): Promise<ApiResponse<Contract>> => {
  const endpoint = floor === '6' ? `/contracts/new/${id}` : `/contracts/old/${id}`
  return request({
    url: endpoint,
    method: 'put',
    data: contract
  })
}

/**
 * 删除合同
 */
export const deleteContract = (floor: string, id: number): Promise<ApiResponse<void>> => {
  const endpoint = floor === '6' ? `/contracts/new/${id}` : `/contracts/old/${id}`
  return request({
    url: endpoint,
    method: 'delete'
  })
}

/**
 * 获取合同统计信息
 */
export const getContractStats = (floor: string): Promise<ApiResponse<ContractStats>> => {
  const endpoint = floor === '6' ? '/contracts/new/stats' : '/contracts/old/stats'
  return request({
    url: endpoint,
    method: 'get'
  })
}

/**
 * 获取即将到期的合同
 */
export const getExpiringContracts = (floor: string, days: number = 30): Promise<ApiResponse<Contract[]>> => {
  const endpoint = floor === '6' ? '/contracts/new/expiring' : '/contracts/old/expiring'
  return request({
    url: endpoint,
    method: 'get',
    params: { days }
  })
}

/**
 * 下载合同文件（文本格式）
 */
export const downloadContract = (floor: string, id: number): Promise<Blob> => {
  const endpoint = floor === '6' ? `/contracts/new/${id}/download` : `/contracts/old/${id}/download`
  // 直接使用axios避免响应拦截器的干扰
  return axios({
    url: `/api${endpoint}`,
    method: 'get',
    responseType: 'blob',
    timeout: 10000,
    headers: {
      'Accept': 'application/octet-stream'
    }
  }).then(response => {
    // 检查响应是否是blob
    if (!(response.data instanceof Blob)) {
      throw new Error('响应数据不是有效的文件格式')
    }
    return response.data
  }).catch(error => {
    console.error('下载合同API错误:', error)
    
    // 处理具体的错误情况
    if (error.response) {
      switch (error.response.status) {
        case 404:
          throw new Error('合同文件不存在')
        case 500:
          throw new Error('服务器生成合同失败')
        default:
          throw new Error(`下载失败: ${error.response.status}`)
      }
    } else if (error.request) {
      throw new Error('网络请求失败，请检查网络连接')
    } else {
      throw new Error('下载请求配置错误')
    }
  })
}

/**
 * 下载合同文件（PDF格式）
 */
export const downloadContractPdf = (floor: string, id: number): Promise<Blob> => {
  const endpoint = floor === '6' ? `/contracts/new/${id}/download-pdf` : `/contracts/old/${id}/download-pdf`
  // 直接使用axios避免响应拦截器的干扰
  return axios({
    url: `/api${endpoint}`,
    method: 'get',
    responseType: 'blob',
    timeout: 30000, // PDF生成可能需要更长时间
    headers: {
      'Accept': 'application/pdf'
    }
  }).then(response => {
    // 检查响应是否是blob
    if (!(response.data instanceof Blob)) {
      throw new Error('响应数据不是有效的PDF格式')
    }
    return response.data
  }).catch(error => {
    console.error('下载合同PDF错误:', error)
    
    // 处理具体的错误情况
    if (error.response) {
      switch (error.response.status) {
        case 404:
          throw new Error('合同文件不存在')
        case 500:
          throw new Error('服务器生成PDF失败')
        default:
          throw new Error(`PDF下载失败: ${error.response.status}`)
      }
    } else if (error.request) {
      throw new Error('网络请求失败，请检查网络连接')
    } else {
      throw new Error('PDF下载请求配置错误')
    }
  })
} 