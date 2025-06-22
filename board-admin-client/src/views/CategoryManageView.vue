<template>
  <div class="category-create">
    <h2>카테고리 생성</h2>
    <el-form @submit.prevent="onSubmit" :model="form" label-width="100px">
      <el-form-item label="카테고리명" prop="name" :rules="[{ required: true, message: '이름을 입력해주세요.' }]">
        <el-input v-model="form.name" placeholder="카테고리 이름" />
      </el-form-item>

      <el-form-item label="설명" prop="description">
        <el-input
            type="textarea"
            v-model="form.description"
            placeholder="간단한 설명을 입력하세요"
            :rows="3"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" native-type="submit">저장</el-button>
        <el-button @click="onCancel">취소</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const form = reactive({
  name: '',
  description: ''
});

async function onSubmit() {
  if (!form.name.trim()) {
    return alert('카테고리 이름은 필수입니다.');
  }

  try {
    await axios.post('/api/category', {
      name: form.name,
      description: form.description
    });
    alert('카테고리가 생성되었습니다.');
    // 목록 페이지로 이동하거나 필요에 따라 변경
    router.push('/');
  } catch (err: any) {
    console.error(err);
    alert(err.response?.data?.message || '카테고리 생성 중 오류가 발생했습니다.');
  }
}

function onCancel() {
  router.back();
}
</script>

<style scoped>
.category-create {
  max-width: 500px;
  margin: 2rem auto;
}
</style>
