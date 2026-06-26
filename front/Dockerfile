FROM node:24-alpine AS deps
WORKDIR /app
RUN corepack enable
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
RUN pnpm install --frozen-lockfile

FROM node:24-alpine AS builder
WORKDIR /app
RUN corepack enable
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN pnpm run build

FROM nginxinc/nginx-unprivileged:alpine
COPY --from=builder /app/out /usr/share/nginx/html
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]