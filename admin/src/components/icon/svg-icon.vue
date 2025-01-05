<template>
  <svg aria-hidden="true" :style="styles">
    <!--currentColor:表示使用当前传递的颜色值，如果没有传递则继承父元素的颜色值-->
    <use :xlink:href="symbolId" fill="currentColor" />
  </svg>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import type { CSSProperties } from 'vue'
import { addUnit } from '@/utils/util'
export default defineComponent({
  name: 'svgIcon',
  props: {
    name: {
      type: String,
      required: true,
      describe: '图标名称',
    },
    size: {
      type: [Number, String],
      default: 16,
      describe: '图标大小',
    },
    color: {
      type: String,
      default: 'inherit',
      describe: '图标颜色',
    },
  },
  setup(props) {
    const symbolId = computed(() => `#${props.name}`)
    const styles = computed<CSSProperties>(() => {
      return {
        width: addUnit(props.size as number | string),
        height: addUnit(props.size as number | string),
        color: props.color,
      }
    })
    return { symbolId, styles }
  },
})
</script>
