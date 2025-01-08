<template>
    <div class="h-[100vh]">
      <QrStream
          @decode="handleDecode"
          @init="handleInit"
          @error="handleError"
      >
        <template v-if="showCloseButton">
          <div class="qr-scanner-container">
            <icon @click="closeScanner" name="el-icon-CircleClose" class="close-view dis-center"/>
            <div class="qr-scanner">
              <div class="box">
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
import {QrStream} from 'vue3-qr-reader'

const showCloseButton = ref(false)

const qrcodeData = ref(null)

const handleDecode = (data) => {
  console.log('Decoded data:', data)
  emit('decode', data)
  qrcodeData.value = data

}

const closeScanner = () => {
  emit('close')
}

const handleInit = async (promise) => {
  try {
    const {capabilities} = await promise
    console.log('Camera capabilities:', capabilities)
    showCloseButton.value = true
  } catch (error) {
    handleError(error)

  }
}

const handleError = (error) => {
  const errorMessages = {
    NotAllowedError: '您需要授予相机访问权限',
    NotFoundError: '这个设备上没有摄像头',
    NotSupportedError: '所需的安全上下文(HTTPS、本地主机)',
    NotReadableError: '相机被占用',
    OverconstrainedError: '安装摄像头不合适',
    StreamApiNotSupportedError: '此浏览器不支持流API',
    InsecureContextError: '仅允许在安全上下文中访问摄像机。使用HTTPS或本地主机，而不是HTTP。',
  }

  const message = errorMessages[error.name] || 'ERROR: 摄像头错误'
  message.error(message)
  console.error(message)
}

defineExpose({qrcodeData})
</script>

<style scoped lang="scss">
.qr-scanner-container {
  position: relative;
  width: 100%;
  height: 100%;

  .close-view {
    position: absolute;
    top: 20px;
    right: 20px;
    font-size: 40px;
    color: #FFFFFF;
    z-index: 1000000;
  }
}

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
  width: 100%;
  /* height: 100%; */
  height: 100vh;
  position: relative;
}

.qr-scanner .box {
  width: 213px;
  height: 213px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  overflow: hidden;
  border: 0.1rem solid rgba(0, 255, 51, 0.2);
  /* background: url('http://resource.beige.world/imgs/gongconghao.png') no-repeat center center; */
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

.qr-scanner .box:after,
.qr-scanner .box:before,
.qr-scanner .angle:after,
.qr-scanner .angle:before {
  content: '';
  display: block;
  position: absolute;
  width: 3vw;
  height: 3vw;

  border: 0.2rem solid transparent;
}

.qr-scanner .box:after,
.qr-scanner .box:before {
  top: 0;
  border-top-color: #00ff33;
}

.qr-scanner .angle:after,
.qr-scanner .angle:before {
  bottom: 0;
  border-bottom-color: #00ff33;
}

.qr-scanner .box:before,
.qr-scanner .angle:before {
  left: 0;
  border-left-color: #00ff33;
}

.qr-scanner .box:after,
.qr-scanner .angle:after {
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