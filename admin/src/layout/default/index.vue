<template>
  <div class="layout-default flex h-screen w-full">
    <!--菜单栏开始-->
    <div class="app-aside">
      <layout-slider></layout-slider>
    </div>
    <!--菜单栏结束-->
    <!--头部和内容开始-->
    <div class="flex flex-col flex-1 min-w-0">
      <!--头部开始-->
      <div class="app-herder">
        <layout-header></layout-header>
      </div>
      <!--头部结束-->
      <!--内容体开始-->
      <div class="app-main flex-1 min-h-0">
        <layout-main></layout-main>
      </div>
      <!--内容体结束-->
    </div>
    <!--头部和内容结束-->
  </div>
</template>

<script lang="ts" setup>
import LayoutMain from './components/main.vue'
import LayoutHeader from './components/header/index.vue'
import LayoutSlider from './components/slider/index.vue'
</script>

<style scoped></style>
