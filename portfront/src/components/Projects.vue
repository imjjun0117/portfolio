<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const projects = ref([])
const isLoading = ref(true)

onMounted(async () => {
  try {
    const res = await axios.get('/api/projects')
    projects.value = res.data
  } catch (e) {
    console.error('Failed to fetch projects:', e)
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <section class="projects">
    <div class="container">
      <div class="section-header fade-in">
        <h2 class="section-title">Projects</h2>
      </div>

      <div v-if="isLoading" class="loading">불러오는 중...</div>

      <div v-else class="card-grid">
        <div
          v-for="(project, i) in projects"
          :key="project.id"
          class="project-card"
          style="cursor:pointer"
          @click="router.push(`/projects/${project.id}`)"
        >
          <!-- 대표 이미지 -->
          <div class="card-thumb">
            <img
              v-if="project.thumbnail"
              :src="project.thumbnail"
              :alt="project.title"
              @error="e => e.target.style.display='none'"
            />
            <div class="thumb-default">
              <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.2">
                <rect x="3" y="3" width="18" height="18" rx="2"/>
                <circle cx="8.5" cy="8.5" r="1.5"/>
                <polyline points="21,15 16,10 5,21"/>
              </svg>
              <span>{{ project.title }}</span>
            </div>
          </div>

          <!-- 바이브코딩 뱃지 -->
          <div v-if="project.vibeCoding" class="vibe-badge">⚡ Vibe Coding</div>

          <!-- 상단: 제목 + 기간 + GitHub -->
          <div class="card-header">
            <div class="card-num">0{{ i + 1 }}</div>
            <div class="card-header-text">
              <div class="card-title-row">
                <h3 class="card-title">{{ project.title }}</h3>
                <a v-if="project.github" :href="project.github" target="_blank" class="github-btn" title="GitHub">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
                  </svg>
                </a>
              </div>
              <span class="card-period">{{ project.period }}</span>
            </div>
          </div>

          <!-- 설명 -->
          <p class="card-desc">{{ project.description }}</p>

          <!-- 기술 스택 -->
          <div class="tag-row">
            <span v-for="skill in project.skills" :key="skill" class="tag">{{ skill }}</span>
          </div>

          <!-- 자세히 보기 -->
          <div class="detail-hint">자세히 보기 →</div>

          <!-- 담당 역할 -->
          <div v-if="project.roles && project.roles.length" class="role-block">
            <ul class="role-list">
              <li v-for="role in project.roles" :key="role">{{ role }}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.projects { padding: 3.5rem 0; }

.section-header { margin-bottom: 3rem; }

.section-title {
  font-size: 2.8rem;
  font-weight: 800;
  background: linear-gradient(135deg, var(--text-color), var(--text-secondary));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.loading { text-align: center; padding: 4rem; color: var(--text-secondary); }

/* ── 그리드 ── */
.card-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
  align-items: start;
}

/* ── 썸네일 ── */
.card-thumb {
  position: relative;
  height: 160px;
  overflow: hidden;
  border-radius: 10px;
  margin-bottom: 1rem;
  background: var(--bg-color);
}

.card-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
  position: relative;
  z-index: 1;
}

.thumb-default {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.6rem;
  background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  color: #334155;
}

.thumb-default svg {
  opacity: 0.4;
}

.thumb-default span {
  font-size: 0.75rem;
  font-weight: 600;
  color: #475569;
  text-align: center;
  padding: 0 1rem;
  line-height: 1.4;
}

.project-card:hover .card-thumb img {
  transform: scale(1.04);
}

/* ── 카드 ── */
.project-card {
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 14px;
  padding: 1.5rem;
  transition: border-color 0.2s, transform 0.2s;
}

.project-card:hover {
  border-color: var(--accent-color);
  transform: translateY(-3px);
}

/* ── 헤더 ── */
.card-header {
  display: flex;
  gap: 0.75rem;
  align-items: flex-start;
  margin-bottom: 0.9rem;
}

.card-num {
  font-size: 0.68rem;
  font-weight: 800;
  color: var(--accent-color);
  opacity: 0.45;
  padding-top: 0.25rem;
  letter-spacing: 0.04em;
  flex-shrink: 0;
}

.card-header-text { flex: 1; min-width: 0; }

.card-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.5rem;
  margin-bottom: 0.2rem;
}

.card-title {
  font-size: 0.97rem;
  font-weight: 800;
  color: var(--text-color);
  line-height: 1.35;
}

.github-btn {
  display: flex;
  align-items: center;
  color: var(--text-secondary);
  padding: 0.25rem;
  border-radius: 5px;
  transition: color 0.2s;
  flex-shrink: 0;
}

.github-btn:hover { color: var(--accent-color); }

.card-period {
  font-size: 0.73rem;
  color: var(--accent-color);
  font-weight: 600;
}

/* ── 설명 ── */
.card-desc {
  font-size: 0.82rem;
  color: var(--text-secondary);
  line-height: 1.65;
  margin-bottom: 0.85rem;
}

/* ── 태그 ── */
.tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.35rem;
  margin-bottom: 0;
}

.tag {
  font-size: 0.68rem;
  font-weight: 600;
  padding: 0.18rem 0.55rem;
  border-radius: 5px;
  background: rgba(96, 165, 250, 0.08);
  color: var(--accent-color);
  border: 1px solid rgba(96, 165, 250, 0.18);
}

.vibe-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
  font-size: 0.65rem;
  font-weight: 700;
  padding: 0.18rem 0.6rem;
  border-radius: 999px;
  background: rgba(139, 92, 246, 0.12);
  border: 1px solid rgba(139, 92, 246, 0.3);
  color: #a78bfa;
  margin-bottom: 0.6rem;
  letter-spacing: 0.03em;
}

.detail-hint {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--accent-color);
  opacity: 0;
  margin-top: 0.6rem;
  transition: opacity 0.2s;
}

.project-card:hover .detail-hint {
  opacity: 1;
}

.role-block {
  margin-top: 0.75rem;
  padding-top: 0.75rem;
  border-top: 1px solid var(--border-color);
}

.role-list { display: flex; flex-direction: column; gap: 0.3rem; }
.role-list li {
  font-size: 0.78rem;
  color: var(--text-secondary);
  padding-left: 0.85rem;
  position: relative;
  line-height: 1.5;
}
.role-list li::before {
  content: '·';
  position: absolute;
  left: 0;
  color: var(--accent-color);
  font-weight: 700;
}

/* ── 반응형 ── */
@media (max-width: 1024px) {
  .card-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 640px) {
  .card-grid { grid-template-columns: 1fr; }
  .section-title { font-size: 2.2rem; }
}
</style>
