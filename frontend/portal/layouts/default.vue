<template>
  <div class="layout-default">
    <AppHeader />

    <main class="main-content">
      <slot />
    </main>

    <AppFooter />
  </div>
</template>

<style scoped>
.layout-default {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  margin-top: var(--header-height);
  min-height: calc(100vh - var(--header-height) - var(--header-height));
}
</style>
