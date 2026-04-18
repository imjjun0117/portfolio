<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const config = ref({
  'hero.badge': '풀스택 개발자',
  'hero.name': '황성준',
  'hero.description': '웹 에이전시부터 SI까지, 다양한 도메인의 실무 프로젝트를 경험한\n3년차 풀스택 개발자입니다.',
  'hero.stat1.value': '3+',
  'hero.stat1.label': '년 경력',
  'hero.stat2.value': '2',
  'hero.stat2.label': '개 기업',
  'hero.stat3.value': 'Full',
  'hero.stat3.label': 'Stack',
  'hero.githubUrl': 'https://github.com/imjjun0117',
  'hero.email': 'hsungjun0117@gmail.com',
})

onMounted(async () => {
  try {
    const res = await axios.get('/api/config')
    config.value = { ...config.value, ...res.data }
  } catch {
    // keep defaults
  }
})
</script>

<template>
  <section class="hero">
    <div class="container hero-inner">
      <div class="hero-content">
        <div class="badge">{{ config['hero.badge'] }}</div>
        <h1 class="title">
          안녕하세요,<br />
          <span class="highlight">{{ config['hero.name'] }}</span>입니다.
        </h1>
        <p class="description" style="white-space: pre-line">{{ config['hero.description'] }}</p>

        <div class="stats">
          <div class="stat">
            <span class="stat-num">{{ config['hero.stat1.value'] }}</span>
            <span class="stat-label">{{ config['hero.stat1.label'] }}</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat">
            <span class="stat-num">{{ config['hero.stat2.value'] }}</span>
            <span class="stat-label">{{ config['hero.stat2.label'] }}</span>
          </div>
          <div class="stat-divider"></div>
          <div class="stat">
            <span class="stat-num">{{ config['hero.stat3.value'] }}</span>
            <span class="stat-label">{{ config['hero.stat3.label'] }}</span>
          </div>
        </div>

        <div class="hero-actions">
          <a href="#projects" class="btn btn-primary">프로젝트 보기</a>
          <a :href="config['hero.githubUrl']" target="_blank" class="btn btn-outline">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
            </svg>
            GitHub
          </a>
          <a :href="`mailto:${config['hero.email']}`" class="btn btn-ghost">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
              <polyline points="22,6 12,13 2,6"/>
            </svg>
            이메일
          </a>
        </div>
      </div>

      <div class="hero-visual">
        <div class="avatar-wrap">
          <div class="avatar">
            <span>{{ config['hero.name']?.charAt(0) || '황' }}</span>
          </div>
          <div class="avatar-ring"></div>
        </div>
        <div class="tech-pills">
          <span class="pill pill-1">Spring Boot</span>
          <span class="pill pill-2">Vue.js</span>
          <span class="pill pill-3">PostgreSQL</span>
          <span class="pill pill-4">Docker</span>
          <span class="pill pill-5">Java</span>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.hero {
  display: flex;
  align-items: flex-start;
  padding-top: 6rem;
  padding-bottom: 3rem;
  position: relative;
  overflow: hidden;
}

.hero::before {
  content: '';
  position: absolute;
  top: -200px;
  right: -200px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(96, 165, 250, 0.08) 0%, transparent 70%);
  pointer-events: none;
}

.hero-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 4rem;
  width: 100%;
}

/* ── 왼쪽 콘텐츠 ── */
.hero-content { flex: 1.2; }

.badge {
  display: inline-block;
  padding: 0.35rem 1rem;
  background: rgba(96, 165, 250, 0.12);
  border: 1px solid rgba(96, 165, 250, 0.3);
  border-radius: 999px;
  font-size: 0.82rem;
  font-weight: 700;
  color: var(--accent-color);
  letter-spacing: 0.04em;
  margin-bottom: 1.5rem;
}

.title {
  font-size: 3rem;
  line-height: 1.15;
  font-weight: 800;
  margin-bottom: 1rem;
  color: var(--text-color);
}

.highlight {
  background: linear-gradient(120deg, var(--accent-color), #818cf8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.description {
  font-size: 0.97rem;
  color: var(--text-secondary);
  line-height: 1.75;
  margin-bottom: 1.75rem;
}

/* ── 통계 ── */
.stats {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  margin-bottom: 1.75rem;
}

.stat {
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
}

.stat-num {
  font-size: 1.6rem;
  font-weight: 800;
  color: var(--text-color);
  line-height: 1;
}

.stat-label {
  font-size: 0.78rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.stat-divider {
  width: 1px;
  height: 36px;
  background: var(--border-color);
}

/* ── 액션 버튼 ── */
.hero-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  padding: 0.7rem 1.5rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  transition: all 0.2s;
  text-decoration: none;
}

.btn-primary {
  background: var(--accent-color);
  color: #fff;
}

.btn-primary:hover {
  filter: brightness(1.1);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(96, 165, 250, 0.3);
}

.btn-outline {
  border: 1.5px solid var(--border-color);
  color: var(--text-color);
  background: transparent;
}

.btn-outline:hover {
  border-color: var(--accent-color);
  color: var(--accent-color);
  transform: translateY(-2px);
}

.btn-ghost {
  color: var(--text-secondary);
  padding: 0.7rem 1rem;
}

.btn-ghost:hover {
  color: var(--text-color);
}

/* ── 오른쪽 비주얼 ── */
.hero-visual {
  flex: 0.8;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  position: relative;
}

.avatar-wrap {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3.5rem;
  font-weight: 800;
  color: #fff;
  position: relative;
  z-index: 1;
  box-shadow: 0 20px 60px rgba(59, 130, 246, 0.3);
}

.avatar-ring {
  position: absolute;
  width: 170px;
  height: 170px;
  border-radius: 50%;
  border: 2px dashed rgba(96, 165, 250, 0.3);
  animation: spin 20s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ── 기술 태그 ── */
.tech-pills {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.6rem;
  max-width: 320px;
}

.pill {
  padding: 0.35rem 0.9rem;
  border-radius: 999px;
  font-size: 0.78rem;
  font-weight: 600;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-secondary);
  transition: all 0.2s;
}

.pill:hover {
  border-color: var(--accent-color);
  color: var(--accent-color);
}

/* 반응형 */
@media (max-width: 992px) {
  .hero-inner {
    flex-direction: column-reverse;
    text-align: center;
    gap: 3rem;
  }

  .title { font-size: 2.8rem; }

  .stats, .hero-actions {
    justify-content: center;
  }

  .avatar { width: 150px; height: 150px; font-size: 3.5rem; }
  .avatar-ring { width: 170px; height: 170px; }

  .description br { display: none; }
}

@media (max-width: 480px) {
  .title { font-size: 2.2rem; }
  .btn { padding: 0.65rem 1.1rem; font-size: 0.85rem; }
}
</style>
