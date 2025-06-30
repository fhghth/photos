<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { photoApi } from "@/utils/request.js";

const router = useRouter();
const featuredWork = ref([]); // 特色作品数据
const loading = ref(false);

// 分类选项映射表
const categoryMap = {
  landscape: "自然风光",
  urban: "城市掠影",
  portrait: "人像写真",
  architecture: "建筑摄影",
  wildlife: "动物世界",
  food: "美食摄影",
  macro: "微距摄影",
  street: "街头摄影",
  documentary: "纪实摄影",
  sports: "体育摄影",
  travel: "旅行摄影",
  abstract: "抽象摄影",
};

// 加载精选分类数据
const loadNewWorks = async () => {
  try {
    loading.value = true;
    const res = await photoApi.getApproved({});

    if (res.code === "200" && res.data?.length > 0) {
      // 创建分类分组数据
      const categories = {};

      res.data.forEach((work) => {
        // 使用统一的分类名称（转换为小写进行比较）
        const categoryKey = work.category
          ? work.category.toLowerCase().trim()
          : "未分类";

        // 获取中文分类名
        const categoryName = categoryMap[categoryKey] || categoryKey;

        if (!categories[categoryKey]) {
          categories[categoryKey] = {
            categoryKey, // 保留原始分类键
            categoryName, // 使用中文分类名
            works: [],
            photographers: new Set(),
            latestWork: work,
          };
        }

        // 更新作品列表
        categories[categoryKey].works.push(work);
        // 添加摄影师
        if (work.username) {
          categories[categoryKey].photographers.add(work.username);
        }

        // 检查是否需要更新最新作品
        if (work.createdAt > categories[categoryKey].latestWork.createdAt) {
          categories[categoryKey].latestWork = work;
        }
      });

      // 转换为数组并按作品数量排序（降序）
      const categoryArray = Object.values(categories);
      categoryArray.sort((a, b) => b.works.length - a.works.length);

      // 获取前4个分类
      const topCategories = categoryArray.slice(0, 4);

      // 构建特色分类数据
      featuredWork.value = topCategories.map((category) => ({
        id: category.latestWork.id, // 使用最新作品的ID作为标识符
        categoryKey: category.categoryKey, // 保留原始分类键
        categoryName: category.categoryName, // 使用中文分类名
        image: category.latestWork.cover
          ? `http://localhost:7080/uploads/${category.latestWork.cover}`
          : require("@/assets/imgs/default-work.jpg"),
        stats: {
          worksCount: category.works.length,
          photographers: category.photographers.size,
        },
      }));
    }
  } catch (error) {
    console.error("获取作品失败:", error);
  } finally {
    loading.value = false;
  }
};

// 跳转到分类页面
const goToCategoryPage = (categoryKey) => {
  router.push(`/images?category=${encodeURIComponent(categoryKey)}`);
};

onMounted(() => {
  loadNewWorks();
});
</script>

<template>
  <section class="series-section">
    <div class="section-title">
      <h2>精选摄影系列</h2>
      <p>探索主题系列作品，沉浸于视觉叙事的世界</p>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>正在加载精选系列...</p>
    </div>

    <div v-else-if="featuredWork.length === 0" class="no-series">
      <i class="fas fa-camera fa-3x"></i>
      <h3>暂无精选系列</h3>
      <p>尚未发现热门分类，作品上传后系统会自动生成精选系列</p>
      <button class="btn" @click="loadNewWorks">重新加载</button>
    </div>

    <div v-else class="series-grid">
      <!-- 特色分类卡片 -->
      <div
        v-if="featuredWork.length > 3"
        class="series-item featured-series"
        @click="goToCategoryPage(featuredWork[3].categoryKey)"
      >
        <div class="image-container">
          <img
            :src="featuredWork[3].image"
            :alt="featuredWork[3].categoryName"
            class="series-image"
            @error="
              (e) => (e.target.src = require('@/assets/imgs/default-work.jpg'))
            "
          />
        </div>
        <div class="series-overlay featured-info">
          <h3 class="series-name featured-title">
            {{ featuredWork[3].categoryName }}
          </h3>
          <div class="featured-stats">
            <div class="stat-group">
              <div class="stat-value">
                {{ featuredWork[3].stats.worksCount }}
              </div>
              <div class="stat-label">作品数量</div>
            </div>
            <div class="stat-group">
              <div class="stat-value">
                {{ featuredWork[3].stats.photographers }}
              </div>
              <div class="stat-label">摄影师</div>
            </div>
          </div>
        </div>
      </div>
      <!-- 三个普通分类卡片 -->
      <div
        v-for="(item, index) in featuredWork.slice(0, 3)"
        :key="item.id"
        class="series-item"
        @click="goToCategoryPage(item.categoryKey)"
      >
        <div class="image-container">
          <img
            :src="item.image"
            :alt="item.categoryName"
            class="series-image"
            @error="
              (e) => (e.target.src = require('@/assets/imgs/default-work.jpg'))
            "
          />
        </div>
        <div class="series-overlay">
          <h3 class="series-name">{{ item.categoryName }}</h3>
          <div class="series-meta">
            <span class="stat">
              <i class="fas fa-images"></i> {{ item.stats.worksCount }} 作品
            </span>
            <span class="stat">
              <i class="fas fa-user"></i>
              {{ item.stats.photographers }} 位摄影师
            </span>
          </div>
        </div>
      </div>

      <!-- 查看更多区域 -->
      <div class="view-more" @click="router.push('/images')">
        <i class="fas fa-arrow-right"></i>
        <h3>查看更多系列</h3>
        <p>探索更多精彩摄影主题系列作品，发现不同视角下的世界之美</p>
        <button class="btn">探索全部</button>
      </div>
    </div>
  </section>
</template>

<style scoped>
/* 样式保持不变 */
.series-section {
  padding: 4rem 5%;
  background: #fff;
}

.section-title {
  text-align: center;
  margin-bottom: 3rem;
}

.section-title h2 {
  font-size: 2.5rem;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.section-title p {
  color: var(--text-secondary);
  font-size: 1.1rem;
  max-width: 600px;
  margin: 0 auto;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 500px;
  gap: 20px;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid rgba(25, 25, 46, 0.1);
  border-top: 5px solid var(--highlight);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.no-series {
  text-align: center;
  padding: 3rem;
  background-color: #f8f9fa;
  border-radius: 15px;
  max-width: 600px;
  margin: 0 auto;
  color: #555;
}

.no-series i {
  margin-bottom: 1.5rem;
  color: var(--accent);
}

.no-series h3 {
  font-size: 1.8rem;
  margin-bottom: 1rem;
}

.no-series p {
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.series-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.series-item {
  position: relative;
  height: 300px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
}

.series-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.featured-series {
  grid-column: 1 / -1;
  height: 400px;
  border-radius: 15px;
  overflow: hidden;
}

.image-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.series-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.series-item:hover .series-image {
  transform: scale(1.1);
}

.series-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent 40%);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 1.5rem;
  color: white;
  opacity: 1;
  transition: all 0.4s ease;
}

.series-item:hover .series-overlay {
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9), transparent 40%);
}

.featured-info {
  justify-content: center;
  align-items: flex-start;
  background: linear-gradient(to right, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3));
}

.series-name {
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.featured-title {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: var(--highlight);
  max-width: 70%;
}

.series-meta {
  display: flex;
  gap: 15px;
  font-size: 1.1rem;
  margin-top: 0.5rem;
  background: rgba(0, 0, 0, 0.5);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  backdrop-filter: blur(5px);
}

.featured-stats {
  display: flex;
  gap: 30px;
  background: rgba(0, 0, 0, 0.5);
  padding: 1.5rem;
  border-radius: 15px;
  backdrop-filter: blur(5px);
}

.stat-group {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 3rem;
  font-weight: 700;
  color: var(--highlight);
}

.stat-label {
  font-size: 1.2rem;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-top: 0.5rem;
}

.view-more {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, var(--accent), #0c2a4d);
  color: white;
  border-radius: 15px;
  padding: 2.5rem;
  transition: all 0.4s ease;
  cursor: pointer;
  text-align: center;
  box-shadow: 0 10px 30px rgba(15, 52, 96, 0.2);
  grid-column: span 1;
  height: 300px;
}

.view-more:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 35px rgba(15, 52, 96, 0.3);
}

.view-more i {
  font-size: 3rem;
  margin-bottom: 1.5rem;
  color: var(--highlight);
  transition: transform 0.4s ease;
}

.view-more:hover i {
  transform: translateX(10px);
}

.view-more h3 {
  font-size: 1.8rem;
  margin-bottom: 0.8rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.view-more p {
  max-width: 300px;
  opacity: 0.9;
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.btn {
  background: var(--highlight);
  color: white;
  border: none;
  padding: 12px 32px;
  border-radius: 30px;
  font-weight: 600;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  box-shadow: 0 4px 15px rgba(233, 69, 96, 0.3);
}

.btn:hover {
  background: #ff2e4d;
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(233, 69, 96, 0.4);
}

@media (max-width: 1200px) {
  .series-grid {
    gap: 20px;
    padding: 0 2rem;
  }

  .featured-title {
    font-size: 2.2rem;
  }

  .featured-series {
    height: 350px;
  }
}

@media (max-width: 992px) {
  .series-grid {
    grid-template-columns: 1fr;
    padding: 0 1rem;
  }

  .view-more {
    grid-column: 1 / -1;
  }

  .featured-series {
    height: 300px;
  }
}

@media (max-width: 768px) {
  .section-title h2 {
    font-size: 2rem;
  }

  .section-title p {
    font-size: 1rem;
  }

  .series-item {
    height: 250px;
  }

  .featured-series {
    height: 280px;
  }

  .featured-title {
    font-size: 1.8rem;
    margin-bottom: 1.5rem;
  }

  .stat-value {
    font-size: 2.2rem;
  }

  .view-more {
    padding: 1.5rem;
  }

  .view-more h3 {
    font-size: 1.5rem;
  }

  .view-more p {
    font-size: 0.95rem;
    max-width: 280px;
  }
}

@media (max-width: 576px) {
  .series-section {
    padding: 3rem 1.5rem;
  }

  .series-item {
    height: 220px;
  }

  .featured-series {
    height: 250px;
  }

  .series-name {
    font-size: 1.5rem;
  }

  .featured-title {
    font-size: 1.6rem;
    margin-bottom: 1rem;
  }

  .featured-stats {
    gap: 20px;
    padding: 1rem;
  }

  .stat-value {
    font-size: 2rem;
  }

  .stat-label {
    font-size: 1rem;
  }

  .btn {
    padding: 10px 24px;
    font-size: 1rem;
  }
}
</style>
