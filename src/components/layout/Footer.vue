<script setup>
const currentYear = new Date().getFullYear();
</script>

<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>关于我们</h3>
          <p>
            我们致力于为摄影爱好者提供展示和交流的平台，让每一张照片都能讲述独特的故事。
          </p>
        </div>

        <div class="footer-section">
          <h3>快速链接</h3>
          <ul>
            <li><a href="#home">首页</a></li>
            <li><a href="#gallery">作品集</a></li>
            <li><a href="#series">特色系列</a></li>
            <li><a href="#about">关于我们</a></li>
          </ul>
        </div>

        <div class="footer-section">
          <h3>联系我们</h3>
          <ul class="contact-info">
            <li><i class="fas fa-envelope"></i> contact@photogallery.com</li>
            <li><i class="fas fa-phone"></i> +86 123 4567 8900</li>
            <li><i class="fas fa-map-marker-alt"></i> 北京市朝阳区艺术中心</li>
          </ul>
        </div>
      </div>

      <div class="footer-bottom">
        <p>&copy; {{ currentYear }} 摄影作品展示平台. 保留所有权利.</p>
        <div class="social-links">
          <a href="#" class="social-link"><i class="fab fa-weixin"></i></a>
          <a href="#" class="social-link"><i class="fab fa-weibo"></i></a>
          <a href="#" class="social-link"><i class="fab fa-qq"></i></a>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background-color: var(--bg-dark);
  color: var(--text-light);
  padding: 4rem 0 2rem;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.footer-section h3 {
  color: white;
  margin-bottom: 1.5rem;
  font-size: 1.25rem;
}

.footer-section p {
  line-height: 1.6;
  color: var(--text-secondary);
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 0.75rem;
}

.footer-section ul li a {
  color: var(--text-secondary);
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-section ul li a:hover {
  color: var(--primary-color);
}

.contact-info li {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-secondary);
}

.contact-info i {
  color: var(--primary-color);
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

.social-links {
  display: flex;
  gap: 1rem;
}

.social-link {
  color: var(--text-secondary);
  font-size: 1.25rem;
  transition: color 0.3s ease;
}

.social-link:hover {
  color: var(--primary-color);
}

@media (max-width: 768px) {
  .footer {
    padding: 2rem 0 1rem;
  }

  .footer-bottom {
    flex-direction: column;
    text-align: center;
  }
}
</style>
