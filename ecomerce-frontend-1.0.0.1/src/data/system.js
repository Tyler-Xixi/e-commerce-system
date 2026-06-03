export const roleMeta = {
  JS01: {
    label: '品类工程师',
    scope: '发起寻源需求、发布 RFQ、汇总报价、生成商务定点单',
    accent: 'primary',
  },
  JS02: {
    label: '品类组长',
    scope: '审核寻源需求、把控 RFQ 质量、推动询价发布',
    accent: 'warning',
  },
  JS03: {
    label: '采购处长',
    scope: '最终审批商务定点、下达采购指令、查看全量模块',
    accent: 'danger',
  },
  JS04: {
    label: '平台管理员',
    scope: '系统配置、用户与菜单维护',
    accent: 'info',
  },
  JS05: {
    label: '供应商操作员',
    scope: '查看 RFQ 公告、提交报价、跟踪自己的报价状态',
    accent: 'success',
  },
  JS06: {
    label: '仓储调度员',
    scope: '库存入库出库、预警处理、物流配送调度',
    accent: 'info',
  },
}

export const demoAccounts = [
  { username: 'engineer', password: '123456', roleCode: 'JS01', name: '品类工程师' },
  { username: 'leader', password: '123456', roleCode: 'JS02', name: '品类组长' },
  { username: 'director', password: '123456', roleCode: 'JS03', name: '采购处长' },
  { username: 'supplier', password: '123456', roleCode: 'JS05', name: '供应商操作员' },
  { username: 'warehouse', password: '123456', roleCode: 'JS06', name: '仓储调度员' },
  { username: 'admin', password: '123456', roleCode: 'JS04', name: '平台管理员' },
]

export const roleMenus = {
  JS01: ['dashboard', 'sourcing', 'products', 'inventory'],
  JS02: ['dashboard', 'sourcing', 'products', 'inventory'],
  JS03: ['dashboard', 'sourcing', 'suppliers', 'products', 'orders', 'inventory', 'logistics', 'payments', 'aftersales', 'users', 'architecture'],
  JS04: ['dashboard', 'sourcing', 'suppliers', 'products', 'orders', 'inventory', 'logistics', 'payments', 'aftersales', 'users', 'architecture'],
  JS05: ['dashboard', 'sourcing'],
  JS06: ['dashboard', 'inventory', 'logistics'],
}

export const menuPermissionMap = {
  dashboard: 'dashboard:todo',
  sourcing: 'sourcing:rfq:list',
  suppliers: 'supplier:list',
  products: 'product:list',
  orders: 'purchase:order:list',
  inventory: 'warehouse:inventory',
  logistics: 'warehouse:logistics',
  payments: 'purchase:payment:list',
  aftersales: 'aftersales:list',
  users: 'system:user:manage',
  architecture: 'system:menu:manage',
}

export const modulePermissionMap = {
  suppliers: { view: 'supplier:list', manage: 'supplier:manage' },
  products: { view: 'product:list', manage: 'product:manage' },
  orders: { view: 'purchase:order:list', manage: 'purchase:order:manage' },
  inventory: { view: 'warehouse:inventory', manage: 'warehouse:inventory:manage' },
  logistics: { view: 'warehouse:logistics', manage: 'warehouse:logistics:manage' },
  payments: { view: 'purchase:payment:list', manage: 'purchase:payment:manage' },
  aftersales: { view: 'aftersales:list', manage: 'aftersales:manage' },
  users: { view: 'system:user:manage', manage: 'system:user:manage' },
}

export const statusMeta = {
  PENDING: { label: '待审核', type: 'warning' },
  ASSIGNED: { label: '已分派', type: 'primary' },
  COMPLETED: { label: '已完成', type: 'success' },
  REJECTED: { label: '已驳回', type: 'danger' },
  DRAFT: { label: '草稿', type: 'info' },
  APPROVING: { label: '审批中', type: 'warning' },
  APPROVED: { label: '已发布', type: 'success' },
  PUBLISHED: { label: '已发布', type: 'success' },
  SUBMITTED: { label: '已提交', type: 'success' },
  LOCKED: { label: '已锁定', type: 'info' },
  HIGH: { label: '高', type: 'danger' },
  MEDIUM: { label: '中', type: 'warning' },
  LOW: { label: '低', type: 'info' },
  NORMAL: { label: '正常', type: 'success' },
  WARNING: { label: '预警', type: 'warning' },
  待接收: { label: '待接收', type: 'warning' },
  正常: { label: '正常', type: 'success' },
  预警: { label: '预警', type: 'warning' },
  上架: { label: '上架', type: 'success' },
  下架: { label: '下架', type: 'info' },
  启用: { label: '启用', type: 'success' },
  禁用: { label: '禁用', type: 'danger' },
  待付款: { label: '待付款', type: 'warning' },
  已付款: { label: '已付款', type: 'success' },
  待入库: { label: '待入库', type: 'warning' },
  已入库: { label: '已入库', type: 'success' },
  运输中: { label: '运输中', type: 'primary' },
  已完成: { label: '已完成', type: 'success' },
}

export const workflowSteps = [
  { roleCode: 'JS01', title: '创建寻源需求', desc: '填写品类、数量、预算和规格，提交给组长审核。' },
  { roleCode: 'JS02', title: '审核并发布 RFQ', desc: '审核需求合理性，通过后自动生成询价单。' },
  { roleCode: 'JS05', title: '供应商报价', desc: '供应商查看 RFQ 公告，仅提交并查看自己的报价。' },
  { roleCode: 'JS01', title: '报价汇总定点', desc: '系统按单价排序，工程师选择最优供应商生成定点单。' },
  { roleCode: 'JS03', title: '最终审批执行', desc: '采购处长审批定点结果，通过后触发采购、付款、入库链路。' },
]

const commonStatusOptions = ['正常', '启用', '禁用']

export const moduleDefinitions = {
  suppliers: {
    path: '/suppliers',
    name: 'suppliers',
    label: '供应商管理',
    icon: 'OfficeBuilding',
    resource: 'suppliers',
    roles: ['JS03', 'JS04'],
    subtitle: '维护供应商编码、等级、评分和准入状态。',
    canCreate: true,
    canUpdate: true,
    canDelete: true,
    columns: [
      { prop: 'code', label: '供应商编码', minWidth: 130 },
      { prop: 'name', label: '供应商名称', minWidth: 180 },
      { prop: 'contact', label: '联系人', minWidth: 100 },
      { prop: 'grade', label: '等级', width: 90 },
      { prop: 'score', label: '评分', width: 90 },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [
      { prop: 'code', label: '供应商编码', required: true },
      { prop: 'name', label: '供应商名称', required: true },
      { prop: 'contact', label: '联系人' },
      { prop: 'grade', label: '等级', type: 'select', options: ['A', 'B', 'C'] },
      { prop: 'score', label: '评分', type: 'number', min: 0, max: 100 },
      { prop: 'status', label: '状态', type: 'select', options: ['正常', '观察', '禁用'] },
    ],
  },
  products: {
    path: '/products',
    name: 'products',
    label: '商品库',
    icon: 'Goods',
    resource: 'products',
    roles: ['JS01', 'JS02', 'JS03', 'JS04'],
    subtitle: '维护采购品类、SKU、计量单位、参考价和上下架状态。',
    canCreate: true,
    canUpdate: true,
    canDelete: true,
    columns: [
      { prop: 'sku', label: 'SKU', minWidth: 130 },
      { prop: 'name', label: '商品名称', minWidth: 180 },
      { prop: 'category', label: '品类', minWidth: 130 },
      { prop: 'unit', label: '单位', width: 80 },
      { prop: 'price', label: '参考价', width: 120, money: true },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [
      { prop: 'sku', label: 'SKU', required: true },
      { prop: 'name', label: '商品名称', required: true },
      { prop: 'category', label: '品类', required: true },
      { prop: 'unit', label: '单位' },
      { prop: 'price', label: '参考价', type: 'number', min: 0 },
      { prop: 'status', label: '状态', type: 'select', options: ['上架', '下架'] },
    ],
  },
  orders: {
    path: '/orders',
    name: 'orders',
    label: '采购订单',
    icon: 'Tickets',
    resource: 'orders',
    roles: ['JS03', 'JS04'],
    subtitle: '商务定点通过后生成采购指令或 PO 单。',
    canCreate: true,
    canUpdate: true,
    canDelete: true,
    columns: [
      { prop: 'code', label: '订单号', minWidth: 130 },
      { prop: 'customer', label: '采购主体', minWidth: 140 },
      { prop: 'product', label: '采购物料', minWidth: 180 },
      { prop: 'amount', label: '金额', width: 120, money: true },
      { prop: 'payStatus', label: '付款状态', width: 110, status: true },
      { prop: 'status', label: '执行状态', width: 110, status: true },
    ],
    fields: [
      { prop: 'code', label: '订单号', required: true },
      { prop: 'customer', label: '采购主体', required: true },
      { prop: 'product', label: '采购物料', required: true },
      { prop: 'amount', label: '金额', type: 'number', min: 0 },
      { prop: 'payStatus', label: '付款状态', type: 'select', options: ['待付款', '已付款'] },
      { prop: 'status', label: '执行状态', type: 'select', options: ['待入库', '运输中', '已完成'] },
    ],
  },
  inventory: {
    path: '/inventory',
    name: 'inventory',
    label: '仓储库存',
    icon: 'Box',
    resource: 'inventory',
    roles: ['JS01', 'JS02', 'JS03', 'JS04', 'JS06'],
    subtitle: '查看可用库存、锁定库存、预警阈值和仓库分布。',
    canCreate: true,
    canUpdate: true,
    canDelete: true,
    columns: [
      { prop: 'sku', label: 'SKU', minWidth: 130 },
      { prop: 'warehouse', label: '仓库', minWidth: 150 },
      { prop: 'available', label: '可用库存', width: 110 },
      { prop: 'locked', label: '锁定库存', width: 110 },
      { prop: 'alert', label: '预警阈值', width: 110 },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [
      { prop: 'sku', label: 'SKU', required: true },
      { prop: 'warehouse', label: '仓库', required: true },
      { prop: 'available', label: '可用库存', type: 'number', min: 0 },
      { prop: 'locked', label: '锁定库存', type: 'number', min: 0 },
      { prop: 'alert', label: '预警阈值', type: 'number', min: 0 },
      { prop: 'status', label: '状态', type: 'select', options: ['正常', '预警'] },
    ],
  },
  logistics: {
    path: '/logistics',
    name: 'logistics',
    label: '物流调度',
    icon: 'Van',
    resource: 'logistics',
    roles: ['JS03', 'JS04', 'JS06'],
    subtitle: '管理承运商、运输路线、预计到达时间和配送状态。',
    canCreate: true,
    canUpdate: true,
    canDelete: false,
    columns: [
      { prop: 'code', label: '物流单号', minWidth: 130 },
      { prop: 'orderNo', label: '关联订单', minWidth: 130 },
      { prop: 'carrier', label: '承运商', minWidth: 130 },
      { prop: 'route', label: '路线', minWidth: 200 },
      { prop: 'eta', label: '预计到达', minWidth: 150, datetime: true },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [
      { prop: 'code', label: '物流单号', required: true },
      { prop: 'orderNo', label: '关联订单' },
      { prop: 'carrier', label: '承运商' },
      { prop: 'route', label: '路线' },
      { prop: 'eta', label: '预计到达', type: 'datetime' },
      { prop: 'status', label: '状态', type: 'select', options: ['待调度', '运输中', '已完成'] },
    ],
  },
  payments: {
    path: '/payments',
    name: 'payments',
    label: '付款结算',
    icon: 'Wallet',
    resource: 'payments',
    roles: ['JS03', 'JS04'],
    subtitle: '跟踪采购订单付款渠道、对账日期和结算状态。',
    canCreate: true,
    canUpdate: true,
    canDelete: false,
    columns: [
      { prop: 'code', label: '结算单号', minWidth: 130 },
      { prop: 'orderNo', label: '订单号', minWidth: 130 },
      { prop: 'channel', label: '付款渠道', minWidth: 120 },
      { prop: 'amount', label: '金额', width: 120, money: true },
      { prop: 'accountDate', label: '对账日期', width: 120 },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [
      { prop: 'code', label: '结算单号', required: true },
      { prop: 'orderNo', label: '订单号' },
      { prop: 'channel', label: '付款渠道', type: 'select', options: ['银行转账', '企业网银', '承兑汇票'] },
      { prop: 'amount', label: '金额', type: 'number', min: 0 },
      { prop: 'accountDate', label: '对账日期', type: 'date' },
      { prop: 'status', label: '状态', type: 'select', options: ['待付款', '已付款', '待对账'] },
    ],
  },
  aftersales: {
    path: '/aftersales',
    name: 'aftersales',
    label: '售后工单',
    icon: 'Service',
    resource: 'aftersales',
    roles: ['JS03', 'JS04'],
    subtitle: '记录采购履约后的退换货、质量异常和责任人。',
    canCreate: true,
    canUpdate: true,
    canDelete: false,
    columns: [
      { prop: 'code', label: '工单号', minWidth: 130 },
      { prop: 'orderNo', label: '订单号', minWidth: 130 },
      { prop: 'type', label: '类型', width: 110 },
      { prop: 'owner', label: '责任人', width: 110 },
      { prop: 'reason', label: '原因', minWidth: 220 },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [
      { prop: 'code', label: '工单号', required: true },
      { prop: 'orderNo', label: '订单号' },
      { prop: 'type', label: '类型', type: 'select', options: ['质量异常', '退货', '换货'] },
      { prop: 'owner', label: '责任人' },
      { prop: 'reason', label: '原因', type: 'textarea' },
      { prop: 'status', label: '状态', type: 'select', options: ['待处理', '处理中', '已完成'] },
    ],
  },
  users: {
    path: '/users',
    name: 'users',
    label: '用户与角色',
    icon: 'User',
    resource: 'users',
    roles: ['JS03', 'JS04'],
    subtitle: '查看系统账号、角色编码、部门和启停状态。',
    canCreate: false,
    canUpdate: false,
    canDelete: false,
    columns: [
      { prop: 'code', label: '用户编码', minWidth: 120 },
      { prop: 'username', label: '登录账号', minWidth: 130 },
      { prop: 'name', label: '姓名', minWidth: 120 },
      { prop: 'role', label: '角色', minWidth: 130 },
      { prop: 'roleCode', label: '角色码', width: 100 },
      { prop: 'dept', label: '部门', minWidth: 120 },
      { prop: 'status', label: '状态', width: 100, status: true },
    ],
    fields: [],
  },
}

export function getRoleLabel(roleCode) {
  return roleMeta[roleCode]?.label || roleCode || '未知角色'
}

export function getStatusMeta(value) {
  return statusMeta[value] || { label: value || '未设置', type: 'info' }
}

export function formatMoney(value) {
  if (value === null || value === undefined || value === '') return '-'
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY',
    maximumFractionDigits: 2,
  }).format(Number(value))
}

export function formatDateTime(value) {
  if (!value) return '-'
  return String(value).replace('T', ' ').slice(0, 16)
}

export function hasRoleAccess(roleCode, roles = []) {
  if (!roles.length) return true
  if (roleCode === 'JS03' || roleCode === 'JS04') return true
  return roles.includes(roleCode)
}

export function getStoredPermissions() {
  try {
    const parsed = JSON.parse(localStorage.getItem('sc_permissions') || '[]')
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

export function hasPermission(permissions, permission) {
  if (!permission) return true
  if (!Array.isArray(permissions) || permissions.length === 0) return true
  return permissions.includes(permission)
}

export function hasMenuAccess(menuKey, permissions, roleCode) {
  const permission = menuPermissionMap[menuKey]
  if (permission && Array.isArray(permissions) && permissions.length > 0) {
    return permissions.includes(permission)
  }
  const allowedKeys = roleMenus[roleCode] || ['dashboard']
  return allowedKeys.includes(menuKey)
}

export function hasModuleAction(moduleKey, action, permissions) {
  const permission = modulePermissionMap[moduleKey]?.[action]
  return hasPermission(permissions, permission)
}

export function generateNo(prefix) {
  const now = new Date()
  const pad = (n) => String(n).padStart(2, '0')
  return `${prefix}-${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}-${String(now.getTime()).slice(-5)}`
}

export function plusDays(days) {
  const date = new Date()
  date.setDate(date.getDate() + days)
  const pad = (n) => String(n).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:00`
}
