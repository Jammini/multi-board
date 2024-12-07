<script setup lang="ts">

import {onMounted, ref} from "vue";
import axios from "axios";
import {ElButton} from "element-plus";
import router from "@/router";

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
})

const moveToEdit = function () {
  router.push({name: "edit", params: {postId: props.postId}});
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data.data;
  });
});

</script>

<template>
  <h2>{{post.title}}</h2>
  <div class="sub">
    <div class="regDate">2024-12-07</div>
  </div>
  <div>{{post.content}}</div>
  <div></div>
  <div class="d-flex justify-content-end">
    <el-button type="warning" @click="moveToEdit()">수정</el-button>
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

</style>
