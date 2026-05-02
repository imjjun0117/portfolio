<script setup>
defineProps({
  screens: { type: Array, required: true },
  selectedIndex: { type: Number, required: true }
})
defineEmits(['select'])
</script>

<template>
  <aside class="screen-list">
    <div class="screen-list-label">화면 목록</div>
    <ul class="screen-items">
      <li
        v-for="(screen, i) in screens"
        :key="screen.id ?? i"
        :class="['screen-item', { active: selectedIndex === i }]"
        @click="$emit('select', i)"
      >
        <div class="screen-thumb">
          <img v-if="screen.image" :src="screen.image" :alt="screen.name"
            @error="e => e.target.style.display='none'" />
          <div class="screen-thumb-default">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <rect x="2" y="3" width="20" height="14" rx="2"/>
              <polyline points="8,21 16,21"/>
              <line x1="12" y1="17" x2="12" y2="21"/>
            </svg>
          </div>
        </div>
        <div class="screen-item-info">
          <span class="screen-item-num">{{ String(i + 1).padStart(2, '0') }}</span>
          <span class="screen-item-name">{{ screen.name || '화면 ' + (i + 1) }}</span>
        </div>
      </li>
    </ul>
  </aside>
</template>

<style scoped>
.screen-list {
  width: 220px;
  flex-shrink: 0;
  background: #0a1628;
  border-right: 1px solid #1e2d4a;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.screen-list-label {
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: #475569;
  padding: 1.25rem 1.25rem 0.75rem;
  border-bottom: 1px solid #1e2d4a;
}

.screen-items {
  list-style: none;
  padding: 0.5rem 0;
  margin: 0;
  flex: 1;
}

.screen-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.65rem 1rem;
  cursor: pointer;
  border-left: 2px solid transparent;
  transition: background 0.15s, border-color 0.15s;
}

.screen-item:hover {
  background: rgba(96, 165, 250, 0.05);
}

.screen-item.active {
  border-left-color: var(--accent-color, #60a5fa);
  background: rgba(96, 165, 250, 0.08);
}

.screen-thumb {
  position: relative;
  width: 52px;
  height: 36px;
  border-radius: 5px;
  overflow: hidden;
  background: #1e293b;
  flex-shrink: 0;
}

.screen-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.screen-thumb-default {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #334155;
}

.screen-item-info {
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
  min-width: 0;
}

.screen-item-num {
  font-size: 0.6rem;
  font-weight: 700;
  color: var(--accent-color, #60a5fa);
  opacity: 0.6;
}

.screen-item-name {
  font-size: 0.78rem;
  font-weight: 600;
  color: #94a3b8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.screen-item.active .screen-item-name {
  color: #f1f5f9;
}
</style>
