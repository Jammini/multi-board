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
    post.value = response.data.data;
  });
});

</script>

<template>
  <h2>{{ post.title }}</h2>
  <div>조회수 : {{ post.viewCount }}</div>
  <div class="sub">
    <div class="regDate">2024-12-07</div>
  </div>
  <div>{{ post.content }}</div>

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
</style>
