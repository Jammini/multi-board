<template>
  <li class="comment-item" :style="depthStyle">
    <div class="comment-header">
      <strong>{{ comment.nickname }}</strong>
      <span class="comment-time">({{ relativeTime(comment.createdAt) }})</span>
      <el-button v-if="!comment.deleted" type="text" size="small" @click="$emit('delete', comment.id)">
        삭제
      </el-button>
    </div>
    <div class="comment-content" :class="{ deleted: comment.deleted }">
      <template v-if="comment.deleted">
        삭제된 댓글입니다
      </template>
      <template v-else>
        {{ comment.comment }}
      </template>
    </div>
    <el-button type="text" size="small" @click="toggleReply">
      답글
    </el-button>
    <div v-if="showReply" class="reply-input">
      <el-input
        type="textarea"
        v-model="replyText"
        placeholder="답글을 입력하세요."
        rows="2"
      />
      <el-button type="primary" size="small" @click="postReply">
        등록
      </el-button>
    </div>
    <!-- 자식 댓글 재귀적으로 렌더링 -->
    <ul class="replies" v-if="children.length">
      <CommentItem
        v-for="child in children"
        :key="child.id"
        :comment="child"
        @delete="$emit('delete', $event)"
        @reply="handleChildReply"
      />
    </ul>
  </li>
</template>

<script setup lang="ts">
import { ref, computed, inject } from "vue";
import { formatDistanceToNow } from "date-fns";
import { ko } from "date-fns/locale/ko";
import { ElButton, ElInput } from "element-plus";

const props = defineProps({
  comment: {
    type: Object,
    required: true,
  },
});

// inject 전체 댓글 reactive 참조
const allComments = inject("allComments");

// 상대 시간 계산 함수
const relativeTime = (dateStr: string) => {
  return formatDistanceToNow(new Date(dateStr), { addSuffix: true, locale: ko });
};

// 현재 댓글의 depth 계산 (depth = path.length / 5)
const depth = computed(() => {
  return props.comment.path ? props.comment.path.length / 5 : 1;
});

// 자식 댓글: 부모의 path로 시작하고, 길이가 부모의 path 길이 + 5인 경우(즉, 바로 하위 댓글)
const children = computed(() => {
  return (allComments.value || []).filter((c: any) => {
    return (
      c.path &&
      c.path.startsWith(props.comment.path) &&
      c.path.length === props.comment.path.length + 5
    );
  });
});

// depth에 따른 스타일 적용: 예를 들어 왼쪽 마진 및 border-left 추가
const depthStyle = computed(() => {
  return {
    marginLeft: `${(depth.value - 1) * 20}px`,
    borderLeft: depth.value > 1 ? "2px solid #ddd" : "none",
    paddingLeft: depth.value > 1 ? "8px" : "0"
  };
});

const showReply = ref(false);
const replyText = ref("");

const toggleReply = () => {
  showReply.value = !showReply.value;
};

const emit = defineEmits(["delete", "reply"]);

const postReply = () => {
  if (!replyText.value.trim()) return;
  emit("reply", { parentPath: props.comment.path, content: replyText.value });
  replyText.value = "";
  showReply.value = false;
};

const handleChildReply = (payload: any) => {
  emit("reply", payload);
};
</script>

<style scoped>
.comment-item {
  padding: 12px 0;
  /* 추가: depth에 따라 라인이 보이도록 기본 스타일 */
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
.deleted {
  color: gray;
  font-style: italic;
}
.reply-input {
  margin: 8px 0;
}
.replies {
  margin-top: 8px;
  list-style-type: none;
  padding: 0;
}
</style>
