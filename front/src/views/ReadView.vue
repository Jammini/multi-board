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
  <div></div>
  <div>{{post.content}}</div>
  <div></div>
  <el-button type="warning" @click="moveToEdit()">수정</el-button>
</template>

<style scoped>

</style>
