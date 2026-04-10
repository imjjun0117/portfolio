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
      <h2 class="section-title">Projects</h2>
      
      <div v-if="isLoading" class="loading">
        데이터를 불러오는 중입니다...
      </div>
      
      <div v-else class="project-grid">
        <ProjectCard 
          v-for="project in projects" 
          :key="project.id" 
          :project="project" 
          @click="openModal(project)"
        />
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
.section-title {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 3rem;
  text-align: center;
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 2.5rem;
}

.loading {
  text-align: center;
  padding: 4rem;
  color: var(--text-secondary);
  font-size: 1.2rem;
}

@media (max-width: 600px) {
  .project-grid {
    grid-template-columns: 1fr;
  }
}
</style>
