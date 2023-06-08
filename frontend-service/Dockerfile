# build stage
FROM node:lts-alpine3.18 as build-stage
ARG ACTIVE_PROFILES
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run deploy:${ACTIVE_PROFILES}

# production stage
FROM nginx:stable-alpine3.17 as production-stage

COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY ["nginx.conf", "/etc/nginx/nginx.conf"]

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
