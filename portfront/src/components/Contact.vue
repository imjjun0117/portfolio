<template>
  <section class="contact">
    <div class="container">
      <div class="contact-card">
        <h2 class="title">Contact Me</h2>
        <p class="subtitle">
          함께 성장할 동료를 찾고 계시거나,<br />
          궁금한 점이 있으시다면 언제든 연락주세요.
        </p>
        
        <div class="contact-methods">
          <div class="method">
            <span class="icon">📧</span>
            <div class="details">
              <h4>Email</h4>
              <p>example@email.com</p>
            </div>
          </div>
          <div class="method">
            <span class="icon">🐱</span>
            <div class="details">
              <h4>GitHub</h4>
              <p><a href="https://github.com/example" target="_blank">github.com/example</a></p>
            </div>
          </div>
          <div class="method">
            <span class="icon">💼</span>
            <div class="details">
              <h4>LinkedIn</h4>
              <p><a href="#" target="_blank">linkedin.com/in/example</a></p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.contact {
  padding-bottom: 10rem;
}

.contact-card {
  background: linear-gradient(135deg, var(--accent-color), #818cf8);
  padding: 5rem 3rem;
  border-radius: 3rem;
  color: white;
  text-align: center;
  box-shadow: 0 30px 60px rgba(59, 130, 246, 0.2);
}

.title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1.5rem;
}

.subtitle {
  font-size: 1.25rem;
  margin-bottom: 4rem;
  opacity: 0.9;
}

.contact-methods {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 3rem;
}

.method {
  display: flex;
  align-items: center;
  gap: 1rem;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 1.5rem 2rem;
  border-radius: 1.5rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.3s;
}

.method:hover {
  transform: translateY(-5px);
  background-color: rgba(255, 255, 255, 0.15);
}

.icon {
  font-size: 2rem;
}

.details {
  text-align: left;
}

.details h4 {
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 0.1rem;
  margin-bottom: 0.25rem;
  opacity: 0.8;
}

.details p {
  font-weight: 700;
}

.details a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .contact-card {
    padding: 3rem 1.5rem;
  }
  .title {
    font-size: 2rem;
  }
  .contact-methods {
    gap: 1rem;
    flex-direction: column;
  }
  .method {
    width: 100%;
  }
}
</style>
