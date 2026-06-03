<script setup>
import AppLayout from '@/components/AppLayout.vue'
import { architectureLayers } from '@/data/mock'
</script>

<template>
  <AppLayout>
    <div class="page-stack">
      <el-card shadow="never" class="section-card module-hero">
        <div>
          <p class="eyebrow">系统架构落地</p>
          <h3>前后端分离架构说明</h3>
          <p>前端以 Vue3 + Element Plus 实现管理端，Nginx 代理 public/auth API，后端按 SpringBoot MVC 三层架构接入 JPA、Redis、MySQL 与 OSS。</p>
        </div>
      </el-card>

      <div class="architecture-grid">
        <el-card v-for="layer in architectureLayers" :key="layer.title" shadow="never" class="arch-card">
          <h3>{{ layer.title }}</h3>
          <el-timeline>
            <el-timeline-item v-for="item in layer.items" :key="item" :timestamp="item" />
          </el-timeline>
        </el-card>
      </div>

      <el-card shadow="never" class="section-card">
        <template #header>
          <div>
            <h3>架构图对照</h3>
            <p>图片来自工作区中的系统架构图和最终架构图，已放入前端 public 资源目录。</p>
          </div>
        </template>
        <div class="arch-images">
          <figure>
            <img src="/final-architecture.png" alt="最终架构图" />
            <figcaption>最终架构图</figcaption>
          </figure>
          <figure>
            <img src="/system-architecture.png" alt="系统架构图" />
            <figcaption>系统架构图</figcaption>
          </figure>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card">
        <template #header>
          <div>
            <h3>接口访问约定</h3>
            <p>开发环境走 Vite proxy，生产环境由 Nginx 反向代理到 SpringBoot 服务。</p>
          </div>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="/api/public/**">健康检查、字典、OSS 上传策略等公开接口</el-descriptions-item>
          <el-descriptions-item label="/api/auth/**">登录、退出、当前用户、权限菜单</el-descriptions-item>
          <el-descriptions-item label="/api/suppliers">供应商 JPA CRUD</el-descriptions-item>
          <el-descriptions-item label="/api/inventory">库存查询、Redis 缓存、库存流水</el-descriptions-item>
          <el-descriptions-item label="/oss/**">阿里云 OSS 附件代理路径</el-descriptions-item>
          <el-descriptions-item label="Authorization">Bearer Token 由 Axios 拦截器统一注入</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </AppLayout>
</template>
