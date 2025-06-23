<template>
  <div class="home">
    <Navbar @upload="showUploadModal = true" />

    <!-- 搜索结果显示 -->
    <div v-if="searchKeyword" class="search-header">
      <h3>搜索结果: "{{ searchKeyword }}"</h3>
      <button @click="clearSearch" class="clear-btn">
        <i class="fas fa-times"></i> 清除搜索
      </button>
    </div>

    <!-- 分类轮播 -->
    <div class="categories-container">
      <div class="scroll-left" @click="scrollCategories('left')">
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
      </div>

      <div class="categories-scroll" ref="categoriesScroll">
        <div
          v-for="(category, index) in categories"
          :key="category.value"
          class="category-tag"
          :class="{ active: activeCategoryIndex === index }"
          @click="selectCategory(index)"
        >
          {{ category.label }}
        </div>
      </div>

      <div class="scroll-right" @click="scrollCategories('right')">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          fill="currentColor"
          viewBox="0 0 16 16"
        >
          <path
            d="M8 0a8 8 0 1 1 0 16A8 8 0 0 1 8 0zM4.5 7.5a.5.5 0 0 0 0 1h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H4.5z"
          />
        </svg>
      </div>
    </div>

    <!-- 作品展示 -->
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading">
        <Loading />
      </el-icon>
      加载中...
    </div>

    <div v-else-if="filteredWorks.length === 0" class="no-results">
      <el-empty description="未找到作品" />
    </div>

    <div v-else class="cards">
      <div
        v-for="(work, index) in filteredWorks"
        :key="work.id + '-' + index"
        class="card"
        :style="{
          'animation-delay': `${index * 0.05}s`,
          'background-image': `linear-gradient(to bottom right, #91defe, #99c0f9, #bdb6ec, #d7b3e3, #efb3d5, #f9bccc)`,
        }"
        @click="viewWorkDetail(work.id)"
      >
        <div class="card-img-container">
          <img
            :src="
              IMAGE_BASE_URL +
              (work.cover || (work.images && work.images[0]?.image_path) || '')
            "
            class="card-img"
            :alt="work.title"
          />
          <div class="overlay"></div>
          <div
            class="card-tag"
            :style="{ backgroundColor: categoryColors[work.category] }"
          >
            {{ getCategoryLabel(work.category) }}
          </div>
        </div>
        <div class="card-content">
          <h3 class="card-title">{{ work.title }}</h3>
          <div class="card-info">
            <span>by {{ work.username }}</span>
            <span>{{ work.likes }}人喜欢</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, onUnmounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElEmpty, ElIcon } from "element-plus";
import { Loading } from "@element-plus/icons-vue";
import Navbar from "../components/layout/Navbar.vue";
import { photoApi } from "@/utils/request.js";

const IMAGE_BASE_URL = "http://localhost:7080/uploads/";

const route = useRoute();
const router = useRouter();

// 分类选项
const categories = ref([
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
]);

// 状态变量
const activeCategoryIndex = ref(null);
const works = ref([]);
const loading = ref(false);
const searchKeyword = ref("");
const categoriesScroll = ref(null);

// 分类颜色映射
const categoryColors = {
  landscape: "#FF7D7D",
  urban: "#5B8FF9",
  portrait: "#FF9D4D",
  architecture: "#32C7A9",
  wildlife: "#FF99C3",
  food: "#FFD666",
  macro: "#9770DB",
  street: "#37A2DA",
};

// 获取分类标签文字
const getCategoryLabel = (categoryValue) => {
  const category = categories.value.find((cat) => cat.value === categoryValue);
  return category ? category.label : "";
};

// 选择分类
const selectCategory = (index) => {
  activeCategoryIndex.value = index;
};

// 滚动分类标签
const scrollCategories = (direction) => {
  if (!categoriesScroll.value) return;

  const scrollAmount = 200;
  const currentScroll = categoriesScroll.value.scrollLeft;
  const maxScroll =
    categoriesScroll.value.scrollWidth - categoriesScroll.value.clientWidth;

  if (direction === "left") {
    categoriesScroll.value.scrollTo({
      left: Math.max(0, currentScroll - scrollAmount),
      behavior: "smooth",
    });
  } else {
    categoriesScroll.value.scrollTo({
      left: Math.min(maxScroll, currentScroll + scrollAmount),
      behavior: "smooth",
    });
  }
};

// 查看作品详情
const viewWorkDetail = (workId) => {
  router.push({ name: "WorkDetail", params: { id: workId } });
};

// 清除搜索
const clearSearch = () => {
  searchKeyword.value = "";
  router.push({ query: {} });
};

// 加载作品数据
const loadWorks = async () => {
  try {
    loading.value = true;

    const params = {
      sort: "createdAt",
      dir: "desc",
    };

    // 如果有搜索关键词，添加查询参数
    if (searchKeyword.value) {
      params.query = searchKeyword.value;
    }

    const res = await photoApi.getApproved(params);

    if (res.code === "200") {
      works.value = res.data;
    } else {
      console.error("获取作品数据失败:", res.msg);
    }
  } catch (error) {
    console.error("获取作品数据时出错:", error);
  } finally {
    loading.value = false;
  }
};

// 监听路由查询参数变化
const stopRouteWatch = watch(
  () => route.query.search,
  (newSearch) => {
    searchKeyword.value = newSearch || "";
    loadWorks();
  }
);

// 组件挂载时初始化
onMounted(() => {
  searchKeyword.value = route.query.search || "";
  loadWorks();
});

// 组件卸载时停止监听
onUnmounted(() => {
  stopRouteWatch();
});

// 过滤作品
const filteredWorks = computed(() => {
  // 应用分类过滤
  let result = works.value;

  if (activeCategoryIndex.value !== null) {
    const category = categories.value[activeCategoryIndex.value].value;
    result = result.filter((work) => work.category === category);
  }

  return result;
});
</script>

<style scoped>
/* 新增分类容器样式 */
.categories-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 20px 0 30px;
  gap: 8px;
  padding: 0 10px;
}

.scroll-left,
.scroll-right {
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 36px;
  height: 36px;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.scroll-left:hover,
.scroll-right:hover {
  background-color: rgba(147, 112, 219, 0.2);
  transform: scale(1.1);
}

.scroll-left svg,
.scroll-right svg {
  width: 20px;
  height: 20px;
}

.categories-scroll {
  display: flex;
  overflow-x: auto;
  scroll-behavior: smooth;
  gap: 10px;
  padding: 5px 0;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
  max-width: 800px;
}

/* 隐藏滚动条 */
.categories-scroll::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.category-tag {
  padding: 8px 20px;
  background-color: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
  flex-shrink: 0;
}

.category-tag:hover {
  background-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-2px);
}

.category-tag.active {
  background-color: rgba(147, 112, 219, 0.6);
  border-color: rgba(147, 112, 219, 0.8);
  color: white;
}

/* 卡片网格布局 */
.cards {
  display: grid;
  gap: 2rem;
  grid-auto-flow: dense;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  margin: auto;
  width: 100%;
}

/* 视差动画关键帧 */
@keyframes slide-fade-in {
  from {
    opacity: 0;
    box-shadow: none;
    transform: scale(0.8) translateY(15vh);
  }
}

.card {
  border-radius: 12px;
  box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  background: white;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  transform-origin: center;
  animation: slide-fade-in both;
  animation-timeline: view();
  animation-range: contain 0% contain 50%;
}

.card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 30px rgba(0, 0, 0, 0.2);
  z-index: 10;
}

/* 特殊卡片布局 */
.card:nth-of-type(4n) {
  grid-column: span 2;
}

.card:nth-of-type(7n) {
  grid-row: span 2;
}

.card-img-container {
  width: 100%;
  height: 250px;
  overflow: hidden;
  position: relative;
}

.card:nth-of-type(7n) .card-img-container {
  height: 100%;
}

.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.card:hover .card-img {
  transform: scale(1.1);
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7) 0%, transparent 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.card:hover .overlay {
  opacity: 1;
}

.card-content {
  padding: 15px;
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  color: white;
  transform: translateY(40px);
  transition: transform 0.3s ease;
  z-index: 2;
}

.card:hover .card-content {
  transform: translateY(0);
}

.card-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.8);
}

.card-tag {
  position: absolute;
  top: 15px;
  right: 15px;
  padding: 4px 12px;
  border-radius: 15px;
  font-size: 0.8rem;
  font-weight: 500;
  color: white;
  z-index: 2;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  font-size: 1.2rem;
  color: #4a3f72;
}

.is-loading {
  font-size: 3rem;
  margin-bottom: 20px;
  animation: spin 2s linear infinite;
}

.no-results {
  margin: 50px auto;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .categories-container {
    padding: 0 5px;
  }

  .scroll-left,
  .scroll-right {
    min-width: 30px;
    height: 30px;
  }

  .category-tag {
    padding: 6px 15px;
    font-size: 13px;
  }

  .cards {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }

  .card:nth-of-type(4n),
  .card:nth-of-type(7n) {
    grid-column: auto;
    grid-row: auto;
  }
}

.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f7d6ff;
  padding-bottom: 30px;
}

/* 添加搜索头部样式 */
.search-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 20px 30px 10px;
  padding: 15px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.search-header h3 {
  font-size: 1.2rem;
  color: #4a3f72;
  margin: 0;
}

.clear-btn {
  background: #f0f0f0;
  border: none;
  border-radius: 20px;
  padding: 8px 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 5px;
}

.clear-btn:hover {
  background: #e0e0e0;
}
</style>
