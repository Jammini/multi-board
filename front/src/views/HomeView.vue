<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const response = ref({
  page: 0,
  size: 0,
  totalCount: 0,
  items: [],
})

axios.get("/api/posts?page=1&size=10&sort=id,desc").then((r) => {
  response.value = r.data.data;
});

</script>

<template>
  <ul>
    <li v-for="post in response.items" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params: {postId: post.id}}">{{post.title}}</router-link>
      </div>

      <div class="sub d-flex">
        <div class="regDate">2024-12-07</div>
      </div>
      <div class="sub d-flex">
        <div class="author">Jammini</div>
      </div>
    </li>
  </ul>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1rem;

    .title {
      a{
        font-size: 1.5rem;
        text-decoration: none;
      }
    }
    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 4px;
      font-size: 0.8rem;
    }
  }

}
</style>
