<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

import AppLayout from '@/components/AppLayout.vue'
import StatCard from '@/components/StatCard.vue'
import { authApi, pageRecords, platformApi, sourcingApi } from '@/services/api'
import { formatDateTime, getRoleLabel, getStatusMeta, getStoredPermissions, hasPermission, roleMeta, workflowSteps } from '@/data/system'

const DASHBOARD_SIZE = 1000

const router = useRouter()
const loading = ref(false)
const user = ref(JSON.parse(localStorage.getItem('sc_user') || '{}'))
const suppliers = ref([])
const rfqs = ref([])
const quotes = ref([])
const awards = ref([])
const alerts = ref([])
const tasks = ref([])
const notifications = ref([])

const roleCode = computed(() => user.value.roleCode || 'JS01')
const role = computed(() => roleMeta[roleCode.value] || roleMeta.JS01)
const permissions = computed(() => getStoredPermissions())

const dashboardStats = computed(() => [
  { label: '供应商总数', value: suppliers.value.length, unit: '家', type: 'primary', trend: '准入与报价池' },
  { label: '在线 RFQ', value: rfqs.value.filter((item) => ['APPROVED', 'PUBLISHED', 'APPROVING'].includes(item.status)).length, unit: '单', type: 'success', trend: '可报价/审批' },
  { label: '库存预警', value: alerts.value.length, unit: '项', type: 'warning', trend: '低于阈值' },
  { label: '待办事项', value: tasks.value.length, unit: '条', type: 'danger', trend: role.value.label },
])

const roleActions = computed(() => {
  const map = {
    JS01: [
      { label: '创建寻源需求', tab: 'demands' },
      { label: '查看报价汇总', tab: 'quotes' },
      { label: '生成商务定点', tab: 'quotes' },
    ],
    JS02: [
      { label: '审核寻源需求', tab: 'demands' },
      { label: '查看 RFQ 质量', tab: 'rfq' },
    ],
    JS03: [
      { label: '审批商务定点', tab: 'awards' },
      { label: '查看采购订单', path: '/orders' },
      { label: '查看付款结算', path: '/payments' },
    ],
    JS05: [
      { label: '查看 RFQ 公告', tab: 'rfq' },
      { label: '提交供应商报价', tab: 'rfq' },
      { label: '我的报价', tab: 'quotes' },
    ],
    JS06: [
      { label: '处理库存预警', path: '/inventory' },
      { label: '调度物流配送', path: '/logistics' },
    ],
  }
  return map[roleCode.value] || map.JS01
})

function goAction(action) {
  if (action.path) {
    router.push(action.path)
    return
  }
  router.push({ path: '/sourcing', query: { tab: action.tab } })
}

function fetchIfAllowed(permission, loader, fallback = { data: [] }) {
  return hasPermission(permissions.value, permission) ? loader() : Promise.resolve(fallback)
}

async function fetchDashboard() {
  loading.value = true
  try {
    const me = await authApi.me()
    user.value = {
      ...me.data,
      role: getRoleLabel(me.data.roleCode),
      roleName: getRoleLabel(me.data.roleCode),
    }
    localStorage.setItem('sc_user', JSON.stringify(user.value))
    if (Array.isArray(me.data.permissions)) {
      localStorage.setItem('sc_permissions', JSON.stringify(me.data.permissions))
    }

    const [supplierRes, rfqRes, quoteRes, awardRes, alertRes, taskRes, notificationRes] = await Promise.allSettled([
      fetchIfAllowed('supplier:list', () => platformApi.list('suppliers', { page: 1, size: DASHBOARD_SIZE })),
      fetchIfAllowed('sourcing:rfq:list', () => sourcingApi.listQuotations({ page: 1, size: DASHBOARD_SIZE })),
      fetchIfAllowed('sourcing:quote:summary', () => sourcingApi.listQuotes({ page: 1, size: DASHBOARD_SIZE })),
      fetchIfAllowed('sourcing:award:list', () => sourcingApi.listAwards({ page: 1, size: DASHBOARD_SIZE })),
      fetchIfAllowed('warehouse:inventory', () => platformApi.getInventoryAlerts()),
      fetchIfAllowed('dashboard:todo', () => sourcingApi.pendingTasks(user.value.id, { page: 1, size: DASHBOARD_SIZE })),
      fetchIfAllowed('dashboard:todo', () => sourcingApi.notifications(user.value.id, { page: 1, size: DASHBOARD_SIZE })),
    ])

    suppliers.value = supplierRes.status === 'fulfilled' ? pageRecords(supplierRes.value) : []
    rfqs.value = rfqRes.status === 'fulfilled' ? pageRecords(rfqRes.value) : []
    quotes.value = quoteRes.status === 'fulfilled' ? pageRecords(quoteRes.value) : []
    awards.value = awardRes.status === 'fulfilled' ? pageRecords(awardRes.value) : []
    alerts.value = alertRes.value?.data || []
    tasks.value = taskRes.status === 'fulfilled' ? pageRecords(taskRes.value) : []
    notifications.value = notificationRes.status === 'fulfilled' ? pageRecords(notificationRes.value) : []
  } catch (error) {
    ElMessage.warning('工作台数据加载不完整，请确认后端和数据库已启动')
  } finally {
    loading.value = false
  }
}

onMounted(fetchDashboard)
</script>

<template>
  <AppLayout>
    <div class="page-stack" v-loading="loading">
      <div class="role-hero">
        <div>
          <p class="eyebrow">当前角色：{{ getRoleLabel(roleCode) }}</p>
          <h3>{{ role.scope }}</h3>
          <p>工作台按角色聚合待办、RFQ、报价和库存状态，避免不同岗位看到无关功能。</p>
        </div>
        <div class="hero-actions">
          <el-button v-for="action in roleActions" :key="action.label" type="primary" plain @click="goAction(action)">
            {{ action.label }}
          </el-button>
        </div>
      </div>

      <div class="stats-grid">
        <StatCard v-for="stat in dashboardStats" :key="stat.label" :stat="stat" />
      </div>

      <el-card shadow="never" class="section-card">
        <template #header>
          <div class="section-head">
            <div>
              <h3>完整寻源业务流程</h3>
              <p>从品类需求到商务定点审批，再触发采购、入库、付款流程。</p>
            </div>
            <el-button text type="primary" @click="router.push('/sourcing')">进入寻源工作区</el-button>
          </div>
        </template>
        <div class="flow-grid five">
          <div v-for="(step, index) in workflowSteps" :key="step.title" class="flow-step" :class="{ active: step.roleCode === roleCode }">
            <span>{{ String(index + 1).padStart(2, '0') }}</span>
            <strong>{{ step.title }}</strong>
            <small>{{ getRoleLabel(step.roleCode) }}</small>
            <p>{{ step.desc }}</p>
          </div>
        </div>
      </el-card>

      <div class="content-grid">
        <el-card shadow="never" class="section-card">
          <template #header>
            <div class="section-head">
              <div>
                <h3>我的待办</h3>
                <p>来自后端 /api/sourcing/tasks/pending/{{ user.id || '-' }}</p>
              </div>
              <el-button text type="primary" @click="fetchDashboard">刷新</el-button>
            </div>
          </template>
          <el-table :data="tasks" row-key="id" stripe empty-text="暂无待办">
            <el-table-column prop="title" label="事项" min-width="240" />
            <el-table-column prop="bizType" label="类型" width="130" />
            <el-table-column prop="priority" label="优先级" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusMeta(row.priority).type" effect="plain">{{ getStatusMeta(row.priority).label }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="deadline" label="截止时间" width="150">
              <template #default="{ row }">{{ formatDateTime(row.deadline) }}</template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card shadow="never" class="section-card">
          <template #header>
            <div>
              <h3>角色数据视图</h3>
              <p>不同角色只暴露与职责相关的核心指标。</p>
            </div>
          </template>
          <div class="insight-list">
            <div>
              <strong>{{ rfqs.length }}</strong>
              <span>RFQ 总数</span>
            </div>
            <div>
              <strong>{{ quotes.length }}</strong>
              <span>报价记录</span>
            </div>
            <div>
              <strong>{{ awards.length }}</strong>
              <span>商务定点单</span>
            </div>
            <div>
              <strong>{{ notifications.length }}</strong>
              <span>消息通知</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </AppLayout>
</template>
