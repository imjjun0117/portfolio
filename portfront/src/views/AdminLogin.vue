<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const username = ref('')
const password = ref('')
const error = ref('')
const loading = ref(false)

const login = async () => {
  error.value = ''
  loading.value = true
  try {
    const res = await axios.post('/api/admin/login', {
      username: username.value,
      password: password.value
    })
    localStorage.setItem('admin_token', res.data.token)
    router.push('/admin/dashboard')
  } catch (e) {
    error.value = '아이디 또는 비밀번호가 틀렸습니다.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <h1>관리자 로그인</h1>
        <p>포트폴리오 관리자 페이지입니다.</p>
      </div>

      <form @submit.prevent="login" class="login-form">
        <div class="form-group">
          <label>아이디</label>
          <input
            v-model="username"
            type="text"
            placeholder="아이디를 입력하세요"
            required
            autocomplete="username"
          />
        </div>

        <div class="form-group">
          <label>비밀번호</label>
          <input
            v-model="password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            required
            autocomplete="current-password"
          />
        </div>

        <p v-if="error" class="error-msg">{{ error }}</p>

        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '로그인 중...' : '로그인' }}
        </button>
      </form>

      <a href="/" class="back-link">← 포트폴리오로 돌아가기</a>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0f172a;
}

.login-card {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 16px;
  padding: 2.5rem;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-header h1 {
  font-size: 1.8rem;
  font-weight: 700;
  color: #f1f5f9;
  margin-bottom: 0.4rem;
}

.login-header p {
  color: #94a3b8;
  font-size: 0.9rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.form-group label {
  font-size: 0.85rem;
  font-weight: 600;
  color: #94a3b8;
}

.form-group input {
  padding: 0.75rem 1rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  color: #f1f5f9;
  font-size: 0.95rem;
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus {
  border-color: #6366f1;
}

.error-msg {
  color: #f87171;
  font-size: 0.85rem;
  text-align: center;
}

.login-btn {
  padding: 0.85rem;
  background: #6366f1;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
  margin-top: 0.5rem;
}

.login-btn:hover:not(:disabled) {
  background: #4f46e5;
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.back-link {
  display: block;
  text-align: center;
  margin-top: 1.5rem;
  color: #94a3b8;
  font-size: 0.85rem;
  text-decoration: none;
  transition: color 0.2s;
}

.back-link:hover {
  color: #6366f1;
}
</style>
