scriptscriptscript
<template>
  <div v-loading="loading">
    <el-card header="基础信息" shadow="hover">
      <el-row :gutter="20">
        <el-col :span="24">编号：{{ detail.id }}</el-col>
        <el-col :span="24">标题：{{ detail.title }}</el-col>
        <el-col :span="24">备注：{{ detail.remark }}</el-col>
        <el-col :span="24">创建时间：{{ detail.createTime }}</el-col>
      </el-row>
    </el-card>
    <el-card header="图片" shadow="hover" style="margin-top: 20px">
      <el-space :size="20">
        <template v-for="(src, index) in detail.image" :key="src">
          <el-image
            style="width: 100px; height: 100px"
            :src="src"
            fit="fill"
            :zoom-rate="2"
            :max-scale="7"
            :min-scale="0.2"
            :initial-index="index"
            :preview-src-list="detail.image"
          >
            <template #error>
              <div
                class="slot-image w-[100px] h-[100px] flex items-center justify-center"
              >
                <icon name="el-icon-Picture" size="50" />
              </div>
            </template>
          </el-image>
        </template>
      </el-space>
    </el-card>
  </div>
</template>
<script lang="ts" setup>
import { getBicycleDetail } from '@/api/bicycle'

const loading = ref(false)
const detail = reactive({
  createTime: '',
  deleteTime: '',
  id: '',
  image: '',
  isDel: 0,
  remark: '这是备注信息',
  title: '山地自行车',
  updateTime: '2025-01-05 07:28:30',
})

const loadData = async (id: string) => {
  try {
    loading.value = true
    const data = await getBicycleDetail({ id })
    for (let key in detail) {
      if (data.hasOwnProperty(key)) {
        detail[key] = data[key]
      }
      if (key === 'image') {
        detail.image = data[key].split(';').filter((src) => !!src)
      }
    }
  } catch (err) {
    console.log('err ==>', err)
  } finally {
    loading.value = false
  }
}

defineExpose({
  loadData,
})
</script>
<style lang="scss" scoped>
.slot-image {
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
}
</style>
