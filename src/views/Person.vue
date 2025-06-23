<template>
  <!-- 加载 -->
  <Loading />
  <!-- 壁纸 -->
  <Background @loadComplete="loadComplete" />
  <!-- 主界面 -->
  <Transition name="fade" mode="out-in">
    <main id="main" v-if="store.imgLoadStatus">
      <div class="container" v-show="!store.backgroundShow">
        <section class="all" v-show="!store.setOpenState">
          <MainLeft />
          <MainRight v-show="!store.boxOpenState" />
          <Box v-show="store.boxOpenState" />
        </section>
        <section
          class="more"
          v-show="store.setOpenState"
          @click="store.setOpenState = false"
        >
          <MoreSet />
        </section>
      </div>
      <!-- 移动端菜单按钮 -->
      <Icon
        class="menu"
        size="24"
        v-show="!store.backgroundShow"
        @click="store.mobileOpenState = !store.mobileOpenState"
      >
        <component :is="store.mobileOpenState ? CloseSmall : HamburgerButton" />
      </Icon>
      <!-- 页脚 -->
      <Transition name="fade" mode="out-in">
        <Footer
          class="f-ter"
          v-show="!store.backgroundShow && !store.setOpenState"
        />
      </Transition>
    </main>
  </Transition>
</template>

<script setup>
import { HamburgerButton, CloseSmall } from "@icon-park/vue-next";
import { mainStore } from "@/store";
import { Icon } from "@vicons/utils";
import Loading from "@/components/personal/Loading.vue";
import Background from "@/components/personal/Background.vue";
import Footer from "@/components/personal/Footer.vue";

import Box from "@/views/Personal/Box/index.vue";
import MoreSet from "@/views/Personal/MoreSet/index.vue";
import MainLeft from "@/views/Personal/Main/Left.vue";
import MainRight from "@/views/Personal/Main/Right.vue";

import { helloInit, checkDays } from "@/utils/getTime.js";
import cursorInit from "@/utils/cursor.js";

import config from "@/../package.json";
import { watch, onMounted, onBeforeUnmount, nextTick } from "vue";

const store = mainStore();

// 页面宽度
const getWidth = () => {
  store.setInnerWidth(window.innerWidth);
};

// 加载完成事件
const loadComplete = () => {
  nextTick(() => {
    // 欢迎提示
    helloInit();
    // 默哀模式
    checkDays();
  });
};

// 监听宽度变化
watch(
  () => store.innerWidth,
  (value) => {
    if (value < 721) {
      store.boxOpenState = false;
      store.setOpenState = false;
    }
  }
);

onMounted(() => {
  // 自定义鼠标
  cursorInit();

  // 屏蔽右键
  document.oncontextmenu = () => {
    ElMessage({
      message: "为了浏览体验，本站禁用右键",
      grouping: true,
      duration: 2000,
    });
    return false;
  };

  // 鼠标中键事件
  window.addEventListener("mousedown", (event) => {
    if (event.button == 1) {
      store.backgroundShow = !store.backgroundShow;
      ElMessage({
        message: `已${store.backgroundShow ? "开启" : "退出"}壁纸展示状态`,
        grouping: true,
      });
    }
  });

  // 监听当前页面宽度
  getWidth();
  window.addEventListener("resize", getWidth);
});

onBeforeUnmount(() => {
  window.removeEventListener("resize", getWidth);
});
</script>

<style lang="scss" scoped>
#main {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  transform: scale(1.2);
  transition: transform 0.3s;
  animation: fade-blur-main-in 0.65s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
  animation-delay: 0.5s;
  .container {
    width: 100%;
    height: 100vh;
    margin: 0 auto;
    padding: 0 0.5vw;
    .all {
      width: 100%;
      height: 100%;
      padding: 0 0.75rem;
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
    }
    .more {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: #00000080;
      backdrop-filter: blur(20px);
      z-index: 2;
      animation: fade 0.5s;
    }
    @media (max-width: 1200px) {
      padding: 0 2vw;
    }
  }
  .menu {
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    top: 84%;
    left: calc(50% - 28px);
    width: 56px;
    height: 34px;
    background: rgb(0 0 0 / 20%);
    backdrop-filter: blur(10px);
    border-radius: 6px;
    transition: transform 0.3s;
    animation: fade 0.5s;
    &:active {
      transform: scale(0.95);
    }
    .i-icon {
      transform: translateY(2px);
    }
    @media (min-width: 721px) {
      display: none;
    }
  }
  @media (max-height: 720px) {
    overflow-y: auto;
    overflow-x: hidden;
    .container {
      height: 721px;
      .more {
        height: 721px;
        width: calc(100% + 6px);
      }
      @media (min-width: 391px) {
        // w 1201px ~ max
        padding-left: 0.7vw;
        padding-right: 0.25vw;
        @media (max-width: 1200px) { // w 1101px ~ 1280px
          padding-left: 2.3vw;
          padding-right: 1.75vw;
        }
        @media (max-width: 1100px) { // w 993px ~ 1100px
          padding-left: 2vw;
          padding-right: calc(2vw - 6px);
        }
        @media (max-width: 992px) { // w 901px ~ 992px
          padding-left: 2.3vw;
          padding-right: 1.7vw;
        }
        @media (max-width: 900px) { // w 391px ~ 900px
          padding-left: 2vw;
          padding-right: calc(2vw - 6px);
        }
      }
    }
    .menu {
      top: 605.64px; // 721px * 0.84
      left: 170.5px; // 391 * 0.5 - 25px
      @media (min-width: 391px) {
        left: calc(50% - 25px);
      }
    }
    .f-ter {
      top: 675px; // 721px - 46px
      @media (min-width: 391px) {
        padding-left: 6px;
      }
    }
  }
  @media (max-width: 390px) {
    overflow-x: auto;
    .container {
      width: 391px;
    }
    .menu {
      left: 167.5px; // 391px * 0.5 - 28px
    }
    .f-ter {
      width: 391px;
    }
    @media (min-height: 721px) {
      overflow-y: hidden;
    }
  }
}

/* Person组件特定样式 */

// 字体文件
@font-face {
  font-family: "Pacifico-Regular";
  font-display: swap;
  src: url("../style/font/Pacifico-Regular.ttf") format("truetype");
}

@font-face {
  font-family: "UnidreamLED";
  font-display: swap;
  src: url("../style/font/UnidreamLED.ttf") format("truetype");
}

// 卡片样式
.cards {
  border-radius: 6px;
  background-color: #00000040;
  backdrop-filter: blur(10px);
  transform: scale(1);
  transition: backdrop-filter 0.3s, transform 0.3s;
  &:hover {
    transform: scale(1.01);
  }
  &:active {
    transform: scale(0.98);
  }
}

// 文字超出
.text-hidden {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

// 弹窗样式
.el-message {
  --el-message-bg-color: #00000040 !important;
  --el-message-text-color: #efefef !important;
  -webkit-backdrop-filter: blur(10px);
  backdrop-filter: blur(10px);
  border-radius: 25px !important;
  border-color: transparent !important;

  .el-message__badge {
    display: none;
  }
  .el-message__content {
    white-space: nowrap;
  }
}

// 进度条样式
.el-progress-bar {
  .el-progress-bar__outer {
    border-radius: 6px;
    background-color: #00000020;
    .el-progress-bar__inner {
      background-color: #efefef;
      border-radius: 6px;
      text-align: center;
      font-family: "UnidreamLED";
      span {
        color: #564d59;
        font-size: 0.9rem;
      }
    }
  }
}

// Tooltip 样式
.el-popper.is-dark {
  background: #ffffff60 !important;
  border: 1px solid transparent !important;
}

// 卡片样式
.el-card {
  border-radius: 8px !important;
  border: 1px solid transparent !important;
  background-color: transparent !important;
  .el-card__header {
    font-weight: bold;
    padding: 16px 20px !important;
    background-color: #ffffff30 !important;
    border-bottom: 1px solid transparent !important;
  }
  .el-card__body {
    padding: 0 !important;
    background-color: #ffffff10 !important;
  }
}

// Transition 动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease-in-out;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

// keyframes 动画
@keyframes fade {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fade-blur-in {
  from {
    filter: blur(20px) brightness(0.3);
    transform: scale(1.6);
  }
  to {
    filter: blur(0) brightness(1);
    transform: scale(1);
  }
}

@keyframes fade-blur-main-in {
  from {
    transform: scale(1.2);
  }
  to {
    transform: scale(1);
  }
}

// 隐藏元素
@media (min-width: 910px) and (max-width: 1200px) {
  .sm-hidden {
    display: none;
  }
}

// 滚动条样式（保留但允许滚动）
::-webkit-scrollbar,
scrollbar {
  width: 6px;
  height: 6px;
  background-color: transparent;
}

::-webkit-scrollbar-thumb,
scrollbar-thumb {
  border-radius: 10px;
  background-color: #eeeeee;
}
/*全局样式*/

*,
a,
p {
  margin: 0;
  padding: 0;
  user-select: none;
  text-decoration: none;
  color: #fff;
  box-sizing: border-box;

  &:hover {
    transition: 0.3s;
  }
}

// 链接悬停效果
a:hover {
  color: rgb(57, 159, 255);
  text-decoration: underline;
}

// 允许页面滚动
body {
  overflow: auto;
  overscroll-behavior: contain;
}
</style>
