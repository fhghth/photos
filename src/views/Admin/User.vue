<!-- 1 -->
<script setup>
import { reactive, ref } from "vue";
import { adminApi } from "../../utils/request";
import { ElMessage, ElNotification as notify } from "element-plus";

const onBack = () => {
  notify("Back");
};
const input = ref("");
const data = reactive({
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  role: "1",
});

const load = (page) => {
  if (page) {
    data.pageNum = page;
  }
  adminApi.user
    .getUsers({
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      role: data.role,
    })
    .then((res) => {
      if (res.code === "200") {
        data.tableData = res.data.list;
        data.total = res.data.total;
      } else {
        ElMessage.error(res.msg);
      }
    });
};
load();
//axios
// request.get("/admin/select").then((res) => {
//   if (res.code === "200") {
//     console.log(res);
//   } else {
//     ElMessage.error(res.msg);
//   }
// });
</script>
<!-- 2 -->
<template>
  <div aria-label="A complete example of page header">
    <el-page-header @back="onBack">
      <template #breadcrumb>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: './page-header.html' }">
            homepage
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <a href="./page-header.html">route 1</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item>route 2</el-breadcrumb-item>
        </el-breadcrumb>
      </template>
    </el-page-header>
  </div>
  <div>
    <div class="card">
      <div>
        <el-input
          v-model="input"
          style="width: 240px"
          placeholder="Please input"
        />
        <button>
          <el-icon><Search /></el-icon>
        </button>
      </div>
      <div style="padding: 10px">
        <el-button type="primary">Primary</el-button>
        <el-button type="success">Success</el-button>
        <el-button type="warning">Warning</el-button>
        <el-button type="danger">Danger</el-button>
      </div>
      <div>
        <!-- <el-table :data="tableData" border style="width: 100%">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="date" label="Date" width="180" />
          <el-table-column prop="name" label="Name" width="180" />
          <el-table-column prop="address" label="Address" />
        </el-table> -->

        <div style="border: 2px; border-radius: 8px; padding: 10px">
          <el-table :data="data.tableData" style="width: 100%">
            <el-table-column type="selection" width="55" />
            <el-table-column property="username" label="账号" width="120" />
            <el-table-column property="name" label="名称" width="120" />
            <el-table-column
              property="phone"
              label="电话"
              width="240"
              show-overflow-tooltip
            />
            <el-table-column property="email" label="邮箱" />
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
