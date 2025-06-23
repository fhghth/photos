<script setup>
import { ref } from "vue";

const series = ref([
  {
    id: 1,
    title: "自然风光系列",
    description:
      "探索地球最壮丽的自然景观，从雄伟山脉到宁静湖泊，从茂密森林到广袤沙漠，感受大自然的鬼斧神工。",
    image: "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05",
    isFeatured: true,
  },
  {
    id: 2,
    title: "城市建筑",
    description: "记录现代都市的建筑之美",
    image: "https://images.unsplash.com/photo-1493246507139-91e8fad9978e",
    count: 42,
    photographers: 8,
  },
  {
    id: 3,
    title: "植物微观",
    description: "探索植物的微观世界",
    image: "https://images.unsplash.com/photo-1418065460487-3e41a6c84dc5",
    count: 28,
    photographers: 5,
  },
  {
    id: 4,
    title: "野生动物",
    description: "捕捉野生动物的精彩瞬间",
    image: "https://images.unsplash.com/photo-1501555088652-021faa106b9b",
    count: 67,
    photographers: 12,
  },
]);
</script>

<template>
  <section class="series-section">
    <div class="section-title">
      <h2>精选摄影系列</h2>
      <p>探索主题系列作品，沉浸于视觉叙事的世界</p>
    </div>

    <div class="series-grid">
      <!-- 特色系列大图 -->
      <div
        v-for="item in series"
        :key="item.id"
        :class="['series-item', { 'featured-series': item.isFeatured }]"
      >
        <img :src="item.image" :alt="item.title" class="series-image" />
        <div :class="['series-overlay', { 'featured-info': item.isFeatured }]">
          <h3 :class="['series-name', { 'featured-title': item.isFeatured }]">
            {{ item.title }}
          </h3>
          <p v-if="item.isFeatured" class="featured-description">
            {{ item.description }}
          </p>
          <div v-else class="series-meta">
            <span><i class="fas fa-images"></i> {{ item.count }} 作品</span>
            <span
              ><i class="fas fa-user"></i>
              {{ item.photographers }} 位摄影师</span
            >
          </div>
        </div>
      </div>

      <!-- 查看更多区域 -->
      <div class="view-more" @click="$router.push('/series')">
        <i class="fas fa-arrow-right"></i>
        <h3>查看更多系列</h3>
        <p>探索更多精彩摄影主题系列作品，发现不同视角下的世界之美</p>
        <button class="btn">浏览全部系列</button>
      </div>
    </div>
  </section>
</template>

<style scoped>
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

.series-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.featured-series {
  grid-column: 1 / -1;
  position: relative;
  height: 500px;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.series-item {
  position: relative;
  height: 300px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.series-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
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
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.2));
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 2rem;
  color: white;
  opacity: 0;
  transition: all 0.4s ease;
  backdrop-filter: blur(3px);
}

.series-item:hover .series-overlay {
  opacity: 1;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.4));
}

.featured-info {
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.4));
  opacity: 1;
  padding: 3rem 2rem;
}

.series-name {
  font-size: 1.6rem;
  font-weight: 600;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.featured-title {
  font-size: 2.8rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.featured-description {
  max-width: 600px;
  font-size: 1.2rem;
  opacity: 0.9;
  line-height: 1.6;
}

.series-meta {
  display: flex;
  gap: 20px;
  font-size: 1.1rem;
  opacity: 0.9;
  margin-top: 1rem;
}

.series-meta span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.series-meta i {
  color: var(--highlight);
  font-size: 1.2rem;
}

.view-more {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, var(--accent), #0c2a4d);
  color: white;
  border-radius: 15px;
  padding: 3rem 2rem;
  transition: all 0.4s ease;
  cursor: pointer;
  text-align: center;
  box-shadow: 0 10px 30px rgba(15, 52, 96, 0.2);
}

.view-more:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(15, 52, 96, 0.3);
}

.view-more i {
  font-size: 4rem;
  margin-bottom: 2rem;
  color: var(--highlight);
  transition: transform 0.4s ease;
}

.view-more:hover i {
  transform: translateX(10px);
}

.view-more h3 {
  font-size: 2rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.view-more p {
  max-width: 300px;
  opacity: 0.9;
  margin-bottom: 2rem;
  line-height: 1.6;
}

.btn {
  background: var(--highlight);
  color: white;
  border: none;
  padding: 12px 28px;
  border-radius: 30px;
  font-weight: 600;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
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
    padding: 0 2rem;
  }

  .featured-title {
    font-size: 2.4rem;
  }
}

@media (max-width: 992px) {
  .series-grid {
    grid-template-columns: 1fr;
    padding: 0 1rem;
  }

  .featured-series {
    height: 400px;
  }

  .view-more {
    padding: 2rem;
  }
}

@media (max-width: 768px) {
  .featured-series {
    height: 300px;
  }

  .featured-title {
    font-size: 2rem;
  }

  .featured-description {
    font-size: 1.1rem;
  }

  .series-item {
    height: 250px;
  }

  .view-more h3 {
    font-size: 1.8rem;
  }

  .view-more i {
    font-size: 3rem;
  }
}

@media (max-width: 576px) {
  .featured-series {
    height: 250px;
  }

  .featured-title {
    font-size: 1.6rem;
  }

  .featured-description {
    font-size: 1rem;
  }

  .series-item {
    height: 200px;
  }

  .series-name {
    font-size: 1.3rem;
  }

  .view-more {
    padding: 1.5rem;
  }

  .view-more h3 {
    font-size: 1.5rem;
  }

  .view-more i {
    font-size: 2.5rem;
  }

  .btn {
    padding: 10px 24px;
    font-size: 1rem;
  }
}
</style>
