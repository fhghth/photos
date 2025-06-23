<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const slides = ref([
  {
    id: 1,
    image:
      "https://images.unsplash.com/photo-1506260408121-e353d10b87c7?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1600&q=80",
    title: "山脉晨曦",
    author: {
      name: "张明远",
      avatar:
        "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    stats: {
      likes: "2.4K",
      views: "15.7K",
      comments: 243,
    },
  },
  {
    id: 2,
    image:
      "https://images.unsplash.com/photo-1501785888041-af3ef285b470?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1600&q=80",
    title: "湖光山色",
    author: {
      name: "李思雨",
      avatar:
        "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    stats: {
      likes: "3.1K",
      views: "22.3K",
      comments: 417,
    },
  },
  {
    id: 3,
    image:
      "https://images.unsplash.com/photo-1469474968028-56623f02e42e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1600&q=80",
    title: "森林秘境",
    author: {
      name: "陈默",
      avatar:
        "https://images.unsplash.com/photo-1560250097-0b93528c311a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=200&q=80",
    },
    stats: {
      likes: "4.7K",
      views: "35.2K",
      comments: 682,
    },
  },
]);

const currentSlide = ref(0);
let interval;

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % slides.value.length;
};

const prevSlide = () => {
  currentSlide.value =
    (currentSlide.value - 1 + slides.value.length) % slides.value.length;
};

onMounted(() => {
  interval = setInterval(nextSlide, 5000);
});

onUnmounted(() => {
  clearInterval(interval);
});
</script>

<template>
  <section class="carousel-section">
    <div class="section-title">
      <h2>探索视觉艺术</h2>
      <p>发现全球顶尖摄影师的精彩作品，获取创作灵感</p>
    </div>

    <div class="carousel-container">
      <div
        v-for="(slide, index) in slides"
        :key="slide.id"
        class="carousel-slide"
        :class="{ active: currentSlide === index }"
        :style="{ backgroundImage: `url(${slide.image})` }"
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
  background: linear-gradient(to bottom, var(--secondary), var(--primary));
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
