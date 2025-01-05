import request from '@/utils/request'

/**
 * 获取角色列表
 * */
export function roleLists(params: any) {
  return request.get({
    url: '/system/roleLists',
    params,
  })
}

/**
 * 获取所有角色
 * */
export function roleAll() {
  return request.get({
    url: '/system/roleAll',
  })
}

/**
 * 新增角色
 * */
export function roleAdd(data: any) {
  return request.post({
    url: '/system/addRole',
    data,
  })
}

/**
 * 编辑角色
 * */
export function roleEdit(data: any) {
  return request.post({
    url: '/system/editRole',
    data,
  })
}

/**
 * 获取角色详情
 * */
export function roleDetail(params: any) {
  return request.get({
    url: '/system/roleDetail',
    params,
  })
}

/**
 * 删除角色
 * */
export function roleDelete(params: any) {
  return request.post({
    url: '/system/roleDelete',
    params,
  })
}

/**
 * 角色授权
 * */
export function roleAuth(data: any) {
  return request.post({
    url: '/system/roleAuth',
    data,
  })
}

/**
 * 获取角色权限列表
 * */
export function roleAuthLists(params: any) {
  return request.get({
    url: '/system/roleMenus',
    params,
  })
}
