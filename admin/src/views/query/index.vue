<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh] pb-5">
      <van-swipe indicator-color="white">
        <van-swipe-item>
          <van-image
            width="100vw"
            height="16em"
            fit="cover"
            src="https://winspace-bikes.com//images/master/2022/3/79-6m3ktG.png"
          />
        </van-swipe-item>
      </van-swipe>
      <div
        class="flex items-center justify-center py-5 flex-col"
        @click="onScanClick"
      >
        <van-icon name="scan" size="100" :color="defaultSetting.theme" />
        <div class="text-primary font-bold text-center mt-1">点击扫码</div>
      </div>
      <div class="flex items-center justify-center px-4 gap-2">
        <van-field
          v-model="searchValue"
          input-align="center"
          placeholder="请输入条形码"
        />
      </div>
      <div class="flex items-center justify-center px-4 mt-5">
        <van-button type="primary" block @click="onClickButton">
          <span class="text-nowrap px-4">查询</span>
        </van-button>
      </div>
      <div
        class="flex items-center justify-center fixed left-[50%] bottom-1 -translate-x-[50%]"
      >
        <a class="text-xs text-primary">备案链接</a>
      </div>
    </div>
  </van-config-provider>
</template>
<script lang="ts" setup>
import { useDark } from '@vueuse/core'
import { PageEnum } from '@/enums/pageEnum.ts'
import defaultSetting from '@/config/setting.ts'

const isDark = useDark()
const router = useRouter()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})
const searchValue = ref('')

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
</script>
