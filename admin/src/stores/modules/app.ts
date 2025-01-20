import { config } from '@/api/user'
import { defineStore } from 'pinia'
import defaultSetting from '@/config/setting.ts'

interface AppState {
  config: Record<string, any> // 系统配置信息
  isMobile: boolean // 菜单类型是否是目录
  isCollapsed: boolean // 菜单是否折叠
  isRouteShow: boolean // 是否显示路由菜单
}

const useAppStore = defineStore({
  id: 'app',
  state: (): AppState => {
    return {
      config: {},
      isMobile: false,
      isCollapsed: false,
      isRouteShow: true,
    }
  },
  actions: {
    // 获取应用配置信息
    getConfig() {
      return new Promise((resolve, reject) => {
        config()
          .then((data) => {
            // 将配置信息赋值为应用配置信息
            for (const key in data) {
              this.config[key] = data[key]
            }
            // pc端主题颜色
            if (data.theme) {
              defaultSetting.theme = data.theme
            }
            // 公众号主题颜色
            if (data.mobileTheme) {
              defaultSetting.mobileTheme = data.mobileTheme
            }
            resolve(data)
          })
          .catch((err) => {
            reject(err)
          })
      })
    },
    // 设置当前菜单的值
    setMobile(value: boolean) {
      this.isMobile = value
    },
    // 切换菜单列表折叠状态
    toggleCollapsed(toggle?: boolean) {
      // ?? 表示如果toggle不为null或者undefined,就是用toggle的值,否则将isCollapsed取反
      this.isCollapsed = toggle ?? !this.isCollapsed
    },
    // 刷新页面
    refreshView() {
      // 先将页面注销
      this.isRouteShow = false
      nextTick(() => {
        // 重新渲染页面
        this.isRouteShow = true
      })
    },
  },
})

export default useAppStore
