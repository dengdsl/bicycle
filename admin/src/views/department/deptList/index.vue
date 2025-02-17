<template>
  <div class="dept-lists">
    <el-card class="!border-none" shadow="never">
      <el-form ref="formRef" class="mb-[-16px]" :model="queryParams" :inline="true">
        <el-form-item label="部门名称" prop="name">
          <el-input class="w-[280px]" v-model="queryParams.name" clearable placeholder="请输入部门名称" @keyup.enter="getLists" />
        </el-form-item>
        <el-form-item label="部门状态" prop="isStop">
          <el-select class="w-[280px]" v-model="queryParams.isStop" style="width: 200px">
            <el-option label="全部" value />
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getLists">查询</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="!border-none mt-4" shadow="never">
      <div>
        <el-button v-perms="['system:deptAdd']" type="primary" @click="handleAdd()">
          <template #icon>
            <icon name="el-icon-Plus" />
          </template>
          新增
        </el-button>
        <el-button @click="handleExpand">展开/折叠</el-button>
      </div>
      <el-table ref="tableRef" class="mt-4" size="large" v-loading="loading" :data="lists" row-key="id" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column label="部门名称" prop="deptName" min-width="150" show-overflow-tooltip />
        <el-table-column label="部门负责人" prop="duty" min-width="100" />
        <el-table-column label="负责人手机号" prop="mobile" min-width="100" />
        <el-table-column label="部门状态" prop="isStop" min-width="100">
          <template #default="{ row }">
            <el-tag class="ml-2" :type="row.isStop ? 'danger' : ''">
              {{ row.isStop ? '停用' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序" prop="sort" min-width="100" />
        <el-table-column label="更新时间" prop="updateTime" min-width="180" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button v-perms="['system:deptAdd']" type="primary" link @click="handleAdd(row.id)"> 新增 </el-button>
            <el-button v-perms="['system:deptEdit']" type="primary" link @click="handleEdit(row)"> 编辑 </el-button>
            <el-button v-if="row.pid !== 0" v-perms="['system:deptDelete']" type="danger" link @click="handleDelete(row.id)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <edit-popup v-if="showEdit" ref="editRef" @success="getLists" @close="showEdit = false" />
  </div>
</template>
<script lang="ts" setup name="department">
import type { ElTable, FormInstance } from 'element-plus'
import EditPopup from './edit.vue'
import { deptDelete, deptLists } from '@/api/department'
import feedback from '@/utils/feedback'
const tableRef = shallowRef<InstanceType<typeof ElTable>>()
const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const formRef = shallowRef<FormInstance>()
let isExpand = false
const loading = ref(false)
const lists = ref<any[]>([])
const queryParams = reactive({
  isStop: '',
  name: ''
})
const showEdit = ref(false)
const getLists = async () => {
  loading.value = true
  lists.value = await deptLists(queryParams)
  loading.value = false
}

const resetParams = () => {
  formRef.value?.resetFields()
  getLists()
}

const handleAdd = async (id?: number) => {
  showEdit.value = true
  await nextTick()
  if (id) {
    editRef.value?.setFormData({
      parentId: id
    })
  }
  editRef.value?.open('add')
}

const handleEdit = async (data: any) => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('edit')
  editRef.value?.getDetail(data)
}

const handleDelete = async (id: number) => {
  await feedback.confirm('确定要删除？')
  await deptDelete({ id })
  feedback.msgSuccess('删除成功')
  getLists()
}

const handleExpand = () => {
  isExpand = !isExpand
  toggleExpand(lists.value, isExpand)
}

const toggleExpand = (children: any[], unfold = true) => {
  for (const key in children) {
    tableRef.value?.toggleRowExpansion(children[key], unfold)
    if (children[key].children) {
      toggleExpand(children[key].children!, unfold)
    }
  }
}

onMounted(async () => {
  await getLists()
  nextTick(() => {
    handleExpand()
  })
})
</script>
