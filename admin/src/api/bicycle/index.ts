import { ContentTypeEnum } from '@/enums/httpEnum'
import request from '@/utils/request'

// 列表请求参数
interface ListRequest {
  id: string
  model: string
  frameNo: string
  conclusion: string
  produceTime: string
}

// 列表相应结果
interface ListResponse {
  id: string
  model: string
  frameNo: string
  conclusion: string
  produceTime: string
}

/**
 * 获取数据列表
 * */
export function getBicycleList(params: ListRequest) {
  return request.get<ListResponse>(
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

export interface ImportRequest {
  file: any
}

/**
 * 批量导入数据列表
 * */
export function importBicycleList(data: ImportRequest) {
  return request.post({
    url: '/bicycle/import',
    data,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_DATA,
    },
  })
}

export interface exportRequest {}

/**
 * 批量导出数据列表
 * */
export function exportBicycleList(params: exportRequest) {
  return request.post({
    url: '/bicycle/export',
    params,
  })
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
