import { createApp } from 'vue'
import App from './App.vue'
import 'virtual:svg-icons-register'
import install from '@/install'
import './permission.ts'
import '@/styles/index.scss'
import { Toast } from 'vant'

const app = createApp(App)
app.use(install)
app.use(Toast)
app.mount('#app')
