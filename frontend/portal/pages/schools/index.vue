<template>
  <div class="page-schools">
    <div class="container">
      <h1>示范校共体</h1>
      <p>城乡学校共同体示范校列表</p>
    </div>
  </div>
</template>

<script setup lang="ts">
useHead({
  title: '示范校共体 - 河南城乡学校共同体发展平台',
  meta: [
    { name: 'description', content: '浏览城乡学校共同体示范校名单与详细信息。' },
  ],
})
</script>

<style scoped>
.page-schools {
  padding: var(--spacing-10) 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>
