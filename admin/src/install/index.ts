import type { App } from 'vue'
const modules = import.meta.glob('./**/*', { eager: true })

// 安装方法，执行某一类相同操作
function install(app: App<Element>) {
  Object.keys(modules).forEach((key) => {
    // ./文件夹名称/文件名称.ts
    const name = key.replace(/(.*\/)*([^.]+).*/gi, '$2')
    const type = key.replace(/^\.\/([\w-]+).*/gi, '$1')
    // name：文件名称  type：文件夹名称
    const module: any = modules[key]
    // module.default：获取得到的是模块中的注册方法
    if (module.default) {
      switch (type) {
        // 用于注册全局指令
        case 'directives':
          app.directive(name, module.default)
          break
        // 使用插件
        case 'modules':
          typeof module.default === 'function' && module.default(app)
          break
      }
    }
  })
}

export default {
  install,
}
