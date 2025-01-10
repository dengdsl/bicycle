<template>
  <div class="supplier-list">
    <el-card class="!border-none" shadow="never">
      <el-form inline :model="queryParams" class="mb-[-16px]">
        <el-form-item label="编号">
          <el-input
            v-model="queryParams.id"
            @keyup.enter="getLists"
            placeholder="请输入编号"
          />
        </el-form-item>
        <el-form-item label="名称">
          <el-input
            v-model="queryParams.title"
            @keyup.enter="getLists"
            placeholder="请输入名称"
          />
        </el-form-item>
        <!-- <el-form-item label="其他">
          <el-select v-model="queryParams.isDel" placeholder="请选择" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option v-for="item in dictData.isDel" :key="item.id" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item> -->
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
          @click="handleExport"
        >
          <template #icon>
            <icon name="el-icon-Download" />
          </template>
          批量导出
        </el-button>
      </el-space>

      <div class="supplier-list mt-2">
        <el-table
          :data="pager.lists"
          v-loading="pager.loading"
          @selection-change="handleSelectChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column
            prop="id"
            label="编号"
            align="center"
            min-width="200"
          />
          <el-table-column
            prop="title"
            label="名称"
            align="center"
            min-width="120"
            show-overflow-tooltip
          />
          <el-table-column
            prop="image"
            label="图片"
            align="center"
            min-width="300"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div class="flex items-center justify-center gap-2">
                <template
                  v-for="(src, index) in row.image.split(';')"
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
          <papination v-model="pager" @change="getLists" />
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
              <b style="color: red">.xlsx</b>
              文件,
              <b style="color: red">必须满足文件内容的格式</b>
              ，否则会导致导入失败，文件模板可通过点击此处
              <b
                style="color: #4a5dff; cursor: pointer"
                @click="handleDownload"
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
import { getBicycleList, deleteBicycle, importBicycleList } from '@/api/bicycle'
import Detail from './detail.vue'
import {
  genFileId,
  UploadInstance,
  UploadProps,
  UploadRawFile,
  UploadUserFile,
} from 'element-plus'

const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const detailRef = shallowRef<InstanceType<typeof Detail>>()
const uploadRef = ref<UploadInstance>()
const showImport = ref(false)
const importLoading = ref(false)
const fileList = ref<UploadUserFile[]>([])
const selectRows = ref<any[]>([])

const showEdit = ref(false)
const drawer = ref(false)
const direction = ref<'rtl' | 'ltr' | 'ttb' | 'btt'>('rtl')
const queryParams = reactive({
  id: '',
  title: '',
  isDel: '',
})

const { getLists, pager, resetPage, resetParams } = usePaging({
  fetchFn: getBicycleList,
  params: queryParams,
})

// 勾选发生变化
const handleSelectChange = (newSelection: any[]) => {
  selectRows.value = newSelection
  console.log('selectRows.value ==>', selectRows.value)
  // toggleRowSelection
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

// 下载服务工单模板文件
const handleDownload = () => {
  // const link = document.createElement('a')
  // link.href = downloadUrl
  // const url = decodeURIComponent(downloadUrl)
  // const urlSuffix = url.split("/")[1]
  // link.download = urlSuffix.split(".")[0]
  // document.body.appendChild(link)
  // link.click()
  // document.body.removeChild(link)
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
const handleExport = () => {}

getLists()
</script>

<style scoped></style>
