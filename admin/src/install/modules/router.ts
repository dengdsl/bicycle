import router from '@/router'
import type { App } from 'vue'

// 注册路由
export default (app: App) => {
  app.use(router)
}
