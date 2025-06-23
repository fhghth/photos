<script setup>
import { ref } from "vue";
import Navbar from "../components/layout/Navbar.vue";
</script>

<template>
  <div class="manager">
    <Navbar />
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

:root {
  --primary: #1a1a2e;
  --secondary: #16213e;
  --accent: #0f3460;
  --highlight: #e94560;
  --text-light: #f1f1f1;
  --text-dark: #333;
  --transition: all 0.3s ease;
}

.manager {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  padding: 2rem 5%;
}

.navbar {
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  color: var(--text-light);
  padding: 1rem 5%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo i {
  color: var(--highlight);
  font-size: 2rem;
}

.logo h1 {
  font-size: 1.8rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.logo span {
  color: var(--highlight);
}

.nav-links {
  display: flex;
  gap: 2rem;
  list-style: none;
}

.nav-links a {
  color: var(--text-light);
  text-decoration: none;
  font-weight: 500;
  font-size: 1.1rem;
  padding: 8px 12px;
  border-radius: 4px;
  transition: var(--transition);
  position: relative;
}

.nav-links a:hover {
  color: var(--highlight);
}

.nav-links a::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: var(--highlight);
  transition: var(--transition);
}

.nav-links a:hover::after {
  width: 100%;
}

.nav-actions {
  display: flex;
  gap: 1.5rem;
  align-items: center;
}

.search-box {
  display: flex;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 30px;
  padding: 8px 15px;
  backdrop-filter: blur(10px);
}

.search-box input {
  background: transparent;
  border: none;
  color: var(--text-light);
  outline: none;
  width: 180px;
  padding: 4px 10px;
}

.search-box input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.search-box button {
  background: none;
  border: none;
  color: var(--text-light);
  cursor: pointer;
  transition: var(--transition);
}

.search-box button:hover {
  color: var(--highlight);
}

.btn {
  background: var(--highlight);
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 30px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn:hover {
  background: #ff2e4d;
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(233, 69, 96, 0.4);
}

/* 添加router-link的样式 */
.router-link-active {
  color: var(--highlight) !important;
}

.router-link-active::after {
  width: 100% !important;
}
</style>
