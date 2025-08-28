import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Admin } from '../api/auth'

export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref<Admin | null>(null)
  const isLoggedIn = ref(false)

  // 登录
  const setUser = (userData: Admin) => {
    user.value = userData
    isLoggedIn.value = true
    localStorage.setItem('user', JSON.stringify(userData))
  }

  // 登出
  const logout = () => {
    user.value = null
    isLoggedIn.value = false
    localStorage.removeItem('user')
  }

  // 初始化用户信息
  const initUser = () => {
    const userData = localStorage.getItem('user')
    if (userData) {
      try {
        const parsedUser = JSON.parse(userData)
        user.value = parsedUser
        isLoggedIn.value = true
      } catch (error) {
        console.error('解析用户信息失败:', error)
        logout()
      }
    }
  }

  return {
    user,
    isLoggedIn,
    setUser,
    logout,
    initUser
  }
})