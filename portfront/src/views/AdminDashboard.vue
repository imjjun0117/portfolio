<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// ── 상태 ──────────────────────────────────────────
const projects = ref([])
const loading = ref(true)
const showModal = ref(false)
const editingId = ref(null)
const deleteConfirmId = ref(null)
const saveLoading = ref(false)
const toast = ref({ show: false, message: '', type: 'success' })

// 파일 업로드용 input refs
const thumbnailInput = ref(null)
const imageInput = ref(null)
const thumbnailUploading = ref(false)
const imageUploading = ref(false)

const emptyForm = () => ({
  title: '',
  description: '',
  longDescription: '',
  period: '',
  github: '',
  thumbnail: '',
  skills: '',
  roles: '',
  images: [],      // 배열로 관리
  problem: '',
  solution: '',
  result: ''
})

const form = ref(emptyForm())
const newImageUrl = ref('')
const modalTitle = computed(() => editingId.value ? '프로젝트 수정' : '프로젝트 추가')

// ── API 헬퍼 ──────────────────────────────────────
const token = () => localStorage.getItem('admin_token')
const authHeaders = () => ({ headers: { Authorization: `Bearer ${token()}` } })

// ── 프로젝트 목록 ──────────────────────────────────
const fetchProjects = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/projects')
    projects.value = res.data
  } catch {
    showToast('데이터를 불러오지 못했습니다.', 'error')
  } finally {
    loading.value = false
  }
}

// ── 모달 열기 ──────────────────────────────────────
const openCreate = () => {
  editingId.value = null
  form.value = emptyForm()
  newImageUrl.value = ''
  showModal.value = true
}

const openEdit = (project) => {
  editingId.value = project.id
  form.value = {
    title: project.title || '',
    description: project.description || '',
    longDescription: project.longDescription || '',
    period: project.period || '',
    github: project.github || '',
    thumbnail: project.thumbnail || '',
    skills: (project.skills || []).join(', '),
    roles: (project.roles || []).join('\n'),
    images: [...(project.images || [])],
    problem: project.problemSolving?.problem || '',
    solution: project.problemSolving?.solution || '',
    result: project.problemSolving?.result || ''
  }
  newImageUrl.value = ''
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  editingId.value = null
}

// ── 파일 업로드 ────────────────────────────────────
const uploadFile = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  const res = await axios.post('/api/admin/upload', formData, {
    headers: {
      Authorization: `Bearer ${token()}`,
      'Content-Type': 'multipart/form-data'
    }
  })
  return res.data.url
}

const onThumbnailFile = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  thumbnailUploading.value = true
  try {
    form.value.thumbnail = await uploadFile(file)
    showToast('썸네일이 업로드되었습니다.')
  } catch {
    showToast('썸네일 업로드에 실패했습니다.', 'error')
  } finally {
    thumbnailUploading.value = false
    e.target.value = ''
  }
}

const onImageFile = async (e) => {
  const files = Array.from(e.target.files)
  if (!files.length) return
  imageUploading.value = true
  try {
    for (const file of files) {
      const url = await uploadFile(file)
      form.value.images.push(url)
    }
    showToast(`이미지 ${files.length}개가 업로드되었습니다.`)
  } catch {
    showToast('이미지 업로드에 실패했습니다.', 'error')
  } finally {
    imageUploading.value = false
    e.target.value = ''
  }
}

// URL 직접 추가
const addImageUrl = () => {
  const url = newImageUrl.value.trim()
  if (!url) return
  form.value.images.push(url)
  newImageUrl.value = ''
}

const removeImage = (index) => {
  form.value.images.splice(index, 1)
}

// ── 저장 ──────────────────────────────────────────
const saveProject = async () => {
  saveLoading.value = true
  try {
    const payload = {
      title: form.value.title,
      description: form.value.description,
      longDescription: form.value.longDescription,
      period: form.value.period,
      github: form.value.github,
      thumbnail: form.value.thumbnail,
      skills: form.value.skills.split(',').map(s => s.trim()).filter(Boolean),
      roles: form.value.roles.split('\n').map(s => s.trim()).filter(Boolean),
      images: form.value.images,
      problem: form.value.problem,
      solution: form.value.solution,
      result: form.value.result
    }

    if (editingId.value) {
      await axios.put(`/api/projects/${editingId.value}`, payload, authHeaders())
      showToast('프로젝트가 수정되었습니다.')
    } else {
      await axios.post('/api/projects', payload, authHeaders())
      showToast('프로젝트가 추가되었습니다.')
    }

    closeModal()
    await fetchProjects()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('저장에 실패했습니다.', 'error')
  } finally {
    saveLoading.value = false
  }
}

// ── 삭제 ──────────────────────────────────────────
const confirmDelete = (id) => { deleteConfirmId.value = id }

const deleteProject = async () => {
  try {
    await axios.delete(`/api/projects/${deleteConfirmId.value}`, authHeaders())
    deleteConfirmId.value = null
    showToast('프로젝트가 삭제되었습니다.')
    await fetchProjects()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('삭제에 실패했습니다.', 'error')
  }
}

// ── 로그아웃 ──────────────────────────────────────
const logout = () => {
  localStorage.removeItem('admin_token')
  router.push('/admin')
}

const handleUnauthorized = () => {
  localStorage.removeItem('admin_token')
  router.push('/admin')
}

// ── 토스트 ────────────────────────────────────────
const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3000)
}

onMounted(fetchProjects)
</script>

<template>
  <div class="admin">
    <!-- 헤더 -->
    <header class="admin-header">
      <div class="admin-header-inner">
        <div class="admin-logo">
          <span class="logo-dot"></span>
          <span>관리자 대시보드</span>
        </div>
        <div class="admin-header-actions">
          <a href="/" target="_blank" class="btn btn-ghost">포트폴리오 보기</a>
          <button @click="logout" class="btn btn-outline">로그아웃</button>
        </div>
      </div>
    </header>

    <main class="admin-main">
      <div class="section-top">
        <div>
          <h2 class="section-title">프로젝트 관리</h2>
          <p class="section-sub">총 {{ projects.length }}개의 프로젝트</p>
        </div>
        <button @click="openCreate" class="btn btn-primary">+ 새 프로젝트</button>
      </div>

      <div v-if="loading" class="loading-box">데이터를 불러오는 중입니다...</div>

      <div v-else class="project-grid">
        <div v-for="project in projects" :key="project.id" class="project-card">
          <div class="card-thumb">
            <img v-if="project.thumbnail" :src="project.thumbnail" :alt="project.title"
              @error="e => e.target.style.display='none'" />
            <div v-else class="thumb-placeholder">No Image</div>
          </div>
          <div class="card-body">
            <div class="card-meta">
              <span class="card-period">{{ project.period }}</span>
              <span class="card-id">#{{ project.id }}</span>
            </div>
            <h3 class="card-title">{{ project.title }}</h3>
            <p class="card-desc">{{ project.description }}</p>
            <div class="card-skills">
              <span v-for="skill in (project.skills || []).slice(0, 4)" :key="skill" class="skill-badge">{{ skill }}</span>
              <span v-if="(project.skills || []).length > 4" class="skill-more">+{{ project.skills.length - 4 }}</span>
            </div>
          </div>
          <div class="card-actions">
            <button @click="openEdit(project)" class="btn btn-sm btn-outline">수정</button>
            <button @click="confirmDelete(project.id)" class="btn btn-sm btn-danger">삭제</button>
          </div>
        </div>

        <div v-if="projects.length === 0" class="empty-state">
          <p>등록된 프로젝트가 없습니다.</p>
          <button @click="openCreate" class="btn btn-primary" style="margin-top:1rem">첫 번째 프로젝트 추가하기</button>
        </div>
      </div>
    </main>

    <!-- ── 프로젝트 편집 모달 ── -->
    <div v-if="showModal" class="modal-backdrop" @click.self="closeModal">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ modalTitle }}</h3>
          <button @click="closeModal" class="modal-close">✕</button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="saveProject" id="project-form">

            <!-- 기본 정보 -->
            <div class="form-section-title">기본 정보</div>

            <div class="form-row">
              <div class="form-group">
                <label>제목 *</label>
                <input v-model="form.title" placeholder="프로젝트 제목" required />
              </div>
              <div class="form-group">
                <label>기간</label>
                <input v-model="form.period" placeholder="2024.01 ~ 2024.06 (6개월)" />
              </div>
            </div>

            <div class="form-group">
              <label>한 줄 설명 *</label>
              <input v-model="form.description" placeholder="카드에 표시되는 짧은 설명" required />
            </div>

            <div class="form-group">
              <label>상세 설명</label>
              <textarea v-model="form.longDescription" rows="4" placeholder="모달에서 표시되는 상세 설명"></textarea>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>GitHub URL</label>
                <input v-model="form.github" placeholder="https://github.com/..." type="url" />
              </div>
              <div class="form-group">
                <label>기술 스택 <span class="hint">(쉼표로 구분)</span></label>
                <input v-model="form.skills" placeholder="Java, Spring Boot, Vue.js" />
              </div>
            </div>

            <div class="form-group">
              <label>담당 역할 <span class="hint">(한 줄에 하나씩)</span></label>
              <textarea v-model="form.roles" rows="3" placeholder="백엔드 API 설계 및 구현&#10;데이터베이스 최적화"></textarea>
            </div>

            <!-- 썸네일 -->
            <div class="form-section-title" style="margin-top:1.5rem">썸네일 이미지</div>
            <div class="form-group">
              <div class="image-upload-area">
                <!-- 미리보기 -->
                <div class="thumb-preview-wrap">
                  <img v-if="form.thumbnail" :src="form.thumbnail" class="thumb-preview"
                    @error="e => e.target.style.display='none'" />
                  <div v-else class="thumb-preview-empty">미리보기 없음</div>
                </div>
                <!-- 업로드 / URL -->
                <div class="image-upload-controls">
                  <input ref="thumbnailInput" type="file" accept="image/*" style="display:none"
                    @change="onThumbnailFile" />
                  <button type="button" class="btn btn-outline btn-sm"
                    :disabled="thumbnailUploading"
                    @click="thumbnailInput.click()">
                    {{ thumbnailUploading ? '업로드 중...' : '파일 선택' }}
                  </button>
                  <span class="divider-text">또는</span>
                  <input v-model="form.thumbnail" placeholder="이미지 URL 직접 입력" class="url-input" />
                </div>
              </div>
            </div>

            <!-- 이미지 목록 -->
            <div class="form-section-title" style="margin-top:1.5rem">상세 이미지 목록</div>
            <div class="form-group">
              <!-- 현재 이미지 목록 -->
              <div v-if="form.images.length" class="image-list">
                <div v-for="(img, i) in form.images" :key="i" class="image-list-item">
                  <img :src="img" @error="e => e.target.style.display='none'" class="image-list-thumb" />
                  <span class="image-list-url">{{ img }}</span>
                  <button type="button" class="btn-remove" @click="removeImage(i)">✕</button>
                </div>
              </div>
              <p v-else class="no-images">추가된 이미지가 없습니다.</p>

              <!-- 이미지 추가 -->
              <div class="image-add-row">
                <input ref="imageInput" type="file" accept="image/*" multiple style="display:none"
                  @change="onImageFile" />
                <button type="button" class="btn btn-outline btn-sm"
                  :disabled="imageUploading"
                  @click="imageInput.click()">
                  {{ imageUploading ? '업로드 중...' : '+ 파일 업로드' }}
                </button>
                <span class="divider-text">또는</span>
                <input v-model="newImageUrl" placeholder="이미지 URL 입력 후 Enter"
                  class="url-input" @keydown.enter.prevent="addImageUrl" />
                <button type="button" class="btn btn-outline btn-sm" @click="addImageUrl">추가</button>
              </div>
            </div>

            <!-- 문제 해결 -->
            <div class="form-section-title" style="margin-top:1.5rem">문제 해결 과정</div>

            <div class="form-group">
              <label>문제 상황</label>
              <textarea v-model="form.problem" rows="2" placeholder="어떤 기술적 문제가 있었나요?"></textarea>
            </div>
            <div class="form-group">
              <label>해결 방법</label>
              <textarea v-model="form.solution" rows="2" placeholder="어떻게 해결했나요?"></textarea>
            </div>
            <div class="form-group">
              <label>결과</label>
              <textarea v-model="form.result" rows="2" placeholder="어떤 성과가 있었나요?"></textarea>
            </div>

          </form>
        </div>

        <div class="modal-footer">
          <button @click="closeModal" class="btn btn-ghost">취소</button>
          <button type="submit" form="project-form" class="btn btn-primary" :disabled="saveLoading">
            {{ saveLoading ? '저장 중...' : (editingId ? '수정 완료' : '추가 완료') }}
          </button>
        </div>
      </div>
    </div>

    <!-- ── 삭제 확인 모달 ── -->
    <div v-if="deleteConfirmId" class="modal-backdrop" @click.self="deleteConfirmId = null">
      <div class="modal modal-sm">
        <div class="modal-header">
          <h3>프로젝트 삭제</h3>
          <button @click="deleteConfirmId = null" class="modal-close">✕</button>
        </div>
        <div class="modal-body">
          <p>이 프로젝트를 삭제하시겠습니까?<br />이 작업은 되돌릴 수 없습니다.</p>
        </div>
        <div class="modal-footer">
          <button @click="deleteConfirmId = null" class="btn btn-ghost">취소</button>
          <button @click="deleteProject" class="btn btn-danger">삭제</button>
        </div>
      </div>
    </div>

    <!-- ── 토스트 ── -->
    <div v-if="toast.show" :class="['toast', `toast-${toast.type}`]">
      {{ toast.message }}
    </div>
  </div>
</template>

<style scoped>
/* ── 레이아웃 ── */
.admin {
  min-height: 100vh;
  background: #0f172a;
  color: #f1f5f9;
  font-family: 'Pretendard', -apple-system, sans-serif;
}

.admin-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #1e293b;
  border-bottom: 1px solid #334155;
}

.admin-header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.admin-logo {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  font-size: 1.1rem;
  font-weight: 700;
  color: #f1f5f9;
}

.logo-dot {
  width: 10px;
  height: 10px;
  background: #6366f1;
  border-radius: 50%;
}

.admin-header-actions {
  display: flex;
  gap: 0.75rem;
  align-items: center;
}

.admin-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2.5rem 2rem;
}

/* ── 섹션 상단 ── */
.section-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 2rem;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #f1f5f9;
}

.section-sub {
  color: #64748b;
  font-size: 0.9rem;
  margin-top: 0.2rem;
}

/* ── 프로젝트 그리드 ── */
.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.5rem;
}

.project-card {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 12px;
  overflow: hidden;
  transition: border-color 0.2s, transform 0.2s;
  display: flex;
  flex-direction: column;
}

.project-card:hover {
  border-color: #6366f1;
  transform: translateY(-2px);
}

.card-thumb {
  height: 160px;
  overflow: hidden;
  background: #0f172a;
}

.card-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #475569;
  font-size: 0.85rem;
}

.card-body {
  padding: 1.25rem;
  flex: 1;
}

.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.card-period { font-size: 0.78rem; color: #6366f1; font-weight: 600; }
.card-id { font-size: 0.75rem; color: #475569; }

.card-title {
  font-size: 1rem;
  font-weight: 700;
  color: #f1f5f9;
  margin-bottom: 0.4rem;
  line-height: 1.4;
}

.card-desc {
  font-size: 0.82rem;
  color: #94a3b8;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 0.75rem;
}

.card-skills {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
}

.skill-badge {
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 4px;
  padding: 0.15rem 0.5rem;
  font-size: 0.72rem;
  color: #94a3b8;
}

.skill-more { font-size: 0.72rem; color: #475569; align-self: center; }

.card-actions {
  padding: 0.75rem 1.25rem;
  border-top: 1px solid #334155;
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

/* ── 버튼 ── */
.btn {
  padding: 0.5rem 1.2rem;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.2s;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
}

.btn-primary { background: #6366f1; color: #fff; }
.btn-primary:hover:not(:disabled) { background: #4f46e5; }
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }

.btn-outline { background: transparent; border: 1px solid #475569; color: #94a3b8; }
.btn-outline:hover:not(:disabled) { border-color: #6366f1; color: #6366f1; }
.btn-outline:disabled { opacity: 0.5; cursor: not-allowed; }

.btn-ghost { background: transparent; color: #94a3b8; }
.btn-ghost:hover { color: #f1f5f9; }

.btn-danger { background: #ef4444; color: #fff; }
.btn-danger:hover { background: #dc2626; }

.btn-sm { padding: 0.35rem 0.85rem; font-size: 0.82rem; }

/* ── 모달 ── */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.7);
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}

.modal {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 16px;
  width: 100%;
  max-width: 720px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.5);
}

.modal-sm { max-width: 400px; }

.modal-header {
  padding: 1.5rem 1.75rem;
  border-bottom: 1px solid #334155;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.modal-header h3 { font-size: 1.15rem; font-weight: 700; color: #f1f5f9; }

.modal-close {
  background: none;
  border: none;
  color: #64748b;
  font-size: 1.1rem;
  cursor: pointer;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  transition: color 0.2s;
}

.modal-close:hover { color: #f1f5f9; }

.modal-body { padding: 1.75rem; overflow-y: auto; flex: 1; }
.modal-body p { color: #94a3b8; line-height: 1.6; }

.modal-footer {
  padding: 1.25rem 1.75rem;
  border-top: 1px solid #334155;
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  flex-shrink: 0;
}

/* ── 폼 ── */
.form-section-title {
  font-size: 0.8rem;
  font-weight: 700;
  color: #6366f1;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin-bottom: 1rem;
  padding-bottom: 0.4rem;
  border-bottom: 1px solid #334155;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
  margin-bottom: 1rem;
}

.form-group label { font-size: 0.8rem; font-weight: 600; color: #94a3b8; }
.form-group .hint { font-weight: 400; color: #475569; }

.form-group input,
.form-group textarea {
  padding: 0.65rem 0.9rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  color: #f1f5f9;
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.2s;
  resize: vertical;
  font-family: inherit;
}

.form-group input:focus,
.form-group textarea:focus { border-color: #6366f1; }

/* ── 썸네일 업로드 ── */
.image-upload-area {
  display: flex;
  gap: 1rem;
  align-items: flex-start;
}

.thumb-preview-wrap {
  width: 120px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
  background: #0f172a;
  border: 1px solid #334155;
  display: flex;
  align-items: center;
  justify-content: center;
}

.thumb-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-preview-empty {
  font-size: 0.72rem;
  color: #475569;
  text-align: center;
  padding: 0.5rem;
}

.image-upload-controls {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.divider-text {
  font-size: 0.75rem;
  color: #475569;
  text-align: center;
}

.url-input {
  flex: 1;
  padding: 0.55rem 0.8rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  color: #f1f5f9;
  font-size: 0.85rem;
  outline: none;
  transition: border-color 0.2s;
  font-family: inherit;
}

.url-input:focus { border-color: #6366f1; }

/* ── 이미지 목록 ── */
.image-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
  max-height: 200px;
  overflow-y: auto;
}

.image-list-item {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  padding: 0.4rem 0.6rem;
}

.image-list-thumb {
  width: 40px;
  height: 28px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
  background: #1e293b;
}

.image-list-url {
  flex: 1;
  font-size: 0.75rem;
  color: #94a3b8;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.btn-remove {
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  font-size: 0.85rem;
  padding: 0.2rem 0.4rem;
  border-radius: 4px;
  transition: color 0.2s;
  flex-shrink: 0;
}

.btn-remove:hover { color: #ef4444; }

.no-images {
  color: #475569;
  font-size: 0.82rem;
  text-align: center;
  padding: 1rem;
  border: 1px dashed #334155;
  border-radius: 8px;
  margin-bottom: 0.75rem;
}

.image-add-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.image-add-row .url-input { min-width: 0; flex: 1; }

/* ── 빈 상태 / 로딩 ── */
.loading-box,
.empty-state {
  text-align: center;
  padding: 5rem 2rem;
  color: #475569;
  font-size: 1rem;
}

/* ── 토스트 ── */
.toast {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  padding: 0.85rem 1.5rem;
  border-radius: 10px;
  font-size: 0.9rem;
  font-weight: 600;
  z-index: 300;
  animation: slide-up 0.3s ease;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
}

.toast-success { background: #059669; color: #fff; }
.toast-error { background: #ef4444; color: #fff; }

@keyframes slide-up {
  from { opacity: 0; transform: translateY(12px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ── 반응형 ── */
@media (max-width: 640px) {
  .form-row { grid-template-columns: 1fr; }
  .section-top { flex-direction: column; align-items: flex-start; gap: 1rem; }
  .project-grid { grid-template-columns: 1fr; }
  .image-upload-area { flex-direction: column; }
  .image-add-row { flex-direction: column; align-items: stretch; }
}
</style>
