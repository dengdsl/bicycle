import axios, { AxiosError, AxiosRequestConfig } from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
// 自定义的响应结果以及自定义的请求参数
import { merge } from 'lodash-es'
import type { ResponseData, RequestOptions } from './type'
import axiosCancel from '@/utils/request/cancel.ts'
import { RequestMethodEnum } from '@/enums/httpEnum.ts'

// 创建Axios实例类
export class Axios {
  // 定义相关的变量
  private axiosInstance: AxiosInstance

  private readonly config: AxiosRequestConfig

  private readonly options: RequestOptions

  // 定义构造函数赋值
  constructor(config: AxiosRequestConfig) {
    this.config = config
    this.options = config.requestOptions
    this.axiosInstance = this.getInstance(config)
    this.setInterceptors()
  }

  // 获取实例对象
  getInstance(config: AxiosRequestConfig) {
    return this.axiosInstance ?? axios.create(config)
  }

  // 设置拦截器
  setInterceptors() {
    // 如果当前实例拦截器不存在就不设置
    if (!this.config.axiosHooks) return
    // 获取到所有的拦截器
    const {
      requestInterceptorsHook,
      responseInterceptorHook,
      requestInterceptorCatchHook,
      responseInterceptorCatchHook,
    } = this.config.axiosHooks

    // 设置请求拦截器
    this.axiosInstance.interceptors.request.use(
      // 请求配置拦截器
      (config) => {
        console.log('config ==>', config)
        this.addCancelToken(config)
        // 判断自定义请求拦截器是否存在
        if (requestInterceptorsHook instanceof Function) {
          // @ts-expect-error
          config = requestInterceptorsHook(config)
        }
        return config
      },
      // 请求异常处理拦截器
      (error: AxiosError) => {
        // 异常处理拦截器存在就进行处理
        if (requestInterceptorCatchHook instanceof Function) {
          requestInterceptorCatchHook(error)
        }
      },
    )
    // 设置响应拦截器
    this.axiosInstance.interceptors.response.use(
      // 设置响应拦截器
      (response: AxiosResponse<ResponseData>) => {
        this.removeCancelToken(response.config.url!)
        if (responseInterceptorHook instanceof Function) {
          response = responseInterceptorHook(response)
        }
        return response
      },
      (error: AxiosError) => {
        if (responseInterceptorCatchHook instanceof Function) {
          responseInterceptorCatchHook(error)
        }
        if (error.code != AxiosError.ERR_CANCELED) {
          this.removeCancelToken(error.config?.url!)
        }
        // 重复请求
        if (
          error.code == AxiosError.ECONNABORTED ||
          error.code == AxiosError.ERR_NETWORK
        ) {
          return new Promise((resolve) => setTimeout(resolve, 500)).then(() =>
            this.retryRequest(error),
          )
        }
        return Promise.reject(error)
      },
    )
  }

  // 添加cancelToken
  addCancelToken(config: AxiosRequestConfig) {
    const { ignoreCancelToken } = config.requestOptions
    // 判断是否忽略重复请求
    if (!ignoreCancelToken) {
      axiosCancel.add(config)
    }
  }

  // 移除cancelToken
  removeCancelToken(url: string) {
    axiosCancel.remove(url)
  }

  // 重复请求
  retryRequest(error: AxiosError) {
    const config = error.config!
    const { retryCount, isOpenRetry } = config.requestOptions
    // 判断是否需要重复请求,post请求不能重复请求
    if (
      !isOpenRetry ||
      config.method?.toUpperCase() === RequestMethodEnum.POST
    ) {
      return Promise.reject(error)
    }
    // 如果重复请求到达最多请求次数之后还是没有成功,抛出错误
    config.retryCount = config.retryCount ?? 0
    if (config.retryCount >= retryCount) {
      return Promise.reject(error)
    }
    // 请求次数自增
    config.retryCount++
    // 重复请求
    return this.axiosInstance.request(config)
  }

  // 封装get请求方式
  get<T = any>(
    config: Partial<AxiosRequestConfig>,
    options?: Partial<RequestOptions>,
  ): Promise<T> {
    // 默认就是get请求
    return this.request(config, options)
  }

  // 封装post请求方式
  post<T = any>(
    config: Partial<AxiosRequestConfig>,
    options?: Partial<RequestOptions>,
  ): Promise<T> {
    return this.request({ ...config, method: 'POST' }, options)
  }

  // 封装请求
  request<T = any>(
    config: Partial<AxiosRequestConfig>,
    options?: Partial<RequestOptions>,
  ): Promise<any> {
    const opt = merge(this.options, options)
    const axiosConfig: AxiosRequestConfig = {
      ...config,
      requestOptions: opt,
    }
    return new Promise((resolve, reject) => {
      this.axiosInstance
        .request<any, AxiosResponse<ResponseData<T>>>(axiosConfig)
        .then((response) => {
          resolve(response)
        })
        .catch((err) => {
          reject(err)
        })
    })
  }
}
