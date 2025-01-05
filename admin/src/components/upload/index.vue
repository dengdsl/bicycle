<template>
  <el-upload
    class="avatar-uploader"
    accept="image/*"
    :action="action"
    :show-file-list="false"
    :on-success="handleAvatarSuccess"
    :on-error="handleError"
  >
    <img v-if="imageUrl || avatar" :src="imageUrl || avatar" class="avatar" />
    <icon v-else name="el-icon-Plus" size="28" class="avatar-uploader-icon" />
  </el-upload>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import config from '@/config'
import type { UploadProps } from 'element-plus'
import feedback from '@/utils/feedback.ts'

const emits = defineEmits(['success', 'error'])

const props = defineProps({
  avatar: {
    type: String,
    default: '',
  },
})

const imageUrl = ref('')

// 动态获取图片上传地址
const action = computed(() => {
  return `${config.baseUrl}api/upload/image`
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
  console.log(uploadFile, uploadFiles)
  imageUrl.value = ''
  emits('error')
  feedback.msgError('上传失败')
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  display: block;
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
  width: 100px;
  height: 100px;
  text-align: center;
}
</style>
