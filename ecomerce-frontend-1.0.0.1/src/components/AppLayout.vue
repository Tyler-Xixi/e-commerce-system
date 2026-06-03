<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Bell,
  Box,
  Connection,
  DataBoard,
  Goods,
  House,
  OfficeBuilding,
  Service,
  SwitchButton,
  Tickets,
  User,
  Van,
  Wallet,
} from '@element-plus/icons-vue'

import { moduleRoutes } from '@/router'
import { getRoleLabel, getStoredPermissions, hasMenuAccess, roleMeta } from '@/data/system'
import { authApi, pageRecords, sourcingApi } from '@/services/api'

const route = useRoute()
const router = useRouter()
const unreadCount = ref(0)

const iconMap = {
  Bell,
  Box,
  Connection,
  DataBoard,
  Goods,
  House,
  OfficeBuilding,
  Service,
  SwitchButton,
  Tickets,
  User,
  Van,
  Wallet,
}

const user = computed(() => JSON.parse(localStorage.getItem('sc_user') || '{}'))
const roleCode = computed(() => user.value.roleCode || 'JS01')
const role = computed(() => roleMeta[roleCode.value] || roleMeta.JS01)
const permissions = computed(() => getStoredPermissions())

const menuItems = computed(() => {
  const allMenus = [
    { path: '/', label: '工作台', icon: 'DataBoard', menuKey: 'dashboard' },
    { path: '/sourcing', label: '寻源定点', icon: 'Connection', menuKey: 'sourcing' },
    ...moduleRoutes.map((item) => ({ ...item, menuKey: item.moduleKey })),
    { path: '/architecture', label: '架构视图', icon: 'House', menuKey: 'architecture' },
  ]
  return allMenus.filter((item) => hasMenuAccess(item.menuKey, permissions.value, roleCode.value))
})

async function logout() {
  try {
    await authApi.logout()
  } catch {
    // Local cleanup is still required if the token has already expired.
  }
  localStorage.removeItem('sc_token')
  localStorage.removeItem('sc_user')
  localStorage.removeItem('sc_permissions')
  router.replace('/login')
}

onMounted(async () => {
  if (!user.value.id) return
  try {
    const response = await sourcingApi.notifications(user.value.id, { page: 1, size: 1000 })
    unreadCount.value = pageRecords(response).filter((item) => Number(item.isRead) !== 1).length
  } catch {
    unreadCount.value = 0
  }
})
</script>

<template>
  <el-container class="app-shell">
    <el-aside class="sidebar" width="252px">
      <button class="brand" type="button" @click="router.push('/')">
        <span class="brand-mark">SC</span>
        <span>
          <strong>电商供应链一体化平台</strong>
          <small>寻源 · 采购 · 仓储协同</small>
        </span>
      </button>

      <el-scrollbar class="menu-scroll">
        <el-menu :default-active="route.path" router class="side-menu">
          <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
            <el-icon><component :is="iconMap[item.icon]" /></el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="topbar" height="68px">
        <div class="topbar-title">
          <p class="eyebrow">Spring Boot API / Vue3 Element Plus / MySQL 数据闭环</p>
          <h2>{{ route.meta.title || route.name }}</h2>
        </div>

        <div class="top-actions">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notice-badge">
            <el-button :icon="Bell" circle aria-label="通知" />
          </el-badge>

          <el-tag effect="plain" round :type="role.accent">
            {{ getRoleLabel(roleCode) }}
          </el-tag>

          <el-dropdown>
            <el-button>
              {{ user.realName || user.name || user.username || '当前用户' }}
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>{{ role.scope }}</el-dropdown-item>
                <el-dropdown-item divided :icon="SwitchButton" @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-content">
        <slot />
      </el-main>
    </el-container>
  </el-container>
</template>
