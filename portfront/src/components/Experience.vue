<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const experiences = ref([])

onMounted(async () => {
  try {
    const res = await axios.get('/api/experiences')
    experiences.value = res.data
  } catch {
    // keep empty
  }
})
</script>

<template>
  <section v-if="experiences.length > 0" class="experience">
    <div class="container">
      <div class="section-header fade-in">
        <h2 class="section-title">Experience</h2>
      </div>

      <div class="timeline">
        <div
          v-for="(exp, i) in experiences"
          :key="exp.id || i"
          class="timeline-item"
        >
          <div class="timeline-dot" :class="{ current: exp.current }"></div>

          <div class="timeline-card">
            <div class="card-top">
              <div class="card-left">
                <div class="company-row">
                  <h3 class="company">{{ exp.company }}</h3>
                  <span v-if="exp.current" class="current-badge">재직중</span>
                </div>
                <div class="role-row">
                  <span class="role">{{ exp.role }}</span>
                  <span class="separator">·</span>
                  <span class="type">{{ exp.type }}</span>
                </div>
              </div>
              <div class="card-right">
                <span class="period">{{ exp.period }}</span>
                <span class="duration">{{ exp.duration }}</span>
              </div>
            </div>

            <p class="card-desc">{{ exp.description }}</p>

            <div class="card-tags">
              <span v-for="tag in exp.tags" :key="tag" class="tech-tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.experience {
  padding: 3.5rem 0;
}

.section-header {
  margin-bottom: 3.5rem;
}

.section-title {
  font-size: 2.8rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, var(--text-color), var(--text-secondary));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* ── 타임라인 ── */
.timeline {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 0;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 7px;
  top: 12px;
  bottom: 12px;
  width: 2px;
  background: var(--border-color);
}

.timeline-item {
  position: relative;
  padding-left: 3rem;
  padding-bottom: 2.5rem;
}

.timeline-item:last-child {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: 0;
  top: 12px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: var(--border-color);
  border: 3px solid var(--bg-color);
  z-index: 1;
}

.timeline-dot.current {
  background: var(--accent-color);
  box-shadow: 0 0 0 4px rgba(96, 165, 250, 0.15);
}

/* ── 카드 ── */
.timeline-card {
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 14px;
  padding: 1.75rem;
  transition: border-color 0.2s, transform 0.2s;
}

.timeline-card:hover {
  border-color: var(--accent-color);
  transform: translateX(4px);
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  margin-bottom: 1rem;
}

.company-row {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.3rem;
}

.company {
  font-size: 1.1rem;
  font-weight: 700;
  color: var(--text-color);
}

.current-badge {
  font-size: 0.7rem;
  font-weight: 700;
  padding: 0.2rem 0.6rem;
  border-radius: 999px;
  background: rgba(96, 165, 250, 0.12);
  color: var(--accent-color);
  border: 1px solid rgba(96, 165, 250, 0.3);
}

.role-row {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.88rem;
}

.role { color: var(--text-color); font-weight: 600; }
.separator { color: var(--border-color); }
.type { color: var(--text-secondary); }

.card-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.2rem;
  flex-shrink: 0;
}

.period { font-size: 0.85rem; font-weight: 600; color: var(--accent-color); }
.duration { font-size: 0.78rem; color: var(--text-secondary); }

.card-desc {
  font-size: 0.9rem;
  color: var(--text-secondary);
  line-height: 1.7;
  margin-bottom: 1.25rem;
  white-space: pre-line;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tech-tag {
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.25rem 0.7rem;
  border-radius: 6px;
  background: rgba(96, 165, 250, 0.08);
  color: var(--accent-color);
  border: 1px solid rgba(96, 165, 250, 0.2);
}

@media (max-width: 640px) {
  .card-top { flex-direction: column; gap: 0.5rem; }
  .card-right { align-items: flex-start; flex-direction: row; gap: 0.75rem; }
  .section-title { font-size: 2.2rem; }
}
</style>
