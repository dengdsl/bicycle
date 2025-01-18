import request from '@/utils/request'

export interface SystemConfigMo {
  id: number
  sort: number
  name: string
  value: string | number
  remark: string
  type: string
  label: string
}

/**
 * 获取系统配置列表
 * */
export function getConfigList() {
  return request.get<Array<SystemConfigMo>>(
    {
      url: '/config/list',
    },
    { isParamsToData: false },
  )
}

/**
 * 获取单个系统配置
 * */
export function getConfig(data:string[]) {
  return request.post(
    {
      url: '/config/get',
      data,
    },
    { isParamsToData: false },
  )
}

export interface SaveConfigRequest {
  id: number
  value: string
}

/**
 * 保存系统配置列表
 * */
export function saveConfigList(data: SaveConfigRequest[]) {
  return request.post(
    {
      url: '/config/save',
      data,
    },
    { isParamsToData: false },
  )
}
