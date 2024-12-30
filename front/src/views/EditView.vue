<script setup lang="ts">
import {ref} from "vue";
import {ElButton, ElInput} from "element-plus";
import axios from 'axios';
import {useRouter} from "vue-router";

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: "",
  hashtags: [] as string[], // 해시태그 추가
})
const newHashtag = ref(""); // 새로운 해시태그 입력 값

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({name: "home"});
  });
}

// 해시태그 삭제
const removeHashtag = (tag: string) => {
  post.value.hashtags = post.value.hashtags.filter((item) => item !== tag);
};

// 해시태그 추가
const addHashtag = () => {
  const trimmedTag = newHashtag.value.trim();
  if (trimmedTag && !post.value.hashtags.includes(trimmedTag)) {
    post.value.hashtags.push(trimmedTag); // 해시태그 추가
  }
  newHashtag.value = ""; // 입력 필드 초기화
};

</script>

<template>
  <div>
    <el-input v-model="post.title" />
  </div>
  <div></div>
  <div class="mt-2">
    <div>
      <el-input v-model="post.content" type="textarea" rows="15"/>
    </div>
  </div>
  <div class="mb-3">
    <el-input
      v-model="newHashtag"
      placeholder="해시태그를 입력 후 Enter를 눌러 추가하세요"
      @keyup.enter="addHashtag"
      class="mb-2"
    />
    <div>
      <el-tag
        v-for="tag in post.hashtags"
        :key="tag"
        closable
        @close="removeHashtag(tag)"
        class="mr-2"
      >
        {{ tag }}
      </el-tag>
    </div>
  </div>
  <div class="mt-2 d-flex justify-content-end">
    <el-button type="warning" @click="edit()">수정완료</el-button>
  </div>

</template>

<style>

</style>
