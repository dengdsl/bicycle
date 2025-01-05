import { ContentTypeEnum } from '@/enums/httpEnum'
import request from '@/utils/request'

/**
 * 获取数据列表
 * */
export function getBicycleList(params: any) {
  return request.get(
    {
      url: '/bicycle/list',
      params,
    },
    { isParamsToData: false },
  )
}

/**
 * 新增数据列表
 * */
export function addBicycle(data: any) {
  return request.post({
    url: '/bicycle/add',
    data,
  })
}

/**
 * 编辑数据列表
 * */
export function editBicycle(data: any) {
  return request.post({
    url: '/bicycle/edit',
    data,
  })
}

/**
 * 删除数据列表
 * */
export function deleteBicycle(params: any) {
  return request.post({
    url: '/bicycle/delete',
    params,
  })
}
/**
 * 获取自行车详情
 * */
export function getBicycleDetail(params: any) {
  return request.get({
    url: '/bicycle/detail',
    params,
  })
}

/**
 * 批量导入数据列表
 * */
export function importBicycleList(data: any) {
  return request.post({
    url: '/bicycle/import',
    data,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_DATA,
    },
  })
}

/**
 * 批量导出数据列表
 * */
export function exportBicycleList(params: any) {
  return request.post({
    url: '/bicycle/export',
    params,
  })
}
