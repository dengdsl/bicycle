import type { AxiosRequestConfig, Canceler } from 'axios'
import axios from 'axios'

const cancelMap = new Map<string, Canceler>()

export class AxiosCancel {
  private static instance?: AxiosCancel

  static createInstance() {
    return this.instance ?? (this.instance = new AxiosCancel())
  }
  add(config: AxiosRequestConfig) {
    const url = config.url!
    this.remove(url)
    config.cancelToken = new axios.CancelToken((cancel) => {
      if (!cancelMap.has(url)) {
        cancelMap.set(url, cancel)
      }
    })
  }
  remove(url: string) {
    if (cancelMap.has(url)) {
      const cancel = cancelMap.get(url)
      cancel && cancel()
      cancelMap.delete(url)
    }
  }
}

const axiosCancel = AxiosCancel.createInstance()

export default axiosCancel
