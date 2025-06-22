<template>
  <el-header class="header">
    <div class="header-container">
      <div class="menu-left">
        <RouterLink to="/" class="nav-link">Home</RouterLink>
        <RouterLink to="/category-manage" class="nav-link">카테고리 관리</RouterLink>
        <RouterLink to="/mypage" class="nav-link">MyPage</RouterLink>
      </div>

      <div>
        <!-- 로그인 안 된 상태 -->
        <template v-if="!isTokenExist">
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
import {ref, onMounted, onBeforeUnmount} from 'vue';
import { useRouter } from 'vue-router';
import { eventBus } from '@/stores/eventBus';
import { ElButton } from 'element-plus';
import { jwtDecode } from "jwt-decode";

const router = useRouter();
const isTokenExist = ref(false);
const userEmail = ref('');
const userName = ref('');

// 로그인 여부 확인 API
const checkLogin = async () => {
  const token = localStorage.getItem('jwt_token');
  if (!token) {
    isTokenExist.value = false;
    return;
  }

  try {
    // JWT 토큰 디코딩하여 사용자 정보 추출
    const decodedToken = jwtDecode<any>(token);  // JWT 토큰 디코딩

    // exp 값 확인 (만료 시간을 확인)
    const currentTime = Math.floor(Date.now() / 1000); // 현재 시간 (초 단위)
    if (decodedToken.exp < currentTime) {
      // 토큰이 만료되었으면 로그아웃 처리
      logout();
      return;
    }

    userEmail.value = decodedToken.sub;  // 'sub'는 이메일
    userName.value = decodedToken.name;  // 'name'은 사용자 닉네임
    isTokenExist.value = true;
  } catch (e) {
    isTokenExist.value = false;
  }
};

// 로그아웃 요청
const logout = async () => {
  try {
    localStorage.removeItem('jwt_token'); // localStorage에서 JWT 토큰 제거
    checkLogin(); // 로그아웃 후 로그인 상태 갱신
    eventBus.emit('logout'); // 로그아웃 이벤트 발생
    router.push('/'); // 홈으로 리디렉션
  } catch (e) {
    alert('로그아웃 실패');
    console.error(e);
  }
};

// 컴포넌트가 마운트되면 로그인 상태 확인
onMounted(() => {
  checkLogin();
  eventBus.on('login', checkLogin); // 로그인 이벤트가 발생하면 로그인 상태 갱신
  eventBus.on('logout', checkLogin); // 로그아웃 시도 시 로그인 상태 갱신
});

onBeforeUnmount(() => {
  eventBus.off('login', checkLogin); // 이벤트 리스너 해제
  eventBus.off('logout', checkLogin);
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
