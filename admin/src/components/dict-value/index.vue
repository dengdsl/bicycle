<template>
  <div>
    <template v-for="(item, index) in getOptions" :key="index">
      <span>{{ index != 0 ? '、' : '' }}{{ item.name }}</span>
    </template>
  </div>
</template>
<script lang="ts" setup>
const props = defineProps({
  options: {
    type: Array,
    default: () => [],
  },
  value: {
    type: [String, Number, Object, Array],
  },
})

const values = computed(() => {
  if (props.value !== null && typeof props.value !== 'undefined') {
    return Array.isArray(props.value)
      ? props.value
      : String(props.value).split(',')
  } else {
    return []
  }
})

const getOptions = computed(() => {
  return props.options.filter((item) => values.value.includes(item.value))
})
</script>
