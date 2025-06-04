<template>
  <div class="password-reset-request">
    <h2>비밀번호 찾기</h2>
    <el-form @submit.prevent="requestReset" :model="form">
      <el-form-item label="이메일" prop="email">
        <el-input v-model="form.email" placeholder="이메일을 입력하세요" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" native-type="submit">이메일로 링크 받기</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const form = reactive({
  email: ''
});

const requestReset = async () => {
  if (!form.email) {
    alert('이메일을 입력해 주세요.');
    return;
  }
  try {
    await axios.post('/api/auth/password-reset/request', { email: form.email });
    alert('비밀번호 재설정 링크를 이메일로 발송했습니다. 메일함을 확인하세요.');
    router.push('/login');
  } catch (err: any) {
    const msg = err.response?.data?.message || '요청 중 오류가 발생했습니다.';
    alert(msg);
  }
};
</script>

<style scoped>
.password-reset-request {
  max-width: 400px;
  margin: 2rem auto;
}
</style>
