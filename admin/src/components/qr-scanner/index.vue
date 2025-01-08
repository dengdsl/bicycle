<template>
  <div class="h-screen">
    <QrStream @decode="handleDecode" @init="handleInit" @error="handleError">
      <template v-if="showCloseButton">
        <div class="relative w-full h-full">
          <div class="absolute top-5 right-5 z-50" @click="closeScanner">
            <icon name="el-icon-CircleClose" size="40" color="#ffffff" />
          </div>
          <div class="qr-scanner w-full h-screen relative">
            <div
              class="box w-52 h-52 absolute top-1/2 left-1/2 -translate-y-1/2 -translate-x-1/2 overflow-hidden border-solid border-[0.1em] border-[#00FF3333]"
            >
              <div class="line"></div>
              <div class="angle"></div>
            </div>
          </div>
        </div>
      </template>
    </QrStream>
  </div>
</template>
<script setup lang="ts">
import { QrStream } from 'vue3-qr-reader'
import feedback from '@/utils/feedback.ts'

const emits = defineEmits(['decode', 'close'])
const showCloseButton = ref(false)

const qrcodeData = ref(null)

const handleDecode = (data: any) => {
  emits('decode', data)
  qrcodeData.value = data
}

const closeScanner = () => {
  emits('close')
}

const handleInit = async (promise) => {
  try {
    const { capabilities } = await promise
    console.log()
    showCloseButton.value = true
  } catch (error) {
    handleError(error)
  }
}

const handleError = (error: any) => {
  const errorMessages = {
    NotAllowedError: '您需要授予相机访问权限',
    NotFoundError: '这个设备上没有摄像头',
    NotSupportedError: '所需的安全上下文(HTTPS、本地主机)',
    NotReadableError: '相机被占用',
    OverconstrainedError: '安装摄像头不合适',
    StreamApiNotSupportedError: '此浏览器不支持流API',
    InsecureContextError:
      '仅允许在安全上下文中访问摄像机。使用HTTPS或本地主机，而不是HTTP。',
  }
  const message = errorMessages[error.name] || 'ERROR: 摄像头错误'
  feedback.msgError(message)
}

defineExpose({ qrcodeData })
</script>

<style scoped lang="scss">
.qr-scanner {
  background-image: linear-gradient(
      0deg,
      transparent 24%,
      rgba(32, 255, 77, 0.1) 25%,
      rgba(32, 255, 77, 0.1) 26%,
      transparent 27%,
      transparent 74%,
      rgba(32, 255, 77, 0.1) 75%,
      rgba(32, 255, 77, 0.1) 76%,
      transparent 77%,
      transparent
    ),
    linear-gradient(
      90deg,
      transparent 24%,
      rgba(32, 255, 77, 0.1) 25%,
      rgba(32, 255, 77, 0.1) 26%,
      transparent 27%,
      transparent 74%,
      rgba(32, 255, 77, 0.1) 75%,
      rgba(32, 255, 77, 0.1) 76%,
      transparent 77%,
      transparent
    );
  background-size: 3rem 3rem;
  background-position: -1rem -1rem;
}

.qr-scanner .line {
  height: calc(100% - 2px);
  width: 100%;
  background: linear-gradient(180deg, rgba(0, 255, 51, 0) 43%, #00ff33 211%);
  border-bottom: 3px solid #00ff33;
  transform: translateY(-100%);
  animation: radar-beam 2s infinite alternate;
  animation-timing-function: cubic-bezier(0.53, 0, 0.43, 0.99);
  animation-delay: 1.4s;
}

.qr-scanner .box::after,
.qr-scanner .box::before,
.qr-scanner .angle::after,
.qr-scanner .angle::before {
  content: '';
  display: block;
  position: absolute;
  width: 3vw;
  height: 3vw;
  border: 0.2rem solid transparent;
}

.qr-scanner .box::after,
.qr-scanner .box::before {
  top: 0;
  border-top-color: #00ff33;
}

.qr-scanner .angle::after,
.qr-scanner .angle::before {
  bottom: 0;
  border-bottom-color: #00ff33;
}

.qr-scanner .box::before,
.qr-scanner .angle::before {
  left: 0;
  border-left-color: #00ff33;
}

.qr-scanner .box::after,
.qr-scanner .angle::after {
  right: 0;
  border-right-color: #00ff33;
}

@keyframes radar-beam {
  0% {
    transform: translateY(-100%);
  }

  100% {
    transform: translateY(0);
  }
}
</style>
