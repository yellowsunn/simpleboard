# build stage
FROM node:14.16-alpine as build-stage
WORKDIR /app
ADD package*.json ./
COPY . .
CMD yan install && yarn run serve

# production stage
FROM nginx:1.21-alpine as production-stage
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
