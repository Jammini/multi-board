<template>
  <div>
    <h2>회원가입</h2>
    <el-form @submit.prevent="signup">
      <el-input v-model="email" placeholder="이메일" />
      <el-input v-model="password" placeholder="비밀번호" type="password" />
      <el-input v-model="name" placeholder="이름" />
      <el-button type="success" @click="signup">회원가입</el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const email = ref('');
const password = ref('');
const name = ref('');
const router = useRouter();

const signup = async () => {
  try {
    const response = await axios.post('/api/users/signup', {
      email: email.value,
      password: password.value,
      name: name.value
    });

    const { status, message } = response.data;

    if (status === 'BAD_REQUEST') {
      alert(`❗ ${message}`);
      return; // 중단
    }

    alert('✅ 회원가입 성공!');
    router.push('/login');
  } catch (error) {
    alert('❗ 회원가입 요청 중 오류가 발생했습니다.');
    console.error(error);
  }
};
</script>
