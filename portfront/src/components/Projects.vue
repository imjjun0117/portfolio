<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import ProjectCard from './ProjectCard.vue'
import ProjectModal from './ProjectModal.vue'

const projects = ref([])
const selectedProject = ref(null)
const isLoading = ref(true)

const fetchProjects = async () => {
  try {
    const response = await axios.get('/api/projects')
    projects.value = response.data
  } catch (error) {
    console.error('Failed to fetch projects:', error)
  } finally {
    isLoading.value = false
  }
}

const openModal = (project) => {
  selectedProject.value = project
  document.body.style.overflow = 'hidden'
}

const closeModal = () => {
  selectedProject.value = null
  document.body.style.overflow = ''
}

onMounted(fetchProjects)
</script>

<template>
  <section class="projects">
    <div class="container">
      <div class="section-header">
        <h2 class="section-title">Case Studies</h2>
        <p class="section-subtitle">실제 비즈니스 문제를 해결한 프로젝트 사례입니다.</p>
      </div>
      
      <div v-if="isLoading" class="loading">
        데이터를 불러오는 중입니다...
      </div>
      
      <div v-else class="slider-wrapper">
        <div class="project-slider" ref="slider">
          <ProjectCard 
            v-for="project in projects" 
            :key="project.id" 
            :project="project" 
            class="slider-item"
            @click="openModal(project)"
          />
        </div>
        <div class="slider-hint">
          <span>좌우로 스크롤하여 더 많은 프로젝트를 확인하세요 &rarr;</span>
        </div>
      </div>

      <ProjectModal 
        v-if="selectedProject" 
        :project="selectedProject" 
        @close="closeModal" 
      />
    </div>
  </section>
</template>

<style scoped>
.projects {
  padding: 100px 0;
  background-color: var(--bg-primary);
}

.section-header {
  margin-bottom: 4rem;
}

.section-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, var(--accent-color), #60a5fa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.section-subtitle {
  font-size: 1.1rem;
  color: var(--text-secondary);
}

.slider-wrapper {
  position: relative;
  margin: 0 -20px;
}

.project-slider {
  display: flex;
  gap: 2rem;
  overflow-x: auto;
  padding: 20px;
  scroll-snap-type: x mandatory;
  scrollbar-width: none; /* Firefox */
}

.project-slider::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.slider-item {
  flex: 0 0 calc(50% - 1rem);
  min-width: 400px;
  scroll-snap-align: start;
}

.slider-hint {
  margin-top: 2rem;
  text-align: right;
  padding-right: 20px;
  color: var(--text-secondary);
  font-size: 0.9rem;
  opacity: 0.7;
}

.loading {
  text-align: center;
  padding: 4rem;
  color: var(--text-secondary);
  font-size: 1.2rem;
}

@media (max-width: 1024px) {
  .slider-item {
    flex: 0 0 calc(80% - 1rem);
    min-width: 300px;
  }
}

@media (max-width: 768px) {
  .section-title {
    font-size: 2.2rem;
  }
  .slider-item {
    flex: 0 0 calc(100% - 1rem);
    min-width: 280px;
  }
}
</style>
