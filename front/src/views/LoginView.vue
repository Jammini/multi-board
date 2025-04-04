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
import {eventBus} from "@/stores/eventBus";
const email = ref('');
const password = ref('');
const router = useRouter();

const login = async () => {
  try {
    const response = await axios.post('/api/login', {
      email: email.value,
      password: password.value
    }, {
      withCredentials: true // 쿠키를 요청에 포함시킴
    });

    // 서버에서 Authorization 헤더로 JWT 토큰을 받아 localStorage에 저장
    const token = response.headers['authorization'].split(' ')[1]; // "Bearer <token>" 형식에서 토큰 추출
    localStorage.setItem('jwt_token', token); // localStorage에 JWT 저장

    eventBus.emit('login');

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
