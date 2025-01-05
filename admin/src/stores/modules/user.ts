import { defineStore } from 'pinia'
import cache from '@/utils/cache'
import type { RouteRecordRaw } from 'vue-router'
import { self, login, logout, getRoutes } from '@/api/user'
import router, { filterAsyncRoutes } from '@/router'
import { TOKEN_KEY } from '@/enums/cacheEnum'
import { PageEnum } from '@/enums/pageEnum'
import { clearAuthInfo, getToken } from '@/utils/auth'

export interface UserState {
  token: string
  userInfo: Record<string, any>
  routes: RouteRecordRaw[]
  menu: any[]
  perms: string[]
}

const useUserStore = defineStore({
  id: 'user',
  state: (): UserState => ({
    token: getToken() || '',
    // 用户信息
    userInfo: {},
    // 路由
    routes: [],
    menu: [],
    // 权限
    perms: [],
  }),
  getters: {},
  actions: {
    resetState() {
      this.token = ''
      this.userInfo = {}
      this.perms = []
    },
    login(payload: string) {
      return new Promise((resolve, reject) => {
        login({
          loginValidate: payload,
        })
          .then((data: any) => {
            this.token = data.token
            cache.setItem(TOKEN_KEY, data.token)
            resolve(data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    logout() {
      return new Promise((resolve, reject) => {
        logout()
          .then(async (data: any) => {
            this.token = ''
            sessionStorage.removeItem('lylist')
            await router.push(PageEnum.LOGIN)
            clearAuthInfo()
            resolve(data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    getUserInfo() {
      return new Promise((resolve, reject) => {
        self()
          .then((data: any) => {
            this.userInfo = data.user
            this.perms = data.permissions
            resolve(data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
    getMenu() {
      return new Promise((resolve, reject) => {
        getRoutes()
          .then((data: any) => {
            this.menu = data
            this.routes = filterAsyncRoutes(data)
            resolve(data)
          })
          .catch((error: any) => {
            reject(error)
          })
      })
    },
  },
})

export default useUserStore
