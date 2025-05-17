<template>
  <div class="password-reset-confirm">
    <h2>비밀번호 재설정</h2>
    <el-form @submit.prevent="confirmReset" :model="form">
      <el-form-item label="새 비밀번호" prop="newPassword">
        <el-input
          v-model="form.newPassword"
          type="password"
          placeholder="새 비밀번호"
        />
      </el-form-item>
      <el-form-item label="비밀번호 확인" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          placeholder="비밀번호 확인"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" native-type="submit">
          비밀번호 변경
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

// URL 의 /password-reset/confirm/:token 에서 가져오는 토큰
const token = route.params.token as string;

const form = reactive({
  newPassword: '',
  confirmPassword: ''
});

const confirmReset = async () => {
  if (form.newPassword !== form.confirmPassword) {
    alert('비밀번호가 일치하지 않습니다.');
    return;
  }
  try {
    const res = await axios.post(
      `/api/auth/password-reset/confirm/${token}`,
      {
        newPassword: form.newPassword,
        confirmPassword: form.confirmPassword
      }
    );
    const { status, message } = res.data;
    if(status !== 'OK') {
      alert(`비밀번호 변경 실패: ${message}`);
      return;
    }
    alert('비밀번호가 성공적으로 변경되었습니다.');
    router.push('/login');
  } catch (err: any) {
    const msg = err.response?.data?.message || '변경 중 오류가 발생했습니다.';
    alert(msg);
  }
};
</script>

<style scoped>
.password-reset-confirm {
  max-width: 400px;
  margin: 2rem auto;
}
</style>
