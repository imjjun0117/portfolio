<template>
  <section class="hero">
    <div class="container hero-inner">
      <div class="hero-content">
        <h2 class="greeting">안녕하세요, 반갑습니다! 👋</h2>
        <h1 class="title">
          사용자를 생각하는<br />
          <span class="highlight">백엔드 개발자 [홍길동]</span>입니다.
        </h1>
        <p class="description">
          복잡한 문제를 단순화하고, 효율적인 시스템 설계를 통해<br />
          비즈니스 가치를 창출하는 것에 집중합니다.
        </p>
        <div class="hero-actions">
          <a href="#projects" class="btn btn-primary">프로젝트 보기</a>
          <a href="#contact" class="btn btn-outline">연락하기</a>
        </div>
      </div>
      <div class="hero-image">
        <div class="image-placeholder">
          <!-- 실제 이미지는 나중에 추가 가능 -->
          <span>DEV</span>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.hero {
  min-height: 90vh;
  display: flex;
  align-items: center;
  padding-top: 100px;
}

.hero-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 4rem;
}

.hero-content {
  flex: 1.2;
}

.greeting {
  font-size: 1.25rem;
  color: var(--accent-color);
  font-weight: 600;
  margin-bottom: 1rem;
}

.title {
  font-size: 3.5rem;
  line-height: 1.2;
  font-weight: 800;
  margin-bottom: 1.5rem;
}

.highlight {
  background: linear-gradient(120deg, var(--accent-color), #818cf8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.description {
  font-size: 1.125rem;
  color: var(--text-secondary);
  margin-bottom: 2.5rem;
  max-width: 600px;
}

.hero-actions {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 0.8rem 2rem;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.2s;
}

.btn-primary {
  background-color: var(--accent-color);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  filter: brightness(1.1);
}

.btn-outline {
  border: 2px solid var(--border-color);
  color: var(--text-color);
}

.btn-outline:hover {
  background-color: var(--card-bg);
  border-color: var(--accent-color);
}

.hero-image {
  flex: 0.8;
  display: flex;
  justify-content: flex-end;
}

.image-placeholder {
  width: 350px;
  height: 350px;
  background: linear-gradient(135deg, var(--card-bg), var(--border-color));
  border-radius: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 4rem;
  font-weight: 800;
  color: var(--accent-color);
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
  transform: rotate(3deg);
  transition: transform 0.3s;
}

.image-placeholder:hover {
  transform: rotate(0deg) scale(1.05);
}

@media (max-width: 992px) {
  .hero-inner {
    flex-direction: column;
    text-align: center;
    gap: 3rem;
  }
  .hero-inner > div {
    flex: 1;
  }
  .description {
    margin-left: auto;
    margin-right: auto;
  }
  .hero-actions {
    justify-content: center;
  }
  .hero-image {
    justify-content: center;
  }
  .title {
    font-size: 2.5rem;
  }
}
</style>
