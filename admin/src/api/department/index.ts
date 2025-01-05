import request from '@/utils/request'

/**
 * 获取部门信息列表
 * */
export function deptLists(params: any) {
  return request.get({
    url: '/system/deptList',
    params,
  })
}

/**
 * 新增部门信息
 * */
export function deptAdd(data) {
  return request.post({
    url: '/system/deptAdd',
    data,
  })
}

/**
 * 修改部门信息
 * */
export function deptEdit(data) {
  return request.post({
    url: '/system/deptEdit',
    data,
  })
}

/**
 * 删除部门信息
 * */
export function deptDelete(params: any) {
  return request.post({
    url: '/system/deptDelete',
    params,
  })
}

/**
 * 获取部门信息
 * */
export function deptDetail(params: any) {
  return request.get({
    url: '/system/deptDetail',
    params,
  })
}
