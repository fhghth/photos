<template>
  <div class="map-page">
    <Navbar />

    <div class="map-container">
      <div class="map-title">
        <p>探索所有摄影作品的地理位置</p>
      </div>

      <div class="map-wrapper">
        <div ref="mapEl" class="map"></div>

        <div class="map-filters">
          <el-select
            v-model="selectedCategory"
            placeholder="所有分类"
            clearable
          >
            <el-option
              v-for="category in categories"
              :key="category.value"
              :label="category.label"
              :value="category.value"
            />
          </el-select>

          <!-- 新增：分类图例 -->
          <div class="legend-container">
            <el-popover
              placement="bottom"
              title="分类图例"
              :width="250"
              trigger="hover"
            >
              <template #reference>
                <el-button class="legend-btn" icon="el-icon-tickets">
                  查看图例
                </el-button>
              </template>
              <div class="legend-content">
                <div
                  v-for="category in categories"
                  :key="category.value"
                  class="legend-item"
                >
                  <div
                    class="color-indicator"
                    :style="{ backgroundColor: categoryColors[category.value] }"
                  ></div>
                  <span class="category-name">{{ category.label }}</span>
                </div>
              </div>
            </el-popover>
          </div>

          <div class="map-actions">
            <el-button
              type="primary"
              @click="refreshMap"
              icon="el-icon-refresh"
            >
              刷新地图
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import Navbar from "../../components/layout/Navbar.vue";
import { adminApi } from "../../utils/request.js";

const route = useRoute();
const router = useRouter();

// 分类选项（与上传表单一致）
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

// 分类颜色映射
const categoryColors = ref({
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
  abstract: "#B39DDB",
  // 默认颜色
  default: "#3498db",
});

// 状态变量
const mapEl = ref(null);
const selectedCategory = ref("");
const photos = ref([]);
let mapInstance = null;
let markers = [];

// 加载高德地图API
const loadAMapScript = () => {
  return new Promise((resolve, reject) => {
    if (window.AMap) return resolve();

    window.amapLoading = true;
    const script = document.createElement("script");
    script.src = `https://webapi.amap.com/maps?v=2.0&key=your_apikey`;
    script.onload = () => {
      window.amapLoading = false;
      resolve();
    };
    script.onerror = (error) => reject(error);
    document.head.appendChild(script);
  });
};

// 初始化地图
const initMap = async () => {
  try {
    // 确保地图容器已准备好
    await nextTick();

    if (!mapEl.value) {
      console.error("地图容器未找到");
      return false;
    }

    // 创建地图实例
    mapInstance = new AMap.Map(mapEl.value, {
      zoom: 4,
      center: [105.375, 38.861],
      viewMode: "2D",
      resizeEnable: true,
      mapStyle: "amap://styles/grey",
    });

    // 添加控件
    mapInstance.addControl(new AMap.ControlBar());
    mapInstance.addControl(new AMap.Scale());
    // mapInstance.addControl(new AMap.HawkEye());

    console.log("地图初始化完成");
    return true;
  } catch (error) {
    console.error("地图初始化失败:", error);
    return false;
  }
};

// 获取数据
const fetchPhotos = async () => {
  try {
    const res = await adminApi.content.getAllPhotosMap();
    if (res.code === "200" && res.data) {
      photos.value = res.data;
      console.log("获取作品数据成功，共", photos.value.length, "件作品");

      // 过滤掉没有位置信息的作品
      return photos.value.filter(
        (photo) => photo.locationLng && photo.locationLat
      );
    } else {
      ElMessage.error("获取作品数据失败: " + res.msg);
      return [];
    }
  } catch (error) {
    console.error("获取作品数据失败:", error);
    ElMessage.error("获取作品数据失败: " + error.message);
    return [];
  }
};

// 添加标记点到地图
const addMarkersToMap = async () => {
  if (!mapInstance) {
    console.error("地图未初始化");
    return;
  }

  // 清除现有标记
  clearMarkers();

  // 获取过滤后的照片数据
  const validPhotos = await fetchPhotos();

  // 检查是否有有效照片
  if (validPhotos.length === 0) {
    ElMessage.info("没有找到有位置信息的作品");
    return;
  }

  // 创建标记数组
  markers = validPhotos
    .map((photo) => {
      // 如果没有位置信息，跳过
      if (!photo.locationLng || !photo.locationLat) return null;

      // 如果有分类筛选且照片分类不匹配，跳过
      if (selectedCategory.value && photo.category !== selectedCategory.value) {
        return null;
      }

      // 解析位置信息
      const lng = parseFloat(photo.locationLng);
      const lat = parseFloat(photo.locationLat);

      if (isNaN(lng) || isNaN(lat)) return null;

      // 获取分类颜色，如果未定义使用默认颜色
      const color =
        categoryColors.value[photo.category] || categoryColors.value.default;

      // 创建标记
      const marker = new AMap.Marker({
        position: [lng, lat],
        offset: new AMap.Pixel(-13, -30),
        content: createMarkerContent(photo, color),
        extData: photo, // 存储照片原始数据
      });

      // 添加点击事件
      marker.on("click", (e) => {
        const clickedPhoto = e.target.getExtData();
        if (clickedPhoto) {
          showPhotoDetail(clickedPhoto, color);
        }
      });

      return marker;
    })
    .filter((marker) => marker !== null); // 过滤掉无效标记

  // 将所有标记添加到地图
  markers.forEach((marker) => mapInstance.add(marker));

  // 调整地图视图以包含所有标记
  mapInstance.setFitView(markers, true, [60, 60, 60, 60]);

  console.log("成功添加", markers.length, "个标记到地图");
};

// 清除所有标记
const clearMarkers = () => {
  if (markers.length > 0) {
    markers.forEach((marker) => mapInstance.remove(marker));
    markers = [];
  }
};

// 创建标记点内容
const createMarkerContent = (photo, color) => {
  // 使用内联样式确保正确渲染
  return `
    <div style="position: relative; display: flex; flex-direction: column; align-items: center; cursor: pointer;">
      <div style="width: 24px; height: 24px; border-radius: 50%; background: ${color}; position: relative; z-index: 1; box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);">
        <div style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; border-radius: 50%; background: inherit; opacity: 0.8; z-index: -1;"></div>
      </div>
      <div style="position: absolute; top: 30px; background: white; padding: 4px 8px; border-radius: 4px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2); font-size: 12px; white-space: nowrap; opacity: 0; transform: translateY(10px); transition: all 0.3s ease; pointer-events: none;">
        ${photo.title}
      </div>
    </div>
  `;
};

// 显示照片详情
const showPhotoDetail = (photo, color) => {
  const categoryLabel =
    categories.value.find((c) => c.value === photo.category)?.label || "其他";

  // 信息窗口内容
  const content = `
    <div style="
      padding: 15px; 
      min-width: 280px; 
      max-width: 350px; 
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
      background: rgba(255, 255, 255, 0.95);
      border-radius: 8px;
      box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
      position: relative;
      border: 1px solid #e1e4e8;
    ">
      <div style="
        position: absolute; 
        top: 0; 
        left: 0; 
        width: 5px; 
        height: 100%; 
        background: ${color};
        border-radius: 8px 0 0 8px;
      "></div>
      
      <div style="margin-left: 15px; padding: 0 10px;">
        <h3 style="
          margin: 0 0 10px 0; 
          font-size: 1.3rem; 
          color: #2c3e50;
          padding-bottom: 8px;
          border-bottom: 1px solid #eaecef;
        ">
          ${photo.title}
        </h3>
        
        <div style="
          background: rgba(245, 247, 250, 0.5);
          border-radius: 6px;
          padding: 12px;
        ">
          <div class="info-grid">
            <div class="info-label">作者:</div>
            <div class="info-value">${photo.username || "未知"}</div>
            
            <div class="info-label">分类:</div>
            <div class="info-value">${categoryLabel}</div>
            
            <div class="info-label">地点:</div>
            <div class="info-value">${photo.locationAddress || "未知"}</div>
            
            <div class="info-label">坐标:</div>
            <div class="info-value">${photo.locationLng}, ${
    photo.locationLat
  }</div>
          </div>
        </div>
        
        <div style="
          margin-top: 15px; 
          text-align: center;
          background: rgba(245, 247, 250, 0.5);
          border-radius: 6px;
          padding: 10px;
        ">
          <img 
            src="http://localhost:7080/uploads/${photo.cover}" 
            alt="${photo.title}" 
            style="
              width:100px;
              height:50px
              object-fit: contain; 
              border: 1px solid #e1e4e8;
              border-radius: 4px;
              background: white;
              padding: 5px;
              box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            "
          >
        </div>
        
        <div style="margin-top: 15px; text-align: center;">
          <button 
            onclick="window.location.href='/work/${photo.id}'" 
            style="
              background: ${color}; 
              color: white; 
              border: none; 
              border-radius: 4px; 
              padding: 10px 20px; 
              font-size: 1rem; 
              cursor: pointer; 
              transition: all 0.3s;
              width: 100%;
              box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            "
            onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 8px rgba(0,0,0,0.2)'" 
            onmouseout="this.style.transform='none'; this.style.boxShadow='0 4px 6px rgba(0,0,0,0.1)'"
          >
            查看作品详情
          </button>
        </div>
      </div>
    </div>
  `;

  // 创建信息窗口
  const infoWindow = new AMap.InfoWindow({
    content: content,
    offset: new AMap.Pixel(0, -40),
    closeWhenClickMap: true,
    isCustom: true,
    autoMove: true,
  });

  // 获取标记位置
  const position = [
    parseFloat(photo.locationLng),
    parseFloat(photo.locationLat),
  ];

  // 打开信息窗口
  infoWindow.open(mapInstance, position);
};

// 刷新地图数据
const refreshMap = async () => {
  ElMessage.info("正在刷新地图数据...");
  try {
    await addMarkersToMap();
    ElMessage.success("地图数据已刷新");
  } catch (error) {
    ElMessage.error("刷新失败: " + error.message);
  }
};

// 页面加载时初始化
onMounted(async () => {
  try {
    // 加载地图API
    await loadAMapScript();

    // 初始化地图
    const mapSuccess = await initMap();

    if (mapSuccess) {
      // 等待DOM和地图都ready
      await nextTick();
      // 自动添加标记点
      await addMarkersToMap();

      // 检查上传成功的标识
      if (route.query.uploadSuccess) {
        ElMessage.success("作品上传成功！已添加到地图");
      }
    }
  } catch (error) {
    console.error("初始化失败:", error);
    ElMessage.error("地图初始化失败: " + error.message);
  }
});

// 监听分类筛选变化
watch(selectedCategory, (newVal) => {
  if (mapInstance) {
    addMarkersToMap();
  }
});
</script>

<style scoped>
.map-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.map-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
  background: #f5f7fa;
}

.map-title {
  text-align: center;
  margin-bottom: 20px;
}

.map-title p {
  color: #7f8c8d;
  font-size: 1.1rem;
  font-weight: 500;
}

.map-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  height: calc(100vh - 100px);
}

.map {
  width: 100%;
  flex: 1;
  min-height: 400px;
}

.map-filters {
  padding: 15px 20px;
  background: white;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

:deep(.el-select) {
  flex: 1;
  min-width: 150px;
  max-width: 300px;
  margin-right: 0;
}

.legend-container {
  display: flex;
  align-items: center;
}

.legend-btn {
  margin: 0;
  border: 1px solid #dcdfe6;
  background-color: #f5f7fa;
}

.legend-content {
  padding: 10px;
}

.legend-item {
  display: flex;
  align-items: center;
  padding: 8px 5px;
  border-bottom: 1px solid #f0f0f0;
}

.legend-item:last-child {
  border-bottom: none;
}

.color-indicator {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  margin-right: 10px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  flex-shrink: 0;
}

.category-name {
  font-size: 0.9rem;
  color: #5a6c80;
}

/* 标记点悬停效果 */
.amap-marker:hover > div > div:first-child > div:last-child {
  opacity: 1;
  transform: translateY(0);
}

/* 动画关键帧 - 添加全局动画定义 */
@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.8;
  }
  70% {
    transform: scale(2);
    opacity: 0;
  }
  100% {
    transform: scale(1);
    opacity: 0;
  }
}

/* 响应式调整 */
@media (max-width: 480px) {
  .amap-info-content {
    max-width: 280px !important;
  }

  .info-grid {
    grid-template-columns: 80px 1fr;
  }
}

.map-actions {
  display: flex;
  align-items: center;
  margin-left: auto;
  gap: 10px;
}
/* 为信息窗口添加全局样式覆盖 */
.amap-info-content {
  background: transparent !important;
  border-radius: 8px !important;
  overflow: visible !important;
  border: none !important;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2) !important;
}

/* 为网格布局添加样式 */
.info-grid {
  display: grid;
  grid-template-columns: 70px 1fr;
  gap: 8px;
  font-size: 0.95rem;
}
.info-label {
  font-weight: bold;
  color: #57606a;
}

.info-value {
  color: #24292e;
}
</style>
