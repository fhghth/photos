<script setup>
import { ref } from "vue";
import Navbar from "../components/layout/Navbar.vue";
import Carousel from "../components/home/Carousel.vue";
import PhotoGrid from "../components/home/PhotoGrid.vue";
import FeaturedSeries from "../components/home/FeaturedSeries.vue";
import Footer from "../components/layout/Footer.vue";
</script>

<template>
  <el-backtop :right="100" :bottom="100" />
  <div class="home">
    <Navbar />
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
