# 第一阶段：构建Vue应用
FROM node AS builder

WORKDIR /app

COPY package.json .
COPY package-lock.json .

# 安装依赖
RUN npm install

# 复制应用代码
COPY . .

# 构建Vue应用
RUN npm run build


# 第二阶段：运行Vue应用
FROM nginx:1.21.0-alpine

# 从第一阶段复制构建的Vue应用到Nginx的默认HTML目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 暴露Nginx服务的端口（默认为80）
EXPOSE 80

# 启动Nginx服务
CMD ["nginx", "-g", "daemon off;"]
