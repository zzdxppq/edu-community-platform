// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  ssr: true,

  devtools: { enabled: true },

  css: ['~/assets/css/main.css'],

  app: {
    head: {
      charset: 'utf-8',
      viewport: 'width=device-width, initial-scale=1',
      title: '河南城乡学校共同体发展平台',
      meta: [
        { name: 'description', content: '河南省城乡学校共同体发展平台，推动城乡教育均衡发展，促进优质教育资源共享。' },
        { name: 'keywords', content: '城乡学校共同体,教育均衡,河南教育,资源共享,示范校' },
      ],
    },
  },

  runtimeConfig: {
    public: {
      adminUrl: process.env.NUXT_PUBLIC_ADMIN_URL || 'http://localhost:3001',
      apiBase: process.env.NUXT_PUBLIC_API_BASE || 'http://localhost:8080/api',
    },
  },

  devServer: {
    port: 3000,
  },

  compatibilityDate: '2024-04-03',
})
