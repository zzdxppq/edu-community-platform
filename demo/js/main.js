/**
 * 河南城乡学校共同体发展平台 - 主交互脚本
 */

document.addEventListener('DOMContentLoaded', function() {
  // 初始化轮播图
  initCarousel();

  // 初始化登录弹窗
  initLoginModal();

  // 初始化导航高亮
  initNavHighlight();

  // 初始化搜索功能
  initSearch();
});

/**
 * 轮播图功能
 */
function initCarousel() {
  const carousel = document.querySelector('.carousel');
  if (!carousel) return;

  const inner = carousel.querySelector('.carousel-inner');
  const items = carousel.querySelectorAll('.carousel-item');
  const indicators = carousel.querySelectorAll('.carousel-indicator');
  const prevBtn = carousel.querySelector('.carousel-control.prev');
  const nextBtn = carousel.querySelector('.carousel-control.next');

  if (items.length === 0) return;

  let currentIndex = 0;
  let autoPlayInterval;

  function goToSlide(index) {
    if (index < 0) index = items.length - 1;
    if (index >= items.length) index = 0;

    currentIndex = index;
    inner.style.transform = `translateX(-${currentIndex * 100}%)`;

    // 更新指示器
    indicators.forEach((indicator, i) => {
      indicator.classList.toggle('active', i === currentIndex);
    });
  }

  function nextSlide() {
    goToSlide(currentIndex + 1);
  }

  function prevSlide() {
    goToSlide(currentIndex - 1);
  }

  function startAutoPlay() {
    autoPlayInterval = setInterval(nextSlide, 5000);
  }

  function stopAutoPlay() {
    clearInterval(autoPlayInterval);
  }

  // 绑定按钮事件
  if (prevBtn) prevBtn.addEventListener('click', prevSlide);
  if (nextBtn) nextBtn.addEventListener('click', nextSlide);

  // 绑定指示器事件
  indicators.forEach((indicator, index) => {
    indicator.addEventListener('click', () => goToSlide(index));
  });

  // 鼠标悬停暂停自动播放
  carousel.addEventListener('mouseenter', stopAutoPlay);
  carousel.addEventListener('mouseleave', startAutoPlay);

  // 开始自动播放
  startAutoPlay();
}

/**
 * 登录弹窗功能
 */
function initLoginModal() {
  const loginBtn = document.querySelector('.nav-login');
  const modalOverlay = document.querySelector('.modal-overlay');
  const modalClose = document.querySelector('.modal-close');

  if (!loginBtn || !modalOverlay) return;

  function openModal() {
    modalOverlay.classList.add('active');
    document.body.style.overflow = 'hidden';
  }

  function closeModal() {
    modalOverlay.classList.remove('active');
    document.body.style.overflow = '';
  }

  loginBtn.addEventListener('click', (e) => {
    e.preventDefault();
    openModal();
  });

  if (modalClose) {
    modalClose.addEventListener('click', closeModal);
  }

  modalOverlay.addEventListener('click', (e) => {
    if (e.target === modalOverlay) {
      closeModal();
    }
  });

  // ESC键关闭
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && modalOverlay.classList.contains('active')) {
      closeModal();
    }
  });
}

/**
 * 导航高亮
 */
function initNavHighlight() {
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll('.nav-link');

  navLinks.forEach(link => {
    const href = link.getAttribute('href');
    if (href && currentPath.includes(href.replace('.html', ''))) {
      link.classList.add('active');
    }
  });
}

/**
 * 搜索功能
 */
function initSearch() {
  const searchForm = document.querySelector('.nav-search');
  const searchInput = document.querySelector('.search-input');
  const searchBtn = document.querySelector('.search-btn');

  if (!searchInput || !searchBtn) return;

  function performSearch() {
    const query = searchInput.value.trim();
    if (query) {
      // 跳转到搜索结果页（demo中显示提示）
      alert('搜索功能演示：您搜索了 "' + query + '"');
    }
  }

  searchBtn.addEventListener('click', performSearch);
  searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
      performSearch();
    }
  });
}

/**
 * 分页功能
 */
function initPagination() {
  const pagination = document.querySelector('.pagination');
  if (!pagination) return;

  const pageItems = pagination.querySelectorAll('.page-item:not(.disabled)');

  pageItems.forEach(item => {
    item.addEventListener('click', function() {
      // 移除所有active
      pagination.querySelectorAll('.page-item').forEach(p => p.classList.remove('active'));
      // 添加当前active
      if (!this.classList.contains('prev') && !this.classList.contains('next')) {
        this.classList.add('active');
      }
    });
  });
}

/**
 * 侧边栏菜单
 */
function initSidebar() {
  const sidebarItems = document.querySelectorAll('.sidebar-item');

  sidebarItems.forEach(item => {
    item.addEventListener('click', function(e) {
      // 移除所有active
      sidebarItems.forEach(i => i.classList.remove('active'));
      // 添加当前active
      this.classList.add('active');
    });
  });
}

/**
 * 工具函数：格式化日期
 */
function formatDate(date) {
  const d = new Date(date);
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return { month, day };
}

/**
 * 工具函数：截断文本
 */
function truncateText(text, maxLength) {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
}
