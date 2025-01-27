<script setup lang="ts">

import {onMounted, ref} from "vue";
import axios from "axios";
import {ElButton} from "element-plus";
import {useRoute, useRouter} from "vue-router";

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  content: "",
  viewCount: 0,
  hashtags: [] as string[], // 해시태그 배열 추가
  files: [] as { fileName: string; downloadUrl: string; isAvailable: boolean }[], // 첨부파일 배열 추가
})

const route = useRoute();
const router = useRouter();

const moveToEdit = () =>{
  router.push({name: "edit", params: {postId: props.postId}});
}

const moveToHome = () => {
  router.push({ name: "home", query: { ...route.query } });
};

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    // 초기 파일 데이터에 isAvailable 플래그 추가
    post.value = {
      ...response.data.data,
      files: response.data.data.files.map((file) => ({
        ...file,
        isAvailable: true,
      })),
    };
  });
});

// 첨부파일 다운로드
const downloadFile = (url: string, fileName: string, fileIndex: number) => {
  console.log("Downloading file:", url); // URL 확인
  axios
  .get(url, {
    responseType: "blob", // 바이너리 데이터로 응답 받기
  })
  .then((response) => {
    if (response.status !== 200) {
      post.value.files[fileIndex].isAvailable = false;
      console.error("파일 다운로드 실패: 잘못된 응답 상태", response.status);
      alert("파일을 다운로드할 수 없습니다.");
      return;
    }

    const contentType = response.headers["content-type"];
    if (contentType && contentType.includes("application/json")) {
      post.value.files[fileIndex].isAvailable = false;
      console.error("파일 다운로드 실패: JSON 응답 수신");
      alert("파일을 다운로드할 수 없습니다.");
      return;
    }

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement("a");
    link.href = url;
    link.setAttribute("download", fileName); // 다운로드 파일명 지정
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  })
  .catch((error) => {
    console.error("파일 다운로드 실패:", error)
    alert("파일 다운로드 중 오류가 발생했습니다.");
    post.value.files[fileIndex].isAvailable = false;
  });
};

</script>

<template>
  <h2>{{ post.title }}</h2>
  <div>조회수 : {{ post.viewCount }}</div>
  <div class="sub">
    <div class="regDate">2024-12-07</div>
  </div>
  <div>{{ post.content }}</div>

  <!-- 첨부파일 목록 -->
  <div v-if="post.files.length > 0" class="attachments mt-3">
    <h3>첨부파일</h3>
    <ul>
      <li v-for="(file, index) in post.files" :key="file.fileName">
        <span
          :style="file.isAvailable ? {} : { textDecoration: 'line-through', color: 'gray' }"
        >
          <a
            v-if="file.isAvailable"
            href="javascript:void(0)"
            @click="downloadFile(file.downloadUrl, file.fileName, index)"
          >
            {{ file.fileName }}
          </a>
          <span v-else>{{ file.fileName }}</span>
        </span>
      </li>
    </ul>
  </div>


  <!-- 해시태그 목록 -->
  <div class="hashtags mt-3">
    <el-tag v-for="tag in post.hashtags" :key="tag" class="mr-2">
      {{ tag }}
    </el-tag>
  </div>

  <div class="d-flex justify-content-end">
    <el-button type="primary" @click="moveToHome">목록으로</el-button>
    <el-button type="warning" @click="moveToEdit">수정</el-button>
  </div>
</template>

<style scoped>


.sub {
  margin-top: 4px;
  font-size: 0.8rem;
}

.content {
  margin-top: 8px;
  white-space: break-spaces;
  line-height: 1.5;
}

.hashtags {
  margin-top: 12px;
}

.mr-2 {
  margin-right: 8px;
}

.mt-3 {
  margin-top: 12px;
}

.attachments {
  margin-top: 12px;
}
.attachments ul {
  padding-left: 16px;
  list-style-type: disc;
}
.attachments li {
  margin-bottom: 4px;
}
</style>
