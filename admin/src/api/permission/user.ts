import request from '@/utils/request'

/**
 * 获取管理员列表
 * */
export function adminLists(params: any) {
  return request.get({
    url: '/system/userLists',
    params,
  })
}

/**
 * 新增管理员
 * */
export function adminAdd(data: any) {
  return request.post({
    url: '/system/addUser',
    data,
  })
}

/**
 * 编辑管理员
 * */
export function adminEdit(data: any) {
  return request.post({
    url: '/system/editUser',
    data,
  })
}

/**
 * 获取管理员详情
 * */
export function adminDetail(params: any) {
  return request.get({
    url: '/system/userDetail',
    params,
  })
}

/**
 * 删除管理员
 * */
export function adminDelete(params: any) {
  return request.post({
    url: '/system/deleteUser',
    params,
  })
}

/**
 * 启用管理员
 * */
export function adminStatus(params: { id: string | number }) {
  return request.get({
    url: '/system/userStatus',
    params,
  })
}
