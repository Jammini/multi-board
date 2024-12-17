<script setup lang="ts">
import axios from "axios";
import {onMounted, ref, watch} from "vue";
import {ElInput, ElPagination} from "element-plus";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();
const input = ref((route.query.keyword as string) || '');

const response = ref({
  page: Number(route.query.page) || 1,
  size: 0,
  totalCount: 0,
  items: [],
})

// 페이지 데이터 조회
const searchData = (page: number, keyword: string) => {
  axios.get(`/api/posts?page=${page}&keyword=${keyword}&size=3&sort=id,desc`).then((r) => {
    response.value = r.data.data;
  });
};

// URL 쿼리 업데이트
const updateQuery = (keyword: string) => {
  router.push({ query: { ...route.query, keyword } });
};

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  const page = Number(route.query.page) || 1;
  const keyword = (route.query.keyword as string) || "";
  input.value = keyword;
  searchData(page, keyword);
});

// input 변경 시 데이터 로드 및 쿼리 업데이트
watch(input, (keyword) => {
  updateQuery(keyword);
  searchData(response.value.page, keyword);
});

// route 변경 시 input 값 및 데이터 동기화
watch(route, (newRoute) => {
  const keyword = (newRoute.query.keyword as string) || "";
  if (keyword !== input.value) {
    input.value = keyword;
    searchData(response.value.page, keyword);
  }
});

</script>

<template>
  <span class="totalCount">전체 게시글 수: {{ response.totalCount }}</span>
  <ul>
    <li v-for="post in response.items" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params: {postId: post.id}, query: { keyword: input, page: response.page }}">{{ post.title }}</router-link>
      </div>

      <div class="sub d-flex">
        <div class="regDate">2024-12-09</div>
      </div>
      <div class="sub d-flex">
        <div class="author">Jammini</div>
      </div>
    </li>
  </ul>
  <el-input v-model="input" style="width: 240px" class="box" placeholder="제목 검색"/>
  <el-pagination :background="true"
                 v-model:current-page="response.page"
                 layout="prev, pager, next"
                 :total="response.totalCount"
                 :default-page-size="3"
                 @current-change="(page) => {
                  router.push({ query: { ...route.query, page } });
                  searchData(page, input);
                 }"/>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1rem;

    .title {
      a {
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
.box {
  margin-bottom: 0.5rem;
}

</style>
