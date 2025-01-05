import { ref } from 'vue'

/**
 * 函数防抖
 * args:调用方法时传递的参数
 * */
export function useLockFn(fn: (...args) => Promise<any>) {
  const isLock = ref(false)
  const lockFn = async (...args: Array<any>) => {
    // 函数防抖
    if (isLock.value) return
    isLock.value = true
    // 发起登录请求
    try {
      await fn(...args)
      isLock.value = false
    } catch (e) {
      isLock.value = false
      throw e
    }
  }
  return {
    isLock,
    lockFn,
  }
}
