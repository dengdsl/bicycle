/**
 * 获取本地缓存中的数据
 * */
function getItem(key: string) {
  return localStorage.getItem(key)
}

/**
 * 删除本地缓存
 * */
function removeItem(key: string) {
  localStorage.removeItem(key)
}

/**
 * 设置数据到本地缓存中
 * */
function setItem(key: string, value: any) {
  localStorage.setItem(
    key,
    typeof value === 'object' ? JSON.stringify(value) : value,
  )
}

export default {
  getItem,
  setItem,
  removeItem,
}
