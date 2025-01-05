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
export function addBicycle(params: any) {
  return request.post(
    {
      url: '/bicycle/add',
      params,
    },
    { isParamsToData: false },
  )
}

/**
 * 编辑数据列表
 * */
export function editBicycle(params: any) {
  return request.post(
    {
      url: '/bicycle/edit',
      params,
    },
    { isParamsToData: false },
  )
}

/**
 * 删除数据列表
 * */
export function deleteBicycle(params: any) {
  return request.get({
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
export function importBicycleList(params: any) {
  return request.get({
    url: '/bicycle/import',
    params,
  })
}

/**
 * 批量导出数据列表
 * */
export function exportBicycleList(params: any) {
  return request.get({
    url: '/bicycle/export',
    params,
  })
}
