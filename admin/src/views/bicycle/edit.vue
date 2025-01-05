<template>
  <popup ref="popupRef" :title="popupTitle" :async="true" :loading="submitLoading" width="550px" @confirm="handleSubmit" @close="handleClose">
    <el-form ref="formRef" :model="formData" label-width="120px" :rules="formRules">
      <el-form-item label="供应商名称" prop="supplierName">
        <el-input v-model="formData.supplierName" placeholder="请输入供应商名称" clearable />
      </el-form-item>
      <el-form-item label="供应商编码" prop="supplierCode">
        <el-input v-model="formData.supplierCode" placeholder="请输入供应商编码" clearable />
      </el-form-item>
      <el-form-item label="供应商地址" prop="supplierAddress">
        <el-input v-model="formData.supplierAddress" placeholder="请输入供应商地址" clearable />
      </el-form-item>
      <el-form-item label="供应商许可证号" prop="licenseNumber">
        <el-input v-model="formData.licenseNumber" placeholder="请输入供应商许可证编号" clearable />
      </el-form-item>
      <el-form-item label="采购次数" prop="purchaseTimes">
        <el-input v-model="formData.purchaseTimes" placeholder="请输入采购次数" clearable />
      </el-form-item>
      <el-form-item label="供应商状态" prop="state">
        <el-radio-group v-model="formData.isDisabled">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="dictRemark">
        <el-input v-model="formData.remark" class="w-full" type="textarea" :autosize="{ minRows: 4, maxRows: 4 }" maxlength="255" show-word-limit clearable />
      </el-form-item>
    </el-form>
  </popup>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import Popup from '@/components/popup/index.vue'
import { ElForm } from 'element-plus'
import feedback from '@/utils/feedback.ts'
import {} from '@/api/bycicle'

const emits = defineEmits(['close', 'success'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const formRef = shallowRef<InstanceType<typeof ElForm>>()
const submitLoading = ref(false)
const openType = ref('')

const popupTitle = computed(() => {
  return openType.value === 'add' ? '新增供应商' : '编辑供应商'
})

const formData = reactive({
  supplierId: '' as string | number,
  supplierName: '',
  supplierCode: '',
  supplierAddress: '',
  licenseNumber: '',
  purchaseTimes: '',
  remark: '',
  isDisabled: 0
})

const formRules = reactive({
  supplierName: [{ required: true, message: '请输入供应商名称', trigger: ['blur'] }],
  supplierCode: [{ required: true, message: '请输入供应商编码', trigger: ['blur'] }],
  isDisabled: [{ required: true, message: '请选择供应商状态', trigger: ['blur'] }],
  supplierAddress: [{ required: true, message: '请输入供应商地址', trigger: ['blur'] }],
  licenseNumber: [{ required: true, message: '请输入供应商许可证', trigger: ['blur'] }],
  purchaseTimes: [{ required: true, message: '请输入采购次数', trigger: ['blur'] }]
})

const handleSubmit = async () => {
  try {
    submitLoading.value = true
    formRef.value?.validate()
    // openType.value === 'add' ? await supplierAdd(formData) : await supplierEdit(formData)
    popupRef.value?.close()
    feedback.msgSuccess('操作成功')
    emits('success')
  } catch {
  } finally {
    submitLoading.value = false
  }
}

const handleClose = () => {
  emits('close')
}

const open = (type: 'add' | 'edit') => {
  openType.value = type
  popupRef.value?.open()
}

const getSupplierDetail = async (id: string) => {
  // formData.supplierId = id
  // try {
  //   const data = await supplierDetail(id)
  //   for (let key in formData) {
  //     if (data.hasOwnProperty(key)) {
  //       formData[key] = data[key]
  //     }
  //   }
  // } catch {}
}

defineExpose({
  open,
  getSupplierDetail
})
</script>

<style scoped></style>
@/api/bicycle
