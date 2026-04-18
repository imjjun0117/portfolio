<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const email = ref('hsungjun0117@gmail.com')
const github = ref('https://github.com/imjjun0117')
const githubDisplay = ref('github.com/imjjun0117')

onMounted(async () => {
  try {
    const res = await axios.get('/api/config')
    if (res.data['contact.email']) email.value = res.data['contact.email']
    if (res.data['contact.github']) {
      github.value = res.data['contact.github']
      githubDisplay.value = res.data['contact.github'].replace('https://', '')
    }
  } catch {
    // keep defaults
  }
})
</script>

<template>
  <section class="contact">
    <div class="container">
      <div class="contact-card fade-in">
        <div class="contact-text">
          <h2 class="title">함께 일하고 싶으시다면<br />편하게 연락 주세요.</h2>
        </div>

        <div class="contact-methods">
          <a :href="`mailto:${email}`" class="method">
            <div class="method-icon">
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                <polyline points="22,6 12,13 2,6"/>
              </svg>
            </div>
            <div class="method-info">
              <span class="method-label">Email</span>
              <span class="method-value">{{ email }}</span>
            </div>
            <span class="method-arrow">→</span>
          </a>

          <a :href="github" target="_blank" class="method">
            <div class="method-icon">
              <svg width="22" height="22" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
              </svg>
            </div>
            <div class="method-info">
              <span class="method-label">GitHub</span>
              <span class="method-value">{{ githubDisplay }}</span>
            </div>
            <span class="method-arrow">→</span>
          </a>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.contact {
  padding: 6rem 0 10rem;
}

.contact-card {
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 24px;
  padding: 4rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 3rem;
  transition: border-color 0.2s;
}

.contact-card:hover {
  border-color: var(--accent-color);
}

.title {
  font-size: 2rem;
  font-weight: 800;
  line-height: 1.35;
  color: var(--text-color);
  margin-bottom: 0.75rem;
}

.subtitle {
  color: var(--text-secondary);
  font-size: 0.95rem;
}

.contact-methods {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  min-width: 320px;
}

.method {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.1rem 1.4rem;
  background: var(--bg-color);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  text-decoration: none;
  transition: all 0.2s;
}

.method:hover {
  border-color: var(--accent-color);
  transform: translateX(4px);
}

.method-icon {
  width: 44px;
  height: 44px;
  background: rgba(96, 165, 250, 0.1);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent-color);
  flex-shrink: 0;
}

.method-info {
  display: flex;
  flex-direction: column;
  gap: 0.15rem;
  flex: 1;
}

.method-label {
  font-size: 0.72rem;
  font-weight: 700;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.method-value {
  font-size: 0.92rem;
  font-weight: 600;
  color: var(--text-color);
}

.method-arrow {
  color: var(--text-secondary);
  font-size: 1rem;
  transition: transform 0.2s, color 0.2s;
}

.method:hover .method-arrow {
  transform: translateX(4px);
  color: var(--accent-color);
}

@media (max-width: 900px) {
  .contact-card {
    flex-direction: column;
    align-items: flex-start;
    padding: 2.5rem;
  }

  .contact-methods {
    min-width: unset;
    width: 100%;
  }
}

@media (max-width: 480px) {
  .title { font-size: 1.6rem; }
  .contact-card { padding: 2rem 1.5rem; border-radius: 16px; }
}
</style>
