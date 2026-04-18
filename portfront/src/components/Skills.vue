<script setup>
import { ref, onMounted } from 'vue'
// API로 로드한 요소에는 fade-in을 쓰지 않음 (PortfolioView observer가 먼저 실행되어 잡지 못함)
import axios from 'axios'

const skillGroups = ref([])

onMounted(async () => {
  try {
    const res = await axios.get('/api/skills')
    skillGroups.value = res.data
  } catch {
    // fallback: keep empty
  }
})
</script>

<template>
  <section class="skills">
    <div class="container">
      <div class="section-header fade-in">
        <h2 class="section-title">Technical Skills</h2>
      </div>

      <div class="skills-grid">
        <div
          v-for="group in skillGroups"
          :key="group.id"
          class="skill-card"
        >
          <div class="card-header">
            <span class="card-icon">{{ group.icon }}</span>
            <h3 class="card-name" :style="{ color: group.color }">{{ group.name }}</h3>
          </div>
          <ul class="skill-list">
            <li v-for="skill in group.skills" :key="skill" class="skill-item">
              <span class="skill-dot" :style="{ background: group.color }"></span>
              {{ skill }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.skills {
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

.section-sub {
  color: var(--text-secondary);
  font-size: 1rem;
}

.skills-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 1.5rem;
}

.skill-card {
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  padding: 2rem;
  transition: transform 0.25s, border-color 0.25s;
}

.skill-card:hover {
  transform: translateY(-4px);
  border-color: var(--accent-color);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.card-icon {
  font-size: 1.5rem;
}

.card-name {
  font-size: 1.1rem;
  font-weight: 700;
}

.skill-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.skill-item {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  font-size: 0.9rem;
  color: var(--text-color);
  font-weight: 500;
}

.skill-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  flex-shrink: 0;
  opacity: 0.8;
}

@media (max-width: 640px) {
  .section-title { font-size: 2.2rem; }
  .skills-grid { grid-template-columns: 1fr 1fr; gap: 1rem; }
  .skill-card { padding: 1.5rem; }
}

@media (max-width: 480px) {
  .skills-grid { grid-template-columns: 1fr; }
}
</style>
