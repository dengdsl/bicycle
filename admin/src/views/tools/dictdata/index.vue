<template>
  <div class="dict-data">
    <el-card class="!border-none" shadow="never">
      <el-page-header class="mb-4" content="数据管理" @back="$router.back()" />
      <el-form ref="formRef" class="mb-[-16px]" v-model="queryParams" :inline="true">
        <el-form-item label="字典类型" prop="serviceOrderSn">
          <el-select class="w-[280px]" v-model="queryParams.dictType">
            <el-option v-for="dict in dictTypeList" :key="dict.id" :label="dict.dictName" :value="dict.dictType" />
          </el-select>
        </el-form-item>
        <el-form-item label="字典名称" prop="userName">
          <el-input class="w-[280px]" v-model="queryParams.name" placeholder="请输入数据名称" />
        </el-form-item>
        <el-form-item label="数据状态" prop="serviceType">
          <el-select class="w-[280px]" v-model="queryParams.state" style="width: 200px">
            <el-option label="全部" value="" />
            <el-option label="正常" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetPage">查询</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="!border-none mt-5" shadow="never">
      <el-button v-perms="['dict:addData']" type="primary" @click="handleAdd">
        <template #icon>
          <icon name="el-icon-Plus" />
        </template>
        新增
      </el-button>
      <div class="dict-list">
        <el-table ref="multipleTable" v-loading="pager.loading" :data="pager.lists" tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="dictType" label="字典类型" />
          <el-table-column prop="name" label="数据名称" />
          <el-table-column prop="value" label="数据值" />
          <el-table-column label="数据状态">
            <template #default="{ row }">
              <el-tag v-if="row.state == 1">正常</el-tag>
              <el-tag v-if="row.state == 0">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="数据备注" />
          <el-table-column label="操作" fixed="right">
            <template #default="{ row }">
              <el-button v-perms="['dict:editData']" type="primary" link @click="handleEdit(row)"> 编辑 </el-button>
              <el-button v-perms="['dict:deleteData']" type="danger" link @click="handleDelete(row)"> 删除 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="flex justify-end mt-4">
          <pagination v-model="pager" @change="getLists" />
        </div>
      </div>
    </el-card>

    <edit-popup v-if="showEdit" ref="editRef" @success="getLists" @close="showEdit = false" />
  </div>
</template>

<script setup lang="ts">
import Pagination from '@/components/papination/index.vue'
import EditPopup from '@/views/tools/dictdata/edit.vue'
import { usePaging } from '@/hooks/usePaging.ts'
import { dictDataList, dictAll, dictDataDelete } from '@/api/dict'
import feedback from '@/utils/feedback.ts'

const showEdit = ref(false)
const route = useRoute()
const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const dictTypeList = ref<
  {
    id: string
    dictName: string
    dictType: string
  }[]
>([])

const queryParams = reactive({
  dictType: route.query.type,
  name: '',
  state: ''
})

const { resetPage, resetParams, pager, getLists } = usePaging({
  params: queryParams,
  fetchFn: dictDataList
})

// 获取所有的字典类型
const getAllDictType = async () => {
  const data = await dictAll()
  // 将所有数据保存到列表中
  dictTypeList.value = data.map((dict) => ({
    id: dict.id,
    dictName: dict.dictName,
    dictType: dict.dictType
  }))
}

const handleAdd = async () => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('add')
}

const handleEdit = async (row: any) => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('edit')
  editRef.value?.dictDataDetail(row.id)
}

const handleDelete = async (row: any) => {
  try {
    console.log(row.id)
    const res = await feedback.confirm('确定要删除该字典数据吗？')
    if (res === 'confirm') {
      await dictDataDelete({ id: row.id })
      feedback.msgSuccess('删除成功')
      getLists()
    }
  } catch {}
}

getAllDictType()
// 获取字典数据列表
getLists()
</script>
