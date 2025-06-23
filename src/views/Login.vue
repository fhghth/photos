<template>
  <div class="login-container">
    <div class="login-shell">
      <!-- 注册面板 -->
      <div
        class="container a-container"
        :class="{
          'slide-active': activePanel === 'register',
          'slide-inactive': activePanel === 'login',
        }"
      >
        <form class="form">
          <h2 class="form_title title">创建账号</h2>
          <div class="form_icons">
            <i class="iconfont icon-qq"></i>
            <i class="iconfont icon-wechat"></i>
            <i class="iconfont icon-bilibili"></i>
          </div>
          <span class="form_span">选择注册方式或电子邮箱注册</span>
          <input
            type="text"
            class="form_input"
            placeholder="用户名"
            v-model="registerData.username"
          />
          <input
            type="email"
            class="form_input"
            placeholder="电子邮箱"
            v-model="registerData.email"
          />
          <div class="password-input">
            <input
              :type="showRegisterPassword ? 'text' : 'password'"
              class="form_input"
              placeholder="密码"
              v-model="registerData.password"
            />
            <span
              @click="showRegisterPassword = !showRegisterPassword"
              class="password-toggle"
            >
              <i
                :class="
                  showRegisterPassword ? 'far fa-eye-slash' : 'far fa-eye'
                "
              ></i>
            </span>
          </div>
          <button
            class="form_button button submit"
            @click.prevent="handleRegister"
          >
            注册账号
          </button>
        </form>
      </div>

      <!-- 登录面板 -->
      <div
        class="container b-container"
        :class="{
          'slide-active': activePanel === 'login',
          'slide-inactive': activePanel === 'register',
        }"
      >
        <form class="form">
          <h2 class="form_title title">登入账号</h2>
          <div class="form_icons">
            <i class="iconfont icon-qq"></i>
            <i class="iconfont icon-wechat"></i>
            <i class="iconfont icon-bilibili"></i>
          </div>
          <span class="form_span">选择登录方式或电子邮箱登录</span>
          <input
            type="user"
            class="form_input"
            :class="{ 'input-error': errors.username }"
            placeholder="用户名"
            v-model="formData.username"
            @input="clearError('user')"
          />
          <div class="password-input">
            <input
              :type="showPassword ? 'text' : 'password'"
              class="form_input"
              :class="{ 'input-error': errors.password }"
              placeholder="密码"
              v-model="formData.password"
              @input="clearError('password')"
            />
            <span @click="showPassword = !showPassword" class="password-toggle">
              <i :class="showPassword ? 'far fa-eye-slash' : 'far fa-eye'"></i>
            </span>
          </div>
          <div class="remember-forgot">
            <div class="remember-me">
              <input
                type="checkbox"
                id="remember"
                v-model="formData.rememberMe"
              />
              <label for="remember">记住我</label>
            </div>
            <a href="#" class="forgot-password">忘记密码?</a>
          </div>
          <button class="form_button button submit" @click.prevent="login">
            登入账号
          </button>
        </form>
      </div>

      <!-- 中间切换区域 -->
      <div
        class="switch"
        id="switch-cnt"
        :class="{
          'is-txr': activePanel === 'register',
          'is-txl': activePanel === 'login',
        }"
      >
        <div
          class="switch_circle"
          :class="{ 'is-txr': activePanel === 'register' }"
        ></div>
        <div
          class="switch_circle switch_circle-t"
          :class="{ 'is-txr': activePanel === 'register' }"
        ></div>

        <!-- 登录面板时的切换内容 -->
        <div
          class="switch_container"
          :class="{ 'is-hidden': activePanel === 'register' }"
        >
          <h2 class="switch_title title" style="letter-spacing: 0">
            你好朋友！
          </h2>
          <p class="switch_description description">
            去注册一个账号，成为尊贵的粉丝会员，让我们踏入奇妙的旅途！
          </p>
          <button
            class="switch_button button switch-btn"
            @click="switchToRegister"
          >
            注册账号
          </button>
        </div>

        <!-- 注册面板时的切换内容 -->
        <div
          class="switch_container"
          :class="{ 'is-hidden': activePanel === 'login' }"
        >
          <h2 class="switch_title title" style="letter-spacing: 0">
            欢迎回来！
          </h2>
          <p class="switch_description description">
            已经有账号了嘛，去登入账号来进入奇妙世界吧！！！
          </p>
          <button
            class="switch_button button switch-btn"
            @click="switchToLogin"
          >
            登入账号
          </button>
        </div>
      </div>
    </div>

    <button @click="router.push('/home')" class="home-button">
      <i class="fas fa-home"></i> 返回首页
    </button>

    <!-- 版本信息 -->
    <div class="version-info">光影视界 v1.5.0</div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { userApi } from "../utils/request";
import { ElMessage } from "element-plus";

const router = useRouter();
const route = useRoute(); // 添加 route 对象
// 登录数据
const formData = ref({
  username: "",
  password: "",
  rememberMe: false,
});

// 注册数据
const registerData = ref({
  username: "",
  email: "",
  password: "",
});

const showPassword = ref(false);
const showRegisterPassword = ref(false);

//清空
const errors = ref({
  username: "",
  password: "",
});

// 当前活动面板（初始为登录）
const activePanel = ref("login");

// 平滑切换到注册面板
const switchToRegister = () => {
  activePanel.value = "register";
};

// 平滑切换到登录面板
const switchToLogin = () => {
  activePanel.value = "login";
};

// 清除错误
const clearError = (field) => {
  if (errors.value[field]) {
    errors.value[field] = "";
  }
};

// 提交登录
const login = async () => {
  // 在控制台打印用户名和密码
  console.log("尝试登录 - 用户名:", formData.value.username);
  console.log("尝试登录 - 密码:", formData.value.password);

  try {
    const res = await userApi.login({
      username: formData.value.username,
      password: formData.value.password,
    });

    console.log("登录响应:", res);

    if (res.code === "200") {
      // 存储用户信息，包含头像
      const userData = {
        ...res.data,
        avatar: res.data.avatar || "/src/assets/imgs/default-avatar.jpg", // 修改默认头像路径
      };
      localStorage.setItem("code_user", JSON.stringify(userData));

      // 检查重定向路径
      const redirectPath = route.query.redirect;

      // 根据用户角色决定重定向目标
      if (userData.role === "0") {
        // 管理员：重定向到原始路径或管理首页
        router.push(redirectPath || "/manager/dashboard");
      } else {
        // 普通用户：重定向到原始路径或网站首页
        router.push(redirectPath || "/home");
      }

      // 触发存储事件，通知其他组件用户信息已更新
      window.dispatchEvent(new Event("storage"));
      console.log("登录状态验证成功，即将跳转");
      ElMessage.success("登录成功");
    } else {
      ElMessage.error(res.msg || "登录失败");
    }
  } catch (error) {
    console.error("登录错误:", error);
    const errorMsg =
      error.response?.data?.msg || error.message || "登录请求失败";
    ElMessage.error(errorMsg);
  }
};

// 处理注册
const handleRegister = async () => {
  console.log("尝试注册 - 用户名:", registerData.value.username);
  console.log("尝试注册 - 邮箱:", registerData.value.email);
  console.log("尝试注册 - 密码:", registerData.value.password);

  try {
    const res = await userApi.register({
      ...registerData.value,
      role: "1", // 设置为普通用户
      name: registerData.value.username, // 昵称默认等于用户名
      phone: "", // 手机号默认为空
    });

    console.log("注册响应:", res);

    if (res.code === "200") {
      console.log("注册状态验证成功");
      ElMessage.success("注册成功");
    } else {
      ElMessage.error(res.msg || "注册失败");
    }
  } catch (error) {
    console.error("注册错误:", error);
    const errorMsg =
      error.response?.data?.msg || error.message || "注册请求失败";
    ElMessage.error(errorMsg);
  }
};
</script>

<style scoped>
/* 居中和滑动动画 */
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #ecf0f3;
  z-index: 1000;
  overflow: auto;
  padding: 20px;
}

.login-shell {
  position: relative;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 12px;
  overflow: hidden;
  z-index: 1001;
}

/* 面板定位和动画 */
.container {
  position: absolute;
  top: 0;
  width: 600px;
  height: 100%;
  padding: 25px;
  background-color: #ecf0f3;
  transition: all 0.7s cubic-bezier(0.23, 1, 0.32, 1);
  display: flex;
  justify-content: center;
  align-items: center;
}

.a-container {
  left: 0;
  z-index: 2;
}

.b-container {
  left: 400px;
  z-index: 1;
}

/* 滑动动画控制 */
.container.slide-active {
  transform: translateX(0);
  z-index: 2;
  opacity: 1;
  visibility: visible;
}

.container.slide-inactive {
  transform: translateX(100%);
  z-index: 1;
  opacity: 0;
  visibility: hidden;
}

/* 注册面板初始位置（右侧） */
.a-container.slide-inactive {
  transform: translateX(100%);
  visibility: hidden;
}

.a-container.slide-active {
  transform: translateX(0);
  visibility: visible;
}

/* 登录面板初始位置（中间） */
.b-container.slide-active {
  transform: translateX(0);
  visibility: visible;
}

.b-container.slide-inactive {
  transform: translateX(-100%);
  visibility: hidden;
}

.form {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.iconfont {
  margin: 0 12px;
  font-size: 25px;
  padding: 10px;
  border-radius: 50%;
  background: #ecf0f3;
  box-shadow: 5px 5px 10px #d1d9e6, -5px -5px 10px #ffffff;
  color: #4b70e2;
  transition: all 0.3s ease;
}

.iconfont:hover {
  transform: translateY(-5px);
  box-shadow: 3px 3px 5px #d1d9e6, -3px -3px 5px #ffffff;
  color: #ff6b6b;
  cursor: pointer;
}

.form_input {
  width: 350px;
  height: 45px;
  margin: 10px 0;
  padding: 0 25px;
  font-size: 16px;
  letter-spacing: 0.15px;
  border: none;
  outline: none;
  background-color: #ecf0f3;
  transition: 0.25s ease;
  border-radius: 12px;
  box-shadow: inset 4px 4px 6px #d1d9e6, inset -4px -4px 6px #ffffff;
  color: #555;
  font-weight: 500;
}

.input-error {
  box-shadow: inset 4px 4px 6px #ffb8b8, inset -4px -4px 6px #ffffff;
}

.form_input:focus {
  box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #ffffff;
}

.password-input {
  position: relative;
  width: 350px;
  margin: 10px 0;
}

.password-toggle {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  color: #777;
  cursor: pointer;
  font-size: 18px;
  transition: all 0.3s ease;
}

.password-toggle:hover {
  color: #4b70e2;
}

.form_span {
  margin: 20px 0;
  color: #555;
  font-size: 14px;
  font-weight: 500;
}

.form_link {
  color: #4b70e2;
  font-size: 15px;
  margin-top: 10px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.form_link:hover {
  color: #ff6b6b;
  text-decoration: underline;
}

.title {
  font-size: 28px;
  font-weight: 800;
  line-height: 1.5;
  color: #2d3748;
  letter-spacing: 3px;
  text-shadow: 2px 2px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 5px;
  background: linear-gradient(135deg, #4b70e2, #ff6b6b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  color: transparent;
}

.description {
  font-size: 16px;
  letter-spacing: 0.5px;
  text-align: center;
  line-height: 1.6;
  color: #555;
  max-width: 350px;
  margin-bottom: 15px;
  font-weight: 500;
}

.button {
  width: 200px;
  height: 50px;
  border-radius: 25px;
  margin-top: 15px;
  font-weight: 700;
  font-size: 16px;
  letter-spacing: 1px;
  background: linear-gradient(135deg, #4b70e2, #6c5ce7);
  color: #ffffff;
  box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #ffffff;
  border: none;
  outline: none;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.button:hover {
  transform: translateY(-5px);
  box-shadow: 5px 5px 15px #d1d9e6, -5px -5px 15px #ffffff;
}

.button:active {
  transform: translateY(0);
  box-shadow: 3px 3px 8px #d1d9e6, -3px -3px 8px #ffffff;
}

/* 中间切换区域 */
.switch {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 400px;
  padding: 50px;
  z-index: 3;
  transition: all 0.7s cubic-bezier(0.23, 1, 0.32, 1);
  background-color: rgba(236, 240, 243, 0.9);
  overflow: hidden;
  box-shadow: 5px 5px 15px rgba(209, 217, 230, 0.6),
    -5px -5px 15px rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(5px);
  border-radius: 20px;
  margin: 0;
}

.is-txr {
  left: calc(100% - 400px);
}

.is-txl {
  left: 0;
}

.switch_circle {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background-color: #ecf0f3;
  box-shadow: inset 8px 8px 12px #d1d9e6, inset -8px -8px 12px #ffffff;
  bottom: -60%;
  left: -60%;
  transition: all 0.7s cubic-bezier(0.23, 1, 0.32, 1);
}

.switch_circle-t {
  top: -30%;
  left: 60%;
  width: 300px;
  height: 300px;
}

.switch_container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: absolute;
  width: 400px;
  padding: 40px 55px;
  transition: all 0.5s ease;
  text-align: center;
}

.is-hidden {
  visibility: hidden;
  opacity: 0;
  position: absolute;
  transition: all 0.5s ease;
}

/* 返回首页按钮 */
.home-button {
  position: absolute;
  top: 25px;
  right: 30px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #4b70e2, #6c5ce7);
  color: white;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  box-shadow: 5px 5px 15px #d1d9e6, -5px -5px 15px #ffffff;
  transition: all 0.3s ease;
  z-index: 2000;
}

.home-button:hover {
  transform: translateY(-3px);
  box-shadow: 3px 3px 10px #d1d9e6, -3px -3px 10px #ffffff;
}

.home-button:active {
  transform: translateY(0);
}

/* 版本信息 */
.version-info {
  position: absolute;
  bottom: 20px;
  right: 30px;
  color: #6c757d;
  font-size: 12px;
  font-weight: 500;
  z-index: 2000;
}

/* 记住我复选框 */
.remember-forgot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 350px;
  margin: 15px 0;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #555;
  font-weight: 500;
  font-size: 14px;
}

.remember-me input {
  width: 18px;
  height: 18px;
  accent-color: #4b70e2;
}

/* 动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s, transform 0.5s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translate(-50%, -30px);
}
.fade-enter-to,
.fade-leave-from {
  opacity: 1;
  transform: translate(-50%, 0);
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .login-shell {
    transform: scale(0.7);
  }
}

@media (max-width: 1000px) {
  .login-shell {
    transform: scale(0.6);
  }
}

@media (max-width: 800px) {
  .login-shell {
    transform: scale(0.5);
  }
}

@media (max-width: 600px) {
  .login-shell {
    transform: scale(0.4);
  }
}
</style>
