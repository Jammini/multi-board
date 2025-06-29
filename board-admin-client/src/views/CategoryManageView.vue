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
      <!-- 이름 편집 -->
      <el-table-column label="이름" prop="name" min-width="240">
        <template #default="{ row }">
          <el-input
              v-model="row.title"
              @input="markEdited(row)"
          />
        </template>
      </el-table-column>

      <!-- 설명 편집 -->
      <el-table-column label="설명" prop="description" min-width="300">
        <template #default="{ row }">
          <el-input
              type="textarea"
              :rows="1"
              v-model="row.description"
              @input="markEdited(row)"
          />
        </template>
      </el-table-column>

      <!-- 첨부파일 사용 여부 -->
      <el-table-column label="첨부파일 사용" prop="attachmentsEnabled" min-width="160">
        <template #default="{ row }">
          <el-switch
              v-model="row.attachmentsEnabled"
              active-text="허용"
              inactive-text="비허용"
              @change="markEdited(row)"
          />
        </template>
      </el-table-column>

      <!-- 비밀글 사용 여부 -->
      <el-table-column label="비밀글 사용" prop="secretEnabled" min-width="160">
        <template #default="{ row }">
          <el-switch
              v-model="row.secretEnabled"
              active-text="허용"
              inactive-text="비허용"
              @change="markEdited(row)"
          />
        </template>
      </el-table-column>

      <!-- 해시태그 사용 여부 -->
      <el-table-column label="해시태그 사용" prop="hashtagsEnabled" min-width="160">
        <template #default="{ row }">
          <el-switch
              v-model="row.hashtagsEnabled"
              active-text="허용"
              inactive-text="비허용"
              @change="markEdited(row)"
          />
        </template>
      </el-table-column>

      <!-- 저장 버튼 컬럼 -->
      <el-table-column label="작업" min-width="120">
        <template #default="{ row }">
          <el-button
              size="mini"
              type="success"
              :disabled="!edited.has(row.id)"
              @click="onUpdate(row)"
          >저장</el-button>
        </template>
      </el-table-column>
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
  title: string;
  description: string;
  attachmentsEnabled: boolean;
  secretEnabled: boolean;
  hashtagsEnabled: boolean
}

const router = useRouter();

const form = reactive({
  name: '',
  description: ''
});


const categories = ref<Category[]>([]);
const edited = ref<Set<number>>(new Set());

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

function markEdited(row: Category) {
  edited.value.add(row.id);
}

async function onUpdate(row: Category) {
  try {
    await axios.put('/api/categories', {
      id: row.id,
      name: row.title,
      description: row.description,
      attachmentsEnabled: row.attachmentsEnabled,
      secretEnabled: row.secretEnabled,
      hashtagsEnabled: row.hashtagsEnabled
    });
    edited.value.delete(row.id);
  } catch (err: any) {
    console.error('업데이트 실패', err);
    alert('카테고리 업데이트 실패');
    await fetchCategories();
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
  max-width: 1200px;
  margin: 2rem auto;
}

.empty-state {
  text-align: center;
  color: #888;
  margin-top: 1rem;
}
</style>
