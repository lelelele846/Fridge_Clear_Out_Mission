// API配置
const config = {
  // 开发环境
  development: {
    baseUrl: 'http://localhost:8080'
  },
  // 生产环境
  production: {
    baseUrl: 'http://your-production-domain.com' // 这里替换为生产环境域名
  }
}

// 根据当前环境获取配置
const env = process.env.NODE_ENV || 'development'
export default config[env] 