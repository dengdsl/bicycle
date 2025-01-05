<template>
  <el-image :style="styles" v-bind="props"></el-image>
</template>

<script setup lang="ts">
import { addUnit } from '@/utils/util'
import { imageProps } from 'element-plus'
import { CSSProperties } from 'vue'

const props = defineProps({
  width: {
    // 图片宽度
    type: [Number, String],
    default: 'auto',
  },
  height: {
    // 图片高度
    type: [Number, String],
    default: 'auto',
  },
  radius: {
    // 图片圆角
    type: [Number, String],
    default: 0,
  },
  ...imageProps,
  hideOnClickModal: {
    type: Boolean,
    default: false,
  },
})

// 动态给出图片样式
const styles = computed<CSSProperties>(() => {
  return {
    width: addUnit(props.width as number | string),
    height: addUnit(props.height as number | string),
    borderRadius: addUnit(props.radius as number | string),
  }
})
</script>

<style scoped lang="scss">
.el-image {
  display: block;
  .el-image__error {
    @apply text-xs;
  }
}
</style>
