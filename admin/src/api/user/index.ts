import request from '@/utils/request'
import { ContentTypeEnum } from '@/enums/httpEnum.ts'

/**
 * 获取图片验证码
 * */
export function captcha() {
  return request.get({
    url: '/system/captcha',
  })
}

/**
 * 获取密码加密公钥
 * */
export function getPublicKey() {
  return request.get({
    url: '/system/publicKey',
  })
}

/**
 * 获取应用配置
 * */
export function config() {
  return request.get({
    url: '/system/config',
  })
}

/**
 * 登录
 * */
export function login(data: { loginValidate: string }) {
  return request.post({
    url: '/system/login',
    data,
    headers: {
      'Content-Type': ContentTypeEnum.FORM_DATA,
    },
  })
}

/**
 * 退出登录
 * */
export function logout() {
  return request.post({
    url: '/system/logout',
  })
}

/**
 * 获取用户信息
 * */
export function self() {
  return request.get({
    url: '/system/self',
  })
}

// 编辑管理员信息
export function setUserInfo(data: any) {
  return request.post({ url: '/system/updateUserInfo', data })
}

/**
 * 获取用户路由菜单
 * */
export function getRoutes() {
  return request.get({
    url: '/system/routes',
  })
}
