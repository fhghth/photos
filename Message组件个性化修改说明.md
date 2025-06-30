# Message 组件个性化修改说明

## 修改概述

将 Message.vue 组件从显示站点信息改为显示用户个人信息，使个人页面更加个性化和用户友好。

## 主要修改内容

### 1. 头像替换 Logo

- **原来**: 显示站点 logo (`VITE_SITE_MAIN_LOGO`)
- **现在**: 显示用户头像 (`currentUser.avatar`)
- **功能**:
  - 支持多种头像格式（base64、相对路径、完整 URL）
  - 头像加载失败时自动显示默认头像
  - 实时更新用户头像

### 2. 用户名替换站点名

- **原来**: 显示站点名称 (`VITE_SITE_URL`)
- **现在**: 显示用户名 (`currentUser.username`)
- **功能**:
  - 显示用户真实姓名
  - 未登录时显示"游客"

### 3. 用户角色显示

- **新增**: 在用户名后显示用户角色
- **角色映射**:
  - `role: "0"` → "管理员"
  - `role: "1"` → "摄影师"
  - 其他 → "用户"

## 技术实现

### 头像 URL 处理

```javascript
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
```

### 用户信息获取

```javascript
const currentUser = ref(
  JSON.parse(localStorage.getItem("code_user")) || {
    username: "游客",
    avatar: "",
    id: null,
    role: "user",
  }
);
```

### 实时更新机制

- 监听 localStorage 变化
- 自动更新用户信息
- 支持头像上传后的实时显示

## 显示效果

### 登录用户

```
[用户头像] 张三.摄影师
```

### 管理员

```
[用户头像] 管理员.管理员
```

### 未登录用户

```
[默认头像] 游客.用户
```

## 样式适配

- 保持了原有的响应式设计
- 头像尺寸：120px（桌面）/ 100px（移动端）
- 用户名字体：Pacifico-Regular
- 角色标签：较小字体，灰色显示

## 功能特点

✅ **个性化显示**: 每个用户看到自己的头像和姓名
✅ **实时更新**: 头像上传后立即显示
✅ **错误处理**: 头像加载失败时显示默认头像
✅ **角色识别**: 根据用户角色显示不同标签
✅ **响应式设计**: 在不同设备上都有良好显示效果
✅ **兼容性**: 支持未登录状态

## 使用场景

1. **个人主页**: 用户访问个人页面时看到自己的信息
2. **头像更新**: 用户上传新头像后立即在个人页面看到
3. **角色展示**: 管理员和普通用户看到不同的角色标识
4. **游客体验**: 未登录用户也能看到友好的界面

## 注意事项

1. 确保用户已登录才能显示真实信息
2. 头像上传功能需要配合之前的修复使用
3. 用户信息存储在 localStorage 中
4. 支持头像的多种格式和路径

这次修改让个人页面真正成为了用户的个性化空间！🎉
