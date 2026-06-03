# 电商供应链一体化平台

一个面向企业采购、寻源、供应商协同、库存物流和付款结算场景的前后端分离系统。项目后端基于 Spring Boot 3，前端基于 Vue 3 + Vite + Element Plus，同时包含一份用于无人超市场景的 LVGL 8.x UI 导出包，可作为嵌入式终端界面参考。

## 项目亮点

- 覆盖寻源需求、RFQ 发布、供应商报价、报价汇总、商务定点、审批执行的完整采购闭环。
- 支持品类工程师、品类组长、采购处长、平台管理员、供应商操作员、仓储调度员等多角色权限控制。
- 后端使用 JWT + Spring Security 做接口认证与权限校验。
- 前端提供登录、工作台、寻源定点、供应商、商品、采购订单、库存、物流、付款、售后、用户角色和架构视图等页面。
- 数据库提供完整演示 SQL，也会在后端启动时自动补齐基础演示账号和部分业务数据。
- 附带 SquareLine Studio 风格的 LVGL 无人超市 UI 导出包，包含登录、首页、购物车和支付页面。

## 技术栈

### 后端

- Java 17
- Spring Boot 3.2.0
- Spring Web
- Spring Security
- Spring Data JPA
- MySQL
- Redis
- JWT
- Maven
- Lombok

### 前端

- Vue 3
- Vite
- Vue Router
- Element Plus
- Axios
- Nginx 反向代理示例

### 嵌入式 UI 导出包

- LVGL 8.x
- SquareLine Studio 导出结构
- 800x480 画布
- Noto Sans SC 中文字体

## 目录结构

```text
电商供应链/
├─ ecomerce-backend-1.0.0.1/               后端 Spring Boot 项目
│  ├─ src/main/java/com/ecommerce/
│  │  ├─ config                            配置与启动初始化数据
│  │  ├─ controller                        REST API 控制器
│  │  ├─ dto                               分页响应 DTO
│  │  ├─ entity                            JPA 实体
│  │  ├─ exception                         全局异常处理
│  │  ├─ repository                        数据访问层
│  │  ├─ security                          JWT、权限与安全配置
│  │  ├─ service                           业务逻辑层
│  │  └─ util                              JWT、分页等工具
│  ├─ src/main/resources/
│  │  ├─ application.yml                   后端配置
│  │  └─ db                                初始化 SQL 片段
│  ├─ e_commerce_system.sql                原始数据库脚本
│  ├─ e_commerce_system_complete.sql       完整演示数据库脚本
│  └─ lvgl_unmanned_store_squareline_export/ LVGL 无人超市 UI 导出包
│
└─ ecomerce-frontend-1.0.0.1/              前端 Vue3 项目
   ├─ src/components                       公共组件
   ├─ src/data                             角色、菜单、模块、状态配置
   ├─ src/router                           路由与权限守卫
   ├─ src/services                         Axios API 封装
   ├─ src/views                            页面视图
   ├─ src/styles                           全局样式
   ├─ public                               架构图等静态资源
   └─ nginx/supply-chain.conf              Nginx 部署示例
```

## 核心业务流程

1. 品类工程师创建寻源需求。
2. 品类组长审核需求并发布 RFQ。
3. 供应商操作员查看 RFQ 并提交报价。
4. 品类工程师查看报价汇总，按单价排序比价并生成商务定点单。
5. 采购处长审批商务定点结果。
6. 审批通过后联动采购订单、付款结算、入库和物流调度流程。

## 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.x
- Redis 6.x+
- Node.js 18+
- npm 9+

## 后端配置

后端默认地址：

```text
http://localhost:8080/api
```

主要配置文件：

```text
ecomerce-backend-1.0.0.1/src/main/resources/application.yml
```

默认数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/e_commerce_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
```

如果本地 MySQL 或 Redis 配置不同，请先修改 `application.yml`。

## 数据库初始化

推荐导入完整演示脚本：

```text
ecomerce-backend-1.0.0.1/e_commerce_system_complete.sql
```

MySQL 示例：

```sql
CREATE DATABASE IF NOT EXISTS e_commerce_system DEFAULT CHARSET utf8mb4;
USE e_commerce_system;
SOURCE D:/SoftWare-Work/Java/JavaWebProjects/电商供应链/ecomerce-backend-1.0.0.1/e_commerce_system_complete.sql;
```

如果命令行不方便处理中文路径，也可以使用 Navicat、DataGrip、DBeaver 等工具打开 SQL 文件后执行。

## 本地启动

### 1. 启动后端

```bash
cd ecomerce-backend-1.0.0.1
mvn spring-boot:run
```

健康检查：

```text
http://localhost:8080/api/public/health
```

正常返回示例：

```json
{
  "status": "UP",
  "service": "e-commerce-system",
  "version": "1.0.0"
}
```

### 2. 启动前端

```bash
cd ../ecomerce-frontend-1.0.0.1
npm install
npm run dev
```

访问前端：

```text
http://localhost:5173
```

如果 npm 缓存权限受限，可以使用：

```bash
npm install --cache ./.npm-cache
```

前端开发代理配置在 `vite.config.js`：

```text
/api -> http://localhost:8080
/oss -> https://example-bucket.oss-cn-hangzhou.aliyuncs.com
```

## 构建命令

后端编译：

```bash
cd ecomerce-backend-1.0.0.1
mvn -q -DskipTests compile
```

前端构建：

```bash
cd ecomerce-frontend-1.0.0.1
npm run build
```

前端预览：

```bash
npm run preview
```

## 演示账号

默认密码均为：

```text
123456
```

| 用户名 | 角色码 | 角色 |
| --- | --- | --- |
| engineer | JS01 | 品类工程师 |
| leader | JS02 | 品类组长 |
| director | JS03 | 采购处长 |
| admin | JS04 | 平台管理员 |
| supplier | JS05 | 供应商操作员 |
| warehouse | JS06 | 仓储调度员 |

## 角色与菜单

| 角色 | 核心职责 | 可访问模块 |
| --- | --- | --- |
| 品类工程师 | 创建寻源需求、报价汇总、生成商务定点单 | 工作台、寻源定点、商品库、仓储库存 |
| 品类组长 | 审核寻源需求、发布 RFQ | 工作台、寻源定点、商品库、仓储库存 |
| 采购处长 | 最终审批商务定点、查看全量业务 | 全量业务模块、架构视图 |
| 平台管理员 | 系统配置、用户与菜单维护 | 全量业务模块、架构视图 |
| 供应商操作员 | 查看 RFQ、提交报价、跟踪报价状态 | 工作台、寻源定点 |
| 仓储调度员 | 库存预警处理、物流调度 | 工作台、仓储库存、物流调度 |

## 前端页面

| 路由 | 页面 | 说明 |
| --- | --- | --- |
| `/login` | 登录页 | 账号密码登录、演示账号快捷登录、后端健康状态检查 |
| `/` | 工作台 | 待办、RFQ、库存预警、订单履约统计 |
| `/sourcing` | 寻源定点 | 需求、RFQ、报价、定点、审批流程 |
| `/suppliers` | 供应商管理 | 供应商编码、等级、评分、准入状态 |
| `/products` | 商品库 | SKU、品类、单位、参考价、上下架 |
| `/orders` | 采购订单 | PO 单、采购主体、采购物料、付款与执行状态 |
| `/inventory` | 仓储库存 | 可用库存、锁定库存、预警阈值 |
| `/logistics` | 物流调度 | 承运商、路线、预计到达、配送状态 |
| `/payments` | 付款结算 | 结算单、付款渠道、金额、对账状态 |
| `/aftersales` | 售后工单 | 退换货、质量异常、责任人 |
| `/users` | 用户与角色 | 系统账号、角色编码、启停状态 |
| `/architecture` | 架构视图 | 展示前后端、Nginx、JPA、Redis、MySQL、OSS 的关系 |

## 主要接口

所有后端接口默认带 `/api` 前缀。

### 认证

```text
POST /api/auth/login
POST /api/auth/logout
GET  /api/auth/me
```

登录成功后前端会把 JWT 放入请求头：

```text
Authorization: Bearer <token>
```

### 菜单权限

```text
GET /api/menu/tree
GET /api/menu/permissions
```

### 公共接口

```text
GET /api/public/health
GET /api/public/dictionaries
GET /api/public/oss/policy
```

### 平台基础模块

```text
GET/POST/PUT/DELETE /api/suppliers
GET/POST/PUT/DELETE /api/products
GET/POST/PUT/DELETE /api/orders
GET/POST/PUT/DELETE /api/inventory
GET/POST/PUT        /api/logistics
GET/POST/PUT        /api/payments
GET/POST/PUT        /api/aftersales
GET                 /api/users
GET                 /api/inventory/alerts
POST                /api/{resource}/{id}/approve
```

### 寻源流程

```text
GET/POST/PUT /api/sourcing/demands
GET/POST/PUT /api/sourcing/quotations
GET/POST     /api/sourcing/quotes
GET/POST/PUT /api/sourcing/awards
GET/POST/PUT /api/sourcing/award-notices
GET/POST     /api/sourcing/approvals
GET          /api/sourcing/tasks/pending/{userId}
GET          /api/sourcing/tasks/count/{userId}
GET          /api/sourcing/notifications/{userId}
```

## LVGL 无人超市 UI 导出包

后端目录下附带：

```text
ecomerce-backend-1.0.0.1/lvgl_unmanned_store_squareline_export
```

功能包括：

- 登录页：点击登录进入主界面。
- 首页：展示商品卡片，支持加入购物车。
- 购物车：展示商品、数量和合计金额。
- 支付页：点击结算后模拟支付完成，并清空购物车返回首页。

接入 LVGL 工程时，把 `ui` 目录复制到目标工程，并把以下文件加入编译：

```text
ui/ui.c
ui/ui_helpers.c
ui/ui_events.c
ui/screens/ui_login.c
ui/screens/ui_main.c
ui/screens/ui_cart.c
ui/screens/ui_pay.c
ui/static/ui_font_notosanssc_16.c
ui/static/ui_font_notosanssc_20.c
ui/static/ui_font_notosanssc_26.c
```

初始化示例：

```c
#include "ui/ui.h"

ui_init();
```

## 部署说明

前端生产构建后会生成 `dist` 目录，可以配合 Nginx 部署。示例配置位于：

```text
ecomerce-frontend-1.0.0.1/nginx/supply-chain.conf
```

推荐生产环境调整：

- 修改后端 `jwt.secret`，不要使用默认演示密钥。
- 限制 Spring Security CORS 允许来源。
- 使用独立 MySQL、Redis 账号，并配置强密码。
- 关闭或降低 `spring.jpa.show-sql`。
- 将数据库密码、JWT 密钥等敏感配置迁移到环境变量或配置中心。

## 常见问题

### 前端提示后端未连接

先确认后端健康检查可访问：

```text
http://localhost:8080/api/public/health
```

再检查前端代理：

```text
ecomerce-frontend-1.0.0.1/vite.config.js
```

### 登录失败

请检查：

1. 数据库是否已导入 `e_commerce_system_complete.sql`。
2. 后端是否成功连接 MySQL 和 Redis。
3. `sys_user.password` 是否为 BCrypt 加密后的密码。
4. 后端控制台是否出现 SQL、认证或权限错误。

### 页面没有数据

请检查：

1. 是否导入完整演示 SQL。
2. 后端是否连接到正确的 `e_commerce_system` 数据库。
3. 当前账号角色是否拥有对应模块权限。

## GitHub 上传建议

建议把后端和前端放在同一个仓库根目录中：

```text
ecommerce-supply-chain/
├─ ecomerce-backend-1.0.0.1
├─ ecomerce-frontend-1.0.0.1
└─ README.md
```

上传前建议忽略以下目录：

```gitignore
target/
node_modules/
dist/
.idea/
.npm-cache/
.playwright-cli/
*.zip
```

## License

本项目用于课程设计、原型演示和学习交流。如用于生产环境，请补充正式 License、接口鉴权策略、审计日志和安全配置。
