<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router"; // 添加 useRoute
import { Camera } from "@element-plus/icons-vue";
import { photoApi } from "../../utils/request";

const router = useRouter();
const route = useRoute(); // 添加 route 对象
const searchQuery = ref(""); //搜索框

// 监视路由变化，当路由改变时重置搜索框
watch(
  () => route.name,
  () => {
    searchQuery.value = ""; // 每次路由变化时重置搜索框
  }
);

const userName = ref("");
//作品数据
const works = ref("");
// 用户状态
const isLoggedIn = ref(false);
const userAvatar = ref("");

// 默认头像
const defaultAvatar = ref(
  "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
);

// 处理头像URL
const getAvatarUrl = (avatarPath) => {
  if (!avatarPath) return defaultAvatar.value;

  // 如果是base64数据，直接返回
  if (avatarPath.startsWith("data:image")) {
    return avatarPath;
  }

  // 如果是相对路径，添加baseURL
  if (avatarPath.startsWith("/")) {
    return `http://localhost:7080${avatarPath}`;
  }

  // 如果是完整URL，直接返回
  return avatarPath;
};

// 判断是否为管理员
const isAdmin = computed(() => {
  const userData = JSON.parse(localStorage.getItem("code_user") || "{}");
  return userData.role === "0"; // 管理员角色是 0
});

// 从 localStorage 加载用户信息
const loadUserInfo = () => {
  try {
    const userData = JSON.parse(localStorage.getItem("code_user") || "{}");
    if (userData && userData.id) {
      isLoggedIn.value = true;
      userAvatar.value = getAvatarUrl(userData.avatar);
      userName.value = userData.username || userData.name || "用户";
    } else {
      isLoggedIn.value = false;
      // 如果当前在管理页面，重定向到登录页
      if (route.path.startsWith("/manager")) {
        router.replace("/login");
      }
    }
  } catch (error) {
    console.error("加载用户信息失败:", error);
    isLoggedIn.value = false;
    // 如果当前在管理页面，重定向到登录页
    if (route.path.startsWith("/manager")) {
      router.replace("/login");
    }
  }
};

// 初始化时加载用户信息
onMounted(() => {
  loadUserInfo();

  // 添加路由守卫
  router.beforeEach((to, from, next) => {
    if (to.path.startsWith("/manager")) {
      const userStr = localStorage.getItem("code_user");
      if (!userStr) {
        next("/login");
        return;
      }
      try {
        const user = JSON.parse(userStr);
        if (!user.id) {
          next("/login");
          return;
        }
      } catch (error) {
        console.error("解析用户信息失败:", error);
        next("/login");
        return;
      }
    }
    next();
  });

  window.addEventListener("storage", (event) => {
    if (event.key === "code_user") {
      loadUserInfo();
    }
  });
});

// 检查是否需要重定向
const checkForRedirect = () => {
  if (route.path.startsWith("/manager")) {
    const userStr = localStorage.getItem("code_user");
    if (!userStr) {
      router.replace("/login");
      return;
    }
    try {
      const user = JSON.parse(userStr);
      if (!user.id) {
        router.replace("/login");
      }
    } catch (error) {
      console.error("解析用户信息失败:", error);
      router.replace("/login");
    }
  }
};

//获取作品信息
const searchByChs = () => {
  if (searchQuery.value) {
    router.push({
      name: "images",
      query: { search: searchQuery.value },
    });
    searchQuery.value = "";
  }
};

// 点击头像跳转到用户个人中心
const goToPerson = () => {
  if (isLoggedIn.value) {
    router.push("/person");
  } else {
    router.push("/login");
  }
};

// 点击头像跳转到用户个人中心或控制台
const handleAvatarClick = () => {
  if (isAdmin.value) {
    // 管理员跳转到控制台
    router.push("/manager/welcome");
  } else {
    // 普通用户跳转到个人中心
    router.push("/person");
  }
};
// 管理员跳转到控制台
const goToManager = () => {
  router.push("/manager/welcome");
};

//退出登录
const logout = () => {
  localStorage.removeItem("code_user");
  location.href = "/home";
};
</script>
<template>
  <nav class="navbar">
    <div class="logo" @click="router.push('/home')">
      <i class="fas fa-camera"></i>
      <h1>光影视界<span>|</span></h1>
    </div>

    <ul class="nav-links">
      <li><router-link to="/home">首页</router-link></li>
      <li><router-link to="/images">作品集</router-link></li>
      <li><router-link to="/map">地图</router-link></li>
    </ul>

    <div class="nav-actions">
      <!-- 搜索框 -->
      <div class="search-box">
        <input
          type="text"
          placeholder="搜索作品..."
          v-model="searchQuery"
          @keyup.enter="searchPhotos"
        />
        <el-button
          ><el-icon :size="18"><Camera /></el-icon
        ></el-button>

        <button @click="searchByChs">
          <i class="fas fa-search"></i>
        </button>
      </div>

      <!-- 用户头像区域 -->

      <div class="user-actions" v-if="isLoggedIn">
        <div class="avatar-container">
          <el-dropdown>
            <div style="display: flex; align-items: center">
              <img
                :src="userAvatar"
                alt="用户头像"
                class="user-avatar"
                @click="handleAvatarClick"
              />
              <span class="el-dropdown-link" style="padding: 15px">
                {{ userName }}
              </span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goToPerson"
                  >个人信息</el-dropdown-item
                >
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item @click="router.push('/login')"
                  >切换账户</el-dropdown-item
                >
                <el-dropdown-item v-if="isAdmin" @click="goToManager"
                  >控制台</el-dropdown-item
                >
                <el-dropdown-item divided @click="logout"
                  >退出登录</el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <!-- 登录按钮 -->
      <button v-else class="btn" @click="router.push('/login')">
        <i class="fas fa-user"></i>
        登录
      </button>
    </div>
  </nav>
</template>

<style scoped>
.nav-links li {
  list-style: none;
}

.nav-links li a {
  position: relative;
  display: block;
  padding: 10px 15px;
  margin: 0 5px;
  text-decoration: none;
  color: var(--text-light);
  font-weight: 500;
  transition: 0.5s;
  z-index: 1;
}

.nav-links li a:hover {
  color: white;
}

.nav-links li a:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-top: 1px solid var(--highlight);
  border-bottom: 1px solid var(--highlight);
  transform: scaleY(2);
  opacity: 0;
  transition: 0.5s;
  z-index: -1;
  border-radius: 4px;
}

.nav-links li a:hover:before {
  transform: scaleY(1.2);
  opacity: 1;
}

.nav-links li a:after {
  content: "";
  position: absolute;
  top: 1px;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--highlight);
  transform: scale(0);
  transition: 0.5s;
  z-index: -1;
  border-radius: 4px;
}

.nav-links li a:hover:after {
  transform: scale(1);
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.avatar-container {
  position: relative;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
  border: 2px solid var(--highlight);
  transition: all 0.3s ease;
}

.user-avatar:hover {
  transform: scale(1.05);
  border-color: white;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.4);
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
  cursor: pointer;
  transition: var(--transition);
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
  gap: 1rem;
  list-style: none;
  margin: 0;
  padding: 0;
  position: static; /* 移除绝对定位 */
  transform: none; /* 移除居中转换 */
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

.router-link-active {
  color: var(--highlight) !important;
}

.router-link-active::after {
  width: 100% !important;
}

.logo:hover {
  transform: scale(1.05);
}
</style>
