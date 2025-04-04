<script setup lang="ts">
import axios from "@/axios.js";
import { onMounted, ref, watch } from "vue";
import { ElInput, ElPagination, ElSelect, ElOption } from "element-plus";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const input = ref((route.query.keyword as string) || "");
const searchType = ref((route.query.type as string) || "title"); // 검색 타입: title 또는 hashtag

const response = ref({
  page: Number(route.query.page) || 1,
  size: 0,
  totalCount: 0,
  items: [],
});

// 게시글 데이터 조회
const searchData = (page: number, keyword: string, type: string) => {
  const baseURL = type === "title" ? `api/posts` : `api/posts/hashtags`;
  const params = type === "title" ? { page, size: 3, keyword } : { page, size: 3, keyword };

  axios.get(baseURL, { params }).then((r) => {
    response.value = r.data.data;
  });
};


// URL 쿼리 업데이트
const updateQuery = (keyword: string, type: string) => {
  router.push({ query: { ...route.query, keyword, type } });
};

// 컴포넌트 마운트 시 데이터 로드
onMounted(() => {
  const page = Number(route.query.page) || 1;
  const keyword = (route.query.keyword as string) || "";
  const type = (route.query.type as string) || "title";
  input.value = keyword;
  searchType.value = type;
  searchData(page, keyword, type);
});

// input 또는 searchType 변경 시 데이터 로드 및 쿼리 업데이트
watch([input, searchType], ([keyword, type]) => {
  updateQuery(keyword, type);
  searchData(response.value.page, keyword, type);
});

// route 변경 시 데이터 동기화
watch(route, (newRoute) => {
  const keyword = (newRoute.query.keyword as string) || "";
  const type = (newRoute.query.type as string) || "title";
  if (keyword !== input.value || type !== searchType.value) {
    input.value = keyword;
    searchType.value = type;
    searchData(response.value.page, keyword, type);
  }
});
</script>

<template>
  <div>
    <span class="totalCount">전체 게시글 수: {{ response.totalCount }}</span>
  </div>
  <!-- 검색 결과 목록 -->
  <ul>
    <li v-for="post in response.items" :key="post.id">
      <div class="title">
        <router-link
          :to="{ name: 'read', params: { postId: post.id }, query: { keyword: input, page: response.page, type: searchType }}"
        >
          {{ post.title }}
        </router-link>
      </div>
      <div class="sub d-flex">
        <div class="regDate">2024-12-07</div>
      </div>
      <div class="sub d-flex">
        <div class="viewCount">조회 수 : {{ post.viewCount }}</div>
      </div>
      <div class="sub d-flex">
        <div class="author">작성자 : {{ post.authorName }}</div>
      </div>
    </li>
  </ul>

  <div>
    <!-- 검색 유형 선택 -->
    <el-select v-model="searchType" placeholder="검색 유형" style="margin-bottom: 10px; width: 150px;">
      <el-option label="제목" value="title" />
      <el-option label="해시태그" value="hashtag" />
    </el-select>
    <!-- 검색 입력 -->
    <el-input
      v-model="input"
      style="width: 240px; margin-bottom: 10px;"
      placeholder="검색어를 입력하세요"
    />
  </div>
  <!-- 페이지네이션 -->
  <el-pagination
    :background="true"
    v-model:current-page="response.page"
    layout="prev, pager, next"
    :total="response.totalCount"
    :default-page-size="3"
    @current-change="(page) => {
      router.push({ query: { ...route.query, page } });
      searchData(page, input, searchType);
    }"
  />
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
</style>
