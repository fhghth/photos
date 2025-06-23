<!-- 1 -->
<script setup>
import {
  Document,
  Menu,
  Location,
  Setting,
  House,
  ArrowDown,
} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

const router = useRouter();

const handleSelect = (key, keyPath) => {
  if (key === "/manager/home") {
    router.push("/manager/home");
  } else if (key === "1-1") {
    router.push("/manager/dashboard/user");
  } else if (key === "1-2") {
    router.push("/manager/dashboard/admin");
  } else if (key === "2-1") {
    router.push("/manager/dashboard/works");
  } else if (key === "2-2") {
    router.push("/manager/dashboard/works");
  }
};

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath);
};

const handleClose = (key, keyPath) => {
  console.log(key, keyPath);
};
</script>
<!-- 2 -->
<template>
  <div class="dashboard-wrapper">
    <!-- 身体区域开始 -->
    <div style="display: flex">
      <!-- 菜单区域开始 -->
      <div>
        <el-col>
          <el-menu
            default-active="/manager/home"
            class="el-menu-vertical-demo"
            @select="handleSelect"
            @open="handleOpen"
            @close="handleClose"
            style="min-height: calc(100vh - 60px)"
          >
            <el-menu-item index="/manager/home">
              <el-icon><House /></el-icon>
              <span>首页</span>
            </el-menu-item>
            <el-sub-menu index="1">
              <template #title>
                <el-icon><location /></el-icon>
                <span>用户管理</span>
              </template>
              <el-menu-item-group title="用户管理">
                <el-menu-item index="1-1">用户信息</el-menu-item>
                <el-menu-item index="1-2">管理员信息</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="Group Two">
                <el-menu-item index="1-3">item three</el-menu-item>
              </el-menu-item-group>
              <el-sub-menu index="1-4">
                <template #title>item four</template>
                <el-menu-item index="1-4-1">item one</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><Menu /></el-icon>
                <span>内容管理</span>
              </template>
              <el-menu-item-group title="内容管理">
                <el-menu-item index="2-1">作品管理</el-menu-item>
                <el-menu-item index="2-2">评论管理</el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-menu-item index="3" disabled>
              <el-icon><document /></el-icon>
              <span>Navigator Three</span>
            </el-menu-item>
            <el-menu-item index="4">
              <el-icon><setting /></el-icon>
              <span>Navigator Four</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </div>
      <!-- 菜单区域结束 -->

      <!-- 内容区域开始 -->
      <div style="flex: 1; width: 0; padding: 10px; background-color: #cadce1">
        <RouterView></RouterView>
      </div>
      <!-- 内容区域结束 -->
    </div>
    <!-- 身体区域结束 -->

    <!-- !!!!!非常重要！！！！！！ -->
  </div>
</template>
<!-- 3 -->
<style scoped>
.dashboard-wrapper {
  margin: -2rem -5%;
}
.el-menu {
  background-color: rgb(243, 228, 246);
  border-color: rgb(243, 228, 246);
}
.el-dropdown {
  cursor: pointer;
}
</style>
