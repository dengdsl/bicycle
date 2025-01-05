<template>
  <div class="dict-edit">
    <popup
      ref="popupRef"
      :title="popupTitle"
      :async="true"
      :loading="submitLoading"
      width="550px"
      @confirm="handleSubmit"
      @close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        label-width="80px"
        :rules="formRules"
      >
        <el-form-item label="字典名称" prop="dictName">
          <el-input
            v-model="formData.dictName"
            placeholder="请输入字典名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input
            v-model="formData.dictType"
            placeholder="请输入字典类型"
            clearable
          />
        </el-form-item>
        <el-form-item label="字典状态" prop="dictStatus">
          <el-radio-group v-model="formData.dictStatus">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="字典备注" prop="dictRemark">
          <el-input
            v-model="formData.dictRemark"
            class="w-full"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 4 }"
            maxlength="512"
            show-word-limit
            clearable
          />
        </el-form-item>
      </el-form>
    </popup>
  </div>
</template>

<script setup lang="ts">
import Popup from '@/components/popup/index.vue'
import { ElForm, FormItemRule } from 'element-plus'
import { dictAdd, dictEdit, getDictDetail } from '@/api/dict'
import feedback from '@/utils/feedback.ts'

const popupTitle = ref('')
const openType = ref('')
const submitLoading = ref(false)
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const formRef = shallowRef<InstanceType<typeof ElForm>>()
const emits = defineEmits(['success', 'close'])

// 表单数据
const formData = reactive({
  id: '',
  dictName: '',
  dictType: '',
  dictStatus: 1,
  dictRemark: '',
})

// 表单验证规则
const formRules = reactive<FormItemRule>({
  dictName: [
    {
      required: true,
      message: '请输入字典名称',
    },
  ],
  dictType: [
    {
      required: true,
      message: '请输入字典类型',
    },
  ],
  dictStatus: [
    {
      required: true,
      message: '请选择字典状态',
    },
  ],
})
const handleSubmit = async () => {
  try {
    submitLoading.value = true
    await formRef.value?.validate()
    openType.value === 'add'
      ? await dictAdd(formData)
      : await dictEdit(formData)
    popupRef.value?.close()
    feedback.msgSuccess('操作成功')
    // 发起新增请求
    emits('success')
  } catch {
  } finally {
    submitLoading.value = false
  }
}

// 关闭表单
const handleClose = () => {
  emits('close')
}

// 获取字典详情
const getDetail = async (id: string) => {
  try {
    const res = await getDictDetail({ id })
    formData.id = res.id
    formData.dictName = res.dictName
    formData.dictType = res.dictType
    formData.dictStatus = res.dictStatus
    formData.dictRemark = res.dictRemark
  } catch {}
}

// 打开表单
const open = (type: string) => {
  openType.value = type || 'add'
  type === 'add'
    ? (popupTitle.value = '新增字典')
    : (popupTitle.value = '编辑字典')
  popupRef.value?.open()
}

defineExpose({
  open,
  getDetail,
})
</script>
