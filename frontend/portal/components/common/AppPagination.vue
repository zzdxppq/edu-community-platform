<template>
  <div v-if="total > 0" class="pagination">
    <span class="pagination-total">共 {{ total }} 条记录</span>

    <div class="pagination-controls">
      <button
        class="pagination-btn"
        :disabled="currentPage <= 1"
        :title="'首页'"
        @click="goTo(1)"
      >
        «
      </button>
      <button
        class="pagination-btn"
        :disabled="currentPage <= 1"
        :title="'上一页'"
        @click="goTo(currentPage - 1)"
      >
        ‹
      </button>

      <template v-for="page in pageList" :key="page">
        <span v-if="page === '...'" class="pagination-ellipsis">…</span>
        <button
          v-else
          class="pagination-btn"
          :class="{ 'pagination-btn--active': page === currentPage }"
          @click="goTo(page as number)"
        >
          {{ page }}
        </button>
      </template>

      <button
        class="pagination-btn"
        :disabled="currentPage >= totalPages"
        :title="'下一页'"
        @click="goTo(currentPage + 1)"
      >
        ›
      </button>
      <button
        class="pagination-btn"
        :disabled="currentPage >= totalPages"
        :title="'末页'"
        @click="goTo(totalPages)"
      >
        »
      </button>
    </div>

    <div class="pagination-size">
      <select
        :value="pageSize"
        class="pagination-select"
        @change="changeSize(Number(($event.target as HTMLSelectElement).value))"
      >
        <option v-for="size in pageSizes" :key="size" :value="size">
          {{ size }} 条/页
        </option>
      </select>
    </div>

    <div class="pagination-jump">
      <span>跳至</span>
      <input
        v-model.number="jumpPage"
        class="pagination-jump-input"
        type="number"
        :min="1"
        :max="totalPages"
        @keyup.enter="handleJump"
      />
      <span>页</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

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

const jumpPage = ref<number | undefined>(undefined)

const totalPages = computed(() => Math.max(1, Math.ceil(props.total / props.pageSize)))

const pageList = computed(() => {
  const pages: (number | string)[] = []
  const current = props.currentPage
  const last = totalPages.value

  if (last <= 7) {
    for (let i = 1; i <= last; i++) pages.push(i)
    return pages
  }

  pages.push(1)
  if (current > 4) pages.push('...')

  const start = Math.max(2, current - 2)
  const end = Math.min(last - 1, current + 2)
  for (let i = start; i <= end; i++) pages.push(i)

  if (current < last - 3) pages.push('...')
  pages.push(last)

  return pages
})

function goTo(page: number) {
  const clamped = Math.max(1, Math.min(page, totalPages.value))
  if (clamped !== props.currentPage) {
    emit('update:currentPage', clamped)
  }
}

function changeSize(size: number) {
  emit('update:pageSize', size)
  emit('update:currentPage', 1)
}

function handleJump() {
  if (jumpPage.value != null && jumpPage.value >= 1 && jumpPage.value <= totalPages.value) {
    goTo(jumpPage.value)
    jumpPage.value = undefined
  }
}
</script>

<style scoped>
.pagination {
  display: flex;
  align-items: center;
  gap: var(--spacing-4);
  font-size: var(--text-sm);
  color: var(--neutral-700);
  flex-wrap: wrap;
}

.pagination-total {
  white-space: nowrap;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
}

.pagination-btn {
  min-width: 32px;
  height: 32px;
  padding: 0 var(--spacing-1);
  border: 1px solid var(--neutral-300);
  border-radius: var(--radius-md);
  background: var(--neutral-50);
  color: var(--neutral-700);
  cursor: pointer;
  font-size: var(--text-sm);
  line-height: 1;
  transition: all var(--transition-fast);
}

.pagination-btn:hover:not(:disabled):not(.pagination-btn--active) {
  color: var(--accent-600);
  border-color: var(--accent-600);
}

.pagination-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.pagination-btn--active {
  background: var(--accent-600);
  border-color: var(--accent-600);
  color: var(--neutral-50);
}

.pagination-ellipsis {
  min-width: 32px;
  text-align: center;
  color: var(--neutral-500);
}

.pagination-size {
  display: flex;
  align-items: center;
}

.pagination-select {
  height: 32px;
  padding: 0 var(--spacing-2);
  border: 1px solid var(--neutral-300);
  border-radius: var(--radius-md);
  background: var(--neutral-50);
  color: var(--neutral-700);
  font-size: var(--text-sm);
  cursor: pointer;
  outline: none;
}

.pagination-select:focus {
  border-color: var(--accent-600);
}

.pagination-jump {
  display: flex;
  align-items: center;
  gap: var(--spacing-1);
  white-space: nowrap;
}

.pagination-jump-input {
  width: 50px;
  height: 32px;
  padding: 0 var(--spacing-1);
  border: 1px solid var(--neutral-300);
  border-radius: var(--radius-md);
  text-align: center;
  font-size: var(--text-sm);
  outline: none;
  -moz-appearance: textfield;
}

.pagination-jump-input::-webkit-inner-spin-button,
.pagination-jump-input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.pagination-jump-input:focus {
  border-color: var(--accent-600);
}
</style>
