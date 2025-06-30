<!-- 1 -->
<script setup>
import { reactive, ref } from "vue";
import { adminApi } from "../../utils/request";
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
  adminApi
    .getCommentsList({
      pageNum: data.pageNum,
      pageSize: data.pageSize,
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

//删除评论方法
const deleteComment = (id, workId) => {
  adminApi.deleteComment(id, workId).then((res) => {
    if (res.code === "200") {
      ElMessage.success("已删除评论");
      load(data.pageNum);
    } else {
      ElMessage.error("删除失败：" + res.msg);
    }
  });
};

load();
</script>
<!-- 2 -->
<template>
  <PageHeader
    :items="[
      { label: '首页', to: '/manager/welcome' },
      { label: '内容管理', to: '/manager/dashboard/comments' },
      { label: '评论管理' },
    ]"
    title="评论管理"
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
            <el-table-column property="workTitle" label="出处" width="180" />

            <el-table-column
              property="comment"
              label="评论内容"
              width="240  "
            />
            <el-table-column
              property="commentTime"
              label="发布时间"
              width="240  "
            />
            <el-table-column label="操作" width="160">
              <template #default="scope">
                <div class="action-buttons">
                  <el-button
                    type="danger"
                    size="small"
                    @click="deleteComment(scope.row.id, scope.row.workId)"
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
