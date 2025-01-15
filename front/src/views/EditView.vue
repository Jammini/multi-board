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
  files: [],
})
const newHashtag = ref(""); // 새로운 해시태그 입력 값
const newFiles = ref<File[]>([]); // 새로 추가된 파일

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

// 게시글 정보 가져오기
axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data.data;
});

// const edit = () => {
//   axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
//     router.replace({name: "home"});
//   });
// }

// 첨부파일 업로드
const uploadFiles = async () => {
  if (newFiles.value.length === 0) return; // 파일이 없으면 업로드 스킵

  const formData = new FormData();
  newFiles.value.forEach((file) => {
    formData.append("files", file);
  });

  const response = await axios.post("/api/files", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  // newFiles.value = response.data.data; // 업로드된 파일 ID 저장
  return response.data.data;
};

const edit = async () => {
  const uploadedFileIds = await uploadFiles(); // 첨부파일 먼저 업로드

  const filesToDelete = post.value.files
    .filter((file) => file.isDeleted)
    .map((file) => file.id);

  const formData = {
    title: post.value.title,
    content: post.value.content,
    hashtags: post.value.hashtags,
    fileIds: uploadedFileIds || [], // 새로 추가된 파일 ID
    filesToDelete, // 삭제할 파일 ID
  };

  axios
  .patch(`/api/posts/${post.value.id}`, formData)
  .then(() => {
    alert("수정 완료");
    router.replace({ name: "home" });
  })
  .catch((error) => {
    console.error("수정 실패:", error);
  });
  // const formData = new FormData();
  // formData.append("title", post.value.title);
  // formData.append("content", post.value.content);
  // formData.append("fileIds", post.value.files.values()); // 파일 ID 추가

  // 삭제할 파일 ID 추가
  // const filesToDelete = post.value.files.filter((file) => file.isDeleted).map((file) => file.id);
  //
  // filesToDelete.forEach((fileId) => {
  //   formData.append("filesToDelete", fileId); // 배열 형식으로 추가
  // });
  // // 새로 추가할 파일 추가
  // newFiles.value.forEach((file) => formData.append("newFiles", file));
  //
  // axios
  // .patch(`/api/posts/${post.value.id}`, formData, {
  //   headers: { "Content-Type": "multipart/form-data" },
  // })
  // .then(() => {
  //   alert("수정 완료");
  //   router.replace({name: "home"});
  // })
  // .catch((error) => {
  //   console.error("수정 실패:", error);
  // });
};

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

// 첨부파일 삭제
const deleteFile = async (fileId: number) => {
  if (!confirm("이 파일을 삭제하시겠습니까?")) return;

  try {
    await axios.delete(`/files/${fileId}`);
    post.value.files = post.value.files.filter((file) => file.id !== fileId);
    alert("파일이 삭제되었습니다.");
  } catch (error) {
    console.error("파일 삭제 실패:", error);
    alert("파일 삭제에 실패했습니다.");
  }
};

const handleNewFileChange = (uploadFileList) => {
  if (uploadFileList.raw) {
    newFiles.value.push(uploadFileList.raw as File);
  }
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
  <!-- 기존 첨부파일 -->
  <div v-if="post.files.length > 0">
    <h3>첨부파일</h3>
    <ul>
      <li v-for="file in post.files.filter((file) => !file.isDeleted)" :key="file.id">
        <span>{{ file.fileName }}</span>
        <el-button type="danger" @click="deleteFile(file.id)">삭제</el-button>
      </li>
    </ul>
  </div>

  <!-- 새로 추가할 첨부파일 -->
  <el-upload
    drag
    :file-list="newFiles"
    :auto-upload="false"
    multiple
    list-type="text"
    @change="handleNewFileChange"
  >
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">여기에 파일을 드래그하거나 클릭하여 업로드</div>
  </el-upload>

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
