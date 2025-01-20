<template>
  <!--用来显示网站备案信息-->
  <footer class="layout relative">
    <div
      class="absolute bottom-0 left-0 w-full text-center p-2 text-xs mx-auto text-white z-10"
    >
      <a class="mx-1 hover:underline" :href="copyright.link" target="_blank">
        {{ copyright.name }}
      </a>
    </div>
    <div
      class="absolute bottom-0 left-0 w-full flex items-center justify-center z-0"
    >
      <div class="max-w-[1920px] z-1 p-1">
        <img
          v-if="copyright.loginFooterBg"
          :src="copyright.loginFooterBg"
          class="w-full h-auto"
        />
      </div>
    </div>
  </footer>
</template>

<script lang="ts" setup>
import useAppStore from '@/stores/modules/app.ts'

const appStore = useAppStore()

// 获取网站备案信息
const copyright = computed(() => {
  return {
    link: appStore.config['filings'],
    name: appStore.config['filingsName'],
    loginFooterBg: appStore.config['loginFooterBg'],
  }
})
</script>
