<template>
  <div class="dict-lists">
    <el-card class="!border-none" shadow="never">
      <el-form ref="formRef" class="mb-[-16px]" v-model="queryParams" :inline="true">
        <el-form-item label="字典类型" prop="serviceOrderSn">
          <el-input class="w-[280px]" v-model="queryParams.dictType" placeholder="请输入字典类型" />
        </el-form-item>
        <el-form-item label="字典名称" prop="userName">
          <el-input class="w-[280px]" v-model="queryParams.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典状态" prop="serviceType">
          <el-select class="w-[280px]" v-model="queryParams.dictStatus" style="width: 200px">
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
      <el-button v-perms="['dict:add']" type="primary" @click="handleAdd">
        <template #icon>
          <icon name="el-icon-Plus" />
        </template>
        新增
      </el-button>
      <div class="dict-list">
        <el-table ref="multipleTable" v-loading="pager.loading" :data="pager.lists" tooltip-effect="dark" style="width: 100%">
          <el-table-column align="center" center type="selection" />
          <el-table-column align="center" prop="id" label="ID" min-width="20" />
          <el-table-column align="center" prop="dictType" label="字典类型" />
          <el-table-column align="center" prop="dictName" label="字典名称" />
          <el-table-column align="center" prop="dictStatus" label="状态">
            <template #default="{ row }">
              <el-tag v-if="row.dictStatus == 1">正常</el-tag>
              <el-tag v-if="row.dictStatus == 0">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="dictRemark" label="备注" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" min-width="80" />
          <el-table-column prop="updateTime" label="更新时间" min-width="80" />
          <el-table-column prop="updateTime" label="操作">
            <template #default="{ row }">
              <el-button v-perms="['dict:edit']" type="primary" link @click="handleEdit(row)"> 编辑 </el-button>
              <el-button v-perms="['dict:dataList']" type="primary" link @click="router.push('/tools/dictionary/data?type=' + row.dictType)"> 数据管理 </el-button>
              <el-button v-perms="['dict:delete']" type="danger" link @click="handleDelete(row)"> 删除 </el-button>
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
import EditPopup from './edit.vue'
import { usePaging } from '@/hooks/usePaging.ts'
import { dictList, dictDelete } from '@/api/dict'
import Pagination from '@/components/papination/index.vue'
import feedback from '@/utils/feedback.ts'

const showEdit = ref(false)
const editRef = shallowRef<InstanceType<typeof EditPopup>>()

const router = useRouter()

const queryParams = reactive<string, any>({
  dictType: '', // 字典类型
  dictName: '', // 字典名称
  dictStatus: '' // 数据状态
})

const { resetParams, resetPage, pager, getLists } = usePaging({
  params: queryParams,
  fetchFn: dictList
})

// 新增字典类型
const handleAdd = async () => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('add')
}

// 编辑字典类型
const handleEdit = async (row: any) => {
  showEdit.value = true
  await nextTick()
  editRef.value?.open('edit')
  editRef.value?.getDetail(row.id)
}

const handleDelete = async (row: any) => {
  try {
    console.log(row.id)
    const res = await feedback.confirm('确定要删除该字典类型吗？')
    if (res === 'confirm') {
      await dictDelete({ id: row.id })
      feedback.msgSuccess('删除成功')
      getLists()
    }
  } catch {}
}

getLists()
</script>
