<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Lock, User } from '@element-plus/icons-vue'

import { authApi, menuApi, publicApi } from '@/services/api'
import { demoAccounts, getRoleLabel, roleMeta } from '@/data/system'

const router = useRouter()
const loading = ref(false)
const apiStatus = ref('正在检测后端接口...')
const form = reactive({
  username: demoAccounts[0].username,
  password: demoAccounts[0].password,
})

function pickAccount(account) {
  form.username = account.username
  form.password = account.password
}

async function checkPublicApi() {
  try {
    const { data } = await publicApi.health()
    apiStatus.value = `${data.service || '后端服务'} 已连接`
  } catch {
    apiStatus.value = '后端未连接：请先启动 Spring Boot，默认地址 http://localhost:8080/api'
  }
}

async function login() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    const { data: loginData } = await authApi.login({ username: form.username, password: form.password })
    localStorage.setItem('sc_token', loginData.token)

    const { data: userData } = await authApi.me()
    let permissions = userData.permissions || loginData.permissions || []
    try {
      const { data } = await menuApi.permissions()
      permissions = Array.isArray(data) ? data : permissions
    } catch {
      permissions = userData.permissions || loginData.permissions || []
    }
    const normalizedUser = {
      ...userData,
      role: getRoleLabel(userData.roleCode),
      roleName: getRoleLabel(userData.roleCode),
      permissions,
    }
    localStorage.setItem('sc_user', JSON.stringify(normalizedUser))
    localStorage.setItem('sc_permissions', JSON.stringify(permissions))

    ElMessage.success(`欢迎，${normalizedUser.realName || normalizedUser.name || normalizedUser.username}`)
    router.replace('/')
  } catch (error) {
    ElMessage.error(error.response?.data?.error || error.response?.data?.message || '登录失败，请检查账号密码或后端服务')
  } finally {
    loading.value = false
  }
}

onMounted(checkPublicApi)
</script>

<template>
  <main class="login-page">
    <section class="login-panel">
      <div class="login-copy">
        <div class="brand-mark large">SC</div>
        <h1>电商供应链一体化平台</h1>
        <p>
          面向品类工程师、品类组长、采购处长、供应商和仓储调度员的协同系统。
          前端直接调用 Spring Boot API，数据落在 MySQL 的供应链业务表。
        </p>

        <div class="login-flow">
          <span>需求</span>
          <span>审核</span>
          <span>RFQ</span>
          <span>报价</span>
          <span>定点</span>
          <span>履约</span>
        </div>
      </div>

      <el-card class="login-card" shadow="never">
        <template #header>
          <div>
            <h2>账号登录</h2>
            <p>{{ apiStatus }}</p>
          </div>
        </template>

        <el-form @submit.prevent>
          <el-form-item>
            <el-input v-model="form.username" size="large" placeholder="用户名" :prefix-icon="User" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.password" size="large" type="password" show-password placeholder="密码：123456" :prefix-icon="Lock" @keyup.enter="login" />
          </el-form-item>
          <el-button type="primary" size="large" :loading="loading" class="full-button" @click="login">
            登录平台
          </el-button>
        </el-form>

        <div class="demo-accounts">
          <p>演示角色</p>
          <button
            v-for="account in demoAccounts"
            :key="account.username"
            type="button"
            :class="{ active: form.username === account.username }"
            @click="pickAccount(account)"
          >
            <strong>{{ roleMeta[account.roleCode]?.label || account.name }}</strong>
            <span>{{ account.username }}</span>
          </button>
        </div>
      </el-card>
    </section>
  </main>
</template>
