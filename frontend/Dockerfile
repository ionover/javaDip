FROM nginx:1.25-alpine
LABEL MAINTAINER="ionov <>"

COPY dist /usr/share/nginx/html

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]