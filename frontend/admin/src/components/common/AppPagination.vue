<template>
  <div v-if="total > 0" class="app-pagination">
    <el-pagination
      v-model:current-page="internalCurrentPage"
      v-model:page-size="internalPageSize"
      :page-sizes="pageSizes"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      background
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  total: number
  currentPage?: number
  pageSize?: number
  pageSizes?: number[]
}>(), {
  currentPage: 1,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
})

const emit = defineEmits<{
  'update:currentPage': [page: number]
  'update:pageSize': [size: number]
}>()

const internalCurrentPage = computed({
  get: () => props.currentPage,
  set: (val: number) => emit('update:currentPage', val),
})

const internalPageSize = computed({
  get: () => props.pageSize,
  set: (val: number) => {
    emit('update:pageSize', val)
    emit('update:currentPage', 1)
  },
})
</script>

<style scoped>
.app-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0;
}
</style>
