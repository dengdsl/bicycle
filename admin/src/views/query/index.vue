<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh] pb-5">
      <van-swipe :autoplay="3000" indicator-color="white">
        <van-swipe-item>
          <van-image
            width="100vw"
            height="14em"
            fit="cover"
            src="https://th.bing.com/th/id/R.86f2e7d124f80302fc4dacbba58b6de8?rik=%2bZgGeku9HWrILA&riu=http%3a%2f%2fcdn.xuansiwei.com%2f9ybcvwp6099%2f1652800730379%2fMT50+(15).jpg%3fx-oss-process%3dstyle%2fsmall&ehk=w%2fqNMi%2fHfafcgDppB0TRBBLGsgEMoXmSfqT5cacXA%2fw%3d&risl=&pid=ImgRaw&r=0"
          />
        </van-swipe-item>
        <van-swipe-item>
          <van-image
            width="100vw"
            height="14em"
            fit="cover"
            src="https://th.bing.com/th/id/R.55cf957a44c7638166d4828e8e45e4b9?rik=gI71JD%2bHyMNFFw&riu=http%3a%2f%2fwenhui.whb.cn%2fu%2fcms%2fwww%2f202305%2f181907223xle.jpg&ehk=MtW6P7KDZGLGIs41uwE9rjLEF0YZ8EUtlNc2Na%2fIT%2fI%3d&risl=&pid=ImgRaw&r=0"
          />
        </van-swipe-item>
        <van-swipe-item>
          <van-image
            width="100vw"
            height="14em"
            fit="cover"
            src="https://th.bing.com/th/id/R.f84eb09a6910e37892c8fd8762528846?rik=5V%2fWHZVmtKQl8Q&riu=http%3a%2f%2fcdn.xuansiwei.com%2f9ybcvwp6099%2f1651907080600%2fECT+(10).jpg%3fx-oss-process%3dstyle%2fsmall&ehk=8f%2bD%2bIwee%2fW3NJUcpA6JhlX8eHZh2ObqenrSwZYIzcs%3d&risl=&pid=ImgRaw&r=0"
          />
        </van-swipe-item>
        <van-swipe-item>
          <van-image
            width="100vw"
            height="14em"
            fit="cover"
            src="https://th.bing.com/th/id/R.637dc1fa4dd61b5a77def88ca9c724e7?rik=2%2bSHCPYt6REKJA&riu=http%3a%2f%2fpic22.nipic.com%2f20120621%2f9793155_093521392138_2.jpg&ehk=MgxNA6jIhF5ztRTxNdLDYEB7%2f%2bNDkWTprveaVW9Izzg%3d&risl=&pid=ImgRaw&r=0"
          />
        </van-swipe-item>
      </van-swipe>
      <div
        class="flex items-center justify-center py-10 flex-col"
        @click="onScanClick"
      >
        <van-icon name="scan" size="100" color="#409eff" />
        <div class="text-primary font-bold text-center mt-1">点击扫码</div>
      </div>
      <div class="flex items-center justify-center px-4 gap-2">
        <van-field
          v-model="searchValue"
          input-align="center"
          placeholder="请输入条形码"
        />
        <van-button type="primary" @click="onClickButton">
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

const isDark = useDark()
const router = useRouter()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})
const searchValue = ref('')

// 打开手机摄像头扫码
const onScanClick = () => {}

// 发起查询
const onClickButton = () => {
  if (!searchValue.value) {
    showFailToast({
      message: '请输入条形码',
    })
    return
  }
  router.push(PageEnum.SEARCH_RESULT)
}
</script>
