<template>
  <div class="edit-popup">
    <popup
      ref="popupRef"
      title="权限设置"
      :async="true"
      width="550px"
      @confirm="handleSubmit"
      @close="handleClose"
    >
      <el-form
        class="ls-form"
        ref="formRef"
        :model="formData"
        label-width="60px"
      >
        <el-scrollbar class="h-[400px] sm:h-[600px]">
          <el-form-item label="权限" prop="menus">
            <div>
              <el-checkbox label="展开/折叠" @change="handleExpand" />
              <el-checkbox label="全选/不全选" @change="handleSelectAll" />
              <el-checkbox v-model="checkStrictly" label="父子联动" />
              <div>
                <el-tree
                  ref="treeRef"
                  :data="menuTree"
                  :props="{
                    label: 'menuName',
                    children: 'children',
                  }"
                  :check-strictly="!checkStrictly"
                  node-key="id"
                  :default-expand-all="isExpand"
                  show-checkbox
                />
              </div>
            </div>
          </el-form-item>
        </el-scrollbar>
      </el-form>
    </popup>
  </div>
</template>
<script lang="ts" setup>
import type { CheckboxValueType, ElTree, FormInstance } from 'element-plus'
import {
  roleAuth,
  roleAuthLists,
  roleDetail,
  roleEdit,
} from '@/api/permission/role'
import { getMenus } from '@/api/permission/menu'
import Popup from '@/components/popup/index.vue'
import { treeToArray } from '@/utils/util'
import feedback from '@/utils/feedback'
const emit = defineEmits(['success', 'close'])
const treeRef = shallowRef<InstanceType<typeof ElTree>>()
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()
const isExpand = ref(false)
const checkStrictly = ref(true)
const menuArray = ref<any[]>([])
const menuTree = ref<any[]>([])
const formData = reactive({
  id: '',
  menus: [] as any[],
})

const getOptions = async () => {
  const data = await getMenus()
  menuTree.value = data
  menuArray.value = treeToArray(data)
}

// 获取所有选择的节点包括半选中节点
const getDeptAllCheckedKeys = () => {
  const checkedKeys = treeRef.value?.getCheckedKeys()
  const halfCheckedKeys = treeRef.value?.getHalfCheckedKeys()!
  checkedKeys?.unshift.apply(checkedKeys, halfCheckedKeys)
  return checkedKeys
}

const setDeptAllCheckedKeys = () => {
  formData.menus.forEach((v) => {
    nextTick(() => {
      treeRef.value?.setChecked(v, true, false)
    })
  })
}

const handleExpand = (check: CheckboxValueType) => {
  const treeList = menuTree.value
  for (let i = 0; i < treeList.length; i++) {
    treeRef.value.store.nodesMap[treeList[i].id].expanded = check
  }
}

const handleSelectAll = (check: CheckboxValueType) => {
  if (check) {
    treeRef.value?.setCheckedKeys(menuArray.value.map((item) => item.id))
  } else {
    treeRef.value?.setCheckedKeys([])
  }
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  formData.menus = getDeptAllCheckedKeys()!
  await roleAuth({
    ...formData,
    menuIds: formData.menus.sort((a, b) => a - b).join(),
  })
  popupRef.value?.close()
  feedback.msgSuccess('操作成功')
  emit('success')
}

const handleClose = () => {
  emit('close')
}

const open = () => {
  popupRef.value?.open()
}

const setFormData = async (row: Record<any, any>) => {
  formData.id = row.id
  await getOptions()
  formData.menus = await roleAuthLists({
    id: row.id,
  })
  nextTick(() => {
    setDeptAllCheckedKeys()
  })
}

defineExpose({
  open,
  setFormData,
})
</script>
