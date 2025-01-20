<template>
  <div :style="{ '--w': `${width}px`, '--h': `${height}px` }">
    <el-upload
      class="avatar-uploader"
      :accept="accept"
      :action="action"
      :data="{ path: filePath }"
      :show-file-list="showFileList"
      :on-success="handleAvatarSuccess"
      :on-error="handleError"
    >
      <div
        class="flex items-center justify-center"
        :style="{ width: `${width}px`, height: `${height}px` }"
      >
        <img v-if="imageUrl || src" :src="imageUrl || src" class="avatar" />
        <icon
          v-else
          name="el-icon-Plus"
          size="28"
          class="avatar-uploader-icon"
        />
      </div>
    </el-upload>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import config from '@/config'
import type { UploadProps } from 'element-plus'
import feedback from '@/utils/feedback'

const emits = defineEmits(['success', 'error'])

const props = defineProps({
  src: {
    type: String,
    default: '',
  },
  width: {
    type: Number,
    default: 100,
  },
  height: {
    type: Number,
    default: 100,
  },
  accept: {
    type: String,
    default: 'image/*',
  },
  server: {
    type: String,
    default: 'api/upload/image',
  },
  filePath: {
    type: String,
    default: '',
  },
  showFileList: {
    type: Boolean,
    default: true,
  },
})

const imageUrl = ref('')

// 动态获取图片上传地址
const action = computed(() => {
  return `${config.baseUrl}${props.server}`
})

// 头像上传成功
const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile,
) => {
  if (response.code === 200) {
    imageUrl.value = URL.createObjectURL(uploadFile.raw!)
    emits('success', response.data)
    feedback.msgSuccess('上传成功')
  } else {
    imageUrl.value = ''
    emits('error')
    feedback.msgError(response.message || '上传失败')
  }
}

// 移除已经上传的文件
const handleError: UploadProps['onRemove'] = (uploadFile, uploadFiles) => {
  console.log(uploadFile.response)
  console.log(uploadFile, uploadFiles)
  imageUrl.value = ''
  emits('error')
  feedback.msgError('上传失败')
}
</script>

<style scoped>
.avatar-uploader .avatar {
  display: block;
  width: 100%;
  height: auto;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: var(--w);
  height: var(--h);
  text-align: center;
}
</style>
