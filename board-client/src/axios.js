import axios from 'axios';

// Axios 요청 인터셉터 설정
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token'); // localStorage에서 JWT 토큰 가져오기

    // 토큰이 존재하면 Authorization 헤더에 Bearer 토큰 추가
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axios;
