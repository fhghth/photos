<script setup>
import { reactive, ref, onMounted } from "vue";
import { photoApi } from "../../utils/request";
import { ElMessage, ElMessageBox } from "element-plus";
import { Sunny, Moon } from "@element-plus/icons-vue";
import { useRouter, useRoute } from "vue-router";

//路由
const router = useRouter();
const route = useRoute();
// 获取用户状态
const currentUser = ref(
  JSON.parse(localStorage.getItem("code_user")) || {
    username: "游客",
    avatar: "",
    id: null,
  }
);

// 主题状态
const theme = ref("dark"); // 默认主题为light

// 切换主题
function changeTheme(newTheme) {
  theme.value = newTheme;
}
// 返回功能
const goBack = () => {
  router.go(-1);
};

const data = reactive({
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
});

// 加载作品信息
const load = (page) => {
  if (page) {
    data.pageNum = page;
  }

  // 获取当前用户的所有作品
  photoApi
    .getAllPhotos({
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: currentUser.value.username,
    })
    .then((res) => {
      if (res.code === "200") {
        data.tableData = res.data.list;
        data.total = res.data.total;
      } else {
        ElMessage.error(res.msg || "获取作品失败");
      }
    })
    .catch((error) => {
      console.error("获取作品失败:", error);
      ElMessage.error("获取作品失败");
    });
};

// 重新提交作品
const reUploadWork = (workId) => {
  ElMessageBox.confirm("确定要重新提交此作品吗？", "重新提交确认", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      photoApi
        .resubmitPhoto(workId)
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success("作品已重新提交审核");
            load(); // 刷新列表
          } else {
            ElMessage.error(res.msg || "重新提交失败");
          }
        })
        .catch((error) => {
          console.error("重新提交失败:", error);
          ElMessage.error("重新提交失败");
        });
    })
    .catch(() => {
      // 用户取消操作
    });
};

// 删除作品
const deleteWork = (workId) => {
  ElMessageBox.confirm("确定要删除此作品吗？此操作不可恢复！", "删除确认", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "error",
  })
    .then(() => {
      photoApi
        .deletePhoto(workId)
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success("作品已删除");
            load(); // 刷新列表
          } else {
            ElMessage.error(res.msg || "删除失败");
          }
        })
        .catch((error) => {
          console.error("删除失败:", error);
          ElMessage.error("删除失败");
        });
    })
    .catch(() => {
      // 用户取消操作
    });
};

// 获取审核状态标签
const getStatusLabel = (role) => {
  switch (role) {
    case "true":
      return "已通过";
    case "false":
      return "待审核";
    case "back":
      return "已退回";
    default:
      return role;
  }
};

// 获取审核状态标签类型
const getStatusType = (role) => {
  switch (role) {
    case "true":
      return "success";
    case "false":
      return "warning";
    case "back":
      return "danger";
    default:
      return "info";
  }
};

// 获取封面信息
const getImageUrl = (cover) => {
  return `http://localhost:7080/uploads/${cover}`;
};

// 初始化加载
onMounted(() => {
  load();
});
</script>

<template>
  <div class="page-container">
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
    <div class="btn-box">
      <el-button
        :icon="Sunny"
        circle
        @click="changeTheme('light')"
        aria-hidden="true"
      />
      <el-button
        :icon="Moon"
        circle
        @click="changeTheme('dark')"
        aria-hidden="true"
      />
    </div>

    <!-- 天空背景 -->
    <div :class="theme" id="sky-container">
      <div class="bg"></div>
      <div class="moon-box">
        <div class="moon"></div>
      </div>
      <div class="sun-box">
        <div class="sun"></div>
      </div>
      <div class="sea"></div>
    </div>

    <div class="card">
      <div style="border: 2px; border-radius: 8px; padding: 10px">
        <el-table :data="data.tableData" style="width: 100%" stripe>
          <el-table-column property="username" label="账号" width="120" />
          <el-table-column property="title" label="作品名称" width="120" />
          <el-table-column
            property="category"
            label="分类"
            width="120"
            show-overflow-tooltip
          />
          <el-table-column
            property="description"
            label="作品描述"
            width="240"
          />
          <el-table-column
            property="locationAddress"
            label="拍摄地点"
            width="240"
          />
          <el-table-column property="role" label="审核状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.role)">
                {{ getStatusLabel(scope.row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="封面" width="120">
            <template #default="scope">
              <el-image
                v-if="scope.row.cover"
                style="width: 60px; height: 60px"
                :src="getImageUrl(scope.row.cover)"
                fit="cover"
              />
              <span v-else>无封面</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160">
            <template #default="scope">
              <div class="action-buttons">
                <el-button
                  type="success"
                  size="small"
                  @click="reUploadWork(scope.row.id)"
                  :disabled="scope.row.role !== 'back'"
                >
                  重新提交
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="deleteWork(scope.row.id)"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页符 -->
      <div style="border: 2px; border-radius: 8px; padding: 10px">
        <el-pagination
          :current-page="data.pageNum"
          :page-size="data.pageSize"
          size="small"
          layout="total, prev, pager, next"
          :total="data.total"
          class="mt-4"
          @current-change="load"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  position: relative;
  min-height: 100vh;
  padding-bottom: 20px;
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

#sky-container {
  height: 300px; /* 缩到300px */
  width: 100%;
  position: relative;
  overflow: hidden;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.sun {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px; /* 缩小尺寸 */
  height: 200px; /* 缩小尺寸 */
  background-color: orange;
  border-radius: 50%;
  box-shadow: 0 0 60px yellow;
}

.moon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(calc(-50% + -60px), calc(-50% + -70px));
  width: 200px;
  height: 200px;
  box-shadow: 60px 70px 0 cyan;
  border-radius: 50%;
  filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.7));
}

.sea {
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 25%;
  backdrop-filter: blur(40px);
  -webkit-backdrop-filter: blur(40px);
  z-index: 10;
}

.sun,
.moon,
.sun-box,
.moon-box,
.bg {
  transition: all 1s ease-in-out;
}

.sun-box,
.moon-box {
  position: relative;
  overflow: hidden;
  height: 100%;
}

/* 白天 */
.light .sun-box {
  height: 100%;
}
.light .moon-box {
  height: 0;
}
.light .bg {
  background: linear-gradient(to bottom, #fffb88, #71c8ff);
}

/* 夜晚 */
.dark .sun-box {
  height: 0;
}
.dark .moon-box {
  height: 100%;
}
.dark .bg {
  background: linear-gradient(to bottom, #1a1a60, #000);
}

/* 切换按钮样式 */
.btn-box {
  position: absolute;
  top: 15px;
  right: 15px;
  z-index: 20;
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.btn-box div {
  background: rgba(255, 255, 255, 0.85);
  color: #000;
  width: 100px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 14px;
  border-radius: 20px;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.btn-box div:hover {
  background: #fff;
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.25);
}

.btn-box div:active {
  transform: translateY(0);
}

.card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-top: 20px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.el-table {
  margin-bottom: 20px;
}

.el-pagination {
  justify-content: center;
}

@media (max-width: 768px) {
  #sky-container {
    height: 200px;
  }

  .sun,
  .moon {
    width: 120px;
    height: 120px;
  }

  .moon {
    transform: translate(calc(-50% + -40px), calc(-50% + -45px));
    box-shadow: 40px 45px 0 cyan;
  }

  .btn-box div {
    width: 80px;
    font-size: 12px;
  }

  .card {
    padding: 10px;
  }
}
</style>
