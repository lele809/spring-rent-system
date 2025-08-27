import axios from 'axios'
import type {AxiosInstance, AxiosResponse, InternalAxiosRequestConfig} from 'axios'
import {ElMessage} from 'element-plus'

// 创建axios实例
const request: AxiosInstance = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
request.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        // 在这里可以添加token等认证信息
        // const token = localStorage.getItem('token')
        // if (token) {
        //   config.headers.Authorization = `Bearer ${token}`
        // }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    (response: AxiosResponse) => {
        // 返回response.data，让调用方直接使用数据
        return response.data
    },
    (error) => {
        let message = '网络错误'

        if (error.response) {
            switch (error.response.status) {
                case 401:
                    message = '未授权，请重新登录'
                    break
                case 403:
                    message = '拒绝访问'
                    break
                case 404:
                    message = '请求地址出错'
                    break
                case 500:
                    message = '服务器内部错误'
                    break
                default:
                    message = error.response.data?.message || '请求失败'
            }
        }

        ElMessage.error(message)
        return Promise.reject(error)
    }
)

export default request