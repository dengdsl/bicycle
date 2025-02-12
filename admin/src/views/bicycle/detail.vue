<template>
  <div v-loading="loading">
    <el-card shadow="never" class="!border-none">
      <el-row
        class="mb-4 border-[1px] border-solid border-info py-2"
        :gutter="20"
      >
        <el-col :span="12">编号：{{ detail.id }}</el-col>
        <el-col :span="12">
          <div class="flex items-center">
            <span>型号：</span>
            <dict-value :options="dictData.model" :value="detail.model" />
          </div>
        </el-col>
      </el-row>
      <el-row
        class="mb-4 border-[1px] border-solid border-info py-2"
        :gutter="20"
      >
        <el-col :span="12">产品编号：{{ detail.frameNo }}</el-col>
        <el-col :span="12">生产日期：{{ detail.produceTime }}</el-col>
      </el-row>
      <el-row
        class="mb-4 border-[1px] border-solid border-info py-2"
        :gutter="20"
      >
        <el-col :span="12">
          <span>结论：</span>
          <el-tag :type="detail.conclusion == '1' ? 'success' : 'danger'">
            <dict-value
              :options="dictData.conclusion"
              :value="detail.conclusion"
            ></dict-value>
          </el-tag>
        </el-col>
        <el-col :span="12">备注：{{ detail.remark }}</el-col>
      </el-row>
      <el-row
        class="mb-4 border-[1px] border-solid border-info py-2"
        :gutter="20"
      >
        <el-col :span="24">二维码编码：{{ detail.qrcode }}</el-col>
      </el-row>
      <el-row
        class="mb-4 border-[1px] border-solid border-info py-2"
        :gutter="20"
      >
        <el-col :span="24">
          <div class="flex items-center">
            <span>二维码：</span>
            <el-image
              style="width: 100px; height: 100px"
              :src="detail.qrImg"
              fit="fill"
              :zoom-rate="2"
              :max-scale="7"
              :min-scale="0.2"
              :preview-src-list="[detail.qrImg]"
            >
              <template #error>
                <div
                  class="slot-image w-[100px] h-[100px] flex items-center justify-center"
                >
                  <icon name="el-icon-Picture" size="50" />
                </div>
              </template>
            </el-image>
          </div>
        </el-col>
      </el-row>
      <el-row class="border-[1px] border-solid border-info py-2" :gutter="20">
        <el-col :span="24">
          <div class="flex items-center">
            <span>X光图片：</span>
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
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script lang="ts" setup>
import { getBicycleDetail } from '@/api/bicycle'
import { useDictData } from '@/hooks/useDictOptions.ts'

const loading = ref(false)
const detail = reactive({
  id: '',
  model: '', // 型号
  frameNo: '', // 车架号
  conclusion: '',
  createTime: '',
  image: [] as string[], // 图片
  qrcode: '', // 二维码编码
  qrImg: '',
  remark: '',
  produceTime: '', // 生产日期
})
const { dictData } = useDictData<{
  model: any[]
  conclusion: any[]
}>(['model', 'conclusion'])

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
