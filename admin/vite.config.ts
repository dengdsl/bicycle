import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { fileURLToPath, URL } from 'url'
import { VantResolver } from '@vant/auto-import-resolver'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/bicycle/',
  server: {
    proxy: {
      '/api': {
        // 配置需要代理的路径 --> 这里的意思是代理http://localhost:80/api/后的所有路由
        target: 'http://127.0.0.1:8888/', // 目标地址 --> 服务器地址
        changeOrigin: true, // 允许跨域
        ws: true, // 允许websocket代理
        // 重写路径 --> 作用与vue配置pathRewrite作用相同
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
  plugins: [
    vue(),
    AutoImport({
      imports: ['vue', 'vue-router'],
      resolvers: [ElementPlusResolver(), VantResolver()],
      eslintrc: {
        enabled: true,
      },
    }),
    Components({
      directoryAsNamespace: true,
      resolvers: [ElementPlusResolver(), VantResolver()],
    }),
    createSvgIconsPlugin({
      iconDirs: [fileURLToPath(new URL('./src/assets/icons', import.meta.url))],
      symbolId: 'local-icon-[dir]-[name]',
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
