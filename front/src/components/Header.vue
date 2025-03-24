<template>
  <el-header class="header">
    <!--    <RouterLink to="/">Home</RouterLink>-->
    <!--    <RouterLink to="/write">글 작성</RouterLink>-->
    <div class="header-container">
      <div class="menu-left">
        <RouterLink to="/" class="nav-link">Home</RouterLink>
        <RouterLink to="/write" class="nav-link">글 작성</RouterLink>
      </div>

      <div>
        <!-- 로그인 안 된 상태 -->
        <template v-if="!isLogin">
          <router-link to="/login">
            <el-button type="primary" plain>로그인</el-button>
          </router-link>
          <router-link to="/signup">
            <el-button type="success" plain>회원가입</el-button>
          </router-link>
        </template>
        <!-- 로그인 된 상태 -->
        <template v-else>
          <span class="mr-2">{{ userName }} 님 환영합니다! </span>
          <el-button type="danger" plain @click="logout">로그아웃</el-button>
        </template>
      </div>

    </div>
  </el-header>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import {eventBus} from "@/stores/eventBus";

const router = useRouter();
const isLogin = ref(false);
const userEmail = ref('');
const userName = ref('');
// 로그인 여부 확인 API
const checkLogin = async () => {
  try {
    const res = await axios.get('/api/users/me', {
      withCredentials: true
    });
    userEmail.value = res.data.data.email;
    userName.value = res.data.data.name;
    isLogin.value = true;
  } catch (e) {
    isLogin.value = false;
  }
};

// 로그아웃 요청
const logout = async () => {
  try {
    await axios.post('/api/logout');
    isLogin.value = false;
    router.push('/');
    eventBus.emit('logout');
    alert('로그아웃 완료');
  } catch (e) {
    alert('로그아웃 실패');
    console.error(e);
  }
};

onMounted(() => {
  checkLogin();
  eventBus.on('login', checkLogin); // 로그인 이벤트 수신 시 checkLogin 실행
  eventBus.on('logout', checkLogin); // 로그아웃 시도 마찬가지
});
</script>

<style scoped>
.header {
  padding: 0 20px;
  height: 60px;
  background-color: #f8f8f8;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}


.menu-left {
  display: flex;
  gap: 20px;
}

.nav-link {
  font-weight: bold;
  color: #333;
  text-decoration: none;
  font-size: 16px;
}

.nav-link.router-link-active {
  color: #409EFF; /* Element Plus primary color */
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

</style>
