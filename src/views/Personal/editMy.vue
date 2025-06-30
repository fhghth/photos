<script setup>
import { ref, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { userApi } from "../../utils/request";
import { ElMessage } from "element-plus";

//路由
const router = useRouter();
const route = useRoute();

// 获取用户状态
const currentUser = ref(
  JSON.parse(localStorage.getItem("code_user")) || {
    username: "游客",
    avatar: "",
    id: null,
  }
);

// 保存原始用户数据，用于重置
const originalUser = ref({ ...currentUser.value });

//主题
const theme = ref("light");

//无头像
const defaultAvatar = ref(
  "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
);

// 头像上传的input引用
const avatarInput = ref(null);

// 跟踪是否有新的头像文件
const hasNewAvatar = ref(false);

// 计算头像URL
const avatarUrl = computed(() => {
  if (!currentUser.value.avatar) {
    return defaultAvatar.value;
  }

  // 如果是base64数据，直接返回
  if (currentUser.value.avatar.startsWith("data:image")) {
    return currentUser.value.avatar;
  }

  // 如果是相对路径，添加baseURL
  if (currentUser.value.avatar.startsWith("/")) {
    return `http://localhost:7080${currentUser.value.avatar}`;
  }

  // 如果是完整URL，直接返回
  return currentUser.value.avatar;
});

//切换主题
const switchTheme = (newTheme) => {
  theme.value = newTheme;
  if (newTheme === "dark") {
    document.body.style.background =
      "linear-gradient(135deg, #1a1a2e 0%, #16213e 100%)";
    document.querySelector(".profile-container").style.background = "#1e1e2d";
    document.querySelector(".profile-container").style.color = "#f0f0f0";
    document.querySelector(".avatar-section").style.background = "#2d2d44";
    document.querySelector(".profile-footer").style.background = "#2d2d44";
  } else {
    document.body.style.background =
      "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)";
    document.querySelector(".profile-container").style.background = "white";
    document.querySelector(".profile-container").style.color = "#333";
    document.querySelector(".avatar-section").style.background = "#f8f9ff";
    document.querySelector(".profile-footer").style.background = "#f8f9ff";
  }
};

// 头像错误处理
const errorHandler = () => {
  return true; // 使用默认头像
};

// 触发文件上传
const triggerFileUpload = () => {
  avatarInput.value.click();
};

// 处理头像更改
const handleAvatarChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    // 创建文件读取器用于预览
    const reader = new FileReader();
    reader.onload = (event) => {
      // 将头像数据设置为读取的结果（base64）用于预览
      currentUser.value.avatar = event.target.result;
    };
    reader.readAsDataURL(file);

    // 标记有新头像文件
    hasNewAvatar.value = true;
  }
};

//保存更改
const saveProfile = async () => {
  try {
    // 创建FormData对象用于文件上传
    const formData = new FormData();
    formData.append("id", currentUser.value.id);
    formData.append("username", currentUser.value.username);
    formData.append("password", currentUser.value.password);
    formData.append("email", currentUser.value.email);
    formData.append("phone", currentUser.value.phone);

    // 如果有新头像文件，添加到FormData
    if (hasNewAvatar.value && avatarInput.value && avatarInput.value.files[0]) {
      formData.append("avatarFile", avatarInput.value.files[0]);
    }

    const res = await userApi.edit(formData);

    if (res.code === "200") {
      // 更新本地存储的用户信息
      const updatedUser = { ...currentUser.value };
      // 如果上传成功，更新头像路径
      if (res.data && res.data.avatar) {
        updatedUser.avatar = res.data.avatar;
      }
      localStorage.setItem("code_user", JSON.stringify(updatedUser));
      // 更新原始数据，用于重置
      originalUser.value = { ...updatedUser };
      // 重置头像状态
      hasNewAvatar.value = false;
      ElMessage.success("编辑成功");
    } else {
      ElMessage.error(res.msg || "编辑失败");
    }
  } catch (error) {
    console.error("编辑错误:", error);
    const errorMsg =
      error.response?.data?.msg || error.message || "编辑请求失败";
    ElMessage.error(errorMsg);
  }
};

//重置
const resetForm = () => {
  // 重置为原始数据
  currentUser.value = { ...originalUser.value };
  // 重置头像状态
  hasNewAvatar.value = false;
  // 清空文件输入
  if (avatarInput.value) {
    avatarInput.value.value = "";
  }
  ElMessage.info("已重置");
};

// 返回功能
const goBack = () => {
  router.go(-1);
};
</script>

<template>
  <div id="app">
    <!-- 返回按钮 -->
    <div class="back-button" @click="goBack">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        fill="currentColor"
        viewBox="0 0 16 16"
      >
        <path
          d="M8 0a8 8 0 1 0 0 16A8 8 0 0 0 8 0zm3.5 7.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5z"
        />
      </svg>
      返回
    </div>
    <!-- 主题切换按钮 -->
    <div class="theme-switch">
      <button class="theme-btn" @click="switchTheme('light')">🌞 明亮</button>
      <button class="theme-btn" @click="switchTheme('dark')">🌙 暗色</button>
    </div>

    <div class="profile-container">
      <div class="profile-header">
        <h1>编辑个人资料</h1>
        <p>更新您的个人信息和设置</p>
      </div>

      <div class="profile-content">
        <div class="avatar-section animation-fade-in">
          <h2 class="section-title">头像设置</h2>

          <div class="avatar-container">
            <div class="avatar-preview">
              <el-avatar :size="150" :src="avatarUrl" @error="errorHandler">
                <img :src="defaultAvatar" />
              </el-avatar>
              <div class="avatar-overlay">点击预览</div>
            </div>
          </div>

          <button class="upload-btn" @click="triggerFileUpload">
            <i class="fas fa-cloud-upload-alt"></i> 上传新头像
          </button>

          <input
            type="file"
            ref="avatarInput"
            accept="image/*"
            style="display: none"
            @change="handleAvatarChange"
          />

          <p style="color: #777; margin-top: 15px; line-height: 1.5">
            支持 JPG、PNG 或 GIF 格式<br />建议尺寸 200×200 像素
          </p>
        </div>

        <div
          class="form-section animation-fade-in"
          style="animation-delay: 0.2s"
        >
          <h2 class="section-title">个人信息</h2>

          <div class="form-group">
            <label>用户名</label>
            <el-input
              v-model="currentUser.username"
              placeholder="输入用户名"
              size="large"
              clearable
            >
              <template #prefix>
                <i class="fas fa-user"></i>
              </template>
            </el-input>
          </div>
          <div class="form-group">
            <label>密码</label>
            <el-input
              v-model="currentUser.password"
              placeholder="输入新密码"
              size="large"
              clearable
              type="password"
            >
              <template #prefix>
                <i class="fas fa-lock"></i>
              </template>
            </el-input>
          </div>

          <div class="form-group">
            <label>电子邮箱</label>
            <el-input
              v-model="currentUser.email"
              placeholder="输入邮箱地址"
              size="large"
              clearable
            >
              <template #prefix>
                <i class="fas fa-envelope"></i>
              </template>
            </el-input>
          </div>

          <div class="form-group">
            <label>手机号码</label>
            <el-input
              v-model="currentUser.phone"
              placeholder="输入手机号码"
              size="large"
              clearable
            >
              <template #prefix>
                <i class="fas fa-phone"></i>
              </template>
            </el-input>
          </div>
        </div>
      </div>

      <div class="profile-footer">
        <button class="action-btn cancel-btn" @click="resetForm">
          <i class="fas fa-undo-alt"></i> 重置
        </button>
        <button class="action-btn save-btn" @click="saveProfile">
          <i class="fas fa-save"></i> 保存更改
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 返回按钮样式 */
.back-button {
  position: absolute;
  top: 20px;
  left: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 15px;
  background-color: #f5f5f5;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 10;
  font-size: 0.95rem;
  color: #5c5c5c;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.back-button:hover {
  background-color: #eaeaea;
  transform: translateX(-3px);
}

.back-button svg {
  width: 18px;
  height: 18px;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

body {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.profile-container {
  background: white;
  border-radius: 20px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 800px;
  overflow: hidden;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
  margin: 20px;
}

.profile-container:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.profile-header {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  color: white;
  padding: 30px 40px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.profile-header::before {
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.1) 0%,
    transparent 70%
  );
  transform: rotate(30deg);
}

.profile-header h1 {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 10px;
  position: relative;
}

.profile-header p {
  font-size: 1.1rem;
  opacity: 0.9;
  position: relative;
}

.profile-content {
  padding: 40px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

@media (max-width: 768px) {
  .profile-content {
    grid-template-columns: 1fr;
  }
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 20px;
  background: #f8f9ff;
  border-radius: 15px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.avatar-section:hover {
  transform: scale(1.02);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.avatar-container {
  position: relative;
  margin-bottom: 20px;
}

.avatar-preview {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: 4px solid white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: relative;
  transition: all 0.3s ease;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-preview:hover {
  transform: scale(1.05);
}

.avatar-overlay {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  text-align: center;
  padding: 8px;
  font-size: 0.9rem;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-container:hover .avatar-overlay {
  opacity: 1;
}

.upload-btn {
  margin-top: 15px;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 10px rgba(106, 17, 203, 0.3);
}

.upload-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(106, 17, 203, 0.4);
}

.upload-btn i {
  font-size: 1.2rem;
}

.form-section {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.form-group {
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #344767;
  font-size: 1rem;
  padding-left: 5px;
}

.el-input,
.el-select {
  width: 100%;
}

.profile-footer {
  padding: 30px 40px;
  background: #f8f9ff;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  border-top: 1px solid #eee;
}

.action-btn {
  padding: 12px 30px;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cancel-btn {
  background: #f8f9ff;
  color: #7e8a9a;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.cancel-btn:hover {
  background: #eef2f7;
  color: #6a11cb;
}

.save-btn {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(106, 17, 203, 0.3);
}

.save-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(106, 17, 203, 0.4);
}

.animation-fade-in {
  animation: fadeIn 0.6s ease forwards;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.section-title {
  font-size: 1.4rem;
  margin-bottom: 20px;
  color: #2c3e50;
  position: relative;
  padding-bottom: 10px;
  border-bottom: 2px solid #e0e7ff;
}

.section-title::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: -2px;
  width: 60px;
  height: 2px;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
}

.theme-switch {
  position: fixed;
  top: 20px;
  right: 20px;
  background: white;
  border-radius: 12px;
  padding: 10px 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  z-index: 100;
  display: flex;
  gap: 10px;
}

.theme-btn {
  background: #f0f2f5;
  border: none;
  border-radius: 10px;
  padding: 8px 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.theme-btn:hover {
  background: #6a11cb;
  color: white;
}
</style>
