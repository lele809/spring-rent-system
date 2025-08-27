/**
 * 错误处理工具
 */

/**
 * 检查DOM元素是否存在且有效
 */
export const isDOMElementValid = (element: Element | null): boolean => {
  return element !== null && 
         element.parentElement !== null && 
         element.isConnected !== false
}

/**
 * 安全的DOM操作包装器
 */
export const safeDOMOperation = <T>(
  operation: () => T,
  fallback?: T,
  errorMessage?: string
): T | undefined => {
  try {
    return operation()
  } catch (error) {
    console.warn(errorMessage || 'DOM操作失败:', error)
    return fallback
  }
}

/**
 * 安全的异步操作包装器
 */
export const safeAsyncOperation = async <T>(
  operation: () => Promise<T>,
  fallback?: T,
  errorMessage?: string
): Promise<T | undefined> => {
  try {
    return await operation()
  } catch (error) {
    console.warn(errorMessage || '异步操作失败:', error)
    return fallback
  }
}

/**
 * 防抖函数
 */
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: ReturnType<typeof setTimeout>
  
  return (...args: Parameters<T>) => {
    clearTimeout(timeout)
    timeout = setTimeout(() => func.apply(null, args), wait)
  }
}

/**
 * 组件状态管理器
 */
export class ComponentStateManager {
  private destroyed = false
  private mounted = false
  
  setMounted(mounted: boolean) {
    this.mounted = mounted
    if (mounted) {
      this.destroyed = false
    }
  }
  
  setDestroyed(destroyed: boolean) {
    this.destroyed = destroyed
    if (destroyed) {
      this.mounted = false
    }
  }
  
  isDestroyed(): boolean {
    return this.destroyed
  }
  
  isMounted(): boolean {
    return this.mounted && !this.destroyed
  }
  
  /**
   * 安全执行操作（仅在组件未销毁时执行）
   */
  safeExecute<T>(operation: () => T, errorMessage?: string): T | undefined {
    if (this.destroyed) {
      console.warn('组件已销毁，跳过操作:', errorMessage || 'unknown operation')
      return undefined
    }
    
    try {
      return operation()
    } catch (error) {
      console.warn(errorMessage || '操作失败:', error)
      return undefined
    }
  }
}

/**
 * 检查是否为parentElement相关错误
 */
export const isParentElementError = (error: any): boolean => {
  return error instanceof TypeError && 
         typeof error.message === 'string' && 
         error.message.includes('parentElement')
}

/**
 * 全局错误过滤器
 */
export const shouldIgnoreError = (error: any): boolean => {
  // 忽略parentElement相关错误
  if (isParentElementError(error)) {
    return true
  }
  
  // 忽略其他已知的无害错误
  if (error instanceof TypeError) {
    const message = error.message.toLowerCase()
    const ignoredMessages = [
      'cannot read properties of null',
      'cannot read property of null',
      'null is not an object'
    ]
    
    return ignoredMessages.some(ignored => 
      message.includes(ignored) && message.includes('parentelement')
    )
  }
  
  return false
} 