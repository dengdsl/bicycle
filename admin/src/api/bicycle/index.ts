import { ContentTypeEnum } from '@/enums/httpEnum'
import request from '@/utils/request'

// 列表请求参数
interface ListRequest {
  id: string
  model: string
  frameNo: string
  conclusion: string
  produceTimeStart: string
  produceTimeEnd: string
}

// 列表相应结果
export interface TableRow {
  id: string
  model: string
  frameNo: string
  conclusion: string
  produceTime: string
  createTime: string
  image: string
  qrImg: string
  qrcode: string
  updateTime: string
  remark: string
  isDel: number
}

/**
 * 获取数据列表
 * */
export function getBicycleList(params: ListRequest) {
  return request.get<Array<TableRow>>(
    {
      url: '/bicycle/list',
      params,
    },
    { isParamsToData: false },
  )
}

export interface AddBicycleRequest {}

/**
 * 新增数据列表
 * */
export function addBicycle(data: AddBicycleRequest) {
  return request.post({
    url: '/bicycle/add',
    data,
  })
}

export interface EditBicycleRequest {}

/**
 * 编辑数据列表
 * */
export function editBicycle(data: EditBicycleRequest) {
  return request.post({
    url: '/bicycle/edit',
    data,
  })
}

/**
 * 删除数据列表
 * */
export function deleteBicycle(params: { id: string }) {
  return request.post({
    url: '/bicycle/delete',
    params,
  })
}

/**
 * 获取自行车详情
 * */
export function getBicycleDetail(params: { id: string }) {
  return request.get({
    url: '/bicycle/detail',
    params,
  })
}

/**
 * 下载模版文件
 * */
export function downloadTemplate() {
  return request.get(
    {
      url: '/bicycle/download/template',
      responseType: 'blob',
      timeout: 30 * 60 * 1000,
    },
    { isDownloadFile: true },
  )
}

/**
 * 批量导入数据列表
 * */
export function importBicycleList(data: any) {
  return request.post(
    {
      url: '/bicycle/import',
      data,
      headers: {
        'Content-Type': ContentTypeEnum.FORM_DATA,
      },
      timeout: 30 * 60 * 1000,
    },
    { isDownloadFile: false },
  )
}

/**
 * 批量导出数据列表
 * */
export function exportBicycleList(data: { ids: string[] }) {
  return request.post(
    {
      url: '/bicycle/export',
      data,
      responseType: 'blob',
      timeout: 30 * 60 * 1000,
    },
    { isDownloadFile: true },
  )
}

/**
 * @description 批量导出数据列表
 * */
export function queryByQrcode(params: { qrcode: string }) {
  return request.get({
    url: '/bicycle/query',
    params,
  })
}
