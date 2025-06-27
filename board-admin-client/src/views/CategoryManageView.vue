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
    <el-divider>생성된 카테고리 목록</el-divider>
    <el-table
        v-if="categories.length"
        :data="categories"
        style="width: 100%"
        border
    >
      <el-table-column prop="title" label="이름" />
      <el-table-column prop="description" label="설명" />

    </el-table>
    <div v-else class="empty-state">등록된 카테고리가 없습니다.</div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

interface Category {
  id: number;
  name: string;
  description: string;
  attachmentsEnabled: boolean;
  commentsEnabled: boolean;
}

const router = useRouter();

const form = reactive({
  name: '',
  description: ''
});


const categories = ref<Category[]>([]);

async function fetchCategories() {
  try {
    const res = await axios.get<Category[]>('/api/categories');
    categories.value = res.data;
  } catch (err) {
    console.error('카테고리 목록 조회 실패', err);
  }
}

async function onSubmit() {
  if (!form.name.trim()) {
    return alert('카테고리 이름은 필수입니다.');
  }

  try {
    await axios.post('/api/categories', {
      name: form.name,
      description: form.description
    });
    alert('카테고리가 생성되었습니다.');
    // 목록 페이지로 이동하거나 필요에 따라 변경
    form.name = '';
    form.description = '';
    await fetchCategories();
    // router.push('/');
  } catch (err: any) {
    console.error(err);
    alert(err.response?.data?.message || '카테고리 생성 중 오류가 발생했습니다.');
  }
}

function onCancel() {
  router.back();
}

async function onToggle(row: Category, field: 'attachmentsEnabled' | 'commentsEnabled') {
  try {
    await axios.patch(`/api/categories/${row.id}/settings`, {
      [field]: row[field]
    });
    // 성공하면 row 필드는 이미 v-model 로 바뀌어 있음
  } catch (err: any) {
    console.error('설정 변경 실패', err);
    alert('설정 변경에 실패했습니다.');
    // 실패 시 이전 값으로 롤백
    await fetchCategories();
  }
}

onMounted(fetchCategories);

</script>

<style scoped>
.category-create {
  max-width: 500px;
  margin: 2rem auto;
}

.empty-state {
  text-align: center;
  color: #888;
  margin-top: 1rem;
}
</style>
