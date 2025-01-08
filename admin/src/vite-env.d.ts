/// <reference types="vite/client" />
declare module '*.vue' {
  import { ComponentOptions } from 'vue'
  const componentOptions: ComponentOptions
  export default componentOptions
}

declare module '*.vue' {
  import Vue from 'vue'
  export default Vue
}

declare module 'nprogress'

declare module 'vue3-qr-reader'
