<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh] pb-5">
      <van-nav-bar
        title="查询结果"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
      />
      <van-swipe indicator-color="white">
        <van-swipe-item>
          <van-image
            width="100vw"
            height="16em"
            fit="cover"
            src="https://winspace-bikes.com//images/master/2022/9/131-83EENS.png"
          />
        </van-swipe-item>
      </van-swipe>
      <div v-if="!pageErr" class="p-2">
        <van-collapse v-model="activeNames">
          <van-collapse-item title="查询结果" name="result">
            <van-row gutter="20">
              <van-col span="4">编号</van-col>
              <van-col span="20">{{ content.id }}</van-col>
            </van-row>
            <div class="h-2"></div>
            <van-row gutter="20">
              <van-col span="4">名称</van-col>
              <van-col span="20">{{ content.title }}</van-col>
            </van-row>
            <div class="h-2"></div>
            <van-row gutter="20">
              <van-col span="4">备注</van-col>
              <van-col span="20">
                {{ content.remark }}
              </van-col>
            </van-row>
          </van-collapse-item>
          <van-collapse-item title="图片信息" name="image">
            <div class="flex items-center flex-wrap p-3 gap-2 bg-page">
              <div class="w-full" v-for="(src, index) in images" :key="index">
                <van-image width="100%" fit="scale-down" :src="src" />
              </div>
            </div>
          </van-collapse-item>
        </van-collapse>
      </div>
      <div v-else class="p-2">
        <van-empty image="error" :description="pageErrMsg" />
      </div>
    </div>
  </van-config-provider>
</template>
<script lang="ts" setup>
import { useDark } from '@vueuse/core'
import { showLoadingToast, closeToast } from 'vant'
import { queryByQrcode } from '@/api/bicycle'

const isDark = useDark()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})

const images = ref<string[]>([])
const content = reactive({
  id: '',
  title: '',
  remark: '',
})

const activeNames = ref(['result', 'image'])
const router = useRouter()
const route = useRoute()
const qrcode = route.query.qrcode as string
const pageErr = ref(false)
const pageErrMsg = ref('')
// 返回上一页
const onClickLeft = () => {
  router.back()
}
// 根据当前二维码查询数据
const queryBicycleInfo = async () => {
  try {
    showLoadingToast({
      message: '加载中...',
      forbidClick: true,
      loadingType: 'spinner',
      duration: 0,
    })
    const data = await queryByQrcode({ qrcode })
    content.id = data.id
    content.title = data.title
    content.remark = data.remark
    images.value = data.image.split(';').filter((item) => !!item)
  } catch (e) {
    console.log('e ==>', e)
  } finally {
    closeToast()
  }
}
if (qrcode) {
  queryBicycleInfo()
} else {
  pageErr.value = true
  pageErrMsg.value = '无效二维码'
}
</script>
