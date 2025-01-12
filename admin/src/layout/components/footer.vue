<template>
  <!--用来显示网站备案信息-->
  <footer class="layout">
    <div class="text-center p-2 text-xs max-w-[900px] mx-auto text-[#979797]">
      <a
        class="mx-1 hover:underline"
        :href="item.link"
        v-for="(item, index) in copyright"
        :key="index"
      >
        {{ item.name }}
      </a>
    </div>
  </footer>
</template>

<script lang="ts" setup>
import useAppStore from '@/stores/modules/app.ts'

const appStore = useAppStore()

// 获取网站备案信息
const copyright = computed(
  () =>
    appStore.config['filings'] || [
      { link: 'https://winspace-bikes.com/', name: '银斯贝自行车' },
    ],
)
</script>
