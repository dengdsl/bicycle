<template>
  <van-config-provider :theme="theme">
    <div class="bg-page">
      <qr-scanner @close="onClose" @decode="onDecode"></qr-scanner>
    </div>
  </van-config-provider>
</template>
<script setup lang="ts">
import { useDark } from '@vueuse/core'
import feedback from '@/utils/feedback.ts'
import { PageEnum } from '@/enums/pageEnum.ts'

const isDark = useDark()
const theme = computed(() => {
  return isDark.value ? 'dark' : 'light'
})

const router = useRouter()
const onClose = () => router.back()
const onDecode = (data: any) => {
  console.log('data ==>', data)
  // JSON反序列化
  // try {
  //   data = JSON.parse(data)
  // } catch (e) {
  //   console.log(e)
  // }
  if (!data) return
  router.replace({
    path: PageEnum.SEARCH_RESULT,
    query: {
      qrcode: data,
    },
  })
  // switch (data.qrType) {
  //   case 'bicycle':
  //     router.replace({
  //       path: PageEnum.SEARCH_RESULT,
  //       query: {
  //         id: data.id,
  //       },
  //     })
  //     break
  //   default:
  //     feedback.msgError('无法识别二维码，请扫码正确的二维码')
  // }
}
</script>
