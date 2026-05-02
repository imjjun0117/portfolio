<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import ScreenList from '@/components/project/ScreenList.vue'
import ScreenViewer from '@/components/project/ScreenViewer.vue'
import DescriptionPanel from '@/components/project/DescriptionPanel.vue'

const route = useRoute()
const router = useRouter()

const project = ref(null)
const loading = ref(true)
const notFound = ref(false)
const selectedScreenIndex = ref(0)

const totalScreens = computed(() => project.value?.screens?.length ?? 0)
const hasPrev = computed(() => selectedScreenIndex.value > 0)
const hasNext = computed(() => selectedScreenIndex.value < totalScreens.value - 1)

const prev = () => { if (hasPrev.value) selectedScreenIndex.value-- }
const next = () => { if (hasNext.value) selectedScreenIndex.value++ }

const onKeydown = (e) => {
  if (totalScreens.value === 0) return
  if (e.key === 'ArrowLeft') prev()
  if (e.key === 'ArrowRight') next()
}

onMounted(async () => {
  document.documentElement.setAttribute('data-theme', 'dark')
  window.addEventListener('keydown', onKeydown)
  try {
    const res = await axios.get(`/api/projects/${route.params.id}`)
    project.value = res.data
  } catch (e) {
    if (e.response?.status === 404) notFound.value = true
  } finally {
    loading.value = false
  }
})

onUnmounted(() => {
  window.removeEventListener('keydown', onKeydown)
})

const goBack = () => {
  if (window.history.length > 1) router.back()
  else router.push('/')
}

const selectScreen = (i) => {
  selectedScreenIndex.value = i
}
</script>

<template>
  <div class="detail-page">

    <!-- 헤더 -->
    <header class="detail-header">
      <div class="detail-header-inner">
        <button @click="goBack" class="back-btn">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
            <polyline points="15,18 9,12 15,6"/>
          </svg>
          목록으로
        </button>
        <a href="/" class="site-logo">황성준</a>
      </div>
    </header>

    <!-- 로딩 -->
    <div v-if="loading" class="state-box">
      <div class="spinner"></div>
    </div>

    <!-- 404 -->
    <div v-else-if="notFound" class="state-box">
      <p class="state-text">프로젝트를 찾을 수 없습니다.</p>
      <button @click="goBack" class="btn-outline">돌아가기</button>
    </div>

    <!-- ══════════════════════════════════════
         스크린 뷰어 레이아웃 (screens 있을 때)
    ══════════════════════════════════════════ -->
    <template v-else-if="project && project.screens && project.screens.length > 0">
      <!-- 프로젝트 개요 바 -->
      <div class="meta-bar">
        <!-- 1행: 제목 + 뱃지 + GitHub -->
        <div class="meta-row1">
          <div class="meta-row1-left">
            <span v-if="project.vibeCoding" class="vibe-badge">⚡ Vibe Coding</span>
            <h1 class="meta-title">{{ project.title }}</h1>
            <span class="meta-period">{{ project.period }}</span>
          </div>
          <a v-if="project.github" :href="project.github" target="_blank" class="github-link">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
              <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
            </svg>
            GitHub
          </a>
        </div>
        <!-- 2행: 한줄 설명 + 기술스택 -->
        <div class="meta-row2">
          <p v-if="project.description" class="meta-desc">{{ project.description }}</p>
          <div class="meta-skills">
            <span v-for="skill in (project.skills || [])" :key="skill" class="skill-tag">{{ skill }}</span>
          </div>
        </div>
      </div>

      <!-- 3패널 뷰어 -->
      <div class="viewer-wrap">
        <ScreenList
          :screens="project.screens"
          :selected-index="selectedScreenIndex"
          @select="selectScreen"
        />

        <!-- 뷰어 + 화살표 버튼 -->
        <div class="viewer-center">
          <ScreenViewer :screen="project.screens[selectedScreenIndex]" />
          <button v-if="hasPrev" class="nav-arrow nav-arrow-left" @click="prev">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polyline points="15,18 9,12 15,6"/>
            </svg>
          </button>
          <button v-if="hasNext" class="nav-arrow nav-arrow-right" @click="next">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polyline points="9,18 15,12 9,6"/>
            </svg>
          </button>
          <!-- 하단 페이지 도트 -->
          <div class="nav-dots">
            <button
              v-for="(_, i) in project.screens"
              :key="i"
              :class="['nav-dot', { active: selectedScreenIndex === i }]"
              @click="selectScreen(i)"
            />
          </div>
        </div>

        <DescriptionPanel
          :screen="project.screens[selectedScreenIndex]"
          :screen-index="selectedScreenIndex"
          :total="project.screens.length"
        />
      </div>
    </template>

    <!-- ══════════════════════════════════════
         기존 레이아웃 폴백 (screens 없을 때)
    ══════════════════════════════════════════ -->
    <main v-else-if="project" class="detail-main">

      <!-- 히어로 이미지 -->
      <div class="hero-img-wrap">
        <img v-if="project.thumbnail" :src="project.thumbnail" :alt="project.title" class="hero-img" />
        <div v-else class="hero-img-default">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1">
            <rect x="3" y="3" width="18" height="18" rx="2"/>
            <circle cx="8.5" cy="8.5" r="1.5"/>
            <polyline points="21,15 16,10 5,21"/>
          </svg>
        </div>
        <div class="hero-img-overlay"></div>
      </div>

      <div class="content-wrap">

        <!-- 프로젝트 타이틀 블록 -->
        <section class="title-block">
          <div class="title-meta">
            <span class="period-badge">{{ project.period }}</span>
            <span v-if="project.vibeCoding" class="vibe-badge-legacy">⚡ Vibe Coding</span>
            <a v-if="project.github" :href="project.github" target="_blank" class="github-link-legacy">
              <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
              </svg>
              GitHub
            </a>
          </div>
          <h1 class="project-title">{{ project.title }}</h1>
          <p class="project-summary">{{ project.description }}</p>
        </section>

        <div class="divider"></div>

        <!-- 기술스택 + 담당역할 -->
        <section class="two-col">
          <div class="col-block">
            <h2 class="block-title">기술 스택</h2>
            <div class="tag-wrap">
              <span v-for="skill in project.skills" :key="skill" class="tag">{{ skill }}</span>
            </div>
          </div>
          <div class="col-block" v-if="project.roles && project.roles.length">
            <h2 class="block-title">담당 역할</h2>
            <ul class="role-list">
              <li v-for="role in project.roles" :key="role">{{ role }}</li>
            </ul>
          </div>
        </section>

        <div class="divider"></div>

        <!-- 상세 설명 -->
        <section v-if="project.longDescription || project.description" class="desc-block">
          <h2 class="block-title">프로젝트 소개</h2>
          <p class="desc-text">{{ project.longDescription || project.description }}</p>
        </section>

        <!-- 문제 해결 -->
        <template v-if="project.problemSolving?.problem">
          <div class="divider"></div>
          <section class="ps-section">
            <h2 class="block-title">문제 해결 과정</h2>
            <div class="ps-grid">
              <div class="ps-card ps-problem">
                <div class="ps-label">문제</div>
                <p class="ps-body">{{ project.problemSolving.problem }}</p>
              </div>
              <div class="ps-card ps-solution">
                <div class="ps-label">해결</div>
                <p class="ps-body">{{ project.problemSolving.solution }}</p>
              </div>
              <div class="ps-card ps-result">
                <div class="ps-label">결과</div>
                <p class="ps-body">{{ project.problemSolving.result }}</p>
              </div>
            </div>
          </section>
        </template>

        <!-- 이미지 갤러리 -->
        <template v-if="project.images && project.images.length">
          <div class="divider"></div>
          <section class="gallery-section">
            <h2 class="block-title">스크린샷</h2>
            <div class="gallery-grid">
              <div v-for="(img, i) in project.images" :key="i" class="gallery-item">
                <img :src="img" :alt="`${project.title} 스크린샷 ${i + 1}`"
                  @error="e => e.target.closest('.gallery-item').style.display='none'" />
              </div>
            </div>
          </section>
        </template>

        <!-- 하단 뒤로가기 -->
        <div class="bottom-nav">
          <button @click="goBack" class="btn-back-bottom">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polyline points="15,18 9,12 15,6"/>
            </svg>
            목록으로 돌아가기
          </button>
        </div>

      </div>
    </main>
  </div>
</template>

<style scoped>
.detail-page {
  min-height: 100vh;
  background: var(--bg-color);
  color: var(--text-color);
  font-family: 'Pretendard', -apple-system, sans-serif;
}

/* ── 헤더 ── */
.detail-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 100;
  background: rgba(10, 22, 40, 0.92);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid #1e2d4a;
}

.detail-header-inner {
  max-width: 100%;
  padding: 0 1.5rem;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.88rem;
  font-weight: 600;
  color: #64748b;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.4rem 0.75rem;
  border-radius: 7px;
  transition: all 0.2s;
  font-family: inherit;
}

.back-btn:hover {
  color: #f1f5f9;
  background: rgba(255, 255, 255, 0.06);
}

.site-logo {
  font-size: 1rem;
  font-weight: 800;
  color: #f1f5f9;
  text-decoration: none;
}

/* ── 상태 박스 ── */
.state-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  gap: 1.5rem;
}

.state-text { color: #64748b; font-size: 1rem; }

.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid #1e2d4a;
  border-top-color: #60a5fa;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

/* ══════════════════════════════════════
   스크린 뷰어 레이아웃
══════════════════════════════════════ */

/* 메타 바 */
.meta-bar {
  position: fixed;
  top: 56px;
  left: 0;
  right: 0;
  z-index: 99;
  background: #0d1b2e;
  border-bottom: 1px solid #1e2d4a;
  padding: 0.6rem 1.5rem 0.7rem;
  display: flex;
  flex-direction: column;
  gap: 0.45rem;
}

/* 1행 */
.meta-row1 {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.meta-row1-left {
  display: flex;
  align-items: center;
  gap: 0.65rem;
  min-width: 0;
}

.meta-title {
  font-size: 0.97rem;
  font-weight: 800;
  color: #f1f5f9;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta-period {
  font-size: 0.73rem;
  font-weight: 600;
  color: #60a5fa;
  flex-shrink: 0;
}

.vibe-badge {
  font-size: 0.63rem;
  font-weight: 700;
  color: #a78bfa;
  background: rgba(139, 92, 246, 0.12);
  border: 1px solid rgba(139, 92, 246, 0.3);
  padding: 0.12rem 0.5rem;
  border-radius: 999px;
  flex-shrink: 0;
}

.github-link {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  font-size: 0.75rem;
  font-weight: 600;
  color: #64748b;
  border: 1px solid #1e2d4a;
  padding: 0.18rem 0.65rem;
  border-radius: 999px;
  text-decoration: none;
  transition: all 0.2s;
  flex-shrink: 0;
}

.github-link:hover { color: #f1f5f9; border-color: #60a5fa; }

/* 2행 */
.meta-row2 {
  display: flex;
  align-items: center;
  gap: 1rem;
  min-width: 0;
}

.meta-desc {
  font-size: 0.78rem;
  color: #64748b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex-shrink: 1;
  min-width: 0;
}

.meta-skills {
  display: flex;
  align-items: center;
  flex-wrap: nowrap;
  gap: 0.3rem;
  flex-shrink: 0;
  overflow-x: auto;
}

.skill-tag {
  font-size: 0.65rem;
  font-weight: 600;
  padding: 0.12rem 0.5rem;
  border-radius: 5px;
  background: rgba(96, 165, 250, 0.08);
  color: #60a5fa;
  border: 1px solid rgba(96, 165, 250, 0.18);
  white-space: nowrap;
  flex-shrink: 0;
}

/* 3패널 뷰어 */
.viewer-wrap {
  display: flex;
  height: calc(100vh - 56px - 76px); /* header(56) + meta-bar(76) */
  margin-top: calc(56px + 76px);
  overflow: hidden;
}

.viewer-center {
  flex: 1;
  position: relative;
  display: flex;
  min-width: 0;
}

/* 화살표 버튼 */
.nav-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(15, 23, 42, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #f1f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s, border-color 0.2s, transform 0.2s;
  z-index: 10;
  backdrop-filter: blur(6px);
}

.nav-arrow:hover {
  background: rgba(96, 165, 250, 0.25);
  border-color: rgba(96, 165, 250, 0.5);
  transform: translateY(-50%) scale(1.08);
}

.nav-arrow-left { left: 1rem; }
.nav-arrow-right { right: 1rem; }

/* 하단 도트 */
.nav-dots {
  position: absolute;
  bottom: 1rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 0.4rem;
  z-index: 10;
}

.nav-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  border: none;
  cursor: pointer;
  transition: background 0.2s, transform 0.2s;
  padding: 0;
}

.nav-dot.active {
  background: #60a5fa;
  transform: scale(1.3);
}

.nav-dot:hover {
  background: rgba(96, 165, 250, 0.6);
}

/* ══════════════════════════════════════
   레거시 레이아웃 (screens 없을 때)
══════════════════════════════════════ */

/* 히어로 이미지 */
.hero-img-wrap {
  position: relative;
  width: 100%;
  height: 380px;
  margin-top: 56px;
  overflow: hidden;
  background: #0a1628;
}

.hero-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.75;
}

.hero-img-default {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e293b, #0f172a);
  color: #334155;
}

.hero-img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom, transparent 40%, var(--bg-color) 100%);
}

/* 본문 */
.content-wrap {
  max-width: 860px;
  margin: 0 auto;
  padding: 0 1.5rem 6rem;
}

/* 타이틀 블록 */
.title-block {
  padding-top: 2rem;
  margin-bottom: 2.5rem;
}

.title-meta {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.period-badge {
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--accent-color);
  background: rgba(96, 165, 250, 0.1);
  border: 1px solid rgba(96, 165, 250, 0.2);
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
}

.vibe-badge-legacy {
  font-size: 0.75rem;
  font-weight: 700;
  color: #a78bfa;
  background: rgba(139, 92, 246, 0.12);
  border: 1px solid rgba(139, 92, 246, 0.3);
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  letter-spacing: 0.03em;
}

.github-link-legacy {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  text-decoration: none;
  transition: all 0.2s;
}

.github-link-legacy:hover { color: var(--text-color); border-color: var(--accent-color); }

.project-title {
  font-size: 2.2rem;
  font-weight: 800;
  line-height: 1.25;
  color: var(--text-color);
  margin-bottom: 0.85rem;
}

.project-summary {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.75;
}

/* 구분선 */
.divider {
  height: 1px;
  background: var(--border-color);
  margin: 2.5rem 0;
}

/* 2단 */
.two-col {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 3rem;
}

.block-title {
  font-size: 0.72rem;
  font-weight: 800;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.1em;
  margin-bottom: 1rem;
}

.tag-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  font-size: 0.8rem;
  font-weight: 600;
  padding: 0.3rem 0.8rem;
  border-radius: 7px;
  background: rgba(96, 165, 250, 0.08);
  color: var(--accent-color);
  border: 1px solid rgba(96, 165, 250, 0.2);
}

.role-list {
  display: flex;
  flex-direction: column;
  gap: 0.55rem;
}

.role-list li {
  font-size: 0.92rem;
  color: var(--text-color);
  padding-left: 1rem;
  position: relative;
  line-height: 1.55;
}

.role-list li::before {
  content: '–';
  position: absolute;
  left: 0;
  color: var(--accent-color);
}

/* 상세 설명 */
.desc-text {
  font-size: 0.97rem;
  color: var(--text-secondary);
  line-height: 1.9;
  white-space: pre-line;
}

/* 문제 해결 */
.ps-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

.ps-card {
  border-radius: 12px;
  padding: 1.25rem;
  border: 1px solid;
}

.ps-problem { background: rgba(239, 68, 68, 0.05); border-color: rgba(239, 68, 68, 0.2); }
.ps-solution { background: rgba(96, 165, 250, 0.05); border-color: rgba(96, 165, 250, 0.2); }
.ps-result { background: rgba(16, 185, 129, 0.05); border-color: rgba(16, 185, 129, 0.2); }

.ps-label {
  font-size: 0.68rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 0.65rem;
}

.ps-problem .ps-label { color: #f87171; }
.ps-solution .ps-label { color: var(--accent-color); }
.ps-result .ps-label { color: #34d399; }

.ps-body {
  font-size: 0.88rem;
  color: var(--text-secondary);
  line-height: 1.65;
}

/* 갤러리 */
.gallery-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.gallery-item {
  border-radius: 10px;
  overflow: hidden;
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  aspect-ratio: 16 / 9;
}

.gallery-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.35s ease;
}

.gallery-item:hover img { transform: scale(1.03); }

/* 하단 버튼 */
.bottom-nav {
  margin-top: 4rem;
  display: flex;
  justify-content: center;
}

.btn-back-bottom {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-secondary);
  background: none;
  border: 1px solid var(--border-color);
  padding: 0.65rem 1.4rem;
  border-radius: 999px;
  cursor: pointer;
  transition: all 0.2s;
  font-family: inherit;
}

.btn-back-bottom:hover { color: var(--text-color); border-color: var(--accent-color); }

.btn-outline {
  padding: 0.65rem 1.4rem;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: none;
  color: var(--text-secondary);
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  font-family: inherit;
  transition: all 0.2s;
}

.btn-outline:hover { border-color: var(--accent-color); color: var(--text-color); }

/* ── 반응형 ── */
@media (max-width: 900px) {
  .viewer-wrap {
    flex-direction: column;
    height: auto;
    overflow: visible;
  }

  /* 스크린 리스트: 가로 스크롤 */
  :deep(.screen-list) {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid #1e2d4a;
    flex-direction: column;
  }
  :deep(.screen-items) {
    display: flex;
    flex-direction: row;
    overflow-x: auto;
    padding: 0.5rem;
    gap: 0.5rem;
  }
  :deep(.screen-item) {
    flex-direction: column;
    width: 88px;
    padding: 0.5rem;
    border-left: none;
    border-bottom: 2px solid transparent;
  }
  :deep(.screen-item.active) {
    border-bottom-color: #60a5fa;
    border-left-color: transparent;
  }
  :deep(.screen-thumb) { width: 72px; height: 48px; }
  :deep(.screen-item-info) { align-items: center; }

  /* 뷰어: 16:9 박스 */
  .viewer-center { min-height: 220px; }
  :deep(.screen-viewer) { min-height: 220px; }

  /* 설명 패널: 전폭 */
  :deep(.desc-panel) {
    width: 100%;
    border-left: none;
    border-top: 1px solid #1e2d4a;
    padding: 1.25rem 1rem;
  }

  .meta-row2 { display: none; }
  .meta-desc { display: none; }
}

@media (max-width: 720px) {
  .hero-img-wrap { height: 240px; }
  .project-title { font-size: 1.6rem; }
  .two-col { grid-template-columns: 1fr; gap: 2rem; }
  .ps-grid { grid-template-columns: 1fr; }
  .gallery-grid { grid-template-columns: 1fr; }
}
</style>
