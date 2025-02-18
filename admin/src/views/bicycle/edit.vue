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
      label-width="100px"
      :rules="formRules"
    >
      <el-form-item label="X光图片" prop="fileList">
        <div class="flex flex-wrap gap-2">
          <el-upload
            v-model:file-list="fileList"
            accept="image/**"
            :data="{ path: 'images' }"
            :action="action"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-success="handleSuccess"
          >
            <icon name="el-icon-Plus"></icon>
          </el-upload>
          <div v-show="defaultImgUrl && formData.proName">
            <el-image
              class="el-upload el-upload--picture-card"
              style="width: 6em; height: 6em"
              :src="defaultImgUrl"
              fit="fill"
              :zoom-rate="2"
              :max-scale="7"
              :min-scale="0.2"
              :preview-src-list="[defaultImgUrl]"
            >
              <template #error>
                <div
                  class="slot-image w-full h-full flex items-center justify-center"
                >
                  <icon name="el-icon-Picture" size="50" />
                </div>
              </template>
            </el-image>
          </div>
        </div>
        <el-dialog v-model="dialogVisible">
          <img class="w-full" :src="dialogImageUrl" alt="Preview Image" />
        </el-dialog>
      </el-form-item>
      <el-form-item label="产品编码" prop="frameNo">
        <el-input
          v-model="formData.frameNo"
          placeholder="请输入产品编码"
          clearable
        />
      </el-form-item>
      <el-form-item label="生产日期" prop="produceTime">
        <el-date-picker
          style="width: 100%"
          v-model="formData.produceTime"
          type="date"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          placeholder="请选择"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="proName">
        <el-select
          v-model="formData.proName"
          filterable
          placeholder="请选择"
          clearable
        >
          <el-option
            v-for="item in dictData.proName"
            :key="item.id"
            :label="item.name"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="产品型号" prop="model">
        <el-select
          v-model="formData.model"
          filterable
          placeholder="请选择"
          clearable
        >
          <el-option
            v-for="item in dictData.model"
            :key="item.id"
            :label="item.name"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="空孔" prop="hollowHole">
        <el-select v-model="formData.hollowHole" placeholder="请选择" clearable>
          <el-option
            v-for="item in dictData.hollowHole"
            :key="item.id"
            :label="item.name"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="内折" prop="inFold">
        <el-select v-model="formData.inFold" placeholder="请选择" clearable>
          <el-option
            v-for="item in dictData.inFold"
            :key="item.id"
            :label="item.name"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="型号" prop="raveling">
        <el-select v-model="formData.raveling" placeholder="请选择" clearable>
          <el-option
            v-for="item in dictData.raveling"
            :key="item.id"
            :label="item.name"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          class="w-full"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4 }"
          maxlength="1024"
          show-word-limit
          clearable
        />
      </el-form-item>
      <el-form-item label="结论" prop="conclusion">
        <el-switch
          v-model="formData.conclusion"
          active-text="通过"
          inactive-text="不通过"
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
import { useDictData } from '@/hooks/useDictOptions.ts'
import { getConfig } from '@/api/config'

const emits = defineEmits(['close', 'success'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const formRef = shallowRef<InstanceType<typeof ElForm>>()
const submitLoading = ref(false)
const openType = ref('')

const { dictData } = useDictData<{
  model: any[]
  hollowHole: any[]
  inFold: any[]
  raveling: any[]
  proName: any[]
}>(['model', 'hollowHole', 'inFold', 'raveling', 'proName'])

const popupTitle = computed(() => {
  return openType.value === 'add' ? '新增' : '编辑'
})

// 动态获取图片上传地址
const action = computed(() => {
  return `${config.baseUrl}api/upload/file`
})

const defaultImageList = ref<
  Array<{
    src: string
    dictType: string
    name: string
    value: string
  }>
>([])
const formData = reactive({
  id: '' as string | number,
  proName: '', // 产品名称
  model: '', // 型号
  frameNo: '', // 车架号
  conclusion: true, // 结论
  produceTime: '', // 生产日期
  remark: '', // 备注
  image: '', // X光图片
  hollowHole: '', // 空孔
  inFold: '', // 内折
  raveling: '', // 乱纱
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
  proName: [
    { required: true, message: '请选择产品名称', trigger: ['blur', 'change'] },
  ],
  model: [
    { required: true, message: '请选择型号', trigger: ['blur', 'change'] },
  ],
  frameNo: [
    { required: true, message: '请输入产品编号', trigger: ['blur', 'change'] },
  ],
  produceTime: [
    { required: true, message: '请选择生产日期', trigger: ['blur', 'change'] },
  ],
  fileList: [
    {
      validator: imageValidate,
      trigger: ['blur'],
    },
  ],
})

// 已上传的文件列表
const fileList = ref<UploadUserFile[]>([])
const defaultImgUrl = ref('')

watch(
  () => [formData.proName, defaultImageList],
  () => {
    const img = defaultImageList.value.find(
      (item) => item.value === formData.proName,
    )
    defaultImgUrl.value = img?.src || ''
  },
)

const dialogImageUrl = ref('')
const dialogVisible = ref(false)

const handleSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  if (response.code === 200) {
    feedback.msgSuccess('上传成功')
  } else {
    feedback.msgError('上传失败')
    fileList.value.some((item) => {
      if (item.uid === uploadFile.uid) {
        item.status = 'fail'
        return true
      }
    })
  }
}
const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url!
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    submitLoading.value = true
    await formRef.value?.validate()
    const images = fileList.value
      .filter((item) => item.status === 'success')
      .map((item) => (item.response ? item.response.data : item.url))
    if (openType.value === 'add') {
      images.push(defaultImgUrl.value)
    }
    formData.image = images.join(';')
    openType.value === 'add'
      ? await addBicycle({ ...formData, conclusion: formData ? 1 : 0 })
      : await editBicycle({ ...formData, conclusion: formData ? 1 : 0 })
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

const open = async (type: 'add' | 'edit') => {
  openType.value = type
  popupRef.value?.open()
  // 获取默认的图片配置信息
  getConfig(['proNameDefaultXImg']).then((res) => {
    if (res.proNameDefaultXImg) {
      defaultImageList.value = JSON.parse(res.proNameDefaultXImg) as Array<{
        src: string
        dictType: string
        name: string
        value: string
      }>
    }
  })
}

const loadData = async (id: string) => {
  formData.id = id
  try {
    const data = await getBicycleDetail({ id })
    for (let key in formData) {
      if (data.hasOwnProperty(key)) {
        formData[key] = data[key]
        if (key === 'image') {
          const images = data[key]
            .split(';')
            .filter((item) => !!item)
            .map((item) => ({
              url: item,
            }))
          fileList.value = images.slice(0, images.length - 1)
          defaultImgUrl.value = images[images.length - 1].url
        } else if (key === 'conclusion') {
          formData[key] = data[key] == 1
        } else if (key === 'model') {
          formData[key] = `${data[key]}`
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
  width: 6em !important;
  height: 6em !important;
}

::v-deep(.el-upload-list__item) {
  width: 6em !important;
  height: 6em !important;
}
</style>
