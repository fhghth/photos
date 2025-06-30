<!-- 1 -->
<script setup>
import { reactive, ref } from "vue";
import { adminApi } from "../../utils/request";
import { ElMessage, ElNotification as notify } from "element-plus";
import PageHeader from "../../components/layout/PageHeader.vue";
import { Search } from "@element-plus/icons-vue";

const input = ref("");
const data = reactive({
  name: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  role: "0",
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
  <PageHeader
    :items="[
      { label: '首页', to: '/manager/welcome' },
      { label: '用户管理', to: '/manager/dashboard/user' },
      { label: '管理员信息' },
    ]"
    title="管理员信息"
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
        <el-button type="success">新增</el-button>
        <el-button type="danger">删除</el-button>
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
