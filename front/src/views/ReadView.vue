<script setup lang="ts">

import {onMounted, ref, provide} from "vue";
import axios from "@/axios.js";
import {ElButton, ElMessage, ElPagination} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import 'element-plus/dist/index.css';
import CommentItem from "@/views/CommentItem.vue";
import { jwtDecode } from "jwt-decode";

// 고정 사용자 아이디 (실제 서비스에서는 로그인 정보를 사용)
const userId = ref(0);
const userName = ref("");

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
  authorId: 0,
  authorName: "",
  hashtags: [] as string[], // 해시태그 배열 추가
  files: [] as { fileName: string; downloadUrl: string; isAvailable: boolean }[], // 첨부파일 배열 추가
})

// 좋아요 관련 상태
const likeCount = ref(0);
const userLiked = ref(false);

const comments = ref([] as any[]); // 댓글 목록
const commentCount = ref(0); // 전체 댓글 수
const newComment = ref(""); // 새 댓글

// 페이지네이션 관련 상태
const currentPage = ref(1);
const pageSize = ref(10);

const route = useRoute();
const router = useRouter();

// Provide 전체 댓글 목록을 자식 재귀 컴포넌트에서 사용하도록 함
provide("allComments", comments);

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
  fetchLikeCount();
  fetchUserLike();
  const token = localStorage.getItem('jwt_token');
  if (token) {
    const decoded: any = jwtDecode(token);  // JWT 디코딩
    userId.value = decoded.userId;  // 토큰에서 사용자 ID 추출
    userName.value = decoded.userName;  // 토큰에서 사용자 이름 추출
  }
});

// 좋아요 수 불러오기
const fetchLikeCount = () => {
  axios
  .get(`/api/likes/posts/${props.postId}/count`)
  .then((response) => {
    likeCount.value = response.data;
  })
  .catch((error) => {
    console.error("좋아요 수 불러오기 실패:", error);
  });
};

// 현재 사용자의 좋아요 여부 확인
const fetchUserLike = () => {
  axios
  .get(`/api/likes/posts/${props.postId}/users/${userId}`)
  .then((response) => {
    userLiked.value = true;
  })
  .catch((error) => {
    // 404 등의 오류이면 좋아요한 내역이 없다고 판단
    userLiked.value = false;
  });
};

// 좋아요/취소 토글
const toggleLike = () => {
  if (!userLiked.value) {
    axios
    .post(`/api/likes/posts/${props.postId}/users/${userId}/optimistic-lock`)
    .then(() => {
      userLiked.value = true;
      fetchLikeCount();
    })
    .catch((error) => {
      console.error("좋아요 실패:", error);
      ElMessage.error("좋아요에 실패했습니다.");
    });
  } else {
    axios
    .delete(`/api/likes/posts/${props.postId}/users/${userId}/optimistic-lock`)
    .then(() => {
      userLiked.value = false;
      fetchLikeCount();
    })
    .catch((error) => {
      console.error("좋아요 취소 실패:", error);
      ElMessage.error("좋아요 취소에 실패했습니다.");
    });
  }
};


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
    // Provide 갱신
    provide("allComments", comments.value);
  })
  .catch((error) => {
    console.error("댓글 불러오기 실패:", error);
  });
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchComments();
};

// 댓글 작성 API 호출
const postComment = (path: string | null, content: string) => {
  if (!content.trim()) {
    ElMessage.error("댓글 내용을 입력해 주세요.");
    return;
  }
  // 예시: nickname과 writerId는 고정값 (추후 로그인 정보로 대체)
  const payload = {
    postId: props.postId,
    nickname: userName.value,
    content: content,
    path: path, // 여기서 부모 댓글의 id가 전달됨.
    writerId: userId.value,
  };

  axios
  .post("/api/comments", payload)
  .then((response) => {
    ElMessage.success("댓글이 등록되었습니다.");
    // 루트 댓글의 경우 newComment 초기화, 답글의 경우 해당 입력값 초기화
    if (path === null) {
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

// 부모의 'reply' 이벤트를 받아 답글 등록 처리
const handleReply = (payload: { parentPath: string; content: string }) => {
  // payload.parentPath는 부모 댓글의 path가 됨.
  postComment(payload.parentPath, payload.content);
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

  <!-- 좋아요 영역 -->
  <div class="like-section mt-3">
    <el-button type="primary" @click="toggleLike">
      {{ userLiked ? "좋아요 취소" : "좋아요" }}
    </el-button>
    <span class="like-count"> 좋아요 {{ likeCount }}개</span>
  </div>


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

  <div class="comments mt-3">
    <h3>댓글 ({{ commentCount }})</h3>
    <ul>
      <!-- 루트 댓글: path 길이가 5인 댓글 -->
      <CommentItem
        v-for="root in comments.filter(c => c.path && c.path.length === 5)"
        :key="root.id"
        :comment="root"
        @delete="deleteComment"
        @reply="handleReply"
      />
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
    <el-button v-if="post.authorId === userId" type="warning" @click="moveToEdit">수정</el-button>
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

.like-section {
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
}
.like-count {
  font-weight: bold;
}
</style>
