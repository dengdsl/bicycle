<template>
  <div>
    <el-tooltip v-bind="props" :disabled="disabled">
      <div
        ref="textRef"
        class="overflow-text truncate"
        :style="{ textOverflow: overflowType }"
      >
        {{ content }}
      </div>
    </el-tooltip>
  </div>
</template>

<script lang="ts" setup>
import { useEventListener } from '@vueuse/core'
import { useTooltipContentProps, type Placement } from 'element-plus'
import type { PropType } from 'vue'

const props = defineProps({
  ...useTooltipContentProps,
  teleported: {
    type: Boolean,
    default: false,
  },
  placement: {
    type: String as PropType<Placement>,
    default: 'top',
  },
  overflowType: {
    type: String as PropType<'ellipsis' | 'unset' | 'clip'>,
    default: 'ellipsis',
  },
})
const textRef = shallowRef<HTMLElement>()
const disabled = ref(false)

useEventListener(textRef, 'mouseenter', () => {
  if (textRef.value?.scrollWidth! > textRef.value?.offsetWidth!) {
    disabled.value = false
  } else {
    disabled.value = true
  }
})
</script>

<style></style>
