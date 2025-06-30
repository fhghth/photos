<template>
  <div class="background-container">
    <div class="a">
      <div class="nav">
        <img
          id="rotateChicken"
          alt="旋转鸡块"
          style="
            border-radius: 50%;
            object-fit: cover;
            width: 100px;
            height: 100px;
          "
        />
      </div>
      <div class="b">
        <div>
          <h1>摄影作品上传平台</h1>
          <p>分享您的摄影作品，记录美好瞬间，探索世界各地的摄影地点</p>
        </div>
      </div>
    </div>
    <div id="app">
      <div class="upload-container">
        <div class="upload-card">
          <div class="card-header">
            <h2>上传摄影作品</h2>
          </div>

          <div class="card-content">
            <div class="upload-form">
              <!-- 左侧：图片上传区域 -->
              <div class="form-left">
                <div class="form-section">
                  <h3><i class="el-icon-picture"></i> 作品图片</h3>

                  <div class="upload-area" @click="triggerFileInput">
                    <div class="upload-icon">
                      <i class="el-icon-upload"></i>
                    </div>
                    <div class="upload-text">点击或拖拽图片到此处上传</div>
                    <div class="upload-hint">
                      支持JPG、PNG格式，每张不超过10MB
                    </div>
                    <input
                      type="file"
                      ref="fileInput"
                      multiple
                      accept="image/*"
                      style="display: none"
                      @change="handleFileChange"
                    />
                  </div>

                  <div class="preview-container">
                    <div
                      v-for="(file, index) in form.files"
                      :key="index"
                      class="preview-item"
                      @click="setCover(index)"
                    >
                      <img :src="file.preview" alt="预览" />
                      <div class="cover-badge" v-if="file.isCover">封面</div>
                      <div class="preview-actions">
                        <button @click.stop="previewImage(index)">
                          <i class="el-icon-view"></i>
                        </button>
                        <button @click.stop="removeFile(index)">
                          <i class="el-icon-delete"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="form-section">
                  <h3><i class="el-icon-document"></i> 作品信息</h3>

                  <el-form-item label="作品标题" required class="title-input">
                    <el-input
                      v-model="form.title"
                      placeholder="请输入作品标题"
                      clearable
                      maxlength="50"
                      show-word-limit
                    />
                  </el-form-item>

                  <el-form-item label="作品分类" required>
                    <el-select
                      v-model="form.category"
                      placeholder="请选择分类"
                      class="full-width"
                      filterable
                    >
                      <el-option
                        v-for="category in categories"
                        :key="category.value"
                        :label="category.label"
                        :value="category.value"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="作品描述">
                    <el-input
                      v-model="form.description"
                      type="textarea"
                      :rows="4"
                      placeholder="请描述您的作品"
                      resize="vertical"
                      maxlength="500"
                      show-word-limit
                    />
                  </el-form-item>
                </div>

                <!-- 水印 -->
                <div class="form-section">
                  <h3><i class="el-icon-collection-tag"></i>添加水印</h3>
                  <el-button plain @click="openWatermarkDialog">
                    添加水印
                  </el-button>

                  <el-dialog
                    v-model="dialogOverflowVisible"
                    title="添加水印"
                    width="600px"
                    draggable
                    overflow
                  >
                    <div class="watermark-dialog">
                      <div class="watermark-preview">
                        <el-watermark
                          :content="watermarkConfig.content"
                          :font="watermarkConfig.font"
                          :z-index="watermarkConfig.zIndex"
                          :rotate="watermarkConfig.rotate"
                          :gap="watermarkConfig.gap"
                          :offset="watermarkConfig.offset"
                        >
                          <div class="preview-image-container">
                            <img
                              v-if="currentWatermarkImage"
                              :src="currentWatermarkImage"
                              class="preview-image"
                            />
                            <div v-else class="no-image-tip">
                              请选择一张图片进行水印预览
                            </div>
                          </div>
                        </el-watermark>
                      </div>

                      <el-form
                        class="watermark-form"
                        :model="watermarkConfig"
                        label-position="top"
                        label-width="120px"
                      >
                        <el-form-item label="水印内容">
                          <el-input v-model="watermarkConfig.content" />
                        </el-form-item>
                        <el-form-item label="水印颜色">
                          <el-color-picker
                            v-model="watermarkConfig.font.color"
                            show-alpha
                          />
                        </el-form-item>
                        <el-form-item label="字体大小">
                          <el-slider
                            v-model="watermarkConfig.font.fontSize"
                            :min="12"
                            :max="48"
                          />
                        </el-form-item>
                        <el-form-item label="透明度">
                          <el-slider
                            v-model="watermarkConfig.font.opacity"
                            :min="0.1"
                            :max="1"
                            :step="0.1"
                          />
                        </el-form-item>
                        <el-form-item label="旋转角度">
                          <el-slider
                            v-model="watermarkConfig.rotate"
                            :min="-180"
                            :max="180"
                          />
                        </el-form-item>
                        <el-form-item label="水平间距">
                          <el-input-number
                            v-model="watermarkConfig.gap[0]"
                            :min="50"
                            :max="300"
                          />
                        </el-form-item>
                        <el-form-item label="垂直间距">
                          <el-input-number
                            v-model="watermarkConfig.gap[1]"
                            :min="50"
                            :max="300"
                          />
                        </el-form-item>
                      </el-form>
                    </div>

                    <template #footer>
                      <div class="dialog-footer">
                        <el-button @click="dialogOverflowVisible = false">
                          取消
                        </el-button>
                        <el-button
                          type="primary"
                          @click="applyWatermarkToAllImages"
                        >
                          应用水印到所有图片
                        </el-button>
                      </div>
                    </template>
                  </el-dialog>
                </div>
              </div>

              <!-- 右侧：地图区域 -->
              <div class="form-right">
                <div class="form-section">
                  <h3><i class="el-icon-location-information"></i> 拍摄地点</h3>

                  <div class="map-controls">
                    <el-input
                      v-model="searchKeyword"
                      placeholder="搜索拍摄地点"
                      clearable
                      @change="searchLocation"
                      @clear="clearSearch"
                    >
                      <template #append>
                        <el-button @click="searchLocation">
                          <i class="el-icon-search"></i>
                        </el-button>
                      </template>
                    </el-input>
                    <el-button @click="useCurrentLocation">
                      <i class="el-icon-location"></i> 使用当前位置
                    </el-button>
                  </div>

                  <div class="map-container">
                    <div ref="mapContainer" class="map-box"></div>
                    <div class="map-actions">
                      <button @click="zoomIn" :disabled="zoomLevel >= 20">
                        <i class="el-icon-zoom-in"></i>
                      </button>
                      <button @click="zoomOut" :disabled="zoomLevel <= 3">
                        <i class="el-icon-zoom-out"></i>
                      </button>
                      <button @click="resetMapView">
                        <i class="el-icon-refresh"></i>
                      </button>
                      <button @click="saveLocation" title="收藏此位置">
                        <i class="el-icon-star-off"></i>
                      </button>
                    </div>
                  </div>

                  <div class="location-info">
                    <div class="coordinates">
                      <span>经度: {{ form.location.lng || "未选择" }}</span>
                      <span>纬度: {{ form.location.lat || "未选择" }}</span>
                    </div>
                    <div class="address" v-if="form.location.address">
                      <el-tag type="info">{{ form.location.address }}</el-tag>
                    </div>
                  </div>

                  <div class="search-results" v-if="searchResults.length > 0">
                    <div
                      v-for="result in searchResults"
                      :key="result.id"
                      class="search-result-item"
                      @click="selectSearchResult(result)"
                    >
                      <div class="result-name">{{ result.name }}</div>
                      <div class="result-address">{{ result.address }}</div>
                      <div class="result-distance" v-if="result.distance">
                        距离: {{ result.distance.toFixed(1) }} km
                      </div>
                    </div>
                  </div>
                </div>

                <div class="form-section">
                  <h3><i class="el-icon-collection-tag"></i> 位置收藏</h3>

                  <div class="saved-locations">
                    <el-tag
                      v-for="(location, index) in savedLocations"
                      :key="index"
                      class="location-tag"
                      @click="applySavedLocation(location)"
                    >
                      {{ location.name || location.address }}
                    </el-tag>

                    <div v-if="savedLocations.length === 0" class="empty-tips">
                      暂无收藏位置，点击地图上的⭐可收藏位置
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-actions">
              <el-button
                type="primary"
                class="submit-btn"
                :loading="uploading"
                @click="submitForm"
              >
                <i class="el-icon-upload"></i>
                {{ uploading ? "上传中..." : "提交作品" }}
              </el-button>
              <el-button @click="resetForm" plain>
                <i class="el-icon-refresh"></i> 重置
              </el-button>
              <el-button @click="goBack" plain>
                <i class="el-icon-back"></i> 返回
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from "vue";
import { photoApi } from "../../utils/request";
import { format } from "date-fns";
import {
  ElMessage,
  ElLoading,
  ElButton,
  ElInput,
  ElSelect,
  ElOption,
  ElForm,
  ElFormItem,
  ElTag,
  ElWatermark,
} from "element-plus";
import { useRouter } from "vue-router";

const dialogOverflowVisible = ref(false);
const router = useRouter();
const mapContainer = ref(null);

// 水印配置
const watermarkConfig = reactive({
  content: "摄影作品",
  font: {
    fontSize: 24,
    fontWeight: "bold",
    color: "rgba(0, 0, 0, 0.3)",
  },
  zIndex: 9,
  rotate: -22,
  gap: [100, 100],
  offset: [20, 20],
});

// 当前水印预览图片
const currentWatermarkImage = ref(null);

// 表单数据
const form = reactive({
  title: "",
  category: "",
  description: "",
  location: {
    lng: null,
    lat: null,
    address: "",
  },
  files: [],
  ctime: "",
  username: "",
});

// 旋转鸡块
const Chicken = {
  container: null,
  frames: [],
  max_frames: 7,
  current_frame: 0,
  target_frame: 0,
  chicken_timer: null,
  animation_player: null,
  init() {
    this.frames = [];
    for (let i = 1; i <= this.max_frames; i++) {
      let frame = new Image();
      frame.src = new URL(
        `../../assets/workUpImg/${i}.jpg`,
        import.meta.url
      ).href;
      this.frames.push(frame);
    }
    if (this.container) {
      this.container.src = this.frames[0].src;
    }
  },
  rotate_chicken(x, y) {
    if (!this.container) return;
    clearTimeout(this.chicken_timer);
    clearInterval(this.animation_player);
    let nums = (x - window.innerWidth / 2) / (y - window.innerHeight / 2);
    let rotate_angle;
    if (y >= window.innerHeight / 2)
      rotate_angle = (Math.atan(nums) * 180) / Math.PI + 90;
    else rotate_angle = 360 - 90 + (Math.atan(nums) * 180) / Math.PI;
    this.target_frame = parseInt(
      (360 - rotate_angle) / (360 / this.max_frames)
    );
    this.target_frame =
      (this.target_frame - 1 + this.max_frames) % this.max_frames;
    this.container.style.transform = `rotate(${-rotate_angle + 20}deg)`;
    this.chicken_timer = setTimeout(() => {
      this.animation_player = setInterval(this.chicken_move.bind(this), 30);
    }, 100);
  },
  chicken_move() {
    if (this.current_frame !== this.target_frame) {
      let direction = this.current_frame < this.target_frame ? 1 : -1;
      this.current_frame += direction;
      if (this.current_frame >= this.max_frames) this.current_frame = 0;
      if (this.current_frame < 0) this.current_frame = this.max_frames - 1;
      this.container.src = this.frames[this.current_frame].src;
    } else {
      clearInterval(this.animation_player);
    }
  },
};

onMounted(() => {
  Chicken.container = document.getElementById("rotateChicken");
  Chicken.init();
  window.addEventListener("mousemove", (event) => {
    Chicken.rotate_chicken(event.clientX, event.clientY);
  });
});
onUnmounted(() => {
  window.removeEventListener("mousemove", Chicken.rotate_chicken);
});

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

// 初始化用户信息
const initUserInfo = () => {
  try {
    // 从localStorage中获取用户信息
    const userData = JSON.parse(localStorage.getItem("code_user") || "{}");

    // 设置用户名 - 优先使用username，没有则使用name
    if (userData.username) {
      form.username = userData.username;
    } else if (userData.name) {
      form.username = userData.name;
    }

    // 设置创建时间 - 使用当前时间
    form.ctime = format(new Date(), "yyyy-MM-dd HH:mm:ss");

    console.log("用户信息初始化成功:", form.username, form.ctime);
  } catch (error) {
    console.error("用户信息初始化失败:", error);
    ElMessage.warning("用户信息初始化失败");
  }
};

// 状态变量
const uploading = ref(false);
const searchKeyword = ref("");
const searchResults = ref([]);
const zoomLevel = ref(11);
const savedLocations = ref([]);
const fileInput = ref(null); // 文件输入引用

// 地图相关变量
let mapInstance = null;
let marker = null;
let geocoderInstance = null;
let placeSearchInstance = null;

// 打开水印对话框
const openWatermarkDialog = () => {
  if (form.files.length === 0) {
    ElMessage.warning("请先上传图片");
    return;
  }

  // 使用封面图片或第一张图片作为预览
  const coverFile = form.files.find((file) => file.isCover);
  currentWatermarkImage.value = coverFile
    ? coverFile.preview
    : form.files[0].preview;

  dialogOverflowVisible.value = true;
};

// 应用水印到所有图片
const applyWatermarkToAllImages = async () => {
  try {
    const loading = ElLoading.service({
      lock: true,
      text: "正在添加水印并保存...",
    });

    // 处理所有图片
    for (const fileObj of form.files) {
      // 创建画布
      const canvas = document.createElement("canvas");
      const ctx = canvas.getContext("2d");

      // 加载图片
      const img = new Image();
      img.src = fileObj.preview;

      // 等待图片加载
      await new Promise((resolve) => {
        img.onload = resolve;
      });

      // 设置画布尺寸
      canvas.width = img.width;
      canvas.height = img.height;

      // 绘制原图
      ctx.drawImage(img, 0, 0);

      // 添加水印
      ctx.globalAlpha = watermarkConfig.font.colorAlpha || 0.3;
      ctx.font = `${watermarkConfig.font.fontSize}px ${
        watermarkConfig.font.family || "Arial"
      }`;
      ctx.fillStyle = watermarkConfig.font.color;
      ctx.textAlign = "center";
      ctx.textBaseline = "middle";

      // 计算水印位置（覆盖整个图片）
      const gapX = watermarkConfig.gap[0] || 200;
      const gapY = watermarkConfig.gap[1] || 200;
      const offsetX = watermarkConfig.offset[0] || 0;
      const offsetY = watermarkConfig.offset[1] || 0;

      for (let x = offsetX; x < canvas.width; x += gapX) {
        for (let y = offsetY; y < canvas.height; y += gapY) {
          ctx.save();
          ctx.translate(x, y);
          ctx.rotate((watermarkConfig.rotate * Math.PI) / 180);
          ctx.fillText(watermarkConfig.content, 0, 0);
          ctx.restore();
        }
      }

      // 将画布转换为Blob
      const blob = await new Promise((resolve) => {
        canvas.toBlob(
          (blob) => {
            resolve(blob);
          },
          "image/jpeg",
          0.9
        );
      });

      // 创建新的文件对象
      const newFile = new File([blob], fileObj.file.name, {
        type: "image/jpeg",
        lastModified: Date.now(),
      });

      // 更新文件对象 - 替换原始文件
      fileObj.file = newFile;
      fileObj.preview = URL.createObjectURL(blob);

      // 标记为已添加水印
      fileObj.hasWatermark = true;
    }

    ElMessage.success("水印已应用到所有图片");
    dialogOverflowVisible.value = false;
  } catch (error) {
    console.error("添加水印失败:", error);
    ElMessage.error("添加水印失败");
  } finally {
    ElLoading.service().close();
  }
};

// 初始化地图
const initMap = async () => {
  await nextTick();

  // 确保地图容器存在
  if (!mapContainer.value) {
    console.error("地图容器未找到");
    return;
  }

  try {
    mapInstance = new AMap.Map(mapContainer.value, {
      zoom: 11,
      center: [116.397428, 39.90923],
      resizeEnable: true,
    });

    mapInstance.on("zoomchange", () => {
      zoomLevel.value = mapInstance.getZoom();
    });

    mapInstance.on("click", async (e) => {
      const loading = ElLoading.service({
        target: mapContainer.value,
        text: "定位中...",
      });

      try {
        const lng = e.lnglat.lng;
        const lat = e.lnglat.lat;

        form.location.lng = lng;
        form.location.lat = lat;

        if (marker) {
          mapInstance.remove(marker);
        }

        marker = new AMap.Marker({
          position: [lng, lat],
          offset: new AMap.Pixel(-15, -30),
          title: "拍摄地点",
        });

        mapInstance.add(marker);
        mapInstance.setCenter([lng, lat]);
        mapInstance.setZoom(15);

        try {
          const result = await reverseGeocode(lng, lat);
          if (result) {
            form.location.address = result.formattedAddress;
          }
        } catch (error) {
          console.error("逆地理编码错误:", error);
          // 如果逆地理编码失败，使用经纬度作为地址
          form.location.address = `经度: ${lng.toFixed(6)}, 纬度: ${lat.toFixed(
            6
          )}`;
          ElMessage.warning("获取详细地址失败，已使用经纬度作为位置信息");
        }
      } catch (error) {
        ElMessage.error("地图操作失败");
        console.error(error);
      } finally {
        loading.close();
      }
    });

    geocoderInstance = new AMap.Geocoder({
      city: "全国",
      radius: 1000,
    });

    placeSearchInstance = new AMap.PlaceSearch({
      city: "全国",
      pageSize: 10,
      pageIndex: 1,
      extensions: "all",
    });

    console.log("高德地图初始化成功");
  } catch (error) {
    console.error("地图初始化失败:", error);
    ElMessage.error("地图初始化失败，请刷新页面重试");
  }
};

// 加载高德地图API
const loadAMapScript = () => {
  return new Promise((resolve, reject) => {
    // 检查是否已加载
    if (window.AMap) {
      resolve();
      return;
    }

    // 检查是否正在加载
    if (window.amapLoading) {
      const timer = setInterval(() => {
        if (window.AMap) {
          clearInterval(timer);
          resolve();
        }
      }, 100);
      return;
    }

    window.amapLoading = true;
    const script = document.createElement("script");
    script.src = `https://webapi.amap.com/maps?v=2.0&key=your_apikey&plugin=AMap.PlaceSearch,AMap.Geocoder`;
    script.onload = () => {
      window.amapLoading = false;
      resolve();
    };
    script.onerror = (error) => {
      window.amapLoading = false;
      reject(error);
      ElMessage.error("地图加载失败，请检查网络连接");
    };
    document.head.appendChild(script);
  });
};

// 逆地理编码
const reverseGeocode = (lng, lat) => {
  return new Promise((resolve, reject) => {
    if (!geocoderInstance) {
      reject(new Error("地理编码器未初始化"));
      return;
    }

    // 添加错误处理和超时处理
    const timeout = setTimeout(() => {
      reject(new Error("逆地理编码请求超时"));
    }, 10000);

    geocoderInstance.getAddress([lng, lat], (status, result) => {
      clearTimeout(timeout);

      if (status === "complete" && result.regeocode) {
        resolve(result.regeocode);
      } else {
        // 添加更详细的错误信息
        let errorMsg = "逆地理编码失败";
        if (status === "error") {
          errorMsg += ": 服务器错误";
        } else if (status === "no_data") {
          errorMsg += ": 无数据";
        }
        reject(new Error(errorMsg));
      }
    });
  });
};

// 地点搜索
const searchLocation = () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning("请输入搜索关键词");
    return;
  }

  if (!placeSearchInstance) {
    ElMessage.error("地点搜索功能未初始化");
    return;
  }

  const loading = ElLoading.service({
    target: ".search-results",
    text: "搜索中...",
  });

  placeSearchInstance.search(searchKeyword.value, (status, result) => {
    loading.close();

    if (status === "complete" && result.poiList) {
      searchResults.value = result.poiList.pois.map((poi) => ({
        id: poi.id,
        name: poi.name,
        address: poi.address,
        location: poi.location,
        distance: poi.distance,
      }));

      if (searchResults.value.length === 0) {
        ElMessage.info("未找到相关地点");
      }
    } else {
      ElMessage.error("搜索失败，请重试");
      searchResults.value = [];
    }
  });
};

// 选择搜索结果
const selectSearchResult = (result) => {
  form.location.lng = result.location.lng;
  form.location.lat = result.location.lat;

  if (marker) {
    mapInstance.remove(marker);
  }

  marker = new AMap.Marker({
    position: [result.location.lng, result.location.lat],
    offset: new AMap.Pixel(-15, -30),
    title: result.name,
  });

  mapInstance.add(marker);
  mapInstance.setCenter([result.location.lng, result.location.lat]);
  mapInstance.setZoom(15);

  form.location.address = result.name;
  if (result.address) {
    form.location.address += ` (${result.address})`;
  }

  searchResults.value = [];
  searchKeyword.value = "";
};

// 清除搜索
const clearSearch = () => {
  searchResults.value = [];
  searchKeyword.value = "";
};

// 使用当前位置
const useCurrentLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const lng = position.coords.longitude;
        const lat = position.coords.latitude;

        form.location.lng = lng;
        form.location.lat = lat;

        if (marker) {
          mapInstance.remove(marker);
        }

        marker = new AMap.Marker({
          position: [lng, lat],
          offset: new AMap.Pixel(-15, -30),
          title: "当前位置",
        });

        mapInstance.add(marker);
        mapInstance.setCenter([lng, lat]);
        mapInstance.setZoom(15);

        try {
          const result = await reverseGeocode(lng, lat);
          if (result) {
            form.location.address = result.formattedAddress;
          }
        } catch (error) {
          ElMessage.error("获取地址信息失败");
        }
      },
      (error) => {
        let errorMessage = "获取当前位置失败: ";
        switch (error.code) {
          case error.PERMISSION_DENIED:
            errorMessage += "用户拒绝了位置请求";
            break;
          case error.POSITION_UNAVAILABLE:
            errorMessage += "位置信息不可用";
            break;
          case error.TIMEOUT:
            errorMessage += "请求超时";
            break;
          default:
            errorMessage += "未知错误";
            break;
        }
        ElMessage.error(errorMessage);
      },
      {
        enableHighAccuracy: true,
        timeout: 5000,
        maximumAge: 0,
      }
    );
  } else {
    ElMessage.error("您的浏览器不支持地理位置功能");
  }
};

// 地图放大
const zoomIn = () => {
  if (mapInstance) mapInstance.zoomIn();
};

// 地图缩小
const zoomOut = () => {
  if (mapInstance) mapInstance.zoomOut();
};

// 重置地图视图
const resetMapView = () => {
  if (!mapInstance) return;

  mapInstance.setZoom(11);
  mapInstance.setCenter([116.397428, 39.90923]);

  if (marker) {
    mapInstance.remove(marker);
    marker = null;
  }

  form.location.lng = null;
  form.location.lat = null;
  form.location.address = "";
};

// 触发文件选择
const triggerFileInput = () => {
  fileInput.value.click();
};

// 处理文件选择
const handleFileChange = (event) => {
  const files = event.target.files;
  if (!files || files.length === 0) return;

  for (let i = 0; i < files.length; i++) {
    const file = files[i];

    // 检查文件类型
    if (!file.type.match("image.*")) {
      ElMessage.warning("请选择图片文件");
      continue;
    }

    // 检查文件大小（10MB）
    if (file.size > 10 * 1024 * 1024) {
      ElMessage.warning(`文件 ${file.name} 超过10MB限制`);
      continue;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
      const isFirst = form.files.length === 0;
      form.files.push({
        file: file,
        preview: e.target.result,
        isCover: isFirst,
      });
    };
    reader.readAsDataURL(file);
  }

  // 重置文件输入，允许再次选择相同文件
  event.target.value = "";
};

// 预览图片
const previewImage = (index) => {
  if (index >= 0 && index < form.files.length) {
    const imageUrl = form.files[index].preview;
    window.open(imageUrl, "_blank");
  }
};

// 删除图片
const removeFile = (index) => {
  if (index >= 0 && index < form.files.length) {
    const removed = form.files.splice(index, 1)[0];

    // 如果删除的是封面图，设置第一张为封面
    if (removed.isCover && form.files.length > 0) {
      form.files[0].isCover = true;
    }
  }
};

// 设置封面
const setCover = (index) => {
  if (index >= 0 && index < form.files.length) {
    form.files.forEach((file, i) => {
      file.isCover = i === index;
    });
  }
};

// 应用保存的位置
const applySavedLocation = (location) => {
  form.location = { ...location };

  if (marker) {
    mapInstance.remove(marker);
  }

  marker = new AMap.Marker({
    position: [location.lng, location.lat],
    offset: new AMap.Pixel(-15, -30),
    title: location.name || location.address,
  });

  mapInstance.add(marker);
  mapInstance.setCenter([location.lng, location.lat]);
  mapInstance.setZoom(15);
};

// 保存位置
const saveLocation = () => {
  if (!form.location.lng || !form.location.lat) {
    ElMessage.warning("请先选择位置");
    return;
  }

  if (!form.location.address) {
    ElMessage.warning("无法保存未命名的位置");
    return;
  }

  // 检查是否已存在相同位置
  const exists = savedLocations.value.some(
    (loc) => loc.lng === form.location.lng && loc.lat === form.location.lat
  );

  if (exists) {
    ElMessage.warning("该位置已收藏");
    return;
  }

  const name = prompt(
    "为这个位置命名:",
    form.location.address.split(",")[0] || "我的位置"
  );

  if (name) {
    savedLocations.value.push({
      name: name,
      address: form.location.address,
      lng: form.location.lng,
      lat: form.location.lat,
    });
    ElMessage.success("位置已保存");

    // 保存到localStorage
    localStorage.setItem(
      "savedLocations",
      JSON.stringify(savedLocations.value)
    );
  }
};

// 提交表单
const submitForm = async () => {
  if (form.files.length === 0) {
    ElMessage.warning("请上传至少一张图片");
    return;
  }

  if (!form.title) {
    ElMessage.warning("请输入作品标题");
    return;
  }

  if (!form.category) {
    ElMessage.warning("请选择作品分类");
    return;
  }

  if (!form.location.lng || !form.location.lat) {
    ElMessage.warning("请选择拍摄地点");
    return;
  }

  uploading.value = true;
  // 在提交前再次更新创建时间（确保是最新时间）
  form.ctime = format(new Date(), "yyyy-MM-dd HH:mm:ss");

  try {
    // 创建 FormData 对象
    const formData = new FormData();

    // 添加基本信息
    formData.append("title", form.title);
    formData.append("category", form.category);
    formData.append("description", form.description);
    formData.append("lng", form.location.lng);
    formData.append("lat", form.location.lat);
    formData.append("address", form.location.address);
    formData.append("ctime", form.ctime);
    formData.append("username", form.username);

    // 添加水印配置信息
    formData.append(
      "watermarkConfig",
      JSON.stringify({
        content: watermarkConfig.content,
        font: watermarkConfig.font,
        rotate: watermarkConfig.rotate,
        gap: watermarkConfig.gap,
        offset: watermarkConfig.offset,
      })
    );

    // 添加图片文件
    form.files.forEach((file, index) => {
      formData.append("files", file.file);
      // 标记封面图片
      if (file.isCover) {
        formData.append("coverIndex", index);
      }
      // 标记是否已添加水印
      formData.append(`watermarkAdded_${index}`, file.hasWatermark ? "1" : "0");
    });

    const res = await photoApi.uploadPhoto(formData);

    if (res.code === "200") {
      ElMessage.success("提交成功");
      // 重置表单
      resetForm();
      // 返回个人中心
      router.push("/person");
    } else {
      ElMessage.error(res.msg || "提交失败");
    }
  } catch (error) {
    console.error("提交错误:", error);
    const errorMsg =
      error.response?.data?.msg || error.message || "提交请求失败";
    ElMessage.error(errorMsg);
  } finally {
    uploading.value = false;
  }
};

// 重置表单
const resetForm = () => {
  form.title = "";
  form.category = "";
  form.description = "";
  form.files = [];
  form.location = {
    lng: null,
    lat: null,
    address: "",
  };
  resetMapView();
};

// 返回
const goBack = () => {
  router.push("/person");
};

// 页面加载时初始化地图
onMounted(async () => {
  // 初始化用户信息
  initUserInfo();
  // 从localStorage加载收藏位置
  const saved = localStorage.getItem("savedLocations");
  if (saved) {
    try {
      savedLocations.value = JSON.parse(saved);
    } catch (e) {
      console.error("加载收藏位置失败:", e);
    }
  } else {
    // 默认收藏位置
    savedLocations.value = [
      {
        name: "西湖",
        address: "浙江省杭州市西湖区西湖风景区",
        lng: 120.129295,
        lat: 30.252481,
      },
      {
        name: "外滩",
        address: "上海市黄浦区外滩",
        lng: 121.490158,
        lat: 31.237536,
      },
    ];
  }

  // 加载高德地图API
  try {
    await loadAMapScript();
    initMap();
  } catch (error) {
    console.error("地图加载失败:", error);
    ElMessage.error("地图加载失败，请刷新页面重试");
  }
});

// 页面卸载时清理资源
onUnmounted(() => {
  if (mapInstance) {
    mapInstance.destroy();
    mapInstance = null;
  }
});
</script>

<style scoped>
.watermark-dialog {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.watermark-preview {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  background-color: #f9f9f9;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image-container {
  max-width: 100%;
  max-height: 100%;
  text-align: center;
}

.preview-image {
  max-width: 100%;
  max-height: 280px;
  object-fit: contain;
}

.no-image-tip {
  color: #999;
  font-size: 16px;
  padding: 20px;
}

.watermark-form {
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #f9f9f9;
}

/* 其余样式保持不变 */
.nav {
  top: 0;
  left: 0;
  width: 100%;
  display: flex;

  padding: 15px 5%;
  z-index: 1000;
  transition: all 0.3s ease;
}

.a {
  position: relative;

  width: 100%;
  height: 120px;
  border: solid 10px #fff;
  box-shadow: 0 0 70px rgb(190, 40, 210);
  display: flex;
  justify-content: center;
  align-items: center;
  /* 设置鼠标移上去时变成小手形状 */
  cursor: pointer;
}
.a::after {
  content: "";
  position: absolute;
  width: 100%;
  height: 120px;
  box-shadow: 0 0 5px rgba(190, 40, 210);
  background-color: rgba(100, 30, 225, 0.4);
}
.a:hover {
  animation: a 1.5s;
}
@keyframes a {
  0%,
  34%,
  68%,
  100% {
    border: solid 10px #fff;
    box-shadow: 0 0 70px rgb(190, 40, 210);
  }
  17%,
  51%,
  85% {
    border: solid 10px rgba(255, 0, 0, 0.5);
    box-shadow: 0 0 90px rgba(255, 0, 0, 0.8);
  }
}
.b,
.b::before {
  z-index: 999;
  color: #fff;
  position: absolute;
  font-size: 16px;
  font-weight: 900;
  /* 设置字体间距 */
  letter-spacing: 12px;
}
.b::before {
  content: "摄影作品上传平台";
  text-shadow: -5px -5px 0px rgb(211, 250, 9), 5px 5px 0px rgb(25, 10, 240);
  /* 使用缩放的方式创建可见显示取余，括号里的四个值分别是top，right，bottom，left */
  clip-path: inset(100% 0px 0px 0px);
}
.a:hover .b::before {
  /* steps设置逐帧动画，值越小越卡顿 */
  animation: move 1.25s steps(2);
}
/* 这是制造混乱的位置和高宽，可以自行改变，随机的 */
@keyframes move {
  0% {
    clip-path: inset(80% 0px 0px 0px);
    transform: translate(-20px, -10px);
  }
  10% {
    clip-path: inset(10% 0px 85% 0px);
    transform: translate(10px, 10px);
  }
  20% {
    clip-path: inset(80% 0px 0px 0px);
    transform: translate(-10px, 10px);
  }
  30% {
    clip-path: inset(10% 0px 85% 0px);
    transform: translate(0px, 5px);
  }
  40% {
    clip-path: inset(50% 0px 30% 0px);
    transform: translate(-5px, 0px);
  }
  50% {
    clip-path: inset(10% 0px 30% 0px);
    transform: translate(5px, 0px);
  }
  60% {
    clip-path: inset(40% 0px 30% 0px);
    transform: translate(5px, 10px);
  }
  70% {
    clip-path: inset(50% 0px 30% 0px);
    transform: translate(-10px, 10px);
  }
  80% {
    clip-path: inset(80% 0px 5% 0px);
    transform: translate(20px, -10px);
  }
  90% {
    clip-path: inset(80% 0px 0px 0px);
    transform: translate(-10px, 0px);
  }
  100% {
    clip-path: inset(80% 0px 0px 0px);
    transform: translate(0px, 0px);
  }
}
/* 样式保持不变 */
:root {
  --primary-color: #3498db;
  --secondary-color: #2ecc71;
  --bg-color: #2172c3;
  --card-bg: #ffffff;
  --border-color: #e0e0e0;
  --text-primary: #333333;
  --text-secondary: #666666;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

body {
  line-height: 1.6;
}
/* 添加背景容器样式 */
.background-container {
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("/src/assets/imgs/sbpk.jpg"); /* 图片URL */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1; /* 确保背景在内容下方 */
}

.upload-container {
  max-width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

.upload-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 20px 0;
  border-bottom: 1px solid var(--border-color);
}

.upload-header h1 {
  font-size: 2.5rem;
  color: var(--primary-color);
  margin-bottom: 10px;
}

.upload-header p {
  font-size: 1.1rem;
  color: var(--text-secondary);
  max-width: 800px;
  margin: 0 auto;
}

.upload-card {
  background: var(--card-bg);
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  margin-bottom: 40px;
}

.card-header {
  padding: 25px 30px;
  border-bottom: 1px solid var(--border-color);
  background: linear-gradient(135deg, var(--primary-color), #1a5276);
  color: white;
}

.card-header h2 {
  font-size: 1.8rem;
  font-weight: 600;
}

.card-content {
  padding: 30px;
}

.upload-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

.form-section {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 25px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.form-section h3 {
  font-size: 1.3rem;
  margin-bottom: 20px;
  color: var(--primary-color);
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border-color);
}

.form-section h3 i {
  font-size: 1.4rem;
}

.upload-area {
  border: 2px dashed var(--border-color);
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  transition: all 0.3s;
  background: #fafcff;
  cursor: pointer;
  margin-bottom: 20px;
}

.upload-area:hover {
  border-color: var(--primary-color);
  background: #f0f8ff;
}

.upload-icon {
  font-size: 3.5rem;
  color: var(--primary-color);
  margin-bottom: 15px;
}

.upload-text {
  font-size: 1.1rem;
  margin-bottom: 10px;
  color: var(--text-primary);
}

.upload-hint {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.preview-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 15px;
  margin-top: 20px;
}

.preview-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
  height: 120px;
  transition: all 0.3s;
}

.preview-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  padding: 5px;
  transform: translateY(100%);
  transition: transform 0.3s;
}

.preview-item:hover .preview-actions {
  transform: translateY(0);
}

.preview-actions button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 1rem;
  padding: 5px;
  margin: 0 5px;
}

.preview-actions button:hover {
  color: var(--secondary-color);
}

.cover-badge {
  position: absolute;
  top: 5px;
  right: 5px;
  background: var(--secondary-color);
  color: white;
  font-size: 0.7rem;
  padding: 3px 8px;
  border-radius: 10px;
}

.map-container {
  border-radius: 8px;
  overflow: hidden;
  position: relative;
  height: 350px;
  border: 1px solid var(--border-color);
  margin-bottom: 15px;
}

.map-box {
  width: 100%;
  height: 100%;
  min-height: 350px;
}

.map-controls {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.location-info {
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 6px;
  margin-top: 15px;
  font-size: 0.95rem;
}

.coordinates {
  display: flex;
  gap: 15px;
  margin-bottom: 8px;
}

.address {
  font-weight: 500;
}

.map-actions {
  position: absolute;
  top: 15px;
  right: 15px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.map-actions button {
  background: white;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.map-actions button:hover {
  background: #f0f0f0;
}

.search-results {
  max-height: 200px;
  overflow-y: auto;
  margin-top: 15px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
}

.search-result-item {
  padding: 10px 15px;
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
  transition: background 0.2s;
}

.search-result-item:hover {
  background: #f0f8ff;
}

.search-result-item:last-child {
  border-bottom: none;
}

.result-name {
  font-weight: 600;
  margin-bottom: 3px;
}

.result-address {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.result-distance {
  font-size: 0.85rem;
  color: #27ae60;
  margin-top: 3px;
}

.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 30px;
  justify-content: center;
}

.submit-btn {
  padding: 12px 35px;
  font-size: 1.1rem;
  font-weight: 600;
}

.db-design {
  background: var(--card-bg);
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  padding: 30px;
}

.db-design h3 {
  text-align: center;
  margin-bottom: 25px;
  color: var(--primary-color);
  font-size: 1.8rem;
}

.tables-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
}

.table-card {
  background: #f8fafc;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.table-card h4 {
  text-align: center;
  margin-bottom: 20px;
  color: var(--primary-color);
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border-color);
}

.table-field {
  display: flex;
  justify-content: space-between;
  padding: 8px 5px;
  border-bottom: 1px dashed var(--border-color);
  font-size: 0.95rem;
}

.table-field:last-child {
  border-bottom: none;
}

.field-name {
  font-weight: 600;
}

.field-type {
  color: #e74c3c;
  font-family: monospace;
}

.field-desc {
  color: var(--text-secondary);
  font-size: 0.85rem;
  margin-top: 3px;
}

.relationships {
  text-align: center;
  margin: 25px 0;
  font-weight: 600;
  color: var(--text-secondary);
}

@media (max-width: 900px) {
  .upload-form {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 600px) {
  .upload-container {
    padding: 0 15px;
  }

  .card-content {
    padding: 20px;
  }

  .coordinates {
    flex-direction: column;
    gap: 5px;
  }
}

/* 添加作品标题输入框样式 */
.title-input {
  margin-bottom: 20px;
}

.title-input :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--border-color) inset;
  transition: all 0.3s ease;
}

.title-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-color) inset;
}

.title-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset;
}

.title-input :deep(.el-input__inner) {
  font-size: 1.1rem;
  padding: 12px 15px;
}

.title-input :deep(.el-input__count) {
  background: transparent;
}

/* 优化输入框样式 */
:deep(.el-input__wrapper),
:deep(.el-textarea__inner) {
  border-radius: 6px;
}

:deep(.el-input__wrapper:hover),
:deep(.el-textarea__inner:hover) {
  border-color: var(--primary-color);
}

:deep(.el-input__wrapper.is-focus),
:deep(.el-textarea__inner:focus) {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 1px var(--primary-color) inset;
}

/* 优化选择器样式 */
.full-width {
  width: 100%;
}

:deep(.el-select .el-input__wrapper) {
  box-shadow: 0 0 0 1px var(--border-color) inset;
}

:deep(.el-select .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-color) inset;
}

:deep(.el-select .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color) inset;
}
</style>
