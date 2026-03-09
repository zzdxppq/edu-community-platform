<template>
  <!-- Placeholder: no banners or single banner fallback -->
  <div v-if="banners.length === 0" class="carousel-placeholder">
    <span class="carousel-placeholder-title">河南城乡学校共同体发展平台</span>
    <span class="carousel-placeholder-sub">推动城乡教育均衡发展，促进优质教育资源共享</span>
  </div>

  <!-- Carousel: 1+ banners -->
  <div
    v-else
    class="carousel"
    @mouseenter="onMouseEnter"
    @mouseleave="onMouseLeave"
  >
    <div class="carousel-viewport">
      <div
        class="carousel-track"
        :style="{ transform: `translateX(-${activeIndex * 100}%)` }"
      >
        <div
          v-for="(item, index) in banners"
          :key="item.id"
          class="carousel-slide"
          :style="{ cursor: item.linkUrl ? 'pointer' : 'default' }"
          @click="handleSlideClick(item)"
        >
          <img
            :src="item.imageUrl"
            :alt="item.title || `轮播图 ${index + 1}`"
            class="carousel-image"
          >
          <div v-if="item.title" class="carousel-caption">
            {{ item.title }}
          </div>
        </div>
      </div>
    </div>

    <!-- Arrows (hidden when only 1 banner) -->
    <template v-if="banners.length > 1">
      <button
        class="carousel-arrow carousel-arrow-left"
        aria-label="上一张"
        @click="prev"
      >&#9664;</button>
      <button
        class="carousel-arrow carousel-arrow-right"
        aria-label="下一张"
        @click="next"
      >&#9654;</button>

      <!-- Indicators -->
      <div class="carousel-indicators">
        <button
          v-for="(_, index) in banners"
          :key="index"
          class="carousel-indicator"
          :class="{ active: index === activeIndex }"
          :aria-label="`切换到第 ${index + 1} 张`"
          @click="goTo(index)"
        />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import type { BannerItem } from '~/types/banner'

const props = defineProps<{
  banners: BannerItem[]
}>()

const activeIndex = ref(0)
let timer: ReturnType<typeof setInterval> | null = null

function startTimer() {
  if (props.banners.length <= 1) return
  timer = setInterval(() => {
    activeIndex.value = (activeIndex.value + 1) % props.banners.length
  }, 5000)
}

function stopTimer() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

function resetTimer() {
  stopTimer()
  startTimer()
}

function next() {
  activeIndex.value = (activeIndex.value + 1) % props.banners.length
  resetTimer()
}

function prev() {
  activeIndex.value = (activeIndex.value - 1 + props.banners.length) % props.banners.length
  resetTimer()
}

function goTo(index: number) {
  activeIndex.value = index
  resetTimer()
}

function onMouseEnter() {
  stopTimer()
}

function onMouseLeave() {
  startTimer()
}

function handleSlideClick(item: BannerItem) {
  if (!item.linkUrl) return
  if (item.linkType === 1) {
    navigateTo(item.linkUrl)
  } else if (item.linkType === 2) {
    window.open(item.linkUrl, '_blank')
  }
}

onMounted(() => {
  startTimer()
})

onUnmounted(() => {
  stopTimer()
})
</script>

<style scoped>
/* Placeholder */
.carousel-placeholder {
  max-width: var(--container-max-width);
  margin: 0 auto;
  aspect-ratio: 1200 / 467;
  background: linear-gradient(135deg, var(--primary-800), var(--primary-900));
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-4);
}

.carousel-placeholder-title {
  color: var(--neutral-50);
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
}

.carousel-placeholder-sub {
  color: var(--neutral-400);
  font-size: var(--text-lg);
}

/* Carousel */
.carousel {
  max-width: var(--container-max-width);
  margin: 0 auto;
  position: relative;
  overflow: hidden;
}

.carousel-viewport {
  aspect-ratio: 1200 / 467;
  overflow: hidden;
}

.carousel-track {
  display: flex;
  height: 100%;
  transition: transform 0.5s ease;
}

.carousel-slide {
  min-width: 100%;
  position: relative;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

/* Caption */
.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: var(--neutral-50);
  padding: var(--spacing-4) var(--spacing-6);
  font-size: var(--text-lg);
}

/* Arrows */
.carousel-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  background: rgba(0, 0, 0, 0.3);
  color: var(--neutral-50);
  border: none;
  cursor: pointer;
  font-size: var(--text-base);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--transition-normal), background var(--transition-normal);
  z-index: 2;
}

.carousel:hover .carousel-arrow {
  opacity: 1;
}

.carousel-arrow:hover {
  background: rgba(0, 0, 0, 0.5);
}

.carousel-arrow-left {
  left: 16px;
}

.carousel-arrow-right {
  right: 16px;
}

/* Indicators */
.carousel-indicators {
  position: absolute;
  bottom: 60px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: var(--spacing-2);
  z-index: 2;
}

.carousel-indicator {
  width: 10px;
  height: 10px;
  border-radius: var(--radius-full);
  background: rgba(255, 255, 255, 0.5);
  border: none;
  cursor: pointer;
  padding: 0;
  transition: background var(--transition-normal);
}

.carousel-indicator.active {
  background: #ffffff;
}
</style>
