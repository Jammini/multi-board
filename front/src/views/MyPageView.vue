<template>
  <div class="my-page">
    <h2>내 프로필</h2>

    <!-- View Mode -->
    <div v-if="!isEditMode">
      <div class="profile-header">
        <el-avatar
          v-if="user.profileThumbnailUrl"
          :src="user.profileThumbnailUrl"
          size="250"
          class="avatar"
        />
        <div v-else class="avatar placeholder">
          <i class="el-icon-user" />
        </div>
      </div>

      <el-descriptions title="프로필 정보" border>
        <el-descriptions-item label="닉네임">{{ user.nickname }}</el-descriptions-item>
        <el-descriptions-item label="이메일">{{ user.email }}</el-descriptions-item>
      </el-descriptions>

      <el-button type="primary" @click="enterEditMode">프로필 변경</el-button>
    </div>

    <!-- Edit Mode -->
    <div v-else>
      <el-form @submit.prevent="onSubmit" :model="form" label-width="100px">
        <div class="profile-header">
          <el-avatar
            v-if="preview"
            :src="preview"
            size="250"
            class="avatar"
          />
          <div v-else class="avatar placeholder">
            <i class="el-icon-user" />
          </div>
        </div>

        <el-form-item label="닉네임">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="프로필 사진">
          <input type="file" @change="onFileChange" accept="image/*" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit">저장</el-button>
          <el-button type="default" @click="cancelEdit">취소</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

// 사용자 정보
const user = reactive({
  email: '',
  nickname: '',
  profileThumbnailUrl: ''
});

// 편집 모드 여부
const isEditMode = ref(false);

// 폼과 미리보기
const form = reactive({ nickname: '', image: null as File | null });
const preview = ref<string | null>(null);

// 초기 로드: 내 정보 가져오기
onMounted(async () => {
  try {
    const res = await axios.get('/api/users/me');
    const data = res.data.data;
    user.email = data.email;
    user.nickname = data.nickname;
    user.profileThumbnailUrl = data.profileFilePath;
  } catch (err) {
    console.error('내 정보 조회 실패:', err);
    router.push('/login');
  }
});

// 편집 모드 진입
function enterEditMode() {
  form.nickname = user.nickname;
  preview.value = user.profileThumbnailUrl || null;
  isEditMode.value = true;
}

// 편집 취소
function cancelEdit() {
  isEditMode.value = false;
}

// 파일 변경 핸들러
function onFileChange(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0] || null;
  form.image = file;
  preview.value = file ? URL.createObjectURL(file) : null;
}

// 프로필 업데이트 제출
async function onSubmit() {
  const data = new FormData();
  data.append('nickname', form.nickname);
  if (form.image) {
    data.append('file', form.image);
  }

  try {
    await axios.put('/api/users/me/profile', data, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    // 업데이트 후 뷰 모드로 전환 및 정보 갱신
    isEditMode.value = false;
    user.nickname = form.nickname;
    user.profileThumbnailUrl = preview.value || '';
    alert('프로필이 업데이트되었습니다.');
  } catch (err: any) {
    const msg = err.response?.data?.message || '프로필 업데이트 중 오류가 발생했습니다.';
    alert(msg);
  }
}
</script>

<style scoped>
.my-page {
  max-width: 600px;
  margin: 2rem auto;
  text-align: center;
}
.profile-header {
  margin-bottom: 2rem;
}
.avatar {
  display: block;
  margin: 0 auto;
  width: 250px;
  height: 250px;
}
.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.avatar.placeholder {
  width: 250px;
  height: 250px;
  line-height: 250px;
  background-color: #f0f0f0;
  color: #ccc;
  font-size: 3rem;
  text-align: center;
  border-radius: 50%;
}
</style>
