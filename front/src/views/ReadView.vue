<script setup lang="ts">

import {onMounted, ref, computed} from "vue";
import axios from "axios";
import {ElButton, ElMessage, ElPagination} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import { formatDistanceToNow } from "date-fns";
import {ko} from "date-fns/locale/ko";
import 'element-plus/dist/index.css';

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

const comments = ref([] as any[]); // 댓글 목록
const commentCount = ref(0); // 전체 댓글 수
const newComment = ref(""); // 새 댓글

// 페이지네이션 관련 상태
const currentPage = ref(1);
const pageSize = ref(10);

const route = useRoute();
const router = useRouter();

// 상대 시간 계산 함수 (예: "3분 전")
const relativeTime = (dateStr: string) => {
  return formatDistanceToNow(new Date(dateStr), { addSuffix: true, locale: ko });
};

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
  fetchComments();
});

// 댓글 목록 조회
const fetchComments = () => {
  // 예시: 페이지 1, 사이즈 10으로 조회 (필요에 따라 변경)
  axios
  .get("/api/comments", {
    params: { postId: props.postId, page: currentPage.value, size: pageSize.value  },
  })
  .then((response) => {
    const data = response.data.data;
    comments.value = data.comment; // CommentPageResponse.comment 배열
    commentCount.value = data.commentCount;
  })
  .catch((error) => {
    console.error("댓글 불러오기 실패:", error);
  });
};

/**
 * 루트 댓글: 자신의 id와 parentCommentId가 같은 댓글
 */
const rootComments = computed(() => {
  return comments.value.filter(
    (c) => Number(c.id) === Number(c.parentCommentId)
  );
});

/**
 * 특정 댓글의 답글(대댓글) 가져오기
 */
const getReplies = (commentId: number | string) => {
  return comments.value.filter(
    (c) =>
      Number(c.parentCommentId) === Number(commentId) &&
      Number(c.id) !== Number(commentId)
  );
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchComments();
};

// 댓글 작성 API 호출
const postComment = (parentId: number | null, content: string) => {
  if (!content.trim()) {
    ElMessage.error("댓글 내용을 입력해 주세요.");
    return;
  }
  // 예시: nickname과 writerId는 고정값 (추후 로그인 정보로 대체)
  const payload = {
    postId: props.postId,
    nickname: "익명",
    content: content,
    parentCommentId: parentId, // 여기서 부모 댓글의 id가 전달됨.
    writerId: 1,
  };

  axios
  .post("/api/comments", payload)
  .then((response) => {
    ElMessage.success("댓글이 등록되었습니다.");
    // 루트 댓글의 경우 newComment 초기화, 답글의 경우 해당 입력값 초기화
    if (parentId === null) {
      newComment.value = "";
    }
    fetchComments();
  })
  .catch((error) => {
    console.error("댓글 등록 실패:", error);
    ElMessage.error("댓글 등록에 실패했습니다.");
  });
};

const deleteComment = (commentId: number) => {
  axios
  .delete(`/api/comments/${commentId}`)
  .then(() => {
    ElMessage.success("댓글이 삭제되었습니다.");
    fetchComments();
  })
  .catch((error) => {
    console.error("댓글 삭제 실패:", error);
    ElMessage.error("댓글 삭제에 실패했습니다.");
  });
};

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

  <!-- 댓글 목록 -->
  <div class="comments mt-3">
    <h3>댓글 ({{ commentCount }})</h3>
    <ul>
      <li v-for="root in rootComments" :key="root.id" class="comment-item">
        <!-- 루트 댓글 -->
        <div class="comment-header">
          <strong>{{ root.nickname }}</strong>
          <span class="comment-time">({{ relativeTime(root.createdAt) }})</span>
          <el-button
            type="text"
            size="small"
            @click="deleteComment(root.id)"
          >
            삭제
          </el-button>
        </div>
        <div class="comment-content" :class="{ deleted: root.deleted }">
          <template v-if="root.deleted">
            삭제된 댓글입니다
          </template>
          <template v-else>
            {{ root.comment }}
          </template>
        </div>
        <!-- 답글 버튼 및 입력폼 -->
        <el-button
          type="text"
          size="small"
          @click="root.showReply = !root.showReply"
        >
          답글
        </el-button>
        <div v-if="root.showReply" class="reply-input">
          <el-input
            type="textarea"
            v-model="root.replyText"
            placeholder="답글을 입력하세요."
            rows="2"
          />
          <el-button
            type="primary"
            size="small"
            @click="postComment(root.id, root.replyText)"
          >
            등록
          </el-button>
        </div>
        <!-- 대댓글 목록 -->
        <ul class="replies" v-if="getReplies(root.id).length">
          <li v-for="reply in getReplies(root.id)" :key="reply.id" class="reply-item">
            <div class="comment-header">
              <strong>{{ reply.nickname }}</strong>
              <span class="comment-time">({{ relativeTime(reply.createdAt) }})</span>
              <el-button type="text" size="small" @click="deleteComment(reply.id)">
                삭제
              </el-button>
            </div>
            <div class="comment-content" :class="{ deleted: reply.deleted }">
              <template v-if="reply.deleted">
                삭제된 댓글입니다
              </template>
              <template v-else>
                {{ reply.comment }}
              </template>
            </div>
          </li>
        </ul>
        <hr class="comment-divider" />
      </li>
    </ul>
    <!-- 페이지네이션 (댓글이 10개 이상일 경우) -->
    <el-pagination
      v-if="commentCount >= pageSize"
      background
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="commentCount"
      :current-page="currentPage"
      @current-change="handlePageChange"
    />
  </div>

  <!-- 루트 댓글 작성 폼 -->
  <div class="new-comment mt-3">
    <el-input
      type="textarea"
      v-model="newComment"
      placeholder="댓글을 입력하세요."
      rows="3"
    />
    <el-button type="primary" class="mt-2" @click="postComment(null, newComment)">
      댓글 등록
    </el-button>
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

/* 댓글 목록 스타일 */
.comments {
  margin-top: 20px;
}

.comments h3 {
  margin-bottom: 8px;
}

.comment-item {
  padding: 12px 0;
}

.comment-header {
  font-size: 0.9rem;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-time {
  color: #888;
  font-size: 0.8rem;
}

.comment-content {
  font-size: 1rem;
  line-height: 1.4;
  margin-bottom: 8px;
}

.comment-divider {
  border: none;
  border-top: 1px solid #ddd;
  margin: 0;
}

/* 답글 입력폼 */
.reply-input {
  margin: 8px 0;
}

/* 대댓글(답글) 스타일 */
.replies {
  margin-left: 20px;
  list-style-type: none;
  padding: 0;
}

.reply-item {
  padding: 8px 0;
  border-bottom: 1px dashed #ccc;
}

/* 회색 처리: 삭제된 댓글 */
.deleted {
  color: gray;
  font-style: italic;
}

</style>
