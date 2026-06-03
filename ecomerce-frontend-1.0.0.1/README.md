# 电商供应链一体化平台前端

基于 Week 3 需求规格说明书、系统架构图和最终架构图实现的管理端前端原型。

## 技术栈

- Vue 3 + Vite
- Element Plus
- Vue Router
- Axios
- Nginx 反向代理 `/api/public/**`、`/api/auth/**`、`/api/**`

## 已实现页面

- 登录页：支持 mock 登录，并预留 `/api/auth/login`
- 工作台：待办、RFQ、库存预警、订单履约统计
- 寻源定点：寻源需求、RFQ、报价汇总、商务定点
- 用户管理、供应商管理、商品管理、订单管理
- 仓储库存、物流调度、支付结算、售后管理
- 架构视图：展示 Vue3 + Nginx + SpringBoot MVC + JPA/Redis/MySQL/OSS 的落地关系

## 演示账号

| 用户名 | 密码 | 角色 |
| --- | --- | --- |
| admin | 123456 | 平台管理员 |
| engineer | 123456 | 品类工程师 |
| supplier | 123456 | 供应商操作员 |

## 本地运行

```bash
npm install
npm run dev
```

如果本机 npm cache 权限受限，可以使用：

```bash
npm install --cache ./.npm-cache
```

如果 Vite 在受限环境中无法启动，也可以直接打开 `standalone-preview.html` 查看前端预览页。

## 后端代理

开发环境代理在 `vite.config.js`：

- `/api` -> `http://127.0.0.1:8080`
- `/oss` -> 阿里云 OSS 示例 bucket

生产环境 Nginx 示例在 `nginx/supply-chain.conf`。

## 当前环境验证记录

本工作区已执行：

- `npm install --cache ./.npm-cache --ignore-scripts`
- `node --check` 检查 JS 文件
- Vue SFC 编译器解析 8 个 `.vue` 文件
- Element Plus 图标导入检查

当前沙箱环境执行 `vite build` 时被系统拒绝启动子进程，错误为 `spawn EPERM`，属于本机执行策略限制；项目文件本身已完成语法级检查。
