#!/bin/sh
JSON_STRING='window.configs = { \
  "VUE_APP_API_DOMAIN":"'"${VUE_APP_API_DOMAIN}"'", \
}'
sed -i "s@// CONFIGURATIONS_PLACEHOLDER@${JSON_STRING}@" /usr/share/nginx/html/index.html
exec "$@"