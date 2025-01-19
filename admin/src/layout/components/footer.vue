<template>
  <!--用来显示网站备案信息-->
  <footer class="layout">
    <div class="text-center p-2 text-xs max-w-[900px] mx-auto text-white">
      <a class="mx-1 hover:underline" :href="copyright.link" target="_blank">
        {{ copyright.name }}
      </a>
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
  }
})
</script>
