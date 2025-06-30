<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { photoApi } from "@/utils/request.js";

const router = useRouter();

// 获取热门作品
const loading = ref(true);
const currentSlide = ref(0); // 统一使用 currentSlide 作为当前索引
let interval = null;
const hotWorks = ref([]);
const slides = ref([]);

// 默认头像
const defaultAvatar = ref(
  "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
);

// 处理头像URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return defaultAvatar.value;
  if (avatar.startsWith("data:image")) return avatar;
  if (avatar.startsWith("http")) return avatar;
  if (avatar.startsWith("/")) return "http://localhost:7080" + avatar;
  return "http://localhost:7080/avatars/" + avatar;
};

// 计算当前显示的作品ID
const currentWorkId = computed(() => {
  if (hotWorks.value.length === 0) return null;
  return hotWorks.value[currentSlide.value].id;
});

const loadHotWorks = async () => {
  try {
    loading.value = true;
    const res = await photoApi.getApproved({});

    if (res.code === "200" && res.data?.length > 0) {
      hotWorks.value = res.data
        .map((work) => {
          const hotScore =
            (work.likeCount || 0) +
            (work.viewCount || 0) +
            (work.commentCount || 0);
          return { ...work, hotScore };
        })
        .sort((a, b) => b.hotScore - a.hotScore)
        .slice(0, 4);

      // 创建展示数据
      slides.value = hotWorks.value.map((work) => ({
        id: work.id,
        title: work.title,
        image: work.cover ? `http://localhost:7080/uploads/${work.cover}` : "",
        author: {
          name: work.username,
          avatar: getAvatarUrl(work.avatar),
        },
        stats: {
          likes: work.likeCount || 0,
          views: work.viewCount || 0,
          comments: work.commentCount || 0,
        },
      }));
    } else {
      hotWorks.value = [];
      slides.value = [];
    }
  } catch (error) {
    console.error("获取热门作品失败:", error);
    hotWorks.value = [];
    slides.value = [];
  } finally {
    loading.value = false;
    startAutoCarousel();
  }
};

const goToWorkDetail = () => {
  if (!currentWorkId.value) return;
  router.push(`/work/${currentWorkId.value}`);
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % slides.value.length;
};

const prevSlide = () => {
  currentSlide.value =
    (currentSlide.value - 1 + slides.value.length) % slides.value.length;
};

// 启动自动轮播
const startAutoCarousel = () => {
  if (interval) {
    clearInterval(interval);
  }
  // 确保有幻灯片可轮播
  if (slides.value.length > 1) {
    interval = setInterval(() => {
      nextSlide();
    }, 5000);
  }
};

// 计算当前轮播图大图背景
const currentBgImage = computed(() => {
  if (slides.value.length === 0) return "";
  // 用当前轮播图的大图做背景，加深色蒙版
  return `linear-gradient(rgba(10,20,40,0.85),rgba(10,20,40,0.85)), url('${
    slides.value[currentSlide.value]?.image || ""
  }')`;
});

onMounted(() => {
  loadHotWorks();
});

onUnmounted(() => {
  if (interval) {
    clearInterval(interval);
  }
});
</script>

<template>
  <section
    class="carousel-section"
    :style="{ backgroundImage: currentBgImage }"
  >
    <div class="section-title">
      <h2>热门摄影作品</h2>
      <p>根据点赞、浏览和评论综合排序的热门作品</p>
    </div>

    <div class="carousel-container">
      <div
        v-for="(slide, index) in slides"
        :key="slide.id"
        class="carousel-slide"
        :class="{ active: currentSlide === index }"
        :style="{ backgroundImage: `url(${slide.image})` }"
        @click="goToWorkDetail"
      >
        <div class="slide-info">
          <div class="slide-content">
            <div class="slide-meta">
              <h2 class="slide-title">{{ slide.title }}</h2>
              <div class="slide-author">
                <img
                  :src="slide.author.avatar"
                  :alt="slide.author.name"
                  class="author-avatar"
                />
                <span>{{ slide.author.name }}</span>
              </div>
            </div>
            <div class="slide-stats">
              <div class="stat">
                <i class="fas fa-heart"></i>
                <span>{{ slide.stats.likes }}</span>
              </div>
              <div class="stat">
                <i class="fas fa-eye"></i>
                <span>{{ slide.stats.views }}</span>
              </div>
              <div class="stat">
                <i class="fas fa-comment"></i>
                <span>{{ slide.stats.comments }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <button class="carousel-btn prev" @click="prevSlide">
        <i class="fas fa-chevron-left"></i>
      </button>
      <button class="carousel-btn next" @click="nextSlide">
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </section>
</template>

<style scoped>
.carousel-section {
  padding: 2rem 5%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  transition: background-image 1s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
}

.section-title {
  text-align: center;
  margin-bottom: 2rem;
  color: var(--text-light);
  position: relative;
}

.section-title h2 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  letter-spacing: 1px;
}

.section-title p {
  color: rgba(255, 255, 255, 0.8);
  font-size: 1.1rem;
  max-width: 600px;
  margin: 0 auto;
}

.carousel-container {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  height: 500px;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.carousel-slide {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: opacity 1.2s ease-in-out;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: flex-end;
  cursor: pointer;
}

.carousel-slide.active {
  opacity: 1;
}

.slide-info {
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.4));
  color: white;
  padding: 2rem;
  width: 100%;
  position: relative;
  backdrop-filter: blur(5px);
}

.slide-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  gap: 2rem;
}

.slide-meta {
  flex: 1;
}

.slide-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.slide-author {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid var(--highlight);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.slide-stats {
  display: flex;
  gap: 24px;
  font-size: 1.2rem;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.75rem 1.5rem;
  border-radius: 30px;
  backdrop-filter: blur(10px);
}

.stat {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.9);
}

.stat i {
  color: var(--highlight);
  font-size: 1.1rem;
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.15);
  border: none;
  color: white;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1.4rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  backdrop-filter: blur(5px);
  z-index: 10;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.carousel-btn.prev {
  left: 30px;
}

.carousel-btn.next {
  right: 30px;
}

.carousel-btn:hover {
  background: var(--highlight);
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 6px 16px rgba(233, 69, 96, 0.4);
}

@media (max-width: 1200px) {
  .slide-content {
    padding: 0 2rem;
  }

  .slide-title {
    font-size: 2rem;
  }
}

@media (max-width: 768px) {
  .carousel-container {
    height: 350px;
  }

  .slide-title {
    font-size: 1.5rem;
  }

  .slide-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
    padding: 0 1rem;
  }

  .slide-stats {
    width: 100%;
    justify-content: space-around;
  }

  .carousel-btn {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .carousel-btn.prev {
    left: 15px;
  }

  .carousel-btn.next {
    right: 15px;
  }
}

@media (max-width: 576px) {
  .carousel-container {
    height: 300px;
  }

  .slide-info {
    padding: 1rem;
  }

  .slide-title {
    font-size: 1.2rem;
    margin-bottom: 0.5rem;
  }

  .slide-author {
    font-size: 1rem;
  }

  .author-avatar {
    width: 36px;
    height: 36px;
  }

  .slide-stats {
    font-size: 0.9rem;
    padding: 0.5rem 1rem;
  }
}
</style>
