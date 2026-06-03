import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/LoginView.vue'
import DashboardView from '@/views/DashboardView.vue'
import ModuleView from '@/views/ModuleView.vue'
import SourcingView from '@/views/SourcingView.vue'
import ArchitectureView from '@/views/ArchitectureView.vue'
import { getStoredPermissions, hasMenuAccess, hasRoleAccess, menuPermissionMap, moduleDefinitions, roleMenus } from '@/data/system'

export const moduleRoutes = Object.values(moduleDefinitions).map((module) => ({
  path: module.path,
  name: module.name,
  label: module.label,
  icon: module.icon,
  moduleKey: module.name,
  roles: module.roles,
}))

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: LoginView, meta: { public: true, title: '登录' } },
    { path: '/', name: 'dashboard', component: DashboardView, meta: { title: '工作台', menuKey: 'dashboard', permission: menuPermissionMap.dashboard } },
    {
      path: '/sourcing',
      name: 'sourcing',
      component: SourcingView,
      meta: { title: '寻源定点', menuKey: 'sourcing', roles: ['JS01', 'JS02', 'JS03', 'JS04', 'JS05'], permission: menuPermissionMap.sourcing },
    },
    {
      path: '/architecture',
      name: 'architecture',
      component: ArchitectureView,
      meta: { title: '架构视图', menuKey: 'architecture', roles: ['JS03', 'JS04'], permission: menuPermissionMap.architecture },
    },
    ...moduleRoutes.map((route) => ({
      path: route.path,
      name: route.name,
      component: ModuleView,
      props: { moduleKey: route.moduleKey },
      meta: { title: route.label, menuKey: route.moduleKey, roles: route.roles, permission: menuPermissionMap[route.moduleKey] },
    })),
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('sc_token')
  const user = JSON.parse(localStorage.getItem('sc_user') || '{}')
  const roleCode = user.roleCode
  const permissions = getStoredPermissions()

  if (!to.meta.public && !token) {
    return { name: 'login' }
  }

  if (to.name === 'login' && token) {
    return { name: 'dashboard' }
  }

  if (!to.meta.public && roleCode) {
    const allowedKeys = roleMenus[roleCode] || ['dashboard']
    const menuKey = to.meta.menuKey
    if (menuKey) {
      const allowed = permissions.length > 0
        ? hasMenuAccess(menuKey, permissions, roleCode)
        : allowedKeys.includes(menuKey) || hasRoleAccess(roleCode, to.meta.roles)
      if (!allowed) return { name: 'dashboard' }
    }
    if (to.meta.permission && !permissions.includes(to.meta.permission) && permissions.length > 0) {
      return { name: 'dashboard' }
    }
  }

  return true
})

export default router
