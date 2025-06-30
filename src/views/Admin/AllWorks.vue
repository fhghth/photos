<!-- 1 -->
<script setup>
import { reactive, ref } from "vue";
import { photoApi, adminApi } from "../../utils/request";
import { ElMessage, ElNotification as notify } from "element-plus";
import PageHeader from "../../components/layout/PageHeader.vue";
import { Search } from "@element-plus/icons-vue";

const input = ref("");
const data = reactive({
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
});

const load = (page) => {
  if (page) {
    data.pageNum = page;
  }
  adminApi.content
    .getAllPhotos({
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      role: "false",
    })
    .then((res) => {
      if (res.code === "200") {
        data.tableData = res.data.list;
        data.total = res.data.total;
        console.log("作品数据：", data.tableData);
      } else {
        ElMessage.error(res.msg);
      }
    });
};
//获取封面信息
const getImageUrl = (cover) => {
  return `http://localhost:7080/uploads/${cover}`;
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

load();
</script>
<!-- 2 -->
<template>
  <PageHeader
    :items="[
      { label: '首页', to: '/manager/welcome' },
      { label: '内容管理', to: '/manager/dashboard/allworks' },
      { label: '所有作品' },
    ]"
    title="所有作品"
  />
  <div>
    <div class="card">
      <div>
        <el-input
          v-model="input"
          style="width: 240px"
          placeholder="Please input"
        />
        <el-button :icon="Search" circle />
      </div>
      <div style="padding: 10px">
        <el-button type="primary">刷新</el-button>
        <el-button type="danger">删除</el-button>
      </div>
      <div>
        <div style="border: 2px; border-radius: 8px; padding: 10px">
          <el-table :data="data.tableData" style="width: 100%">
            <el-table-column type="selection" width="55" />
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
              width="240  "
            />
            <el-table-column
              property="locationAddress"
              label="发布地点"
              width="240  "
            />
            <el-table-column property="role" label="审核状态" width="120">
              <template #default="scope">
                <el-tag
                  :type="scope.row.role === 'true' ? 'success' : 'danger'"
                >
                  {{ scope.row.role === "true" ? "已审核" : "待审核" }}
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
                    type="danger"
                    size="small"
                    @click="deleteWork(scope.row.id)"
                    :disabled="scope.row.role === 'back'"
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
            :currentpage="data.pageNum"
            :pageSize="data.pageSize"
            size="small"
            layout="total,prev, pager, next"
            :total="data.total"
            class="mt-4"
            @current-change="load"
          />
          <!-- @接的是事件，是v-on的缩写 -->
        </div>
      </div>
    </div>
  </div>
</template>
<!-- 3 -->
<style scoped></style>
