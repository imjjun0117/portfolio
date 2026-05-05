<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// ── 공통 ──────────────────────────────────────────
const activeTab = ref('projects')
const toast = ref({ show: false, message: '', type: 'success' })
const token = () => localStorage.getItem('admin_token')
const authHeaders = () => ({ headers: { Authorization: `Bearer ${token()}` } })

const showToast = (message, type = 'success') => {
  toast.value = { show: true, message, type }
  setTimeout(() => { toast.value.show = false }, 3000)
}

const handleUnauthorized = () => {
  localStorage.removeItem('admin_token')
  router.push('/admin')
}

const logout = () => {
  localStorage.removeItem('admin_token')
  router.push('/admin')
}

// ── 파일 업로드 ──────────────────────────────────
const uploadFile = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  const res = await axios.post('/api/admin/upload', formData, {
    headers: { Authorization: `Bearer ${token()}`, 'Content-Type': 'multipart/form-data' }
  })
  return res.data.url
}

// ════════════════════════════════════════════════
// 프로젝트 탭
// ════════════════════════════════════════════════
const projects = ref([])
const projectsLoading = ref(true)
const showProjectModal = ref(false)
const editingProjectId = ref(null)
const saveProjectLoading = ref(false)
const deleteProjectConfirmId = ref(null)
const thumbnailInput = ref(null)
const thumbnailUploading = ref(false)

const emptyProjectForm = () => ({
  title: '', description: '',
  startDate: '', endDate: '', ongoing: false,
  github: '', thumbnail: '', skills: '', vibeCoding: false,
  screens: []
})

// ── 스크린 관리 ──────────────────────────────────
const screenImageInputs = ref([])
const screenImageUploading = ref([])

const emptyScreen = () => ({
  name: '', description: '', image: '',
  features: '', roles: '', techs: '', highlights: ''
})

const addScreen = () => {
  projectForm.value.screens.push(emptyScreen())
  screenImageUploading.value.push(false)
}

const removeScreen = (i) => {
  projectForm.value.screens.splice(i, 1)
  screenImageUploading.value.splice(i, 1)
}

const moveScreenUp = (i) => {
  if (i === 0) return
  const arr = projectForm.value.screens
  ;[arr[i - 1], arr[i]] = [arr[i], arr[i - 1]]
}

const moveScreenDown = (i) => {
  const arr = projectForm.value.screens
  if (i === arr.length - 1) return
  ;[arr[i], arr[i + 1]] = [arr[i + 1], arr[i]]
}

const onScreenImageFile = async (e, i) => {
  const file = e.target.files[0]; if (!file) return
  screenImageUploading.value[i] = true
  try {
    projectForm.value.screens[i].image = await uploadFile(file)
    showToast('스크린 이미지가 업로드되었습니다.')
  } catch { showToast('이미지 업로드에 실패했습니다.', 'error') }
  finally { screenImageUploading.value[i] = false; e.target.value = '' }
}

const screensToPayload = (screens) => screens.map((s, i) => ({
  name: s.name, description: s.description, image: s.image, displayOrder: i,
  features: s.features ? s.features.split('\n').map(x => x.trim()).filter(Boolean) : [],
  roles: s.roles ? s.roles.split('\n').map(x => x.trim()).filter(Boolean) : [],
  techs: s.techs ? s.techs.split(',').map(x => x.trim()).filter(Boolean) : [],
  highlights: s.highlights ? s.highlights.split('\n').map(x => x.trim()).filter(Boolean) : []
}))
const projectForm = ref(emptyProjectForm())
const projectModalTitle = computed(() => editingProjectId.value ? '프로젝트 수정' : '프로젝트 추가')

const fetchProjects = async () => {
  projectsLoading.value = true
  try {
    const res = await axios.get('/api/projects?all=true')
    projects.value = res.data
  } catch { showToast('데이터를 불러오지 못했습니다.', 'error') }
  finally { projectsLoading.value = false }
}

const openCreateProject = () => {
  editingProjectId.value = null
  projectForm.value = emptyProjectForm()
  showProjectModal.value = true
}

const openEditProject = (p) => {
  editingProjectId.value = p.id
  const parts = (p.period || '').split(' ~ ')
  projectForm.value = {
    title: p.title || '', description: p.description || '',
    startDate: parseToInputMonth(parts[0] || ''),
    endDate: parseToInputMonth(parts[1] || ''),
    ongoing: (parts[1] || '') === '현재',
    github: p.github || '', thumbnail: p.thumbnail || '',
    skills: (p.skills || []).join(', '), vibeCoding: p.vibeCoding || false,
    screens: (p.screens || []).map(s => ({
      name: s.name || '', description: s.description || '', image: s.image || '',
      features: (s.features || []).join('\n'),
      roles: (s.roles || []).join('\n'),
      techs: (s.techs || []).join(', '),
      highlights: (s.highlights || []).join('\n')
    }))
  }
  screenImageUploading.value = (p.screens || []).map(() => false)
  showProjectModal.value = true
}

const onThumbnailFile = async (e) => {
  const file = e.target.files[0]; if (!file) return
  thumbnailUploading.value = true
  try { projectForm.value.thumbnail = await uploadFile(file); showToast('썸네일이 업로드되었습니다.') }
  catch { showToast('썸네일 업로드에 실패했습니다.', 'error') }
  finally { thumbnailUploading.value = false; e.target.value = '' }
}

const projectDuration = computed(() =>
  calcDurationStr(projectForm.value.startDate, projectForm.value.endDate, projectForm.value.ongoing)
)
watch(() => projectForm.value.ongoing, (val) => {
  if (val) projectForm.value.endDate = ''
})

const saveProject = async () => {
  saveProjectLoading.value = true
  try {
    const { startDate, endDate, ongoing } = projectForm.value
    const period = startDate
      ? `${formatMonth(startDate)} ~ ${ongoing ? '현재' : formatMonth(endDate)}`
      : ''
    const payload = {
      title: projectForm.value.title, description: projectForm.value.description, period,
      github: projectForm.value.github, thumbnail: projectForm.value.thumbnail,
      skills: projectForm.value.skills.split(',').map(s => s.trim()).filter(Boolean),
      vibeCoding: projectForm.value.vibeCoding,
      screens: screensToPayload(projectForm.value.screens)
    }
    if (editingProjectId.value) {
      await axios.put(`/api/projects/${editingProjectId.value}`, payload, authHeaders())
      showToast('프로젝트가 수정되었습니다.')
    } else {
      await axios.post('/api/projects', payload, authHeaders())
      showToast('프로젝트가 추가되었습니다.')
    }
    showProjectModal.value = false; editingProjectId.value = null
    await fetchProjects()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('저장에 실패했습니다.', 'error')
  } finally { saveProjectLoading.value = false }
}

const toggleProjectHidden = async (project) => {
  try {
    await axios.put(`/api/projects/${project.id}`, { ...project,
      skills: project.skills, hidden: !project.hidden
    }, authHeaders())
    await fetchProjects()
    showToast(project.hidden ? '공개로 변경되었습니다.' : '숨김 처리되었습니다.')
  } catch { showToast('변경에 실패했습니다.', 'error') }
}

const deleteProject = async () => {
  try {
    await axios.delete(`/api/projects/${deleteProjectConfirmId.value}`, authHeaders())
    deleteProjectConfirmId.value = null
    showToast('프로젝트가 삭제되었습니다.'); await fetchProjects()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('삭제에 실패했습니다.', 'error')
  }
}

// ════════════════════════════════════════════════
// 날짜 헬퍼
// ════════════════════════════════════════════════
// "YYYY-MM" → "YYYY.MM"
const formatMonth = (yyyymm) => {
  if (!yyyymm) return ''
  const [y, m] = yyyymm.split('-')
  return `${y}.${m}`
}

// "YYYY.MM" → "YYYY-MM"  (input[type=month] value format)
const parseToInputMonth = (dot) => {
  if (!dot || dot === '현재') return ''
  return dot.replace('.', '-')   // "2024.05" → "2024-05"
}

// startYYYYMM, endYYYYMM: "YYYY-MM" strings; current: boolean
const calcDurationStr = (startYYYYMM, endYYYYMM, current) => {
  if (!startYYYYMM) return ''
  const [sy, sm] = startYYYYMM.split('-').map(Number)
  let ey, em
  if (current || !endYYYYMM) {
    const now = new Date()
    ey = now.getFullYear(); em = now.getMonth() + 1
  } else {
    ;[ey, em] = endYYYYMM.split('-').map(Number)
  }
  const total = (ey - sy) * 12 + (em - sm) + 1   // inclusive months
  if (total <= 0) return current ? '재직 중' : '1개월 미만'
  const years = Math.floor(total / 12)
  const rem = total % 12
  let str = ''
  if (years === 0) str = `${rem}개월`
  else if (rem === 0) str = `${years}년`
  else str = `${years}년 ${rem}개월`
  return current ? `약 ${str}` : str
}

// ════════════════════════════════════════════════
// 경력 탭
// ════════════════════════════════════════════════
const experiences = ref([])
const expLoading = ref(true)
const showExpModal = ref(false)
const editingExpId = ref(null)
const saveExpLoading = ref(false)
const deleteExpConfirmId = ref(null)

const emptyExpForm = () => ({
  company: '', startDate: '', endDate: '', role: '', type: '',
  description: '', tags: '', current: false, displayOrder: 0, hidden: false
})
const expForm = ref(emptyExpForm())
const expModalTitle = computed(() => editingExpId.value ? '경력 수정' : '경력 추가')

// 날짜가 바뀔 때마다 재직 기간 자동 계산 (표시용)
const expAutoEndDate = ref('')   // endDate 표시 (현재 체크시 오늘 기준)
const expDuration = computed(() =>
  calcDurationStr(expForm.value.startDate, expForm.value.endDate, expForm.value.current)
)
// 현재 체크 시 endDate 비우기
watch(() => expForm.value.current, (val) => {
  if (val) expForm.value.endDate = ''
})

const fetchExperiences = async () => {
  expLoading.value = true
  try { const res = await axios.get('/api/experiences?all=true'); experiences.value = res.data }
  catch { showToast('경력 데이터를 불러오지 못했습니다.', 'error') }
  finally { expLoading.value = false }
}

const openCreateExp = () => {
  editingExpId.value = null; expForm.value = emptyExpForm(); showExpModal.value = true
}

const openEditExp = (exp) => {
  editingExpId.value = exp.id
  // period like "2024.05 ~ 현재" or "2022.08 ~ 2023.09"
  const parts = (exp.period || '').split(' ~ ')
  expForm.value = {
    company: exp.company || '',
    startDate: parseToInputMonth(parts[0] || ''),
    endDate: parseToInputMonth(parts[1] || ''),
    role: exp.role || '', type: exp.type || '', description: exp.description || '',
    tags: (exp.tags || []).join(', '), current: exp.current || false, displayOrder: exp.displayOrder || 0,
    hidden: exp.hidden || false
  }
  showExpModal.value = true
}

const saveExp = async () => {
  saveExpLoading.value = true
  try {
    const { startDate, endDate, current } = expForm.value
    const period = startDate
      ? `${formatMonth(startDate)} ~ ${current ? '현재' : formatMonth(endDate)}`
      : ''
    const duration = calcDurationStr(startDate, endDate, current)
    const payload = {
      company: expForm.value.company, period, duration,
      role: expForm.value.role, type: expForm.value.type, description: expForm.value.description,
      tags: expForm.value.tags.split(',').map(s => s.trim()).filter(Boolean),
      current: expForm.value.current, displayOrder: Number(expForm.value.displayOrder),
      hidden: expForm.value.hidden
    }
    if (editingExpId.value) {
      await axios.put(`/api/experiences/${editingExpId.value}`, payload, authHeaders())
      showToast('경력이 수정되었습니다.')
    } else {
      await axios.post('/api/experiences', payload, authHeaders())
      showToast('경력이 추가되었습니다.')
    }
    showExpModal.value = false; editingExpId.value = null; await fetchExperiences()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('저장에 실패했습니다.', 'error')
  } finally { saveExpLoading.value = false }
}

const toggleExpHidden = async (exp) => {
  try {
    await axios.put(`/api/experiences/${exp.id}`, { ...exp,
      tags: exp.tags, hidden: !exp.hidden
    }, authHeaders())
    await fetchExperiences()
    showToast(exp.hidden ? '공개로 변경되었습니다.' : '숨김 처리되었습니다.')
  } catch { showToast('변경에 실패했습니다.', 'error') }
}

const deleteExp = async () => {
  try {
    await axios.delete(`/api/experiences/${deleteExpConfirmId.value}`, authHeaders())
    deleteExpConfirmId.value = null; showToast('경력이 삭제되었습니다.'); await fetchExperiences()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('삭제에 실패했습니다.', 'error')
  }
}

// ════════════════════════════════════════════════
// 기술 스택 탭
// ════════════════════════════════════════════════
const skillGroups = ref([])
const skillsLoading = ref(true)
const showSkillModal = ref(false)
const editingSkillId = ref(null)
const saveSkillLoading = ref(false)
const deleteSkillConfirmId = ref(null)

const emptySkillForm = () => ({
  name: '', icon: '', color: '#3b82f6', skills: '', displayOrder: 0
})
const skillForm = ref(emptySkillForm())
const skillModalTitle = computed(() => editingSkillId.value ? '기술 그룹 수정' : '기술 그룹 추가')

const fetchSkillGroups = async () => {
  skillsLoading.value = true
  try { const res = await axios.get('/api/skills'); skillGroups.value = res.data }
  catch { showToast('기술 데이터를 불러오지 못했습니다.', 'error') }
  finally { skillsLoading.value = false }
}

const openCreateSkill = () => {
  editingSkillId.value = null; skillForm.value = emptySkillForm(); showSkillModal.value = true
}

const openEditSkill = (sg) => {
  editingSkillId.value = sg.id
  skillForm.value = {
    name: sg.name || '', icon: sg.icon || '', color: sg.color || '#3b82f6',
    skills: (sg.skills || []).join(', '), displayOrder: sg.displayOrder || 0
  }
  showSkillModal.value = true
}

const saveSkill = async () => {
  saveSkillLoading.value = true
  try {
    const payload = {
      name: skillForm.value.name, icon: skillForm.value.icon, color: skillForm.value.color,
      skills: skillForm.value.skills.split(',').map(s => s.trim()).filter(Boolean),
      displayOrder: Number(skillForm.value.displayOrder)
    }
    if (editingSkillId.value) {
      await axios.put(`/api/skills/${editingSkillId.value}`, payload, authHeaders())
      showToast('기술 그룹이 수정되었습니다.')
    } else {
      await axios.post('/api/skills', payload, authHeaders())
      showToast('기술 그룹이 추가되었습니다.')
    }
    showSkillModal.value = false; editingSkillId.value = null; await fetchSkillGroups()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('저장에 실패했습니다.', 'error')
  } finally { saveSkillLoading.value = false }
}

const deleteSkill = async () => {
  try {
    await axios.delete(`/api/skills/${deleteSkillConfirmId.value}`, authHeaders())
    deleteSkillConfirmId.value = null; showToast('기술 그룹이 삭제되었습니다.'); await fetchSkillGroups()
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('삭제에 실패했습니다.', 'error')
  }
}

// ════════════════════════════════════════════════
// 사이트 설정 탭
// ════════════════════════════════════════════════
const siteConfig = ref({})
const configLoading = ref(true)
const saveConfigLoading = ref(false)
const configForm = ref({})

const CONFIG_LABELS = {
  'hero.badge': '뱃지 텍스트',
  'hero.name': '이름',
  'hero.description': '소개 문구',
  'hero.stat1.value': '통계1 값',
  'hero.stat1.label': '통계1 라벨',
  'hero.stat2.value': '통계2 값',
  'hero.stat2.label': '통계2 라벨',
  'hero.stat3.value': '통계3 값',
  'hero.stat3.label': '통계3 라벨',
  'hero.githubUrl': 'GitHub URL',
  'hero.email': '이메일',
  'contact.email': '연락처 이메일',
  'contact.github': '연락처 GitHub URL',
}

const fetchSiteConfig = async () => {
  configLoading.value = true
  try {
    const res = await axios.get('/api/config')
    siteConfig.value = res.data
    configForm.value = { ...res.data }
  } catch { showToast('설정을 불러오지 못했습니다.', 'error') }
  finally { configLoading.value = false }
}

const saveSiteConfig = async () => {
  saveConfigLoading.value = true
  try {
    await axios.put('/api/config', configForm.value, authHeaders())
    siteConfig.value = { ...configForm.value }
    showToast('설정이 저장되었습니다.')
  } catch (e) {
    if (e.response?.status === 401) handleUnauthorized()
    else showToast('저장에 실패했습니다.', 'error')
  } finally { saveConfigLoading.value = false }
}

// ── 탭 전환 시 데이터 로드 ────────────────────────
const switchTab = (tab) => {
  activeTab.value = tab
  if (tab === 'projects' && projects.value.length === 0) fetchProjects()
  if (tab === 'experiences' && experiences.value.length === 0) fetchExperiences()
  if (tab === 'skills' && skillGroups.value.length === 0) fetchSkillGroups()
  if (tab === 'config' && Object.keys(siteConfig.value).length === 0) fetchSiteConfig()
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

    <!-- 탭 네비게이션 -->
    <nav class="tab-nav">
      <div class="tab-nav-inner">
        <button
          v-for="tab in [
            { key: 'projects', label: '프로젝트' },
            { key: 'experiences', label: '경력' },
            { key: 'skills', label: '기술 스택' },
            { key: 'config', label: '사이트 설정' },
          ]"
          :key="tab.key"
          :class="['tab-btn', { active: activeTab === tab.key }]"
          @click="switchTab(tab.key)"
        >{{ tab.label }}</button>
      </div>
    </nav>

    <main class="admin-main">

      <!-- ════ 프로젝트 탭 ════ -->
      <div v-if="activeTab === 'projects'">
        <div class="section-top">
          <div>
            <h2 class="section-title">프로젝트 관리</h2>
            <p class="section-sub">총 {{ projects.length }}개의 프로젝트</p>
          </div>
          <button @click="openCreateProject" class="btn btn-primary">+ 새 프로젝트</button>
        </div>

        <div v-if="projectsLoading" class="loading-box">데이터를 불러오는 중입니다...</div>
        <div v-else class="project-grid">
          <div v-for="project in projects" :key="project.id" class="project-card" :class="{ 'card-hidden': project.hidden }">
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
              <button @click="toggleProjectHidden(project)" class="btn btn-sm" :class="project.hidden ? 'btn-warning' : 'btn-outline'">
                {{ project.hidden ? '숨김 해제' : '숨김' }}
              </button>
              <button @click="openEditProject(project)" class="btn btn-sm btn-outline">수정</button>
              <button @click="deleteProjectConfirmId = project.id" class="btn btn-sm btn-danger">삭제</button>
            </div>
          </div>
          <div v-if="projects.length === 0" class="empty-state">
            <p>등록된 프로젝트가 없습니다.</p>
            <button @click="openCreateProject" class="btn btn-primary" style="margin-top:1rem">첫 번째 프로젝트 추가하기</button>
          </div>
        </div>
      </div>

      <!-- ════ 경력 탭 ════ -->
      <div v-if="activeTab === 'experiences'">
        <div class="section-top">
          <div>
            <h2 class="section-title">경력 관리</h2>
            <p class="section-sub">총 {{ experiences.length }}개의 경력</p>
          </div>
          <button @click="openCreateExp" class="btn btn-primary">+ 경력 추가</button>
        </div>

        <div v-if="expLoading" class="loading-box">데이터를 불러오는 중입니다...</div>
        <div v-else class="list-stack">
          <div v-for="exp in experiences" :key="exp.id" class="list-card" :class="{ 'card-hidden': exp.hidden }">
            <div class="list-card-body">
              <div class="list-card-top">
                <div>
                  <div style="display:flex;align-items:center;gap:0.6rem">
                    <h3 class="list-title">{{ exp.company }}</h3>
                    <span v-if="exp.current" class="badge-current">재직중</span>
                  </div>
                  <div class="list-sub">{{ exp.role }} · {{ exp.type }}</div>
                </div>
                <div style="text-align:right;flex-shrink:0">
                  <div class="list-period">{{ exp.period }}</div>
                  <div class="list-duration">{{ exp.duration }}</div>
                </div>
              </div>
              <p class="list-desc">{{ exp.description }}</p>
              <div class="tag-row">
                <span v-for="tag in exp.tags" :key="tag" class="skill-badge">{{ tag }}</span>
              </div>
            </div>
            <div class="card-actions">
              <button @click="toggleExpHidden(exp)" class="btn btn-sm" :class="exp.hidden ? 'btn-warning' : 'btn-outline'">
                {{ exp.hidden ? '숨김 해제' : '숨김' }}
              </button>
              <button @click="openEditExp(exp)" class="btn btn-sm btn-outline">수정</button>
              <button @click="deleteExpConfirmId = exp.id" class="btn btn-sm btn-danger">삭제</button>
            </div>
          </div>
          <div v-if="experiences.length === 0" class="empty-state">
            <p>등록된 경력이 없습니다.</p>
          </div>
        </div>
      </div>

      <!-- ════ 기술 스택 탭 ════ -->
      <div v-if="activeTab === 'skills'">
        <div class="section-top">
          <div>
            <h2 class="section-title">기술 스택 관리</h2>
            <p class="section-sub">총 {{ skillGroups.length }}개의 그룹</p>
          </div>
          <button @click="openCreateSkill" class="btn btn-primary">+ 그룹 추가</button>
        </div>

        <div v-if="skillsLoading" class="loading-box">데이터를 불러오는 중입니다...</div>
        <div v-else class="skills-admin-grid">
          <div v-for="sg in skillGroups" :key="sg.id" class="skill-admin-card">
            <div class="skill-admin-header">
              <span class="skill-icon">{{ sg.icon }}</span>
              <h3 class="skill-name" :style="{ color: sg.color }">{{ sg.name }}</h3>
              <span class="skill-order">#{{ sg.displayOrder }}</span>
            </div>
            <div class="skill-chips">
              <span v-for="s in sg.skills" :key="s" class="skill-chip" :style="{ borderColor: sg.color + '44', color: sg.color }">{{ s }}</span>
            </div>
            <div class="card-actions" style="padding:0.75rem 1.25rem;border-top:1px solid #334155">
              <button @click="openEditSkill(sg)" class="btn btn-sm btn-outline">수정</button>
              <button @click="deleteSkillConfirmId = sg.id" class="btn btn-sm btn-danger">삭제</button>
            </div>
          </div>
          <div v-if="skillGroups.length === 0" class="empty-state" style="grid-column:1/-1">
            <p>등록된 기술 그룹이 없습니다.</p>
          </div>
        </div>
      </div>

      <!-- ════ 사이트 설정 탭 ════ -->
      <div v-if="activeTab === 'config'">
        <div class="section-top">
          <div>
            <h2 class="section-title">사이트 설정</h2>
            <p class="section-sub">Hero 섹션 및 연락처 정보 관리</p>
          </div>
          <button @click="saveSiteConfig" class="btn btn-primary" :disabled="saveConfigLoading">
            {{ saveConfigLoading ? '저장 중...' : '전체 저장' }}
          </button>
        </div>

        <div v-if="configLoading" class="loading-box">데이터를 불러오는 중입니다...</div>
        <div v-else class="config-form">
          <div class="config-section">
            <div class="config-section-title">Hero 섹션</div>
            <div class="config-grid">
              <template v-for="key in ['hero.badge','hero.name','hero.githubUrl','hero.email','hero.stat1.value','hero.stat1.label','hero.stat2.value','hero.stat2.label','hero.stat3.value','hero.stat3.label']" :key="key">
                <div class="form-group">
                  <label>{{ CONFIG_LABELS[key] || key }}</label>
                  <input v-model="configForm[key]" :placeholder="key" />
                </div>
              </template>
              <div class="form-group config-full">
                <label>소개 문구</label>
                <textarea v-model="configForm['hero.description']" rows="3" placeholder="소개 문구"></textarea>
              </div>
            </div>
          </div>

          <div class="config-section">
            <div class="config-section-title">연락처</div>
            <div class="config-grid">
              <div class="form-group">
                <label>{{ CONFIG_LABELS['contact.email'] }}</label>
                <input v-model="configForm['contact.email']" placeholder="이메일" />
              </div>
              <div class="form-group">
                <label>{{ CONFIG_LABELS['contact.github'] }}</label>
                <input v-model="configForm['contact.github']" placeholder="GitHub URL" />
              </div>
            </div>
          </div>
        </div>
      </div>

    </main>

    <!-- ════ 프로젝트 모달 ════ -->
    <div v-if="showProjectModal" class="modal-backdrop" @click.self="showProjectModal = false">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ projectModalTitle }}</h3>
          <button @click="showProjectModal = false" class="modal-close">✕</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveProject" id="project-form">
            <div class="form-section-title">기본 정보</div>
            <div class="form-row">
              <div class="form-group">
                <label>제목 *</label>
                <input v-model="projectForm.title" placeholder="프로젝트 제목" required />
              </div>
              <div class="form-group">
                <label>바이브코딩 여부</label>
                <label class="vibe-check">
                  <input type="checkbox" v-model="projectForm.vibeCoding" />
                  <span>AI 바이브코딩으로 개발된 프로젝트</span>
                </label>
              </div>
            </div>
            <div class="form-row">
              <div class="form-group" style="grid-column: 1 / -1">
                <label>기간</label>
                <div class="date-range-row">
                  <input type="month" v-model="projectForm.startDate" class="month-input" />
                  <span class="date-sep">~</span>
                  <input type="month" v-model="projectForm.endDate" class="month-input"
                    :disabled="projectForm.ongoing" />
                  <label class="current-check">
                    <input type="checkbox" v-model="projectForm.ongoing" />
                    진행 중
                  </label>
                </div>
                <div v-if="projectDuration" class="duration-badge">{{ projectDuration }}</div>
              </div>
            </div>
            <div class="form-group">
              <label>한 줄 설명 *</label>
              <input v-model="projectForm.description" placeholder="카드에 표시되는 짧은 설명" required />
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>GitHub URL</label>
                <input v-model="projectForm.github" placeholder="https://github.com/..." type="url" />
              </div>
              <div class="form-group">
                <label>기술 스택 <span class="hint">(쉼표로 구분)</span></label>
                <input v-model="projectForm.skills" placeholder="Java, Spring Boot, Vue.js" />
              </div>
            </div>

            <div class="form-section-title" style="margin-top:1.5rem">썸네일 이미지</div>
            <div class="form-group">
              <div class="image-upload-area">
                <div class="thumb-preview-wrap">
                  <img v-if="projectForm.thumbnail" :src="projectForm.thumbnail" class="thumb-preview"
                    @error="e => e.target.style.display='none'" />
                  <div v-else class="thumb-preview-empty">미리보기 없음</div>
                </div>
                <div class="image-upload-controls">
                  <input ref="thumbnailInput" type="file" accept="image/*" style="display:none" @change="onThumbnailFile" />
                  <button type="button" class="btn btn-outline btn-sm" :disabled="thumbnailUploading" @click="thumbnailInput.click()">
                    {{ thumbnailUploading ? '업로드 중...' : '파일 선택' }}
                  </button>
                  <span class="divider-text">또는</span>
                  <input v-model="projectForm.thumbnail" placeholder="이미지 URL 직접 입력" class="url-input" />
                </div>
              </div>
            </div>

            <!-- 화면 뷰어 섹션 -->
            <div class="form-section-title" style="margin-top:1.5rem">화면 뷰어 <span class="hint">( 등록 시 PPT형 3패널 레이아웃으로 표시)</span></div>
            <div v-for="(screen, si) in projectForm.screens" :key="si" class="screen-editor-card">
              <div class="screen-editor-header">
                <span class="screen-editor-num">화면 {{ si + 1 }}</span>
                <div class="screen-editor-actions">
                  <button type="button" class="btn-icon" @click="moveScreenUp(si)" :disabled="si === 0" title="위로">↑</button>
                  <button type="button" class="btn-icon" @click="moveScreenDown(si)" :disabled="si === projectForm.screens.length - 1" title="아래로">↓</button>
                  <button type="button" class="btn-icon btn-icon-danger" @click="removeScreen(si)" title="삭제">✕</button>
                </div>
              </div>
              <div class="screen-editor-body">
                <div class="form-row">
                  <div class="form-group">
                    <label>화면 이름</label>
                    <input v-model="screen.name" placeholder="로그인 화면" />
                  </div>
                  <div class="form-group">
                    <label>이미지</label>
                    <div style="display:flex;gap:0.5rem;align-items:center">
                      <input :ref="el => screenImageInputs[si] = el" type="file" accept="image/*" style="display:none"
                        @change="e => onScreenImageFile(e, si)" />
                      <button type="button" class="btn btn-outline btn-sm"
                        :disabled="screenImageUploading[si]"
                        @click="screenImageInputs[si]?.click()">
                        {{ screenImageUploading[si] ? '업로드 중...' : '파일 선택' }}
                      </button>
                      <input v-model="screen.image" placeholder="또는 URL 입력" class="url-input" style="flex:1" />
                    </div>
                    <img v-if="screen.image" :src="screen.image" class="screen-img-preview"
                      @error="e => e.target.style.display='none'" />
                  </div>
                </div>
                <div class="form-group">
                  <label>설명</label>
                  <textarea v-model="screen.description" rows="2" placeholder="이 화면에 대한 설명"></textarea>
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>구현 기능 <span class="hint">(줄바꿈으로 구분)</span></label>
                    <textarea v-model="screen.features" rows="3" placeholder="로그인 기능&#10;JWT 토큰 발급"></textarea>
                  </div>
                  <div class="form-group">
                    <label>담당 역할 <span class="hint">(줄바꿈으로 구분)</span></label>
                    <textarea v-model="screen.roles" rows="3" placeholder="API 설계&#10;프론트엔드 구현"></textarea>
                  </div>
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>사용 기술 <span class="hint">(쉼표로 구분)</span></label>
                    <input v-model="screen.techs" placeholder="Spring Security, JWT" />
                  </div>
                  <div class="form-group">
                    <label>특이사항 <span class="hint">(줄바꿈으로 구분)</span></label>
                    <textarea v-model="screen.highlights" rows="2" placeholder="RefreshToken 자동 갱신 구현"></textarea>
                  </div>
                </div>
              </div>
            </div>
            <button type="button" class="btn btn-outline btn-sm" style="margin-top:0.75rem;width:100%" @click="addScreen">
              + 화면 추가
            </button>
          </form>
        </div>
        <div class="modal-footer">
          <button @click="showProjectModal = false" class="btn btn-ghost">취소</button>
          <button type="submit" form="project-form" class="btn btn-primary" :disabled="saveProjectLoading">
            {{ saveProjectLoading ? '저장 중...' : (editingProjectId ? '수정 완료' : '추가 완료') }}
          </button>
        </div>
      </div>
    </div>

    <!-- ════ 경력 모달 ════ -->
    <div v-if="showExpModal" class="modal-backdrop" @click.self="showExpModal = false">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ expModalTitle }}</h3>
          <button @click="showExpModal = false" class="modal-close">✕</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveExp" id="exp-form">
            <div class="form-row">
              <div class="form-group">
                <label>회사명 *</label>
                <input v-model="expForm.company" placeholder="(주)회사명" required />
              </div>
              <div class="form-group">
                <label>표시 순서</label>
                <input v-model="expForm.displayOrder" type="number" placeholder="1" />
              </div>
            </div>
            <!-- 근무 기간 -->
            <div class="form-section-title">근무 기간</div>
            <div class="form-group" style="flex-direction:row;align-items:center;gap:0.75rem;margin-bottom:0.75rem">
              <input type="checkbox" v-model="expForm.current" id="current-check" style="width:16px;height:16px;cursor:pointer;flex-shrink:0" />
              <label for="current-check" style="margin:0;cursor:pointer;font-size:0.9rem;color:#f1f5f9">현재 재직 중</label>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>시작 월 *</label>
                <input type="month" v-model="expForm.startDate" required />
              </div>
              <div class="form-group">
                <label>종료 월</label>
                <input type="month" v-model="expForm.endDate"
                  :disabled="expForm.current"
                  :style="expForm.current ? 'opacity:0.4;cursor:not-allowed' : ''"
                  :placeholder="expForm.current ? '현재 재직 중' : '종료 월 선택'" />
              </div>
            </div>
            <div class="form-group" v-if="expDuration">
              <label>재직 기간 (자동 계산)</label>
              <div class="duration-display">{{ expDuration }}</div>
            </div>
            <!-- 역할 / 유형 -->
            <div class="form-section-title" style="margin-top:0.5rem">직책 정보</div>
            <div class="form-row">
              <div class="form-group">
                <label>직급</label>
                <input v-model="expForm.role" placeholder="개발팀 대리" />
              </div>
              <div class="form-group">
                <label>업무 유형</label>
                <input v-model="expForm.type" placeholder="SI 개발" />
              </div>
            </div>
            <div class="form-group">
              <label>업무 내용</label>
              <textarea v-model="expForm.description" rows="3" placeholder="담당한 주요 업무를 입력하세요."></textarea>
            </div>
            <div class="form-group">
              <label>기술 태그 <span class="hint">(쉼표로 구분)</span></label>
              <input v-model="expForm.tags" placeholder="Java, Spring Boot, Oracle" />
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button @click="showExpModal = false" class="btn btn-ghost">취소</button>
          <button type="submit" form="exp-form" class="btn btn-primary" :disabled="saveExpLoading">
            {{ saveExpLoading ? '저장 중...' : (editingExpId ? '수정 완료' : '추가 완료') }}
          </button>
        </div>
      </div>
    </div>

    <!-- ════ 기술 그룹 모달 ════ -->
    <div v-if="showSkillModal" class="modal-backdrop" @click.self="showSkillModal = false">
      <div class="modal modal-sm">
        <div class="modal-header">
          <h3>{{ skillModalTitle }}</h3>
          <button @click="showSkillModal = false" class="modal-close">✕</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="saveSkill" id="skill-form">
            <div class="form-row">
              <div class="form-group">
                <label>그룹명 *</label>
                <input v-model="skillForm.name" placeholder="Backend" required />
              </div>
              <div class="form-group">
                <label>아이콘 (이모지)</label>
                <input v-model="skillForm.icon" placeholder="⚙️" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>색상 (HEX)</label>
                <div style="display:flex;gap:0.5rem;align-items:center">
                  <input type="color" v-model="skillForm.color" style="width:40px;height:36px;padding:2px;border-radius:6px;border:1px solid #334155;background:#0f172a;cursor:pointer" />
                  <input v-model="skillForm.color" placeholder="#3b82f6" style="flex:1" />
                </div>
              </div>
              <div class="form-group">
                <label>표시 순서</label>
                <input v-model="skillForm.displayOrder" type="number" placeholder="1" />
              </div>
            </div>
            <div class="form-group">
              <label>기술 목록 <span class="hint">(쉼표로 구분)</span></label>
              <textarea v-model="skillForm.skills" rows="3" placeholder="Java, Spring Boot, JPA"></textarea>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button @click="showSkillModal = false" class="btn btn-ghost">취소</button>
          <button type="submit" form="skill-form" class="btn btn-primary" :disabled="saveSkillLoading">
            {{ saveSkillLoading ? '저장 중...' : (editingSkillId ? '수정 완료' : '추가 완료') }}
          </button>
        </div>
      </div>
    </div>

    <!-- ════ 삭제 확인 모달들 ════ -->
    <div v-if="deleteProjectConfirmId" class="modal-backdrop" @click.self="deleteProjectConfirmId = null">
      <div class="modal modal-sm">
        <div class="modal-header">
          <h3>프로젝트 삭제</h3>
          <button @click="deleteProjectConfirmId = null" class="modal-close">✕</button>
        </div>
        <div class="modal-body"><p>이 프로젝트를 삭제하시겠습니까?<br />이 작업은 되돌릴 수 없습니다.</p></div>
        <div class="modal-footer">
          <button @click="deleteProjectConfirmId = null" class="btn btn-ghost">취소</button>
          <button @click="deleteProject" class="btn btn-danger">삭제</button>
        </div>
      </div>
    </div>

    <div v-if="deleteExpConfirmId" class="modal-backdrop" @click.self="deleteExpConfirmId = null">
      <div class="modal modal-sm">
        <div class="modal-header">
          <h3>경력 삭제</h3>
          <button @click="deleteExpConfirmId = null" class="modal-close">✕</button>
        </div>
        <div class="modal-body"><p>이 경력을 삭제하시겠습니까?</p></div>
        <div class="modal-footer">
          <button @click="deleteExpConfirmId = null" class="btn btn-ghost">취소</button>
          <button @click="deleteExp" class="btn btn-danger">삭제</button>
        </div>
      </div>
    </div>

    <div v-if="deleteSkillConfirmId" class="modal-backdrop" @click.self="deleteSkillConfirmId = null">
      <div class="modal modal-sm">
        <div class="modal-header">
          <h3>기술 그룹 삭제</h3>
          <button @click="deleteSkillConfirmId = null" class="modal-close">✕</button>
        </div>
        <div class="modal-body"><p>이 기술 그룹을 삭제하시겠습니까?</p></div>
        <div class="modal-footer">
          <button @click="deleteSkillConfirmId = null" class="btn btn-ghost">취소</button>
          <button @click="deleteSkill" class="btn btn-danger">삭제</button>
        </div>
      </div>
    </div>

    <!-- 토스트 -->
    <div v-if="toast.show" :class="['toast', `toast-${toast.type}`]">{{ toast.message }}</div>
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

.admin-header-actions { display: flex; gap: 0.75rem; align-items: center; }

/* ── 탭 네비게이션 ── */
.tab-nav {
  background: #1e293b;
  border-bottom: 1px solid #334155;
  position: sticky;
  top: 64px;
  z-index: 99;
}

.tab-nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  gap: 0;
}

.tab-btn {
  padding: 0.85rem 1.4rem;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  color: #64748b;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: color 0.2s, border-color 0.2s;
  font-family: inherit;
}

.tab-btn:hover { color: #94a3b8; }
.tab-btn.active { color: #6366f1; border-bottom-color: #6366f1; }

/* ── 메인 ── */
.admin-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2.5rem 2rem;
}

.section-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 2rem;
}

.section-title { font-size: 1.8rem; font-weight: 700; color: #f1f5f9; }
.section-sub { color: #64748b; font-size: 0.9rem; margin-top: 0.2rem; }

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

.project-card:hover { border-color: #6366f1; transform: translateY(-2px); }

.card-thumb { height: 160px; overflow: hidden; background: #0f172a; }
.card-thumb img { width: 100%; height: 100%; object-fit: cover; }
.thumb-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; color: #475569; font-size: 0.85rem; }

.card-body { padding: 1.25rem; flex: 1; }
.card-meta { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
.card-period { font-size: 0.78rem; color: #6366f1; font-weight: 600; }
.card-id { font-size: 0.75rem; color: #475569; }
.card-title { font-size: 1rem; font-weight: 700; color: #f1f5f9; margin-bottom: 0.4rem; line-height: 1.4; }
.card-desc { font-size: 0.82rem; color: #94a3b8; line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; margin-bottom: 0.75rem; }
.card-skills { display: flex; flex-wrap: wrap; gap: 0.4rem; }

/* ── 경력 목록 ── */
.list-stack { display: flex; flex-direction: column; gap: 1rem; }

.list-card {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 12px;
  overflow: hidden;
  transition: border-color 0.2s;
}

.list-card:hover { border-color: #6366f1; }
.list-card-body { padding: 1.5rem; }

.list-card-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  margin-bottom: 0.75rem;
}

.list-title { font-size: 1.05rem; font-weight: 700; color: #f1f5f9; }
.list-sub { font-size: 0.85rem; color: #94a3b8; margin-top: 0.25rem; }
.list-period { font-size: 0.85rem; color: #6366f1; font-weight: 600; }
.list-duration { font-size: 0.78rem; color: #475569; margin-top: 0.2rem; }
.list-desc { font-size: 0.88rem; color: #94a3b8; line-height: 1.6; margin-bottom: 1rem; }
.badge-current { font-size: 0.68rem; font-weight: 700; padding: 0.2rem 0.55rem; border-radius: 999px; background: rgba(99,102,241,0.12); color: #6366f1; border: 1px solid rgba(99,102,241,0.3); }

.tag-row { display: flex; flex-wrap: wrap; gap: 0.4rem; }

/* ── 기술 스택 그리드 ── */
.skills-admin-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.25rem;
}

.skill-admin-card {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 12px;
  overflow: hidden;
  transition: border-color 0.2s;
}

.skill-admin-card:hover { border-color: #6366f1; }

.skill-admin-header {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  padding: 1.25rem 1.25rem 0.75rem;
}

.skill-icon { font-size: 1.3rem; }
.skill-name { font-size: 1rem; font-weight: 700; flex: 1; }
.skill-order { font-size: 0.72rem; color: #475569; }

.skill-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
  padding: 0 1.25rem 1.25rem;
}

.skill-chip {
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.2rem 0.6rem;
  border-radius: 6px;
  border: 1px solid;
  background: transparent;
}

/* ── 사이트 설정 ── */
.config-form { display: flex; flex-direction: column; gap: 2rem; }
.config-section { background: #1e293b; border: 1px solid #334155; border-radius: 12px; padding: 1.75rem; }
.config-section-title { font-size: 0.78rem; font-weight: 700; color: #6366f1; text-transform: uppercase; letter-spacing: 0.08em; margin-bottom: 1.25rem; padding-bottom: 0.4rem; border-bottom: 1px solid #334155; }
.config-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
.config-full { grid-column: 1 / -1; }

/* ── 공통 배지/칩 ── */
.skill-badge { background: #0f172a; border: 1px solid #334155; border-radius: 4px; padding: 0.15rem 0.5rem; font-size: 0.72rem; color: #94a3b8; }
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
  font-family: inherit;
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
.btn-warning { background: #f59e0b; color: #fff; }
.btn-warning:hover { background: #d97706; }
.card-hidden { opacity: 0.45; border-style: dashed !important; }
.btn-sm { padding: 0.35rem 0.85rem; font-size: 0.82rem; }

/* ── 모달 ── */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.7);
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
  box-shadow: 0 25px 60px rgba(0,0,0,0.5);
}

.modal-sm { max-width: 480px; }

.modal-header {
  padding: 1.5rem 1.75rem;
  border-bottom: 1px solid #334155;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.modal-header h3 { font-size: 1.15rem; font-weight: 700; color: #f1f5f9; }
.modal-close { background: none; border: none; color: #64748b; font-size: 1.1rem; cursor: pointer; padding: 0.25rem 0.5rem; border-radius: 4px; transition: color 0.2s; }
.modal-close:hover { color: #f1f5f9; }
.modal-body { padding: 1.75rem; overflow-y: auto; flex: 1; }
.modal-body p { color: #94a3b8; line-height: 1.6; }
.modal-footer { padding: 1.25rem 1.75rem; border-top: 1px solid #334155; display: flex; justify-content: flex-end; gap: 0.75rem; flex-shrink: 0; }

/* ── 폼 ── */
.form-section-title { font-size: 0.8rem; font-weight: 700; color: #6366f1; text-transform: uppercase; letter-spacing: 0.08em; margin-bottom: 1rem; padding-bottom: 0.4rem; border-bottom: 1px solid #334155; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
.form-group { display: flex; flex-direction: column; gap: 0.35rem; margin-bottom: 1rem; }
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

/* ── 이미지 업로드 ── */
.image-upload-area { display: flex; gap: 1rem; align-items: flex-start; }
.thumb-preview-wrap { width: 120px; height: 80px; flex-shrink: 0; border-radius: 8px; overflow: hidden; background: #0f172a; border: 1px solid #334155; display: flex; align-items: center; justify-content: center; }
.thumb-preview { width: 100%; height: 100%; object-fit: cover; }
.thumb-preview-empty { font-size: 0.72rem; color: #475569; text-align: center; padding: 0.5rem; }
.image-upload-controls { flex: 1; display: flex; flex-direction: column; gap: 0.5rem; }
.divider-text { font-size: 0.75rem; color: #475569; text-align: center; }
.url-input { flex: 1; padding: 0.55rem 0.8rem; background: #0f172a; border: 1px solid #334155; border-radius: 8px; color: #f1f5f9; font-size: 0.85rem; outline: none; transition: border-color 0.2s; font-family: inherit; }
.url-input:focus { border-color: #6366f1; }
.image-list { display: flex; flex-direction: column; gap: 0.5rem; margin-bottom: 0.75rem; max-height: 200px; overflow-y: auto; }
.image-list-item { display: flex; align-items: center; gap: 0.6rem; background: #0f172a; border: 1px solid #334155; border-radius: 8px; padding: 0.4rem 0.6rem; }
.image-list-thumb { width: 40px; height: 28px; object-fit: cover; border-radius: 4px; flex-shrink: 0; background: #1e293b; }
.image-list-url { flex: 1; font-size: 0.75rem; color: #94a3b8; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.btn-remove { background: none; border: none; color: #64748b; cursor: pointer; font-size: 0.85rem; padding: 0.2rem 0.4rem; border-radius: 4px; transition: color 0.2s; flex-shrink: 0; }
.btn-remove:hover { color: #ef4444; }
.no-images { color: #475569; font-size: 0.82rem; text-align: center; padding: 1rem; border: 1px dashed #334155; border-radius: 8px; margin-bottom: 0.75rem; }

.duration-display {
  padding: 0.65rem 0.9rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  color: #6366f1;
  font-size: 0.95rem;
  font-weight: 700;
}

/* ── 바이브코딩 체크 ── */
.vibe-check {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.65rem 0.9rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  cursor: pointer;
  transition: border-color 0.2s;
}
.vibe-check:hover { border-color: #8b5cf6; }
.vibe-check input[type=checkbox] {
  width: 15px !important;
  height: 15px !important;
  padding: 0 !important;
  background: none !important;
  border: none !important;
  cursor: pointer;
  flex-shrink: 0;
  accent-color: #8b5cf6;
}
.vibe-check span { font-size: 0.88rem; color: #94a3b8; }

/* ── 날짜 범위 picker ── */
.date-range-row {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  flex-wrap: wrap;
}
.month-input {
  flex: 1;
  min-width: 130px;
  padding: 0.65rem 0.9rem;
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 8px;
  color: #f1f5f9;
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.2s;
  font-family: inherit;
  color-scheme: dark;
}
.month-input:focus { border-color: #6366f1; }
.month-input:disabled { opacity: 0.4; cursor: not-allowed; }
.date-sep { color: #64748b; font-size: 0.9rem; flex-shrink: 0; }
.current-check {
  display: flex;
  align-items: center;
  gap: 0.35rem;
  font-size: 0.85rem;
  color: #94a3b8;
  cursor: pointer;
  white-space: nowrap;
  flex-shrink: 0;
}
.current-check input[type=checkbox] {
  width: 15px !important;
  height: 15px !important;
  padding: 0 !important;
  background: none !important;
  border: none !important;
  cursor: pointer;
  flex-shrink: 0;
}
.duration-badge {
  display: inline-block;
  margin-top: 0.45rem;
  padding: 0.2rem 0.7rem;
  background: rgba(99, 102, 241, 0.12);
  border: 1px solid rgba(99, 102, 241, 0.3);
  border-radius: 999px;
  font-size: 0.78rem;
  font-weight: 700;
  color: #6366f1;
}
.image-add-row { display: flex; align-items: center; gap: 0.5rem; flex-wrap: wrap; }
.image-add-row .url-input { min-width: 0; flex: 1; }

/* ── 화면 뷰어 에디터 ── */
.screen-editor-card {
  background: #0f172a;
  border: 1px solid #334155;
  border-radius: 10px;
  margin-bottom: 1rem;
  overflow: hidden;
}

.screen-editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.65rem 1rem;
  background: #1e293b;
  border-bottom: 1px solid #334155;
}

.screen-editor-num {
  font-size: 0.8rem;
  font-weight: 700;
  color: #6366f1;
}

.screen-editor-actions {
  display: flex;
  gap: 0.35rem;
}

.btn-icon {
  background: transparent;
  border: 1px solid #334155;
  color: #94a3b8;
  width: 28px;
  height: 28px;
  border-radius: 6px;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.15s;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-family: inherit;
}

.btn-icon:hover:not(:disabled) { border-color: #6366f1; color: #6366f1; }
.btn-icon:disabled { opacity: 0.3; cursor: not-allowed; }
.btn-icon-danger:hover:not(:disabled) { border-color: #ef4444; color: #ef4444; }

.screen-editor-body { padding: 1rem; }

.screen-img-preview {
  margin-top: 0.5rem;
  width: 100%;
  max-height: 120px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #334155;
}

/* ── 빈 상태 / 로딩 ── */
.loading-box, .empty-state { text-align: center; padding: 5rem 2rem; color: #475569; font-size: 1rem; }

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
  box-shadow: 0 8px 24px rgba(0,0,0,0.3);
}

.toast-success { background: #059669; color: #fff; }
.toast-error { background: #ef4444; color: #fff; }

@keyframes slide-up {
  from { opacity: 0; transform: translateY(12px); }
  to   { opacity: 1; transform: translateY(0); }
}

/* ── 반응형 ── */
@media (max-width: 640px) {
  .form-row, .config-grid { grid-template-columns: 1fr; }
  .section-top { flex-direction: column; align-items: flex-start; gap: 1rem; }
  .project-grid, .skills-admin-grid { grid-template-columns: 1fr; }
  .image-upload-area { flex-direction: column; }
  .image-add-row { flex-direction: column; align-items: stretch; }
  .tab-btn { padding: 0.75rem 0.9rem; font-size: 0.82rem; }
}
</style>
