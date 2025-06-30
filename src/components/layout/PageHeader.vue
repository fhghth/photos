<template>
  <el-page-header @back="onBack">
    <template #breadcrumb>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item
          v-for="(item, idx) in items"
          :key="idx"
          :to="item.to"
          @click="handleClick(item)"
        >
          <span v-if="item.to" style="cursor: pointer; color: #6c63ff">{{
            item.label
          }}</span>
          <span v-else>{{ item.label }}</span>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </template>
    <template #title>
      <span>{{ title }}</span>
    </template>
  </el-page-header>
</template>

<script setup>
import { useRouter } from "vue-router";
const props = defineProps({
  items: { type: Array, default: () => [] }, // [{label, to}]
  title: { type: String, default: "" },
});
const router = useRouter();
const onBack = () => {
  router.back();
};
const handleClick = (item) => {
  if (item.to) router.push(item.to);
};
</script>
