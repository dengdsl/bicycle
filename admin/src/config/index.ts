const config = {
  terminal: 1, //终端
  title: '自行车管理系统', //网站默认标题
  version: '1.0.0', //版本号
  baseUrl: `${import.meta.env.VITE_APP_BASE_URL || ''}/`, //请求接口域名
  urlPrefix: 'api', //请求默认前缀
  timeout: 20 * 1000, //请求超时时长
}

export default config
