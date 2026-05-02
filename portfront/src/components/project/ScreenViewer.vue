<script setup>
defineProps({
  screen: { type: Object, required: true }
})
</script>

<template>
  <div class="screen-viewer">
    <div class="image-area">
      <img
        v-if="screen.image"
        :src="screen.image"
        :alt="screen.name"
        class="screen-image"
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
    </div>
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
}

.screen-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 8px;
  box-shadow: 0 8px 48px rgba(0, 0, 0, 0.6);
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
</style>
