import request from '@/utils/request'

/**
 * 获取字典列表
 * */
export function dictList(params: any) {
  return request.get(
    {
      url: '/dict/list',
      params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 获取所有字典类型
 * */
export function dictAll() {
  return request.get({
    url: '/dict/all',
  })
}

/**
 * 新增字典类型
 * */
export function dictAdd(params: any) {
  return request.post(
    {
      url: '/dict/add',
      data: params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 获取字典类型详情
 * */
export function getDictDetail(params: any) {
  return request.get(
    {
      url: '/dict/detail',
      params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 编辑字典类型
 * */
export function dictEdit(params: any) {
  return request.post(
    {
      url: '/dict/edit',
      data: params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 删除字典类型
 * */
export function dictDelete(params: any) {
  return request.post(
    {
      url: '/dict/delete',
      params,
    },
    { ignoreCancelToken: true, isParamsToData: false },
  )
}

/**
 * 获取字典数据列表，带分页
 * */
export function dictDataList(params: any) {
  return request.get(
    {
      url: '/dict/dataList',
      params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 获取字典数据列表，不带分页
 * */
export function dictDataAll(params: any) {
  return request.get(
    {
      url: '/dict/allDictData',
      params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 新增字典数据
 * */
export function dictDataAdd(params: any) {
  return request.post(
    {
      url: '/dict/addData',
      data: params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 编辑字典数据
 * */
export function dictDataEdit(params: any) {
  return request.post(
    {
      url: '/dict/editData',
      data: params,
    },
    { ignoreCancelToken: true },
  )
}

/**
 * 删除字典数据
 * */
export function dictDataDelete(params: any) {
  return request.post(
    {
      url: '/dict/deleteData',
      params: params,
    },
    { ignoreCancelToken: true, isParamsToData: false },
  )
}

/**
 * 禁用/启用字典数据
 * */
export function getDictDataDetail(params: any) {
  return request.get(
    {
      url: '/dict/dataDetail',
      params: params,
    },
    { ignoreCancelToken: true },
  )
}
