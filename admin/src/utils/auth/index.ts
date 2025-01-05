import { TOKEN_KEY } from '@/enums/cacheEnum'
import { resetRouter } from '@/router'
import useTabsStore from '@/stores/modules/multipleTabs'
import useUserStore from '@/stores/modules/user'
import cache from '@/utils/cache'

export function getToken() {
  return cache.getItem(TOKEN_KEY)
}

export function clearAuthInfo() {
  const userStore = useUserStore()
  const tabsStore = useTabsStore()
  userStore.resetState()
  tabsStore.$reset()
  cache.removeItem(TOKEN_KEY)
  resetRouter()
}
