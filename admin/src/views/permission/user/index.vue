<template>
  <div class="admin">
    <el-card class="!border-none" shadow="never">
      <el-form class="mb-[-16px]" :model="formData" inline>
        <el-form-item label="登录账号">
          <el-input v-model="formData.account" class="w-[280px]" clearable placeholder="请输入管理员账号" @keyup.enter="resetPage" />
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input v-model="formData.nickname" class="w-[280px]" clearable placeholder="请输入管理员名称" @keyup.enter="resetPage" />
        </el-form-item>
        <el-form-item label="管理员角色">
          <el-select class="w-[280px]" v-model="formData.roleId" style="width: 200px">
            <el-option label="全部" value="" />
            <el-option v-for="(item, index) in optionsData.role" :key="index" :label="item.roleName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetPage">查询</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-loading="pager.loading" class="mt-4 !border-none" shadow="never">
      <el-button v-perms="['system:addUser']" type="primary" @click="handleAdd">
        <template #icon>
          <icon name="el-icon-Plus" />
        </template>
        新增
      </el-button>
      <div class="mt-4">
        <el-table :data="pager.lists" size="large">
          <el-table-column label="ID" prop="id" min-width="60" />
          <el-table-column label="头像" min-width="100">
            <template #default="{ row }">
              <el-avatar :size="50" :src="row.avatar"></el-avatar>
            </template>
          </el-table-column>
          <el-table-column label="登录账号" prop="account" min-width="100" />
          <el-table-column label="用户昵称" prop="username" min-width="100" />
          <el-table-column label="角色" prop="roleName" show-tooltip-when-overflow min-width="100" />
          <el-table-column label="部门" prop="deptName" show-tooltip-when-overflow min-width="100" />
          <el-table-column label="创建时间" prop="createTime" min-width="180" />
          <el-table-column label="状态" min-width="100">
            <template #default="{ row }">
              <el-switch v-perms="['system:userStatus']" v-if="row.id != 1" :model-value="row.userState" :active-value="0" :inactive-value="0" @change="changeStatus($event, row.id)" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button v-perms="['system:editUser']" type="primary" link @click="handleEdit(row)"> 编辑 </el-button>
              <el-button v-if="row.id != 1" v-perms="['system:deleteUser']" type="danger" link @click="handleDelete(row.id)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="flex mt-4 justify-end">
        <pagination v-model="pager" @change="getLists" />
      </div>
    </el-card>
    <edit-popup v-if="showEdit" ref="editRef" @success="getLists" @close="showEdit = false" />
  </div>
</template>

<script lang="ts" setup>
import { adminLists, adminDelete, adminStatus } from '@/api/permission/user'
import { roleAll } from '@/api/permission/role'
import { useDictOptions } from '@/hooks/useDictOptions'
import { usePaging } from '@/hooks/usePaging'
import Pagination from '@/components/papination/index.vue'
import feedback from '@/utils/feedback'
import EditPopup from './edit.vue'

const editRef = shallowRef<InstanceType<typeof EditPopup>>()

// 表单数据
const formData = reactive({
  account: '',
  nickname: '',
  roleId: ''
})
const showEdit = ref(false)
const { pager, getLists, resetParams, resetPage } = usePaging({
  fetchFn: adminLists,
  params: formData
})

const changeStatus = async (active: any, id: number) => {
  try {
    await feedback.confirm(`确定${active ? '停用' : '开启'}当前管理员？`)
    await adminStatus({ id })
    feedback.msgSuccess('修改成功')
    getLists()
  } catch (error) {
    getLists()
  }
}
const handleAdd = async () => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('add')
}

const handleEdit = async (data: any) => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('edit')
  editRef.value?.setFormData(data)
}

const handleDelete = async (id: number) => {
  await feedback.confirm('确定要删除？')
  await adminDelete({ id })
  feedback.msgSuccess('删除成功')
  getLists()
}

const { optionsData } = useDictOptions<{
  role: any[]
}>({
  role: {
    api: roleAll
  }
})

onMounted(() => {
  getLists()
})
</script>
