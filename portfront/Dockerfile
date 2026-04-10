# 1. Build stage
FROM node:20-slim AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# 2. Production stage
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
# Nginx default config should work, but for SPA we might need a custom one if routing exists.
# Since it's a One Page Scroll site, default is fine.
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
