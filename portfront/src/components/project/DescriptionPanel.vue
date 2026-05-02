<script setup>
defineProps({
  screen: { type: Object, required: true },
  screenIndex: { type: Number, required: true },
  total: { type: Number, required: true }
})
</script>

<template>
  <aside class="desc-panel">
    <!-- 화면 번호 -->
    <div class="panel-counter">
      <span class="counter-current">{{ String(screenIndex + 1).padStart(2, '0') }}</span>
      <span class="counter-sep">/</span>
      <span class="counter-total">{{ String(total).padStart(2, '0') }}</span>
    </div>

    <!-- 화면 이름 -->
    <h3 class="screen-name">{{ screen.name || '화면 ' + (screenIndex + 1) }}</h3>

    <!-- 설명 -->
    <p v-if="screen.description" class="screen-desc">{{ screen.description }}</p>

    <!-- 구현 기능 -->
    <div v-if="screen.features && screen.features.length" class="panel-section">
      <h4 class="panel-section-label">구현 기능</h4>
      <ul class="panel-list">
        <li v-for="item in screen.features" :key="item">{{ item }}</li>
      </ul>
    </div>

    <!-- 담당 역할 -->
    <div v-if="screen.roles && screen.roles.length" class="panel-section">
      <h4 class="panel-section-label">담당 역할</h4>
      <ul class="panel-list">
        <li v-for="item in screen.roles" :key="item">{{ item }}</li>
      </ul>
    </div>

    <!-- 사용 기술 -->
    <div v-if="screen.techs && screen.techs.length" class="panel-section">
      <h4 class="panel-section-label">사용 기술</h4>
      <div class="tech-chips">
        <span v-for="tech in screen.techs" :key="tech" class="tech-chip">{{ tech }}</span>
      </div>
    </div>

    <!-- 특이사항 -->
    <div v-if="screen.highlights && screen.highlights.length" class="panel-section">
      <h4 class="panel-section-label">특이사항</h4>
      <ul class="panel-list highlight-list">
        <li v-for="item in screen.highlights" :key="item">{{ item }}</li>
      </ul>
    </div>
  </aside>
</template>

<style scoped>
.desc-panel {
  width: 280px;
  flex-shrink: 0;
  background: #0d1b2e;
  border-left: 1px solid #1e2d4a;
  padding: 1.75rem 1.5rem;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.panel-counter {
  display: flex;
  align-items: baseline;
  gap: 0.3rem;
}

.counter-current {
  font-size: 2rem;
  font-weight: 800;
  color: var(--accent-color, #60a5fa);
  line-height: 1;
}

.counter-sep {
  font-size: 1rem;
  color: #334155;
}

.counter-total {
  font-size: 1rem;
  font-weight: 600;
  color: #334155;
}

.screen-name {
  font-size: 1.1rem;
  font-weight: 800;
  color: #f1f5f9;
  line-height: 1.4;
  margin: 0;
}

.screen-desc {
  font-size: 0.85rem;
  color: #94a3b8;
  line-height: 1.75;
  white-space: pre-line;
  margin: 0;
}

.panel-section {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.panel-section-label {
  font-size: 0.62rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: #475569;
  margin: 0;
  padding-bottom: 0.4rem;
  border-bottom: 1px solid #1e2d4a;
}

.panel-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.45rem;
}

.panel-list li {
  font-size: 0.82rem;
  color: #94a3b8;
  padding-left: 1rem;
  position: relative;
  line-height: 1.55;
}

.panel-list li::before {
  content: '·';
  position: absolute;
  left: 0;
  color: var(--accent-color, #60a5fa);
  font-weight: 700;
}

.highlight-list li {
  color: #fbbf24;
}

.highlight-list li::before {
  color: #fbbf24;
}

.tech-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.35rem;
}

.tech-chip {
  font-size: 0.7rem;
  font-weight: 600;
  padding: 0.2rem 0.55rem;
  border-radius: 5px;
  background: rgba(96, 165, 250, 0.08);
  color: var(--accent-color, #60a5fa);
  border: 1px solid rgba(96, 165, 250, 0.2);
}
</style>
