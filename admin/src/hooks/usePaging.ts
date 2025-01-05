interface Options {
  page?: number
  size?: number
  fetchFn: (_args: any) => Promise<any>
  params?: Record<any, any>
  firstLoading?: boolean
}

/**
 * 获取分页参数
 * */
export function usePaging(options: Options) {
  const {
    page = 1,
    size = 15,
    fetchFn,
    params = {},
    firstLoading = false,
  } = options
  // 记录分页初始参数
  const paramsInit: Record<any, any> = Object.assign({}, toRaw(params))

  // 分页数据
  const pager = reactive({
    page,
    size,
    total: 0,
    lists: [] as any[],
    loading: firstLoading,
  })

  // 请求分页数据
  const getLists = () => {
    pager.loading = true
    fetchFn({
      pageNo: pager.page,
      pageSize: pager.size,
      ...params,
    })
      .then((res: any) => {
        if (res instanceof Array) {
          pager.lists = res
          pager.total = res.length
          pager.loading = false
        } else {
          pager.lists = res?.lists
          pager.total = res?.count
        }
        return Promise.resolve(res)
      })
      .finally(() => {
        pager.loading = false
      })
  }

  // 重置为第一页
  const resetPage = () => {
    pager.page = 1
    getLists()
  }

  // 重置请求参数
  const resetParams = () => {
    Object.keys(paramsInit).forEach((key) => {
      params[key] = paramsInit[key]
    })
    getLists()
  }

  return {
    pager,
    getLists,
    resetPage,
    resetParams,
  }
}
