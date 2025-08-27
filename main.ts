import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

// Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// Element Plus 中文语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// 自定义全局样式
import './assets/styles/global.css'

// 创建应用实例
const app = createApp(App)

// 全局错误处理器
app.config.errorHandler = (err, instance, info) => {
  console.warn('Vue全局错误处理器:', err, info)
  
  // 特别处理DOM相关错误
  if (err instanceof TypeError && err.message.includes('parentElement')) {
    console.warn('捕获到parentElement错误，已忽略:', err)
    return // 忽略这个错误，防止应用崩溃
  }
  
  // 其他错误继续抛出
  console.error('应用错误:', err)
}

// 捕获未处理的Promise拒绝
window.addEventListener('unhandledrejection', (event) => {
  console.warn('未处理的Promise拒绝:', event.reason)
  
  // 如果是DOM相关错误，阻止默认处理
  if (event.reason instanceof TypeError && 
      event.reason.message.includes('parentElement')) {
    console.warn('忽略parentElement Promise错误')
    event.preventDefault()
  }
})

// 捕获全局JavaScript错误
window.addEventListener('error', (event) => {
  if (event.error instanceof TypeError && 
      event.error.message.includes('parentElement')) {
    console.warn('忽略parentElement全局错误:', event.error)
    event.preventDefault()
    return false
  }
})

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用插件
app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
})

// 挂载应用
app.mount('#app')