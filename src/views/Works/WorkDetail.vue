<template>
  <el-backtop :right="100" :bottom="100" />
  <div class="work-detail">
    <!-- 返回按钮 -->
    <div class="back-button" @click="goBack">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        fill="currentColor"
        viewBox="0 0 16 16"
      >
        <path
          d="M8 0a8 8 0 1 0 0 16A8 8 0 0 0 8 0zm3.5 7.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5z"
        />
      </svg>
      返回
    </div>

    <!-- 内容容器 -->
    <div class="content-container">
      <!-- 顶部标题和标签 -->
      <div v-if="work" class="work-header">
        <h1>{{ work.title }}</h1>
        <div
          class="work-tag"
          :style="{ backgroundColor: categoryColors[work.category] }"
        >
          {{ getCategoryLabel(work.category) }}
        </div>
      </div>

      <!-- 主体内容区 -->
      <div v-if="work" class="work-container">
        <!-- 左侧作品展示区（包含大图、缩略图和信息统计） -->
        <div class="gallery-section">
          <div class="gallery-container">
            <div
              class="main-image"
              :style="{ backgroundImage: `url(${currentImage})` }"
            ></div>
            <div class="thumbnails">
              <div
                v-for="(image, index) in work.images"
                :key="image.id"
                class="thumbnail-item"
                :class="{ active: currentIndex === index }"
                @click="changeImage(index)"
              >
                <img
                  :src="getImageUrl(image.imagePath)"
                  alt="缩略图"
                  @error="handleImageError"
                />
              </div>
            </div>
          </div>

          <!-- 作品统计数据 - 移动到下方 -->
          <div class="work-stats">
            <el-button
              color="#626aef"
              :dark="isDark"
              plain
              circle
              :loading="likeLoading"
              @click="handleLike"
            >
              <el-icon v-if="!likeLoading">
                <Pointer v-if="!isLiked" />
                <Pointer style="color: #ff4d4f" v-else />
              </el-icon>
            </el-button>
            <el-button color="#626aef" :dark="isDark" plain circle
              ><el-icon><StarFilled /></el-icon
            ></el-button>
            <el-button color="#626aef" :dark="isDark" plain circle
              ><el-icon><Share /></el-icon
            ></el-button>
          </div>
          <div class="work-stats">
            <div class="stat-item">
              <span class="stat-value">{{ work.viewCount || "0" }}</span>
              <span class="stat-label">浏览量</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ work.likeCount || "0" }}</span>
              <span class="stat-label">喜欢</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ work.commentCount || "0" }}</span>
              <span class="stat-label">评论</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ work.collections || "0" }}</span>
              <span class="stat-label">收藏</span>
            </div>
          </div>
        </div>

        <!-- 右侧作者信息区 -->
        <div class="author-info">
          <div class="author-header">
            <img
              :src="getAvatarUrl(work.avatar) || defaultAvatar"
              alt="作者头像"
              class="author-avatar"
            />
            <div class="author-details">
              <h2>{{ work.username }}</h2>
              <p>摄影师 | {{ work.followers || "0" }} 位关注者</p>
            </div>
            <button class="follow-btn">+ 关注</button>
          </div>

          <div class="work-meta">
            <div class="meta-item">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="20"
                height="20"
                fill="#9770DB"
                viewBox="0 0 16 16"
              >
                <path
                  d="M8 1a2 2 0 0 0-2 2v2H5V3a3 3 0 1 1 6 0v2h-1V3a2 2 0 0 0-2-2zM5 5H3.36a1.5 1.5 0 0 0-1.483 1.277L.85 13.13A2.5 2.5 0 0 0 3.322 16h9.356a2.5 2.5 0 0 0 2.472-2.87l-1.028-6.853A1.5 1.5 0 0 0 12.64 5H11v1.5a.5.5 0 0 1-1 0V5H6v1.5a.5.5 0 0 1-1 0V5z"
                />
              </svg>
              <div>
                <span>拍摄设备</span>
                <p>{{ work.camera || "未指定" }}</p>
              </div>
            </div>

            <div class="meta-item">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="20"
                height="20"
                fill="#9770DB"
                viewBox="0 0 16 16"
              >
                <path
                  d="M6.5 1A1.5 1.5 0 0 0 5 2.5V3H1.5A1.5 1.5 0 0 0 0 4.5v8A1.5 1.5 0 0 0 1.5 14h13a1.5 1.5 0 0 0 1.5-1.5v-8A1.5 1.5 0 0 0 14.5 3H11v-.5A1.5 1.5 0 0 0 9.5 1h-3zm0 1h3a.5.5 0 0 1 .5.5V3H6v-.5a.5.5 0 0 1 .5-.5zm1.5 5a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 1 .5-.5zm-4 0a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 1 .5-.5zm8 0a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 1 .5-.5z"
                />
              </svg>
              <div>
                <span>拍摄日期</span>
                <p>{{ formatDate(work.createdAt) }}</p>
              </div>
            </div>

            <div class="meta-item">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="20"
                height="20"
                fill="#9770DB"
                viewBox="0 0 16 16"
              >
                <path
                  d="M8 16s6-5.686 6-10A6 6 0 0 0 2 6c0 4.314 6 10 6 10zm0-7a3 3 0 1 1 0-6 3 3 0 0 1 0 6z"
                />
              </svg>
              <div>
                <span>拍摄地点</span>
                <p>
                  {{
                    work.locationAddress || work.locationLng
                      ? `经度: ${work.locationLng}, 纬度: ${work.locationLat}`
                      : "未知地点"
                  }}
                </p>
              </div>
            </div>
          </div>

          <div class="work-description">
            <h3>作品描述</h3>
            <p>
              {{ work.description || "暂无描述" }}
            </p>
          </div>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-icon class="is-loading">
          <Loading />
        </el-icon>
        加载中...
      </div>

      <!-- 没有找到作品的提示 -->
      <div v-if="!loading && !work" class="no-results">
        <el-empty description="未找到该作品" />
      </div>

      <!-- 底部评论区域 -->
      <!-- 评论区域 -->
      <div v-if="work" class="comments-section">
        <h2>
          评论 <span>({{ comments.length }})</span>
        </h2>

        <div class="add-comment">
          <img
            :src="getAvatarUrl(currentUser.avatar) || defaultAvatar"
            alt="用户头像"
            class="user-avatar"
          />
          <div class="comment-input">
            <textarea
              v-model="newComment"
              placeholder="写下你的评论..."
              :disabled="!currentUser.id"
            ></textarea>
            <button
              @click="submitComment"
              :disabled="!newComment.trim() || submittingComment"
            >
              {{ submittingComment ? "提交中..." : "发表评论" }}
            </button>
          </div>
        </div>

        <div v-if="comments.length > 0" class="comments-list">
          <div
            v-for="comment in comments"
            :key="comment.id"
            class="comment-item"
          >
            <img
              :src="getAvatarUrl(comment.avatar) || defaultAvatar"
              alt="用户头像"
              class="comment-avatar"
            />
            <div class="comment-content">
              <div class="comment-header">
                <h4>{{ comment.username }}</h4>
                <span class="comment-time">{{
                  formatCommentTime(comment.commentTime)
                }}</span>
              </div>
              <p>{{ comment.comment }}</p>
              <div class="comment-actions">
                <button @click="replyToComment(comment)">回复</button>
                <button>
                  <el-icon><Star /></el-icon>
                  {{ comment.likes || 0 }}
                </button>
              </div>

              <!-- 回复区域 -->
              <div
                v-if="comment.replies && comment.replies.length > 0"
                class="replies"
              >
                <div
                  v-for="reply in comment.replies"
                  :key="reply.id"
                  class="reply-item"
                >
                  <img
                    :src="getAvatarUrl(reply.avatar) || defaultAvatar"
                    alt="用户头像"
                    class="reply-avatar"
                  />
                  <div class="reply-content">
                    <div class="reply-header">
                      <h5>{{ reply.username }}</h5>
                      <span class="reply-time">{{
                        formatCommentTime(reply.commentTime)
                      }}</span>
                    </div>
                    <p>{{ reply.comment }}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="no-comments">
          <el-empty description="暂无评论，快来发表第一条评论吧" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { photoApi } from "@/utils/request.js";
import { Loading, Share, StarFilled, Pointer } from "@element-plus/icons-vue";

//获取用户状态、
const currentUser = ref(
  JSON.parse(localStorage.getItem("code_user")) || {
    username: "游客",
    avatar: "",
    id: null,
  }
);

//路由
const router = useRouter();
const route = useRoute();

//作品状态
const isLiked = ref(false); // 用户是否已点赞
const isShared = ref(false); // 用户是否已分享
const likeLoading = ref(false); // 点赞加载状态
const collectLoading = ref(false); // 收藏加载状态
const shareLoading = ref(false); // 分享加载状态

// 检查用户是否已点赞
const checkLikeStatus = async () => {
  if (!currentUser.value?.id || !work.value?.id) {
    isLiked.value = false;
    return;
  }
  try {
    const res = await photoApi.checkLikeStatus({
      workId: work.value.id,
      userId: currentUser.value.id,
    });
    if (res.code === "200") {
      isLiked.value = !!res.data.liked;
    }
  } catch (error) {
    isLiked.value = false;
    console.error("检查点赞状态失败:", error);
  }
};

// 默认头像
const defaultAvatar =
  "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png";

const getAvatarUrl = (avatar) => {
  if (!avatar) return defaultAvatar;
  if (avatar.startsWith("data:image")) return avatar;
  if (avatar.startsWith("http")) return avatar;
  if (avatar.startsWith("/")) return "http://localhost:7080" + avatar;
  return "http://localhost:7080/avatars/" + avatar;
};

// 返回功能
const goBack = () => {
  router.go(-1);
};

// 使用 null 作为初始值
const work = ref(null);
const loading = ref(true);
const IMAGE_BASE_URL = "http://localhost:7080/uploads/";

const categoryColors = {
  landscape: "#FF7D7D",
  urban: "#5B8FF9",
  portrait: "#FF9D4D",
  architecture: "#32C7A9",
  wildlife: "#FF99C3",
  food: "#FFD666",
  macro: "#9770DB",
  street: "#37A2DA",
  documentary: "#6C757D",
  sports: "#20C997",
  travel: "#FFB347",
  abstract: "#B39",
};

const getCategoryLabel = (categoryValue) => {
  const categories = [
    { value: "landscape", label: "自然风光" },
    { value: "urban", label: "城市掠影" },
    { value: "portrait", label: "人像写真" },
    { value: "architecture", label: "建筑摄影" },
    { value: "wildlife", label: "动物世界" },
    { value: "food", label: "美食摄影" },
    { value: "macro", label: "微距摄影" },
    { value: "street", label: "街头摄影" },
    { value: "documentary", label: "纪实摄影" },
    { value: "sports", label: "体育摄影" },
    { value: "travel", label: "旅行摄影" },
    { value: "abstract", label: "抽象摄影" },
  ];
  const category = categories.find((cat) => cat.value === categoryValue);
  return category ? category.label : "摄影作品";
};

const currentIndex = ref(0);

// 添加日期格式化函数
const formatDate = (dateString) => {
  if (!dateString) return "未知日期";

  try {
    const date = new Date(dateString);
    return date.toLocaleDateString("zh-CN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    });
  } catch (e) {
    console.error("日期格式错误:", e);
    return "日期格式错误";
  }
};

const getImageUrl = (imagePath) => {
  if (!imagePath) return defaultAvatar;
  return IMAGE_BASE_URL + imagePath;
};
//当前
const currentImage = computed(() => {
  if (work.value?.images?.length > 0) {
    return getImageUrl(work.value.images[currentIndex.value].imagePath);
  }
  return defaultAvatar;
});

const changeImage = (index) => {
  currentIndex.value = index;
};

const handleImageError = (e) => {
  e.target.src = "https://via.placeholder.com/150?text=Image+Error";
  e.target.onerror = null;
};

//
//
//
// 评论功能
const newComment = ref("");
const comments = ref([]);
const submittingComment = ref(false);

// 格式化评论时间
const formatCommentTime = (timestamp) => {
  if (!timestamp) return "未知时间";

  const now = new Date();
  const commentTime = new Date(timestamp);
  const diffSeconds = Math.floor((now - commentTime) / 1000);

  if (diffSeconds < 60) {
    return "刚刚";
  } else if (diffSeconds < 3600) {
    return `${Math.floor(diffSeconds / 60)}分钟前`;
  } else if (diffSeconds < 86400) {
    return `${Math.floor(diffSeconds / 3600)}小时前`;
  } else {
    return commentTime.toLocaleDateString("zh-CN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    });
  }
};

// 加载评论
const loadComments = async () => {
  if (!work.value?.id) return;

  try {
    const res = await photoApi.getComments(work.value.id);
    if (res.code === "200") {
      comments.value = res.data;

      // 更新作品的评论数
      if (comments.value.length > 0) {
        work.value.commentCount = comments.value.length;
      }
    }
  } catch (error) {
    console.error("加载评论失败:", error);
    ElMessage.error("加载评论失败");
  }
};

// 提交评论
const submitComment = async () => {
  if (!currentUser.value?.id) {
    ElMessage.warning("请先登录后再评论");
    return;
  }

  if (!newComment.value.trim()) {
    ElMessage.warning("评论内容不能为空");
    return;
  }

  submittingComment.value = true;

  try {
    const commentData = {
      workId: work.value.id,
      userId: currentUser.value.id,
      username: currentUser.value.username,
      comment: newComment.value.trim(),
    };

    const res = await photoApi.submitComment(commentData);
    if (res.code === "200") {
      // 添加新评论到列表顶部
      comments.value.unshift({
        ...res.data,
        avatar: currentUser.value.avatar,
      });

      // 更新评论计数
      work.value.commentCount = comments.value.length;

      // 清空输入框
      newComment.value = "";

      ElMessage.success("评论发表成功");
    }
  } catch (error) {
    console.error("提交评论失败:", error);
    ElMessage.error("评论发表失败");
  } finally {
    submittingComment.value = false;
  }
};

// 回复评论
const replyToComment = (comment) => {
  // 实现回复功能
  ElMessage.info("回复功能开发中");
};

// 在加载作品详情后加载评论
watch(
  () => work.value,
  (newWork) => {
    if (newWork) {
      loadComments();
    }
  }
);
//
//
//
// 增加浏览量的函数
const incrementView = async () => {
  if (!work.value?.id) return;
  try {
    const res = await photoApi.viewPhoto({
      workId: work.value.id,
    });
    if (res.code === "200") {
      work.value.viewCount = res.data.viewCount;

      console.log("浏览量", res.data);
    }
  } catch (error) {
    console.error("增加浏览量失败:", error);
  }
};

//点赞
// 处理点赞
const handleLike = async () => {
  if (!currentUser.value?.id) {
    ElMessage.warning("请先登录后再点赞");
    return;
  }
  if (!work.value?.id) return;

  likeLoading.value = true;
  try {
    const res = await photoApi.likePhoto({
      workId: work.value.id,
      userId: currentUser.value.id,
    });
    if (res.code === "200") {
      // 点赞状态切换
      isLiked.value = !isLiked.value;
      work.value.likeCount = res.data.likeCount;
      ElMessage.success(isLiked.value ? "点赞成功!" : "已取消点赞");

      console.log(res.data);
    }
  } catch (error) {
    ElMessage.error("操作失败，请稍后再试");
    console.error("点赞操作失败:", error);
  } finally {
    likeLoading.value = false;
  }
};

//收藏
const submitCollect = async () => {};

// 转发
const submitShare = async () => {};

// 自动轮播
let carouselTimer = null;
const startCarousel = () => {
  if (work.value?.images?.length > 1) {
    carouselTimer = setInterval(() => {
      currentIndex.value = (currentIndex.value + 1) % work.value.images.length;
    }, 3000);
  }
};

onMounted(async () => {
  loading.value = true;
  const id = route.params.id;
  try {
    const res = await photoApi.getPhotoDetail(id);
    if (res.code === "200" && res.data) {
      work.value = {
        title: "未命名作品",
        category: "landscape",
        username: "未知作者",
        avatar: "",
        followers: 0,
        camera: "未指定",
        locationAddress: "",
        locationLng: 0,
        locationLat: 0,
        description: "暂无描述",
        viewCount: 0,
        likeCount: 0,
        commentCount: 0,
        collections: 0,
        images: [],
        ...res.data,
      };
      work.value.cover =
        work.value.cover || work.value.images?.[0]?.imagePath || "";
      console.log("作品详情接口返回：", res.data);
    } else {
      work.value = null;
    }
  } catch (e) {
    console.error("加载作品详情失败:", e);
    work.value = null;
  } finally {
    loading.value = false;
  }

  if (work.value) {
    await incrementView(); // 先增加浏览量
    await checkLikeStatus(); // 再检查点赞状态
    startCarousel();
  }

  return () => {
    if (carouselTimer) {
      clearInterval(carouselTimer);
    }
  };
});

// 监听work变化，自动检查点赞状态（防止切换作品时状态不对）
watch(
  () => work.value?.id,
  (newVal) => {
    if (newVal) {
      checkLikeStatus();
    }
  }
);

watch(work, (val) => {
  console.log("work.avatar:", val?.avatar);
});
</script>

<style scoped>
/* 主要修改：使白色区域占满屏幕 */
.work-detail {
  width: 100%;
  background: white;
  min-height: 100vh;
  position: relative;
  display: flex;
  flex-direction: column;
}

/* 新增内容容器 */
.content-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

/* 返回按钮样式 */
.back-button {
  position: absolute;
  top: 20px;
  left: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 15px;
  background-color: #f5f5f5;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 10;
  font-size: 0.95rem;
  color: #5c5c5c;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.back-button:hover {
  background-color: #eaeaea;
  transform: translateX(-3px);
}

.back-button svg {
  width: 18px;
  height: 18px;
}

.work-header {
  text-align: center;
  margin-top: 40px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.work-header h1 {
  font-size: 2.2rem;
  margin-bottom: 15px;
  color: #2c3e50;
}

.work-tag {
  display: inline-block;
  padding: 6px 15px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  color: white;
}

.work-container {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  flex: 1;
}

/* 左侧大图区域样式 */
.gallery-section {
  flex: 1;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}

.gallery-container {
  display: flex;
  gap: 20px;
  flex: 1;
}

.main-image {
  flex: 1;
  min-height: 400px;
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  border-radius: 10px;
  background-color: #f8f9fa;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  transition: all 0.5s ease;
}

.thumbnails {
  width: 120px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.thumbnail-item {
  position: relative;
  width: 120px;
  height: 90px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  background: #f8f9fa;
}

.thumbnail-item.active {
  border-color: #9770db;
  transform: scale(1.05);
  box-shadow: 0 3px 8px rgba(151, 112, 219, 0.4);
}

.thumbnail-item:hover {
  transform: scale(1.05);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.thumbnail-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.thumbnail-item:hover img {
  transform: scale(1.1);
}

/* 作品统计信息样式 - 大图下方 */
.work-stats {
  display: flex;
  justify-content: space-around;
  background: #f9f7ff;
  border-radius: 12px;
  padding: 15px;
  margin-top: 20px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.05);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  color: #9770db;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 0.9rem;
  color: #777;
}

/* 右侧作者信息区域样式 */
.author-info {
  width: 320px;
  background: #f9f7ff;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.author-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.author-avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 15px;
  border: 2px solid #9770db;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.author-details h2 {
  font-size: 1.3rem;
  margin-bottom: 5px;
}

.author-details p {
  color: #777;
  font-size: 0.9rem;
}

.follow-btn {
  margin-left: auto;
  padding: 8px 18px;
  background: #9770db;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(151, 112, 219, 0.3);
}

.follow-btn:hover {
  background: #7a5db3;
  transform: translateY(-2px);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
  padding: 10px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.meta-item span {
  display: block;
  font-size: 0.85rem;
  color: #888;
}

.meta-item p {
  font-weight: 500;
  margin-top: 3px;
}

.work-description {
  margin: 25px 0;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.work-description h3 {
  font-size: 1.2rem;
  margin-bottom: 12px;
  color: #2c3e50;
}

.work-description p {
  line-height: 1.7;
  color: #555;
  padding: 0 10px;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  font-size: 1.2rem;
  color: #4a3f72;
  flex: 1;
}

.is-loading {
  font-size: 3rem;
  margin-bottom: 20px;
  animation: spin 2s linear infinite;
}

.no-results {
  margin: 50px auto;
  text-align: center;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 评论区域样式 */
.comments-section {
  background: #f9f7ff;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  margin-top: 20px;
}

.comments-section h2 {
  font-size: 1.5rem;
  margin-bottom: 20px;
  color: #2c3e50;
}

.comments-section h2 span {
  font-size: 1rem;
  color: #9770db;
}

.add-comment {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  padding-bottom: 25px;
  border-bottom: 1px solid #eee;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-input {
  flex: 1;
}

.comment-input textarea {
  width: 100%;
  height: 100px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 10px;
  resize: none;
  font-family: inherit;
  font-size: 1rem;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.comment-input textarea:focus {
  border-color: #9770db;
  outline: none;
  box-shadow: 0 0 0 2px rgba(151, 112, 219, 0.2);
}

.comment-input button {
  padding: 10px 25px;
  background: #9770db;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-weight: 500;
  float: right;
  transition: all 0.3s ease;
}

.comment-input button:hover {
  background: #7a5db3;
  transform: translateY(-2px);
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.comment-item {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.comment-avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.comment-header h4 {
  font-size: 1rem;
}

.comment-time {
  font-size: 0.85rem;
  color: #888;
}

.comment-content p {
  line-height: 1.6;
  margin-bottom: 10px;
  color: #444;
}

.comment-actions {
  display: flex;
  gap: 15px;
}

.comment-actions button {
  background: none;
  border: none;
  color: #777;
  font-size: 0.9rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  transition: color 0.2s ease;
}

.comment-actions button:hover {
  color: #9770db;
}

/* 回复区域 */
.replies {
  margin-top: 15px;
  padding-left: 15px;
  border-left: 2px solid #eee;
}

.reply-item {
  display: flex;
  gap: 10px;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.reply-header h5 {
  font-size: 0.9rem;
  font-weight: 500;
  color: #2c3e50;
}

.reply-time {
  font-size: 0.8rem;
  color: #888;
}

.reply-content p {
  font-size: 0.9rem;
  color: #555;
  line-height: 1.5;
}

.no-comments {
  text-align: center;
  padding: 30px 0;
  color: #888;
}

/* 响应式设计 */
@media (max-width: 900px) {
  .work-container {
    flex-direction: column;
  }

  .author-info {
    width: 100%;
  }

  .gallery-container {
    flex-direction: column;
  }

  .thumbnails {
    width: 100%;
    flex-direction: row;
    flex-wrap: wrap;
    margin-top: 15px;
  }

  .thumbnail-item {
    width: calc(25% - 10px);
    height: 80px;
  }
}

@media (max-width: 600px) {
  .work-header h1 {
    font-size: 1.8rem;
    margin-top: 30px;
  }

  .main-image {
    min-height: 300px;
  }

  .thumbnail-item {
    width: calc(33.33% - 8px);
    height: 70px;
  }

  .work-stats {
    flex-direction: column;
    gap: 10px;
    padding: 10px;
  }

  .stat-item {
    flex-direction: row;
    gap: 10px;
    justify-content: flex-start;
    width: 100%;
  }

  .stat-value {
    font-size: 1.3rem;
    margin-bottom: 0;
  }

  .author-header {
    flex-wrap: wrap;
  }

  .follow-btn {
    margin-top: 10px;
    margin-left: 0;
    width: 100%;
  }

  .back-button {
    top: 10px;
    left: 10px;
    font-size: 0.85rem;
    padding: 6px 12px;
  }

  .content-container {
    padding: 10px;
  }
}
</style>
