<script setup lang="ts">
/**
 * RichTextViewer - Story 4.3
 * Read-only renderer for WangEditor HTML content.
 * Used to display rich-text content in detail/preview pages.
 */
import '@wangeditor/editor/dist/css/style.css'
import { DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE, DEFAULT_LINE_HEIGHT } from './RichTextEditor.vue'

defineProps<{
  content: string
}>()
</script>

<template>
  <div
    class="rich-text-viewer w-e-text-container"
    data-w-e-textarea="true"
  >
    <div class="rich-text-viewer__body" v-html="content" />
  </div>
</template>

<style scoped>
.rich-text-viewer {
  font-family: v-bind("DEFAULT_FONT_FAMILY"), 'Microsoft YaHei', sans-serif;
  font-size: v-bind("DEFAULT_FONT_SIZE");
  line-height: v-bind("DEFAULT_LINE_HEIGHT");
  word-break: break-word;
  overflow-wrap: break-word;
}

.rich-text-viewer__body :deep(img) {
  max-width: 100%;
  height: auto;
}

.rich-text-viewer__body :deep(table) {
  border-collapse: collapse;
  width: 100%;
}

.rich-text-viewer__body :deep(td),
.rich-text-viewer__body :deep(th) {
  border: 1px solid #dcdfe6;
  padding: 8px;
}

.rich-text-viewer__body :deep(blockquote) {
  border-left: 4px solid #dcdfe6;
  padding-left: 12px;
  margin: 12px 0;
  color: #606266;
}

.rich-text-viewer__body :deep(a) {
  color: #1283e9;
  text-decoration: none;
}

.rich-text-viewer__body :deep(a:hover) {
  text-decoration: underline;
}
</style>
