<script setup>
defineProps({
  stat: {
    type: Object,
    required: true,
  },
})
</script>

<template>
  <section class="stat-card" :class="`stat-${stat.type}`">
    <div>
      <p>{{ stat.label }}</p>
      <strong>{{ stat.value }}<span>{{ stat.unit }}</span></strong>
    </div>
    <el-tag effect="plain">{{ stat.trend }}</el-tag>
  </section>
</template>
