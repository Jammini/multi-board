<template>
  <div class="my-page">
    <h2>내 프로필</h2>
    <el-form @submit.prevent="onSubmit" :model="form" label-position="top">
      <div class="profile-header">
        <!-- 프로필 이미지: preview에 항상 이미지 URL 또는 기본 이미지가 들어있음 -->
        <el-avatar
          :src="preview"
          size="250"
          class="avatar"
        />

        <div class="photo-buttons">
          <!-- 사진이 없으면 변경 버튼, 있으면 삭제 버튼 -->
          <el-button
            v-if="removeFlag"
            size="mini"
            @click="triggerFileInput"
          >
            사진 변경
          </el-button>
          <el-button
            v-else
            size="mini"
            type="danger"
            @click="removePhoto"
          >
            사진 삭제
          </el-button>
        </div>

        <input
          ref="fileInput"
          type="file"
          class="hidden"
          @change="onFileChange"
          accept="image/*"
        />
      </div>

      <el-form-item label="닉네임">
        <el-input v-model="form.nickname" placeholder="닉네임을 입력하세요" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" native-type="submit">저장</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const API_BASE = import.meta.env.VITE_API_BASE_URL || '';
const defaultAvatar = '/images/default-avatar.png';

const form = reactive({ nickname: '', image: null as File | null });
// 삭제 플래그 (true면 사진 없음/변경 상태, false면 사진 존재/삭제 상태)
const removeFlag = ref(false);
const preview = ref<string>(defaultAvatar);
const fileInput = ref<HTMLInputElement | null>(null);

onMounted(async () => {
  try {
    const res = await axios.get('/api/users/me');
    const data = res.data.data;
    form.nickname = data.nickname;
    if (data.profileFileId) {
      preview.value = `${API_BASE}/files/preview/${data.profileFileId}`;
      removeFlag.value = false;
    } else {
      preview.value = defaultAvatar;
      removeFlag.value = true;
    }
  } catch (err) {
    console.error('내 정보 조회 실패:', err);
    router.push('/login');
  }
});

function triggerFileInput() {
  fileInput.value?.click();
}

function removePhoto() {
  form.image = null;
  preview.value = defaultAvatar;
  removeFlag.value = true;
}

function onFileChange(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0] || null;
  form.image = file;
  if (file) {
    // 업로드 전 임시 preview
    preview.value = URL.createObjectURL(file);
    removeFlag.value = false;
  }
}

async function onSubmit() {
  const formData = new FormData();
  formData.append('nickname', form.nickname);
  formData.append('removePhoto', String(removeFlag.value));
  removeFlag.value = false; // 제출 후 초기화
  if (form.image) {
    formData.append('file', form.image);
    form.image = null; // 업로드 후 초기화
  }
  try {
    await axios.put('/api/users/me/profile', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    alert('프로필이 업데이트되었습니다.');
  } catch (err: any) {
    alert(err.response?.data?.message || '오류가 발생했습니다.');
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
  position: relative;
  margin-bottom: 2rem;
}
.avatar {
  display: block;
  margin: 0 auto;
  width: 250px;
  height: 250px;
}
.avatar img {
  width: 100% !important;
  height: 100% !important;
  object-fit: cover;
}
.photo-buttons {
  margin-top: 0.5rem;
  display: flex;
  justify-content: center;
  gap: 1rem;
}
.hidden {
  display: none;
}
</style>
