import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 字体大小选项
export const FONT_SIZES = {
  small: { label: '小', value: '14px', scale: 0.875 },
  medium: { label: '中', value: '16px', scale: 1 },
  large: { label: '大', value: '18px', scale: 1.125 },
  xlarge: { label: '特大', value: '20px', scale: 1.25 }
} as const

// 背景主题选项
export const BACKGROUND_THEMES = {
  default: {
    label: '默认渐变',
    value: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  blue: {
    label: '海洋蓝',
    value: 'linear-gradient(135deg, #74b9ff 0%, #0984e3 100%)'
  },
  green: {
    label: '森林绿',
    value: 'linear-gradient(135deg, #55efc4 0%, #00b894 100%)'
  },
  purple: {
    label: '紫罗兰',
    value: 'linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%)'
  },
  orange: {
    label: '日落橙',
    value: 'linear-gradient(135deg, #fdcb6e 0%, #e17055 100%)'
  },
  pink: {
    label: '樱花粉',
    value: 'linear-gradient(135deg, #fd79a8 0%, #e84393 100%)'
  },
  dark: {
    label: '深色模式',
    value: 'linear-gradient(135deg, #2d3436 0%, #636e72 100%)'
  },
  light: {
    label: '浅色模式',
    value: 'linear-gradient(135deg, #ddd6fe 0%, #e0e7ff 100%)'
  }
} as const

export type FontSizeKey = keyof typeof FONT_SIZES
export type BackgroundThemeKey = keyof typeof BACKGROUND_THEMES

export const useSettingsStore = defineStore('settings', () => {
  // 状态
  const fontSize = ref<FontSizeKey>('medium')
  const backgroundTheme = ref<BackgroundThemeKey>('default')

  // 计算属性
  const currentFontSize = computed(() => FONT_SIZES[fontSize.value])
  const currentBackgroundTheme = computed(() => BACKGROUND_THEMES[backgroundTheme.value])
  
  // CSS变量对象
  const cssVariables = computed(() => ({
    '--app-font-size': currentFontSize.value.value,
    '--app-font-scale': currentFontSize.value.scale.toString(),
    '--app-background': currentBackgroundTheme.value.value
  }))

  // 设置字体大小
  const setFontSize = (size: FontSizeKey) => {
    fontSize.value = size
    saveSettings()
    applySettings()
  }

  // 设置背景主题
  const setBackgroundTheme = (theme: BackgroundThemeKey) => {
    backgroundTheme.value = theme
    saveSettings()
    applySettings()
  }

  // 保存设置到本地存储
  const saveSettings = () => {
    const settings = {
      fontSize: fontSize.value,
      backgroundTheme: backgroundTheme.value
    }
    localStorage.setItem('app-settings', JSON.stringify(settings))
  }

  // 从本地存储加载设置
  const loadSettings = () => {
    try {
      const savedSettings = localStorage.getItem('app-settings')
      if (savedSettings) {
        const settings = JSON.parse(savedSettings)
        if (settings.fontSize && FONT_SIZES[settings.fontSize as FontSizeKey]) {
          fontSize.value = settings.fontSize
        }
        if (settings.backgroundTheme && BACKGROUND_THEMES[settings.backgroundTheme as BackgroundThemeKey]) {
          backgroundTheme.value = settings.backgroundTheme
        }
      }
    } catch (error) {
      console.error('加载设置失败:', error)
    }
  }

  // 应用设置到DOM
  const applySettings = () => {
    const root = document.documentElement
    Object.entries(cssVariables.value).forEach(([key, value]) => {
      root.style.setProperty(key, value)
    })
  }

  // 重置设置
  const resetSettings = () => {
    fontSize.value = 'medium'
    backgroundTheme.value = 'default'
    saveSettings()
    applySettings()
  }

  // 初始化设置
  const initSettings = () => {
    loadSettings()
    applySettings()
  }

  return {
    // 状态
    fontSize,
    backgroundTheme,
    
    // 计算属性
    currentFontSize,
    currentBackgroundTheme,
    cssVariables,
    
    // 方法
    setFontSize,
    setBackgroundTheme,
    saveSettings,
    loadSettings,
    applySettings,
    resetSettings,
    initSettings
  }
})