<template>
  <div class="post-stats">
    <h2>📊 게시글 통계</h2>

    <!-- 시간별 통계 -->
    <section class="hourly-stats">
      <h3>🕒 시간별 통계</h3>
      <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="날짜 선택"
          @change="loadHourlyStats"
      />
      <v-chart :option="hourlyChartOptions" style="height: 400px; margin-top: 20px;" />
    </section>

    <!-- 일자별 통계 -->
    <section class="daily-stats" style="margin-top: 40px;">
      <h3>📅 일자별 통계</h3>
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="~"
          start-placeholder="시작일"
          end-placeholder="종료일"
          @change="loadDailyStats"
      />
      <v-chart :option="dailyChartOptions" style="height: 400px; margin-top: 20px;" />
    </section>

    <!-- 주간 통계 -->
    <section class="weekly-stats" style="margin-top: 40px;">
      <h3>📈 주간 통계</h3>
      <el-select v-model="selectedWeek" @change="fetchWeeklyStats">
        <el-option v-for="n in 4" :key="n - 1" :label="`${n - 1}주 전`" :value="n - 1" />
      </el-select>
      <div v-if="weeklyStats" style="margin-top: 10px;">
        <p><strong>{{ weeklyStats.startDate }} ~ {{ weeklyStats.endDate }}</strong></p>
        <p><strong>총 게시글 수:</strong> {{ weeklyStats.totalPostCount }}</p>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import dayjs from 'dayjs'

const selectedDate = ref(null)
const dateRange = ref([])
const selectedWeek = ref(0)

const hourlyChartOptions = ref({})
const dailyChartOptions = ref({})
const weeklyStats = ref(null)

const loadHourlyStats = async () => {
  if (!selectedDate.value) return

  const res = await axios.get('/api/admin/stats/posts/hourly', {
    params: {
      date: dayjs(selectedDate.value).format('YYYY-MM-DD')
    }
  })

  hourlyChartOptions.value = {
    title: {
      text: '시간대별 게시글 비율',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}시: {c}건 ({d}%)'
    },
    legend: {
      bottom: '0%',
      left: 'center'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        data: res.data.map(item => ({
          name: `${item.hour}`,
          value: item.postCount
        })),
        label: {
          formatter: '{b}시'
        }
      }
    ]
  }
}

const loadDailyStats = async () => {
  if (!dateRange.value || dateRange.value.length < 2) return

  const [start, end] = dateRange.value

  const res = await axios.get('/api/admin/stats/posts/daily', {
    params: {
      start: dayjs(start).format('YYYY-MM-DD'),
      end: dayjs(end).format('YYYY-MM-DD')
    }
  })

  dailyChartOptions.value = {
    title: {
      text: '일자별 게시글 수',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: res.data.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '게시글 수',
        type: 'bar',
        data: res.data.map(item => item.postCount),
        barWidth: '50%'
      }
    ]
  }
}

const fetchWeeklyStats = async () => {
  const res = await axios.get('/api/admin/stats/posts/weekly', {
    params: { weeks: selectedWeek.value }
  })
  weeklyStats.value = res.data
}
</script>

<style scoped>
.post-stats {
  max-width: 900px;
  margin: auto;
  padding: 20px;
}
</style>
