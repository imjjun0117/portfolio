<script setup>
defineProps({
  project: Object
})
</script>

<template>
  <div class="card">
    <div class="card-image">
      <img :src="project.thumbnail" :alt="project.title" />
    </div>
    <div class="card-body">
      <h3 class="card-title">{{ project.title }}</h3>
      <p class="card-desc">{{ project.description }}</p>
      <div class="tags">
        <span v-for="skill in project.skills.slice(0, 3)" :key="skill" class="tag">
          {{ skill }}
        </span>
        <span v-if="project.skills.length > 3" class="tag-more">
          +{{ project.skills.length - 3 }}
        </span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  background-color: var(--card-bg);
  border-radius: 1.25rem;
  overflow: hidden;
  border: 1px solid var(--border-color);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
}

.card:hover {
  transform: translateY(-10px);
  border-color: var(--accent-color);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.card-image {
  height: 220px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.card:hover .card-image img {
  transform: scale(1.1);
}

.card-body {
  padding: 1.5rem;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
}

.card-desc {
  font-size: 0.95rem;
  color: var(--text-secondary);
  margin-bottom: 1.5rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.tag {
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--accent-color);
  background-color: rgba(59, 130, 246, 0.1);
  padding: 0.25rem 0.75rem;
  border-radius: 6px;
}

.tag-more {
  font-size: 0.75rem;
  color: var(--text-secondary);
  padding: 0.25rem 0.5rem;
}
</style>
