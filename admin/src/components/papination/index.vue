<template>
  <div class="pagination">
    <div class="pagination">
      <el-pagination
        v-bind="props"
        :pager-count="5"
        v-model:currentPage="pager.page"
        v-model:pageSize="pager.size"
        :page-sizes="props.pageSizes"
        :layout="layout"
        :total="pager.total || 0"
        :hide-on-single-page="false"
        @size-change="sizeChange"
        @current-change="pageChange"
      ></el-pagination>
    </div>
  </div>
</template>

<script lang="ts" setup>
interface Props {
  modelValue?: Record<string, any>
  pageSizes?: number[]
  layout?: string
}
// 创建自定义属性，并设置初始值
const props = withDefaults(defineProps<Props>(), {
  modelValue: () => ({}),
  pageSizes: [15, 30, 45, 60],
  layout: 'total, sizes, prev, pager, next, jumper',
})

// 创建自定以事件
const emit = defineEmits<{
  (event: 'update:modelValue', value: Record<string, any>): void
  (event: 'change'): void
}>()

const pager = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  },
})

// 长度发生变化
const sizeChange = () => {
  pager.value.page = 1
  emit('change')
}

// 分页发生变化
const pageChange = () => {
  emit('change')
}
</script>
