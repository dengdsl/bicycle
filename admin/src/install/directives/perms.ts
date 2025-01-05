/**
 * perm 操作权限处理
 * 指令用法：
 *  <el-button v-perms="['auth.menu/edit']">编辑</el-button>
 */
import useUserStore from '@/stores/modules/user.ts'

export default {
  mounted: (el: HTMLElement, binding: any) => {
    const { value } = binding
    const userStore = useUserStore()
    // 获取用户权限列表
    const permissions = userStore.perms
    // 所有权限
    const all_permission = '*'
    // 给dom节点绑定指令是值需要传递一个数组
    if (Array.isArray(value)) {
      if (value.length > 0) {
        // 判断是否拥有权限
        // @ts-ignore
        const hasPermission = permissions.some((key: string) => {
          return all_permission == key || value.includes(key)
        })
        // 没有权限，不显示dom节点
        if (!hasPermission) {
          el.parentNode && el.parentNode.removeChild(el)
        }
      }
    } else {
      throw new Error('like v-perms="[\'auth.menu/edit\']"')
    }
  },
}
