<template>
  <div class="error-page">
    <div class="error-container">
      <h1 class="error-code">{{ error?.statusCode || 500 }}</h1>
      <p class="error-message">
        {{ error?.statusCode === 404 ? '页面不存在' : '服务器错误' }}
      </p>
      <p class="error-description">
        {{ error?.statusCode === 404
          ? '您访问的页面不存在，请检查地址是否正确。'
          : '服务器遇到了一个错误，请稍后重试。'
        }}
      </p>
      <button class="error-button" @click="handleError">返回首页</button>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  error: {
    statusCode: number
    message: string
  }
}>()

const handleError = () => clearError({ redirect: '/' })
</script>

<style scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--neutral-200);
}

.error-container {
  text-align: center;
  padding: var(--spacing-10);
}

.error-code {
  font-size: 72px;
  font-weight: var(--font-bold);
  color: var(--primary-800);
  margin-bottom: var(--spacing-4);
}

.error-message {
  font-size: var(--text-3xl);
  color: var(--neutral-900);
  margin-bottom: var(--spacing-4);
}

.error-description {
  font-size: var(--text-lg);
  color: var(--neutral-600);
  margin-bottom: var(--spacing-8);
}

.error-button {
  background: var(--primary-800);
  color: var(--neutral-50);
  border: none;
  padding: var(--spacing-3) var(--spacing-8);
  font-size: var(--text-lg);
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.error-button:hover {
  background: var(--primary-900);
}
</style>
