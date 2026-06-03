<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check, Plus, Refresh, TrendCharts } from '@element-plus/icons-vue'

import AppLayout from '@/components/AppLayout.vue'
import { pageRecords, pageTotal, platformApi, sourcingApi } from '@/services/api'
import { formatDateTime, formatMoney, generateNo, getRoleLabel, getStatusMeta, getStoredPermissions, hasPermission, plusDays } from '@/data/system'

const PAGE_SIZE = 10
const LOOKUP_SIZE = 1000

const route = useRoute()
const activeTab = ref(route.query.tab || 'demands')
const loading = ref(false)
const demandDrawer = ref(false)
const quoteDrawer = ref(false)
const selectedRfq = ref(null)

const user = computed(() => JSON.parse(localStorage.getItem('sc_user') || '{}'))
const roleCode = computed(() => user.value.roleCode || 'JS01')
const permissions = computed(() => getStoredPermissions())

const demands = ref([])
const quotations = ref([])
const quotes = ref([])
const awards = ref([])
const suppliers = ref([])
const products = ref([])
const rfqLookup = ref([])

const pagination = reactive({
  demands: { page: 1, size: PAGE_SIZE, total: 0 },
  rfq: { page: 1, size: PAGE_SIZE, total: 0 },
  quotes: { page: 1, size: PAGE_SIZE, total: 0 },
  awards: { page: 1, size: PAGE_SIZE, total: 0 },
})

const demandForm = reactive({
  categoryCode: '',
  quantity: 100,
  budget: 0,
  specification: '',
  deadline: plusDays(7),
})

const quoteForm = reactive({
  rfqId: null,
  quoteType: 'L2',
  unitPrice: 0,
  moq: 100,
  deliveryDate: plusDays(15),
  warrantyPeriod: '3年质保',
  paymentTerms: '验收后60天付清',
})

const canCreateDemand = computed(() => hasPermission(permissions.value, 'sourcing:demand:create') && ['JS01', 'JS03', 'JS04'].includes(roleCode.value))
const canReviewDemand = computed(() => hasPermission(permissions.value, 'sourcing:demand:review') && ['JS02', 'JS03', 'JS04'].includes(roleCode.value))
const canQuote = computed(() => hasPermission(permissions.value, 'sourcing:quote:create') && roleCode.value === 'JS05')
const canCreateAward = computed(() => hasPermission(permissions.value, 'sourcing:award:manage') && ['JS01', 'JS03', 'JS04'].includes(roleCode.value))
const canApproveAward = computed(() => hasPermission(permissions.value, 'sourcing:award:approve') && ['JS03', 'JS04'].includes(roleCode.value))

const supplierMap = computed(() => new Map(suppliers.value.map((item) => [Number(item.id), item])))
const rfqMap = computed(() => new Map(rfqLookup.value.map((item) => [Number(item.id), item])))

const visibleDemands = computed(() => {
  if (roleCode.value === 'JS01') {
    return demands.value.filter((item) => !item.createdBy || Number(item.createdBy) === Number(user.value.id) || Number(item.assigneeId) === Number(user.value.id))
  }
  return demands.value
})

const visibleRfqs = computed(() => {
  if (roleCode.value === 'JS05') {
    return quotations.value.filter((item) => item.status !== 'DRAFT')
  }
  return quotations.value
})

const enrichedQuotes = computed(() => quotes.value.map((item) => {
  const supplier = supplierMap.value.get(Number(item.supplierId))
  const rfq = rfqMap.value.get(Number(item.rfqId))
  return {
    ...item,
    supplierName: item.supplierName || supplier?.name || `供应商 ${item.supplierId}`,
    rfqNo: item.rfqNo || rfq?.rfqNo || `RFQ-${item.rfqId}`,
    categoryCode: rfq?.categoryCode,
  }
}))

const quoteRanking = computed(() => {
  const source = roleCode.value === 'JS05'
    ? enrichedQuotes.value.filter((item) => Number(item.supplierId) === Number(user.value.id))
    : enrichedQuotes.value
  return [...source].sort((a, b) => Number(a.unitPrice || 0) - Number(b.unitPrice || 0))
})

const enrichedAwards = computed(() => awards.value.map((item) => {
  const supplier = supplierMap.value.get(Number(item.supplierId))
  const rfq = rfqMap.value.get(Number(item.rfqId))
  return {
    ...item,
    supplierName: item.supplierName || supplier?.name || `供应商 ${item.supplierId}`,
    rfqNo: item.rfqNo || rfq?.rfqNo || `RFQ-${item.rfqId}`,
  }
}))

const visibleAwards = computed(() => {
  if (roleCode.value === 'JS05') {
    return enrichedAwards.value.filter((item) => Number(item.supplierId) === Number(user.value.id))
  }
  return enrichedAwards.value
})

const quoteTotal = computed(() => {
  const quantity = Number(selectedRfq.value?.quantity || 0)
  return Number(quoteForm.unitPrice || 0) * quantity
})

function statusType(status) {
  return getStatusMeta(status).type
}

function statusLabel(status) {
  return getStatusMeta(status).label
}

function resetDemandForm() {
  Object.assign(demandForm, {
    categoryCode: products.value[0]?.category || products.value[0]?.name || '',
    quantity: 100,
    budget: 0,
    specification: '',
    deadline: plusDays(7),
  })
}

function resetQuoteForm(rfq) {
  selectedRfq.value = rfq
  Object.assign(quoteForm, {
    rfqId: rfq.id,
    quoteType: 'L2',
    unitPrice: 0,
    moq: 100,
    deliveryDate: plusDays(15),
    warrantyPeriod: '3年质保',
    paymentTerms: '验收后60天付清',
  })
  quoteDrawer.value = true
}

function demandParams() {
  return {
    page: pagination.demands.page,
    size: pagination.demands.size,
    ...(roleCode.value === 'JS01' ? { userId: user.value.id, mineOnly: true } : {}),
  }
}

function rfqParams() {
  return {
    page: pagination.rfq.page,
    size: pagination.rfq.size,
    ...(roleCode.value === 'JS05' ? { publishedOnly: true } : {}),
  }
}

function quoteParams() {
  return {
    page: pagination.quotes.page,
    size: pagination.quotes.size,
    ...(roleCode.value === 'JS05' ? { supplierId: user.value.id } : {}),
  }
}

function awardParams() {
  return {
    page: pagination.awards.page,
    size: pagination.awards.size,
    ...(roleCode.value === 'JS05' ? { supplierId: user.value.id } : {}),
  }
}

function applyPageResult(result, key, target) {
  if (result.status !== 'fulfilled') {
    target.value = []
    pagination[key].total = 0
    return false
  }
  target.value = pageRecords(result.value)
  pagination[key].total = pageTotal(result.value)
  return true
}

async function normalizeEmptyPages() {
  const entries = [
    ['demands', demands],
    ['rfq', quotations],
    ['quotes', quotes],
    ['awards', awards],
  ]
  const shouldReload = entries.some(([key, target]) => {
    const pageState = pagination[key]
    if (target.value.length || pageState.total <= 0 || pageState.page <= 1) return false
    const nextPage = Math.ceil(pageState.total / pageState.size)
    if (nextPage === pageState.page) return false
    pageState.page = nextPage
    return true
  })
  if (shouldReload) await fetchAll()
}

function applyLookupResult(result, target) {
  target.value = result.status === 'fulfilled' ? pageRecords(result.value) : []
}

function resetPage(key) {
  pagination[key].page = 1
}

function handleSourcingPageChange(key, page) {
  pagination[key].page = page
  fetchAll()
}

function fetchIfAllowed(permission, loader, fallback = { data: { records: [], total: 0 } }) {
  return hasPermission(permissions.value, permission) ? loader() : Promise.resolve(fallback)
}

async function fetchAll() {
  loading.value = true
  try {
    const [dRes, qRes, qtRes, aRes, sRes, pRes, rfqLookupRes] = await Promise.allSettled([
      fetchIfAllowed('sourcing:demand:list', () => sourcingApi.listDemands(demandParams())),
      fetchIfAllowed('sourcing:rfq:list', () => sourcingApi.listQuotations(rfqParams())),
      fetchIfAllowed('sourcing:quote:summary', () => sourcingApi.listQuotes(quoteParams())),
      fetchIfAllowed('sourcing:award:list', () => sourcingApi.listAwards(awardParams())),
      fetchIfAllowed('supplier:list', () => platformApi.list('suppliers', { page: 1, size: LOOKUP_SIZE })),
      fetchIfAllowed('product:list', () => platformApi.list('products', { page: 1, size: LOOKUP_SIZE })),
      fetchIfAllowed('sourcing:rfq:list', () => sourcingApi.listQuotations({ page: 1, size: LOOKUP_SIZE })),
    ])
    const loaded = [
      applyPageResult(dRes, 'demands', demands),
      applyPageResult(qRes, 'rfq', quotations),
      applyPageResult(qtRes, 'quotes', quotes),
      applyPageResult(aRes, 'awards', awards),
    ]
    applyLookupResult(sRes, suppliers)
    applyLookupResult(pRes, products)
    applyLookupResult(rfqLookupRes, rfqLookup)
    if (loaded.includes(false) || [sRes, pRes, rfqLookupRes].some((item) => item.status !== 'fulfilled')) {
      ElMessage.warning('寻源数据加载不完整，请确认后端服务可用')
    }
    await normalizeEmptyPages()
  } catch {
    ElMessage.warning('寻源数据加载失败，请确认后端服务可用')
  } finally {
    loading.value = false
  }
}

async function submitDemand() {
  if (!demandForm.categoryCode || !demandForm.quantity || !demandForm.deadline) {
    ElMessage.warning('请填写品类、数量和截止时间')
    return
  }
  const specification = [
    demandForm.specification,
    demandForm.budget ? `预算：${formatMoney(demandForm.budget)}` : '',
  ].filter(Boolean).join('；')

  await sourcingApi.createDemand({
    demandNo: generateNo('DEM'),
    categoryCode: demandForm.categoryCode,
    quantity: demandForm.quantity,
    specification,
    deadline: demandForm.deadline,
    status: 'PENDING',
    assigneeId: user.value.id,
    createdBy: user.value.id,
  })
  ElMessage.success('寻源需求已提交给品类组长审核')
  demandDrawer.value = false
  resetPage('demands')
  await fetchAll()
}

async function approveDemand(row) {
  await ElMessageBox.confirm('通过后将自动生成 RFQ，并发布给供应商报价。', '确认审核通过', { type: 'warning' })
  await sourcingApi.updateDemand(row.id, { ...row, status: 'COMPLETED' })
  await sourcingApi.createQuotation({
    demandId: row.id,
    categoryCode: row.categoryCode,
    quantity: row.quantity,
    specification: row.specification,
    deliveryDate: plusDays(30),
    paymentTerms: '验收合格后60天付款',
    quoteDeadline: row.deadline || plusDays(5),
    sourceType: 'INQUIRY',
    status: 'APPROVED',
    createdBy: row.assigneeId || user.value.id,
    remark: '需求审核通过后自动发布 RFQ',
  })
  await sourcingApi.approve({
    bizType: 'DEMAND',
    bizId: row.id,
    approverId: user.value.id,
    action: 'APPROVE',
    opinion: '需求合理，已发布询价单',
  })
  ElMessage.success('已审核通过并自动发布 RFQ')
  activeTab.value = 'rfq'
  resetPage('demands')
  resetPage('rfq')
  await fetchAll()
}

async function rejectDemand(row) {
  await ElMessageBox.confirm('驳回后需求将回退给品类工程师修改。', '确认驳回需求', { type: 'warning' })
  await sourcingApi.updateDemand(row.id, { ...row, status: 'REJECTED' })
  await sourcingApi.approve({
    bizType: 'DEMAND',
    bizId: row.id,
    approverId: user.value.id,
    action: 'REJECT',
    opinion: '需求信息需要补充，请修改后重新提交',
  })
  ElMessage.success('已驳回需求')
  resetPage('demands')
  await fetchAll()
}

async function submitQuote() {
  if (!selectedRfq.value || !quoteForm.unitPrice || !quoteForm.deliveryDate) {
    ElMessage.warning('请填写报价单价和交付日期')
    return
  }
  await sourcingApi.createQuote({
    rfqId: selectedRfq.value.id,
    supplierId: user.value.id,
    quoteType: quoteForm.quoteType,
    unitPrice: quoteForm.unitPrice,
    totalPrice: quoteTotal.value,
    deliveryDate: quoteForm.deliveryDate,
    warrantyPeriod: quoteForm.warrantyPeriod,
    paymentTerms: quoteForm.paymentTerms,
    batchPriceGradient: JSON.stringify({ moq: quoteForm.moq }),
    costBreakdown: JSON.stringify({ material: '按供应商报价明细附件确认' }),
    status: 'SUBMITTED',
  })
  ElMessage.success('报价已提交，品类工程师可在报价汇总中比价')
  quoteDrawer.value = false
  activeTab.value = 'quotes'
  resetPage('quotes')
  await fetchAll()
}

async function createAwardFromQuote(row) {
  await ElMessageBox.confirm(`确认选择 ${row.supplierName} 作为意向定点供应商？`, '生成商务定点单', { type: 'warning' })
  await sourcingApi.createAward({
    awardNo: generateNo('BA'),
    rfqId: row.rfqId,
    supplierId: row.supplierId,
    analysisReport: `系统按单价排序，选择 ${row.supplierName}，单价 ${row.unitPrice}，总价 ${row.totalPrice}`,
    status: 'APPROVING',
    createdBy: user.value.id,
  })
  ElMessage.success('商务定点单已生成，等待采购处长审批')
  activeTab.value = 'awards'
  resetPage('awards')
  await fetchAll()
}

async function approveAward(row) {
  await ElMessageBox.confirm('审批通过后将生成采购订单、付款结算和物流调度记录。', '采购处长最终审批', { type: 'warning' })
  const rfq = rfqMap.value.get(Number(row.rfqId)) || {}
  const winningQuote = enrichedQuotes.value.find((item) => Number(item.rfqId) === Number(row.rfqId) && Number(item.supplierId) === Number(row.supplierId)) || {}
  const orderNo = generateNo('PO')
  const amount = Number(winningQuote.totalPrice || 0)

  await sourcingApi.updateAward(row.id, { ...row, status: 'APPROVED' })
  await sourcingApi.approve({
    bizType: 'AWARD',
    bizId: row.id,
    approverId: user.value.id,
    action: 'APPROVE',
    opinion: '商务定点结果合理，同意下达采购指令',
  })

  await Promise.allSettled([
    platformApi.create('orders', {
      code: orderNo,
      customer: '采购中心',
      product: rfq.categoryCode || row.rfqNo,
      amount,
      payStatus: '待付款',
      status: '待入库',
    }),
    platformApi.create('payments', {
      code: generateNo('PAY'),
      orderNo,
      channel: '企业网银',
      amount,
      accountDate: new Date().toISOString().slice(0, 10),
      status: '待付款',
    }),
    platformApi.create('logistics', {
      code: generateNo('LOG'),
      orderNo,
      carrier: '待分配',
      route: `${row.supplierName} -> 中心仓`,
      eta: plusDays(30),
      status: '待调度',
    }),
  ])

  ElMessage.success('商务定点已审批通过，并触发采购、付款和物流流程')
  resetPage('awards')
  await fetchAll()
}

async function rejectAward(row) {
  await ElMessageBox.confirm('确认驳回该商务定点结果？', '驳回商务定点', { type: 'warning' })
  await sourcingApi.updateAward(row.id, { ...row, status: 'REJECTED' })
  await sourcingApi.approve({
    bizType: 'AWARD',
    bizId: row.id,
    approverId: user.value.id,
    action: 'REJECT',
    opinion: '定点依据不足，请重新比价',
  })
  ElMessage.success('已驳回商务定点单')
  resetPage('awards')
  await fetchAll()
}

watch(() => route.query.tab, (tab) => {
  if (tab) activeTab.value = tab
})

onMounted(async () => {
  await fetchAll()
  resetDemandForm()
})
</script>

<template>
  <AppLayout>
    <div class="page-stack" v-loading="loading">
      <el-card shadow="never" class="section-card module-hero">
        <div>
          <p class="eyebrow">角色：{{ getRoleLabel(roleCode) }} / 寻源业务闭环</p>
          <h3>寻源定点工作区</h3>
          <p>
            按需求文档串联 JS01 创建需求、JS02 审核发布 RFQ、JS05 报价、JS01 比价定点、JS03 最终审批。
          </p>
        </div>
        <div class="module-actions">
          <el-button :icon="Refresh" @click="fetchAll">刷新</el-button>
          <el-button v-if="canCreateDemand" type="primary" :icon="Plus" @click="resetDemandForm(); demandDrawer = true">创建寻源需求</el-button>
        </div>
      </el-card>

      <el-card shadow="never" class="section-card">
        <el-tabs v-model="activeTab">
          <el-tab-pane v-if="roleCode !== 'JS05' && roleCode !== 'JS06'" label="寻源需求" name="demands">
            <el-table :data="visibleDemands" row-key="id" border empty-text="暂无寻源需求">
              <el-table-column prop="demandNo" label="需求编号" min-width="150" />
              <el-table-column prop="categoryCode" label="采购品类" min-width="140" />
              <el-table-column prop="quantity" label="数量" width="100" />
              <el-table-column prop="specification" label="规格/预算" min-width="260" show-overflow-tooltip />
              <el-table-column prop="deadline" label="截止时间" width="150">
                <template #default="{ row }">{{ formatDateTime(row.deadline) }}</template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="110">
                <template #default="{ row }">
                  <el-tag :type="statusType(row.status)" effect="plain">{{ statusLabel(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" fixed="right" width="190">
                <template #default="{ row }">
                  <el-button v-if="canReviewDemand && row.status !== 'COMPLETED'" link type="success" @click="approveDemand(row)">通过并发 RFQ</el-button>
                  <el-button v-if="canReviewDemand && row.status !== 'REJECTED'" link type="danger" @click="rejectDemand(row)">驳回</el-button>
                  <span v-if="!canReviewDemand">等待组长审核</span>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-bar">
              <el-pagination
                background
                layout="prev, pager, next, total"
                :current-page="pagination.demands.page"
                :page-size="pagination.demands.size"
                :total="pagination.demands.total"
                @current-change="(page) => handleSourcingPageChange('demands', page)"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane v-if="roleCode !== 'JS06'" label="RFQ 公告" name="rfq">
            <el-table :data="visibleRfqs" row-key="id" border empty-text="暂无 RFQ">
              <el-table-column prop="rfqNo" label="RFQ 编号" min-width="160" />
              <el-table-column prop="categoryCode" label="采购品类" min-width="140" />
              <el-table-column prop="quantity" label="数量" width="100" />
              <el-table-column prop="specification" label="询价说明" min-width="260" show-overflow-tooltip />
              <el-table-column prop="quoteDeadline" label="报价截止" width="150">
                <template #default="{ row }">{{ formatDateTime(row.quoteDeadline) }}</template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="110">
                <template #default="{ row }">
                  <el-tag :type="statusType(row.status)" effect="plain">{{ statusLabel(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" fixed="right" width="150">
                <template #default="{ row }">
                  <el-button v-if="canQuote" link type="primary" @click="resetQuoteForm(row)">提交报价</el-button>
                  <el-button v-else link type="primary" disabled>等待报价</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-bar">
              <el-pagination
                background
                layout="prev, pager, next, total"
                :current-page="pagination.rfq.page"
                :page-size="pagination.rfq.size"
                :total="pagination.rfq.total"
                @current-change="(page) => handleSourcingPageChange('rfq', page)"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane v-if="roleCode !== 'JS06'" label="报价汇总" name="quotes">
            <el-alert v-if="roleCode === 'JS05'" class="mb-12" show-icon type="info" :closable="false" title="供应商角色只展示当前账号提交的报价，不展示其他供应商价格。" />
            <el-table :data="quoteRanking" row-key="id" border empty-text="暂无报价">
              <el-table-column type="index" label="排名" width="70" />
              <el-table-column prop="supplierName" label="供应商" min-width="160" />
              <el-table-column prop="rfqNo" label="RFQ" min-width="150" />
              <el-table-column prop="quoteType" label="报价类型" width="110" />
              <el-table-column prop="unitPrice" label="单价" width="120" sortable>
                <template #default="{ row }">{{ formatMoney(row.unitPrice) }}</template>
              </el-table-column>
              <el-table-column prop="totalPrice" label="总价" width="140" sortable>
                <template #default="{ row }">{{ formatMoney(row.totalPrice) }}</template>
              </el-table-column>
              <el-table-column prop="deliveryDate" label="交付日期" width="150">
                <template #default="{ row }">{{ formatDateTime(row.deliveryDate) }}</template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="110">
                <template #default="{ row }">
                  <el-tag :type="statusType(row.status)" effect="plain">{{ statusLabel(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column v-if="canCreateAward" label="操作" fixed="right" width="150">
                <template #default="{ row, $index }">
                  <el-button :icon="TrendCharts" :disabled="pagination.quotes.page !== 1 || $index !== 0" link type="primary" @click="createAwardFromQuote(row)">
                    选择定点
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-bar">
              <el-pagination
                background
                layout="prev, pager, next, total"
                :current-page="pagination.quotes.page"
                :page-size="pagination.quotes.size"
                :total="pagination.quotes.total"
                @current-change="(page) => handleSourcingPageChange('quotes', page)"
              />
            </div>
          </el-tab-pane>

          <el-tab-pane v-if="roleCode !== 'JS06'" label="商务定点" name="awards">
            <el-table :data="visibleAwards" row-key="id" border empty-text="暂无商务定点单">
              <el-table-column prop="awardNo" label="定点单号" min-width="160" />
              <el-table-column prop="rfqNo" label="关联 RFQ" min-width="150" />
              <el-table-column prop="supplierName" label="意向供应商" min-width="170" />
              <el-table-column prop="analysisReport" label="比价依据" min-width="260" show-overflow-tooltip />
              <el-table-column prop="status" label="状态" width="110">
                <template #default="{ row }">
                  <el-tag :type="statusType(row.status)" effect="plain">{{ statusLabel(row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column v-if="canApproveAward" label="采购处长操作" fixed="right" width="190">
                <template #default="{ row }">
                  <el-button v-if="row.status !== 'APPROVED'" :icon="Check" link type="success" @click="approveAward(row)">审批通过</el-button>
                  <el-button v-if="row.status !== 'REJECTED'" link type="danger" @click="rejectAward(row)">驳回</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="pagination-bar">
              <el-pagination
                background
                layout="prev, pager, next, total"
                :current-page="pagination.awards.page"
                :page-size="pagination.awards.size"
                :total="pagination.awards.total"
                @current-change="(page) => handleSourcingPageChange('awards', page)"
              />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>

      <el-drawer v-model="demandDrawer" title="创建寻源需求" size="520px">
        <el-form label-position="top">
          <el-form-item label="采购品类">
            <el-input v-model="demandForm.categoryCode" placeholder="例如：DDR4 16GB 内存条" />
          </el-form-item>
          <el-form-item label="预估数量">
            <el-input-number v-model="demandForm.quantity" :min="1" class="wide-input" />
          </el-form-item>
          <el-form-item label="预算金额">
            <el-input-number v-model="demandForm.budget" :min="0" class="wide-input" />
          </el-form-item>
          <el-form-item label="规格要求">
            <el-input v-model="demandForm.specification" type="textarea" :rows="4" placeholder="填写技术规格、交付要求或质量标准" />
          </el-form-item>
          <el-form-item label="需求截止时间">
            <el-date-picker v-model="demandForm.deadline" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" class="wide-input" />
          </el-form-item>
          <div class="drawer-actions">
            <el-button @click="demandDrawer = false">取消</el-button>
            <el-button type="primary" @click="submitDemand">提交组长审核</el-button>
          </div>
        </el-form>
      </el-drawer>

      <el-drawer v-model="quoteDrawer" title="提交供应商报价" size="520px">
        <el-descriptions v-if="selectedRfq" :column="1" border class="mb-12">
          <el-descriptions-item label="RFQ">{{ selectedRfq.rfqNo }}</el-descriptions-item>
          <el-descriptions-item label="品类">{{ selectedRfq.categoryCode }}</el-descriptions-item>
          <el-descriptions-item label="数量">{{ selectedRfq.quantity }}</el-descriptions-item>
        </el-descriptions>
        <el-form label-position="top">
          <el-form-item label="报价类型">
            <el-segmented v-model="quoteForm.quoteType" :options="['L1', 'L2', 'L3']" />
          </el-form-item>
          <el-form-item label="单价">
            <el-input-number v-model="quoteForm.unitPrice" :min="0" :precision="2" class="wide-input" />
          </el-form-item>
          <el-form-item label="MOQ 最小起订量">
            <el-input-number v-model="quoteForm.moq" :min="1" class="wide-input" />
          </el-form-item>
          <el-form-item label="交付日期">
            <el-date-picker v-model="quoteForm.deliveryDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" class="wide-input" />
          </el-form-item>
          <el-form-item label="质保周期">
            <el-input v-model="quoteForm.warrantyPeriod" />
          </el-form-item>
          <el-form-item label="付款条件">
            <el-input v-model="quoteForm.paymentTerms" />
          </el-form-item>
          <el-alert :closable="false" type="success" show-icon>
            <template #title>报价总额：{{ formatMoney(quoteTotal) }}</template>
          </el-alert>
          <div class="drawer-actions">
            <el-button @click="quoteDrawer = false">取消</el-button>
            <el-button type="primary" @click="submitQuote">提交报价</el-button>
          </div>
        </el-form>
      </el-drawer>
    </div>
  </AppLayout>
</template>
