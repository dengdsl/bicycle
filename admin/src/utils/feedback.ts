import {
  ElMessage,
  ElMessageBox,
  ElNotification,
  ElLoading,
} from 'element-plus'
import type { ElMessageBoxOptions } from 'element-plus'
import type { LoadingInstance } from 'element-plus/es/components/loading/src/loading'

export class Feedback {
  private loadingInstance: LoadingInstance | null = null
  static instance: Feedback | null = null
  static getInstance() {
    return this.instance ?? (this.instance = new Feedback())
  }
  // 消息提示
  msg(message: string) {
    ElMessage({
      message,
    })
  }
  // 错误消息
  msgError(message: string) {
    ElMessage({
      type: 'error',
      message,
    })
  }
  // 成功消息
  msgSuccess(message: string) {
    ElMessage({
      message,
      type: 'success',
    })
  }
  // 警告消息
  msgWarning(message: string) {
    ElMessage({
      message,
      type: 'warning',
    })
  }
  // 弹出提示
  alert(msg: string) {
    ElMessageBox.alert(msg, '系统提示')
  }
  // 错误提示
  alertError(msg: string) {
    ElMessageBox.alert(msg, '系统提示', { type: 'error' })
  }
  // 成功提示
  alertSuccess(msg: string) {
    ElMessageBox.alert(msg, '系统提示', { type: 'success' })
  }
  // 警告提示
  alertWarning(msg: string) {
    ElMessageBox.alert(msg, '系统提示', { type: 'warning' })
  }
  // 通知提示
  notify(msg: string) {
    ElNotification.info(msg)
  }
  // 错误通知
  notifyError(msg: string) {
    ElNotification.error(msg)
  }
  // 成功通知
  notifySuccess(msg: string) {
    ElNotification.success(msg)
  }
  // 警告通知
  notifyWarning(msg: string) {
    ElNotification.warning(msg)
  }
  // 确认窗体
  confirm(msg: string, title = '温馨提示') {
    return ElMessageBox.confirm(msg, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
  }
  // 提交内容
  prompt(content: string, title: string, options?: ElMessageBoxOptions) {
    return ElMessageBox.prompt(content, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      ...options,
    })
  }
  // 打开全局loading
  loading(msg: string) {
    this.loadingInstance = ElLoading.service({
      lock: true,
      text: msg,
    })
  }
  // 关闭全局loading
  closeLoading() {
    this.loadingInstance?.close()
  }
}

const feedback = Feedback.getInstance()

export default feedback
