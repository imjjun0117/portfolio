<script setup>
import { ref } from 'vue'

defineProps({
  project: Object
})
defineEmits(['close'])

const currentImage = ref(0)
const slider = ref(null)

const handleScroll = (e) => {
  const width = e.target.offsetWidth
  currentImage.value = Math.round(e.target.scrollLeft / width)
}

const scrollToImage = (index) => {
  if (slider.value) {
    const width = slider.value.offsetWidth
    slider.value.scrollTo({
      left: width * index,
      behavior: 'smooth'
    })
  }
}
</script>

<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <button class="close-btn" @click="$emit('close')">&times;</button>
      
      <div class="modal-body">
        <div class="modal-header">
          <div class="header-main">
            <h2 class="title">{{ project.title }}</h2>
            <div class="project-period">{{ project.period }}</div>
          </div>
          <p class="description">{{ project.description }}</p>
        </div>

        <!-- Image Slider Section -->
        <div v-if="project.images && project.images.length" class="image-slider-container">
          <div class="image-slider" ref="slider" @scroll="handleScroll">
            <div v-for="(img, index) in project.images" :key="index" class="slide">
              <img :src="img" :alt="`${project.title} screenshot ${index + 1}`" />
            </div>
          </div>
          <div class="slider-dots">
            <span 
              v-for="(_, index) in project.images" 
              :key="index" 
              class="dot" 
              :class="{ active: currentImage === index }"
              @click="scrollToImage(index)"
            ></span>
          </div>
        </div>

        <div class="info-grid">
          <div class="info-section">
            <h4>📄 상세 내용</h4>
            <p class="long-description">{{ project.longDescription }}</p>
          </div>

          <div class="info-section">
            <h4>💡 기술 스택</h4>
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
          <a v-if="project.github" :href="project.github" target="_blank" class="github-link">
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

.header-main {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 1rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.title {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 0;
}

.project-period {
  font-size: 1rem;
  font-weight: 600;
  color: var(--accent-color);
  background-color: rgba(59, 130, 246, 0.1);
  padding: 0.4rem 1rem;
  border-radius: 2rem;
  margin-bottom: 0.3rem;
}

.description {
  font-size: 1.1rem;
  color: var(--text-secondary);
}

.long-description {
  line-height: 1.8;
  color: var(--text-secondary);
  font-size: 1.05rem;
  white-space: pre-wrap;
}

.image-slider-container {
  margin-bottom: 3rem;
  position: relative;
}

.image-slider {
  display: flex;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scrollbar-width: none;
  border-radius: 1.5rem;
  aspect-ratio: 16 / 9;
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
}

.image-slider::-webkit-scrollbar {
  display: none;
}

.slide {
  flex: 0 0 100%;
  scroll-snap-align: center;
  height: 100%;
}

.slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.slider-dots {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 1rem;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: var(--border-color);
  cursor: pointer;
  transition: all 0.3s;
}

.dot.active {
  background-color: var(--accent-color);
  transform: scale(1.2);
  width: 20px;
  border-radius: 10px;
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
