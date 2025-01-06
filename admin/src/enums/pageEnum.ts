export enum PageEnum {
  //登录页面
  LOGIN = '/login',
  //无权限页面
  ERROR_403 = '/403',
  // 404
  ERROR_404 = '/:pathMatch(.*)*',
  INDEX = '/',
  // 查询界面
  SEARCH = '/query',
  // 查询结果界面
  SEARCH_RESULT = '/query/result',
}
