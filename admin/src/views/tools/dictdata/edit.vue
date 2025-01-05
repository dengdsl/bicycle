<template>
  <div class="dict-data-edit">
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
        <el-form-item label="字典类型" prop="dictType">
          <el-select
            class="w-[280px]"
            disabled
            v-model="formData.dictType"
            placeholder="请选择字典类型"
          >
            <el-option
              v-for="dict in dictTypeList"
              :key="dict.id"
              :label="dict.dictName"
              :value="dict.dictType"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="数据名称" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入数据名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="数据值" prop="value">
          <el-input
            v-model="formData.value"
            placeholder="请输入数据值"
            clearable
          />
        </el-form-item>
        <el-form-item label="数据状态" prop="state">
          <el-radio-group v-model="formData.state">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input
            v-model="formData.sort"
            placeholder="请输入排序值"
            clearable
          />
          <div class="form-tips">数值越大越排前</div>
        </el-form-item>
        <el-form-item label="备注" prop="dictRemark">
          <el-input
            v-model="formData.remark"
            class="w-full"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 4 }"
            maxlength="128"
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
import {
  dictAll,
  dictDataAdd,
  dictDataEdit,
  getDictDataDetail,
  getDictDetail,
} from '@/api/dict'
import feedback from '@/utils/feedback.ts'
import { ElForm, FormItemRule } from 'element-plus'

const emits = defineEmits(['close', 'success'])
const route = useRoute()
const popupTitle = ref('')
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const submitLoading = ref(false)
const openType = ref('')
const formRef = shallowRef<InstanceType<typeof ElForm>>()
const dictTypeList = ref<
  {
    id: string
    dictName: string
    dictType: string
  }[]
>([])

const formData = reactive({
  id: '',
  dictType: route.query.type,
  name: '',
  value: '',
  state: 1,
  remark: '',
  sort: 0,
})

// 表单验证规则
const formRules = reactive<FormItemRule>({
  dictType: [
    {
      required: true,
      message: '请选择字典类型',
    },
  ],
  name: [
    {
      required: true,
      message: '请输入字典名称',
    },
  ],
  value: [
    {
      required: true,
      message: '请输入字典数据值',
    },
  ],
  state: [
    {
      required: true,
      message: '请选择字典状态',
    },
  ],
  sort: [
    {
      required: true,
      message: '请输入排序',
    },
  ],
})

// 获取所有的字典类型
const getAllDictType = async () => {
  const data = await dictAll()
  // 将所有数据保存到列表中
  dictTypeList.value = data.map((dict) => ({
    id: dict.id,
    dictName: dict.dictName,
    dictType: dict.dictType,
  }))
}

const open = async (type: 'add' | 'edit') => {
  openType.value = type
  type === 'add'
    ? (popupTitle.value = '新增字典数据')
    : (popupTitle.value = '编辑字典数据')
  popupRef.value?.open()
}

const handleSubmit = async () => {
  try {
    submitLoading.value = true
    await formRef.value?.validate()
    openType.value === 'add'
      ? await dictDataAdd(formData)
      : await dictDataEdit(formData)
    popupRef.value?.close()
    feedback.msgSuccess('操作成功')
    // 发起新增请求
    emits('success')
  } catch {
  } finally {
    submitLoading.value = false
  }
}

const dictDataDetail = async (id) => {
  formData.id = id
  const data = await getDictDataDetail({ id })
  // 将数据设置到表单数据中
  formData.dictType = data.dictType
  formData.name = data.name
  formData.value = data.value
  formData.state = data.state
  formData.remark = data.remark
  formData.sort = data.sort
}

const handleClose = () => {
  emits('close')
}

getAllDictType()

defineExpose({
  open,
  dictDataDetail,
})
</script>
