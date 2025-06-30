<!-- 1 -->
<script setup>
import { useRoute, useRouter } from "vue-router"; // 添加 useRoute

const router = useRouter();
const route = useRoute(); // 添加 route 对象

const toDashboard = () => {
  router.push("/manager/dashboard/user");
};
</script>
<!-- 2 -->
<template>
  <div class="content">
    <img src="../../assets/imgs/bac 3.png" alt="background" class="back-3" />

    <div class="title">
      <h3>The Land of Serene Beauty</h3>
      <h1>welcome</h1>
    </div>

    <img src="../../assets/imgs/bac 2.2.png" alt="background" class="back-2" />
    <img src="../../assets/imgs/bac 1.png" alt="background" class="back-1" />

    <div class="info-wrap">
      <p>
        Kerala is a tropical paradise in southern India, known for its
        breathtaking landscapes, rich culture, and serene backwaters. Whether
        you're seeking adventure, relaxation, or a taste of India's unique
        traditions, Kerala has something for everyone. From the lush hill
        stations to pristine beaches, and Ayurvedic retreats to vibrant
        festivals, this enchanting state offers a perfect blend of natural
        beauty and cultural experiences.
      </p>
    </div>

    <div class="cta">
      <button @click="toDashboard">
        ENTER
        <i class="fa-solid fa-arrow-right"></i>
      </button>
    </div>

    <div class="slider">
      <i class="fa-solid fa-chevron-left"></i>
      <i class="fa-solid fa-chevron-right"></i>
    </div>
  </div>
</template>
<!-- 3 -->
<style scoped>
* {
  padding: 0;
  margin: 0;
  border: none;
  outline: none;
  font-family: "Inter", sans-serif;
}

body {
  background-image: url(img/bac\ 4.png);
  background-size: cover;
  background-position: top;
  background-repeat: no-repeat;
  overflow: hidden;
}

body::after {
  content: "";
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  background: linear-gradient(transparent 50%, rgb(0, 0, 0));
}

/* Background-images */

.content img {
  position: absolute;
  bottom: -12%;
  left: -12%;
}

header {
  position: absolute;
  top: 0;
  z-index: 1;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.active {
  background-color: #16423c;
  color: white;
}

.title {
  position: absolute;
  top: 40%;
  right: 50%;
  transform: translate(50%, -50%);
}

.title h3 {
  font-size: 1.5rem;
  font-weight: 400;
  letter-spacing: 15px;
  color: white;
  text-align: center;
}

.title h1 {
  font-size: 15rem;
  font-weight: 800;
  letter-spacing: 50px;
  text-transform: uppercase;
  color: white;
  text-align: center;
  margin: -20px 0;
}

p {
  font-size: 14px;
  font-weight: 300;
  letter-spacing: 1px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.7);
  width: 70%;
  text-align: center;
  text-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
}

.info-wrap {
  width: 100%;
  display: flex;
  justify-content: center;
  position: absolute;
  bottom: 18%;
  z-index: 1;
  left: 0;
}

.cta {
  position: absolute;
  bottom: 8%;
  display: flex;
  justify-content: center;
  width: 100%;
  z-index: 1;
  left: 0%;
}

.cta button {
  font-size: 18px;
  font-weight: 400;
  letter-spacing: 3px;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.8);
  background-color: transparent;
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 50px;
  height: 50px;
  width: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.2s;
  cursor: pointer;
}

.cta button:hover {
  background-color: rgba(255, 255, 255, 0.8);
  color: rgb(53, 53, 53);
}

.slider {
  position: absolute;
  top: 50%;
  width: 100%;
  display: flex;
  justify-content: space-between;
}
.slider i {
  padding: 0 5%;
  font-size: 36px;
  color: rgba(255, 255, 255, 0.4);
}

/* Animations */
@keyframes bottomIn {
  from {
    transform: translateY(200px);
  }
  to {
    transform: translateY(0);
  }
}

.back-1 {
  animation: bottomIn 1s ease-out forwards;
}

.back-2 {
  animation: bottomIn 1.3s ease-out forwards;
}

.back-3 {
  animation: bottomIn 1.5s ease-out forwards;
}

@keyframes bottomInText {
  from {
    transform: translateY(500px);
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

h3 {
  animation: bottomInText 1s ease-out forwards;
}

h1 {
  animation: bottomInText 1.2s ease-out forwards;
}

p {
  animation: bottomInText 1.2s ease-out forwards;
  animation-delay: 0.2s;
  opacity: 0;
}

button {
  animation: bottomInText 1.2s ease-out forwards;
  animation-delay: 0.3s;
  opacity: 0;
}

@keyframes backgroundImage {
  from {
    background-position: top;
  }
  to {
    background-position: 50% 14%;
  }
}

body {
  animation: backgroundImage 1.6s ease-out forwards;
}

@keyframes topIn {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@keyframes zoomOut {
  from {
    transform: scale(1.5);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.slider {
  animation: zoomOut 1.2s ease-out forwards;
}
</style>
