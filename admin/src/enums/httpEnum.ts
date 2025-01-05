/**请求体类型**/
export enum ContentTypeEnum {
  // json格式
  JSON = 'application/json;charset=UTF-8',
  // form-data 上传图片资源
  FORM_DATA = 'multipart/form-data',
}
/**请求方式**/
export enum RequestMethodEnum {
  GET = 'GET',
  POST = 'POST',
}
/**请求响应状态码**/
export enum ResponseStatusEnum {
  SUCCESS = 200, // 请求成功
  FAILED = 300, // 请求失败
  TOKEN_INVALID = 301, // token参数无效
  PARAMS_VALID_ERROR = 310, // 参数校验错误
  PARAMS_TYPE_ERROR = 311, // 参数类型错误
  REQUEST_METHOD_ERROR = 312, // 请求方法错误
  ASSERT_MYBATIS_ERROR = 314, // 断言mybatis错误
  ACCOUNT_DISABLE_ERROR = 331, // 账号被禁用
  CAPTCHA_ERROR = 334, // 验证码错误
  NO_PERMISSION = 403, // 无小关权限
  REQUEST_404_ERROR = 404, // 请求接口不存在
  SYSTEM_ERROR = 500, // 系统发生错误
}
