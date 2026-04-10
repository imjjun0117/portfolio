<script setup>
const skillGroups = [
  {
    name: 'Backend',
    skills: ['Java', 'Spring Boot', 'PostgreSQL', 'JPA', 'Redis']
  },
  {
    name: 'Frontend',
    skills: ['JavaScript', 'Vue.js', 'Vite', 'CSS3', 'HTML5']
  },
  {
    name: 'DevOps & Others',
    skills: ['Docker', 'AWS', 'Git', 'Maven', 'REST API']
  }
]
</script>

<template>
  <section class="skills">
    <div class="container">
      <h2 class="section-title">Technical Skills</h2>
      <div class="skills-grid">
        <div v-for="group in skillGroups" :key="group.name" class="skill-card">
          <h3>{{ group.name }}</h3>
          <div class="skill-tags">
            <span v-for="skill in group.skills" :key="skill" class="tag">
              {{ skill }}
            </span>
          </div>
        </div>
      </div>
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

.skills-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.skill-card {
  background-color: var(--card-bg);
  padding: 2.5rem;
  border-radius: 1.5rem;
  border: 1px solid var(--border-color);
  transition: transform 0.3s, border-color 0.3s;
}

.skill-card:hover {
  transform: translateY(-5px);
  border-color: var(--accent-color);
}

.skill-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  color: var(--accent-color);
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.tag {
  background-color: var(--bg-color);
  padding: 0.5rem 1rem;
  border-radius: 999px;
  font-size: 0.9rem;
  font-weight: 600;
  border: 1px solid var(--border-color);
}
</style>
