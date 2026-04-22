# frontend
FROM node:22-alpine AS frontend-build
WORKDIR /app

# cache the dependencies and configs
COPY frontend/package.json frontend/yarn.lock ./
RUN yarn install --frozen-lockfile

COPY frontend ./
RUN yarn build --configuration production

# nginx
FROM nginx:alpine
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=frontend-build /app/dist/frontend/browser/ /usr/share/nginx/html/