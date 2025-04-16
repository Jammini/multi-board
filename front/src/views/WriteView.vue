<script setup lang="ts">
import {ref} from "vue";
import {ElButton, ElInput} from "element-plus";
import axios from '@/axios.js';
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");
const isSecret = ref(false);
const hashtags = ref<string[]>([]);
const newHashtag = ref("");
const files = ref<File[]>([]);
const uploadFileIds = ref<number[]>([]);

const router = useRouter();

// 해시태그 추가
const addHashtag = () => {
  const trimmedTag = newHashtag.value.trim();
  if (trimmedTag && !hashtags.value.includes(trimmedTag)) {
    hashtags.value.push(trimmedTag); // 해시태그 추가
  }
  newHashtag.value = ""; // 입력 필드 초기화
};

// 해시태그 삭제
const removeHashtag = (tag: string) => {
  hashtags.value = hashtags.value.filter((item) => item !== tag);
};

// 첨부파일 업로드
const uploadFiles = async () => {
  if (files.value.length === 0) return; // 파일이 없으면 업로드 스킵

  const formData = new FormData();
  files.value.forEach((file) => {
    formData.append("files", file);
  });

  const response = await axios.post("/api/files", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  uploadFileIds.value = response.data.data; // 업로드된 파일 ID 저장
};

// 게시글 작성 요청
const write = async () => {
  await uploadFiles(); // 첨부파일 먼저 업로드

  const postData = {
    title: title.value,
    content: content.value,
    isSecret: isSecret.value,
    hashtags: hashtags.value,
    fileIds: uploadFileIds.value, // 업로드된 파일 ID 포함
  };

  axios
  .post("/api/posts", postData)
  .then(() => {
    router.replace({ name: "home" });
  })
  .catch((error) => {
    console.error("게시글 작성 실패:", error);
  });
};

// 첨부파일 변경 시 처리
const handleChange = (uploadFileList) => {
  // 개별 파일 객체를 배열에 추가
  if (uploadFileList.raw) {
    files.value.push(uploadFileList.raw as File); // raw가 존재하면 File로 변환하여 추가
  }
};

</script>

<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요" />
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" placeholder="내용을 입력해주세요" />
  </div>
  <div class="mt-2">
    <!-- 첨부파일 업로드 -->
    <el-upload
      drag
      action=""
      :auto-upload="false"
      :file-list="files"
      multiple
      list-type="text"
      @change="handleChange"
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">여기에 파일을 드래그하거나 클릭하여 업로드하세요</div>
    </el-upload>
  </div>

  <div class="mt-2">
    <!-- 해시태그 입력 필드 -->
    <el-input
      v-model="newHashtag"
      placeholder="해시태그를 입력 후 Enter를 눌러 추가하세요"
      @keyup.enter="addHashtag"
    />
  </div>
  <div class="mt-2">
    <!-- 추가된 해시태그를 화면에 표시 -->
    <div v-if="hashtags.length > 0">
      <span v-for="tag in hashtags" :key="tag" class="mr-1">
        <el-tag closable @close="removeHashtag(tag)">
          {{ tag }}
        </el-tag>
      </span>
    </div>
  </div>
  <!-- 비밀글 체크박스 추가 -->
  <div class="mt-2">
    <el-checkbox v-model="isSecret">비밀글로 작성</el-checkbox>
  </div>

  <div class="mt-2">
    <!-- 작성 완료 버튼 -->
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">작성완료</el-button>
    </div>
  </div>

</template>

<style>
.mr-1 {
  margin-right: 5px;
}
.mt-2 {
  margin-top: 10px;
}

</style>
