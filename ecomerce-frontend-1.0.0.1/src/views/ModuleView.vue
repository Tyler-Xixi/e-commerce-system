<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'

import AppLayout from '@/components/AppLayout.vue'
import { pageRecords, pageTotal, platformApi } from '@/services/api'
import { formatDateTime, formatMoney, getStatusMeta, getStoredPermissions, hasModuleAction, moduleDefinitions } from '@/data/system'

const props = defineProps({ moduleKey: { type: String, required: true } })
const PAGE_SIZE = 10

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const drawerVisible = ref(false)
const editingRow = ref(null)
const query = reactive({ keyword: '', status: '' })
const pagination = reactive({ page: 1, size: PAGE_SIZE })
const form = reactive({})

const moduleConfig = computed(() => moduleDefinitions[props.moduleKey])
const firstColumnProp = computed(() => moduleConfig.value?.columns?.[0]?.prop || 'id')
const permissions = computed(() => getStoredPermissions())
const canCreateRecord = computed(() => moduleConfig.value?.canCreate && hasModuleAction(props.moduleKey, 'manage', permissions.value))
const canUpdateRecord = computed(() => moduleConfig.value?.canUpdate && hasModuleAction(props.moduleKey, 'manage', permissions.value))
const canDeleteRecord = computed(() => moduleConfig.value?.canDelete && hasModuleAction(props.moduleKey, 'manage', permissions.value))

const filteredRows = computed(() => {
  const columns = moduleConfig.value?.columns || []
  const keyword = query.keyword.trim().toLowerCase()
  return rows.value.filter((row) => {
    const matchKeyword = !keyword || columns.some((column) => String(row[column.prop] ?? '').toLowerCase().includes(keyword))
    const matchStatus = !query.status || row.status === query.status || row.payStatus === query.status
    return matchKeyword && matchStatus
  })
})

const statusItems = computed(() => {
  const values = new Set()
  rows.value.forEach((row) => {
    if (row.status) values.add(row.status)
    if (row.payStatus) values.add(row.payStatus)
  })
  return [...values]
})

function displayCell(row, column) {
  const value = row[column.prop]
  if (column.money) return formatMoney(value)
  if (column.datetime) return formatDateTime(value)
  return value ?? '-'
}

function resetForm(row = null) {
  Object.keys(form).forEach((key) => delete form[key])
  for (const field of moduleConfig.value.fields || []) {
    form[field.prop] = row?.[field.prop] ?? defaultValue(field)
  }
}

function defaultValue(field) {
  if (field.type === 'number') return field.min ?? 0
  if (field.type === 'select') return field.options?.[0] || ''
  return ''
}

async function fetchData() {
  if (!moduleConfig.value?.resource) return
  loading.value = true
  try {
    const response = await platformApi.list(moduleConfig.value.resource, {
      page: pagination.page,
      size: pagination.size,
    })
    rows.value = pageRecords(response)
    total.value = pageTotal(response)
    if (!rows.value.length && total.value > 0 && pagination.page > 1) {
      pagination.page = Math.ceil(total.value / pagination.size)
      await fetchData()
    }
  } catch {
    ElMessage.warning('后端接口暂未响应，请确认 Spring Boot 已启动')
    rows.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function fetchFirstPage() {
  pagination.page = 1
  fetchData()
}

function handlePageChange(page) {
  pagination.page = page
  fetchData()
}

function openCreate() {
  if (!canCreateRecord.value) return
  editingRow.value = null
  resetForm()
  drawerVisible.value = true
}

function openEdit(row) {
  if (!canUpdateRecord.value) return
  editingRow.value = row
  resetForm(row)
  drawerVisible.value = true
}

async function submitForm() {
  if (editingRow.value && !canUpdateRecord.value) return
  if (!editingRow.value && !canCreateRecord.value) return
  const requiredMissing = (moduleConfig.value.fields || []).find((field) => field.required && !form[field.prop])
  if (requiredMissing) {
    ElMessage.warning(`请填写${requiredMissing.label}`)
    return
  }

  try {
    if (editingRow.value) {
      await platformApi.update(moduleConfig.value.resource, editingRow.value.id, { ...editingRow.value, ...form })
      ElMessage.success('记录已更新')
    } else {
      await platformApi.create(moduleConfig.value.resource, { ...form })
      ElMessage.success('记录已新增')
    }
    drawerVisible.value = false
    await fetchFirstPage()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '保存失败，请检查后端接口')
  }
}

async function removeRow(row) {
  if (!canDeleteRecord.value) return
  await ElMessageBox.confirm('删除后不可恢复，确认继续？', '删除记录', { type: 'warning' })
  await platformApi.remove(moduleConfig.value.resource, row.id)
  ElMessage.success('记录已删除')
  await fetchData()
}

onMounted(fetchData)
watch(() => props.moduleKey, () => {
  query.keyword = ''
  query.status = ''
  fetchFirstPage()
})
watch(() => [query.keyword, query.status], () => {
  pagination.page = 1
})
</script>

<template>
  <AppLayout>
    <div class="page-stack">
      <el-card shadow="never" class="section-card module-hero">
        <div>
          <p class="eyebrow">业务模块 / {{ moduleConfig.label }}</p>
          <h3>{{ moduleConfig.label }}</h3>
          <p>{{ moduleConfig.subtitle }}</p>
        </div>
        <div class="module-actions">
          <el-button :icon="Refresh" @click="fetchData">刷新</el-button>
          <el-button v-if="canCreateRecord" :icon="Plus" type="primary" @click="openCreate">新增记录</el-button>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card" v-loading="loading">
        <template #header>
          <div class="section-head">
            <div>
              <h3>数据列表</h3>
              <p>接口：/api/{{ moduleConfig.resource }}，当前 {{ filteredRows.length }} / {{ total }} 条</p>
            </div>
          </div>
        </template>

        <div class="table-toolbar">
          <el-input v-model="query.keyword" :prefix-icon="Search" clearable placeholder="搜索当前模块" />
          <el-select v-if="statusItems.length" v-model="query.status" clearable placeholder="状态筛选">
            <el-option v-for="item in statusItems" :key="item" :label="getStatusMeta(item).label" :value="item" />
          </el-select>
        </div>

        <el-table :data="filteredRows" :row-key="firstColumnProp" border stripe highlight-current-row empty-text="暂无数据">
          <el-table-column type="selection" width="46" />
          <el-table-column v-for="column in moduleConfig.columns" :key="column.prop" :prop="column.prop" :label="column.label" :min-width="column.minWidth" :width="column.width">
            <template #default="{ row }">
              <el-tag v-if="column.status" effect="plain" :type="getStatusMeta(row[column.prop]).type">
                {{ getStatusMeta(row[column.prop]).label }}
              </el-tag>
              <span v-else>{{ displayCell(row, column) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="170">
            <template #default="{ row }">
              <el-button v-if="canUpdateRecord" link type="primary" @click="openEdit(row)">编辑</el-button>
              <el-button v-if="canDeleteRecord" link type="danger" @click="removeRow(row)">删除</el-button>
              <span v-if="!canUpdateRecord && !canDeleteRecord" class="muted">只读</span>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-bar">
          <el-pagination
            background
            layout="prev, pager, next, total"
            :current-page="pagination.page"
            :page-size="pagination.size"
            :total="total"
            @current-change="handlePageChange"
          />
        </div>
      </el-card>

      <el-drawer v-model="drawerVisible" :title="editingRow ? `编辑${moduleConfig.label}` : `新增${moduleConfig.label}`" size="520px">
        <el-form label-position="top">
          <el-form-item v-for="field in moduleConfig.fields" :key="field.prop" :label="field.label" :required="field.required">
            <el-input-number
              v-if="field.type === 'number'"
              v-model="form[field.prop]"
              :min="field.min"
              :max="field.max"
              class="wide-input"
            />
            <el-select v-else-if="field.type === 'select'" v-model="form[field.prop]" class="wide-input">
              <el-option v-for="option in field.options" :key="option" :label="option" :value="option" />
            </el-select>
            <el-date-picker
              v-else-if="field.type === 'datetime'"
              v-model="form[field.prop]"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="wide-input"
            />
            <el-date-picker
              v-else-if="field.type === 'date'"
              v-model="form[field.prop]"
              type="date"
              value-format="YYYY-MM-DD"
              class="wide-input"
            />
            <el-input v-else-if="field.type === 'textarea'" v-model="form[field.prop]" type="textarea" :rows="4" />
            <el-input v-else v-model="form[field.prop]" />
          </el-form-item>
          <div class="drawer-actions">
            <el-button @click="drawerVisible = false">取消</el-button>
            <el-button type="primary" @click="submitForm">保存</el-button>
          </div>
        </el-form>
      </el-drawer>
    </div>
  </AppLayout>
</template>
