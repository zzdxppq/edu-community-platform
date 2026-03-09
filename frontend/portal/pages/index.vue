<template>
  <div class="page-home">
    <HomeCarousel :banners="banners" />
  </div>
</template>

<script setup lang="ts">
useHead({
  title: '首页 - 河南城乡学校共同体发展平台',
})

const { banners } = useBanners()
</script>

<style scoped>
.page-home {
  padding: 0;
}
</style>
