import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('sc_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem('sc_token')
      localStorage.removeItem('sc_user')
      localStorage.removeItem('sc_permissions')
      window.location.href = '/login'
    } else if (err.response && err.response.status === 403) {
      ElMessage.error(err.response.data?.message || '当前角色没有该操作权限')
    } else if (err.response && err.response.status >= 500) {
      ElMessage.error('后端服务异常，请检查 Spring Boot 控制台日志')
    }
    return Promise.reject(err)
  }
)

export const publicApi = {
  health: () => request.get('/public/health'),
  dictionaries: () => request.get('/public/dictionaries'),
  ossPolicy: () => request.get('/public/oss/policy'),
}

export const authApi = {
  login: (payload) => request.post('/auth/login', payload),
  logout: () => request.post('/auth/logout'),
  me: () => request.get('/auth/me'),
}

export const menuApi = {
  tree: () => request.get('/menu/tree'),
  permissions: () => request.get('/menu/permissions'),
}

export const platformApi = {
  list: (resource, params) => request.get(`/${resource}`, { params }),
  create: (resource, data) => request.post(`/${resource}`, data),
  update: (resource, id, data) => request.put(`/${resource}/${id}`, data),
  remove: (resource, id) => request.delete(`/${resource}/${id}`),
  approve: (resource, id, data) => request.post(`/${resource}/${id}/approve`, data),
  getInventoryAlerts: () => request.get('/inventory/alerts'),
}

export const sourcingApi = {
  listDemands: (params) => request.get('/sourcing/demands', { params }),
  createDemand: (data) => request.post('/sourcing/demands', data),
  updateDemand: (id, data) => request.put(`/sourcing/demands/${id}`, data),
  listQuotations: (params) => request.get('/sourcing/quotations', { params }),
  createQuotation: (data) => request.post('/sourcing/quotations', data),
  updateQuotation: (id, data) => request.put(`/sourcing/quotations/${id}`, data),
  listQuotes: (params) => request.get('/sourcing/quotes', { params }),
  createQuote: (data) => request.post('/sourcing/quotes', data),
  listAwards: (params) => request.get('/sourcing/awards', { params }),
  createAward: (data) => request.post('/sourcing/awards', data),
  updateAward: (id, data) => request.put(`/sourcing/awards/${id}`, data),
  listNotices: (params) => request.get('/sourcing/award-notices', { params }),
  createNotice: (data) => request.post('/sourcing/award-notices', data),
  updateNotice: (id, data) => request.put(`/sourcing/award-notices/${id}`, data),
  listApprovals: (bizType, bizId) => request.get('/sourcing/approvals', { params: { bizType, bizId } }),
  approve: (data) => request.post('/sourcing/approvals', data),
  pendingTasks: (userId, params) => request.get(`/sourcing/tasks/pending/${userId}`, { params }),
  taskCount: (userId) => request.get(`/sourcing/tasks/count/${userId}`),
  notifications: (userId, params) => request.get(`/sourcing/notifications/${userId}`, { params }),
}

export function unwrap(response) {
  return response?.data ?? response
}

export function pageRecords(payload) {
  const data = unwrap(payload)
  if (Array.isArray(data)) return data
  if (Array.isArray(data?.records)) return data.records
  if (Array.isArray(data?.content)) return data.content
  return []
}

export function pageTotal(payload) {
  const data = unwrap(payload)
  if (Array.isArray(data)) return data.length
  return Number(data?.total ?? data?.totalElements ?? pageRecords(data).length)
}

export default request
