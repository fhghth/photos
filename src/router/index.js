//1.导入相应模块
import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/home",
    name: "home",
    component: () => import("../views/Home.vue"),
  },
  {
    path: "/images",
    name: "images",
    component: () => import("../views/Images.vue"),
  },
  {
    path: "/work/:id",
    name: "WorkDetail",
    component: () => import("../views/Works/WorkDetail.vue"),
  },
  {
    path: "/map",
    name: "map",
    component: () => import("../views/Map/Map.vue"),
  },
  {
    path: "/manager",
    name: "manager",
    component: () => import("../views/Manager.vue"),
    redirect: "/manager/dashboard",
    children: [
      {
        path: "welcome",
        name: "welcome",
        component: () => import("../views/Admin/Welcome.vue"),
        meta: { requiresAdmin: true }, // 标记需要管理员权限
      },

      {
        path: "dashboard",
        name: "dashboard",
        component: () => import("../views/Dashboard.vue"),
        meta: { requiresAdmin: true }, // 标记需要管理员权限
        children: [
          //用户(管理员)管理
          {
            path: "admin",
            name: "admin",
            component: () => import("../views/Admin/Admin.vue"),
          },
          //用户（用户）管理
          {
            path: "user",
            name: "user",
            component: () => import("../views/Admin/User.vue"),
          },
          //所有作品管理
          {
            path: "allworks",
            name: "allworks",
            component: () => import("../views/Admin/AllWorks.vue"),
          },
          //审核（作品）管理
          {
            path: "works",
            name: "works",
            component: () => import("../views/Admin/Works.vue"),
          },
          //内容（评论）管理
          {
            path: "comments",
            name: "comments",
            component: () => import("../views/Admin/Comments.vue"),
          },
        ],
      },
    ],
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/person",
    name: "person",
    component: () => import("../views/Person.vue"),
  },
  {
    path: "/mywork",
    name: "mywork",
    component: () => import("../views/Personal/myWork.vue"),
  },
  {
    path: "/editmy",
    name: "editmy",
    component: () => import("../views/Personal/editMy.vue"),
  },
  {
    path: "/uploadmodal",
    name: "uploadmodal",
    component: () => import("../components/common/UploadModal.vue"),
  },
  {
    path: "/404",
    name: "notFound",
    component: () => import("../views/404.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },
];

//4.创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes,
  linkActiveClass: "active",
});

// 全局路由守卫
router.beforeEach((to, from, next) => {
  // 检查所有以 /manager 开头的路由
  if (to.matched.some((record) => record.path.startsWith("/manager"))) {
    const userData = JSON.parse(localStorage.getItem("code_user") || "{}");

    // 检查用户是否登录且是管理员（role=0）
    if (userData.id && userData.role === "0") {
      next(); // 放行
    } else {
      // 重定向到登录页，并携带原始路径
      next({
        path: "/login",
        query: { redirect: to.fullPath },
      });
    }
  } else {
    next(); // 非管理路由直接放行
  }
});
//5.导出路由实例
export default router;
