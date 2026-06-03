// =============================================
// 电商供应链 - 后端真实数据模式
// 本文件仅保留静态架构/常量数据，所有业务数据从 Spring Boot 后端实时拉取
// 后端服务地址: http://127.0.0.1:8080/api
// 代理转发: vite.config.js 已配置 /api -> 后端
// =============================================

export const architectureLayers = [
  {
    title: 'Vue3 + Element Plus 前端层',
    items: ['Vite 开发 / 构建', 'Vue Router 指令式路由', 'Axios 拦截器注入 JWT', '响应式模块页面（dashboard / users / suppliers 等）'],
  },
  {
    title: 'Spring Boot 3.2 后端层',
    items: ['Spring Security + JWT 认证', 'RESTful Controller 统一返回 ResponseEntity', 'Service 层封装业务逻辑', 'Spring Data JPA Repository 持久化'],
  },
  {
    title: 'MySQL + Redis 数据层',
    items: ['MySQL 8 主库：e_commerce_system（10 张业务表）', 'Redis 7 缓存：库存热点数据、Token 黑名单', 'JPA ddl-auto: update 自动建表', 'Jackson 序列化 Redis 值'],
  },
  {
    title: '基础设施层',
    items: ['Vite proxy 开发代理 / Nginx 生产反向代理', 'OSS 附件上传 / 下载预签名', '第三方物流 API 对接预留', '单体服务部署在 8080 端口'],
  },
]

