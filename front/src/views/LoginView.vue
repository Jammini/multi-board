<template>
  <div>
    <h2>로그인</h2>
    <el-form @submit.prevent="login">
      <el-input v-model="email" placeholder="이메일" />
      <el-input v-model="password" placeholder="비밀번호" type="password" />
      <el-button type="primary" native-type="submit">로그인</el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import {eventBus} from "@/stores/eventBus";

const userStore = useUserStore();
const email = ref('');
const password = ref('');
const router = useRouter();

const login = async () => {
  try {
    await axios.post('/api/login', {
      email: email.value,
      password: password.value
    }, {
      withCredentials: true
    });
    eventBus.emit('login');
    userStore.login(email.value); // 로그인 상태 저장
    alert('로그인 성공!');
    router.push('/');
  } catch (error: any) {
    const message =
      error.response?.data?.message || '로그인 중 알 수 없는 오류가 발생했습니다.';
    alert(`로그인 실패: ${message}`);
    console.error('로그인 실패:', error);
  }
};
</script>
