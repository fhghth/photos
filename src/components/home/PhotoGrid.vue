<script setup>
import { ref, onMounted } from "vue";
import { photoApi } from "@/utils/request.js";
import { useRouter } from "vue-router";

const router = useRouter();
const photos = ref([]);
const loading = ref(true);

// 获取用户状态
const currentUser = ref(
  JSON.parse(localStorage.getItem("code_user")) || {
    username: "游客",
    avatar: "",
    id: null,
  }
);

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

const loadNewWorks = async () => {
  try {
    loading.value = true;
    const res = await photoApi.getApproved({});

    if (res.code === "200" && res.data?.length > 0) {
      // 按创建时间排序（最新的在前）
      const sortedWorks = res.data.sort(
        (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
      );

      // 只取前4个最新作品
      photos.value = sortedWorks.slice(0, 4).map((work) => ({
        id: work.id,
        title: work.title || "未命名作品",
        image: work.cover ? `http://localhost:7080/uploads/${work.cover}` : "",
        author: {
          name: work.username || "匿名作者",
          avatar: getAvatarUrl(work.avatar),
        },
        description: work.description || "作品描述暂无",
        createdAt: work.createdAt,
        stats: {
          likes: work.likeCount || 0,
          views: work.viewCount || 0,
          comments: work.commentCount || 0,
        },
        isLiked: false, // 初始化为未点赞状态
      }));

      // 检查每个作品的点赞状态
      await checkLikeStatus();
    }
  } catch (error) {
    console.error("获取热门作品失败:", error);
  } finally {
    loading.value = false;
  }
};

// 检查用户是否已点赞所有作品
const checkLikeStatus = async () => {
  if (!currentUser.value?.id) {
    return;
  }

  try {
    // 为所有作品并行检查点赞状态
    const checkPromises = photos.value.map((photo) =>
      photoApi.checkLikeStatus({
        workId: photo.id, // 使用作品ID而不是work.value.id
        userId: currentUser.value.id,
      })
    );

    const results = await Promise.all(checkPromises);

    // 更新每个作品的点赞状态
    results.forEach((res, index) => {
      if (res.code === "200" && res.data.liked) {
        photos.value[index].isLiked = true;
      }
    });
  } catch (error) {
    console.error("检查点赞状态失败:", error);
  }
};

// 图片点赞功能
const likeLoading = ref(false);

const handleLike = async (photoId, event) => {
  event.stopPropagation(); // 阻止事件冒泡

  if (!currentUser.value?.id) {
    alert("请先登录后再点赞");
    return;
  }

  likeLoading.value = true;
  try {
    const index = photos.value.findIndex((p) => p.id === photoId);
    const photo = photos.value[index];

    const res = await photoApi.likePhoto({
      workId: photoId,
      userId: currentUser.value.id,
      type: photo.isLiked ? "unlike" : "like",
    });

    if (res.code === "200") {
      // 更新该作品的点赞状态和数量
      photos.value[index] = {
        ...photo,
        isLiked: !photo.isLiked,
        stats: {
          ...photo.stats,
          likes: photo.isLiked ? photo.stats.likes - 1 : photo.stats.likes + 1,
        },
      };

      // 更新UI
      photos.value = [...photos.value];
    }
  } catch (error) {
    console.error("点赞操作失败:", error);
    alert("点赞失败，请重试");
  } finally {
    likeLoading.value = false;
  }
};

// 跳转到作品详情页
const goToWorkDetail = (workId) => {
  router.push(`/work/${workId}`);
};

onMounted(() => {
  loadNewWorks();
});
</script>

<template>
  <section class="photography-section">
    <div class="section-title">
      <h2>最新摄影作品</h2>
      <p>展示最近上传的四部精选摄影作品</p>
    </div>

    <div class="loader" v-if="loading">
      <div class="spinner"></div>
    </div>

    <div class="no-results" v-else-if="photos.length === 0">
      <p>暂无作品展示</p>
    </div>

    <div class="photo-grid" v-else>
      <div
        v-for="photo in photos"
        :key="photo.id"
        class="photo-card"
        @click="goToWorkDetail(photo.id)"
      >
        <div class="photo-header">
          <img
            class="photo-image"
            :src="photo.image"
            :alt="photo.title"
            @error="
              (e) => (e.target.src = require('@/assets/imgs/default-work.jpg'))
            "
          />
          <div class="overlay-icons">
            <button
              class="icon-btn"
              @click.stop="handleLike(photo.id, $event)"
              :disabled="likeLoading"
              :title="photo.isLiked ? '取消点赞' : '点赞作品'"
            >
              <i
                class="fas fa-heart"
                :style="{ color: photo.isLiked ? '#ff6b6b' : '' }"
              ></i>
            </button>
            <button class="icon-btn" @click.stop title="收藏作品">
              <i class="fas fa-bookmark"></i>
            </button>
            <button class="icon-btn" @click.stop title="分享作品">
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
              class="author-avatar"
              :src="photo.author.avatar"
              :alt="photo.author.name"
              @error="
                (e) =>
                  (e.target.src = require('@/assets/imgs/default-avatar.jpg'))
              "
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
        <div class="photo-time">
          上传于 {{ new Date(photo.createdAt).toLocaleDateString("zh-CN") }}
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

.loader,
.no-results {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.no-results p {
  font-size: 1.2rem;
  color: #666;
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
  cursor: pointer;
  display: flex;
  flex-direction: column;
}

.photo-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.15);
}

.photo-header {
  position: relative;
  height: 250px;
  overflow: hidden;
  flex-shrink: 0;
}

.photo-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
  background-color: #f8f9fa;
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
  flex-grow: 1;
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
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
  flex-grow: 1;
}

.author-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  background-color: #eee;
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

.photo-time {
  padding: 8px 15px;
  font-size: 0.85rem;
  color: #888;
  border-top: 1px solid #eee;
  text-align: right;
  background: #f9f9f9;
}

@media (max-width: 768px) {
  .photo-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }

  .section-title h2 {
    font-size: 2rem;
  }
}

@media (max-width: 576px) {
  .photo-grid {
    grid-template-columns: 1fr;
  }

  .photography-section {
    padding: 2rem;
  }

  .photo-header {
    height: 200px;
  }
}
</style>
