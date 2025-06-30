<template>
  <!-- 基本信息 -->
  <div class="message">
    <!-- 用户头像和姓名 -->
    <div class="logo">
      <img
        class="logo-img"
        :src="userAvatar"
        alt="用户头像"
        @error="handleAvatarError"
      />
      <div
        :class="{
          name: true,
          'text-hidden': true,
          long: userName.length >= 6,
        }"
      >
        <span class="bg">{{ userName }}</span>
        <span class="sm">.{{ userRole }}</span>
      </div>
    </div>
    <!-- 简介 -->
    <div class="description cards" @click="changeBox">
      <div class="content">
        <Icon size="16">
          <QuoteLeft />
        </Icon>
        <Transition name="fade" mode="out-in">
          <div :key="descriptionText.hello + descriptionText.text" class="text">
            <p>{{ descriptionText.hello }}</p>
            <p>{{ descriptionText.text }}</p>
          </div>
        </Transition>
        <Icon size="16">
          <QuoteRight />
        </Icon>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Icon } from "@vicons/utils";
import { QuoteLeft, QuoteRight } from "@vicons/fa";
import { Error } from "@icon-park/vue-next";
import { mainStore } from "@/store";
import { computed, reactive, watch, ref } from "vue";

const store = mainStore();

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

// 获取用户信息
const currentUser = ref(
  JSON.parse(localStorage.getItem("code_user")) || {
    username: "游客",
    avatar: "",
    id: null,
    role: "user",
  }
);

// 用户头像
const userAvatar = computed(() => {
  return getAvatarUrl(currentUser.value.avatar);
});

// 用户名
const userName = computed(() => {
  return currentUser.value.username || "游客";
});

// 用户角色
const userRole = computed(() => {
  const role = currentUser.value.role;
  if (role === "0") return "管理员";
  if (role === "1") return "用户";
  return "其他";
});

// 头像错误处理
const handleAvatarError = (e) => {
  e.target.src = defaultAvatar.value;
};

// 简介区域文字
const descriptionText = reactive({
  hello: import.meta.env.VITE_DESC_HELLO,
  text: import.meta.env.VITE_DESC_TEXT,
});

// 切换右侧功能区
const changeBox = () => {
  if (store.getInnerWidth >= 721) {
    store.boxOpenState = !store.boxOpenState;
  } else {
    ElMessage({
      message: "当前页面宽度不足以开启盒子",
      grouping: true,
      icon: h(Error, {
        theme: "filled",
        fill: "#efefef",
      }),
    });
  }
};

// 监听状态变化
watch(
  () => store.boxOpenState,
  (value) => {
    if (value) {
      descriptionText.hello = import.meta.env.VITE_DESC_HELLO_OTHER;
      descriptionText.text = import.meta.env.VITE_DESC_TEXT_OTHER;
    } else {
      descriptionText.hello = import.meta.env.VITE_DESC_HELLO;
      descriptionText.text = import.meta.env.VITE_DESC_TEXT;
    }
  }
);

// 监听localStorage变化，实时更新用户信息
const updateUserInfo = () => {
  try {
    const userData = JSON.parse(localStorage.getItem("code_user") || "{}");
    if (userData && userData.id) {
      currentUser.value = userData;
    }
  } catch (error) {
    console.error("更新用户信息失败:", error);
  }
};

// 监听storage事件
window.addEventListener("storage", (event) => {
  if (event.key === "code_user") {
    updateUserInfo();
  }
});

// 初始化时更新用户信息
updateUserInfo();
</script>

<style lang="scss" scoped>
.message {
  .logo {
    display: flex;
    flex-direction: row;
    align-items: center;
    animation: fade 0.5s;
    max-width: 460px;
    .logo-img {
      border-radius: 50%;
      width: 120px;
    }
    .name {
      width: 100%;
      padding-left: 22px;
      transform: translateY(-8px);
      font-family: "Pacifico-Regular";

      .bg {
        font-size: 5rem;
      }

      .sm {
        margin-left: 6px;
        font-size: 2rem;
        @media (min-width: 721px) and (max-width: 789px) {
          display: none;
        }
      }
    }
    @media (max-width: 768px) {
      .logo-img {
        width: 100px;
      }
      .name {
        height: 128px;
        .bg {
          font-size: 4.5rem;
        }
      }
    }

    @media (max-width: 720px) {
      max-width: 100%;
    }
  }

  .description {
    padding: 1rem;
    margin-top: 3.5rem;
    max-width: 460px;
    animation: fade 0.5s;

    .content {
      display: flex;
      justify-content: space-between;

      .text {
        margin: 0.75rem 1rem;
        line-height: 2rem;
        margin-right: auto;
        transition: opacity 0.2s;

        p {
          &:nth-of-type(1) {
            font-family: "Pacifico-Regular";
          }
        }
      }

      .xicon:nth-of-type(2) {
        align-self: flex-end;
      }
    }
    @media (max-width: 720px) {
      max-width: 100%;
      pointer-events: none;
    }
  }
  // @media (max-width: 390px) {
  //   .logo {
  //     flex-direction: column;
  //     .logo-img {
  //       display: none;
  //     }
  //     .name {
  //       margin-left: 0;
  //       height: auto;
  //       transform: none;
  //       text-align: center;
  //       .bg {
  //         font-size: 3.5rem;
  //       }
  //       .sm {
  //         font-size: 1.4rem;
  //       }
  //     }
  //   }
  //   .description {
  //     margin-top: 2.5rem;
  //   }
  // }
}
</style>
