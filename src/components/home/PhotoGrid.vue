<script setup>
import { ref, onMounted } from "vue";

const photos = ref([]);
const loadingPhotos = ref(true);

// 模拟API获取照片数据
const fetchPhotos = () => {
  loadingPhotos.value = true;

  // 模拟API调用延迟
  setTimeout(() => {
    photos.value = [
      {
        id: 1,
        title: "雪山之巅",
        description:
          "黎明时分，金色的阳光洒在雪山之巅，云雾缭绕间仿佛置身仙境。拍摄于西藏珠穆朗玛峰大本营。",
        image:
          "https://images.unsplash.com/photo-1455218873509-8097305ee378?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80",
        author: {
          name: "王峰",
          avatar:
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80",
        },
        stats: {
          likes: 245,
          views: 1562,
          comments: 42,
        },
      },
      {
        id: 2,
        title: "都市森林",
        description:
          "现代摩天大楼组成的钢铁森林，在夜晚灯光的映照下展现未来科技感。",
        image:
          "https://images.unsplash.com/photo-1477959858617-67f85cf4f1df?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80",
        author: {
          name: "林小雅",
          avatar:
            "https://images.unsplash.com/photo-1586297135537-94bc9ba060aa?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80",
        },
        stats: {
          likes: 312,
          views: 2534,
          comments: 76,
        },
      },
      {
        id: 3,
        title: "沙漠行者",
        description:
          "落日余晖中的沙漠驼队，长长的影子映在沙丘上，展现生命的坚韧。",
        image:
          "https://images.unsplash.com/photo-1505142468610-359e7d316be0?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80",
        author: {
          name: "张明远",
          avatar:
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80",
        },
        stats: {
          likes: 421,
          views: 3897,
          comments: 93,
        },
      },
      {
        id: 4,
        title: "湖中倒影",
        description: "宁静的湖面如同明镜，完美倒映着天空的云彩和岸边的秋叶。",
        image:
          "https://images.unsplash.com/photo-1542273917363-3b1817f9a4b8?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80",
        author: {
          name: "李思雨",
          avatar:
            "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?ixlib=rb-4.0.3&auto=format&fit=crop&w=100&q=80",
        },
        stats: {
          likes: 382,
          views: 2875,
          comments: 64,
        },
      },
    ];
    loadingPhotos.value = false;
  }, 1500);
};

// 图片点赞功能
const likePhoto = (photoId) => {
  const photo = photos.value.find((p) => p.id === photoId);
  if (photo) {
    photo.stats.likes++;
  }
};

onMounted(() => {
  fetchPhotos();
});
</script>

<template>
  <section class="photography-section">
    <div class="section-title">
      <h2>最新摄影作品</h2>
      <p>浏览最新上传的摄影作品，感受多样化的视觉表达</p>
    </div>

    <div class="loader" v-if="loadingPhotos">
      <div class="spinner"></div>
    </div>

    <div class="photo-grid" v-else>
      <div v-for="photo in photos" :key="photo.id" class="photo-card">
        <div class="photo-header">
          <img :src="photo.image" :alt="photo.title" class="photo-image" />
          <div class="overlay-icons">
            <button class="icon-btn" @click="likePhoto(photo.id)">
              <i class="fas fa-heart"></i>
            </button>
            <button class="icon-btn">
              <i class="fas fa-bookmark"></i>
            </button>
            <button class="icon-btn">
              <i class="fas fa-share-alt"></i>
            </button>
          </div>
        </div>
        <div class="photo-body">
          <h3 class="photo-title">{{ photo.title }}</h3>
          <p class="photo-description">{{ photo.description }}</p>
        </div>
        <div class="photo-footer">
          <div class="photo-author">
            <img
              :src="photo.author.avatar"
              :alt="photo.author.name"
              class="author-img"
            />
            <span>{{ photo.author.name }}</span>
          </div>
          <div class="photo-stats">
            <div class="stat-container">
              <i class="fas fa-heart"></i>
              <span>{{ photo.stats.likes }}</span>
            </div>
            <div class="stat-container">
              <i class="fas fa-eye"></i>
              <span>{{ photo.stats.views }}</span>
            </div>
            <div class="stat-container">
              <i class="fas fa-comment"></i>
              <span>{{ photo.stats.comments }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.photography-section {
  padding: 4rem 5%;
  background-color: #f9f9f9;
}

.section-title {
  text-align: center;
  margin-bottom: 2rem;
}

.section-title h2 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  color: var(--text-dark);
}

.section-title p {
  color: #666;
  font-size: 1.1rem;
  max-width: 600px;
  margin: 0 auto;
}

.loader {
  display: flex;
  justify-content: center;
  padding: 3rem 0;
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

.photo-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 25px;
  max-width: 1400px;
  margin: 0 auto;
}

.photo-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease;
}

.photo-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.15);
}

.photo-header {
  position: relative;
  height: 250px;
  overflow: hidden;
}

.photo-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.photo-card:hover .photo-image {
  transform: scale(1.1);
}

.overlay-icons {
  position: absolute;
  top: 15px;
  right: 15px;
  display: flex;
  gap: 10px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.photo-card:hover .overlay-icons {
  opacity: 1;
}

.icon-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--accent);
  font-size: 1.1rem;
  cursor: pointer;
  transition: var(--transition);
  border: none;
}

.icon-btn:hover {
  background: var(--highlight);
  color: white;
  transform: scale(1.1);
}

.photo-body {
  padding: 1.5rem;
}

.photo-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 0.8rem;
  color: var(--text-dark);
}

.photo-description {
  color: #666;
  font-size: 0.95rem;
  margin-bottom: 1.2rem;
  line-height: 1.6;
}

.photo-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #eee;
  padding: 1.2rem;
}

.photo-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.photo-stats {
  display: flex;
  gap: 15px;
  font-size: 0.9rem;
  color: #777;
}

.stat-container {
  display: flex;
  align-items: center;
  gap: 5px;
}

@media (max-width: 768px) {
  .photo-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 576px) {
  .photo-grid {
    grid-template-columns: 1fr;
  }
}
</style>
