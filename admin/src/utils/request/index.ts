import { AxiosError } from 'axios'
import type { AxiosRequestConfig } from 'axios'
import { merge } from 'lodash-es'
import { Axios } from './axios'
import { AxiosHooks } from './type'
import feedback from '@/utils/feedback.ts'

import config from '@/config'
import { clearAuthInfo, getToken } from '@/utils/auth'
import {
  ContentTypeEnum,
  RequestMethodEnum,
  ResponseStatusEnum,
} from '@/enums/httpEnum.ts'
import router from '@/router'
import { PageEnum } from '@/enums/pageEnum'

// 处理axiosHooks钩子函数
const axiosHooks: AxiosHooks = {
  // 请求拦截器
  requestInterceptorsHook(config) {

    const params = config.params || {}
    const headers = config.headers || {}
    // POST请求下如果无data，则将params视为data
    if (
      config.method?.toUpperCase() === RequestMethodEnum.POST &&
      config.requestOptions.isParamsToData &&
      !Reflect.has(config, 'data')
    ) {
      config.data = params
    }
    headers['admin-token'] = getToken()

    return config
  },
  requestInterceptorCatchHook(err) {
    return err
  },
  // 响应拦截器，所有的请求接口返回的响应内容都会先经过此处
  async responseInterceptorHook(response) {
    const { isTransformResponse, isReturnDefaultResponse } =
      response.config.requestOptions

    // 返回默认响应，当需要获取响应头及其他数据时可使用
    if (isReturnDefaultResponse) {
      return response
    }
    // 是否需要对数据进行处理
    if (!isTransformResponse) {
      return response.data
    }
    const { code, data, message } = response.data

    // 判断响应状态码是否请求成功
    switch (code) {
      case ResponseStatusEnum.SUCCESS:
        return Promise.resolve(data)
      case ResponseStatusEnum.FAILED:
        message && feedback.msgError(message || '请求失败')
        return Promise.reject(data)
      case ResponseStatusEnum.TOKEN_INVALID:
        message && feedback.msgError(message || 'token参数无效')
        clearAuthInfo()
        await router.push(PageEnum.LOGIN)
        return Promise.reject(data)
      case ResponseStatusEnum.PARAMS_VALID_ERROR:
        message && feedback.msgError(message || '参数校验错误')
        return Promise.reject()
      case ResponseStatusEnum.PARAMS_TYPE_ERROR:
        message && feedback.msgError(message || '请求参数类型错误')
        return Promise.reject(data)
      case ResponseStatusEnum.REQUEST_METHOD_ERROR:
        message && feedback.msgError(message || '请求方法错误')
        return Promise.reject(data)
      case ResponseStatusEnum.ASSERT_MYBATIS_ERROR:
        message && feedback.msgError(message || '断言Mybatis-Plus错误')
        return Promise.reject(data)
      case ResponseStatusEnum.ACCOUNT_DISABLE_ERROR:
        message && feedback.msgError(message || '账号被禁用')
        return Promise.reject(data)
      case ResponseStatusEnum.CAPTCHA_ERROR:
        message && feedback.msgError(message || '验证码错误')
        return Promise.reject(data)
      case ResponseStatusEnum.NO_PERMISSION:
        message && feedback.msgError(message || '无相关权限')
        return Promise.reject(data)
      case ResponseStatusEnum.REQUEST_404_ERROR:
        message && feedback.msgError(message || '请求接口不存在')
        return Promise.reject(data)
      case ResponseStatusEnum.SYSTEM_ERROR:
        message && feedback.msgError(message || '服务器开小差')
        return Promise.reject(data)
      default:
        feedback.msgError(message || '请求失败')
        return Promise.reject(data)
    }
  },
  responseInterceptorCatchHook(error) {
    if (error.code !== AxiosError.ERR_CANCELED) {
      const message = error.message as string
      feedback.msgError(message || '处理响应结果失败')
    }
    return Promise.reject(error)
  },
}

const defaultOptions: AxiosRequestConfig = {
  axiosHooks: axiosHooks,
  timeout: config.timeout,
  baseURL: config.baseUrl + config.urlPrefix,
  headers: {
    'Content-Type': ContentTypeEnum.JSON,
  },
  requestOptions: {
    // 是否将params参数作为data参数，仅限于post请求
    isParamsToData: true,
    // 是否返回默认响应
    isReturnDefaultResponse: false,
    // 是否需要对响应数据进行处理
    isTransformResponse: true,
    // 是否忽略重复请求
    ignoreCancelToken: false,
    // 重复请求次数
    retryCount: 2,
    // 是否开启,默认开启重复请求
    isOpenRetry: true,
  },
}
function createAxios(opt?: Partial<AxiosRequestConfig>) {
  return new Axios(merge(defaultOptions, opt || {}))
}

export default createAxios()
