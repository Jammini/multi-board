<template>
  <div class="member-management">
    <h2>회원 관리</h2>
    <!-- 검색 바 -->
    <div class="search-bar">
      <el-input
          v-model="searchKeyword"
          placeholder="검색어를 입력하세요 (이메일/이름/닉네임)"
          @keyup.enter="fetchMembers"
          clearable
          style="width: 300px;"
      />
      <el-button type="primary" @click="fetchMembers">검색</el-button>
    </div>

    <!-- 회원 목록 테이블 -->
    <el-table
        :data="members"
        border
        style="width: 100%"
        v-loading="loading"
        element-loading-text="로딩 중..."
    >
      <!-- 번호 -->
      <el-table-column type="index" label="번호" width="60" />

      <!-- 회원 아이디(이메일) -->
      <el-table-column prop="email" label="회원아이디" min-width="180" />

      <!-- 이름 -->
      <el-table-column prop="name" label="이름" min-width="120" />

      <!-- 닉네임 -->
      <el-table-column prop="nickname" label="닉네임" min-width="120">
        <template #default="{ row }">
          {{ row.nickname || '-' }}
        </template>
      </el-table-column>

      <!-- 가입일자 -->
      <el-table-column prop="createdAt" label="가입일자" min-width="150">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>

      <!-- 활성화 여부 -->
      <el-table-column label="활성화 여부" prop="isActive" min-width="150">
        <template #default="{ row }">
          <el-switch
              v-model="row.isActive"
              active-text="활성"
              inactive-text="비활성"
              @change="markEdited(row)"
          />
        </template>
      </el-table-column>

      <!-- 행별 저장 버튼 -->
      <el-table-column label="비밀번호 초기화" min-width="120">
        <template #default="{ row }">
          <el-button
              size="mini"
              type="warning"
              @click="onResetPassword(row)"
          >
            초기화
          </el-button>
        </template>
      </el-table-column>


      <!-- 행별 저장 버튼 -->
      <el-table-column label="작업" fixed="right" width="100">
        <template #default="{ row }">
          <el-button
              size="mini"
              type="success"
              :disabled="!editedRows.has(row.id)"
              @click="onUpdate(row)"
          >
            저장
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 데이터 없을 때 안내 -->
    <div v-if="!members.length && !loading" class="empty-state">
      등록된 회원이 없습니다.
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import dayjs from 'dayjs';

interface Member {
  id: number;
  email: string;
  name: string;
  nickname: string | null;
  createdAt: string;
  isActive: boolean;
}

// 검색어
const searchKeyword = ref('');
// 회원 목록
const members = ref<Member[]>([]);
// 편집된 행 추적 (저장 버튼 활성화용)
const editedRows = ref<Set<number>>(new Set());
// 로딩 상태
const loading = ref(false);

// 서버에서 회원 목록 조회
async function fetchMembers() {
  loading.value = true;
  try {
    const res = await axios.get<Member[]>('/api/users', {
      params: { keyword: searchKeyword.value }
    });
    members.value = res.data;
    editedRows.value.clear();
  } catch (err) {
    console.error('회원 목록 조회 실패:', err);
    alert('회원 목록 조회에 실패했습니다.');
  } finally {
    loading.value = false;
  }
}

// 변경 감지: 해당 행 ID 저장 Set에 추가
function markEdited(row: Member) {
  editedRows.value.add(row.id);
}

// 행별 활성화 상태 업데이트
async function onUpdate(row: Member) {
  try {
    await axios.put(`/api/users/${row.id}`, {
      isActive: row.isActive
    });
    editedRows.value.delete(row.id);
  } catch (err) {
    console.error('회원 활성화 상태 업데이트 실패:', err);
    alert('변경사항 저장에 실패했습니다.');
  }
}

// 비밀번호 초기화 요청 함수 추가
async function onResetPassword(row: Member) {
  try {
    await axios.post('/client/internal/auth/password-reset/request',
        { email: row.email }
        ,  {
          headers: {
            'X-Internal-Secret': 'multiproject-secret',
          },
        });
    alert(`’${row.email}’ 계정의 비밀번호 초기화 요청이 전송되었습니다.`);
  } catch (err) {
    console.error('비밀번호 초기화 요청 실패:', err);
    alert('비밀번호 초기화 요청에 실패했습니다.');
  }
}


// 날짜 포맷팅
function formatDate(dateStr: string) {
  return dayjs(dateStr).format('YYYY-MM-DD HH:mm');
}

onMounted(fetchMembers);
</script>

<style scoped>
.member-management {
  max-width: 1000px;
  margin: 2rem auto;
}
.search-bar {
  margin-bottom: 1rem;
  display: flex;
  gap: 0.5rem;
  align-items: center;
}
.empty-state {
  text-align: center;
  color: #888;
  margin-top: 1rem;
}
</style>
