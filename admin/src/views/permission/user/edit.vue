<template>
  <div class="edit-popup">
    <popup
      ref="popupRef"
      :title="popupTitle"
      :async="true"
      width="550px"
      @confirm="handleSubmit"
      @close="handleClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        label-width="84px"
        :rules="formRules"
      >
        <el-form-item label="登录账号" prop="account">
          <el-input
            v-model="formData.account"
            :disabled="isRoot"
            placeholder="请输入账号"
            clearable
          />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <div>
            <div>
              <upload
                file-path="avatar"
                :url="formData.avatar"
                @success="uploadSuccess"
                @error="formData.avatar = ''"
              />
            </div>
            <div class="form-tips">
              建议尺寸：100*100px，支持jpg，jpeg，png格式
            </div>
          </div>
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickname">
          <el-input
            v-model="formData.nickname"
            placeholder="请输入名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="归属部门" prop="deptIds">
          <el-tree-select
            class="flex-1"
            v-model="formData.deptIds"
            :data="optionsData.dept"
            clearable
            node-key="id"
            multiple
            :props="{
              value: 'id',
              label: 'deptName',
              disabled(data: any) {
                return !!data.isStop
              },
            }"
            check-strictly
            :default-expand-all="true"
            placeholder="请选择上级部门"
          />
        </el-form-item>

        <el-form-item label="角色" prop="roleIds">
          <el-select
            v-model="formData.roleIds"
            :disabled="isRoot"
            multiple
            class="flex-1"
            clearable
            placeholder="请选择角色"
          >
            <el-option v-if="isRoot" label="系统管理员" value="0" />
            <el-option
              v-for="(item, index) in optionsData.role"
              :key="index"
              :label="item.roleName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model.trim="formData.password"
            show-password
            clearable
            placeholder="请输入密码"
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="passwordConfirm">
          <el-input
            v-model.trim="formData.passwordConfirm"
            show-password
            clearable
            placeholder="请输入确认密码"
          />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model.trim="formData.email"
            clearable
            placeholder="请输入邮箱"
          />
        </el-form-item>

        <el-form-item label="管理员状态" v-if="!isRoot">
          <el-switch
            v-model="formData.userState"
            :active-value="0"
            :inactive-value="1"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 6 }"
            placeholder="请输入备注"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
    </popup>
  </div>
</template>
<script lang="ts" setup>
import type { FormInstance } from 'element-plus'
import Popup from '@/components/popup/index.vue'
import { adminAdd, adminEdit, adminDetail } from '@/api/permission/user'
import { useDictOptions } from '@/hooks/useDictOptions'
import { roleAll } from '@/api/permission/role'
import Upload from '@/components/upload/index.vue'
import feedback from '@/utils/feedback'
import { deptLists } from '@/api/department'

const emit = defineEmits(['success', 'close'])
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const mode = ref('add')
const popupTitle = computed(() => {
  return mode.value == 'edit' ? '编辑管理员' : '新增管理员'
})

const formData = reactive({
  id: 0,
  account: '',
  nickname: '',
  deptIds: [],
  roleIds: [],
  avatar: '',
  password: '',
  passwordConfirm: '',
  userState: 0,
  email: '',
  remark: '',
})

const isRoot = computed(() => {
  return formData.id == 1
})

// 头像上传成功
const uploadSuccess = (url: string) => {
  formData.avatar = url
}

const passwordConfirmValidator = (
  rule: object,
  value: string,
  callback: any,
) => {
  if (formData.password) {
    if (!value) callback(new Error('请再次输入密码'))
    if (value !== formData.password) callback(new Error('两次输入密码不一致!'))
  }
  callback()
}
const formRules = reactive({
  account: [
    {
      required: true,
      message: '请输入账号',
      trigger: ['blur'],
    },
  ],
  avatar: [
    {
      required: true,
      message: '请上传头像',
      trigger: ['blur'],
    },
  ],
  nickname: [
    {
      required: true,
      message: '请输入名称',
      trigger: ['blur'],
    },
  ],
  roleIds: [
    {
      type: 'array',
      required: true,
      message: '请选择角色',
      trigger: ['blur'],
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
  ] as any[],
  passwordConfirm: [
    {
      required: true,
      message: '请再次输入密码',
      trigger: 'blur',
    },
    {
      validator: passwordConfirmValidator,
      trigger: 'blur',
    },
  ] as any[],
})

const { optionsData } = useDictOptions<{
  role: any[]
  dept: any[]
}>({
  role: {
    api: roleAll,
  },
  dept: {
    api: deptLists,
  },
})

const handleSubmit = async () => {
  await formRef.value?.validate()
  mode.value == 'edit' ? await adminEdit(formData) : await adminAdd(formData)
  popupRef.value?.close()
  feedback.msgSuccess('操作成功')
  emit('success')
}

const open = (type = 'add') => {
  mode.value = type
  popupRef.value?.open()
}

const setFormData = async (row: any) => {
  formRules.password = []
  formRules.passwordConfirm = [
    {
      validator: passwordConfirmValidator,
      trigger: 'blur',
    },
  ]
  const data = await adminDetail({
    id: row.id,
  })
  for (const key in formData) {
    if (data[key] != null && data[key] != undefined) {
      formData[key] = data[key]
    }
  }
}

const handleClose = () => {
  emit('close')
}
defineExpose({
  open,
  setFormData,
})
</script>
