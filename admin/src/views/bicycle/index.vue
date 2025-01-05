<template>
  <div class="supplier-list">
    <el-card class="!border-none" shadow="never">
      <el-form inline :model="queryParams" class="mb-[-16px]">
        <el-form-item label="编号">
          <el-input v-model="queryParams.supplierId" @keyup.enter="getLists" placeholder="请输入编号" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.supplierName" @keyup.enter="getLists" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item label="其他">
          <el-select v-model="queryParams.isDisabled" placeholder="请选择" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option v-for="item in dictData.isDisabled" :key="item.id" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetPage">搜索</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="!border-none mt-4" shadow="never">
      <el-space direction="horizontal" alignment="start" :size="10">
        <el-button v-perms="['supplier:add']" type="primary" @click="handleAdd">
          <template #icon>
            <icon name="el-icon-Plus" />
          </template>
          新增
        </el-button>
        <el-button v-perms="['supplier:add']" type="primary" @click="handleAdd">
          <template #icon>
            <icon name="el-icon-UploadFilled" />
          </template>
          批量导入
        </el-button>
        <el-button v-perms="['supplier:add']" type="primary" @click="handleAdd">
          <template #icon>
            <icon name="el-icon-Download" />
          </template>
          批量导出
        </el-button>
      </el-space>

      <div class="supplier-list mt-2">
        <el-table :data="pager.lists" v-loading="pager.loading">
          <el-table-column prop="supplierId" label="图片" align="center" min-width="200" />
          <el-table-column prop="supplierName" label="编号" align="center" min-width="120" show-overflow-tooltip />
          <el-table-column prop="supplierCode" label="说明" align="center" min-width="120" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" align="center" min-width="180" />
          <el-table-column prop="updateTime" label="更新时间" align="center" min-width="180" />
          <el-table-column label="操作" align="center" min-width="200" fixed="right">
            <template #default="{ row }">
              <el-button v-perms="['supplier:edit']" type="primary" link @click="handleEdit(row)"> 编辑 </el-button>
              <el-button v-perms="['supplier:detail']" type="primary" link @click="handleEdit(row)"> 详情 </el-button>
              <el-button v-perms="['supplier:delete']" type="danger" link @click="handleDelete(row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container mt-4 flex justify-end">
          <papination v-model="pager" @change="getLists" />
        </div>
      </div>
    </el-card>
    <edit-popup ref="editRef" v-if="showEdit" @success="getLists" @close="showEdit = false" />
  </div>
</template>

<script setup lang="ts">
import EditPopup from './edit.vue'
import { usePaging } from '@/hooks/usePaging.ts'
import { useDictData } from '@/hooks/useDictOptions.ts'
import feedback from '@/utils/feedback.ts'
import { getBicycleList, editBicycle, addBicycle, deleteBicycle } from '@/api/bicycle'

const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const showEdit = ref(false)
const queryParams = reactive({
  supplierId: '',
  supplierName: '',
  supplierCode: '',
  isDisabled: ''
})

const { dictData } = useDictData<{
  isDisabled: any[]
}>(['isDisabled'])

const { getLists, pager, resetPage, resetParams } = usePaging({
  fetchFn: getBicycleList,
  params: queryParams
})

// 编辑

// 新增
const handleAdd = async () => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('add')
}

// 编辑
const handleEdit = async (row: any) => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('edit')
  await nextTick()
  editRef.value?.getSupplierDetail(row.supplierId)
}

// 删除
const handleDelete = async (row: any) => {
  try {
    // const res = await feedback.confirm('确定要删除当前供应商吗？')
    // if (res === 'confirm') {
    //   await supplierDelete({ id: row.supplierId })
    //   feedback.msgSuccess('删除成功')
    //   getLists()
    // }
  } catch {}
}

// 切换状态
const handleStatus = async (row: any) => {
  try {
    // const res = await feedback.confirm(`确定要${row.isDisabled == 1 ? '启用' : '禁用'}当前供应商吗？`)
    // if (res === 'confirm') {
    //   await supplierStatus({ id: row.supplierId })
    //   feedback.msgSuccess('启用成功')
    //   getLists()
    // }
  } catch {}
}
// getLists()
</script>

<style scoped></style>
