<template>
  <div class="error">
    <div>
      <!-- 错误状态码开始-->
      <slot name="content">
        <div class="error-code">{{ code }}</div>
      </slot>
      <div class="text-lg text-tx-secondary mt-7 mb-7">{{ title }}</div>
      <el-button v-if="showButton" type="primary" @click="router.go(-1)">
        {{ seconds }} 秒后返回上一页
      </el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onBeforeUnmount, ref } from 'vue'
import { useRouter } from 'vue-router'

// 定义自定义属性，状态码，标题，是否显示按钮
const props = defineProps({
  code: String,
  title: String,
  showButton: {
    type: Boolean,
    default: true,
  },
})

// 定义定时器，五秒钟之后返回上一个页面
let timer: any = null
const seconds = ref(5)
// 需要使用编程式路由导航跳转到上一个界面
const router = useRouter()

// 如果按钮显示，那么开启定时器五秒钟之后返回上一个界面
if (props.showButton) {
  timer = setInterval(() => {
    // 如果倒计时为0，则跳转到上一个界面中
    if (seconds.value <= 0) {
      // 清除定时器
      // ts-ignore
      clearInterval(timer)
      router.back()
    } else {
      seconds.value--
    }
  }, 1000)
}

// 组件销毁自动清除定时器
onBeforeUnmount(() => {
  timer.value && clearInterval(timer)
})
</script>

<style scoped lang="scss">
.error {
  text-align: center;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
