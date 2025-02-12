<template>
  <van-config-provider :theme="theme">
    <div class="bg-page min-h-[100vh]">
      <van-nav-bar
        title="查询结果"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
      />
      <div v-if="!pageErr" class="p-2">
        <van-cell-group>
          <van-row>
            <van-col
              span="24"
              v-for="(src, index) in detail.image"
              :key="index"
            >
              <van-image
                style="display: block"
                width="100%"
                class="min-h-20"
                fit="scale-down"
                :src="src"
                @click="onClickImage(index)"
              />
            </van-col>
          </van-row>
        </van-cell-group>
        <van-cell-group class="p-4" title="详细描述">
          <van-row class="pb-4" gutter="20">
            <van-col span="8">编号</van-col>
            <van-col span="16">{{ detail.id }}</van-col>
          </van-row>
          <van-row class="pb-4" gutter="20">
            <van-col span="8">型号</van-col>
            <van-col span="16">
              <dict-value
                :options="dictData.model"
                :value="detail.model"
              ></dict-value>
            </van-col>
          </van-row>
          <van-row class="pb-4" gutter="20">
            <van-col span="8">产品编号</van-col>
            <van-col span="16">
              {{ detail.frameNo }}
            </van-col>
          </van-row>
          <van-row gutter="20">
            <van-col span="8">生产日期</van-col>
            <van-col span="16">
              {{ detail.produceTime }}
            </van-col>
          </van-row>
          <!--<van-row class="pb-4" gutter="20">-->
          <!--  <van-col span="8">二维码编码</van-col>-->
          <!--  <van-col span="16">-->
          <!--    {{ detail.qrcode }}-->
          <!--  </van-col>-->
          <!--</van-row>-->
          <!--<van-row class="pb-4" gutter="20">-->
          <!--  <van-col span="8">二维码</van-col>-->
          <!--  <van-col span="16">-->
          <!--    <van-image width="100" fit="scale-down" :src="detail.qrImg" />-->
          <!--  </van-col>-->
          <!--</van-row>-->
        </van-cell-group>
      </div>
      <div v-else class="p-2">
        <van-empty image="error" :description="pageErrMsg" />
      </div>
    </div>
    <van-image-preview
      v-model:show="showPreview"
      :images="detail.image"
      :start-position="startPosition"
    ></van-image-preview>
  </van-config-provider>
</template>
<script lang="ts" setup>
import { useDark } from '@vueuse/core'
import { showLoadingToast, closeToast, showImagePreview } from 'vant'
import { queryByQrcode } from '@/api/bicycle'
import { useDictData } from '@/hooks/useDictOptions'

const isDark = useDark()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})

const detail = reactive({
  id: '',
  model: '', // 型号
  frameNo: '', // 车架号
  conclusion: '',
  createTime: '',
  image: [] as string[], // 图片
  qrcode: '', // 二维码编码
  qrImg: '',
  remark: '',
  produceTime: '', // 生产日期
})
const { dictData } = useDictData<{
  model: any[]
  conclusion: any[]
}>(['model', 'conclusion'])

const router = useRouter()
const route = useRoute()
const qrcode = route.query.qrcode as string
const pageErr = ref(false)
const pageErrMsg = ref('')
const showPreview = ref(false)
const startPosition = ref(0)
// 返回上一页
const onClickLeft = () => {
  router.back()
}
// 图片预览
const onClickImage = (index: number) => {
  showPreview.value = true
  startPosition.value = index
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
    for (let key in detail) {
      if (data.hasOwnProperty(key)) {
        detail[key] = data[key]
      }
      if (key === 'image') {
        detail.image = data[key].split(';').filter((src) => !!src)
      }
    }
  } catch (e) {
    console.log('e ==>', e)
    pageErr.value = true
    pageErrMsg.value = '查询失败，请重试'
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
