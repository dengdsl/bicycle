import { dictDataAll } from '@/api/dict'

interface Options {
  [propName: string]: {
    api: (...args: any[]) => Promise<any>
    params?: Record<string, any>
    transformData?(data: any): any
  }
}

/**
 * 根据请求对象中的数据发起请求获取数据信息
 * */
export function useDictOptions<T = any>(
  options: Options,
): { optionsData: T; refresh: typeof refresh } {
  const optionsData: any = reactive({})
  // 遍历options对象创建请求集合
  const optionsKeys = Object.keys(options)
  const apiLists = optionsKeys.map((key) => {
    optionsData[key] = []
    const { api, params = {} } = options[key]
    return () => api(toRaw(params) || {})
  })

  // 创建方法发起所有请求
  const refresh = async () => {
    const res = await Promise.allSettled(apiLists.map((api) => api()))
    res.forEach((item, index) => {
      // 获取当前请求对应的对象中的key
      const key = optionsKeys[index]
      // 只处理请求成功的数据
      if (item.status === 'fulfilled') {
        // 是否需要对数据进行处理
        const { transformData } = options[key]
        // 将请求结果保存
        optionsData[key] = transformData
          ? transformData(item.value)
          : item.value
      }
    })
  }

  refresh()
  return {
    refresh,
    optionsData,
  }
}

/**
 * 创建请求对象
 * */
export function useDictData<T = any>(dict: string[]) {
  const options: Options = {}
  // 遍历字典，用于获取字典列表中的每个数据
  for (let key in dict) {
    options[dict[key]] = {
      api: dictDataAll,
      params: {
        dictType: dict[key],
      },
    }
  }

  // 调用方法发起请求获取数据字典数据
  const { optionsData } = useDictOptions<T>(options)
  return {
    dictData: optionsData,
  }
}
