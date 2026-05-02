<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

defineProps({
  screen: { type: Object, required: true }
})

const lightboxOpen = ref(false)

const openLightbox = () => { lightboxOpen.value = true }
const closeLightbox = () => { lightboxOpen.value = false }

const onKeydown = (e) => {
  if (e.key === 'Escape') closeLightbox()
}

onMounted(() => window.addEventListener('keydown', onKeydown))
onUnmounted(() => window.removeEventListener('keydown', onKeydown))
</script>

<template>
  <div class="screen-viewer">
    <div class="image-area">
      <img
        v-if="screen.image"
        :src="screen.image"
        :alt="screen.name"
        class="screen-image"
        @click="openLightbox"
        @error="e => e.target.style.display='none'"
      />
      <div v-else class="image-empty">
        <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
          <rect x="2" y="3" width="20" height="14" rx="2"/>
          <polyline points="8,21 16,21"/>
          <line x1="12" y1="17" x2="12" y2="21"/>
        </svg>
        <span>이미지 없음</span>
      </div>

      <!-- 확대 힌트 -->
      <div v-if="screen.image" class="zoom-hint">
        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
          <line x1="11" y1="8" x2="11" y2="14"/><line x1="8" y1="11" x2="14" y2="11"/>
        </svg>
        클릭하여 크게 보기
      </div>
    </div>

    <!-- 라이트박스 -->
    <Teleport to="body">
      <div v-if="lightboxOpen" class="lightbox" @click.self="closeLightbox">
        <button class="lightbox-close" @click="closeLightbox">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </button>
        <img :src="screen.image" :alt="screen.name" class="lightbox-image" />
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.screen-viewer {
  flex: 1;
  background: #060e1a;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  min-width: 0;
}

.image-area {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.5rem;
  position: relative;
}

.screen-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 8px 48px rgba(0, 0, 0, 0.6);
  cursor: zoom-in;
  transition: transform 0.2s, box-shadow 0.2s;
}

.screen-image:hover {
  transform: scale(1.01);
  box-shadow: 0 12px 60px rgba(0, 0, 0, 0.7);
}

/* 확대 힌트 */
.zoom-hint {
  position: absolute;
  bottom: 1.25rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 0.35rem;
  font-size: 0.72rem;
  color: rgba(255, 255, 255, 0.3);
  pointer-events: none;
  transition: opacity 0.2s;
}

.image-area:hover .zoom-hint {
  color: rgba(255, 255, 255, 0.55);
}

.image-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  color: #1e3a5f;
}

.image-empty span {
  font-size: 0.85rem;
  color: #1e3a5f;
}

/* 라이트박스 */
.lightbox {
  position: fixed;
  inset: 0;
  z-index: 9999;
  background: rgba(0, 0, 0, 0.92);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  animation: lb-in 0.18s ease;
  cursor: zoom-out;
}

@keyframes lb-in {
  from { opacity: 0; }
  to   { opacity: 1; }
}

.lightbox-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 6px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.8);
  animation: lb-scale 0.18s ease;
  cursor: default;
}

@keyframes lb-scale {
  from { transform: scale(0.95); }
  to   { transform: scale(1); }
}

.lightbox-close {
  position: absolute;
  top: 1.25rem;
  right: 1.25rem;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
}

.lightbox-close:hover {
  background: rgba(255, 255, 255, 0.2);
}
</style>
