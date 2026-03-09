# ---- Build Stage ----
FROM node:20-alpine AS build
WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm install --frozen-lockfile 2>/dev/null || npm install

COPY . .
RUN npm run build

# ---- Runtime Stage (Nuxt 3 SSR needs Node.js) ----
FROM node:20-alpine
WORKDIR /app

COPY --from=build /app/.output .output

EXPOSE 3000

ENV HOST=0.0.0.0
ENV PORT=3000
ENV NODE_ENV=production

CMD ["node", ".output/server/index.mjs"]
