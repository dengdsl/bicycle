<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh]">
      <div style="width: 100vw" :style="{ height: `${100 / (4 / 3)}vw` }">
        <van-swipe indicator-color="white" :autoplay="autoPlay">
          <van-swipe-item v-for="(item, index) in bannerImages" :key="index">
            <van-image
              width="100vw"
              :height="`${100 / (4 / 3)}vw`"
              position="center"
              :src="item.src"
            />
          </van-swipe-item>
        </van-swipe>
      </div>
      <div
        class="flex items-center justify-center py-5 flex-col"
        @click="onScanClick"
      >
        <van-icon name="scan" size="100" :color="appStore.config.mobileTheme" />
        <div class="text-mobilePrimary font-bold text-center mt-1">
          点击扫码
        </div>
      </div>
      <div class="flex items-center justify-center px-4 gap-2">
        <van-field
          v-model="searchValue"
          input-align="center"
          placeholder="产品编码/二维码编码"
        />
      </div>
      <div class="flex items-center justify-center px-4 mt-5">
        <van-button type="primary" block @click="onClickButton">
          <span class="text-nowrap px-4">查询</span>
        </van-button>
      </div>
      <!--<div-->
      <!--  class="flex items-center justify-center fixed left-[50%] bottom-1 -translate-x-[50%]"-->
      <!--&gt;-->
      <!--  <a class="text-xs text-mobilePrimary">备案链接</a>-->
      <!--</div>-->
    </div>
  </van-config-provider>
</template>
<script lang="ts" setup>
import { useDark } from '@vueuse/core'
import { PageEnum } from '@/enums/pageEnum.ts'
import useAppStore from '@/stores/modules/app.ts'
import { getConfig } from '@/api/config'
import { ref } from 'vue'

const appStore = useAppStore()

const isDark = useDark()
const router = useRouter()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})
const searchValue = ref('')
const autoPlay = ref(0)
const bannerImages = ref<Array<{ src: string; sort: number }>>([])

// 打开手机摄像头扫码
const onScanClick = () => {
  router.push(PageEnum.SCAN_PAGE)
}

// 发起查询
const onClickButton = () => {
  if (!searchValue.value) {
    showFailToast({
      message: '请输入条形码',
    })
    return
  }
  router.push({
    path: PageEnum.SEARCH_RESULT,
    query: { qrcode: searchValue.value },
  })
}
// 获取轮播图配置信息
const getBannerConfig = async () => {
  try {
    const config = await getConfig(['autoplay', 'bannerImgs'])
    if (config.autoplay) {
      autoPlay.value = parseInt(config.autoplay)
    }
    if (config.bannerImgs) {
      bannerImages.value = JSON.parse(config.bannerImgs)
      bannerImages.value.sort((a, b) => a.sort - b.sort)
    }
  } catch (err) {
    showFailToast({
      message: '获取轮播图配置信息失败',
    })
  }
}
getBannerConfig()
</script>
