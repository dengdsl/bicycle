<template>
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
      label-width="120px"
      :rules="formRules"
    >
      <el-form-item label="名称" prop="title">
        <el-input v-model="formData.title" placeholder="请输入名称" clearable />
      </el-form-item>
      <el-form-item label="图片" prop="fileList">
        <el-upload
          v-model:file-list="fileList"
          accept="image/**"
          :action="action"
          list-type="picture-card"
          :on-preview="handlePictureCardPreview"
        >
          <icon name="el-icon-Plus"></icon>
        </el-upload>
        <el-dialog v-model="dialogVisible">
          <img w-full :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          class="w-full"
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 4 }"
          maxlength="1024"
          show-word-limit
          clearable
        />
      </el-form-item>
    </el-form>
  </popup>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import Popup from '@/components/popup/index.vue'
import { ElForm, UploadProps, UploadUserFile } from 'element-plus'
import feedback from '@/utils/feedback'
import { addBicycle, editBicycle, getBicycleDetail } from '@/api/bicycle'
import config from '@/config'

const emits = defineEmits(['close', 'success'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const formRef = shallowRef<InstanceType<typeof ElForm>>()
const submitLoading = ref(false)
const openType = ref('')

const popupTitle = computed(() => {
  return openType.value === 'add' ? '新增' : '编辑'
})

// 动态获取图片上传地址
const action = computed(() => {
  return `${config.baseUrl}api/upload/file`
})

const formData = reactive({
  id: '' as string | number,
  title: '',
  remark: '',
  image: '',
})

const imageValidate = (_: any, __: any, callback: any) => {
  console.log(fileList.value.length)
  if (!fileList.value.length) {
    callback(new Error('请上传图片'))
  } else if (
    !fileList.value.filter((item) => item.status === 'success').length
  ) {
    callback(new Error('图片未上传成功，请删除重试！'))
  } else {
    callback()
  }
}
const formRules = reactive({
  title: [{ required: true, message: '请输入名称', trigger: ['blur'] }],
  fileList: [
    {
      validator: imageValidate,
      trigger: ['blur'],
    },
  ],
})

// 已上传的文件列表
const fileList = ref<UploadUserFile[]>([])

const dialogImageUrl = ref('')
const dialogVisible = ref(false)

const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url!
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    submitLoading.value = true
    await formRef.value?.validate()
    console.log(fileList.value)
    formData.image = fileList.value
      .filter((item) => item.status === 'success')
      .map((item) => (item.response ? item.response.data : item.url))
      .join(';')
    console.log(formData.image)
    openType.value === 'add'
      ? await addBicycle(formData)
      : await editBicycle(formData)
    popupRef.value?.close()
    feedback.msgSuccess('操作成功')
    emits('success')
  } catch (err) {
    console.log('err ==>', err)
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

const loadData = async (id: string) => {
  formData.id = id
  try {
    const data = await getBicycleDetail({ id })
    for (let key in formData) {
      if (data.hasOwnProperty(key)) {
        formData[key] = data[key]
        if (key === 'image') {
          fileList.value = data[key]
            .split(';')
            .filter((item) => !!item)
            .map((item) => ({
              url: item,
            }))
        }
      }
    }
  } catch (err) {
    console.log('err ==>', err)
  }
}

defineExpose({
  open,
  loadData,
})
</script>

<style lang="scss" scoped>
::v-deep(.el-upload--picture-card) {
  width: 8em !important;
  height: 8em !important;
}

::v-deep(.el-upload-list__item) {
  width: 8em !important;
  height: 8em !important;
}
</style>
