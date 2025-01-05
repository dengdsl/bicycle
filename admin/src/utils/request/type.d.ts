import 'axios'
import type { AxiosResponse, AxiosRequestConfig } from 'axios'

// 定义请求参数列表结构
export interface RequestOptions {
  // 是否将params当作data使用，仅限于post请求
  isParamsToData: boolean
  // 是否返回默认请求
  isReturnDefaultResponse: boolean
  // 是否对响应进行进行处理再发挥
  isTransformResponse: boolean
  // 是否忽略重复请i去
  ignoreCancelToken: boolean
  // 重复请求的次数
  retryCount: number
  // 是否开启重复请求
  isOpenRetry: boolean
}

// 后台响应的数据结构
export interface ResponseData<T = any> {
  code: number
  data: T
  message: string
}

// 定义AxiosHooks，包含请求拦截器，请求异常拦截器，响应拦截器，响应异常拦截器
export interface AxiosHooks {
  // 请求拦截器hooks
  requestInterceptorsHook?: (config: AxiosRequestConfig) => AxiosRequestConfig
  // 异常处理拦截器
  requestInterceptorCatchHook?: (error: Error) => void
  // 请求相应拦截器,返回结果为axios默认响应结果或者自定义相应结果或者直接返回data的类型
  responseInterceptorHook?: (
    response: AxiosResponse<ResponseData<T>>,
  ) => AxiosResponse<ResponseData> | ResponseData | T
  // 请求相应异常处理拦截器
  responseInterceptorCatchHook: (error: AxiosError) => void
}

// 返回自定义axios模块
declare module 'axios' {
  interface AxiosRequestConfig {
    retryCount?: number
    axiosHooks?: AxiosHooks
    requestOptions: RequestOptions
  }
}
