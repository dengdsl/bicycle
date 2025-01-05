import request from '@/utils/request'

/**
 * 获取路由菜单列表
 * */
export function getMenus() {
  return request.get({
    url: '/system/menus',
  })
}

/**
 * 添加菜单
 * */
export function menuAdd(data: Record<string, any>) {
  return request.post({
    url: '/system/menu/add',
    data,
  })
}

/**
 * 编辑菜单
 * */
export function menuEdit(data: Record<string, any>) {
  return request.post({
    url: '/system/menu/edit',
    data,
  })
}

/**
 * 菜单删除
 * */
export function menuDelete(id: string | number) {
  return request.post(
    {
      url: `/system/menu/del`,
      params: { id },
    },
    { isParamsToData: false },
  )
}

/**
 * 菜单详情
 * */
export function menuDetail(id: string | number) {
  return request.get(
    {
      url: `/system/menu/detail`,
      params: { id },
    },
    { isParamsToData: false },
  )
}
