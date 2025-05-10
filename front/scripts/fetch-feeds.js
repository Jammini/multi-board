import fs from 'fs'
import axios from 'axios'

const BASE = 'http://localhost:8080'   // 실제 배포 도메인 또는 로컬 dev 서버 주소

async function fetchAndSave(path, outFile) {
  const res = await axios.get(`${BASE}${path}`, { responseType: 'text' })
  fs.writeFileSync(`public/${outFile}`, res.data, 'utf-8')
  console.log(`✔ ${outFile} generated`)
}

async function run() {
  await fetchAndSave('/rss',  'rss.xml')
  await fetchAndSave('/atom', 'atom.xml')
}

run().catch(err => {
  console.error(err)
  process.exit(1)
})
