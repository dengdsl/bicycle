import * as ElementPlusIcons from '@element-plus/icons-vue'
// @ts-ignore
import localIconsName from 'virtual:svg-icons-names'
// 本地图标前缀
export const LOCAL_ICON_PREFIX = 'local-icon-'
// element-plus图标前缀
export const EL_ICON_PREFIX = 'el-icon-'

const elIconsName: string[] = []

// 将element-plus中的图标全部拼接上前缀
for (const [, component] of Object.entries(ElementPlusIcons)) {
  elIconsName.push(`${EL_ICON_PREFIX}${component.name}`)
}

export function getElementPlusIconNames() {
  return elIconsName
}
export function getLocalIconNames() {
  return localIconsName
}
