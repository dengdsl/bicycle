<template>
  <div class="supplier-list">
    <el-card class="!border-none" shadow="never">
      <el-form inline :model="queryParams" class="mb-[-16px]">
        <el-form-item label="ID">
          <el-input
            v-model="queryParams.id"
            @keyup.enter="getLists"
            placeholder="请输入编号"
          />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-select
            v-model="queryParams.proName"
            filterable
            placeholder="请选择"
            clearable
            style="width: 200px"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="item in dictData.proName"
              :key="item.id"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品型号">
          <el-select
            v-model="queryParams.model"
            filterable
            placeholder="请选择"
            clearable
            style="width: 200px"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="item in dictData.model"
              :key="item.id"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="产品编号">
          <el-input
            v-model="queryParams.frameNo"
            @keyup.enter="getLists"
            placeholder="请输入产品编号"
          />
        </el-form-item>
        <el-form-item label="结论">
          <el-select
            v-model="queryParams.conclusion"
            placeholder="请选择"
            clearable
            style="width: 200px"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="item in dictData.conclusion"
              :key="item.id"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="空孔">
          <el-select
            v-model="queryParams.hollowHole"
            placeholder="请选择"
            clearable
            style="width: 200px"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="item in dictData.hollowHole"
              :key="item.id"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="内折">
          <el-select
            v-model="queryParams.inFold"
            placeholder="请选择"
            clearable
            style="width: 200px"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="item in dictData.inFold"
              :key="item.id"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="乱纱">
          <el-select
            v-model="queryParams.raveling"
            placeholder="请选择"
            clearable
            style="width: 200px"
          >
            <el-option label="全部" value="" />
            <el-option
              v-for="item in dictData.raveling"
              :key="item.id"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="生产日期">
          <el-date-picker
            v-model="produceTime"
            type="daterange"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            @change="handleChange"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="resetPage">搜索</el-button>
          <el-button @click="resetParams">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="!border-none mt-4" shadow="never">
      <el-space direction="horizontal" alignment="start" :size="10">
        <el-button v-perms="['bicycle:add']" type="primary" @click="handleAdd">
          <template #icon>
            <icon name="el-icon-Plus" />
          </template>
          新增
        </el-button>
        <el-button
          v-perms="['bicycle:import']"
          type="primary"
          @click="handleImport"
        >
          <template #icon>
            <icon name="el-icon-UploadFilled" />
          </template>
          批量导入
        </el-button>
        <el-button
          v-perms="['bicycle:export']"
          type="primary"
          :loading="exportLoading"
          @click="handleExport"
        >
          <template #icon>
            <icon name="el-icon-Download" />
          </template>
          {{ selectIds.btnText }}
        </el-button>
        <el-button
          v-if="selectIds.isSelected"
          type="info"
          @click="handleClearSelect"
        >
          <template #icon>
            <icon name="el-icon-Close" />
          </template>
          批量取消{{ selectIds.selectNum }}条勾选
        </el-button>
      </el-space>

      <div class="supplier-list mt-2">
        <el-table
          ref="tableRef"
          :data="pager.lists"
          row-key="id"
          v-loading="pager.loading"
          @select-all="handleSelectChange"
          @select="handleSelectChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column
            prop="id"
            label="ID"
            align="center"
            min-width="180"
          />
          <el-table-column
            prop="proName"
            label="产品名称"
            align="center"
            min-width="200"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <dict-value
                :options="dictData.proName"
                :value="row.proName"
              ></dict-value>
            </template>
          </el-table-column>
          <el-table-column
            prop="qrImg"
            label="二维码"
            align="center"
            min-width="80"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div class="flex items-center justify-center gap-2">
                <el-image
                  style="width: 50px; height: 50px; flex-shrink: 0"
                  :src="row.qrImg"
                  fit="fill"
                  :zoom-rate="2"
                  :max-scale="7"
                  :min-scale="0.2"
                  :preview-src-list="[row.qrImg]"
                  :preview-teleported="true"
                />
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="image"
            label="X光图片"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div
                class="image-box flex items-center justify-center gap-2 overflow-x-auto"
              >
                <template
                  v-for="(src, index) in row.image?.split(';')"
                  :key="src"
                >
                  <el-image
                    style="width: 50px; height: 50px; flex-shrink: 0"
                    :src="src"
                    fit="fill"
                    :zoom-rate="2"
                    :max-scale="7"
                    :min-scale="0.2"
                    :preview-src-list="row.image.split(';')"
                    :initial-index="index"
                    :preview-teleported="true"
                  />
                </template>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="frameNo"
            label="产品编号"
            align="center"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column
            prop="model"
            label="产品型号"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <dict-value
                :options="dictData.model"
                :value="row.model"
              ></dict-value>
            </template>
          </el-table-column>
          <el-table-column
            prop="produceTime"
            label="生产日期"
            align="center"
            min-width="120"
          />

          <el-table-column
            prop="conclusion"
            label="结论"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-tag :type="row.conclusion == 1 ? 'success' : 'primary'">
                <dict-value
                  :options="dictData.conclusion"
                  :value="row.conclusion"
                ></dict-value>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="hollowHole"
            label="空孔"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-tag :type="row.hollowHole == 1 ? 'success' : 'danger'">
                <dict-value
                  :options="dictData.hollowHole"
                  :value="row.hollowHole"
                ></dict-value>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="inFold"
            label="内折"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-tag :type="row.inFold == 1 ? 'success' : 'danger'">
                <dict-value
                  :options="dictData.inFold"
                  :value="row.inFold"
                ></dict-value>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="raveling"
            label="乱纱"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <el-tag :type="row.raveling == 1 ? 'success' : 'danger'">
                <dict-value
                  :options="dictData.raveling"
                  :value="row.raveling"
                ></dict-value>
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            align="center"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column
            prop="createTime"
            label="创建时间"
            align="center"
            min-width="180"
          />
          <el-table-column
            prop="updateTime"
            label="更新时间"
            align="center"
            min-width="180"
          />
          <el-table-column
            label="操作"
            align="center"
            min-width="200"
            fixed="right"
          >
            <template #default="{ row }">
              <el-button
                v-perms="['bicycle:edit']"
                type="primary"
                link
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button
                v-perms="['bicycle:detail']"
                type="primary"
                link
                @click="handleDetail(row)"
              >
                详情
              </el-button>
              <el-button
                v-perms="['bicycle:delete']"
                type="danger"
                link
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container mt-4 flex justify-end">
          <papination v-model="pager" @change="handlePaginationChange" />
        </div>
      </div>
    </el-card>
    <!-- 编辑 -->
    <edit-popup
      ref="editRef"
      v-if="showEdit"
      @success="getLists"
      @close="showEdit = false"
    />
    <!-- 详情 -->
    <div>
      <el-drawer
        v-model="drawer"
        destroy-on-close
        :close-on-click-modal="false"
        title="详情"
        :direction="direction"
        size="50%"
        :before-close="handleClose"
      >
        <template #default>
          <Detail ref="detailRef"></Detail>
        </template>
      </el-drawer>
    </div>
    <!-- 批量导入 -->
    <el-dialog v-model="showImport">
      <template #header>
        <div class="my-header">批量导入</div>
      </template>
      <div class="content">
        <!-- ,.xls -->
        <el-upload
          v-model:file-list="fileList"
          class="upload-demo"
          ref="uploadRef"
          :auto-upload="false"
          drag
          accept=".xlsx"
          :limit="1"
          :on-exceed="handleExceed"
        >
          <el-icon class="el-icon--upload">
            <upload-filled />
          </el-icon>
          <div class="el-upload__text">
            支持文件拖拽上传或者
            <em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              *仅支持
              <b class="text-danger">.xlsx</b>
              文件,
              <b class="text-danger">必须满足文件内容的格式</b>
              ，否则会导致导入失败，文件模板可通过点击此处
              <b
                class="text-primary"
                style="cursor: pointer"
                @click="downloadTemplate"
              >
                下载模板
              </b>
            </div>
          </template>
        </el-upload>
      </div>

      <template #footer>
        <div class="footer-button text-center">
          <el-button @click="showImport = false">取消</el-button>
          <el-button
            type="primary"
            :loading="importLoading"
            @click="submitUpload"
          >
            上传
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import EditPopup from './edit.vue'
import { usePaging } from '@/hooks/usePaging.ts'
import feedback from '@/utils/feedback.ts'
import {
  getBicycleList,
  deleteBicycle,
  importBicycleList,
  downloadTemplate,
  exportBicycleList,
} from '@/api/bicycle'
import Detail from './detail.vue'
import {
  ElTable,
  genFileId,
  UploadInstance,
  UploadProps,
  UploadRawFile,
  UploadUserFile,
} from 'element-plus'
import { useDictData } from '@/hooks/useDictOptions.ts'

const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const detailRef = shallowRef<InstanceType<typeof Detail>>()
const tableRef = shallowRef<InstanceType<typeof ElTable>>()
const uploadRef = ref<UploadInstance>()
const showImport = ref(false)
const importLoading = ref(false)
const exportLoading = ref(false)
const fileList = ref<UploadUserFile[]>([])
const selectRows = reactive<Record<string, Array<string>>>({})

const showEdit = ref(false)
const drawer = ref(false)
const direction = ref<'rtl' | 'ltr' | 'ttb' | 'btt'>('rtl')
const produceTime = ref<string[]>([])
const queryParams = reactive({
  id: '',
  proName: '',
  model: '',
  frameNo: '',
  conclusion: '',
  hollowHole: '',
  inFold: '',
  raveling: '',
  produceTimeStart: '',
  produceTimeEnd: '',
})

const { dictData } = useDictData<{
  model: any[]
  conclusion: any[]
  hollowHole: any[]
  inFold: any[]
  raveling: any[]
  proName: any[]
}>(['model', 'conclusion', 'hollowHole', 'inFold', 'raveling', 'proName'])

const { getLists, pager, resetPage, resetParams } = usePaging({
  fetchFn: getBicycleList,
  params: queryParams,
})

const selectIds = computed(() => {
  const ids = Object.values(selectRows).flat()
  return {
    btnText: ids.length > 0 ? `批量导出${ids.length}条` : '批量导出全部',
    isSelected: !!ids.length,
    selectNum: ids.length,
    ids,
  }
})

// 筛选时间发生变化
const handleChange = (dates: string[] | null) => {
  if (dates) {
    queryParams.produceTimeStart = `${dates[0]} 00:00:00`
    queryParams.produceTimeEnd = `${dates[1]} 23:59:59`
  } else {
    queryParams.produceTimeStart = ''
    queryParams.produceTimeEnd = ''
  }
}

// 勾选发生变化
const handleSelectChange = (selectList: any) => {
  // 如果当前数据全部选中，将当前选中的数据全部添加到数据列表进行保存
  if (selectList.length) {
    selectRows[pager.page] = selectList.map((item: any) => item.id)
  } else {
    // 如果不存在，删除数据列表中的键值对
    if (Object.prototype.hasOwnProperty.call(selectRows, pager.page)) {
      delete selectRows[pager.page]
    }
  }
}
// 分页发生变化
const handlePaginationChange = async () => {
  await getLists()
  try {
    await getLists()
    await nextTick()
    if (!selectRows[pager.page]) return
    tableRef.value?.clearSelection()
    pager.lists.forEach((row) => {
      if (selectRows[pager.page].includes(row.id)) {
        tableRef.value?.toggleRowSelection(row, true)
      }
    })
  } catch (err) {
    console.log('err==>', err)
  }
}
// 清空勾选
const handleClearSelect = () => {
  tableRef.value?.clearSelection()
  Object.keys(selectRows).forEach((key) => {
    delete selectRows[key]
  })
}

// 上传时覆盖前一个文件
const handleExceed: UploadProps['onExceed'] = (files) => {
  uploadRef.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  uploadRef.value!.handleStart(file)
}

const handleClose = (done: () => void) => {
  done()
}

// 批量导入文件
const submitUpload = async () => {
  try {
    if (fileList.value.length == 0) {
      return feedback.msgError('请上传excel文件')
    }

    importLoading.value = true
    console.log(fileList.value[0].raw)

    await importBicycleList({ file: fileList.value[0].raw })
    feedback.msgSuccess('上传成功')
    // 清空文件
    uploadRef.value!.clearFiles()
    showImport.value = false
    // 重新获取数据列表
    getLists()
  } catch (err) {
    console.log('err==>', err)
    feedback.msgError('上传失败，请联系管理员进行处理')
  } finally {
    importLoading.value = false
  }
}

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
  editRef.value?.loadData(row.id)
}

// 查看详情
const handleDetail = async (row: any) => {
  drawer.value = true
  await nextTick()
  detailRef.value?.loadData(row.id)
}

// 删除
const handleDelete = async (row: any) => {
  try {
    const res = await feedback.confirm('确定要删除吗？')
    if (res === 'confirm') {
      await deleteBicycle({ id: row.id })
      feedback.msgSuccess('删除成功')
      getLists()
    }
  } catch (err) {
    console.log('err ==>', err)
  }
}

// 批量导入
const handleImport = () => {
  showImport.value = true
}
const handleExport = async () => {
  try {
    exportLoading.value = true
    const res = await feedback.confirm(
      `确定要${selectIds.value.btnText}吗？`,
      '防误操作提示',
    )
    if (res === 'confirm') {
      await exportBicycleList({ ids: selectIds.value.ids || [] })
    }
  } catch (err) {
    if ('cancel' === err) return
    console.log('err ==>', err)
    feedback.msgError('导出失败，请联系管理员进行处理')
    await Promise.reject()
  } finally {
    exportLoading.value = false
  }
}

getLists()
</script>

<style scoped>
.image-box::-webkit-scrollbar {
  display: none;
}
</style>
