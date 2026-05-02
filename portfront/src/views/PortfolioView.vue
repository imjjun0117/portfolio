<script setup>
import { onMounted } from 'vue'
import Hero from '../components/Hero.vue'
import Experience from '../components/Experience.vue'
import Projects from '../components/Projects.vue'
import Skills from '../components/Skills.vue'

const isDark = true

onMounted(() => {
  document.documentElement.setAttribute('data-theme', 'dark')

  // 스크롤 페이드인 애니메이션
  const io = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible')
          io.unobserve(entry.target)
        }
      })
    },
    { threshold: 0.12 }
  )

  const observeNew = () => {
    document.querySelectorAll('.fade-in:not(.visible)').forEach((el) => io.observe(el))
  }

  // 초기 등록
  observeNew()

  // 데이터 로드 후 동적으로 추가된 .fade-in 요소도 감지
  const mo = new MutationObserver(observeNew)
  mo.observe(document.body, { childList: true, subtree: true })
})
</script>

<template>
  <div>
    <header class="header">
      <div class="container header-content">
        <a href="#" class="logo">황성준</a>
        <nav>
          <ul class="nav-links">
            <li><a href="#experience">Experience</a></li>
            <li><a href="#projects">Projects</a></li>
            <li><a href="#skills">Skills</a></li>
            <li>
              <a
                href="https://github.com/imjjun0117"
                target="_blank"
                class="nav-github"
                aria-label="GitHub"
              >
                <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor">
                  <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
                </svg>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </header>

    <main>
      <Hero id="hero" />
      <Experience id="experience" />
      <Projects id="projects" />
      <Skills id="skills" />
    </main>

    <footer class="footer">
      <div class="container">
        <p>© 2025 황성준. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: var(--header-bg);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--border-color);
}

.header-content {
  height: 64px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.15rem;
  font-weight: 800;
  color: var(--text-color);
  text-decoration: none;
  letter-spacing: -0.01em;
}

.nav-links {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-links a {
  font-weight: 600;
  font-size: 0.88rem;
  color: var(--text-secondary);
  transition: color 0.2s;
  text-decoration: none;
}

.nav-links a:hover {
  color: var(--text-color);
}

.nav-github {
  display: flex;
  align-items: center;
  color: var(--text-secondary);
  transition: color 0.2s;
}

.nav-github:hover {
  color: var(--text-color);
}

.footer {
  padding: 2.5rem 0;
  text-align: center;
  border-top: 1px solid var(--border-color);
  color: var(--text-secondary);
  font-size: 0.85rem;
}

@media (max-width: 768px) {
  .nav-links { gap: 1.2rem; }
  .nav-links a { font-size: 0.8rem; }
}

@media (max-width: 480px) {
  .nav-links li:nth-child(1) { display: none; }
}
</style>
