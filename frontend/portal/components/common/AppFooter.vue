<template>
  <footer class="footer">
    <div class="footer-container">
      <div class="footer-links">
        <span class="footer-links-label">友情链接：</span>
        <a
          v-for="(link, index) in friendLinks"
          :key="link.url"
          :href="link.url"
          target="_blank"
          rel="noopener noreferrer"
          class="footer-link"
        >
          {{ link.name }}
          <span v-if="index < friendLinks.length - 1" class="footer-link-sep">|</span>
        </a>
      </div>
      <div class="footer-copyright">
        {{ siteConfig.copyright }}
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
const { siteConfig, friendLinks } = useSiteConfig()
</script>

<style scoped>
.footer {
  background: var(--primary-800);
  color: var(--neutral-50);
  height: var(--header-height);
  display: flex;
  align-items: center;
}

.footer-container {
  max-width: var(--container-max-width);
  margin: 0 auto;
  padding: 0 var(--spacing-5);
  width: 100%;
  text-align: center;
}

.footer-links {
  font-size: var(--text-sm);
  margin-bottom: var(--spacing-1);
}

.footer-links-label {
  opacity: 0.8;
}

.footer-link {
  color: var(--neutral-50);
  text-decoration: none;
  transition: opacity var(--transition-fast);
}

.footer-link:hover {
  opacity: 0.8;
  text-decoration: underline;
}

.footer-link-sep {
  margin: 0 var(--spacing-2);
  opacity: 0.5;
}

.footer-copyright {
  font-size: var(--text-xs);
  opacity: 0.7;
}
</style>
