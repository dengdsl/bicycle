<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh]">
    <van-nav-bar
        title="扫一扫" left-text="返回" left-arrow @click-left="onClickLeft" />
   <qr-scanner></qr-scanner>
  </div>
  </van-config-provider>
</template>
<script setup lang="ts">
import {useDark} from "@vueuse/core";


const isDark = useDark()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})

const router = useRouter()
const onClickLeft = () => router.back()
const onDecode = (data: any) => {
  console.log(data)
}
</script>