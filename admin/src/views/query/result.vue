<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh] pb-5">
      <van-nav-bar
        title="查询结果"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
      />
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
      <div class="p-2">
        <van-collapse v-model="activeNames">
          <van-collapse-item title="查询结果" name="result">
            <van-row gutter="20">
              <van-col span="4">编号</van-col>
              <van-col span="20">BY1246546544546565</van-col>
            </van-row>
            <div class="h-2"></div>
            <van-row gutter="20">
              <van-col span="4">名称</van-col>
              <van-col span="20">山地超级自行车</van-col>
            </van-row>
            <div class="h-2"></div>
            <van-row gutter="20">
              <van-col span="4">备注</van-col>
              <van-col span="20">
                这是备注信息这是备注信息这是备注信息这是备注信息这是备注信息
              </van-col>
            </van-row>
          </van-collapse-item>
          <van-collapse-item title="图片信息" name="image">
            <div class="flex items-center flex-wrap p-3 gap-2 bg-page">
              <div class="w-full" v-for="item in 10" :key="item">
                <van-image
                  width="100%"
                  fit="scale-down"
                  src="https://th.bing.com/th/id/R.637dc1fa4dd61b5a77def88ca9c724e7?rik=2%2bSHCPYt6REKJA&riu=http%3a%2f%2fpic22.nipic.com%2f20120621%2f9793155_093521392138_2.jpg&ehk=MgxNA6jIhF5ztRTxNdLDYEB7%2f%2bNDkWTprveaVW9Izzg%3d&risl=&pid=ImgRaw&r=0"
                />
              </div>
            </div>
          </van-collapse-item>
        </van-collapse>
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

const isDark = useDark()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})

const activeNames = ref(['result'])
const router = useRouter()
// 返回上一页
const onClickLeft = () => {
  router.back()
}
</script>
