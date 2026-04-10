<script setup>
defineProps({
  project: Object
})
defineEmits(['close'])
</script>

<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <button class="close-btn" @click="$emit('close')">&times;</button>
      
      <div class="modal-body">
        <div class="modal-header">
          <h2 class="title">{{ project.title }}</h2>
          <p class="description">{{ project.description }}</p>
        </div>

        <div class="info-grid">
          <div class="info-section">
            <h4>💡 프로젝트 개요</h4>
            <div class="skills-list">
              <span v-for="skill in project.skills" :key="skill" class="skill-tag">
                {{ skill }}
              </span>
            </div>
          </div>

          <div class="info-section">
            <h4>👨‍💻 담당 역할</h4>
            <ul>
              <li v-for="role in project.roles" :key="role">{{ role }}</li>
            </ul>
          </div>

          <div class="info-section problem-solving">
            <h4>🚀 문제 해결 경험</h4>
            <div class="ps-content">
              <div class="ps-item">
                <span class="label">Problem:</span> {{ project.problemSolving.problem }}
              </div>
              <div class="ps-item">
                <span class="label">Solution:</span> {{ project.problemSolving.solution }}
              </div>
              <div class="ps-item">
                <span class="label">Result:</span> {{ project.problemSolving.result }}
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <a :href="project.github" target="_blank" class="github-link">
            GitHub에서 코드 보기 &rarr;
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(5px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 1.5rem;
}

.modal-content {
  background-color: var(--bg-color);
  width: 100%;
  max-width: 800px;
  max-height: 90vh;
  border-radius: 2rem;
  position: relative;
  overflow-y: auto;
  border: 1px solid var(--border-color);
  animation: modalEnter 0.3s ease-out;
}

@keyframes modalEnter {
  from { opacity: 0; transform: scale(0.9) translateY(20px); }
  to { opacity: 1; transform: scale(1) translateY(0); }
}

.close-btn {
  position: absolute;
  top: 1.5rem;
  right: 2rem;
  font-size: 2.5rem;
  color: var(--text-secondary);
  line-height: 1;
  transition: color 0.2s;
}

.close-btn:hover {
  color: var(--accent-color);
}

.modal-body {
  padding: 4rem 3rem;
}

.modal-header {
  margin-bottom: 3rem;
  border-bottom: 2px solid var(--border-color);
  padding-bottom: 2rem;
}

.title {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 1rem;
}

.description {
  font-size: 1.1rem;
  color: var(--text-secondary);
}

.info-grid {
  display: flex;
  flex-direction: column;
  gap: 3rem;
}

.info-section h4 {
  font-size: 1.25rem;
  margin-bottom: 1.5rem;
  color: var(--text-color);
}

.skills-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.skill-tag {
  background-color: var(--card-bg);
  padding: 0.5rem 1rem;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  font-size: 0.9rem;
  font-weight: 600;
}

.info-section ul {
  list-style: disc;
  padding-left: 1.5rem;
  color: var(--text-secondary);
}

.info-section li {
  margin-bottom: 0.5rem;
}

.problem-solving {
  background-color: var(--card-bg);
  padding: 2rem;
  border-radius: 1.5rem;
  border: 1px dashed var(--accent-color);
}

.ps-content {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.ps-item {
  line-height: 1.8;
}

.label {
  font-weight: 800;
  color: var(--accent-color);
  margin-right: 0.5rem;
}

.modal-footer {
  margin-top: 4rem;
  text-align: right;
}

.github-link {
  display: inline-block;
  background-color: var(--accent-color);
  color: white;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 700;
  transition: all 0.2s;
}

.github-link:hover {
  transform: translateX(5px);
  filter: brightness(1.1);
}

@media (max-width: 768px) {
  .modal-body {
    padding: 3rem 1.5rem;
  }
}
</style>
