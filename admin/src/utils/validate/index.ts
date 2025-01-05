/**
 * 检查路由地址是否是外链地址
 * */
export function isExternal(path: string) {
  return /^(https?:|mailto:|tel:)/.test(path)
}
