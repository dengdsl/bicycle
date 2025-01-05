<template>
  <main class="main-wrap h-full bg-page">
    <el-scrollbar>
      <div class="p-4">
        <!--这里全部显示的是二级路由-->
        <router-view v-if="isRouteShow" v-slot="{ Component, route }">
          <!--添加缓存规则-->
          <keep-alive :include="includeList" :max="20">
            <component :is="Component" :key="route.fullPath"></component>
          </keep-alive>
        </router-view>
      </div>
    </el-scrollbar>
  </main>
</template>

<script lang="ts" setup>
import useAppStore from '@/stores/modules/app'
import useTabsStore from '@/stores/modules/multipleTabs'
import useSettingStore from '@/stores/modules/setting'
const appStore = useAppStore()

const tabStore = useTabsStore()

const settingStore = useSettingStore()

// 动态获取当前路由的显示状态
const isRouteShow = computed(() => appStore.isRouteShow)
// 获取路由菜单中需要缓存的路由列表
const includeList = computed(() =>
  settingStore.openMultipleTabs ? tabStore.getCacheTabList : [],
)
</script>
