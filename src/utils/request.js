import axios from "axios";

// 创建axios实例
const api = axios.create({
  baseURL: "http://localhost:7080",
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    if (config.method === "post") {
      config.data = config.data || {};
    }
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error("请求错误:", error);
    return Promise.reject(error);
  }
);

// 用户相关接口
export const userApi = {
  login: (data) => api.post("/login", data), //用了
  register: (data) => api.post("/register", data), //用了
  edit: (data) => {
    // 检查是否为FormData，如果是则设置正确的Content-Type
    if (data instanceof FormData) {
      return api.post("/edit", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
    } else {
      return api.post("/edit", data);
    }
  },
};

// 作品相关接口
export const photoApi = {
  // 获取轮播图数据
  getCarousel: () => api.get("/photos/carousel"),

  // 获取作品列表
  getApproved: (params) => api.get("/photos/worksApprove", { params }), //用了

  // 获取特色系列
  getFeaturedSeries: () => api.get("/photos/series"),

  // 上传作品
  uploadPhoto: (formData) =>
    api.post("/photos/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }), //用了

  // 点赞作品
  likePhoto: (data) => api.post("/work-stats/like", data), //用了

  //检查点赞
  checkLikeStatus: (data) => api.get("/work-stats/check", { params: data }), //用了

  // 浏览作品
  viewPhoto: (data) => api.post("/work-stats/view", data), //用了

  //获取作品评论
  getComments: (workId) => api.get(`/photos/comments/${workId}`), //用了

  //提交评论
  submitComment: (data) => api.post("/photos/comment", data), //用了

  // 获取作品详情
  getPhotoDetail: (photoId) => api.get(`/photos/detail/${photoId}`), //用了

  // 重新提交作品
  resubmitPhoto: (workId) => api.put(`/photos/${workId}/resubmit`), //用了

  // 删除作品
  deletePhoto: (workId) => api.delete(`/photos/delete/${workId}`), //用了
  // 获取用户所有作品（带分页）
  getAllPhotos: (params) => api.get("/photos/user", { params }),
};

// 分类相关接口
export const categoryApi = {
  getCategories: () => api.get("/categories"),
  getPhotosByCategory: (categoryId) =>
    api.get(`/categories/${categoryId}/photos`),
};

//管理员仪表盘接口
// ===================== 管理员仪表盘接口 =====================
export const adminApi = {
  //获取评论列表
  getCommentsList: (params) => api.get(`/admin/commentsList`, { params }), //用了

  //删除评论
  deleteComment: (id, workId) =>
    api.delete(`/admin/deleteComment/${id}/${workId}`),

  // 用户管理接口
  user: {
    // 获取用户列表（带分页和角色过滤）
    getUsers: (params) => api.get("/admin/selectPage", { params }), //用了

    // 添加新用户
    addUser: (data) => api.post("/admin/users", data),

    // 更新用户信息
    updateUser: (id, data) => api.put(`/admin/users/${id}`, data),

    // 删除用户
    deleteUser: (id) => api.delete(`/admin/users/${id}`),

    // 批量删除用户
    batchDeleteUsers: (ids) =>
      api.delete("/admin/users/batch", { data: { ids } }),

    // 按角色获取用户
    getUsersByRole: (role) => api.get("/admin/select", { params: { role } }),
  },

  // 内容管理接口
  content: {
    // 获取所有作品信息（管理员视图）
    getAllPhotos: (params) => api.get("/admin/allPhotos", { params }), //用了

    // 获取所有作品信息（管理员视图）（用于地图）
    getAllPhotosMap: (params) => api.get("/admin/photosMap", { params }), //用了

    // 获取待审核作品
    getPendingPhotos: (params) => api.get("/admin/photosPending", { params }), //用了

    // 审核作品
    reviewPhoto: (id, status) =>
      api.put(`/admin/${status === "true" ? "approve" : "reject"}/${id}`), //用了

    // 获取作品统计数据
    getPhotoStats: () => api.get("/admin/photos/stats"),
  },

  // 系统设置接口
  settings: {
    // 获取系统配置
    getConfig: () => api.get("/admin/settings"),

    // 更新系统配置
    updateConfig: (data) => api.put("/admin/settings", data),

    // 获取管理员日志
    getLogs: (params) => api.get("/admin/logs", { params }),

    // 获取仪表盘统计数据
    getDashboardStats: () => api.get("/admin/dashboard/stats"),
  },
};
export default api;
