<script setup>
import { ref, onMounted } from 'vue'
import Skills from './components/Skills.vue'
import Projects from './components/Projects.vue'
import Contact from './components/Contact.vue'

const isDark = ref(true)

const toggleTheme = () => {
  isDark.value = !isDark.value
  document.documentElement.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
}

onMounted(() => {
  // 기본 다크모드 설정
  document.documentElement.setAttribute('data-theme', 'dark')
})
</script>

<template>
  <header class="header">
    <div class="container header-content">
      <h1 class="logo">PORTFOLIO</h1>
      <nav>
        <ul class="nav-links">
          <li><a href="#projects">Projects</a></li>
          <li><a href="#skills">Skills</a></li>
          <li><a href="#contact">Contact</a></li>
          <li>
            <button @click="toggleTheme" class="theme-toggle">
              <span v-if="isDark">🌙</span>
              <span v-else>☀️</span>
            </button>
          </li>
        </ul>
      </nav>
    </div>
  </header>

  <main>
    <Projects id="projects" />
    <Skills id="skills" />
    <Contact id="contact" />
  </main>

  <footer class="footer">
    <div class="container">
      <p>&copy; 2024 Developer Portfolio. All rights reserved.</p>
    </div>
  </footer>
</template>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  background-color: var(--header-bg);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid var(--border-color);
}

.header-content {
  height: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--accent-color);
}

.nav-links {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-links a {
  font-weight: 600;
  font-size: 0.95rem;
  transition: color 0.2s;
}

.nav-links a:hover {
  color: var(--accent-color);
}

.theme-toggle {
  font-size: 1.2rem;
  padding: 0.5rem;
  border-radius: 50%;
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.theme-toggle:hover {
  border-color: var(--accent-color);
}

.footer {
  padding: 3rem 0;
  text-align: center;
  border-top: 1px solid var(--border-color);
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .nav-links {
    gap: 1rem;
  }
  .nav-links a {
    font-size: 0.8rem;
  }
}
</style>
