<!-- 1 -->
<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const goHome = () => {
  router.push("/home");
};
</script>
<!-- 2 -->
<template>
  <div class="not-found">
    <div class="content">
      <img src="../assets/imgs/404.png" alt="404" />
      <h1>页面未找到</h1>
      <p>抱歉，您访问的页面不存在</p>
      <button class="btn" @click="goHome">
        <i class="fas fa-home"></i>
        返回首页
      </button>
    </div>
  </div>
</template>
<!-- 3 -->
<style scoped>
.not-found {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  color: var(--text-light);
}

.content {
  text-align: center;
  padding: 2rem;
}

.content img {
  max-width: 400px;
  margin-bottom: 2rem;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: var(--highlight);
}

p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.8;
}

.btn {
  background: var(--highlight);
  color: white;
  border: none;
  padding: 12px 28px;
  border-radius: 30px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn:hover {
  background: #ff2e4d;
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(233, 69, 96, 0.4);
}

@media (max-width: 768px) {
  .content img {
    max-width: 300px;
  }

  h1 {
    font-size: 2rem;
  }

  p {
    font-size: 1.1rem;
  }
}
</style>
