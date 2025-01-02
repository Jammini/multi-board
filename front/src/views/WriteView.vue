<script setup lang="ts">
import {ref} from "vue";
import {ElButton, ElInput} from "element-plus";
import axios from 'axios';
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");
const hashtags = ref<string[]>([]);
const newHashtag = ref("");

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

const write = function () {
  axios.post("/api/posts", {
    title: title.value,
    content: content.value,
    hashtags: hashtags.value,
  })
  .then(() => {
    router.replace({name: "home"});
  })
}

</script>

<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요" />
  </div>
  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" placeholder="내용을 입력해주세요" />
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
