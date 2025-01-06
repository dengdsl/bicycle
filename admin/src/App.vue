<template>
  <div>
    <el-config-provider :locale="elConfig.locale" :z-index="elConfig.zIndex">
        <router-view />
    </el-config-provider>
  </div>
</template>

<script setup lang="ts">
import zhCn from 'element-plus/dist/locale/zh-cn.js'
import { useWindowSize, useThrottleFn, useDark } from '@vueuse/core'
import { ScreenEnum } from '@/enums/MenuEnum'
import useAppStore from '@/stores/modules/app'
import useSettingStore from '@/stores/modules/setting'

const elConfig = {
  zIndex: 3000,
  locale: zhCn,
}

const appStore = useAppStore()
const settingStore = useSettingStore()

// 获取窗口宽度
const { width } = useWindowSize()

const isDark = useDark()
onMounted(async () => {
  // 设置当前主题色
  settingStore.setTheme(isDark.value)
  // 获取引用配置信息
  const data: any = await appStore.getConfig()
  // 设置网站logo
  let favicon: HTMLLinkElement = document.querySelector("link[rel='icon']")
  // 如果已经存在该标签直接修改地址即可
  if (favicon) {
    favicon.href = data.webFavicon
    return
  }
  // 该标签不存在,自动创建标签
  if (data.webFavicon) {
    favicon = document.createElement('link')
    favicon.rel = 'link'
    favicon.href = data.webFavicon
    document.head.append(favicon)
  }
})

// 监听窗口的宽度变化
watch(
  width,
  useThrottleFn((value) => {
    if (value > ScreenEnum.SM) {
      appStore.setMobile(false)
      appStore.toggleCollapsed(false)
    } else {
      appStore.setMobile(true)
      appStore.toggleCollapsed(true)
    }
    if (value < ScreenEnum.MD) {
      appStore.toggleCollapsed(true)
    }
  }),
  {
    immediate: true,
  },
)
</script>
<style scoped></style>
