import request from '@/utils/request'

/**
 * 获取系统配置列表
 * */
export function getConfigList() {
  return request.get<ListResponse>(
    {
      url: '/config/list',
    },
    { isParamsToData: false },
  )
}

/**
 * 获取单个系统配置
 * */
export function getConfig(params: { id: number }) {
  return request.get<ListResponse>(
    {
      url: '/config/get',
      params,
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
export function saveConfigList(data: SaveConfigRequest) {
  return request.get<ListResponse>(
    {
      url: '/config/save',
      data,
    },
    { isParamsToData: false },
  )
}
