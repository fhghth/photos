<script setup>
import { ref } from "vue";
import Navbar from "../components/layout/Navbar.vue";
import Carousel from "../components/home/Carousel.vue";
import PhotoGrid from "../components/home/PhotoGrid.vue";
import FeaturedSeries from "../components/home/FeaturedSeries.vue";
import Footer from "../components/layout/Footer.vue";

const showUploadModal = ref(false);

const handleUpload = (data) => {
  console.log("上传数据:", data);
  // TODO: 处理上传逻辑
  showUploadModal.value = false;
};
</script>

<template>
  <div class="home">
    <Navbar @upload="showUploadModal = true" />
    <main>
      <Carousel />
      <PhotoGrid />
      <FeaturedSeries />
    </main>
    <Footer />
  </div>
</template>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
}
</style>
